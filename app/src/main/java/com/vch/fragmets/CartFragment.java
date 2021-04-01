package com.vch.fragmets;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.vch.R;
import com.vch.activities.HomeActivity;
import com.vch.bean.ProductDetail;
import com.vch.response.CouponData;
import com.vch.response.MyCartData;
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

import static androidx.recyclerview.widget.LinearLayoutManager.HORIZONTAL;
import static com.vch.fragmets.ProductsFragment.totalTV;

/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    AlertDialog.Builder mealPlanBuilder;
    private static List<ProductDetail> productDetails = new ArrayList<>();
    private static List<ProductDetail> cartDetails = new ArrayList<>();
    // private static List<Coupon> couponData = new ArrayList<>();
    private ProductRVAdapter adapter;
    private CartRVAdapter cartAdapter;
    private TextView subtotalTV, cgstTV, sgstTV, grandTotalTV, badgeTV, cartTotalTV, redeemedTotalTV, couponTV, taxAmountTV, spiceLevelTV;
    private EditText nameTV, commentET;
    private double grandTotalD;
    private RadioButton redeemedRB, couponRB;
    private RadioGroup radioGroup;
    private List<String> typeList = new ArrayList<>();
    private Button couponBT;
    private LinearLayout couponLL, couponTotalLL,shippingcharge;
    private String subTotalStr, countStr, grandTotalStr, redeemedPointStr = "0", couponCodeStr = "", couponDiscountStr = "0", cashBackStr = "", taxAmountStr = "0", spicyLevelStr = "", commentStr;
    private Integer grandtotalInt;
    TextView cartd_not_item;
    LinearLayout whole_layout,checkout_LL;

    public CartFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_cart, container, false);
        cartd_not_item=view.findViewById(R.id.cartd_not_item);
        whole_layout=view.findViewById(R.id.whole_layout);
        checkout_LL=view.findViewById(R.id.checkout_LL);
        mealPlanBuilder = new AlertDialog.Builder(getActivity());
        subtotalTV = view.findViewById(R.id.sub_total_TV);
        cgstTV = view.findViewById(R.id.c_g_s_t_TV);
        sgstTV = view.findViewById(R.id.s_g_s_t_TV);
        taxAmountTV = view.findViewById(R.id.tax_amount_TV);
        grandTotalTV = view.findViewById(R.id.grand_total_TV);
        couponTV = view.findViewById(R.id.coupon_total_TV);
        redeemedTotalTV = view.findViewById(R.id.redeemed_total_TV);
        badgeTV = view.findViewById(R.id.badge_TV);
        couponTotalLL = view.findViewById(R.id.coupon_total_LL);
        commentET = view.findViewById(R.id.comment_ET);
        spiceLevelTV = view.findViewById(R.id.select_spicy_level_TV);
        shippingcharge = view.findViewById(R.id.Shipping_cost);

        cartTotalTV = view.findViewById(R.id.cart_total_TV);
        couponBT = view.findViewById(R.id.coupon_BT);
        couponLL = view.findViewById(R.id.coupon_LL);

        radioGroup = view.findViewById(R.id.radiogroup);
        redeemedRB = view.findViewById(R.id.redeemed_RB);
        couponRB = view.findViewById(R.id.coupon_RB);

        typeList.add("Low");
        typeList.add("Medium");
        typeList.add("High");


        spiceLevelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mealPlanBuilder.setTitle("Select ");

                final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.select_dialog_singlechoice);
                for (String s : typeList) {
                    arrayAdapter.add(s);
                }


                mealPlanBuilder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                mealPlanBuilder.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String strName = arrayAdapter.getItem(which);
                        if (strName != null) {
                            spiceLevelTV.setText(strName);
                            spicyLevelStr = strName;
                        }
                    }
                });
                mealPlanBuilder.show();
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                if (selectedId == R.id.redeemed_RB) {

                    couponLL.setVisibility(View.GONE);

                    grandTotalTV.setText(subTotalStr);
                    nameTV.setText("");
                    cartTotalTV.setText(String.format(" items for  %s ₹", subTotalStr));
                    couponDiscountStr = "0";
                    couponCodeStr = "";
                    couponTV.setText("0.0");

                    double redeemed = 0;
                    try {

                        redeemedPointStr = redeemedTotalTV.getText().toString();
                        redeemed = Double.parseDouble(redeemedPointStr);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    grandTotalD = Double.parseDouble(subTotalStr);
                    if (grandTotalD > redeemed) grandTotalD = grandTotalD - redeemed;
                    grandTotalStr = String.valueOf(grandTotalD);
                    grandTotalTV.setText(grandTotalStr);

                    cartTotalTV.setText(String.format(" items for  %s ₹", String.valueOf(grandTotalD)));

                    double taxAmountD = grandTotalD - (getCGST(grandTotalD) * 2);
                    taxAmountTV.setText(String.valueOf(taxAmountD));
                    cgstTV.setText(String.valueOf(getCGST(grandTotalD)));
                    sgstTV.setText(String.valueOf(getCGST(grandTotalD)));
                }

                if (selectedId == R.id.coupon_RB) {
                    redeemedPointStr = "0";
                    couponBT.setText("Apply");
                    grandTotalTV.setText(subTotalStr);
                    grandTotalD = Double.parseDouble(subTotalStr);
                    couponLL.setVisibility(View.VISIBLE);
                    double taxAmountD = grandTotalD - (getCGST(grandTotalD) * 2);
                    taxAmountTV.setText(String.valueOf(taxAmountD));
                    cgstTV.setText(String.valueOf(getCGST(grandTotalD)));
                    sgstTV.setText(String.valueOf(getCGST(grandTotalD)));
                    cartTotalTV.setText(String.format(" items for  %s ₹", subTotalStr));
                }
            }
        });

        cartAdapter = new CartRVAdapter(getActivity(), cartDetails);
        RecyclerView recyclerView1 = view.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView1.setLayoutManager(mLayoutManager1);
        recyclerView1.setItemAnimator(new DefaultItemAnimator());
        recyclerView1.setAdapter(cartAdapter);
        recyclerView1.setNestedScrollingEnabled(false);
        //autoCompleteAdapter = new CouponACTVAdapter(getActivity(), R.layout.autocompleteitem, coupons);

        adapter = new ProductRVAdapter(getActivity(), productDetails, "CART");
        final RecyclerView recyclerView = view.findViewById(R.id.recycler_view_p);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHorizontalScrollBarEnabled(true);
        nameTV = view.findViewById(R.id.auto_complete_TV);
        myCartCall(getActivity());
        view.findViewById(R.id.checkout_LL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (grandTotalD < 100) {

                    Toast.makeText(getActivity(), "Please Select Minimun Amount Should be  100 ₹", Toast.LENGTH_SHORT).show();
                } else
                {
                    if (redeemedRB.isChecked()) {
                        couponDiscountStr = "0";
                    } else if (couponRB.isChecked()) {
                        redeemedPointStr = "0";
                    }
                    commentStr = commentET.getText().toString();
                    spicyLevelStr = spiceLevelTV.getText().toString();
                    if (spicyLevelStr.equals("Select Spice Level")) {
                        spicyLevelStr = "";
                    }
                    HomeActivity.addFragment(PaymentFragment.newInstance(grandTotalStr, redeemedPointStr, couponCodeStr, couponDiscountStr, cashBackStr, subTotalStr, spicyLevelStr, commentStr), false);
                }
            }
        });
        view.findViewById(R.id.coupon_TV).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (couponLL.getVisibility() == View.GONE)
                    couponLL.setVisibility(View.VISIBLE);
                else couponLL.setVisibility(View.GONE);*/
            }
        });

        couponBT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
                String couponStr = nameTV.getText().toString();
                String subTotalStr = subtotalTV.getText().toString();
                String userId = App.pref.getString(Constant.USER_ID, "");
                applyCouponCall(userId, couponStr, subTotalStr);

            }
        });
        return view;
    }

    private void applyCouponCall(String userId, final String couponStr, final String subTotal) {
        final ProgressDialog dialog = new ProgressDialog(getActivity());
        AppConfig.showLoading(dialog);
        Call<ResponseBody> call = AppConfig.getLoadInterface().applyCouponCall(AppConfig.setRequestBody(userId), AppConfig.setRequestBody(couponStr), AppConfig.setRequestBody(subTotal));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("coupon_response", responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")) {

                            CouponData data = new Gson().fromJson(responseData, CouponData.class);
                            double couponAmt = AppConfig.stringToDouble(data.getData().getCouponAmount());
                            double subTotal1 = AppConfig.stringToDouble(subTotalStr) - couponAmt;
                            couponBT.setText("Applied");
                            couponCodeStr = data.getData().getCouponCode();
                            couponDiscountStr = data.getData().getCouponAmount();
                            cashBackStr = data.getData().getCashback();
                            subTotalStr = String.valueOf(subTotal1);
                            couponTV.setText("-" + couponAmt);
                            couponTotalLL.setVisibility(View.VISIBLE);
                            //setTotal(AppConfig.stringToDouble(subTotal), getCGST(subTotal1), getGrandTotal(subTotal1), Integer.parseInt(countStr), AppConfig.stringToDouble(taxAmountStr));
                            setTotal(AppConfig.stringToDouble(subTotal), getCGST(subTotal1), subTotal1, Integer.parseInt(countStr), AppConfig.stringToDouble(taxAmountStr));
                            double taxAmountD = grandTotalD - (getCGST(grandTotalD) * 2);
                            taxAmountTV.setText(String.valueOf(taxAmountD));
                        } else {
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
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

    private double getGrandTotal(double subTotal) {
        double cgst = getCGST(subTotal);
        return subTotal + (cgst * 2);
    }

    private double getCGST(double subTotal) {
        return ((subTotal * 2.5) / 100);
    }


    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Cart");
        HomeActivity.enableViews(true);
    }

    public void myCartCall(Activity activity) {
        final ProgressDialog dialog = new ProgressDialog(activity);
        AppConfig.showLoading(dialog);
        String regId = App.pref.getString(Constant.USER_ID, "");
        Log.e("USER_ID", "fcm_code" + regId);
        Call<ResponseBody> call = AppConfig.getLoadInterface().myCartCall(AppConfig.setRequestBody(regId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        String responseData = response.body().string();
                        Log.e("home_response", responseData);
                        JSONObject object = new JSONObject(responseData);
                        if (object.getString(Constant.STATUS).equals("1")) {
                            cartd_not_item.setVisibility(View.GONE);
                            whole_layout.setVisibility(View.VISIBLE);
                            checkout_LL.setVisibility(View.VISIBLE);
                            productDetails = new ArrayList<>();
                            cartDetails = new ArrayList<>();
                            MyCartData homeResponse = new Gson().fromJson(responseData, MyCartData.class);
                            redeemedTotalTV.setText(String.valueOf(homeResponse.getRedeamPoint()));
                            nameTV.setText("");
                            couponLL.setVisibility(View.GONE);
                            couponTV.setText("");
                            productDetails = homeResponse.getProductTreat();
                            adapter.updateList(productDetails);
                            cartDetails = homeResponse.getMsg();
                            cartAdapter.updateList(cartDetails);
                            //.....
                            DataHolder.setCount(homeResponse.getMsg().size()+"");
                            DataHolder.setSubTotal(homeResponse.getSubtotal()+"");
                            setTotal(homeResponse.getSubtotal(), homeResponse.getCgst(), homeResponse.getGrandTotal(), homeResponse.getMsg().size(), homeResponse.getTaxableAmount());
                        } else {
                            productDetails = new ArrayList<>();
                            adapter.updateList(productDetails);
                            //fro cardvalue
                            checkout_LL.setVisibility(View.GONE);
                            cartd_not_item.setVisibility(View.VISIBLE);
                            whole_layout.setVisibility(View.GONE);
                            subtotalTV.setText("0");
                            cgstTV.setText("0");
                            sgstTV.setText("0");
                            grandTotalTV.setText("0");
                            countStr = String.valueOf("0");
                            DataHolder.setCount("0");
                            DataHolder.setSubTotal("0");
                            cartTotalTV.setText(String.format(" items for %s","0"));
                            cartDetails = new ArrayList<>();
                            cartAdapter.updateList(cartDetails);
                            AppConfig.showToast(object.getString("msg"));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JsonSyntaxException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
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

    private void setTotal(Double subTotal, Double CGST, Double grandTotal, int count, Double taxAmount) {
        try {
            if (subtotalTV != null && grandTotal != null && cgstTV != null && sgstTV != null) {

                grandTotalD = grandTotal;
                subTotalStr = String.valueOf(subTotal);
                grandTotalStr = String.valueOf(grandTotal);
                taxAmountStr = String.valueOf(taxAmount);
                subtotalTV.setText(String.valueOf(subTotal));
                cgstTV.setText(String.valueOf(CGST));
                sgstTV.setText(String.valueOf(CGST));
                grandTotalTV.setText(String.valueOf(grandTotal));
                taxAmountTV.setText(taxAmountStr);
                badgeTV.setText(String.format("Checkout %s", String.valueOf(count)));
                countStr = String.valueOf(count);
                cartTotalTV.setText(String.format(" items for %s", String.valueOf(grandTotal)));
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

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);

            return new ProductRVAdapter.MyViewHolder(itemView);
        }


        @Override
        public void onBindViewHolder(final ProductRVAdapter.MyViewHolder holder, final int position) {


            holder.titleTV.setText(results.get(position).getPName());
            if (results.get(position).getDiscountPrice().equals("0"))
                holder.priceTV.setText(results.get(position).getProductPrice());
            else holder.priceTV.setText(results.get(position).getDiscountPrice());
            holder.quantityTV.setText(results.get(position).getQuantity());
            if (results.get(position).getQuantity().equals("0")) {
                holder.cartLL.setVisibility(View.GONE);
                holder.cartBT.setVisibility(View.VISIBLE);

            } else {
                holder.cartLL.setVisibility(View.VISIBLE);
                holder.cartBT.setVisibility(View.GONE);
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

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView titleTV, priceTV, quantityTV, addCartIV, removeCartIV;
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
                cartBT = view.findViewById(R.id.add_cart_button);
                cartBT.setOnClickListener(this);
                addCartIV.setOnClickListener(this);
                removeCartIV.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.add_cart_button:
                    case R.id.add_cart_IV:
                        updateCartCall(results.get(getAdapterPosition()).getProductId(), App.pref.getString(Constant.USER_ID, ""), "add", results.get(getAdapterPosition()).getQuantity(), getAdapterPosition());
                        break;
                    case R.id.remove_cart_IV:
                        updateCartCall(results.get(getAdapterPosition()).getProductId(), App.pref.getString(Constant.USER_ID, ""), "delete", results.get(getAdapterPosition()).getQuantity(), getAdapterPosition());
                        break;
                }
            }
        }

        private void updateCartCall(String productId, String userId, String status, String quantity, final int position) {
            String Quantity;

            if (status.equals("add")) {
                int count = Integer.parseInt(quantity);
                count = count + 1;
                quantity = String.valueOf(count);
                Quantity = String.valueOf(count);
            } else {
                if (!quantity.equals("0")) {
                    int count = Integer.parseInt(quantity);
                    count = count - 1;
                    Quantity = String.valueOf(count);
                } else {
                    Quantity = quantity;
                }
            }
            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateCartCall(AppConfig.setRequestBody(productId), AppConfig.setRequestBody(userId), AppConfig.setRequestBody(status), AppConfig.setRequestBody(quantity));

            final String finalQuantity = Quantity;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.e("Cart_Data", responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString(Constant.STATUS).equals("1")) {
                                AppConfig.showToast(object.getString("msg"));
                                DataHolder.setCount(object.getString("cart_count"));
                                DataHolder.setSubTotal(object.getString("sub_total"));
                                totalTV.setText("Cart Total:  %s ₹"+object.getString("sub_total"));
                                badgeTV.setText(object.getString("cart_count")+"");
                                String coucn=DataHolder.getCount();
                                myCartCall(activity);
                                results.get(position).setQuantity(finalQuantity);
                                notifyDataSetChanged();
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
    }

    class CartRVAdapter extends RecyclerView.Adapter<CartRVAdapter.MyViewHolder> {

        private Activity activity;
        private List<ProductDetail> results;

        public CartRVAdapter(Activity mContext, List<ProductDetail> results) {
            this.activity = mContext;
            this.results = results;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cart, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

            int quantity = Integer.parseInt(results.get(position).getQuantity());
            int price = Integer.parseInt(results.get(position).getProductPrice());
            int total = quantity * price;

            holder.titleTV.setText(results.get(position).getPName());
            holder.priceTV.setText(String.valueOf(total));
            holder.quantityTV.setText(results.get(position).getQuantity());
            Glide.with(activity).asBitmap().load(results.get(position).getImageUrl()).into(holder.imageIV);
        }

        @Override
        public int getItemCount() {
            return results.size();
        }

        public void updateList(List<ProductDetail> cartDetails) {
            results = cartDetails;
            notifyDataSetChanged();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView titleTV, priceTV, quantityTV, addCartIV, removeCartIV;
            ImageView imageIV;

            public MyViewHolder(final View view) {
                super(view);
                titleTV = view.findViewById(R.id.product_name_TV);
                priceTV = view.findViewById(R.id.product_price_TV);
                imageIV = view.findViewById(R.id.product_IV);
                addCartIV = view.findViewById(R.id.add_cart_IV);
                removeCartIV = view.findViewById(R.id.remove_cart_IV);
                quantityTV = view.findViewById(R.id.cart_quantity_TV);
                addCartIV.setOnClickListener(this);
                removeCartIV.setOnClickListener(this);
            }

            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.add_cart_button:
                        updateCartCall(results.get(getAdapterPosition()).getProductId(), App.pref.getString(Constant.USER_ID, ""), "add", results.get(getAdapterPosition()).getQuantity(), getAdapterPosition());
                        break;
                    case R.id.add_cart_IV:
                        updateCartCall(results.get(getAdapterPosition()).getProductId(), App.pref.getString(Constant.USER_ID, ""), "add", results.get(getAdapterPosition()).getQuantity(), getAdapterPosition());
                        break;
                    case R.id.remove_cart_IV:
                        updateCartCall(results.get(getAdapterPosition()).getProductId(), App.pref.getString(Constant.USER_ID, ""), "delete", results.get(getAdapterPosition()).getQuantity(), getAdapterPosition());
                        break;
                }
            }
        }

        private void updateCartCall(String productId, String userId, String status, String quantity, final int position) {
            String Quantity;

            if (status.equals("add")) {
                int count = Integer.parseInt(quantity);
                count = count + 1;
                quantity = String.valueOf(count);
                Quantity = String.valueOf(count);
            } else {
                if (!quantity.equals("0")) {
                    int count = Integer.parseInt(quantity);
                    count = count - 1;
                    Quantity = String.valueOf(count);

                } else {
                    Quantity = quantity;
                }
            }

            final ProgressDialog dialog = new ProgressDialog(activity);
            AppConfig.showLoading(dialog);
            Call<ResponseBody> call = AppConfig.getLoadInterface().updateCartCall(AppConfig.setRequestBody(productId), AppConfig.setRequestBody(userId), AppConfig.setRequestBody(status), AppConfig.setRequestBody(quantity));

            final String finalQuantity = Quantity;
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseData = response.body().string();
                            Log.e("Cart_Data", responseData);
                            JSONObject object = new JSONObject(responseData);
                            if (object.getString(Constant.STATUS).equals("1")) {
                                AppConfig.showToast(object.getString("msg"));
                                //ProductsFragment.setTotal(object.getString("cart_count"),object.getString("sub_total"));

                                myCartCall(activity);
                                results.get(position).setQuantity(finalQuantity);
                                notifyDataSetChanged();
                            } else {
                                AppConfig.showToast(object.getString("msg"));
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (JsonSyntaxException e) {
                            e.printStackTrace();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
    }

}
