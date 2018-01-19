package com.liftdom.workout_assistor;

import android.content.Intent;
import android.util.Log;

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
    private boolean mIsTemplateImperial;
    private String mViewCursor; // the current set we're on

    public WorkoutProgressModelClass(){
        // necessary for Firebase
    }

    public WorkoutProgressModelClass(String date, boolean completedBool, HashMap<String, HashMap<String, List<String>>> exInfoHashMap,
                                     String privateJournal, String publicComment, String mediaResource, boolean
                                             isTemplateImperial){
        mDate = date;
        mCompletedBool = completedBool;
        mExInfoHashMap = exInfoHashMap;
        mPrivateJournal = privateJournal;
        mPublicComment = publicComment;
        mMediaResource = mediaResource;
        mIsTemplateImperial = isTemplateImperial;
    }

    public String exNameForCursor(){
        String exName;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        exName = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(0);

        return exName;
    }

    public String setForCursor(){
        String set;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        set = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt(tokens[2]));

        return set;
    }

    public void next(){

    }

    public void previous(){

    }

    public void toggleCheck(){
        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        String currentSet = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt(tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet);

        String[] tokens2 = currentSet.split(delims);

        if(tokens2[1].equals("checked")){
            tokens2[1] = "unchecked";
        }else{
            tokens2[1] = "checked";
        }

        String newString = "";

        for(int i = 0; i < tokens2.length; i++){
            if(i == 0){
                newString = tokens2[i];
            }else{
                newString = newString + "_" + tokens2[i];
            }
        }

        getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").set(Integer.parseInt(tokens[2]), newString);
        String currentSet2 = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt
                (tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet2);


    }

    public String getViewCursor() {
        return mViewCursor;
    }

    public void setViewCursor(String mViewCursor) {
        this.mViewCursor = mViewCursor;
    }

    public boolean isIsTemplateImperial() {
        return mIsTemplateImperial;
    }

    public void setIsTemplateImperial(boolean mIsTemplateImperial) {
        this.mIsTemplateImperial = mIsTemplateImperial;
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
