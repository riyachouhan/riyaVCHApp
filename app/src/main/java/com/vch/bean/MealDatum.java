package com.vch.bean;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MealDatum implements Parcelable
{

    @SerializedName("meal_plan_id")
    @Expose
    private String mealPlanId;
    @SerializedName("meal_qty")
    @Expose
    private String mealQty;
    @SerializedName("plan_amount")
    @Expose
    private String planAmount;
    public final static Parcelable.Creator<MealDatum> CREATOR = new Creator<MealDatum>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MealDatum createFromParcel(Parcel in) {
            return new MealDatum(in);
        }

        public MealDatum[] newArray(int size) {
            return (new MealDatum[size]);
        }

    }
            ;

    protected MealDatum(Parcel in) {
        this.mealPlanId = ((String) in.readValue((String.class.getClassLoader())));
        this.mealQty = ((String) in.readValue((String.class.getClassLoader())));
        this.planAmount = ((String) in.readValue((String.class.getClassLoader())));
    }

    public MealDatum() {
    }

    public String getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(String mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public String getMealQty() {
        return mealQty;
    }

    public void setMealQty(String mealQty) {
        this.mealQty = mealQty;
    }

    public String getPlanAmount() {
        return planAmount;
    }

    public void setPlanAmount(String planAmount) {
        this.planAmount = planAmount;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(mealPlanId);
        dest.writeValue(mealQty);
        dest.writeValue(planAmount);
    }

    public int describeContents() {
        return 0;
    }
}
