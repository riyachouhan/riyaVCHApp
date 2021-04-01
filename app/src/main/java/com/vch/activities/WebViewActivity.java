package com.vch.activities;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.vch.R;
import com.vch.utiles.AppConfig;
import com.vch.utiles.AvenuesParams;
import com.vch.utiles.Constant;
import com.vch.utiles.LoadingDialog;
import com.vch.utiles.RSAUtility;
import com.vch.utiles.ServiceHandler;
import com.vch.utiles.ServiceUtility;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;


public class WebViewActivity extends AppCompatActivity {
    Intent mainIntent;
    String encVal;
    boolean flag;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        mainIntent = getIntent();

        new RenderView().execute();

    }


    private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            LoadingDialog.showLoadingDialog(WebViewActivity.this, "Loading...");

        }

        @Override
        protected Void doInBackground(Void... arg0) {


            ServiceHandler sh = new ServiceHandler();

            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, Constant.access_code));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL)
                    , ServiceHandler.GET, params);
            System.out.println("RSA KEy"+vResponse);
            if(!ServiceUtility.chkNull(vResponse).equals("") && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR")==-1){
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY,mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0,vEncVal.length()-1), vResponse);

                Log.e("encrption....", encVal);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            LoadingDialog.cancelLoading();

            @SuppressWarnings("unused")
            class MyJavaScriptInterface
            {
                @JavascriptInterface
                public void processHTML(String html)
                {
                    // process the html as needed by the app
                    String status = null;

                    if(html.indexOf("Failure")!=-1){
                        status = "Transaction Declined!";
                        Log.e("Transaction Failure...", "Transaction Failure........."+html);

                    }else if(html.indexOf("Success")!=-1){
                            status=html;

                        flag = true;
                        Log.e("Transaction Success...", "Transaction Success........."+html);
                        Intent intent = new Intent(getApplicationContext(),StatusActivity.class);
                        intent.putExtra("transStatus", status);
                        startActivity(intent);
                        finish();

//16,17,24,26,29,31


                    }else if(html.indexOf("Aborted")!=-1){
                        Log.e("Transaction Aborted...", "Transaction Aborted........."+html);
                        status = "Transaction Cancelled!";
                    }else{
                        status = "Status Not Known!";
                    }

                }
            }


            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient(){
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    if(url.indexOf("/ccavResponseHandler.php")!=-1){
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });

            StringBuffer params = null;

            try {
                params = new StringBuffer();
            } catch (Exception e1) {

                e1.printStackTrace();
            }


            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE,Constant.access_code));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID,Constant.merchant_id));
			params.append(ServiceUtility.addToPostParams("tid",""));
			params.append(ServiceUtility.addToPostParams("language","EN"));
            params.append(ServiceUtility.addToPostParams("merchant_param3",mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
            params.append(ServiceUtility.addToPostParams("merchant_param5",Constant.custom_tag));

            params.append(ServiceUtility.addToPostParams(AvenuesParams.billing_name,mainIntent.getStringExtra(AvenuesParams.billing_name)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.billing_zip,mainIntent.getStringExtra(AvenuesParams.billing_zip)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.billing_address,mainIntent.getStringExtra(AvenuesParams.billing_address)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.billing_tel,mainIntent.getStringExtra(AvenuesParams.billing_tel)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.billing_email,mainIntent.getStringExtra(AvenuesParams.billing_email)));

            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID,mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL,Constant.redirct_url));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL,Constant.cancel_url));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL,URLEncoder.encode(encVal)));

            String vPostParams = params.substring(0,params.length()-1);
            try {
                webview.postUrl(Constant.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                AppConfig.showToast("Exception occured while opening webview.");
            }

        }

    }
}
