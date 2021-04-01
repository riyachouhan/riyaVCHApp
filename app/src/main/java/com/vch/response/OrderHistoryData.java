package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.OrderHistory;

import java.util.List;

public class OrderHistoryData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private List<OrderHistory> orderHistory = null;
    public final static Parcelable.Creator<OrderHistoryData> CREATOR = new Creator<OrderHistoryData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public OrderHistoryData createFromParcel(Parcel in) {
            return new OrderHistoryData(in);
        }

        public OrderHistoryData[] newArray(int size) {
            return (new OrderHistoryData[size]);
        }

    }
            ;

    protected OrderHistoryData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.orderHistory, (OrderHistory.class.getClassLoader()));
    }

    public OrderHistoryData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<OrderHistory> getOrderHistory() {
        return orderHistory;
    }

    public void setOrderHistory(List<OrderHistory> orderHistory) {
        this.orderHistory = orderHistory;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(orderHistory);
    }

    public int describeContents() {
        return 0;
    }

}
