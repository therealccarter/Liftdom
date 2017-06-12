package com.liftdom.liftdom.utils;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 6/12/2017.
 */

public class WorkoutHistoryModelClass {

    private String mPublicDescription;
    private String mPrivateJournal;
    private String mDate;
    private String mMediaRef;
    private HashMap<String, List<String>> mWorkoutInfoMap;

    public WorkoutHistoryModelClass(){
        // necessary for Firebase
    }

    public WorkoutHistoryModelClass(String publicDescription, String privateJournal, String date, String mediaRef,
                                    HashMap<String, List<String>> workoutInfoMap){
        mPublicDescription = publicDescription;
        mPrivateJournal = privateJournal;
        mDate = date;
        mMediaRef = mediaRef;
        mWorkoutInfoMap = workoutInfoMap;
    }

    public String getPublicDescription() {
        return mPublicDescription;
    }

    public void setPublicDescription(String mPublicDescription) {
        this.mPublicDescription = mPublicDescription;
    }

    public String getPrivateJournal() {
        return mPrivateJournal;
    }

    public void setPrivateJournal(String mPrivateJournal) {
        this.mPrivateJournal = mPrivateJournal;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public String getMediaRef() {
        return mMediaRef;
    }

    public void setMediaRef(String mMediaRef) {
        this.mMediaRef = mMediaRef;
    }

    public HashMap<String, List<String>> getWorkoutInfoMap() {
        return mWorkoutInfoMap;
    }

    public void setWorkoutInfoMap(HashMap<String, List<String>> mWorkoutInfoMap) {
        this.mWorkoutInfoMap = mWorkoutInfoMap;
    }
}
