package com.vch.fragmets;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.activities.MerchantActivity;
import com.vch.activities.WebViewActivity;
import com.vch.adapters.GeoAutoCompleteAdapter;
import com.vch.bean.AddressData;
import com.vch.bean.Slot;
import com.vch.response.GetAddressData;
import com.vch.response.GetSlotsData;
import com.vch.response.PaymentResponse;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.AvenuesParams;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;

/**
 * A simple {@link Fragment} subclass.
 */
public class PaymentFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    private double longitude;
    private double latitude;
    private GoogleMap mMap;
    int countDrop1 = 0;
    int Adreese=0;
    private GoogleApiClient googleApiClient;
    AutoCompleteTextView autoCompleteTextView;
    View view;
    AlertDialog.Builder builderSingle;
    List<AddressData> addressData = new ArrayList<>();
    List<Slot> slotList = new ArrayList<>();
    String slotId = "", deliveryType = "Now", grandTotalStr, addressStr, zipStr, addressIDStr,subtotal;
    float shipping,grandTotalInt;
    TextView deliveryNowTV, deliveryLaterTV, selectTimeTV, addAddressTV, grandTotalTV,shippingChargeTV,subtotalTV,dialogTV;
    LinearLayout deliveryNowLL, deliveryLaterLL, addAddressLL;
    Integer ActivityRequestCode = 2;

    public PaymentFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_payment, container, false);
        if (getArguments() != null) {

            grandTotalStr = getArguments().getString("grandTotal");
            grandTotalInt = getArguments().getInt("grandTotal");
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

            mapFragment.getMapAsync(this);
            googleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();


            autoCompleteTextView = view.findViewById(R.id.et_location);

            autoCompleteTextView.setThreshold(1);
            autoCompleteTextView.addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {}

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void afterTextChanged(Editable s) {
                    if (s.length() > 0) {
                        loadDataDrop1(autoCompleteTextView.getText().toString());
                        //   getLatLongFromAddress(et_location.getText().toString(), "starting point");
                        //  str_first_add = et_location.getText().toString();

                    }
                }
            });
            selectTimeTV = view.findViewById(R.id.select_time_slot_TV);
            grandTotalTV = view.findViewById(R.id.grand_total_TV);
            deliveryNowTV = view.findViewById(R.id.deliver_now_TV);
            deliveryLaterTV = view.findViewById(R.id.deliver_later_TV);
            deliveryNowLL = view.findViewById(R.id.deliver_now_LL);
            deliveryLaterLL = view.findViewById(R.id.deliver_later_LL);
            addAddressTV = view.findViewById(R.id.add_address_TV);
            addAddressLL = view.findViewById(R.id.add_address_LL);
            shippingChargeTV = view.findViewById(R.id.shipping_charge);
            subtotalTV = view.findViewById(R.id.subtotalTV);
            dialogTV = view.findViewById(R.id.dialogTV);

           // shippingChargeTV.setText(shipping +"");
           subtotalTV.setText(subtotal+"");
            grandTotalTV.setText(String.format("Total:  %s ₹", grandTotalStr ));
            Log.d("grandtotalvalue",grandTotalStr);

            grandTotalInt = Float.parseFloat(grandTotalStr);

            if (grandTotalInt<400){
                Log.d("comparision value",grandTotalInt +"");

                shippingChargeTV.setVisibility(View.VISIBLE);
               dialogTV.setVisibility(View.VISIBLE);

        Double newgrand = Double.parseDouble (grandTotalStr);
                grandTotalInt = (float) (newgrand + 40);

                grandTotalStr = grandTotalInt + "";
            }

            else{

                shippingChargeTV.setVisibility(View.GONE);
                dialogTV.setVisibility(View.GONE);
            }

            subtotalTV.setText(String.format("Grand Total:  %s ₹", grandTotalStr ));



            if (deliveryType.equals("Now")) {
                deliveryNowLL.setVisibility(View.GONE);
                slotId = "45 minutes";
            }
            builderSingle = new AlertDialog.Builder(getActivity());
            selectTimeTV.setOnClickListener(this);
            deliveryNowTV.setOnClickListener(this);
            deliveryLaterTV.setOnClickListener(this);
            addAddressTV.setOnClickListener(this);
            view.findViewById(R.id.submit_BT).setOnClickListener(this);
            view.findViewById(R.id.add_address_BT).setOnClickListener(this);
            getAddressCall();
        }
        return view;
    }

    private void loadDataDrop1(String s) {

        try {
            if (countDrop1 == 0) {
                List<String> l1 = new ArrayList<>();
                if (s != null) {
                    l1.add(s);
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(getActivity(), l1, "" + latitude, "" + longitude);
                    autoCompleteTextView.setAdapter(ga);
                }

            }
            countDrop1++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onMapReady(GoogleMap googleMap)
    {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng latLng = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        mMap.setOnMarkerDragListener(this);
        //Adding a long click listener to the map
        mMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        getCurrentLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.clear();
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        //Adding a new marker to the current pressed position we are also making the draggable true
        mMap.addMarker(new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)).draggable(true));
    }

    @Override
    public void onMarkerDragStart(Marker marker) {
    }

    @Override
    public void onMarkerDrag(Marker marker) {
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        latitude = marker.getPosition().latitude;
        longitude = marker.getPosition().longitude;
        //Moving the map
        moveMap();
    }

    private void getCurrentLocation() {
        //Creating a location object

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        Location location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        if (location != null) {
            //Getting longitude and latitude
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            App.editor.putString("LAT", String.valueOf(latitude));
            App.editor.putString("LON", String.valueOf(longitude));
            App.editor.apply();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                System.out.println("Address" + addresses);
                //   String address22 = addresses.get(0).getAddressLine(1);
                //  String address1=addresses.get(0).getLocality();
                String address22 = addresses.get(0).getAddressLine(0);
                //String Addressvalue = address22;
                if (address22 != null) {
                    autoCompleteTextView.setText(address22);
                }
                System.out.println("");
            } catch (IOException e) {
                e.printStackTrace();
            }

            //moving the map to location
            moveMap();
        }


    }

    //Function to move the map
    private void moveMap() {
        //String to display current latitude and longitude
        String msg = latitude + ", " + longitude;
        //Creating a LatLng Object to store Coordinates
        LatLng latLng = new LatLng(latitude, longitude);
        //Adding marker to map
        BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.location);
        Bitmap b = bitmapdraw.getBitmap();
        Bitmap smallMarker = Bitmap.createScaledBitmap(b, 50, 50, false);
        mMap.addMarker(new MarkerOptions().position(latLng) //setting position
                .draggable(true).icon(BitmapDescriptorFactory.fromBitmap(smallMarker)) //Making the marker draggable
                .title("Current Location")); //Adding a title

        //Moving the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        //Animating the camera
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        //Displaying current coordinates in toast
        //Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void deliverySlotCall() {

        // String userId = App.pref.getString(Constant.USER_ID,"");
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().deliverySlotCall();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Delivery_slots_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {

                            GetSlotsData addressResponse = new Gson().fromJson(responseData, GetSlotsData.class);
                            slotList = addressResponse.getSlots();

                            //AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
                            builderSingle.setTitle("Select Time Slot");

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.select_dialog_singlechoice);
                            for (Slot slot : slotList) {
                                arrayAdapter.add(slot.getShow());
                            }

                            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    for (Slot slot : slotList) {
                                        if (strName.equals(slot.getShow())) {
                                            slotId = slot.getSlot();
                                            selectTimeTV.setText(slot.getShow());
                                        }
                                    }
                                }
                            });
                            builderSingle.show();
                        } else {

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Something went wrong.");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });

    }

    private void getAddressCall() {
        String userId = App.pref.getString(Constant.USER_ID, "");
        Call<ResponseBody> call = AppConfig.getLoadInterface().setAddressCall(AppConfig.setRequestBody(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Get_ADDRESS_Response", responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1"))
                        {
                            Adreese=0;
                            deliveryNowTV.setText("Place Your Order");
                            GetAddressData addressResponse = new Gson().fromJson(responseData, GetAddressData.class);
                            addressData = addressResponse.getMsg();
                            setAddress();
                        } else
                        {
                            Adreese=1;
                            deliveryNowTV.setText("Add Personal details ");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Something went wrong.");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Checkout");
        HomeActivity.enableViews(true);
    }

    private void setAddress() {
        try {
            RadioGroup ll = new RadioGroup(getActivity());
            ll.setOrientation(LinearLayout.VERTICAL);

            for (int i = 0; i < addressData.size(); i++) {
                RadioButton rdbtn = new RadioButton(getActivity());
                rdbtn.setId((addressData.size() * 2) + i);

                if (i == 0) {
                    rdbtn.setChecked(true);
                    addressIDStr = addressData.get(i).getAddressId();
                }
                addressStr = String.format("%s, \n%s, %s, ", addressData.get(i).getCustomerName(), addressData.get(i).getHouseNumber(), addressData.get(i).getAddress());
                rdbtn.setText(addressStr);
                ll.addView(rdbtn);
            }
            ((ViewGroup) view.findViewById(R.id.radiogroup)).removeAllViews();
            ((ViewGroup) view.findViewById(R.id.radiogroup)).addView(ll);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        final String userId = App.pref.getString(Constant.USER_ID, "");
        switch (v.getId()) {

            case R.id.deliver_now_TV:

                if(Adreese==1)
                {
                    Toast.makeText(getContext(), "Please Add Delivery Address", Toast.LENGTH_SHORT).show();
                }else
                {
                    double total = 0;
                    try {
                        total = Double.parseDouble(grandTotalStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                        total = 0;
                    }

                    if (total < 2500)
                    {

                        placeOrder("Paytm");
                       /* final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getLayoutInflater();
                        final View view = inflater.inflate(R.layout.dailog_payment_option, null);
                        final RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                                String paymentType;
                                if (radioGroup.getCheckedRadioButtonId() == R.id.online_payment_RB)
                                {
                                    paymentType = "COD";
                                } else {
                                    paymentType = "Paytm";
                                }

                                placeOrder(paymentType);

                            }
                        });

                        alertDialog.setView(view);
                        alertDialog.create();
                        alertDialog.show();*/
                    } else {
                        placeOrder("");
                    }

                }
                /*if (deliveryNowLL.getVisibility() == View.GONE)
                {
                    deliveryType = "Now";
                    slotId = "45 minutes";
                    deliveryNowLL.setVisibility(View.VISIBLE);
                    deliveryLaterLL.setVisibility(View.GONE);
                    deliveryNowTV.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                    deliveryNowTV.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));
                    deliveryLaterTV.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
                    deliveryLaterTV.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
                }*/
                break;
            case R.id.deliver_later_TV:
                if (deliveryLaterLL.getVisibility() == View.GONE) {
                    deliveryType = "Later";
                    selectTimeTV.setText("Select Time Slot");
                    deliveryLaterLL.setVisibility(View.VISIBLE);
                    deliveryNowLL.setVisibility(View.GONE);
                    deliveryLaterTV.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimary));
                    deliveryLaterTV.setTextColor(getActivity().getResources().getColor(R.color.colorWhite));

                    deliveryNowTV.setBackgroundColor(getActivity().getResources().getColor(R.color.colorWhite));
                    deliveryNowTV.setTextColor(getActivity().getResources().getColor(R.color.colorBlack));
                }
                break;
            case R.id.select_time_slot_TV:
                if (slotList.size() > 0) {
                    builderSingle.show();
                } else deliverySlotCall();

                break;
            case R.id.add_address_BT:

                RadioGroup labelGroup = view.findViewById(R.id.label_RG);
                EditText customerNameET = view.findViewById(R.id.name_ET);
                EditText mobileET = view.findViewById(R.id.mobile_ET);
                EditText houseNumberET = view.findViewById(R.id.house_number_ET);
                EditText landmarkET = view.findViewById(R.id.landmark_ET);
                EditText zipET = view.findViewById(R.id.zip_ET);

                int selectedId = labelGroup.getCheckedRadioButtonId();
                RadioButton labelButton = view.findViewById(selectedId);

                String lableStr = labelButton.getText().toString();
                String customerNameStr = customerNameET.getText().toString();
                String mobileStr = mobileET.getText().toString();
                String addressSr = autoCompleteTextView.getText().toString();
                String houseNumberStr = houseNumberET.getText().toString();
                String landmarkStr = landmarkET.getText().toString();
                String zipStr = zipET.getText().toString();

                LatLng latLng = AppConfig.getLocationFromAddress(getContext(),addressSr);
                if (latLng!=null){
                    latitude = latLng.latitude;
                    longitude = latLng.longitude;
                }
                if (AppConfig.distance(Constant.Lat, Constant.Lon, latitude, longitude) < 7)

                    if (customerNameStr.length() > 0) {
                        if (mobileStr.length() > 0) {
                            if (houseNumberET.length() > 0) {
                                if (landmarkStr.length() > 0) {
                                    if (zipStr.length() > 0) {

                                        addAddressCall(lableStr, customerNameStr, mobileStr, addressSr, houseNumberStr, landmarkStr, zipStr, userId);
                                    } else
                                        AppConfig.showToast("postal/zip code required.");
                                } else
                                    AppConfig.showToast("landmark/street required.");
                            } else
                                AppConfig.showToast("House no. required.");
                        } else
                            AppConfig.showToast("Mobile required.");
                    } else
                        AppConfig.showToast("Name required.");
                else
                    AppConfig.showToast("we are enable to provide delivery on your address.");
                //addAddressCall(lableStr, customerNameStr, mobileStr, addressSr, houseNumberStr, landmarkStr, zipStr, userId);

                break;
            case R.id.add_address_TV:

                if (addAddressLL.getVisibility() == View.GONE) {
                    addAddressLL.setVisibility(View.VISIBLE);
                    EditText customerName = view.findViewById(R.id.name_ET);
                    EditText mobile = view.findViewById(R.id.mobile_ET);
                    customerName.setText(HomeActivity.detail.getUserName());
                    mobile.setText(HomeActivity.detail.getUserPhone());
                } else {
                    addAddressLL.setVisibility(View.GONE);
                }

                break;
            case R.id.submit_BT:
                double total = 0;
                try {
                    total = Double.parseDouble(grandTotalStr);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    total = 0;
                }

               if (total < 2500)
               {
                    final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                    LayoutInflater inflater = getLayoutInflater();
                    final View view = inflater.inflate(R.layout.dailog_payment_option, null);
                    final RadioGroup radioGroup = view.findViewById(R.id.radiogroup);
                    alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    alertDialog.setPositiveButton("Place Order", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            String paymentType;
                            if (radioGroup.getCheckedRadioButtonId() == R.id.online_payment_RB) {
                                paymentType = "COD";
                            } else {
                                paymentType = "Paytm";
                            }

                            placeOrder(paymentType);

                        }
                    });

                    alertDialog.setView(view);
                    alertDialog.create();
                    alertDialog.show();
                } else {
                    placeOrder("");
                }
                break;
        }
    }

    private void placeOrder(String payment)
    {

        final String userId = App.pref.getString(Constant.USER_ID, "");
        for (AddressData data : addressData) {
            String address = String.format("%s, \n%s, %s, ", data.getCustomerName(), data.getHouseNumber(), data.getAddress());
            if (address.equals(addressStr)) {
                addressIDStr = data.getAddressId();
            }
        }
        if (getArguments() != null) {
            String redeemedPoint = getArguments().getString("redeemedPoint");
            String coupon_code = getArguments().getString("coupon_code");
            String coupon_discount = getArguments().getString("coupon_discount");
            String cashback = getArguments().getString("cashback");
            String spicy = getArguments().getString("spicy");
            String comment = getArguments().getString("comment");
            String addressID = addressIDStr;
            String sub_total = getArguments().getString("sub_total");
            String deliveryTypeStr = deliveryType;

            if (addressID != null) {
                placeOrderCall(userId, addressID, slotId, deliveryTypeStr, redeemedPoint, coupon_discount, coupon_code, cashback, payment, sub_total,spicy,comment);

            } else {
                AppConfig.showToast("select Address for delivery");
            }

        }
    }

    public static Fragment newInstance(String grandTotal, String redeemedPointStr, String couponCodeStr, String couponDiscountStr, String cashBackStr, String subTotalStr, String spicyStr, String commentStr) {
        PaymentFragment fragment = new PaymentFragment();
        Bundle arg = new Bundle();
        arg.putString("grandTotal", grandTotal);
        arg.putString("redeemedPoint", redeemedPointStr);
        arg.putString("coupon_code", couponCodeStr);
        arg.putString("coupon_discount", couponDiscountStr);
        arg.putString("cashback", cashBackStr);
        arg.putString("sub_total", subTotalStr);
        arg.putString("spicy", spicyStr);
        arg.putString("comment", commentStr);
        fragment.setArguments(arg);
        return fragment;
    }

    private void addAddressCall(String lableStr, String customerNameStr, String mobileStr, String addressStr, String houseNumberStr, String landmarkStr, String zipStr, String userId) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().addAddressCall(AppConfig.setRequestBody(lableStr), AppConfig.setRequestBody(customerNameStr), AppConfig.setRequestBody(houseNumberStr), AppConfig.setRequestBody(addressStr), AppConfig.setRequestBody(mobileStr), AppConfig.setRequestBody(landmarkStr), AppConfig.setRequestBody(zipStr), AppConfig.setRequestBody(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("ADD_ADDRESS_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            AppConfig.showToast(object.getString("msg"));
                            getAddressCall();
                            addAddressLL.setVisibility(View.GONE);
                            EditText customerNameET = view.findViewById(R.id.name_ET);
                            EditText mobileET = view.findViewById(R.id.mobile_ET);
                            EditText houseNumberET = view.findViewById(R.id.house_number_ET);
                            EditText landmarkET = view.findViewById(R.id.landmark_ET);
                            EditText zipET = view.findViewById(R.id.zip_ET);
                            customerNameET.setText("");
                            mobileET.setText("");
                            houseNumberET.setText("");
                            landmarkET.setText("");
                            zipET.setText("");
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Something went wrong.");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });

    }

    private void placeOrderCall(String user_id, String address_id,
                                String delivery_time, String delType,
                                String redeam_point, String coupon_discount,
                                String coupon_code, String cashback,
                                final String payment_method, final String sub_total,
                                String spicy, String comment) {

        Log.e("user_id", user_id);
        Log.e("address_id", address_id);
        Log.e("delivery_time", delivery_time);
        Log.e("delType", delType);
        Log.e("redeemed_point", redeam_point);
        Log.e("coupon_discount", coupon_discount);
        Log.e("coupon_code", coupon_code);
        Log.e("cashBack", cashback);
        Log.e("payment_method", payment_method);
        Log.e("sub_total", sub_total);
        Log.e("spicy", spicy);
        Log.e("comment", comment);

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterfacepayment().placeOrderCall(AppConfig.setRequestBody(user_id),
                AppConfig.setRequestBody(address_id),
                AppConfig.setRequestBody(delivery_time),
                AppConfig.setRequestBody(delType),
                AppConfig.setRequestBody(redeam_point),
                AppConfig.setRequestBody(coupon_discount),
                AppConfig.setRequestBody(coupon_code),
                AppConfig.setRequestBody(cashback),
                AppConfig.setRequestBody(payment_method),
                AppConfig.setRequestBody(sub_total),
                AppConfig.setRequestBody(spicy),
                AppConfig.setRequestBody(comment));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("PLACE_ORDER_CALL", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1"))
                        {
                            JSONObject order_details=object.getJSONObject("order_details");
                            String order_id=order_details.getString("order_id");
                            String billing_tel=order_details.getString("billing_tel");
                            String billing_email=order_details.getString("billing_email");
                            GenerateCheckSum(/*grandTotalStr*/"1",order_id,billing_tel,billing_email);
                            /* Intent intent = new Intent(getActivity(), MerchantActivity.class);
                            intent.putExtra(AvenuesParams.ORDER_ID, order_id);
                            intent.putExtra(AvenuesParams.AMOUNT,grandTotalStr);
                            intent.putExtra(AvenuesParams.billing_tel, billing_tel);
                            intent.putExtra(AvenuesParams.billing_email, billing_email);
                            startActivity(intent);
                           */
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    AppConfig.showToast("Something went wrong.");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });

       /* if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS}, 101);
        }*/
    }

    //for send datat
    public void GenerateCheckSum(final String amount,final String orderid,final String billingntel,final String billingemail)
    {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage("Please wait...........");
        dialog.show();
        String regId = App.RegPref.getString("regId","");
        Log.e("PLACE_checksum_param", "regisid "+regId +"  order  id"+AppConfig.setRequestBody(orderid)+" "+AppConfig.setRequestBody(billingemail)+" "+
                AppConfig.setRequestBody(amount)+" "+
                AppConfig.setRequestBody(billingntel)+" "+
                AppConfig.setRequestBody(App.pref.getString(Constant.USER_ID,"")));

        Call<ResponseBody> call = AppConfig.getLoadInterfacepayment().generateChecksum(
                AppConfig.setRequestBody(orderid),
                AppConfig.setRequestBody(billingemail),
                AppConfig.setRequestBody(amount),
                AppConfig.setRequestBody(billingntel),
                AppConfig.setRequestBody(App.pref.getString(Constant.USER_ID,"")));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("PLACE_checksum", response.toString());
                if (response.isSuccessful())
                {
                    try {
                        dialog.dismiss();
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        JSONObject bodyobj=object.getJSONObject("body");
                        String checksumtoken_pay=bodyobj.getString("txnToken");
                        String midString="VIJAYC26242490811855";
                        String txnTokenString=checksumtoken_pay;
                        String txnAmountString=amount;
                        callPaytmMethods(txnTokenString,txnAmountString,midString,orderid);
                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }else
                {
                    dialog.dismiss();
                    AppConfig.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
            }
        });

        //...........
    }

    public void callPaytmMethods(String txnTokenString,String txnAmountString,String midString,String orderid_pay)
    {
        String userid=App.pref.getString(Constant.USER_ID,"");
        String callBackUrl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderid_pay;
        String datdem="paytm data"+orderid_pay+" midid "+midString+" txttoken "+txnTokenString+" txtamount "+txnAmountString+" callback "+callBackUrl;
        Log.d("paytm data",orderid_pay+" midid "+midString+" txttoken "+txnTokenString+" txtamount "+txnAmountString+" callback "+callBackUrl);
        PaytmOrder paytmOrder = new PaytmOrder(orderid_pay, midString, txnTokenString, txnAmountString, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback()
        {
            @Override
            public void onTransactionResponse(Bundle bundle)
            {
                Toast.makeText(getContext(), "Response (onTransactionResponse) : "+bundle.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {
                Toast.makeText(getContext(), "error process "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Toast.makeText(getContext(), "s authentication "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Toast.makeText(getContext(), "some error "+s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Toast.makeText(getContext(), "s srint "+s+" s1 "+s1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Toast.makeText(getContext(), "on transaction "+s, Toast.LENGTH_SHORT).show();
            }
        });

        String host = "https://securegw.paytm.in/";
        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(getActivity(), ActivityRequestCode);
    }

}
