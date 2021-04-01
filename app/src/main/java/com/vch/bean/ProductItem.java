package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductItem implements Parcelable {

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pname")
    @Expose
    private String pname;
    @SerializedName("dish_type")
    @Expose
    private String dishType;
    @SerializedName("prepared_by_chef")
    @Expose
    private String preparedByChef;
    @SerializedName("ingredients")
    @Expose
    private String ingredients;
    @SerializedName("key_facts")
    @Expose
    private String keyFacts;
    @SerializedName("calaries")
    @Expose
    private String calaries;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("min_prepration_time")
    @Expose
    private String minPreprationTime;
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("product_qty")
    @Expose
    private String productQty;
    public final static Parcelable.Creator<ProductItem> CREATOR = new Creator<ProductItem>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductItem createFromParcel(Parcel in) {
            return new ProductItem(in);
        }

        public ProductItem[] newArray(int size) {
            return (new ProductItem[size]);
        }

    };

    protected ProductItem(Parcel in) {
        this.productId = ((String) in.readValue((String.class.getClassLoader())));
        this.pname = ((String) in.readValue((String.class.getClassLoader())));
        this.dishType = ((String) in.readValue((String.class.getClassLoader())));
        this.preparedByChef = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = ((String) in.readValue((String.class.getClassLoader())));
        this.keyFacts = ((String) in.readValue((String.class.getClassLoader())));
        this.calaries = ((String) in.readValue((String.class.getClassLoader())));
        this.productPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.minPreprationTime = ((String) in.readValue((String.class.getClassLoader())));
        this.img = ((String) in.readValue((String.class.getClassLoader())));
        this.productQty = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProductItem() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDishType() {
        return dishType;
    }

    public void setDishType(String dishType) {
        this.dishType = dishType;
    }

    public String getPreparedByChef() {
        return preparedByChef;
    }

    public void setPreparedByChef(String preparedByChef) {
        this.preparedByChef = preparedByChef;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getKeyFacts() {
        return keyFacts;
    }

    public void setKeyFacts(String keyFacts) {
        this.keyFacts = keyFacts;
    }

    public String getCalaries() {
        return calaries;
    }

    public void setCalaries(String calaries) {
        this.calaries = calaries;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getMinPreprationTime() {
        return minPreprationTime;
    }

    public void setMinPreprationTime(String minPreprationTime) {
        this.minPreprationTime = minPreprationTime;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getProductQty() {
        return productQty;
    }

    public void setProductQty(String productQty) {
        this.productQty = productQty;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(pname);
        dest.writeValue(dishType);
        dest.writeValue(preparedByChef);
        dest.writeValue(ingredients);
        dest.writeValue(keyFacts);
        dest.writeValue(calaries);
        dest.writeValue(productPrice);
        dest.writeValue(minPreprationTime);
        dest.writeValue(img);
        dest.writeValue(productQty);
    }

    public int describeContents() {
        return 0;
    }
}