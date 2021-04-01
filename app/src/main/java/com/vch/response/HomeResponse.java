package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.Msg;

public class HomeResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private Msg msg;
    public final static Parcelable.Creator<HomeResponse> CREATOR = new Creator<HomeResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public HomeResponse createFromParcel(Parcel in) {
            return new HomeResponse(in);
        }

        public HomeResponse[] newArray(int size) {
            return (new HomeResponse[size]);
        }

    }
            ;

    protected HomeResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((Msg) in.readValue((Msg.class.getClassLoader())));
    }

    public HomeResponse() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Msg getMsg() {
        return msg;
    }

    public void setMsg(Msg msg) {
        this.msg = msg;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
    }

    public int describeContents() {
        return 0;
    }
}
