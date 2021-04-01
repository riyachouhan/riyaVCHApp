package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.UserData;

public class RegisterResponse implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("user_data")
    @Expose
    private UserData userData;
    public final static Parcelable.Creator<RegisterResponse> CREATOR = new Creator<RegisterResponse>() {


        @SuppressWarnings({
                "unchecked"
        })
        public RegisterResponse createFromParcel(Parcel in) {
            return new RegisterResponse(in);
        }

        public RegisterResponse[] newArray(int size) {
            return (new RegisterResponse[size]);
        }

    }
            ;

    protected RegisterResponse(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.msg = ((String) in.readValue((String.class.getClassLoader())));
        this.userData = ((UserData) in.readValue((UserData.class.getClassLoader())));
    }

    public RegisterResponse() {
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

    public UserData getUserData() {
        return userData;
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(msg);
        dest.writeValue(userData);
    }

    public int describeContents() {
        return 0;
    }

}
