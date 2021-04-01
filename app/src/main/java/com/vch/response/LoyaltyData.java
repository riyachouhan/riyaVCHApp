package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.Transaction;

import java.util.List;

public class LoyaltyData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("balance_point")
    @Expose
    private Integer balancePoint;
    @SerializedName("earn_point")
    @Expose
    private Integer earnPoint;
    @SerializedName("redeam_point")
    @Expose
    private Integer redeamPoint;
    @SerializedName("transaction")
    @Expose
    private List<Transaction> transaction = null;
    public final static Parcelable.Creator<LoyaltyData> CREATOR = new Creator<LoyaltyData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public LoyaltyData createFromParcel(Parcel in) {
            return new LoyaltyData(in);
        }

        public LoyaltyData[] newArray(int size) {
            return (new LoyaltyData[size]);
        }

    }
            ;

    protected LoyaltyData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.balancePoint = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.earnPoint = ((Integer) in.readValue((Integer.class.getClassLoader())));
        this.redeamPoint = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.transaction, (Transaction.class.getClassLoader()));
    }

    public LoyaltyData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBalancePoint() {
        return balancePoint;
    }

    public void setBalancePoint(Integer balancePoint) {
        this.balancePoint = balancePoint;
    }

    public Integer getEarnPoint() {
        return earnPoint;
    }

    public void setEarnPoint(Integer earnPoint) {
        this.earnPoint = earnPoint;
    }

    public Integer getRedeamPoint() {
        return redeamPoint;
    }

    public void setRedeamPoint(Integer redeamPoint) {
        this.redeamPoint = redeamPoint;
    }

    public List<Transaction> getTransaction() {
        return transaction;
    }

    public void setTransaction(List<Transaction> transaction) {
        this.transaction = transaction;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeValue(balancePoint);
        dest.writeValue(earnPoint);
        dest.writeValue(redeamPoint);
        dest.writeList(transaction);
    }

    public int describeContents() {
        return 0;
    }
}