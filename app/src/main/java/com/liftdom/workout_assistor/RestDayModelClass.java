package com.liftdom.workout_assistor;

/**
 * Created by Brodin on 3/16/2018.
 */

public class RestDayModelClass {

    private boolean mIsRestDay;
    private String mDate;
    private String mRefKey;
    private boolean mIsRevise;

    public RestDayModelClass(){
        // for firebase
    }

    public RestDayModelClass(boolean isRestDay, String date, String refKey, boolean isRevise){
        mIsRestDay = isRestDay;
        mDate = date;
        mRefKey = refKey;
        mIsRevise = isRevise;
    }

    public boolean isIsRestDay() {
        return mIsRestDay;
    }

    public void setIsRestDay(boolean mIsRestDay) {
        this.mIsRestDay = mIsRestDay;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public boolean isIsRevise() {
        return mIsRevise;
    }

    public void setIsRevise(boolean mIsRevise) {
        this.mIsRevise = mIsRevise;
    }
}
