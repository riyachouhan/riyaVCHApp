package com.vch.fragmets;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.response.RegisterResponse;
import com.vch.response.UserResponse;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

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
public class OtpFragment extends Fragment {
    private static final String TAG = OtpFragment.class.getSimpleName();
    public static Fragment newInstance(String userId,String otp) {
        OtpFragment fragment = new OtpFragment();

        Bundle arg = new Bundle();
        arg.putString("userId",userId);
        arg.putString("otp",otp);
        fragment.setArguments(arg);

        return fragment;
    }

    public OtpFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_otp, container, false);

        if (getArguments()!=null){
            final String userId = getArguments().getString("userId");
            final String otp = getArguments().getString("otp");


            view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText otpET = view.findViewById(R.id.mobile_ET);
                    String otpStr = otpET.getText().toString();

                    if (otpStr.length()>0&& otpStr.length()==6){

                        checkCodeCall(userId,otpStr,"");
                    }else {
                        AppConfig.showToast("Code must be of 6 digits");
                    }
                }
            });
        }

        return view;
    }

    private void checkCodeCall(String userId, String otpStr, String type) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        String reg_id = App.RegPref.getString("regId","");
        Call<ResponseBody>call = AppConfig.getLoadInterface().CheckOtpCall(
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(otpStr),
                AppConfig.setRequestBody(type),
                AppConfig.setRequestBody(reg_id));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e(TAG+"user_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast("register successfully.");
                            RegisterResponse registerResponse = new Gson().fromJson(responseData,RegisterResponse.class);

                            getProfileCall(registerResponse.getUserData().getUserId());

                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    AppConfig.showToast("Something went wrong");
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

    private void getProfileCall(String userId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody>call = AppConfig.getLoadInterface().getProfileCall(AppConfig.setRequestBody(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e(TAG+"Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){

                            UserResponse UserResponse = new Gson().fromJson(responseData,UserResponse.class);
                           // DataHolder.setUserData(UserResponse.getData());
                            App.editor.putString(Constant.USER_ID,UserResponse.getData().getUserId());
                            App.editor.putBoolean(Constant.IS_LOGIN,true);
                            App.editor.apply();

                            Intent i = new Intent(getActivity(), HomeActivity.class);
                            i.putExtra("userData",UserResponse.getData());
                            startActivity(i);
                            getActivity().finish();
                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    AppConfig.showToast("Something went wrong");
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
