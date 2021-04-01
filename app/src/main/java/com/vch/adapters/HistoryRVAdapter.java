package com.vch.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vch.R;
import com.vch.bean.Transaction;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class HistoryRVAdapter extends RecyclerView.Adapter<HistoryRVAdapter.MyViewHolder> {

    Activity activity;
    private List<Transaction> results;

    public HistoryRVAdapter(Activity mContext, List<Transaction> results) {
        this.activity = mContext;
        this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.sNoTV.setText(String.valueOf(position + 1));
        holder.orderIdTV.setText(results.get(position).getOrderId());
        /*if (results.get(position).getType().equals("credit"))
            holder.orderAmountTV.setText(results.get(position).getCreditAmount());
        else*/
            holder.orderAmountTV.setText(results.get(position).getAmount());
        holder.typeTV.setText(results.get(position).getType());
        holder.remarkTV.setText(results.get(position).getRemark());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView sNoTV, orderIdTV, orderAmountTV, typeTV, remarkTV;

        public MyViewHolder(final View view) {
            super(view);
            sNoTV = view.findViewById(R.id.s_no_TV);
            orderIdTV = view.findViewById(R.id.order_id_TV);
            orderAmountTV = view.findViewById(R.id.order_amt_TV);
            typeTV = view.findViewById(R.id.type_TV);
            remarkTV = view.findViewById(R.id.remark_TV);
        }
    }

    public void updateList(List<Transaction> transactions) {
        results = transactions;
        notifyDataSetChanged();
    }
}
