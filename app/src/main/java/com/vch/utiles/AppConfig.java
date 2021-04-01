package com.vch.utiles;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Address;
import android.location.Geocoder;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by pintu22 on 17/11/17.
 */

public class AppConfig {

    private static Retrofit retrofit = null;
    private static LoadInterface loadInterface = null;
    private final static Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
            "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                    "\\@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
    );

    private static Retrofit getClient() {
        //if (retrofit == null)
        {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static Retrofit getClientpayment() {
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(100, TimeUnit.SECONDS)
                    .readTimeout(100, TimeUnit.SECONDS).build();
            retrofit = new Retrofit.Builder()
                    .baseUrl(Constant.BASE_URL_MAIN)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        return retrofit;
    }
    public static LoadInterface getLoadInterface()
    {
       // if (loadInterface == null)
        {
            loadInterface = AppConfig.getClient().create(LoadInterface.class);
        }
        return loadInterface;
    }

    public static LoadInterface getLoadInterfacepayment()
    {
        //if (loadInterface == null) {
            loadInterface = AppConfig.getClientpayment().create(LoadInterface.class);
        //}
        return loadInterface;
    }

    public static void showToast(String s) {
        Toast.makeText(App.getInstance(), s, Toast.LENGTH_SHORT).show();
    }

    public static void showLongToast(String s) {
        Toast.makeText(App.getInstance(), s, Toast.LENGTH_LONG).show();
    }


    public static ProgressDialog showLoading(ProgressDialog progress){
        try{
            progress.setMessage("Please Wait...");
            progress.setCancelable(false);
            progress.setIndeterminate(true);
            progress.setCanceledOnTouchOutside(false);
            progress.show();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        return progress;
    }

    public static void ShowAlertDialog(Context context, String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });


        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    public static void hideLoading(ProgressDialog progress){
        try{
            if(progress!=null){
                if(progress.isShowing()){
                    progress.dismiss();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void showDownloading(ProgressBar progress){
        if(progress!=null){
            progress.setVisibility(View.VISIBLE);
        }
    }

    public static void hideDownloading(ProgressBar progress){
        if(progress!=null){
            progress.setVisibility(View.GONE);
        }
    }

    public static boolean isInternetOn() {

        ConnectivityManager connectivity = (ConnectivityManager)App.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    Log.w("INTERNET:", String.valueOf(i));
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        Log.w("INTERNET:", "connected!");
                        return true;
                    }
                }
            }
        }
        return false;

    }

    public static boolean isStringNullOrBlank(String str){
        if(str==null){
            return true;
        }
        else if(str.equals("null") || str.equals("")){
            return true;
        }
        return false;
    }

    public static boolean checkEmail(String email) {
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static double stringToDouble(String s){
        double d = 0;
        try {
            d = Double.parseDouble(s);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return d;
    }

    //method to close keyboard
    public static void closeKeyboard(IBinder windowToken) {
        InputMethodManager mgr = (InputMethodManager) App.getInstance().getSystemService(Context.INPUT_METHOD_SERVICE);
        mgr.hideSoftInputFromWindow(windowToken, 0);
    }

    public static RequestBody setRequestBody(String param){
        return RequestBody.create(MediaType.parse("multipart/form-data"), param);
    }


    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        Log.e("Distance ",dist+"");
        return (dist);
    }

    public static LatLng getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private static double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
}
