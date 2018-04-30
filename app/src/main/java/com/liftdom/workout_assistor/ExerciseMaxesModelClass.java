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
        mExerciseName = exerciseName;
        mMaxValue = maxValue;
        mIsImperial = isImperial;
        mDate = date;
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

    public boolean isGreater(String newMax, boolean isImperial){
        boolean greater = false;

        if(isImperial == isIsImperial()){
            if(Integer.parseInt(newMax) > Integer.parseInt(getMaxValue())){
                greater = true;
            }
        }else{
            if(!isImperial && isIsImperial()){
                // convert from metric to imperial
                if(metricToImperial(newMax) > Integer.parseInt(getMaxValue())){
                    greater = true;
                }
            }else if(isImperial && !isIsImperial()){
                // convert from imperial to metric
                if(imperialToMetric(newMax) > Integer.parseInt(getMaxValue())){
                    greater = true;
                }
            }
        }

        return greater;
    }

    private int metricToImperial(String input){
        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        return lbsInt;
    }

    private int imperialToMetric(String input){
        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        return kgInt;
    }
}
