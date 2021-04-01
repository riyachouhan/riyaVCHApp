package com.vch.fragmets;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.HomeRVAdapter;
import com.vch.bean.MenuArray;
import com.vch.bean.SliderArray;
import com.vch.response.HomeResponse;
import com.vch.utiles.AViewFlipper;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;
import com.vch.utiles.DataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
private AViewFlipper vf;
    List<SliderArray> sliderArrays = new ArrayList<>();
    List<MenuArray> menuArrays = new ArrayList<>();
    HomeRVAdapter adapter;
    RecyclerView recyclerView;
    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        vf = view.findViewById(R.id.add_service_AVF);
        recyclerView = view.findViewById(R.id.recycler_view);
        getHomeCall();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);

        return view;
    }

    private void getHomeCall() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        String userId = App.pref.getString(Constant.USER_ID,"");
        Call<ResponseBody> call = AppConfig.getLoadInterface().homeCall(AppConfig.setRequestBody(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("home_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            menuArrays = new ArrayList<>();
                            sliderArrays = new ArrayList<>();
                            vf.removeAllViews();
                            HomeResponse homeResponse = new Gson().fromJson(responseData,HomeResponse.class);

                            sliderArrays = homeResponse.getMsg().getSliderArray();
                            for (SliderArray aXMEN : sliderArrays) {
                                setFlipperImage(aXMEN.getImageUrl(),aXMEN.getName(),aXMEN.getDescription());
                            }
                            menuArrays = homeResponse.getMsg().getMenuArray();

                            adapter = new HomeRVAdapter(getActivity(),menuArrays);
                            recyclerView.setAdapter(adapter);
                            //adapter.updateList(menuArrays);
                            DataHolder.setCount(homeResponse.getMsg().getCartCount());
                            DataHolder.setSubTotal(homeResponse.getMsg().getSubtotal());

                        }else {
                            menuArrays = new ArrayList<>();
                            sliderArrays = new ArrayList<>();
                            adapter.updateList(menuArrays);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
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

    private void setFlipperImage(String res, final String name, final String description) {
        //Log.i("Set Filpper Called", res+"");
        if (getActivity()!=null)
        Glide.with(getActivity()).asBitmap().load(res).into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap res, Transition<? super Bitmap> transition) {
                try {
                    View child = getLayoutInflater().inflate(R.layout.item_banner, null);

                    ImageView image = child.findViewById(R.id.image_view);
                    TextView nameTV = child.findViewById(R.id.name_TV);
                    TextView descriptionTV = child.findViewById(R.id.description_TV);
                    Drawable drawable = new BitmapDrawable(getResources(),res);
                    image.setBackground(drawable);
                    nameTV.setText(name);
                    descriptionTV.setText(description);
                    vf.addView(child);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
        HomeActivity.enableViews(false);

    }
}
