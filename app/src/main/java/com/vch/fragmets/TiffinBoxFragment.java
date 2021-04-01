package com.vch.fragmets;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vch.PolicyFragment;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.activities.WebViewActivity;
import com.vch.bean.AddressData;
import com.vch.bean.MealDatum;
import com.vch.response.GetAddressData;
import com.vch.response.MealPlanData;
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

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TiffinBoxFragment extends Fragment {
    AlertDialog.Builder mealPlanBuilder, daysBuilder;
    TextView mealPlanTV, nDaysTV, amountTV, typeTV;
    String mealPlanId, mealPlanQty, nDays, type, amount;
    Button saveBT;
    List<String> typeList = new ArrayList<>();
    CheckBox policyCB;
    TextView policyTV;
    RadioButton codRB,onlineRB;
    RadioGroup paymentRG;
    List<AddressData> addressData = new ArrayList<>();
    private String addressStr;
    View view1;
    private String addressId = "";
    private boolean isMultiple = true;

    public TiffinBoxFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view1 = inflater.inflate(R.layout.fragment_tiffin_box, container, false);
        if (getActivity() != null) {
            mealPlanBuilder = new AlertDialog.Builder(getActivity());
            daysBuilder = new AlertDialog.Builder(getActivity());
            mealPlanTV = view1.findViewById(R.id.select_meal_plan_TV);
            nDaysTV = view1.findViewById(R.id.select_no_days_TV);
            typeTV = view1.findViewById(R.id.type_TV);
            amountTV = view1.findViewById(R.id.amount_TV);
            policyCB = view1.findViewById(R.id.policy_CB);
            policyTV = view1.findViewById(R.id.policy_Tv);
            saveBT = view1.findViewById(R.id.submit_BT);
            paymentRG = view1.findViewById(R.id.payment_RG);
            codRB = view1.findViewById(R.id.c_o_d_RB);
            onlineRB = view1.findViewById(R.id.online_payment_RB);


            typeList.add("Lunch");
            typeList.add("Dinner");
            typeList.add("Both");
            getAddressCall();
            mealPlanTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getMealPlanCall();
                }
            });

            policyTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    HomeActivity.addFragment(new PolicyFragment(),true);
                }
            });
            nDaysTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String str[] = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};
                    final String strId[] = {"1", "2", "3", "4", "5", "6", "7"};



                    if (isMultiple) {
                        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
                        LayoutInflater inflater = getLayoutInflater();

                        View view = inflater.inflate(R.layout.dialog_selected_days, null);
                        ListView lv = (ListView) view.findViewById(R.id.listView1);
                        ArrayAdapter<String> adp = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_multichoice, str);
                        lv.setAdapter(adp);
                        final boolean[] checkedItems = new boolean[str.length];
                        alertDialog.setMultiChoiceItems(str, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                // TODO Auto-generated method stub

                                int count = 0;
                                for (int i = 0; i < checkedItems.length; i++) {
                                    // loop through the checkedItems array, if checkedItems
                                    // increment count*/

                                    if (checkedItems[i]) {

                                        count++;

                                    }
                                    if (count > 7) {
                                        // if the number of checked items become
                                        // four, set the last checkedItems item 'which'
                                        // to false and uncheck the checkbox
                                        checkedItems[which] = false;
                                        ((AlertDialog) dialog).getListView().setItemChecked(which, false);
                                        break;
                                    }
                                }
                            }
                        });
                        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });
                        alertDialog.setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int which) {
                                dialogInterface.dismiss();
                                StringBuilder days = new StringBuilder();
                                StringBuilder daysStr = new StringBuilder();

                                for (int i = 0; i < checkedItems.length; i++) {
                                    // loop through the checkedItems array, if checkedItems
                                    // increment count*/

                                    if (checkedItems[i]) {
                                        if (days.length() > 0) {

                                            daysStr.append(",").append(str[i]);
                                            days.append(",").append(strId[i]);
                                        } else {
                                            days = new StringBuilder(strId[i]);
                                            daysStr = new StringBuilder(str[i]);
                                        }
                                    }
                                }
                                nDays = days.toString();
                                nDaysTV.setText(daysStr.toString());
                            }
                        });

                        alertDialog.show();
                    } else {

                        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_singlechoice);

                            arrayAdapter.add("Monday");
                            arrayAdapter.add("Tuesday");
                            arrayAdapter.add("Wednesday");
                            arrayAdapter.add("Thursday");
                            arrayAdapter.add("Friday");
                            arrayAdapter.add("Saturday");
                            arrayAdapter.add("Sunday");



                        mealPlanBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                        mealPlanBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String strName = arrayAdapter.getItem(which);
                                if (strName != null) {
                                    nDaysTV.setText(strName);
                                    switch (strName){
                                        case "Monday":
                                            nDays = "1";
                                            break;
                                        case "Tuesday":
                                            nDays = "2";
                                            break;
                                        case "Wednesday":
                                            nDays = "3";
                                            break;
                                        case "Thursday":
                                            nDays = "4";
                                            break;
                                        case "Friday":
                                            nDays = "5";
                                            break;
                                        case "Saturday":
                                            nDays = "6";
                                            break;
                                        case "Sunday":
                                            nDays = "7";
                                            break;
                                    }

                                }
                            }
                        });
                        mealPlanBuilder.show();
                    }


                    /*final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_multichoice);

                        arrayAdapter.add("Monday");
                        arrayAdapter.add("Tuesday");
                        arrayAdapter.add("Wednesday");
                        arrayAdapter.add("Thursday");
                        arrayAdapter.add("Friday");
                        arrayAdapter.add("Saturday");
                        arrayAdapter.add("Sunday");*/

                }
            });
            typeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mealPlanBuilder.setTitle("Select ");

                    final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_singlechoice);
                    for (String s : typeList ) {
                        arrayAdapter.add(s);
                    }


                    mealPlanBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    mealPlanBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String strName = arrayAdapter.getItem(which);
                            if (strName != null) {
                                typeTV.setText(strName);
                                type = strName;
                            }
                        }
                    });
                    mealPlanBuilder.show();
                }
            });

            saveBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (!mealPlanTV.getText().toString().equals("Select meal plan")) {
                        if (!nDaysTV.getText().toString().equals("Select number of days")) {
                            if (!typeTV.getText().toString().equals("Select")) {
                                String UserId = App.pref.getString(Constant.USER_ID, "");
                                for (AddressData data : addressData) {
                                    String address = String.format(
                                            "%s, \n%s, %s, ",
                                            data.getCustomerName(),
                                            data.getHouseNumber(),
                                            data.getAddress()
                                    );
                                    if (address.equals(addressStr)) {
                                        addressId = data.getAddressId();
                                    }
                                }

                                if (policyCB.isChecked()) {

                                    if (addressId.length()>0) {
                                        String paymentMethod;
                                        if (paymentRG.getCheckedRadioButtonId()==R.id.c_o_d_RB){
                                            paymentMethod = "COD";
                                        }else{
                                            paymentMethod = "online";
                                        }
                                        addTiffinBox(UserId, mealPlanId, nDays, mealPlanQty, amount, type, addressId,paymentMethod);
                                    }else
                                        AppConfig.showToast("Please select Address or add Address in My address");
                                }else
                                    AppConfig.showToast("Please accept policies");
                            } else
                                AppConfig.showToast("please select lunch or dinner or both");
                        } else
                            AppConfig.showToast("please select number of days");
                    } else {
                        AppConfig.showToast("please select meal plan.");
                    }
                }
            });

        }
        return view1;
    }

    private void addTiffinBox(String userId, String mealPlanId, String nDays, String mealPlanQty, final String amount, String type, String addressId, final String paymentMethod) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().addTiffinBox(
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(mealPlanId),
                AppConfig.setRequestBody(nDays),
                AppConfig.setRequestBody(mealPlanQty),
                AppConfig.setRequestBody(amount),
                AppConfig.setRequestBody(type),
                AppConfig.setRequestBody(paymentMethod),
                AppConfig.setRequestBody(addressId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("ADD_Tiffin_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            PaymentResponse successResponse = new Gson().fromJson(responseData,PaymentResponse.class);
                           /* AppConfig.showToast(object.getString("message"));
                            HomeActivity.addFragment(new TiffinBoxFragment(),false);*/
                            // addAddressLL.setVisibility(View.GONE);
                            if (paymentMethod.equals("online")){

                                Intent intent = new Intent(getActivity(),WebViewActivity.class);
                                intent.putExtra(AvenuesParams.ACCESS_CODE, Constant.access_code);
                                intent.putExtra(AvenuesParams.MERCHANT_ID, Constant.merchant_id);
                                intent.putExtra(AvenuesParams.ORDER_ID, successResponse.getOrderDetails().getOrderId());
                                intent.putExtra(AvenuesParams.CURRENCY, Constant.currency1);
                                intent.putExtra(AvenuesParams.AMOUNT, "1.00"/*amount*/);
                                intent.putExtra(AvenuesParams.billing_name, successResponse.getOrderDetails().getBillingName());
                                intent.putExtra(AvenuesParams.billing_zip, successResponse.getOrderDetails().getBillingZip());
                                intent.putExtra(AvenuesParams.billing_address, successResponse.getOrderDetails().getBillingAddress());
                                intent.putExtra(AvenuesParams.billing_tel, successResponse.getOrderDetails().getBillingTel());
                                intent.putExtra(AvenuesParams.billing_email, successResponse.getOrderDetails().getBillingEmail());

                                intent.putExtra(AvenuesParams.REDIRECT_URL, Constant.redirct_url);
                                intent.putExtra(AvenuesParams.CANCEL_URL, Constant.cancel_url);
                                intent.putExtra(AvenuesParams.RSA_KEY_URL, Constant.rsa_url);


                                startActivity(intent);
                            }else {
                                AppConfig.showToast(object.getString("message"));
                                String orderId = successResponse.getOrderDetails().getOrderId();
                                String billingAddress = successResponse.getOrderDetails().getBillingAddress();
                                String billingEmail = successResponse.getOrderDetails().getBillingEmail();
                                String billingName = successResponse.getOrderDetails().getBillingName();
                                String billingTel = successResponse.getOrderDetails().getBillingTel();
                                String billingZip = successResponse.getOrderDetails().getBillingZip();
                                HomeActivity.addFragment(FinishOrderFragment.newInstance("<b>Order Id </b>= "+(orderId)+"<br/><b>Payment Method</b> = "+"COD"+"<br/><b>Order total</b> = "+(amount)+"<br/><b>Order Address</b><br/> <br/>"+(billingName)+"<br/>"+(billingAddress)+"<br/>"+(billingZip)), false);
                            }
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
                AppConfig.showToast("Something went wrong.");
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });


    }

    private void getMealPlanCall() {
        Call<ResponseBody> call = AppConfig.getLoadInterface().getMealPlan();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")) {
                            final MealPlanData planData = new Gson().fromJson(responseData, MealPlanData.class);
                            mealPlanBuilder.setTitle("Select Number of Meals");

                            final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_singlechoice);
                            for (MealDatum slot : planData.getMealData()) {
                                arrayAdapter.add(slot.getMealQty() + " meal plan for Rs. " + slot.getPlanAmount());
                            }

                            mealPlanBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });

                            mealPlanBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String strName = arrayAdapter.getItem(which);
                                    for (MealDatum slot : planData.getMealData()) {
                                        if (strName.equals(slot.getMealQty() + " meal plan for Rs. " + slot.getPlanAmount())) {
                                            mealPlanId = slot.getMealPlanId();
                                            mealPlanQty = slot.getMealQty();
                                            amount = slot.getPlanAmount();
                                            mealPlanTV.setText(slot.getMealQty() + " meal plan for Rs. " + slot.getPlanAmount());
                                            amountTV.setText(slot.getPlanAmount());
                                            if (slot.getMealQty().equals("1")){
                                                isMultiple = false;
                                                typeList = new ArrayList<>();
                                                typeList.add("Lunch");
                                                typeList.add("Dinner");
                                            }else {
                                                isMultiple = true;
                                                typeList = new ArrayList<>();
                                                typeList.add("Lunch");
                                                typeList.add("Dinner");
                                                typeList.add("Both");
                                            }
                                        }
                                    }
                                }
                            });
                            mealPlanBuilder.show();


                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
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

                        if (object.getString("status").equals("1")) {
                            GetAddressData addressResponse = new Gson().fromJson(responseData, GetAddressData.class);
                            addressData = addressResponse.getMsg();
                            setAddress();
                        } else {
                            AppConfig.showToast("No Address present, Please Add Address in My Address.");
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

    private void setAddress() {
        RadioGroup ll = new RadioGroup(getActivity());
        ll.setOrientation(LinearLayout.VERTICAL);

        for (int i = 0; i < addressData.size(); i++) {
            RadioButton rdbtn = new RadioButton(getActivity());
            rdbtn.setId((addressData.size() * 1) + i);

            if (i==0){
                rdbtn.setChecked(true);
                addressId = addressData.get(i).getAddressId();
            }
            addressStr = String.format(
                    /*"%s, \n"%s,*/" %s, ",
                   /* addressData.get(i).getCustomerName(),*/
                  /*  addressData.get(i).getHouseNumber(),*/
                    addressData.get(i).getAddress());
            rdbtn.setText(addressStr);
            ll.addView(rdbtn);
        }
        ((ViewGroup) view1.findViewById(R.id.radiogroup)).removeAllViews();
        ((ViewGroup) view1.findViewById(R.id.radiogroup)).addView(ll);
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Tiffin Box");
        HomeActivity.enableViews(true);
    }
}
