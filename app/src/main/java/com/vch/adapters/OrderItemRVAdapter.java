package com.vch.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.bean.ProductItem;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class OrderItemRVAdapter extends RecyclerView.Adapter<OrderItemRVAdapter.MyViewHolder> {

    Activity activity;
    private List<ProductItem> results;

    public OrderItemRVAdapter(List<ProductItem> results) {
        this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order_history_item, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.name.setText(results.get(position).getPname());
        holder.quantity.setText(String.format("%s Qty", results.get(position).getProductQty()));


        Float total = null;
        try {
            total = Integer.parseInt(results.get(position).getProductQty())*Float.parseFloat(results.get(position).getProductPrice());
        } catch (NumberFormatException e) {
            total = 0f;
            e.printStackTrace();
        }
        String s = String.format("%.2f", total);
        holder.price.setText(String.format(" %s", s));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, quantity, price;

        public MyViewHolder(final View view) {
            super(view);
            name = view.findViewById(R.id.menu_name);
            quantity = view.findViewById(R.id.quantity);
            price = view.findViewById(R.id.price);
        }
    }
}
