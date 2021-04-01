package com.vch.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.TiffinOrder;

import java.util.List;

public class TiffinResponse {
    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("tiffin_orders")
    @Expose
    private List<TiffinOrder> tiffinOrders = null;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<TiffinOrder> getTiffinOrders() {
        return tiffinOrders;
    }

    public void setTiffinOrders(List<TiffinOrder> tiffinOrders) {
        this.tiffinOrders = tiffinOrders;
    }
}
