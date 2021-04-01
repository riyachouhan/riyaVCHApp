package com.vch.fragmets;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.adapters.ProductRVAdapter;
import com.vch.bean.ProductDetail;
import com.vch.response.SearchDATA;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

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
public class SearchFragment extends Fragment {

    ProductRVAdapter adapter;
    List<ProductDetail> productDetails = new ArrayList<>();

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_serch, container, false);
        if (getArguments()!=null){
            searchCall(getArguments().getString("keyword"));
            adapter = new ProductRVAdapter(getActivity(),productDetails, "SEARCH");

            RecyclerView recyclerView = view.findViewById(R.id.search_RV);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
            recyclerView.setNestedScrollingEnabled(false);

        }
        return view;
    }

    private void searchCall(String keyword) {
        String regId = App.pref.getString(Constant.USER_ID,"");
        Log.e("USER_ID","fcm_code"+regId);
        Call<ResponseBody> call = AppConfig.getLoadInterface().searchCall(AppConfig.setRequestBody(keyword),
                AppConfig.setRequestBody(regId));

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Search_Data",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            SearchDATA data = new Gson().fromJson(responseData,SearchDATA.class);
                            productDetails = data.getMsg();
                            adapter.updateList(productDetails);
                        }else {
                            AppConfig.showToast("No Data to show");
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
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public static Fragment newInstance(String newText) {
        SearchFragment fragment = new SearchFragment();

        Bundle arg = new Bundle();
        arg.putString("keyword",newText);
        fragment.setArguments(arg);
        return fragment;
    }
}
