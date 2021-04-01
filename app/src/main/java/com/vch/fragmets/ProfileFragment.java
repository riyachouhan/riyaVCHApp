package com.vch.fragmets;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.response.UserDetail;
import com.vch.response.UserResponse;
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
public class ProfileFragment extends Fragment {
    String imagePath;
    CircleImageView profileCIV;
    EditText nameET;
    TextView passwordTV,mobileTV,emailET;

    Dialog dialog;
    PickImageDialog pickImageDialog;
    public ProfileFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        profileCIV = view.findViewById(R.id.profile_CIV);
        emailET= view.findViewById(R.id.email_ET);
        mobileTV = view.findViewById(R.id.mobile_TV);
        nameET= view.findViewById(R.id.name_ET);
        passwordTV= view.findViewById(R.id.password_TV);

        passwordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HomeActivity.addFragment(new ChangePasswordFragment(),true);
            }
        });
        //erDetail detail = getArguments().getParcelable("userData");
        setProfile();

        view.findViewById(R.id.submit_BT).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameStr = nameET.getText().toString();
                String emailStr = emailET.getText().toString();
                if (AppConfig.checkEmail(emailStr) && nameStr.length()>0)
                    updateProfileCall(nameStr,emailStr,App.pref.getString(Constant.USER_ID,""));
                else
                    AppConfig.showToast("Fill Required Field");
            }
        });

        profileCIV.setOnClickListener(new View.OnClickListener() {
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
                        changePhotoCall(imagePath, App.pref.getString(Constant.USER_ID,""));
                    }
                }).show(getActivity().getSupportFragmentManager());
            }
        });
        return view;
    }

    private void updateProfileCall(String nameStr, String emailStr,final String userId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody>call = AppConfig.getLoadInterface().updateProfileCall(
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(nameStr),
                AppConfig.setRequestBody(emailStr));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast("Update successfully.");
                            getProfileCall(userId);
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

    private void setProfile() {
        UserDetail userData = HomeActivity.detail;
        nameET.setText(userData.getUserName());
        emailET.setText(userData.getUserEmail());
        mobileTV.setText(userData.getUserPhone());
        Glide.with(getActivity()).asBitmap().load(userData.getUserProfileImage()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                profileCIV.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                profileCIV.setImageResource(R.drawable.default_user);
            }
        });
    }


    private void changePhotoCall(String imagePath, final String userId) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        File file = new File(imagePath);
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
        Call<ResponseBody>call = AppConfig.getLoadInterface().changePhotoCall(AppConfig.setRequestBody(userId),body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast("Update successfully.");
                            getProfileCall(userId);
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
                            setProfile();

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
                t.printStackTrace();
                AppConfig.hideLoading(dialog);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        setProfile();
        getActivity().setTitle("Profile");
    }
}
