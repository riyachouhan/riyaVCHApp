package com.vch.fragmets;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.AddressRVAdapter;
import com.vch.adapters.GeoAutoCompleteAdapter;
import com.vch.bean.AddressData;
import com.vch.response.GetAddressData;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAddressFragment extends Fragment implements View.OnClickListener, OnMapReadyCallback, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMapLongClickListener {
    View view;
    private static AddressRVAdapter adapter;
    private static List<AddressData> addressData = new ArrayList<>();
    int countDrop1 = 0;
    TextView addAddressTV;
    LinearLayout addAddressLL;

    private GoogleMap mMap;

    private double longitude;
    private double latitude;

    private GoogleApiClient googleApiClient;
    AutoCompleteTextView autoCompleteTextView;

    public MyAddressFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_my_address, container, false);
        addAddressLL = view.findViewById(R.id.add_address_LL);
        addAddressTV = view.findViewById(R.id.add_address_TV);


        view.findViewById(R.id.add_address_BT).setOnClickListener(this);
        addAddressTV.setOnClickListener(this);


        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.mapView);

        mapFragment.getMapAsync(this);
        googleApiClient = new GoogleApiClient.Builder(getContext()).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(LocationServices.API).build();


        autoCompleteTextView = view.findViewById(R.id.et_location);

        autoCompleteTextView.setThreshold(1);
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    loadDataDrop1(autoCompleteTextView.getText().toString());
                    //   getLatLongFromAddress(et_location.getText().toString(), "starting point");
                    //  str_first_add = et_location.getText().toString();

                }
            }
        });

        adapter = new AddressRVAdapter(getActivity(), addressData, latitude, longitude);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(adapter);

        getAddressCall(getActivity());

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Address");
        HomeActivity.enableViews(true);
    }


    public static void getAddressCall(Activity activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
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

                        if (object.getString("status").equals("1")) {
                            GetAddressData addressResponse = new Gson().fromJson(responseData, GetAddressData.class);
                            addressData = addressResponse.getMsg();
                            adapter.updateList(addressData);
                        } else {
                            addressData = new ArrayList<>();
                            adapter.updateList(addressData);
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

    @Override
    public void onClick(View v) {
        String userId = App.pref.getString(Constant.USER_ID, "");
        switch (v.getId()) {
            case R.id.add_address_TV:

                if (addAddressLL.getVisibility() == View.GONE) {
                    addAddressLL.setVisibility(View.VISIBLE);

                    EditText customerNameET = view.findViewById(R.id.name_ET);
                    EditText mobileET = view.findViewById(R.id.mobile_ET);
                    customerNameET.setText(HomeActivity.detail.getUserName());
                    mobileET.setText(HomeActivity.detail.getUserPhone());

                } else {
                    addAddressLL.setVisibility(View.GONE);
                }

                break;
            case R.id.add_address_BT:

                RadioGroup labelGroup = view.findViewById(R.id.label_RG);
                EditText customerNameET = view.findViewById(R.id.name_ET);
                EditText mobileET = view.findViewById(R.id.mobile_ET);
                EditText houseNumberET = view.findViewById(R.id.house_number_ET);
                EditText landmarkET = view.findViewById(R.id.landmark_ET);
                EditText zipET = view.findViewById(R.id.zip_ET);
                Button button = view.findViewById(R.id.add_address_BT);
                button.setEnabled(false);

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
                Log.e("lat",latitude+"");
                Log.e("lon",longitude+"");
                if (AppConfig.distance(Constant.Lat, Constant.Lon, latitude, longitude) < 7) {
                    if (customerNameStr.length() > 0) {
                        if (mobileStr.length() > 0 && mobileStr.length()==10) {
                            if (houseNumberET.length() > 0) {
                                if (landmarkStr.length() > 0) {
                                    if (zipStr.length() > 0) {

                                        addAddressCall(lableStr, customerNameStr, mobileStr, addressSr, houseNumberStr, landmarkStr, zipStr, userId);
                                    } else
                                        AppConfig.showToast("Please enter Pin code.");
                                } else
                                    AppConfig.showToast("Please enter Landmark.");
                            } else
                                AppConfig.showToast("Please enter Door, Floor and Building number.");
                        } else
                            AppConfig.showToast("Please enter valid Mobile number.");
                    } else
                        AppConfig.showToast("Please enter Name.");
                } else
                    AppConfig.showToast("We are enable to provide delivery on your address.");

                break;
        }
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
    public void onMapReady(GoogleMap googleMap) {
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
            longitude = location.getLongitude();
            latitude = location.getLatitude();

            App.editor.putString("LAT", String.valueOf(latitude));
            App.editor.putString("LON", String.valueOf(longitude));
            App.editor.apply();
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                System.out.println("Address" + addresses);
                String address22 = addresses.get(0).getAddressLine(0);
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
        try {
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
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
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
                            getAddressCall(getActivity());
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
                Button button = view.findViewById(R.id.add_address_BT);
                button.setEnabled(false);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
                Button button = view.findViewById(R.id.add_address_BT);
                button.setEnabled(false);
            }
        });
    }


}
