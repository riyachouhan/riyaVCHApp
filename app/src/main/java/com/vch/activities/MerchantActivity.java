package com.vch.activities;
//Mobile number: 7777777777
//Password: Paytm12345
//		OTP: 489871

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.paytm.pgsdk.PaytmMerchant;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.vch.R;
import com.vch.response.RegisterResponse;
import com.vch.response.SearchDATA;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.AvenuesParams;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Part;

/**
 * This is the sample app which will make use of the PG SDK. This activity will
 * show the usage of Paytm PG SDK API's.
 **/

public class MerchantActivity extends AppCompatActivity {

    private String  TAG ="MainActivity";
    ProgressBar progressBar;
    String midString;
    String txnAmountString;
    String orderIdString;
    String txnTokenString;
    Button txnProcessBtn;
    //String host = "https://securegw-stage.paytm.in/";
    String host = "https://securegw.paytm.in/";
    Integer ActivityRequestCode = 2;
    EditText amount;
    Intent in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.paytmactivity);
        in=getIntent();
        String orderid=in.getStringExtra(AvenuesParams.ORDER_ID);
        String billlingemail=in.getStringExtra(AvenuesParams.billing_email);
        String amount=in.getStringExtra(AvenuesParams.AMOUNT);
        Toast.makeText(this, ""+amount+" userid  "+App.pref.getString(Constant.USER_ID,""), Toast.LENGTH_SHORT).show();
        String billingtel=in.getStringExtra(AvenuesParams.billing_tel);
        amount="1";
        GenerateCheckSum(amount,orderid,billingtel,billlingemail);
    }

    public void GenerateCheckSum(final String amount,final String orderid,final String billingntel,final String billingemail)
    {
        final ProgressDialog dialog = new ProgressDialog(MerchantActivity.this);
        dialog.setMessage("Please wait...........");
        dialog.show();
        String regId = App.RegPref.getString("regId","");
        Log.e("PLACE_checksum_param", "regisid "+regId +"  order  id"+AppConfig.setRequestBody(orderid)+" "+AppConfig.setRequestBody(billingemail)+" "+
                 AppConfig.setRequestBody(amount)+" "+
                 AppConfig.setRequestBody(billingntel)+" "+
                 AppConfig.setRequestBody(App.pref.getString(Constant.USER_ID,"")));

        Call<ResponseBody> call = AppConfig.getLoadInterfacepayment().generateChecksum(
                AppConfig.setRequestBody(orderid),
                AppConfig.setRequestBody(billingemail),
                AppConfig.setRequestBody(amount),
                AppConfig.setRequestBody(billingntel),
                AppConfig.setRequestBody(App.pref.getString(Constant.USER_ID,"")));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("PLACE_checksum", response.toString());
                if (response.isSuccessful())
                {
                    try {
                        dialog.dismiss();
                        String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        JSONObject bodyobj=object.getJSONObject("body");
                        String checksumtoken_pay=bodyobj.getString("txnToken");
                        midString="VIJAYC26242490811855";
                        txnTokenString=checksumtoken_pay;
                        txnAmountString=amount;
                        callPaytmMethods(txnTokenString,txnAmountString,midString,orderid);
                       }catch (Exception e)
                    {
                        e.printStackTrace();
                    }
                }else
                {
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

        //...........
    }

    public void callPaytmMethods(String txnTokenString,String txnAmountString,String midString,String orderid_pay)
    {
        String callBackUrl = "https://securegw.paytm.in/theia/paytmCallback?ORDER_ID="+orderid_pay;
        String datdem="paytm data"+orderid_pay+" midid "+midString+" txttoken "+txnTokenString+" txtamount "+txnAmountString+" callback "+callBackUrl;
        Log.d("paytm data",orderid_pay+" midid "+midString+" txttoken "+txnTokenString+" txtamount "+txnAmountString+" callback "+callBackUrl);
        PaytmOrder paytmOrder = new PaytmOrder(orderid_pay, midString, txnTokenString, txnAmountString, callBackUrl);
        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback()
        {
            @Override
            public void onTransactionResponse(Bundle bundle) {
                Toast.makeText(MerchantActivity.this, "Response (onTransactionResponse) : "+bundle.toString(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {
                Toast.makeText(MerchantActivity.this, "error process "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void clientAuthenticationFailed(String s) {
                Toast.makeText(MerchantActivity.this, "s authentication "+s, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void someUIErrorOccurred(String s) {
                Toast.makeText(MerchantActivity.this, "some error "+s, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {
                Toast.makeText(MerchantActivity.this, "s srint "+s+" s1 "+s1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {
                Toast.makeText(MerchantActivity.this, "on transaction "+s, Toast.LENGTH_SHORT).show();
            }
        });

        transactionManager.setShowPaymentUrl(host + "theia/api/v1/showPaymentPage");
        transactionManager.startTransaction(MerchantActivity.this, ActivityRequestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == ActivityRequestCode && data != null)
        {
            Toast.makeText(this,  "result code "+resultCode+" on acitivyt result "+data.getStringExtra("nativeSdkForMerchantMessage") + " activity "+data.getStringExtra("response"), Toast.LENGTH_SHORT).show();
        }
*/
        Log.e(TAG ," result code "+resultCode);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ActivityRequestCode && data != null) {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e(TAG, key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Log.e(TAG, " data "+  data.getStringExtra("nativeSdkForMerchantMessage"));
            Log.e(TAG, " data response - "+data.getStringExtra("response"));

            String paytmresponse=data.getStringExtra("response");
            try {
          //      JsonObject paytmrespo=paytmresponse;
            }catch (Exception e)
            {
                e.printStackTrace();
            }
          /*
 data response - {"BANKNAME":"WALLET","BANKTXNID":"1395841115",
 "CHECKSUMHASH":"7jRCFIk6mrep+IhnmQrlrL43KSCSXrmM+VHP5pH0hekXaaxjt3MEgd1N9mLtWyu4VwpWexHOILCTAhybOo5EVDmAEV33rg2VAS/p0PXdk\u003d",
 "CURRENCY":"INR","GATEWAYNAME":"WALLET","MID":"EAc0553138556","ORDERID":"100620202152",
 "PAYMENTMODE":"PPI","RESPCODE":"01","RESPMSG":"Txn Success","STATUS":"TXN_SUCCESS",
 "TXNAMOUNT":"2.00","TXNDATE":"2020-06-10 16:57:45.0","TXNID":"20200610111212800110168328631290118"}
  */


        }else{
            Log.e(TAG, " payment failed");
        }
    }

}
/*extends AppCompatActivity  implements PaytmPaymentTransactionCallback
{
	Button start_transaction;
	EditText cust_mobile_no;
	String mobilenovalue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchantapp);
		start_transaction=findViewById(R.id.start_transaction);
		cust_mobile_no=findViewById(R.id.cust_mobile_no);
		start_transaction.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v)
			{
				mobilenovalue=cust_mobile_no.getText().toString();
				if(mobilenovalue.length()!=0)
				{
					GenerateCheckSum(mobilenovalue);
				}else
				{
					Toast.makeText(MerchantActivity.this, "Please enter ", Toast.LENGTH_SHORT).show();
				}

			}
		});
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

	}

	//This is to refresh the order id: Only for the Sample App's purpose.
	@Override
	protected void onStart(){
		super.onStart();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	public void GenerateCheckSum(final String mobilenovalue)
	{
		Random r = new Random(System.currentTimeMillis());
		final String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
				+ r.nextInt(10000);
		//for retorilft
		final ProgressDialog dialog = new ProgressDialog(MerchantActivity.this);
		dialog.setMessage("Please wait...........");
		dialog.show();
		String regId = App.RegPref.getString("regId","");
		Log.e("FCM_CODE","fcm_code"+regId);

		Call<ResponseBody> call = AppConfig.getLoadInterfacepayment().generateChecksum(AppConfig.setRequestBody(orderId),
				AppConfig.setRequestBody(orderId),
				AppConfig.setRequestBody("1"),
				AppConfig.setRequestBody(mobilenovalue),
				AppConfig.setRequestBody("testing@gmail.com"));
		call.enqueue(new Callback<ResponseBody>() {
			@Override
			public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
				if (response.isSuccessful())
				{
					try {
						dialog.dismiss();
						String responseData = response.body().string();
						Log.e("Data_response",responseData);
						JSONObject object = new JSONObject(responseData);
						String checksumtoken_pay=object.getString("token");
						String callbackurl_pay=object.getString("CALLBACK_URL");
						String merchant_pay=object.getString("Merchant_ID");
						String merchantkey_pay=object.getString("Merchant_Key");
						String orderid_pay=object.getString("ORDER_ID");
						String custid_pay=object.getString("CUST_ID");
						String channelid_pay=object.getString("CHANNEL_ID");
						String industrytypeid_pay=object.getString("INDUSTRY_TYPE_ID");
						String txnamount_pay=object.getString("TXN_AMOUNT");
						String website_pay=object.getString("Website");
						String msisdn_pay=object.getString("MSISDN");
						onStartTransaction(checksumtoken_pay,callbackurl_pay,merchant_pay,merchantkey_pay,
								orderid_pay,custid_pay,channelid_pay,
								industrytypeid_pay,txnamount_pay,website_pay,msisdn_pay);

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

		//...........
	}

	//...........
	public void onStartTransaction(String checksumtoken_pay,String callbackurl_pay,String merchant_pay,String merchantkey_pay,
								   String orderid_pay,String custid_pay,String channelid_pay,
								   String industrytypeid_pay,String txnamount_pay,String website_pay,
								   String msisdn_pay)
	{
		PaytmPGService Service = PaytmPGService.getProductionService();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// these are mandatory parameters
		paramMap.put("ORDER_ID", orderid_pay);
		paramMap.put("MID", merchant_pay);
		paramMap.put("CUST_ID",custid_pay);
		paramMap.put("CHANNEL_ID", channelid_pay);
		paramMap.put("TXN_AMOUNT",txnamount_pay);
		paramMap.put("WEBSITE", website_pay);
		paramMap.put("CALLBACK_URL",callbackurl_pay+orderid_pay);
		paramMap.put("CHECKSUMHASH", checksumtoken_pay);
		paramMap.put("INDUSTRY_TYPE_ID", industrytypeid_pay);
		*//*paramMap.put("EMAIL","testing@gmail.com");
		paramMap.put("MOBILE_NO","8878010247");*//*
		PaytmOrder Order = new PaytmOrder(paramMap);

		Service.initialize(Order,null);

		//finally starting the payment transaction
		Service.startPaymentTransaction(this, true, true, MerchantActivity.this);
	}

	//all these overriden method is to detect the payment result accordingly
	@Override
	public void onTransactionResponse(Bundle inResponse) {

		Log.d("LOG", "Payment Transaction : " + inResponse);
		String response=inResponse.getString("RESPMSG");
		if (response.equals("Txn Successful."))
		{
			//new ConfirmMerchent().execute();
		}else
		{
			Toast.makeText(getApplicationContext(),response,Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	public void networkNotAvailable() {
		Toast.makeText(this, "Network error", Toast.LENGTH_LONG).show();
	}

	@Override
	public void clientAuthenticationFailed(String s) {
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	@Override
	public void someUIErrorOccurred(String s) {
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onErrorLoadingWebPage(int i, String s, String s1) {
		Toast.makeText(this, s, Toast.LENGTH_LONG).show();
	}

	@Override
	public void onBackPressedCancelTransaction() {
		Toast.makeText(this, "Back Pressed", Toast.LENGTH_LONG).show();
	}

	@Override
	public void onTransactionCancel(String s, Bundle bundle) {
		Toast.makeText(this, s + bundle.toString(), Toast.LENGTH_LONG).show();
	}

}
*/
/*extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.merchantapp);
		initOrderId();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}

	//This is to refresh the order id: Only for the Sample App's purpose.
	@Override
	protected void onStart(){
		super.onStart();
		initOrderId();
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
	}


	private void initOrderId() {
		Random r = new Random(System.currentTimeMillis());
		String orderId = "ORDER" + (1 + r.nextInt(2)) * 10000
				+ r.nextInt(10000);
		EditText orderIdEditText = (EditText) findViewById(R.id.order_id);
		orderIdEditText.setText(orderId);
	}

	public void onStartTransaction(View view)
	{
		PaytmPGService Service = PaytmPGService.getProductionService();
		HashMap<String, String> paramMap = new HashMap<String, String>();
		// these are mandatory parameters
		paramMap.put("ORDER_ID", ((EditText) findViewById(R.id.order_id)).getText().toString());
		paramMap.put("MID", ((EditText) findViewById(R.id.merchant_id)).getText().toString());
		paramMap.put("CUST_ID", ((EditText) findViewById(R.id.customer_id)).getText().toString());
		paramMap.put("CHANNEL_ID", ((EditText) findViewById(R.id.channel_id)).getText().toString());
		paramMap.put("INDUSTRY_TYPE_ID", ((EditText) findViewById(R.id.industry_type_id)).getText().toString());
		paramMap.put("WEBSITE", ((EditText) findViewById(R.id.website)).getText().toString());
		paramMap.put("TXN_AMOUNT", ((EditText) findViewById(R.id.transaction_amount)).getText().toString());
		paramMap.put("THEME", ((EditText) findViewById(R.id.theme)).getText().toString());
		paramMap.put("EMAIL", ((EditText) findViewById(R.id.cust_email_id)).getText().toString());
		paramMap.put("MOBILE_NO", ((EditText) findViewById(R.id.cust_mobile_no)).getText().toString());
		PaytmOrder Order = new PaytmOrder(paramMap);

		PaytmMerchant Merchant = new PaytmMerchant(
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumGenerator.jsp",
				"https://pguat.paytm.com/paytmchecksum/paytmCheckSumVerify.jsp");

		Service.initialize(Order, null);

		Service.startPaymentTransaction(this, true, true,
				new PaytmPaymentTransactionCallback() {
					@Override
					public void someUIErrorOccurred(String inErrorMessage) {
						// Some UI Error Occurred in Payment Gateway Activity.
						// // This may be due to initialization of views in
						// Payment Gateway Activity or may be due to //
						// initialization of webview. // Error Message details
						// the error occurred.
					}

					@Override
					public void onTransactionResponse(Bundle inResponse) {
						Log.d("LOG", "Payment Transaction is successful " + inResponse);
						Toast.makeText(getApplicationContext(), "Payment Transaction is successful ", Toast.LENGTH_LONG).show();
					}

					@Override
					public void networkNotAvailable() { // If network is not
														// available, then this
														// method gets called.
					}

					@Override
					public void clientAuthenticationFailed(String inErrorMessage) {
						// This method gets called if client authentication
						// failed. // Failure may be due to following reasons //
						// 1. Server error or downtime.
						// 2. Server unable to
						// generate checksum or checksum response is not in
						// proper format.
						// 3. Server failed to authenticate
						// that client. That is value of payt_STATUS is 2. //
						// Error Message describes the reason for failure.
					}

					@Override
					public void onErrorLoadingWebPage(int iniErrorCode,
                                                      String inErrorMessage, String inFailingUrl) {

					}

					// had to be added: NOTE
					@Override
					public void onBackPressedCancelTransaction() {
						// TODO Auto-generated method stub
					}

					@Override
					public void onTransactionCancel(String inErrorMessage, Bundle inResponse) {

					}

				});
	}
}
*/