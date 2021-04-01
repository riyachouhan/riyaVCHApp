package com.vch.response;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.vch.bean.MealDatum;

import java.util.List;

public class MealPlanData implements Parcelable
{

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("meal_data")
    @Expose
    private List<MealDatum> mealData = null;
    public final static Parcelable.Creator<MealPlanData> CREATOR = new Creator<MealPlanData>() {


        @SuppressWarnings({
                "unchecked"
        })
        public MealPlanData createFromParcel(Parcel in) {
            return new MealPlanData(in);
        }

        public MealPlanData[] newArray(int size) {
            return (new MealPlanData[size]);
        }

    }
            ;

    protected MealPlanData(Parcel in) {
        this.status = ((Integer) in.readValue((Integer.class.getClassLoader())));
        in.readList(this.mealData, (MealDatum.class.getClassLoader()));
    }

    public MealPlanData() {
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public List<MealDatum> getMealData() {
        return mealData;
    }

    public void setMealData(List<MealDatum> mealData) {
        this.mealData = mealData;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(status);
        dest.writeList(mealData);
    }

    public int describeContents() {
        return 0;
    }

}
