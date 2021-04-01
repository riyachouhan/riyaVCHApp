package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetail implements Parcelable
{

    @SerializedName("product_id")
    @Expose
    private String productId;
    @SerializedName("pname")
    @Expose
    private String pName;
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
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("min_prepration_time")
    @Expose
    private String minPreprationTime;
    @SerializedName("image_url")
    @Expose
    private String imageUrl;
    @SerializedName("quantity")
    @Expose
    private String quantity;
    @SerializedName("product_labal")
    @Expose
    private String productLabel;
    public final static Parcelable.Creator<ProductDetail> CREATOR = new Creator<ProductDetail>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductDetail createFromParcel(Parcel in) {
            return new ProductDetail(in);
        }

        public ProductDetail[] newArray(int size) {
            return (new ProductDetail[size]);
        }

    };

    protected ProductDetail(Parcel in) {
        this.productId = ((String) in.readValue((String.class.getClassLoader())));
        this.pName = ((String) in.readValue((String.class.getClassLoader())));
        this.dishType = ((String) in.readValue((String.class.getClassLoader())));
        this.preparedByChef = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = ((String) in.readValue((String.class.getClassLoader())));
        this.keyFacts = ((String) in.readValue((String.class.getClassLoader())));
        this.calaries = ((String) in.readValue((String.class.getClassLoader())));
        this.productPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.discountPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.minPreprationTime = ((String) in.readValue((String.class.getClassLoader())));
        this.imageUrl = ((String) in.readValue((String.class.getClassLoader())));
        this.quantity = ((String) in.readValue((String.class.getClassLoader())));
        this.productLabel = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProductDetail() {
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getPName() {
        return pName;
    }

    public void setPName(String pName) {
        this.pName = pName;
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

    public String getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(String discountPrice) {
        this.discountPrice = discountPrice;
    }

    public String getMinPreprationTime() {
        return minPreprationTime;
    }

    public void setMinPreprationTime(String minPreprationTime) {
        this.minPreprationTime = minPreprationTime;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getProductLabel() {
        return productLabel;
    }

    public void setProductLabel(String productLabel) {
        this.productLabel = productLabel;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(pName);
        dest.writeValue(dishType);
        dest.writeValue(preparedByChef);
        dest.writeValue(ingredients);
        dest.writeValue(keyFacts);
        dest.writeValue(calaries);
        dest.writeValue(productPrice);
        dest.writeValue(discountPrice);
        dest.writeValue(minPreprationTime);
        dest.writeValue(imageUrl);
        dest.writeValue(quantity);
        dest.writeValue(productLabel);
    }

    public int describeContents() {
        return 0;
    }
}
