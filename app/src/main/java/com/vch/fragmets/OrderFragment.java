package com.vch.fragmets;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.adapters.OrderItemRVAdapter;
import com.vch.utiles.AppConfig;
import com.vch.utiles.DataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class OrderFragment extends Fragment {


    public OrderFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        TextView orderId = view.findViewById(R.id.order_id_TV);
        TextView name = view.findViewById(R.id.name_TV);
        TextView orderAmount = view.findViewById(R.id.order_amount_TV);
        TextView orderDate = view.findViewById(R.id.order_date_TV);

        TextView deliveryDate = view.findViewById(R.id.delivery_date_TV);
        TextView orderStatus = view.findViewById(R.id.order_status_TV);
        TextView subtotal = view.findViewById(R.id.sub_total_TV);
        TextView redeemed = view.findViewById(R.id.redeemed_TV);
        TextView coupon = view.findViewById(R.id.coupon_TV);
        TextView taxable = view.findViewById(R.id.taxable_TV);
        TextView cgst = view.findViewById(R.id.cgst_TV);
        TextView sgst = view.findViewById(R.id.sgst_TV);
        TextView grandTotal = view.findViewById(R.id.grand_total_TV);
        final Button addReview = view.findViewById(R.id.submit_BT);

        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());

                // ...Irrelevant code for customizing the buttons and title
                LayoutInflater inflater = getActivity().getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.dialog_review, null);
                dialogBuilder.setView(dialogView);
                final EditText editText = (EditText) dialogView.findViewById(R.id.review_ET);
                final RatingBar ratingBar = (RatingBar) dialogView.findViewById(R.id.ratingbar);
                dialogBuilder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                             addReviewCall(HomeActivity.detail.getUserId(),editText.getText().toString(),ratingBar.getRating());
                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                             dialog.dismiss();
                    }
                });


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();
            }
        });

        orderId.setText(DataHolder.getOrderHistory().getOrderId());
        name.setText(HomeActivity.detail.getUserName());
        orderAmount.setText(DataHolder.getOrderHistory().getOrderTotal());
        orderDate.setText(DataHolder.getOrderHistory().getOrderTime());

        deliveryDate.setText(DataHolder.getOrderHistory().getDeliveryDate());
        String myString =DataHolder.getOrderHistory().getOrderStatus();
        orderStatus.setText(String.format("%s%s", myString.substring(0, 1).toUpperCase(), myString.substring(1)));
        subtotal.setText(DataHolder.getOrderHistory().getSubTotal());
        redeemed.setText(DataHolder.getOrderHistory().getRedeamPoint());
        coupon.setText(DataHolder.getOrderHistory().getCouponDiscount());
        taxable.setText(DataHolder.getOrderHistory().getTaxableAmount());
        cgst.setText(DataHolder.getOrderHistory().getTaxAmount());
        sgst.setText(DataHolder.getOrderHistory().getTaxAmount());
        grandTotal.setText(DataHolder.getOrderHistory().getOrderTotal());

        OrderItemRVAdapter mAdapter = new OrderItemRVAdapter(DataHolder.getOrderHistory().getProductItems());

        RecyclerView recyclerView = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setAdapter(mAdapter);

        return view;
    }

    private void addReviewCall(String userId, String review, float rating) {

        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().addReviewCall(
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(String.valueOf(rating)),
                AppConfig.setRequestBody(review));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);

                        if (object.getString("status").equals("1")){
                            AppConfig.showToast("Review is sent successful.");


                            // HomeActivity.addFragment(new MyAddressFragment(),false);
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
