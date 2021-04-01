package com.vch.adapters;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.MenuArray;
import com.vch.fragmets.ProductsFragment;

import java.util.List;

/**
 * Created by pintu22 on 21/8/17.
 */

public class HomeRVAdapter extends RecyclerView.Adapter<HomeRVAdapter.MyViewHolder> {

    private Activity activity;
    private List<MenuArray> results;

    public HomeRVAdapter(Activity mContext, List<MenuArray> results) {
        this.activity = mContext;
        this.results = results;

    }

    @Override
    public MyViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_home_lv, parent, false);

        return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if ((position % 2) == 0) {
            holder.rightLL.setVisibility(View.VISIBLE);
            holder.leftLL.setVisibility(View.GONE);
            holder.titleRightTV.setText(results.get(position).getName());
            Glide.with(activity).asBitmap().load(results.get(position).getCategoryImage()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap res, Transition<? super Bitmap> transition) {
                    try {
                        Drawable drawable = new BitmapDrawable(activity.getResources(), res);
                        holder.RightIV.setBackground(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });

        } else {
            holder.leftLL.setVisibility(View.VISIBLE);
            holder.rightLL.setVisibility(View.GONE);
            holder.titleLeftTV.setText(results.get(position).getName());
            Glide.with(activity).asBitmap().load(results.get(position).getCategoryImage()).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(Bitmap res, Transition<? super Bitmap> transition) {
                    try {
                        Drawable drawable = new BitmapDrawable(activity.getResources(), res);
                        holder.LeftIV.setBackground(drawable);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<MenuArray> menuArrays) {
        results = menuArrays;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView titleLeftTV, titleRightTV;
        private ImageView LeftIV, RightIV;
        private LinearLayout rightLL, leftLL;

        public MyViewHolder(final View view) {
            super(view);

            rightLL = view.findViewById(R.id.right_LL);
            titleRightTV = view.findViewById(R.id.right_TV);
            RightIV = view.findViewById(R.id.right_IV);
            leftLL = view.findViewById(R.id.left_LL);
            titleLeftTV = view.findViewById(R.id.left_TV);
            LeftIV = view.findViewById(R.id.left_IV);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HomeActivity.addFragment(ProductsFragment.newInstance(
                            results.get(getAdapterPosition()).getId(), results), true);
                }
            });
        }
    }

}
