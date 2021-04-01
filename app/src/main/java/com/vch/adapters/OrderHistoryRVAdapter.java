package com.vch.adapters;

import android.app.Activity;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.OrderHistory;
import com.vch.fragmets.OrderFragment;
import com.vch.utiles.DataHolder;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class OrderHistoryRVAdapter extends RecyclerView.Adapter<OrderHistoryRVAdapter.MyViewHolder> {

    Activity activity;
    private List<OrderHistory> results;
    public OrderHistoryRVAdapter(Activity mContext, List<OrderHistory> historyList) {
        this.activity = mContext;
        this.results = historyList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tiffin, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        OrderHistory lastOrderItemList;
        lastOrderItemList = results.get(position);

        holder.snoTV.setText(String.format("%d", position + 1));
        holder.orderIdTextView.setText(String.format("%s", lastOrderItemList.getOrderId()));
        holder.dateTextView.setText(lastOrderItemList.getOrderTotal());
/*
        holder.totalOrderPriceTextView.setText(lastOrderItemList.getSubTotal());
        holder.totalBillPriceTextView.setText(lastOrderItemList.getOrderTotal());
        OrderItemRVAdapter mAdapter = new OrderItemRVAdapter(lastOrderItemList.getProductItems());
        CustomGridLayoutManager layoutManager  = new CustomGridLayoutManager(activity);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);*/
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<OrderHistory> historyList) {
        results = historyList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderIdTextView, dateTextView,snoTV;


        public MyViewHolder(final View view) {
            super(view);
            orderIdTextView = view.findViewById(R.id.order_id_TV);
            dateTextView = view.findViewById(R.id.order_amount_TV);
            snoTV = view.findViewById(R.id.s_no_TV);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataHolder.setOrderHistory(results.get(getAdapterPosition()));
                    HomeActivity.addFragment(new OrderFragment(),true);
                }
            });
           /* orderTypeTextView = view.findViewById(R.id.order_type_last_order_item);
            totalOrderPriceTextView = view.findViewById(R.id.total_order_price);
            totalBillPriceTextView = view.findViewById(R.id.total_bill_order_price);
            recyclerView = view.findViewById(R.id.item_list_last_order_item);*/
            /*trackOrderBT = view.findViewById(R.id.track_order_BT);
            trackOrderBT.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    OrderHistory orderHistory = results.get(getAdapterPosition());

                    //HomeActivity.addFragment(TrackOrderFragment.newInstance(orderHistory),true);
                }
            });*/
        }
    }
   /* public class CustomGridLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context) {
            super(context);
        }

        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            //Similarly you can customize "canScrollHorizontally()" for managing horizontal scroll
            return isScrollEnabled && super.canScrollVertically();
        }
    }*/
}
