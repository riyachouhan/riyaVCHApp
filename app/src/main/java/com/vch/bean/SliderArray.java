package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SliderArray implements Parcelable
{

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("link_url")
    @Expose
    private String linkUrl;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    public final static Parcelable.Creator<SliderArray> CREATOR = new Creator<SliderArray>() {


        @SuppressWarnings({
                "unchecked"
        })
        public SliderArray createFromParcel(Parcel in) {
            return new SliderArray(in);
        }

        public SliderArray[] newArray(int size) {
            return (new SliderArray[size]);
        }

    }
            ;

    protected SliderArray(Parcel in) {
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.linkUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.status = ((String) in.readValue((String.class.getClassLoader())));
        this.description = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
    }

    public SliderArray() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setLinkUrl(String linkUrl) {
        this.linkUrl = linkUrl;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(name);
        dest.writeValue(linkUrl);
        dest.writeValue(status);
        dest.writeValue(description);
        dest.writeValue(imageUrl);
    }

    public int describeContents() {
        return 0;
    }

}
