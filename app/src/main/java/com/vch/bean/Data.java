package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Data implements Parcelable
{

    @SerializedName("coupon_amount")
    @Expose
    private String couponAmount;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;
    @SerializedName("cashback")
    @Expose
    private String cashback;
    @SerializedName("min_purchase")
    @Expose
    private String minPurchase;
    public final static Parcelable.Creator<Data> CREATOR = new Creator<Data>() {


        @SuppressWarnings({"unchecked"})
        public Data createFromParcel(Parcel in) {
            return new Data(in);
        }

        public Data[] newArray(int size) {
            return (new Data[size]);
        }

    }
            ;

    protected Data(Parcel in) {
        this.couponAmount = ((String) in.readValue((String.class.getClassLoader())));
        this.couponCode = ((String) in.readValue((String.class.getClassLoader())));
        this.cashback = ((String) in.readValue((String.class.getClassLoader())));
        this.minPurchase = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Data() {
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    public String getCashback() {
        return cashback;
    }

    public void setCashback(String cashback) {
        this.cashback = cashback;
    }

    public String getMinPurchase() {
        return minPurchase;
    }

    public void setMinPurchase(String minPurchase) {
        this.minPurchase = minPurchase;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(couponAmount);
        dest.writeValue(couponCode);
        dest.writeValue(cashback);
        dest.writeValue(minPurchase);
    }

    public int describeContents() {
        return 0;
    }

}
