package com.vch.response;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.ProductDetail;

import java.util.List;

public class SearchDATA implements Parcelable {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private List<ProductDetail> msg = null;
    public final static Parcelable.Creator<SearchDATA> CREATOR = new Creator<SearchDATA>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SearchDATA createFromParcel(Parcel in) {
            return new SearchDATA(in);
        }

        public SearchDATA[] newArray(int size) {
            return (new SearchDATA[size]);
        }

    };

    protected SearchDATA(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.msg, (ProductDetail.class.getClassLoader()));
    }

    public SearchDATA() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<ProductDetail> getMsg() {
        return msg;
    }

    public void setMsg(List<ProductDetail> msg) {
        this.msg = msg;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(msg);
    }

    public int describeContents() {
        return 0;
    }
}