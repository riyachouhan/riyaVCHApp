package com.vch.fragmets;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.utiles.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class PartyOrderFragment extends Fragment {

    EditText nameET,mobileET,emailET,messageET;
    public PartyOrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_party_order, container, false);
        nameET = view.findViewById(R.id.name_ET);
        mobileET = view.findViewById(R.id.mobile_ET);
        emailET = view.findViewById(R.id.email_ET);
        messageET = view.findViewById(R.id.message_ET);


        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameET.getText().toString();
                String mobile = mobileET.getText().toString();
                String email = emailET.getText().toString();
                String message = messageET.getText().toString();


                if (name.length()>0){
                    if (mobile.length()>0){
                        if (email.length()>0 && AppConfig.checkEmail(email)){
                            if (message.length()>0){

                                partyOrderCall(HomeActivity.detail.getUserId(),name,mobile,email,message);
                            }else {
                                AppConfig.showToast("Message required.");
                            }
                        }else {
                            AppConfig.showToast("Please enter valid Email.");
                        }
                    }else {
                        AppConfig.showToast("Mobile required.");
                    }
                }else {
                    AppConfig.showToast("Name required.");
                }
            }
        });


        return view;
    }

    private void partyOrderCall(String id,String name, String mobile, String email, String message) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().partyOrderCall(
                AppConfig.setRequestBody(id),
                AppConfig.setRequestBody(name),
                AppConfig.setRequestBody(email),
                AppConfig.setRequestBody(mobile),
                AppConfig.setRequestBody(message));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("changePassword",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")){

                            AppConfig.showToast(object.getString("message"));
                            getActivity().onBackPressed();

                        }else
                            AppConfig.showToast(object.getString("message"));
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    AppConfig.showToast("something went wrong");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                AppConfig.showToast("something went wrong");
                AppConfig.hideLoading(dialog);
            }
        });

    }



    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Party Order");
        HomeActivity.enableViews(true);
    }
}
