package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetails implements Parcelable
{

    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("billing_name")
    @Expose
    private String billingName;
    @SerializedName("billing_zip")
    @Expose
    private String billingZip;
    @SerializedName("billing_address")
    @Expose
    private String billingAddress;
    @SerializedName("billing_tel")
    @Expose
    private String billingTel;
    @SerializedName("billing_email")
    @Expose
    private String billingEmail;
    public final static Parcelable.Creator<OrderDetails> CREATOR = new Creator<OrderDetails>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OrderDetails createFromParcel(Parcel in) {
            return new OrderDetails(in);
        }

        public OrderDetails[] newArray(int size) {
            return (new OrderDetails[size]);
        }

    }
            ;

    protected OrderDetails(Parcel in) {
        this.orderId = ((String) in.readValue((String.class.getClassLoader())));
        this.billingName = ((String) in.readValue((String.class.getClassLoader())));
        this.billingZip = ((String) in.readValue((String.class.getClassLoader())));
        this.billingAddress = ((String) in.readValue((String.class.getClassLoader())));
        this.billingTel = ((String) in.readValue((String.class.getClassLoader())));
        this.billingEmail = ((String) in.readValue((String.class.getClassLoader())));
    }

    public OrderDetails() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getBillingName() {
        return billingName;
    }

    public void setBillingName(String billingName) {
        this.billingName = billingName;
    }

    public String getBillingZip() {
        return billingZip;
    }

    public void setBillingZip(String billingZip) {
        this.billingZip = billingZip;
    }

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    public String getBillingTel() {
        return billingTel;
    }

    public void setBillingTel(String billingTel) {
        this.billingTel = billingTel;
    }

    public String getBillingEmail() {
        return billingEmail;
    }

    public void setBillingEmail(String billingEmail) {
        this.billingEmail = billingEmail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(orderId);
        dest.writeValue(billingName);
        dest.writeValue(billingZip);
        dest.writeValue(billingAddress);
        dest.writeValue(billingTel);
        dest.writeValue(billingEmail);
    }

    public int describeContents() {
        return 0;
    }
}
