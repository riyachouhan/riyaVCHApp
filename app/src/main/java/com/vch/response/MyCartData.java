package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.Coupon;
import com.vch.bean.ProductDetail;

import java.util.List;

public class MyCartData implements Parcelable
{


@SerializedName("status")
@Expose
private Double status;
@SerializedName("msg")
@Expose
private List<ProductDetail> msg = null;
@SerializedName("product_treat")
@Expose
private List<ProductDetail> productTreat = null;
@SerializedName("coupon")
@Expose
private List<Coupon> coupon = null;
@SerializedName("subtotal")
@Expose
private Double subtotal;
@SerializedName("cgst")
@Expose
private Double cgst;
@SerializedName("grand_total")
@Expose
private Double grandTotal;
@SerializedName("redeam_point")
@Expose
private Double redeamPoint;
@SerializedName("taxable_amount")
@Expose
private Double taxableAmount;
public final static Parcelable.Creator<MyCartData> CREATOR = new Creator<MyCartData>() {


@SuppressWarnings({
"unchecked"
})
public MyCartData createFromParcel(Parcel in) {
return new MyCartData(in);
}

public MyCartData[] newArray(int size) {
return (new MyCartData[size]);
}

}
;

protected MyCartData(Parcel in) {
this.status = ((Double) in.readValue((Double.class.getClassLoader())));
in.readList(this.msg, (ProductDetail.class.getClassLoader()));
in.readList(this.productTreat, (java.lang.Object.class.getClassLoader()));
in.readList(this.coupon, (java.lang.Object.class.getClassLoader()));
this.subtotal = ((Double) in.readValue((Double.class.getClassLoader())));
this.cgst = ((Double) in.readValue((Double.class.getClassLoader())));
this.grandTotal = ((Double) in.readValue((Double.class.getClassLoader())));
this.redeamPoint = ((Double) in.readValue((Double.class.getClassLoader())));
this.taxableAmount = ((Double) in.readValue((Double.class.getClassLoader())));
}

public MyCartData() {
}

public Double getStatus() {
return status;
}

public void setStatus(Double status) {
this.status = status;
}

public List<ProductDetail> getMsg() {
return msg;
}

public void setMsg(List<ProductDetail> msg) {
this.msg = msg;
}

public List<ProductDetail> getProductTreat() {
return productTreat;
}

public void setProductTreat(List<ProductDetail> productTreat) {
this.productTreat = productTreat;
}

public List<Coupon> getCoupon() {
return coupon;
}

public void setCoupon(List<Coupon> coupon) {
this.coupon = coupon;
}

public Double getSubtotal() {
return subtotal;
}

public void setSubtotal(Double subtotal) {
this.subtotal = subtotal;
}

public Double getCgst() {
return cgst;
}

public void setCgst(Double cgst) {
this.cgst = cgst;
}

public Double getGrandTotal() {
return grandTotal;
}

public void setGrandTotal(Double grandTotal) {
this.grandTotal = grandTotal;
}

public Double getRedeamPoint() {
return redeamPoint;
}

public void setRedeamPoint(Double redeamPoint) {
this.redeamPoint = redeamPoint;
}

public Double getTaxableAmount() {
return taxableAmount;
}

public void setTaxableAmount(Double taxableAmount) {
this.taxableAmount = taxableAmount;
}

public void writeToParcel(Parcel dest, int flags) {
dest.writeValue(status);
dest.writeList(msg);
dest.writeList(productTreat);
dest.writeList(coupon);
dest.writeValue(subtotal);
dest.writeValue(cgst);
dest.writeValue(grandTotal);
dest.writeValue(redeamPoint);
dest.writeValue(taxableAmount);
}

public int describeContents() {
return 0;
}

}