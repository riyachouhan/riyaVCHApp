package com.vch.bean;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class OrderHistory
{

    @SerializedName("product_items")
    @Expose
    private List<ProductItem> productItems = null;
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("order_id")
    @Expose
    private String orderId;
    @SerializedName("delivery_date")
    @Expose
    private String deliveryDate;
    @SerializedName("sub_total")
    @Expose
    private String subTotal;
    @SerializedName("redeam_point")
    @Expose
    private String redeamPoint;
    @SerializedName("order_total")
    @Expose
    private String orderTotal;
    @SerializedName("shipping_charge")
    @Expose
    private String shippingCharge;
    @SerializedName("order_time")
    @Expose
    private String orderTime;
    @SerializedName("payment_status")
    @Expose
    private String paymentStatus;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("order_status")
    @Expose
    private String orderStatus;
    @SerializedName("coupon_discount")
    @Expose
    private String couponDiscount;
    @SerializedName("taxable_amount")
    @Expose
    private String taxableAmount;
    @SerializedName("tax_amount")
    @Expose
    private String taxAmount;
    @SerializedName("coupon_code")
    @Expose
    private String couponCode;

    public List<ProductItem> getProductItems() {
        return productItems;
    }

    public void setProductItems(List<ProductItem> productItems) {
        this.productItems = productItems;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getRedeamPoint() {
        return redeamPoint;
    }

    public void setRedeamPoint(String redeamPoint) {
        this.redeamPoint = redeamPoint;
    }

    public String getOrderTotal() {
        return orderTotal;
    }

    public void setOrderTotal(String orderTotal) {
        this.orderTotal = orderTotal;
    }

    public String getShippingCharge() {
        return shippingCharge;
    }

    public void setShippingCharge(String shippingCharge) {
        this.shippingCharge = shippingCharge;
    }

    public String getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(String orderTime) {
        this.orderTime = orderTime;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public void setPaymentStatus(String paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getCouponDiscount() {
        return couponDiscount;
    }

    public void setCouponDiscount(String couponDiscount) {
        this.couponDiscount = couponDiscount;
    }

    public String getTaxableAmount() {
        return taxableAmount;
    }

    public void setTaxableAmount(String taxableAmount) {
        this.taxableAmount = taxableAmount;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }


}
