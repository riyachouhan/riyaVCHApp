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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.BaseActivity;
import com.vch.activities.HomeActivity;
import com.vch.response.RegisterResponse;
import com.vch.response.UserResponse;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoginFragment extends Fragment {

    EditText emailET,passwordET;
    private static final String TAG = LoginFragment.class.getSimpleName();
    public LoginFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_login, container, false);
        emailET = view.findViewById(R.id.email_ET);
        passwordET = view.findViewById(R.id.password_ET);
        view.findViewById(R.id.sign_up_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.addFragment(new RegisterFragment(), true);
            }
        });
        view.findViewById(R.id.forgot_password_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseActivity.addFragment(new ForgotPasswordFragment(), true);
            }
        });
        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = emailET.getText().toString();
                String passwordStr = passwordET.getText().toString();

                if (emailStr.length()>0){
                    if (passwordStr.length()>0){
                        loginCall(emailStr,passwordStr);
                    }else
                        AppConfig.showToast("Please enter Password.");
                }else
                    AppConfig.showToast("Please enter Mobile Number.");

                //startActivity(new Intent(getActivity(), HomeActivity.class));
            }
        });

        return view;
    }

    private void loginCall(String emailStr, String passwordStr) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
         dialog.setMessage("Please wait...........");
         dialog.show();
        String regId = App.RegPref.getString("regId","");
        Log.e("FCM_CODE","fcm_code"+regId);
        Call<ResponseBody> call = AppConfig.getLoadInterface().loginCall(AppConfig.setRequestBody(emailStr),
                AppConfig.setRequestBody(passwordStr),
                AppConfig.setRequestBody(regId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful())
                {
                    try {
                        dialog.dismiss();
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast("Login successfully.");
                            RegisterResponse registerResponse = new Gson().fromJson(responseData,RegisterResponse.class);
                            getProfileCall(registerResponse.getUserData().getUserId());
                        }else {
                            String msg=object.getString("msg");
                            Toast.makeText(getActivity(), ""+msg, Toast.LENGTH_SHORT).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
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
                            //DataHolder.setUserData(UserResponse.getData());
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
                t.printStackTrace();
                AppConfig.hideLoading(dialog);
            }
        });


    }
}
