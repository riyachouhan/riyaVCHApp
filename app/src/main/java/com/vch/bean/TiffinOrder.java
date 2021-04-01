package com.vch.bean;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TiffinOrder {

    @SerializedName("user_meal_plan_id")
    @Expose
    private String userMealPlanId;
    @SerializedName("tiffin_order_id")
    @Expose
    private String tiffinOrderId;
    @SerializedName("meal_qty")
    @Expose
    private String mealQty;
    @SerializedName("user_address")
    @Expose
    private String userAddress;
    @SerializedName("lunch_dinner")
    @Expose
    private String lunchDinner;
    @SerializedName("total_meal")
    @Expose
    private String totalMeal;
    @SerializedName("balance_meal")
    @Expose
    private String balanceMeal;
    @SerializedName("plan_total_amount")
    @Expose
    private String planTotalAmount;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("total_tiffin")
    @Expose
    private String totalTiffin;
    @SerializedName("left_tiffin")
    @Expose
    private String leftTiffin;
    @SerializedName("delivered_tiffin")
    @Expose
    private String deliveredTiffin;
    @SerializedName("user_meal_details")
    @Expose
    private List<UserMealDetail> userMealDetails = null;

    public String getUserMealPlanId() {
        return userMealPlanId;
    }

    public void setUserMealPlanId(String userMealPlanId) {
        this.userMealPlanId = userMealPlanId;
    }

    public String getTiffinOrderId() {
        return tiffinOrderId;
    }

    public void setTiffinOrderId(String tiffinOrderId) {
        this.tiffinOrderId = tiffinOrderId;
    }

    public String getMealQty() {
        return mealQty;
    }

    public void setMealQty(String mealQty) {
        this.mealQty = mealQty;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getLunchDinner() {
        return lunchDinner;
    }

    public void setLunchDinner(String lunchDinner) {
        this.lunchDinner = lunchDinner;
    }

    public String getTotalMeal() {
        return totalMeal;
    }

    public void setTotalMeal(String totalMeal) {
        this.totalMeal = totalMeal;
    }

    public String getBalanceMeal() {
        return balanceMeal;
    }

    public void setBalanceMeal(String balanceMeal) {
        this.balanceMeal = balanceMeal;
    }

    public String getPlanTotalAmount() {
        return planTotalAmount;
    }

    public void setPlanTotalAmount(String planTotalAmount) {
        this.planTotalAmount = planTotalAmount;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getTotalTiffin() {
        return totalTiffin;
    }

    public void setTotalTiffin(String totalTiffin) {
        this.totalTiffin = totalTiffin;
    }

    public String getLeftTiffin() {
        return leftTiffin;
    }

    public void setLeftTiffin(String leftTiffin) {
        this.leftTiffin = leftTiffin;
    }

    public String getDeliveredTiffin() {
        return deliveredTiffin;
    }

    public void setDeliveredTiffin(String deliveredTiffin) {
        this.deliveredTiffin = deliveredTiffin;
    }

    public List<UserMealDetail> getUserMealDetails() {
        return userMealDetails;
    }

    public void setUserMealDetails(List<UserMealDetail> userMealDetails) {
        this.userMealDetails = userMealDetails;
    }
}
