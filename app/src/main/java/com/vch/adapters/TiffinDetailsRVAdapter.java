package com.vch.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.bean.UserMealDetail;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class TiffinDetailsRVAdapter extends RecyclerView.Adapter<TiffinDetailsRVAdapter.MyViewHolder> {

    Activity activity;
    private List<UserMealDetail> results;

    public TiffinDetailsRVAdapter(Activity mContext, List<UserMealDetail> results) {
        this.activity = mContext;
        this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tiffin_details, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.date.setText(results.get(position).getDayDate());
        holder.quantity.setText(results.get(position).getMealQtyNo());
        holder.status.setText(results.get(position).getDeliverStatus());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView date,quantity,status;


        public MyViewHolder(final View view) {
            super(view);
            date = view.findViewById(R.id.tiffin_date_TV);
            quantity = view.findViewById(R.id.tiffin_quantity_TV);
            status = view.findViewById(R.id.tiffin_status_TV);

        }
    }
}
