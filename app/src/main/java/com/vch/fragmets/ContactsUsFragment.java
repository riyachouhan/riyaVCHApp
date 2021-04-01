package com.vch.fragmets;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.vch.R;
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
public class ContactsUsFragment extends Fragment {
    private EditText nameEt,mobileEt,emailEt,messageEt;

    Context context;
    LinearLayout Phone,Email;
    Intent intent;
    public ContactsUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts_us, container, false);

         context = getActivity();

         nameEt = view.findViewById(R.id.name_ET);
         mobileEt = view.findViewById(R.id.mobile_ET);
         emailEt = view.findViewById(R.id.email_ET);
         messageEt = view.findViewById(R.id.message_ET);
         Phone = view.findViewById(R.id.tvPhone);
         Email = view.findViewById(R.id.tvEmail);

        view.findViewById(R.id.submit_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = nameEt.getText().toString();
                String mobile = mobileEt.getText().toString();
                String email = emailEt.getText().toString();
                String message = messageEt.getText().toString();


                if (name.length()>0){
                    if (mobile.length()>0&&mobile.length()==10){
                        if (email.length()>0&&AppConfig.checkEmail(email)){
                            if (message.length()>0){
                                contactUsCall(name,mobile,email,message);
                            }else
                                AppConfig.showToast("Message Required.");
                        }else
                            AppConfig.showToast("Email Required or Invalid");
                    }else
                        AppConfig.showToast("Mobile Required.");
                }else
                    AppConfig.showToast("Name Required.");
            }
        });

        Phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:07312530256" ));
                context.startActivity(intent);

            }
        });

        Email.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(Intent.ACTION_SEND);
                intent.setData(Uri.parse("mailto:" ));
                String[] to={"vijaychaathouse@gmail.com"};
                intent.putExtra(Intent.EXTRA_EMAIL, to);
                intent.putExtra(Intent.EXTRA_SUBJECT, "Hi, this was sent from my app");
                intent.putExtra(Intent.EXTRA_TEXT, "hey whats up");
                intent.setType("message/rfc822");
                Intent chooser = Intent.createChooser(intent, "send Email");
                context.startActivity(chooser);

            }
        });


        return view;
    }

    private void contactUsCall(final String name, String mobile, String email, String message) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().contactUsCall(
                AppConfig.setRequestBody(name),
                AppConfig.setRequestBody(mobile),
                AppConfig.setRequestBody(email),
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
                            nameEt.setText("");
                            mobileEt.setText("");
                            emailEt.setText("");
                            messageEt.setText("");
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
        getActivity().setTitle("Contact Us");
    }
}
