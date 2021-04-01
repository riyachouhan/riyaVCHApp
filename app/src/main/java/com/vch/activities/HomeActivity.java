package com.vch.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.navigation.NavigationView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.fragmets.AboutUsFragment;
import com.vch.fragmets.ContactsUsFragment;
import com.vch.fragmets.FragmentTermsConditions;
import com.vch.fragmets.HomeFragment;
import com.vch.fragmets.LoyaltyProgramFragment;
import com.vch.fragmets.MyAddressFragment;
import com.vch.fragmets.OrderHistoryFragment;
import com.vch.fragmets.PartyOrderFragment;
import com.vch.fragmets.PrivacyPolicyFragment;
import com.vch.fragmets.ProfileFragment;
import com.vch.fragmets.ReferNEarnFragment;
import com.vch.fragmets.SearchFragment;
import com.vch.fragmets.ShippingPolicyFragment;
import com.vch.fragmets.SuccessFragmentOnline;
import com.vch.fragmets.TiffinBoxFragment;
import com.vch.response.UserDetail;
import com.vch.response.UserResponse;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static ActionBarDrawerToggle mDrawerToggle;
    private static TextView name;
    private static CircleImageView imageView;
    private static boolean mToolBarNavigationListenerIsRegistered = false;
    private static FragmentManager fm;
    private static ActionBar actionBar;
    boolean doubleBackToExitPressedOnce = false;
    private static AppCompatActivity activity;
    public static UserDetail detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        if (getIntent().getExtras() != null) {
            detail = getIntent().getExtras().getParcelable("userData");
            fm = getSupportFragmentManager();
            activity = this;
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();

            final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

            mDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(mDrawerToggle);
            mDrawerToggle.syncState();

            NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
            navigationView.setNavigationItemSelectedListener(this);

            View view = navigationView.getHeaderView(0);
            name = view.findViewById(R.id.name_TV);
            imageView = view.findViewById(R.id.profile_CIV);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    }
                    addFragment(new ProfileFragment(), true);
                }
            });

            setname(this);

            addFragment(new HomeFragment(), false);

        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else if (!doubleBackToExitPressedOnce) {
                this.doubleBackToExitPressedOnce = true;

                AppConfig.showToast("Please click BACK again to exit.");

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        doubleBackToExitPressedOnce = false;
                    }
                }, 2000);
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        MenuItem search = menu.findItem(R.id.menu_search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        return super.onOptionsItemSelected(item);
    }


    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.equals(""))
                    addFragment(new HomeFragment(), false);
                else
                    addFragment(SearchFragment.newInstance(newText), false);
                return true;
            }
        });
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment;
        switch (id) {
            case R.id.nav_home:

                clearFragmentStack();
                fragment = new HomeFragment();
                break;

            case R.id.nav_aboutus:

                clearFragmentStack();
                fragment = new AboutUsFragment();
                break;


            case R.id.nav_order_history:

                fragment = new OrderHistoryFragment();
                break;
            case R.id.nav_tiffin_box:

                fragment = new TiffinBoxFragment();
                break;
            case R.id.nav_refer_n_earn:

                fragment = new ReferNEarnFragment();
                break;
            case R.id.nav_loyalty_program:

                fragment = new LoyaltyProgramFragment();
                break;
            case R.id.nav_party_order:

                fragment = new PartyOrderFragment();
                break;
            case R.id.nav_my_address:

                fragment = new MyAddressFragment();
                break;
            case R.id.nav_contact_us:

                fragment = new ContactsUsFragment();
                break;

            case R.id.nav_privacy_policy:

                fragment = new PrivacyPolicyFragment();
                break;

            case R.id.nav_terms_conditions:

                fragment = new FragmentTermsConditions();
                break;

            case R.id.nav_shipping_policy:

                fragment = new ShippingPolicyFragment();
                break;

            case R.id.nav_sign_out:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HomeActivity.this);
                alertDialogBuilder.setTitle("Do you want to Sign Out ?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        App.editor.clear();
                        startActivity(new Intent(HomeActivity.this,BaseActivity.class));
                        finish();

                    }
                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
                return true;
            default:
                clearFragmentStack();
                fragment = new HomeFragment();
                break;
        }
        addFragment(fragment, true);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void addFragment(Fragment fragment, boolean addToBackStack) {

        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, fragment, "");
        //if (!tag.equals("Home"))
        if (addToBackStack) {
            transaction.addToBackStack(null);
        }
        transaction.commit();
    }

    public static void clearFragmentStack() {
        fm.popBackStackImmediate(0, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    public static void enableViews(boolean enable) {

        if (enable) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_left_white);
            actionBar.setDisplayHomeAsUpEnabled(false);
            // Remove hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(false);
            // Show back button
            actionBar.setDisplayHomeAsUpEnabled(true);

            if (!mToolBarNavigationListenerIsRegistered) {
                mDrawerToggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Doesn't have to be onBackPressed
                        activity.onBackPressed();
                    }
                });

                mToolBarNavigationListenerIsRegistered = true;
            }

        } else {

            // Remove back button

            // Show hamburger
            mDrawerToggle.setDrawerIndicatorEnabled(true);
            //mDrawerToggle.setDrawerArrowDrawable();
            // Remove the/any drawer toggle listener
            mDrawerToggle.setToolbarNavigationClickListener(null);
            mToolBarNavigationListenerIsRegistered = false;
        }
    }
    public static void setname(Activity activity){
        name.setText(detail.getUserName());

        Glide.with(activity).asBitmap().load(detail.getUserProfileImage()).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, Transition<? super Bitmap> transition) {
                imageView.setImageBitmap(resource);
            }

            @Override
            public void onLoadFailed(@Nullable Drawable errorDrawable) {
                super.onLoadFailed(errorDrawable);
                imageView.setImageResource(R.drawable.default_user);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("TAG" ," result code "+resultCode);
        // -1 means successful  // 0 means failed
        // one error is - nativeSdkForMerchantMessage : networkError
        if (requestCode == 2 && data != null)
        {
            Bundle bundle = data.getExtras();
            if (bundle != null) {
                for (String key : bundle.keySet()) {
                    Log.e("TAG", key + " : " + (bundle.get(key) != null ? bundle.get(key) : "NULL"));
                }
            }
            Log.e("TAG", " data "+  data.getStringExtra("nativeSdkForMerchantMessage"));
            Log.e("TAG", " data response - "+data.getStringExtra("response"));
            String paytmresponse=data.getStringExtra("response");
            try
            {
                if(paytmresponse.equalsIgnoreCase(""))
                {

                }else
                {
                    Log.e("Data_response", paytmresponse);
                    JSONObject object = new JSONObject(paytmresponse);
                    String txtID = object.getString("TXNID");
                    String ORDERID = object.getString("ORDERID");
                    OrederStatusSend(ORDERID, txtID);
                }
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
            Log.e("TAG", " payment failed");
        }
    }

    private void PaymentSucceeeDiaog(final String transaction_id,final String orderid,final Activity mContext)
    {
        //SuccessFragmentOnline
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        final View dialogView;
        dialogView = layoutInflater.inflate(R.layout.paytmseccess, null);
        final BottomSheetDialog dialog = new BottomSheetDialog(mContext, R.style.DialogStyle);
        dialog.setContentView(dialogView);
         final TextView btnVerify =dialogView.findViewById(R.id.btnVerify);
        btnVerify.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                dialog.dismiss();
                clearFragmentStack();
         /*       Fragment fragment = new SuccessFragmentOnline();
                addFragment(fragment, true);
         */   }
        });
        dialog.show();
    }

    public void OrederStatusSend(final String order_id,final String transaction_id)
    {
        final ProgressDialog dialog = new ProgressDialog(HomeActivity.this);
        dialog.setMessage("Please wait...........");
        dialog.show();
        String userid=App.pref.getString(Constant.USER_ID,"");
        Call<ResponseBody> call = AppConfig.getLoadInterfacepayment().OrderStatusChange(
                AppConfig.setRequestBody(userid)
                ,AppConfig.setRequestBody(order_id),
                AppConfig.setRequestBody(transaction_id));

        call.enqueue(new Callback<ResponseBody>()
        {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                Log.e("PLACE_checksum", response.toString());
                if (response.isSuccessful())
                {
                    try {
                       String responseData = response.body().string();
                        Log.e("Data_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        String userid=App.pref.getString(Constant.USER_ID,"");
                        if(dialog.isShowing())
                            dialog.dismiss();

                        if(object.getString("status").equalsIgnoreCase("1"))
                        {
                            Toast.makeText(HomeActivity.this, "tramscation id "+transaction_id, Toast.LENGTH_SHORT).show();
                            //getProfileCall(transaction_id,order_id,dialog,App.pref.getString(Constant.USER_ID,""));
                            // HomeActivity.clearFragmentStack();
                            //PaymentSucceeeDiaog(transaction_id,order_id,HomeActivity.this);
                            //PaymentSucceeeDiaog(transaction_id,order_id,HomeActivity.this);
                            Fragment fragment = new SuccessFragmentOnline();
                            addFragment(fragment, true);

                        }else
                        {
                            Toast.makeText(HomeActivity.this, "tramscation id "+transaction_id, Toast.LENGTH_SHORT).show();
                             getProfileCall(transaction_id,order_id,dialog,App.pref.getString(Constant.USER_ID,""));
                        }

                    }catch (Exception e)
                    {
                        if(dialog.isShowing())
                            dialog.dismiss();
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


   private void getProfileCall(final String transaction_id,final String order_id,final ProgressDialog dialog,String userId) {

        String useridv=userId;
        Call<ResponseBody> call = AppConfig.getLoadInterface().getProfileCall(AppConfig.setRequestBody(userId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        dialog.dismiss();
                        if (object.getString(Constant.STATUS).equals("1")){

                            UserResponse UserResponse = new Gson().fromJson(responseData,UserResponse.class);
                            //DataHolder.setUserData(UserResponse.getData());
                            HomeActivity.detail = UserResponse.getData();
                            App.editor.putString(Constant.USER_ID,UserResponse.getData().getUserId());
                            App.editor.putBoolean(Constant.IS_LOGIN,true);
                            App.editor.apply();

                            PaymentSucceeeDiaog(transaction_id,order_id,HomeActivity.this);

                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    }catch (Exception e)
                    {
                        if(dialog.isShowing())
                            dialog.dismiss();
                        e.printStackTrace();
                    }
                }else {
                    if(dialog.isShowing())
                        dialog.dismiss();
                    AppConfig.showToast("Something went wrong");
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if(dialog.isShowing())
                    dialog.dismiss();
                t.printStackTrace();
            }
        });
    }

}
