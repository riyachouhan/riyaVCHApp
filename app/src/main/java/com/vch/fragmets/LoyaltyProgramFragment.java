package com.vch.fragmets;


import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.HistoryRVAdapter;
import com.vch.bean.Transaction;
import com.vch.response.LoyaltyData;
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
public class LoyaltyProgramFragment extends Fragment {
    View view;
    TextView earnedTV,redeemedTV,balancedTV,benefit,memberStatus;
    ImageView his1,his2,mem1,mem2,up1,up2,memberIV;
    LinearLayout history,history_LL,member,lin1,lin2,lin3,lin4,upgrade;
    private static List<Transaction> cartDetails = new ArrayList<>();
    private HistoryRVAdapter cartAdapter;
    public LoyaltyProgramFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_loyalty_program, container, false);

        earnedTV = view.findViewById(R.id.earned_TV);
        redeemedTV = view.findViewById(R.id.redeemed_TV);
        balancedTV = view.findViewById(R.id.balanced_TV);
        benefit = view.findViewById(R.id.benefit);
        memberStatus = view.findViewById(R.id.member_status);
        memberIV = view.findViewById(R.id.member_IV);
        his1 = view.findViewById(R.id.his1);
        his2 = view.findViewById(R.id.his2);
        mem1 = view.findViewById(R.id.mem1);
        mem2 = view.findViewById(R.id.mem2);
        up1 = view.findViewById(R.id.up1);
        up2 = view.findViewById(R.id.up2);
        history = view.findViewById(R.id.history);
        member = view.findViewById(R.id.member_ship);
        history_LL = view.findViewById(R.id.history_LL);
        lin1 = view.findViewById(R.id.lin1);
        lin2 = view.findViewById(R.id.lin2);
        lin3 = view.findViewById(R.id.lin3);
        lin4 = view.findViewById(R.id.lin4);
        upgrade = view.findViewById(R.id.upgrade);
        cartAdapter = new HistoryRVAdapter(getActivity(), cartDetails);

        RecyclerView recyclerView1 = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(cartAdapter);
        recyclerView1.setNestedScrollingEnabled(false);

        view.findViewById(R.id.history).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             if(his1.getVisibility() == View.VISIBLE)
             {
                 his1.setVisibility(View.GONE);
                 his2.setVisibility(View.VISIBLE);
                 history_LL.setVisibility(View.VISIBLE);

             }
             else if(his2.getVisibility() == View.VISIBLE)
             {
                 his1.setVisibility(View.VISIBLE);
                 his2.setVisibility(View.GONE);
                 history_LL.setVisibility(View.GONE);
             }

            }
        });

        view.findViewById(R.id.member_ship).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mem1.getVisibility() == View.VISIBLE)
                {
                    mem1.setVisibility(View.GONE);
                    mem2.setVisibility(View.VISIBLE);
                    benefit.setVisibility(View.VISIBLE);

                }
                else if(mem2.getVisibility() == View.VISIBLE)
                {
                    mem1.setVisibility(View.VISIBLE);
                    mem2.setVisibility(View.GONE);
                    benefit.setVisibility(View.GONE);
                }

            }
        });

        view.findViewById(R.id.upgrade).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(up1.getVisibility() == View.VISIBLE)
                {
                    up1.setVisibility(View.GONE);
                    up2.setVisibility(View.VISIBLE);
                    lin1.setVisibility(View.VISIBLE);
                    lin2.setVisibility(View.VISIBLE);
                    lin3.setVisibility(View.VISIBLE);
                    lin4.setVisibility(View.VISIBLE);

                }
                else if(up2.getVisibility() == View.VISIBLE)
                {
                    up1.setVisibility(View.VISIBLE);
                    up2.setVisibility(View.GONE);
                    lin1.setVisibility(View.GONE);
                    lin2.setVisibility(View.GONE);
                    lin3.setVisibility(View.GONE);
                    lin4.setVisibility(View.GONE);
                }

            }
        });

        view.findViewById(R.id.member_ship).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mem1.getVisibility() == View.VISIBLE)
                {
                    mem1.setVisibility(View.GONE);
                    mem2.setVisibility(View.VISIBLE);
                    benefit.setVisibility(View.VISIBLE);

                }
                else if(mem2.getVisibility() == View.VISIBLE)
                {
                    mem1.setVisibility(View.VISIBLE);
                    mem2.setVisibility(View.GONE);
                    benefit.setVisibility(View.GONE);
                }

            }
        });



        loyaltyCall();
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("My Reward Points");
        HomeActivity.enableViews(true);
    }

    private void loyaltyCall(){
        String userId = App.pref.getString(Constant.USER_ID,"");
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().loyaltyCall(AppConfig.setRequestBody(userId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Loyalty points",responseData);
                        JSONObject object  = new JSONObject(responseData);
                        if (object.getString("status").equals("1")){
                            LoyaltyData loyaltyData = new Gson().fromJson(responseData,LoyaltyData.class);
                            String earnPoint = String.valueOf(loyaltyData.getEarnPoint());
                            String redeemedPoint = String.valueOf(loyaltyData.getRedeamPoint());
                            String balancePoint = String.valueOf(loyaltyData.getBalancePoint());

                            
                            cartDetails = loyaltyData.getTransaction();
                            cartAdapter.updateList(cartDetails);

                            earnedTV.setText(earnPoint);
                            redeemedTV.setText(redeemedPoint);
                            balancedTV.setText(balancePoint);


                            View rightView = view.findViewById(R.id.right_View);
                            View leftView = view.findViewById(R.id.left_View);
                            float leftWeight = 1000-loyaltyData.getEarnPoint();
                            float rightWeight = 1000-leftWeight;
                            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rightView.getLayoutParams();
                            params.weight = rightWeight;
                            rightView.setLayoutParams(params);

                            LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) leftView.getLayoutParams();
                            param.weight = leftWeight;
                            leftView.setLayoutParams(param);

                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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
