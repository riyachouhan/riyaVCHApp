package com.vch.response;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class UserResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("data")
    @Expose
    private UserDetail data;
    public final static Parcelable.Creator<UserResponse> CREATOR = new Creator<UserResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserResponse createFromParcel(Parcel in) {
            return new UserResponse(in);
        }

        public UserResponse[] newArray(int size) {
            return (new UserResponse[size]);
        }

    }
            ;

    protected UserResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.data = ((UserDetail) in.readValue((UserDetail.class.getClassLoader())));
    }

    public UserResponse() {
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

    public UserDetail getData() {
        return data;
    }

    public void setData(UserDetail data) {
        this.data = data;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(data);
    }

    public int describeContents() {
        return 0;
    }

}

