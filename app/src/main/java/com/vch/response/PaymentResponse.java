package com.vch.response;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.OrderDetails;

public class PaymentResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("order_details")
    @Expose
    private OrderDetails orderDetails;
    public final static Parcelable.Creator<PaymentResponse> CREATOR = new Creator<PaymentResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public PaymentResponse createFromParcel(Parcel in) {
            return new PaymentResponse(in);
        }

        public PaymentResponse[] newArray(int size) {
            return (new PaymentResponse[size]);
        }

    }
            ;

    protected PaymentResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.orderDetails = ((OrderDetails) in.readValue((OrderDetails.class.getClassLoader())));
    }

    public PaymentResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public OrderDetails getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(OrderDetails orderDetails) {
        this.orderDetails = orderDetails;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(orderDetails);
    }

    public int describeContents() {
        return 0;
    }

}
