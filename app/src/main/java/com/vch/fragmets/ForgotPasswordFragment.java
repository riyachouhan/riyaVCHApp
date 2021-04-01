package com.vch.fragmets;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.BaseActivity;
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
public class ForgotPasswordFragment extends Fragment {


    public ForgotPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forgot_password, container, false);

        final EditText mobileET = view.findViewById(R.id.mobile_ET);
        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String mobileStr = mobileET.getText().toString();
                if (!mobileStr.equals("")&& mobileStr.length()==10){
                    forgotpasswordCall(mobileStr);

                }else
                    AppConfig.showToast("Enter Valid Mobile number.");
            }
        });

        return view;
    }

    private void forgotpasswordCall(String mobileStr) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);

        Call<ResponseBody> call = AppConfig.getLoadInterface().forgotPasswordCall(AppConfig.setRequestBody(mobileStr));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast(object.getString("msg"));

                            BaseActivity.addFragment(new LoginFragment(),false);

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
                AppConfig.showToast("Check Internet connection ");
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });



    }

}
