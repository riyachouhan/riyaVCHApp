package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Slot implements Parcelable
{

    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("show")
    @Expose
    private String show;
    public final static Parcelable.Creator<Slot> CREATOR = new Creator<Slot>() {


        @SuppressWarnings({
                "unchecked"
        })
        public Slot createFromParcel(Parcel in) {
            return new Slot(in);
        }

        public Slot[] newArray(int size) {
            return (new Slot[size]);
        }

    }
            ;

    protected Slot(Parcel in) {
        this.slot = ((String) in.readValue((String.class.getClassLoader())));
        this.show = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Slot() {
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public String getShow() {
        return show;
    }

    public void setShow(String show) {
        this.show = show;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(slot);
        dest.writeValue(show);
    }

    public int describeContents() {
        return 0;
    }
}
