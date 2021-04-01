package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MenuArray implements Parcelable
{

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("cat_url")
    @Expose
    private String catUrl;
    @SerializedName("category_image")
    @Expose
    private String categoryImage;
    public final static Parcelable.Creator<MenuArray> CREATOR = new Creator<MenuArray>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MenuArray createFromParcel(Parcel in) {
            return new MenuArray(in);
        }

        public MenuArray[] newArray(int size) {
            return (new MenuArray[size]);
        }

    }
            ;

    protected MenuArray(Parcel in) {
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.name = ((String) in.readValue((String.class.getClassLoader())));
        this.catUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.categoryImage = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MenuArray() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatUrl() {
        return catUrl;
    }

    public void setCatUrl(String catUrl) {
        this.catUrl = catUrl;
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(id);
        dest.writeValue(name);
        dest.writeValue(catUrl);
        dest.writeValue(categoryImage);
    }

    public int describeContents() {
        return 0;
    }
}
