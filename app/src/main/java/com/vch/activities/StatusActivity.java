package com.vch.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.vch.R;
import com.vch.bean.OrderStatus;
import com.vch.utiles.AppConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StatusActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.fragment_finish_order);

        Intent mainIntent = getIntent();

        String status = mainIntent.getStringExtra("transStatus");
        String[] strings = status.split("</center>");
        status = strings[1];

        String[] spilt = status.split("</body>");

        status = spilt[0];

       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            tv4.setText(Html.fromHtml(strings[0]+"</head>", Html.FROM_HTML_MODE_COMPACT));
        } else {
            tv4.setText(Html.fromHtml(strings[0]+"</head>"));
        }*/
       findViewById(R.id.continue_BT).setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               finish();
           }
       });

       Log.e("status",status);
        OrderStatus oStatus = new Gson().fromJson(status,OrderStatus.class);

        orderPaymentStatus(oStatus.getOrderId(),oStatus.getTrackingId(),oStatus.getBankRefNo(),
                oStatus.getOrderStatus(),oStatus.getFailureMessage(),oStatus.getPaymentMode(),
                oStatus.getCardName(),oStatus.getStatusCode(),oStatus.getStatusMessage(),
                oStatus.getCurrency(),oStatus.getAmount(),oStatus.getBillingName(),
                oStatus.getBillingAddress(),oStatus.getBillingCity(),oStatus.getBillingState(),
                oStatus.getBillingZip(),oStatus.getBillingCountry(),oStatus.getBillingTel(),
                oStatus.getBillingEmail(),oStatus.getResponseCode(),oStatus.getTransDate());
    }

private void orderPaymentStatus(final String orderId, final String trackingId, final String bankRefNo,
                                final String orderStatus, final String failureMessage, final String paymentMode,
                                final String cardName, String statusCode, final String statusMessage,
                                final String currency, final String amount, final String billingName,
                                final String billingAddress, final String billingCity, final String billingState,
                                final String billingZip, final String billingCountry, final String billingTel,
                                final String billingEmail, final String responseCode, final String transDate){

    final ProgressDialog dialog = new ProgressDialog(this);
    AppConfig.showLoading(dialog);
    Call<ResponseBody> call = AppConfig.getLoadInterface().orderPaymentStatusCall(
            AppConfig.setRequestBody(orderId),
            AppConfig.setRequestBody(trackingId),
            AppConfig.setRequestBody(bankRefNo),
            AppConfig.setRequestBody(orderStatus),
            AppConfig.setRequestBody(failureMessage),
            AppConfig.setRequestBody(paymentMode),
            AppConfig.setRequestBody(statusMessage),
            AppConfig.setRequestBody(currency),
            AppConfig.setRequestBody(amount),
            AppConfig.setRequestBody(billingName),
            AppConfig.setRequestBody(billingAddress),
            AppConfig.setRequestBody(billingCity),
            AppConfig.setRequestBody(billingState),
            AppConfig.setRequestBody(billingZip),
            AppConfig.setRequestBody(billingCountry),
            AppConfig.setRequestBody(billingTel),
            AppConfig.setRequestBody(billingEmail),
            AppConfig.setRequestBody(responseCode),
            AppConfig.setRequestBody(transDate),
            AppConfig.setRequestBody(cardName));

    call.enqueue(new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
            if (response.isSuccessful()) {
                try {
                    String responseData = response.body().string();
                    Log.e("ORDERpaymentstatusCALL", responseData);
                    JSONObject object = new JSONObject(responseData);

                    if (object.getString("status").equals("1")) {

                        String status ="<b>Order Id</b> = "+(orderId)+"<br/><b>Order Status</b> = "+(orderStatus)+"<br/><b>Payment Method</b> = "+"Online"+"<br/><b>Order Total</b> = "+(amount)+"<br/><b>Address</b> = "+(billingName)+"<br/>"+(billingAddress)+"<br/>"+(billingCity)+"<br/>"+(billingState)+"<br/>"+(billingZip);
                        TextView tv4 = (TextView) findViewById(R.id.order_id);
                        tv4.setText(Html.fromHtml(status));
                        }else {
                            AppConfig.showToast(object.getString("msg"));
                           //omeActivity.addFragment(FinishOrderFragment.newInstance(successResponse.getOrderDetails().getOrderId()), false);
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
            AppConfig.hideLoading(dialog);
            t.printStackTrace();
        }
    });
}
    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

   /* @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("userData",HomeActivity.detail);
        startActivity(i);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(getApplicationContext(), HomeActivity.class);
        i.putExtra("userData",HomeActivity.detail);
        startActivity(i);
        finish();
    }*/
}
