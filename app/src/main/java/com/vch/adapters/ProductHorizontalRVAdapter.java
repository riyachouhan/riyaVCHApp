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

public class ProductHorizontalRVAdapter extends RecyclerView.Adapter<ProductHorizontalRVAdapter.MyViewHolder> {

    Activity activity;
    //private List<GetActivityResult> results;
    public static int size = 6;
    public ProductHorizontalRVAdapter(Activity mContext) {
        this.activity = mContext;
       // this.results = results;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product_horizontal, parent, false);

            return new MyViewHolder(itemView);

    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
      /*  IngredientsRVAdapter mAdapter = new IngredientsRVAdapter(activity);
        CustomGridLayoutManager layoutManager  = new CustomGridLayoutManager(activity,2);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);*/
    }

    @Override
    public int getItemCount() {
        return size;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView titleTV;
        ImageView cardView;
        RecyclerView recyclerView;
        public MyViewHolder(final View view) {
            super(view);

            recyclerView = view.findViewById(R.id.recycler_view);

           /* view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (view.findViewById(R.id.ingredients_LL).getVisibility()==View.GONE){
                        view.findViewById(R.id.ingredients_LL).setVisibility(View.VISIBLE);
                    }else {
                        view.findViewById(R.id.ingredients_LL).setVisibility(View.GONE);
                    }
                }
            });*/
        }
    }

    /*public class CustomGridLayoutManager extends GridLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomGridLayoutManager(Context context,int i) {
            super(context,i);
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
