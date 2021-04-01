package com.vch.fragmets;


import android.app.ProgressDialog;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
public class TremsConditionsFragment extends Fragment {
    private TextView policyTv;

    public TremsConditionsFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_policy, container, false);

            policyTv = view.findViewById(R.id.policy_Tv);

            policyCall();




        return view;
    }

    private void policyCall() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().termsNCondition();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("ADD_Tiffin_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {

                            policyTv.setText(Html.fromHtml(object.getString("message")));

                            //HomeActivity.addFragment(new TiffinBoxFragment(),false);
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
                AppConfig.showToast("Something went wrong.");
                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });
    }


}
