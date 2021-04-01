package com.vch.adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.vch.R;
import com.vch.bean.AddressData;
import com.vch.fragmets.MyAddressFragment;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRVAdapter extends RecyclerView.Adapter<AddressRVAdapter.MyViewHolder> {

    private Activity activity;
    private List<AddressData> results;
    double latitude, logitude;
    int countDrop1 = 0;

    public AddressRVAdapter(Activity mContext, List<AddressData> results, double latitude, double logitude) {
        this.activity = mContext;
        this.results = results;
        this.latitude = latitude;
        this.logitude = logitude;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_address, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.nameTV.setText(results.get(position).getCustomerName());
        holder.houseTV.setText(results.get(position).getHouseNumber());
        holder.addressTV.setText(results.get(position).getAddress());
        holder.mobileTV.setText(results.get(position).getMobileNo());
        holder.landmarkTV.setText(results.get(position).getLandmark());
        holder.pincodeTV.setText(results.get(position).getPincode());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<AddressData> addressData) {
        results = addressData;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV, houseTV, addressTV, mobileTV, landmarkTV, pincodeTV;
        ImageView updateIV, deleteIV;

        public MyViewHolder(final View view) {
            super(view);

            nameTV = view.findViewById(R.id.name_TV);
            houseTV = view.findViewById(R.id.house_number_TV);
            addressTV = view.findViewById(R.id.address_TV);
            mobileTV = view.findViewById(R.id.mobile_TV);
            landmarkTV = view.findViewById(R.id.landmark_TV);
            pincodeTV = view.findViewById(R.id.pin_code_TV);
            updateIV = view.findViewById(R.id.update_IV);
            deleteIV = view.findViewById(R.id.delete_IV);

            updateIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    LayoutInflater inflater = activity.getLayoutInflater();
                    countDrop1 = 0;

                    final View alertLayout = inflater.inflate(R.layout.dialog_update_address, null);
                    final EditText nameET = alertLayout.findViewById(R.id.name_ET);
                    final EditText mobileET = alertLayout.findViewById(R.id.mobile_ET);
                    final EditText houseET = alertLayout.findViewById(R.id.house_number_ET);
                    final EditText landmarkET = alertLayout.findViewById(R.id.landmark_ET);
                    final EditText zipET = alertLayout.findViewById(R.id.zip_ET);
                    final AutoCompleteTextView autoCompleteTextView = alertLayout.findViewById(R.id.et_location);

                    final RadioGroup labelGroup = alertLayout.findViewById(R.id.label_RG);
                    switch (results.get(getAdapterPosition()).getLabel()) {
                        case "Home":
                            ((RadioButton)alertLayout.findViewById(R.id.home_RB)).setChecked(true);
                            break;
                        case "Office":
                            ((RadioButton)alertLayout.findViewById(R.id.office_RB)).setChecked(true);
                            break;
                        case "Other":
                            ((RadioButton)alertLayout.findViewById(R.id.other_RB)).setChecked(true);
                            break;
                    }

                    nameET.setText(results.get(getAdapterPosition()).getCustomerName());
                    mobileET.setText(results.get(getAdapterPosition()).getMobileNo());
                    houseET.setText(results.get(getAdapterPosition()).getHouseNumber());
                    landmarkET.setText(results.get(getAdapterPosition()).getLandmark());
                    zipET.setText(results.get(getAdapterPosition()).getPincode());
                    autoCompleteTextView.setText(results.get(getAdapterPosition()).getAddress());

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
                                loadDataDrop1(autoCompleteTextView.getText().toString(), autoCompleteTextView);
                            }
                        }
                    });

                    AlertDialog.Builder alert = new AlertDialog.Builder(activity);
                    alert.setTitle("Update Address");
                    // this is set the view from XML inside AlertDialog
                    alert.setView(alertLayout);
                    // disallow cancel of AlertDialog on click of back button and outside touch
                    alert.setCancelable(true);
                    alert.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            int selectedId = labelGroup.getCheckedRadioButtonId();
                            RadioButton labelButton = alertLayout.findViewById(selectedId);

                            String lableStr = labelButton.getText().toString();
                            String customerNameStr = nameET.getText().toString();
                            String mobileStr = mobileET.getText().toString();
                            String addressSr = autoCompleteTextView.getText().toString();
                            String houseNumberStr = houseET.getText().toString();
                            String landmarkStr = landmarkET.getText().toString();
                            String zipStr = zipET.getText().toString();
                            String userId = results.get(getAdapterPosition()).getUserId();
                            String addressId = results.get(getAdapterPosition()).getAddressId();
                            LatLng latLng = AppConfig.getLocationFromAddress(activity, addressSr);
                            if (latLng != null) {
                                latitude = latLng.latitude;
                                logitude = latLng.longitude;
                            }
                            if (AppConfig.distance(Constant.Lat, Constant.Lon, latitude, logitude) < 7)
                                if (customerNameStr.length() > 0) {
                                    if (mobileStr.length() > 0 && mobileStr.length() == 10) {
                                        if (houseNumberStr.length() > 0) {
                                            if (landmarkStr.length() > 0) {
                                                if (zipStr.length() > 0) {

                                                    addAddressCall(lableStr, customerNameStr, mobileStr, addressSr, houseNumberStr, landmarkStr, zipStr, userId, addressId);
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

                            else {
                                AppConfig.showToast("we are enable to provide delivery on your address.");
                            }
                        }
                    });
                    alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    });
                    AlertDialog dialog = alert.create();
                    dialog.show();
                }
            });

            deleteIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);
                    alertDialogBuilder.setTitle("Delete Address").
                            setMessage("Delete Address will permanently remove your address, Are you Sure want to Delete?")
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    deleteAddressCall(results.get(getAdapterPosition()).getAddressId());
                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).show();
                }
            });
        }
    }


    private void addAddressCall(String lableStr,
                                String customerNameStr,
                                String mobileStr,
                                String addressStr,
                                String houseNumberStr,
                                String landmarkStr,
                                String zipStr,
                                String userId,
                                String addressId) {

        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().updateAddressCall(
                AppConfig.setRequestBody(lableStr),
                AppConfig.setRequestBody(customerNameStr),
                AppConfig.setRequestBody(houseNumberStr),
                AppConfig.setRequestBody(addressStr),
                AppConfig.setRequestBody(mobileStr),
                AppConfig.setRequestBody(landmarkStr),
                AppConfig.setRequestBody(zipStr),
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(addressId)
        );

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
                            MyAddressFragment.getAddressCall(activity);
                            //HomeActivity.addFragment(new MyAddressFragment(),false);
                            // addAddressLL.setVisibility(View.GONE);

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


    private void loadDataDrop1(String s, AutoCompleteTextView autoCompleteTextView) {

        try {
            if (countDrop1 == 0) {
                List<String> l1 = new ArrayList<>();
                if (s != null) {
                    l1.add(s);
                    GeoAutoCompleteAdapter ga = new GeoAutoCompleteAdapter(activity, l1, "" + latitude, "" + logitude);
                    autoCompleteTextView.setAdapter(ga);
                }

            }
            countDrop1++;

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteAddressCall(String addressId) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().deleteAddressCall(
                AppConfig.setRequestBody(App.pref.getString(Constant.USER_ID, "")),
                AppConfig.setRequestBody(addressId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            AppConfig.showToast("Address has been deleted successfully.");
                            MyAddressFragment.getAddressCall(activity);

                            // HomeActivity.addFragment(new MyAddressFragment(),false);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
}
