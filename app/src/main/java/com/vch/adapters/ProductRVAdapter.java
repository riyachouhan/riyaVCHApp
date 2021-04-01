package com.vch.adapters;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.bean.ProductDetail;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by pintu22 on 21/8/17.
 */

public class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.MyViewHolder> {

    private Activity activity;
    private List<ProductDetail> results;
    private String tag;
    public ProductRVAdapter(Activity mContext, List<ProductDetail> productDetails, String tag) {
        this.activity = mContext;
       this.results = productDetails;
       this.tag = tag;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);

            return new MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {


        holder.titleTV.setText(results.get(position).getPName());

        if (results.get(position).getDiscountPrice().equals("0"))
            holder.priceTV.setText(results.get(position).getProductPrice());
        else
            holder.priceTV.setText(results.get(position).getDiscountPrice());

        holder.quantityTV.setText(results.get(position).getQuantity());
        if (results.get(position).getQuantity().equals("0")){
            holder.cartLL.setVisibility(View.GONE);
            holder.cartBT.setVisibility(View.VISIBLE);

        }else {
            holder.cartLL.setVisibility(View.VISIBLE);
            holder.cartBT.setVisibility(View.GONE);
        }


        if (results.get(position).getProductLabel().equals("")){
            holder.label.setVisibility(View.GONE);

        } else {
            holder.label.setVisibility(View.VISIBLE);
            holder.label.setText(results.get(position).getProductLabel());
        }

        Glide.with(activity).asBitmap().load(results.get(position).getImageUrl()).into(new SimpleTarget<Bitmap>() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onResourceReady(Bitmap res, Transition<? super Bitmap> transition) {
                try {
                    Drawable drawable = new BitmapDrawable(activity.getResources(), res);
                    holder.imageIV.setBackground(drawable);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        /*IngredientsRVAdapter mAdapter = new IngredientsRVAdapter(activity);
        CustomGridLayoutManager layoutManager  = new CustomGridLayoutManager(activity,2);
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(mAdapter);*/
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<ProductDetail> productDetails) {
        results = productDetails;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView titleTV,priceTV,quantityTV,addCartIV,removeCartIV,label;
        ImageView imageIV;
        RecyclerView recyclerView;
        LinearLayout cartLL;
        Button cartBT;
        public MyViewHolder(final View view) {
            super(view);

            recyclerView = view.findViewById(R.id.recycler_view);
            titleTV = view.findViewById(R.id.product_name_TV);
            priceTV = view.findViewById(R.id.product_price_TV);
            imageIV = view.findViewById(R.id.product_IV);
            addCartIV = view.findViewById(R.id.add_cart_IV);
            removeCartIV = view.findViewById(R.id.remove_cart_IV);
            quantityTV = view.findViewById(R.id.cart_quantity_TV);
            cartLL = view.findViewById(R.id.cart_LL);
            label = view.findViewById(R.id.label);
            cartBT = view.findViewById(R.id.add_cart_button);
            cartBT.setOnClickListener(this);
            addCartIV.setOnClickListener(this);
            removeCartIV.setOnClickListener(this);
            /*view.setOnClickListener(new View.OnClickListener() {
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

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.add_cart_button:
                    updateCartCall(results.get(getAdapterPosition()).getProductId(),
                            App.pref.getString(Constant.USER_ID,""),"add"
                            ,results.get(getAdapterPosition()).getQuantity(),getAdapterPosition());
                    break;
                case R.id.add_cart_IV:
                    updateCartCall(results.get(getAdapterPosition()).getProductId(),
                            App.pref.getString(Constant.USER_ID,""),"add"
                            ,results.get(getAdapterPosition()).getQuantity(),getAdapterPosition());
                    break;
                case R.id.remove_cart_IV:
                    updateCartCall(results.get(getAdapterPosition()).getProductId(),
                            App.pref.getString(Constant.USER_ID,""),"delete"
                            ,results.get(getAdapterPosition()).getQuantity(),getAdapterPosition());
                    break;
            }
        }
    }

    private void updateCartCall(String productId, String userId, String status, String quantity, final int position) {
        String Quantity ;

        if (status.equals("add")){
            int count = Integer.parseInt(quantity);
            count=count+1;
            quantity = String.valueOf(count);
            Quantity = String.valueOf(count);
        }else {
            if (!quantity.equals("0")){
                int count = Integer.parseInt(quantity);
                count=count-1;
                Quantity = String.valueOf(count);
            }else {
                Quantity = quantity;
            }
        }
        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().updateCartCall(AppConfig.setRequestBody(productId),
                AppConfig.setRequestBody(userId),
                AppConfig.setRequestBody(status),
                AppConfig.setRequestBody(quantity));

        final String finalQuantity = Quantity;
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        String responseData = response.body().string();
                        Log.e("Cart_Data",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){
                            AppConfig.showToast(object.getString("msg"));
                          /*  if (tag.equals("PRODUCT")){

                                DataHolder.setCount(object.getString("cart_count"));
                                DataHolder.setSubTotal(object.getString("sub_total"));
                                ProductsFragment.setTotal(object.getString("cart_count"),object.getString("sub_total"));
                                ProductsFragment.productDetailCall(activity);
                            }else if (tag.equals("CART")){
                                CartFragment.myCartCall(activity);
                            }*/
                            results.get(position).setQuantity(finalQuantity);
                            notifyDataSetChanged();
                        }else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    AppConfig.showToast("Something went wrong");
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


    public class CustomGridLayoutManager extends GridLayoutManager {
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
    }
}
