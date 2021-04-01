package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.ProductDetail;

import java.util.List;

public class ProductsResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private List<ProductDetail> productDetail = null;
    public final static Parcelable.Creator<ProductsResponse> CREATOR = new Creator<ProductsResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductsResponse createFromParcel(Parcel in) {
            return new ProductsResponse(in);
        }

        public ProductsResponse[] newArray(int size) {
            return (new ProductsResponse[size]);
        }

    }
            ;

    protected ProductsResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        in.readList(this.productDetail, (ProductDetail.class.getClassLoader()));
    }

    public ProductsResponse() {
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

    public List<ProductDetail> getProductDetail() {
        return productDetail;
    }

    public void setProductDetail(List<ProductDetail> productDetail) {
        this.productDetail = productDetail;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeList(productDetail);
    }

    public int describeContents() {
        return 0;
    }
}
