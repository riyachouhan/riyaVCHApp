package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.Slot;

import java.util.List;

public class GetSlotsData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("msg")
    @Expose
    private List<Slot> slots = null;
    public final static Parcelable.Creator<GetSlotsData> CREATOR = new Creator<GetSlotsData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public GetSlotsData createFromParcel(Parcel in) {
            return new GetSlotsData(in);
        }

        public GetSlotsData[] newArray(int size) {
            return (new GetSlotsData[size]);
        }

    }
            ;

    protected GetSlotsData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.slots, (Slot.class.getClassLoader()));
    }

    public GetSlotsData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<Slot> getSlots() {
        return slots;
    }

    public void setSlots(List<Slot> slots) {
        this.slots = slots;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(slots);
    }

    public int describeContents() {
        return 0;
    }

}

