package com.vch.fragmets;


import android.app.ProgressDialog;
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
public class ChangePasswordFragment extends Fragment {
    View view;

    public ChangePasswordFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view =  inflater.inflate(R.layout.fragment_change_password, container, false);
        view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText oldPasswordET = view.findViewById(R.id.old_password_ET);
                EditText newPasswordET = view.findViewById(R.id.new_password_ET);
                EditText confirmPasswordET = view.findViewById(R.id.confirm_password_ET);

                String oldPasswordStr = oldPasswordET.getText().toString();
                String newPasswordStr = newPasswordET.getText().toString();
                String confirmPasswordStr = confirmPasswordET.getText().toString();

                if (oldPasswordStr.length()>0){
                    if (newPasswordStr.length()>0){
                    if (confirmPasswordStr.equals(newPasswordStr)){

                        changePasswordCall(oldPasswordStr,newPasswordStr, App.pref.getString(Constant.USER_ID,""));
                    }else AppConfig.showToast("password not matched");
                    }else
                        AppConfig.showToast("Enter password");
                }else
                    AppConfig.showToast("Enter Password.");
            }
        });
        return view;
    }

    private void changePasswordCall(String oldPasswordStr, String newPasswordStr, final String userId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().changePassword(
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(oldPasswordStr),
                AppConfig.setRequestBody(newPasswordStr));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("changePassword",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString("status").equals("1")){
                            EditText oldPasswordET = view.findViewById(R.id.old_password_ET);
                            EditText newPasswordET = view.findViewById(R.id.new_password_ET);
                            EditText confirmPasswordET = view.findViewById(R.id.confirm_password_ET);

                            oldPasswordET.setText("");
                            newPasswordET.setText("");
                            confirmPasswordET.setText("");
                            AppConfig.showToast(object.getString("msg"));
                        }else
                            AppConfig.showToast(object.getString("msg"));
                        getProfileCall(userId);
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

    private void getProfileCall(String userId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(AppConfig.setRequestBody(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){

                            UserResponse UserResponse = new Gson().fromJson(responseData,UserResponse.class);
                            //DataHolder.setUserData(UserResponse.getData());
                            HomeActivity.detail = UserResponse.getData();
                            HomeActivity.setname(getActivity());
                            App.editor.putString(Constant.USER_ID,UserResponse.getData().getUserId());
                            App.editor.putBoolean(Constant.IS_LOGIN,true);
                            App.editor.apply();


                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JsonSyntaxException e) {
                        e.printStackTrace();
                        App.editor.clear();

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
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Change Password");
    }
}
