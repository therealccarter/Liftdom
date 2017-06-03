package com.liftdom.workout_assistor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/31/2017.
 */

public class WorkoutProgressModelClass {

    private String mDate;
    private boolean mCompletedBool;
    private HashMap<String, HashMap<String, List<String>>> mExInfoHashMap;
    private String mPrivateJournal;
    private String mPublicComment;
    private String mMediaResource;

    public WorkoutProgressModelClass(){
        // necessary for Firebase
    }

    public WorkoutProgressModelClass(String date, boolean completedBool, HashMap<String, HashMap<String, List<String>>> exInfoHashMap,
                                     String privateJournal, String publicComment, String mediaResource){
        mDate = date;
        mCompletedBool = completedBool;
        mExInfoHashMap = exInfoHashMap;
        mPrivateJournal = privateJournal;
        mPublicComment = publicComment;
        mMediaResource = mediaResource;
    }

    public String getMediaResource() {
        return mMediaResource;
    }

    public void setMediaResource(String mMediaResource) {
        this.mMediaResource = mMediaResource;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isCompletedBool() {
        return mCompletedBool;
    }

    public void setCompletedBool(boolean mCompletedBool) {
        this.mCompletedBool = mCompletedBool;
    }

    public HashMap<String, HashMap<String, List<String>>> getExInfoHashMap() {
        return mExInfoHashMap;
    }

    public void setExInfoHashMap(HashMap<String, HashMap<String, List<String>>> mExInfoHashMap) {
        this.mExInfoHashMap = mExInfoHashMap;
    }

    public String getPrivateJournal() {
        return mPrivateJournal;
    }

    public void setPrivateJournal(String mPrivateJournal) {
        this.mPrivateJournal = mPrivateJournal;
    }

    public String getPublicComment() {
        return mPublicComment;
    }

    public void setPublicComment(String mPublicComment) {
        this.mPublicComment = mPublicComment;
    }

}
