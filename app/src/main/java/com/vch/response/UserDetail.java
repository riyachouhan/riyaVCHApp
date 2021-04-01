package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
public class UserDetail implements Parcelable
{

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("user_email")
    @Expose
    private String userEmail;
    @SerializedName("user_name")
    @Expose
    private String userName;
    @SerializedName("user_added_on")
    @Expose
    private String userAddedOn;
    @SerializedName("user_phone")
    @Expose
    private String userPhone;
    @SerializedName("loyalty_status")
    @Expose
    private String loyaltyStatus;
    @SerializedName("user_referral_code")
    @Expose
    private String userReferralCode;
    @SerializedName("user_referrer_id")
    @Expose
    private String userReferrerId;
    @SerializedName("user_pincode")
    @Expose
    private String userPincode;
    @SerializedName("user_profile_image")
    @Expose
    private String userProfileImage;
    public final static Parcelable.Creator<UserDetail> CREATOR = new Creator<UserDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public UserDetail createFromParcel(Parcel in) {
            return new UserDetail(in);
        }

        public UserDetail[] newArray(int size) {
            return (new UserDetail[size]);
        }

    }
            ;

    protected UserDetail(Parcel in) {
        this.userId = ((String) in.readValue((String.class.getClassLoader())));
        this.userEmail = ((String) in.readValue((String.class.getClassLoader())));
        this.userName = ((String) in.readValue((String.class.getClassLoader())));
        this.userAddedOn = ((String) in.readValue((String.class.getClassLoader())));
        this.userPhone = ((String) in.readValue((String.class.getClassLoader())));
        this.loyaltyStatus = ((String) in.readValue((String.class.getClassLoader())));
        this.userReferralCode = ((String) in.readValue((String.class.getClassLoader())));
        this.userReferrerId = ((String) in.readValue((String.class.getClassLoader())));
        this.userPincode = ((String) in.readValue((String.class.getClassLoader())));
        this.userProfileImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public UserDetail() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserAddedOn() {
        return userAddedOn;
    }

    public void setUserAddedOn(String userAddedOn) {
        this.userAddedOn = userAddedOn;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getLoyaltyStatus() {
        return loyaltyStatus;
    }

    public void setLoyaltyStatus(String loyaltyStatus) {
        this.loyaltyStatus = loyaltyStatus;
    }

    public String getUserReferralCode() {
        return userReferralCode;
    }

    public void setUserReferralCode(String userReferralCode) {
        this.userReferralCode = userReferralCode;
    }

    public String getUserReferrerId() {
        return userReferrerId;
    }

    public void setUserReferrerId(String userReferrerId) {
        this.userReferrerId = userReferrerId;
    }

    public String getUserPincode() {
        return userPincode;
    }

    public void setUserPincode(String userPincode) {
        this.userPincode = userPincode;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(userId);
        dest.writeValue(userEmail);
        dest.writeValue(userName);
        dest.writeValue(userAddedOn);
        dest.writeValue(userPhone);
        dest.writeValue(loyaltyStatus);
        dest.writeValue(userReferralCode);
        dest.writeValue(userReferrerId);
        dest.writeValue(userPincode);
        dest.writeValue(userProfileImage);
    }

    public int describeContents() {
        return 0;
    }

}