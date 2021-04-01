package com.vch.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.TiffinOrder;
import com.vch.fragmets.TifinDetailFragment;
import com.vch.utiles.DataHolder;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class IngredientsRVAdapter extends RecyclerView.Adapter<IngredientsRVAdapter.MyViewHolder> {

    Activity activity;
    private List<TiffinOrder> results;

    public IngredientsRVAdapter(Activity mContext,List<TiffinOrder> results) {
        this.activity = mContext;
        this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_tiffin, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.orderTv.setText(results.get(position).getTiffinOrderId());
        holder.amountTV.setText(results.get(position).getPlanTotalAmount());
        holder.snoTV.setText(String.format("%d", position + 1));
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<TiffinOrder> historyList) {
        results = historyList;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView orderTv,amountTV,snoTV;


        public MyViewHolder(final View view) {
            super(view);
            orderTv = view.findViewById(R.id.order_id_TV);
            amountTV = view.findViewById(R.id.order_amount_TV);
            snoTV = view.findViewById(R.id.s_no_TV);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DataHolder.setTiffinDetails(results.get(getAdapterPosition()));
                    HomeActivity.addFragment(new TifinDetailFragment(),true);
                }
            });

        }
    }
}
