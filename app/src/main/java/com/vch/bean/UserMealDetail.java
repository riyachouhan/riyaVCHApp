package com.vch.bean;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserMealDetail {
    @SerializedName("day_date")
    @Expose
    private String dayDate;
    @SerializedName("meal_qty_no")
    @Expose
    private String mealQtyNo;
    @SerializedName("deliver_status")
    @Expose
    private String deliverStatus;

    public String getDayDate() {
        return dayDate;
    }

    public void setDayDate(String dayDate) {
        this.dayDate = dayDate;
    }

    public String getMealQtyNo() {
        return mealQtyNo;
    }

    public void setMealQtyNo(String mealQtyNo) {
        this.mealQtyNo = mealQtyNo;
    }

    public String getDeliverStatus() {
        return deliverStatus;
    }

    public void setDeliverStatus(String deliverStatus) {
        this.deliverStatus = deliverStatus;
    }
}
