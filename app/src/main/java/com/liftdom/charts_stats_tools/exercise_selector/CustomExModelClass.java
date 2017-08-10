package com.liftdom.charts_stats_tools.exercise_selector;

/**
 * Created by Brodin on 8/9/2017.
 */

public class CustomExModelClass {

    private String mExerciseName;
    private String mDescription;
    private String mDateCreated;
    private String mRefKey;

    public CustomExModelClass(){
        // necessary for Firebase ya dig
    }

    public CustomExModelClass(String exerciseName, String description, String dateCreated, String refKey){
        mExerciseName = exerciseName;
        mDescription = description;
        mDateCreated = dateCreated;
        mRefKey = refKey;
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String mExerciseName) {
        this.mExerciseName = mExerciseName;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String mDateCreated) {
        this.mDateCreated = mDateCreated;
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }
}
