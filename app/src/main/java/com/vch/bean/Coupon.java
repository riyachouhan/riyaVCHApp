package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupon implements Parcelable
{

    @SerializedName("code")
    @Expose
    private String code;
    @SerializedName("coupon_dis")
    @Expose
    private String couponDis;
    public final static Parcelable.Creator<Coupon> CREATOR = new Creator<Coupon>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Coupon createFromParcel(Parcel in) {
            return new Coupon(in);
        }

        public Coupon[] newArray(int size) {
            return (new Coupon[size]);
        }

    }
            ;

    protected Coupon(Parcel in) {
        this.code = ((String) in.readValue((String.class.getClassLoader())));
        this.couponDis = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Coupon() {
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCouponDis() {
        return couponDis;
    }

    public void setCouponDis(String couponDis) {
        this.couponDis = couponDis;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(code);
        dest.writeValue(couponDis);
    }

    public int describeContents() {
        return 0;
    }
}
