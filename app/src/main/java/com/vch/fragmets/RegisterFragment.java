package com.vch.fragmets;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.BaseActivity;
import com.vch.response.RegisterResponse;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickCancel;
import com.vansuita.pickimage.listeners.IPickResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class RegisterFragment extends Fragment {

    EditText nameET,emailET,mobileET,passwordET,confirmET,referralET;
    CircleImageView profileCIV;
    String imagePath = "";
    CheckBox policyCB;
    TextView policyTV;
    public RegisterFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        nameET = view.findViewById(R.id.name_ET);
        emailET = view.findViewById(R.id.email_ET);
        mobileET = view.findViewById(R.id.mobile_ET);
        passwordET = view.findViewById(R.id.password_ET);
        confirmET = view.findViewById(R.id.confirm_password_ET);
        referralET = view.findViewById(R.id.referal_code_ET);

        policyCB = view.findViewById(R.id.policy_CB);
        policyTV = view.findViewById(R.id.policy_Tv);
        profileCIV = view.findViewById(R.id.profile_CIV);

        view.findViewById(R.id.profile_CIV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PickImageDialog dialog = PickImageDialog.build(new PickSetup());
                dialog.setOnPickCancel(new IPickCancel() {
                    @Override
                    public void onCancelClick() {
                        dialog.dismiss();
                    }
                }).setOnPickResult(new IPickResult() {
                    @Override
                    public void onPickResult(PickResult r) {
                        //TODO: do what you have to...

                        Log.e("Image path",r.getPath());

                        imagePath = r.getPath();

                        Log.e("Image path",imagePath);
                        profileCIV.setImageBitmap(r.getBitmap());
                        //addImageCall(final_path, DataHolder.getTeachLoginResponse().getResult().getId());
                    }
                }).show(getActivity().getSupportFragmentManager());
            }
        });


        policyTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BaseActivity.addFragment(new TremsConditionsFragment(),true);
            }
        });
        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String nameStr = nameET.getText().toString();
                String emailStr = emailET.getText().toString();
                String mobileStr = mobileET.getText().toString();
                String passwordStr = passwordET.getText().toString();
                String confirmStr = confirmET.getText().toString();
                String referalCodeStr = referralET.getText().toString();
                if (nameStr.length()>0){
                    if (emailStr.length()>0){
                        if(AppConfig.checkEmail(emailStr)){
                            if (mobileStr.length()>0&&mobileStr.length()==10){
                                if (passwordStr.length()>0){
                                    if (confirmStr.length()>0){
                                        if (passwordStr.equals(confirmStr)){
                                            if (policyCB.isChecked()){
                                                RequestBody name = AppConfig.setRequestBody(nameStr);
                                                RequestBody email = AppConfig.setRequestBody(emailStr);
                                                RequestBody mobile = AppConfig.setRequestBody(mobileStr);
                                                RequestBody password = AppConfig.setRequestBody(passwordStr);
                                                RequestBody regId = AppConfig.setRequestBody(App.RegPref.getString("regId", ""));
                                                RequestBody referalCode = AppConfig.setRequestBody(referalCodeStr);


                                                registerCall(name, email, mobile, password, regId, referalCode);
                                            }else
                                                AppConfig.showToast("Please accept Terms and Conditions.");
                                        }else
                                            AppConfig.showToast("Password not matched.");
                                    }else
                                        AppConfig.showToast("Please enter Confirm password.");
                                }else
                                    AppConfig.showToast("Please enter Password.");
                            }else
                                AppConfig.showToast("Please enter valid Mobile.");
                        }else
                            AppConfig.showToast("Invalid Email. ");
                        }else
                            AppConfig.showToast("Please enter Email.");
                }else
                    AppConfig.showToast("Please enter Name.");
            }
        });


        return view;
    }

    private void registerCall(RequestBody nameStr, RequestBody emaiStr, RequestBody mobileStr, RequestBody passswordStr, RequestBody regId,RequestBody referalCodeStr)  {
        //App.RegPref.getString("reg_id","");
        Call<ResponseBody> call;

        if (imagePath.equals("")) {
            call = AppConfig.getLoadInterface().registerCall(
                        nameStr,
                    emaiStr,passswordStr,mobileStr,regId,referalCodeStr);
        } else {
            File file = new File(imagePath);
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part body = MultipartBody.Part.createFormData("user_img", file.getName(), requestFile);
            call = AppConfig.getLoadInterface().registerCall(
                    nameStr,emaiStr,passswordStr,mobileStr,regId,referalCodeStr,body);
        }

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast(" Successfully Registered.");
                            RegisterResponse registerResponse = new Gson().fromJson(responseData,RegisterResponse.class);
                            BaseActivity.addFragment(OtpFragment.newInstance(registerResponse.getUserData().getUserId(),
                                    registerResponse.getUserData().getUserOtp()),true);
                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }catch (JsonSyntaxException e) {
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
