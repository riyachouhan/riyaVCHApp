package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Msg implements Parcelable
{
    @SerializedName("sliderArray")
    @Expose
    private List<SliderArray> sliderArray = null;
    @SerializedName("menuArray")
    @Expose
    private List<MenuArray> menuArray = null;
    @SerializedName("cart_count")
    @Expose
    private String cartCount;
    @SerializedName("subtotal")
    @Expose
    private String subtotal;
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

    protected Msg(Parcel in) {
        in.readList(this.sliderArray, (SliderArray.class.getClassLoader()));
        in.readList(this.menuArray, (MenuArray.class.getClassLoader()));
        this.cartCount = ((String) in.readValue((String.class.getClassLoader())));
        this.subtotal = ((String) in.readValue((String.class.getClassLoader())));
    }

    public Msg() {
    }

    public List<SliderArray> getSliderArray() {
        return sliderArray;
    }

    public void setSliderArray(List<SliderArray> sliderArray) {
        this.sliderArray = sliderArray;
    }

    public List<MenuArray> getMenuArray() {
        return menuArray;
    }

    public void setMenuArray(List<MenuArray> menuArray) {
        this.menuArray = menuArray;
    }

    public String getCartCount() {
        return cartCount;
    }

    public void setCartCount(String cartCount) {
        this.cartCount = cartCount;
    }

    public String getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(String subtotal) {
        this.subtotal = subtotal;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(sliderArray);
        dest.writeList(menuArray);
        dest.writeValue(cartCount);
        dest.writeValue(subtotal);
    }

    public int describeContents() {
        return 0;
    }


}
