package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.AddressData;

import java.util.List;

public class GetAddressData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private List<AddressData> msg = null;
    public final static Parcelable.Creator<GetAddressData> CREATOR = new Creator<GetAddressData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GetAddressData createFromParcel(Parcel in) {
            return new GetAddressData(in);
        }

        public GetAddressData[] newArray(int size) {
            return (new GetAddressData[size]);
        }

    }
            ;

    protected GetAddressData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.msg, (AddressData.class.getClassLoader()));
    }

    public GetAddressData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<AddressData> getMsg() {
        return msg;
    }

    public void setMsg(List<AddressData> msg) {
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
