package com.vch.bean;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductTreat implements Parcelable
{

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
    @SerializedName("img")
    @Expose
    private String img;
    @SerializedName("min_prepration_time")
    @Expose
    private String minPreprationTime;
    @SerializedName("product_price")
    @Expose
    private String productPrice;
    @SerializedName("discount_price")
    @Expose
    private String discountPrice;
    @SerializedName("qua")
    @Expose
    private String qua;
    public final static Parcelable.Creator<ProductTreat> CREATOR = new Creator<ProductTreat>() {


        @SuppressWarnings({
                "unchecked"
        })
        public ProductTreat createFromParcel(Parcel in) {
            return new ProductTreat(in);
        }

        public ProductTreat[] newArray(int size) {
            return (new ProductTreat[size]);
        }

    }
            ;

    protected ProductTreat(Parcel in) {
        this.productId = ((String) in.readValue((String.class.getClassLoader())));
        this.pname = ((String) in.readValue((String.class.getClassLoader())));
        this.dishType = ((String) in.readValue((String.class.getClassLoader())));
        this.preparedByChef = ((String) in.readValue((String.class.getClassLoader())));
        this.ingredients = ((String) in.readValue((String.class.getClassLoader())));
        this.keyFacts = ((String) in.readValue((String.class.getClassLoader())));
        this.calaries = ((String) in.readValue((String.class.getClassLoader())));
        this.img = ((String) in.readValue((String.class.getClassLoader())));
        this.minPreprationTime = ((String) in.readValue((String.class.getClassLoader())));
        this.productPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.discountPrice = ((String) in.readValue((String.class.getClassLoader())));
        this.qua = ((String) in.readValue((String.class.getClassLoader())));
    }

    public ProductTreat() {
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

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getMinPreprationTime() {
        return minPreprationTime;
    }

    public void setMinPreprationTime(String minPreprationTime) {
        this.minPreprationTime = minPreprationTime;
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

    public String getQua() {
        return qua;
    }

    public void setQua(String qua) {
        this.qua = qua;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(productId);
        dest.writeValue(pname);
        dest.writeValue(dishType);
        dest.writeValue(preparedByChef);
        dest.writeValue(ingredients);
        dest.writeValue(keyFacts);
        dest.writeValue(calaries);
        dest.writeValue(img);
        dest.writeValue(minPreprationTime);
        dest.writeValue(productPrice);
        dest.writeValue(discountPrice);
        dest.writeValue(qua);
    }

    public int describeContents() {
        return 0;
    }
}
