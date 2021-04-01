package com.vch.activities;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.fragmets.LandingFragment;
import com.vch.response.UserResponse;
import com.vch.services.GPSTracker;
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

public class BaseActivity extends AppCompatActivity {

    private static final String TAG = BaseActivity.class.getSimpleName();
    private static FragmentManager fm;
    String[] PERMISSIONS = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.CALL_PHONE};
    public static UserResponse UserResponse;

    private static final int PERMISSION_REQUEST_CODE = 007;
    private Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        fm = getSupportFragmentManager();
        activity = this;
        if (checkPermission(this, PERMISSIONS)) {
            proceed();
        } else {
            requestPermission();
        }
    }

    public static void addFragment(Fragment fragment, boolean addToBackStack) {

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, "");
        //if (!tag.equals("Home"))
        if (addToBackStack)
            transaction.addToBackStack(null);
        transaction.commit();
    }

    public static void clearFragmentStack() {
        fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static boolean checkPermission(Context context, String... permissions) {
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(activity, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.CALL_PHONE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    proceed();
                } else {
                    requestPermission();
                }
                break;
        }
    }

    private void proceed() {
        //**************************** Location ( Latitude & longitude ) *********************************
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED&&
                ActivityCompat.checkSelfPermission(getApplicationContext(),
                        Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE},
                        PERMISSION_REQUEST_CODE);
            }
        } else {
            checkGPS();
        }
    }

    private void checkGPS() {
        GPSTracker gpsTracker = new GPSTracker();
        if (gpsTracker.canGetLocation()) {
            gpsTracker.getLocation();
            gpsTracker.getLatitude();
            gpsTracker.getLongitude();

            App.editor.putString("LAT", String.valueOf(gpsTracker.getLatitude()));
            App.editor.putString("LON", String.valueOf(gpsTracker.getLongitude()));
            App.editor.apply();if (App.pref.getBoolean(Constant.IS_LOGIN,false))
                getProfileCall(App.pref.getString(Constant.USER_ID,""));
            else
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    addFragment(new LandingFragment(), false);
                }
            }, 0);
        } else
            showSettingsAlert();
    }

    private void getProfileCall(String userId) {
        final ProgressDialog dialog = new ProgressDialog(BaseActivity.this);
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(AppConfig.setRequestBody(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e(TAG+"Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){

                            UserResponse = new Gson().fromJson(responseData,UserResponse.class);
                            //DataHolder.setUserData(UserResponse.getData());
                            HomeActivity.detail = UserResponse.getData();
                            App.editor.putString(Constant.USER_ID,UserResponse.getData().getUserId());
                            App.editor.putBoolean(Constant.IS_LOGIN,true);
                            App.editor.apply();

                            Intent i = new Intent(BaseActivity.this, HomeActivity.class);
                            i.putExtra("userData",UserResponse.getData());
                            startActivity(i);
                            finish();
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

    public void showSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(activity);
        alertDialog.setTitle("GPS settings");
        alertDialog.setMessage("GPS is not enabled. Do you want to go to settings menu?");
        alertDialog.setPositiveButton("Settings", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), 1);
            }
        });
        alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                finish();
            }
        });
        alertDialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSION_REQUEST_CODE && resultCode == 1) {
            checkGPS();
        }else if (requestCode==PERMISSION_REQUEST_CODE)
            checkGPS();
    }
}
