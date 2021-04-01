package com.vch.adapters;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.vch.R;

/**
 * Created by pintu22 on 21/8/17.
 */

public class FeedbackRVAdapter extends RecyclerView.Adapter<FeedbackRVAdapter.MyViewHolder> {

    Activity activity;
    //private List<GetActivityResult> results;
    public static int size = 6;
    public FeedbackRVAdapter(Activity mContext) {
        this.activity = mContext;
       // this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_feedback, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView cardView;

        public MyViewHolder(final View view) {
            super(view);
        }
    }
}
