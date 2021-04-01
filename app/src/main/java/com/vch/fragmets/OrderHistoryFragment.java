package com.vch.fragmets;


import android.app.ProgressDialog;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.IngredientsRVAdapter;
import com.vch.adapters.OrderHistoryRVAdapter;
import com.vch.bean.OrderHistory;
import com.vch.bean.TiffinOrder;
import com.vch.response.OrderHistoryData;
import com.vch.response.TiffinResponse;
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
public class OrderHistoryFragment extends Fragment {
    List<OrderHistory> historyList = new ArrayList<>();
    List<TiffinOrder> tiffinList = new ArrayList<>();
    TabLayout tabLayout;
    RecyclerView recyclerView;
    OrderHistoryRVAdapter adapter;
    IngredientsRVAdapter mAdapter;

    public OrderHistoryFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);


        recyclerView = view.findViewById(R.id.recycler_view);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.addTab(tabLayout.newTab().setText("Orders"));
        tabLayout.addTab(tabLayout.newTab().setText("Tiffin Orders"));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int right = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).getRight();
                tabLayout.scrollTo(right,0);
                         tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();

                    switch (tabLayout.getSelectedTabPosition()) {
                        case 0:
                            getAddressCall();
                            break;
                        case 1:
                            getTiffinBox();
                            break;
                    }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        getAddressCall();

        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Order History");
        HomeActivity.enableViews(true);
    }

    private void getAddressCall() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        String userId = App.pref.getString(Constant.USER_ID, "");
        Call<ResponseBody> call = AppConfig.getLoadInterface().getOrderCall(AppConfig.setRequestBody(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Get_ADDRESS_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            OrderHistoryData addressResponse = new Gson().fromJson(responseData, OrderHistoryData.class);
                            historyList = addressResponse.getOrderHistory();
                            adapter = new OrderHistoryRVAdapter(getActivity(),historyList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(adapter);
                            recyclerView.setNestedScrollingEnabled(false);
                            adapter.updateList(historyList);

                        } else {
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
    private void getTiffinBox() {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        String userId = App.pref.getString(Constant.USER_ID, "");
        Call<ResponseBody> call = AppConfig.getLoadInterface().getTiffinBox(AppConfig.setRequestBody(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("Get_ADDRESS_Response", responseData);
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")) {
                            TiffinResponse addressResponse = new Gson().fromJson(responseData, TiffinResponse.class);
                            tiffinList = addressResponse.getTiffinOrders();
                            mAdapter = new IngredientsRVAdapter(getActivity(),tiffinList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            recyclerView.setLayoutManager(mLayoutManager);
                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                            recyclerView.setAdapter(mAdapter);
                            recyclerView.setNestedScrollingEnabled(false);

                        } else {
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
}
