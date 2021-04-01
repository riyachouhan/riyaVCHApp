package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddressData implements Parcelable
{

    @SerializedName("address_id")
    @Expose
    private String addressId;
    @SerializedName("customer_name")
    @Expose
    private String customerName;
    @SerializedName("house_number")
    @Expose
    private String houseNumber;
    @SerializedName("address")
    @Expose
    private String address;
    @SerializedName("mobile_no")
    @Expose
    private String mobileNo;
    @SerializedName("pincode")
    @Expose
    private String pincode;
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("landmark")
    @Expose
    private String landmark;
    public final static Parcelable.Creator<Msg> CREATOR = new Creator<Msg>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Msg createFromParcel(Parcel in) {
            return new Msg(in);
        }

        public Msg[] newArray(int size) {
            return (new Msg[size]);
        }

    }
            ;

    protected AddressData(Parcel in) {
        this.addressId = ((String) in.readValue((String.class.getClassLoader())));
        this.customerName = ((String) in.readValue((String.class.getClassLoader())));
        this.houseNumber = ((String) in.readValue((String.class.getClassLoader())));
        this.address = ((String) in.readValue((String.class.getClassLoader())));
        this.mobileNo = ((String) in.readValue((String.class.getClassLoader())));
        this.pincode = ((String) in.readValue((String.class.getClassLoader())));
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.label = ((String) in.readValue((String.class.getClassLoader())));
        this.landmark = ((String) in.readValue((String.class.getClassLoader())));
    }

    public AddressData() {
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(addressId);
        dest.writeValue(customerName);
        dest.writeValue(houseNumber);
        dest.writeValue(address);
        dest.writeValue(mobileNo);
        dest.writeValue(pincode);
        dest.writeValue(userId);
        dest.writeValue(label);
        dest.writeValue(landmark);
    }

    public int describeContents() {
        return 0;
    }

}
