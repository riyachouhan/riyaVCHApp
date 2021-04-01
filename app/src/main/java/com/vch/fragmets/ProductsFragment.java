package com.vch.fragmets;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.MenuArray;
import com.vch.bean.ProductDetail;
import com.vch.response.SearchDATA;
import com.vch.utiles.App;
import com.vch.utiles.AppConfig;
import com.vch.utiles.Constant;
import com.vch.utiles.DataHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProductsFragment extends Fragment {

    private static List<MenuArray> menuArrays = new ArrayList<>();
    private static List<ProductDetail> productDetails = new ArrayList<>();
    private static String cat_id;
    static TextView badgeTV,totalTV;
    private TabLayout tabLayout;
    private ProductRVAdapter adapter;
    public ProductsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_products, container, false);

        if (getArguments()!=null){

            cat_id = getArguments().getString("catId");
            menuArrays = getArguments().getParcelableArrayList("menuList");


            tabLayout = (TabLayout) view.findViewById(R.id.tabs);
            setupViewPager();

            tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                @Override
                public void onTabSelected(TabLayout.Tab tab) {
                    int right = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).getRight();
                    tabLayout.scrollTo(right,0);
           /*         tabLayout.getTabAt(tabLayout.getSelectedTabPosition()).select();*/
                    cat_id = menuArrays.get(tabLayout.getSelectedTabPosition()).getId();
                    productDetailCall(getActivity());

                }

                @Override
                public void onTabUnselected(TabLayout.Tab tab) {

                }

                @Override
                public void onTabReselected(TabLayout.Tab tab) {

                }
            });

            badgeTV = view.findViewById(R.id.badge_TV);
            totalTV = view.findViewById(R.id.cart_total_TV);
            cat_id = getArguments().getString("catId");
            int i = 0;
            assert cat_id != null;

            for (int j=0;j<menuArrays.size();j++){
                if (cat_id.equals(menuArrays.get(j).getId())){
                    i=j;
                }
            }

            int right = ((ViewGroup) tabLayout.getChildAt(0)).getChildAt(tabLayout.getSelectedTabPosition()).getRight();
            tabLayout.scrollTo(right,0);

            final int finalI = i;
            new Handler().postDelayed(
                    new Runnable() {
                        @Override public void run() {
                            tabLayout.getTabAt(finalI).select();
                        }
                    }, 100);


            adapter = new ProductRVAdapter(getActivity(),productDetails, "PRODUCT ₹ ");

            RecyclerView recyclerView1 = view.findViewById(R.id.recycler_view_p);
            RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
            recyclerView1.setLayoutManager(mLayoutManager1);
            recyclerView1.setItemAnimator(new DefaultItemAnimator());
            recyclerView1.setAdapter(adapter);
            recyclerView1.setNestedScrollingEnabled(false);
            setTotal(DataHolder.getCount(),DataHolder.getSubTotal());

            view.findViewById(R.id.cart_total_TV).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!badgeTV.getText().toString().equals("0"))
                    HomeActivity.addFragment(new CartFragment(),true);
                    else
                        AppConfig.showToast("Your Cart is Empty.");
                    // recyclerView.getLayoutManager().scrollToPosition(mLayoutManager.findLastVisibleItemPosition() - 1);
                }
            });
            view.findViewById(R.id.icon_RL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!badgeTV.getText().toString().equals("0"))
                        HomeActivity.addFragment(new CartFragment(),true);
                    else
                        AppConfig.showToast("Add items to cart.");
                    // recyclerView.getLayoutManager().scrollToPosition(mLayoutManager.findLastVisibleItemPosition() - 1);
                }
            });



            productDetailCall(getActivity());

        }
        return view;
    }
    private void setupViewPager() {

        for (int i=0;i<menuArrays.size();i++){
            MenuArray menuArray = menuArrays.get(i);
            tabLayout.addTab(tabLayout.newTab().setText(menuArray.getName()));
        }

    }
    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("");
        HomeActivity.enableViews(true);
    }


    public static Fragment newInstance(String id, List<MenuArray> results) {
        ProductsFragment fragment = new ProductsFragment();

        Bundle arg = new Bundle();
        arg.putString("catId",id);
        arg.putParcelableArrayList("menuList", (ArrayList<? extends Parcelable>) results);
        fragment.setArguments(arg);
        return fragment;
    }

    public  void productDetailCall(Activity activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
        String regId = App.pref.getString(Constant.USER_ID,"");
        Log.e("USER_ID","USER_ID"+regId);
        Log.e("Cat_ID","Cat_ID"+cat_id);

        Call<ResponseBody> call = AppConfig.getLoadInterface().productsCall(AppConfig.setRequestBody(cat_id),AppConfig.setRequestBody(regId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()){
                    try {
                        productDetails = new ArrayList<>();
                        String responseData = response.body().string();
                        Log.e("home_response",responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")){

                            SearchDATA homeResponse = new Gson().fromJson(responseData,SearchDATA.class);
                            productDetails = homeResponse.getMsg();
                        }else {
                            productDetails = new ArrayList<>();
                            AppConfig.showToast(object.getString("₹msg "));
                        }
                    } catch (IOException e) {
                        productDetails = new ArrayList<>();
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        productDetails = new ArrayList<>();
                        e.printStackTrace();
                    } catch (JSONException e) {
                        productDetails = new ArrayList<>();
                        e.printStackTrace();
                    }
                    adapter.updateList(productDetails);
                }else {
                    AppConfig.showToast("Something went wrong");
                }
                AppConfig.hideLoading(dialog);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                productDetails = new ArrayList<>();

                AppConfig.hideLoading(dialog);
                t.printStackTrace();
            }
        });
    }


    public  void setTotal(String cart_count, String sub_total) {

        try {
            if (badgeTV!=null&&totalTV!=null){
                System.out.println(cart_count);
                System.out.println(sub_total);
                badgeTV.setText(cart_count);
                badgeTV.setVisibility(View.VISIBLE);
                totalTV.setText(String.format("Cart Total:  %s ₹", sub_total));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class ProductRVAdapter extends RecyclerView.Adapter<ProductRVAdapter.MyViewHolder> {

        private Activity activity;
        private List<ProductDetail> results;
        private String tag;
        public ProductRVAdapter(Activity mContext, List<ProductDetail> productDetails, String tag) {
            this.activity = mContext;
            this.results = productDetails;
            this.tag = tag;
        }

        @Override
        public ProductRVAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_product, parent, false);

            return new ProductRVAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final ProductRVAdapter.MyViewHolder holder, final int position) {


            holder.titleTV.setText(results.get(position).getPName());
            holder.priceTV.setText(results.get(position).getProductPrice());
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
            }else {
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
                label = view.findViewById(R.id.label);
                removeCartIV = view.findViewById(R.id.remove_cart_IV);
                quantityTV = view.findViewById(R.id.cart_quantity_TV);
                cartLL = view.findViewById(R.id.cart_LL);
                cartBT = view.findViewById(R.id.add_cart_button);
                cartBT.setOnClickListener(this);
                addCartIV.setOnClickListener(this);
                removeCartIV.setOnClickListener(this);
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
                                DataHolder.setCount(object.getString("cart_count"));
                                DataHolder.setSubTotal(object.getString("sub_total"));
                                setTotal(object.getString("cart_count"),object.getString("sub_total"));
                                productDetailCall(activity);
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

            public CustomGridLayoutManager(Context context, int i) {
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
}
