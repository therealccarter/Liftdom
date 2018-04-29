package com.liftdom.workout_assistor;

/**
 * Created by Brodin on 4/26/2018.
 */
public class ExerciseMaxesModelClass {

    // we'll have the key be the name of the exercise
    String mExerciseName;
    String mMaxValue;
    boolean mIsImperial;
    String mDate;

    public ExerciseMaxesModelClass(){
        // for firebase
    }

    public ExerciseMaxesModelClass(String exerciseName, String maxValue, boolean isImperial, String date){

    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String mExerciseName) {
        this.mExerciseName = mExerciseName;
    }

    public String getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(String mMaxValue) {
        this.mMaxValue = mMaxValue;
    }

    public boolean isIsImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }
}
