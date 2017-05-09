package com.liftdom.template_editor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/9/2017.
 */

public class TemplateModelClass {

    private String mTemplateName;
    private String mUserId;
    private String mUserName;
    private boolean mIsPublic;
    private String mDateCreated;
    private HashMap<Integer, List<String>> mMondayMap;
    private HashMap<Integer, List<String>> mTuesdayMap;
    private HashMap<Integer, List<String>> mWednesdayMap;
    private HashMap<Integer, List<String>> mThursdayMap;
    private HashMap<Integer, List<String>> mFridayMap;
    private HashMap<Integer, List<String>> mSaturdayMap;
    private HashMap<Integer, List<String>> mSundayMap;
    private boolean mIsAlgorithm;
    private HashMap<String, List<String>> mAlgorithmInfo;


    public TemplateModelClass(){
        //necessary for Firebase
    }

    public TemplateModelClass(String templateName, String userId, String userName, boolean isPublic,
                              String dateCreated,
                              HashMap<Integer, List<String>> mondayMap,
                              HashMap<Integer, List<String>> tuesdayMap,
                              HashMap<Integer, List<String>> wednesdayMap,
                              HashMap<Integer, List<String>> thursdayMap,
                              HashMap<Integer, List<String>> fridayMap,
                              HashMap<Integer, List<String>> saturdayMap,
                              HashMap<Integer, List<String>> sundayMap,
                              boolean isAlgorithm, HashMap<String, List<String>> algorithmInfo){

        mTemplateName = templateName;
        mUserId = userId;
        mUserName = userName;
        mIsPublic = isPublic;
        mDateCreated = dateCreated;
        mMondayMap = mondayMap;
        mTuesdayMap = tuesdayMap;
        mWednesdayMap = wednesdayMap;
        mThursdayMap = thursdayMap;
        mFridayMap = fridayMap;
        mSaturdayMap = saturdayMap;
        mSundayMap = sundayMap;
        mIsAlgorithm = isAlgorithm;
        mAlgorithmInfo = algorithmInfo;
    }

    public String getTemplateName() {
        return mTemplateName;
    }

    public void setTemplateName(String templateName) {
        mTemplateName = templateName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public boolean isPublic() {
        return mIsPublic;
    }

    public void setIsPublic(boolean isPublic) {
        mIsPublic = isPublic;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public HashMap<Integer, List<String>> getMondayMap() {
        return mMondayMap;
    }

    public void setMondayMap(HashMap<Integer, List<String>> mondayMap) {
        mMondayMap = mondayMap;
    }

    public HashMap<Integer, List<String>> getTuesdayMap() {
        return mTuesdayMap;
    }

    public void setTuesdayMap(HashMap<Integer, List<String>> tuesdayMap) {
        mTuesdayMap = tuesdayMap;
    }

    public HashMap<Integer, List<String>> getWednesdayMap() {
        return mWednesdayMap;
    }

    public void setWednesdayMap(HashMap<Integer, List<String>> wednesdayMap) {
        mWednesdayMap = wednesdayMap;
    }

    public HashMap<Integer, List<String>> getThursdayMap() {
        return mThursdayMap;
    }

    public void setThursdayMap(HashMap<Integer, List<String>> thursdayMap) {
        mThursdayMap = thursdayMap;
    }

    public HashMap<Integer, List<String>> getFridayMap() {
        return mFridayMap;
    }

    public void setFridayMap(HashMap<Integer, List<String>> fridayMap) {
        mFridayMap = fridayMap;
    }

    public HashMap<Integer, List<String>> getSaturdayMap() {
        return mSaturdayMap;
    }

    public void setSaturdayMap(HashMap<Integer, List<String>> saturdayMap) {
        mSaturdayMap = saturdayMap;
    }

    public HashMap<Integer, List<String>> getSundayMap() {
        return mSundayMap;
    }

    public void setSundayMap(HashMap<Integer, List<String>> sundayMap) {
        mSundayMap = sundayMap;
    }

    public boolean getIsAlgorithm() {
        return mIsAlgorithm;
    }

    public void setIsAlgorithm(boolean isAlgorithm) {
        mIsAlgorithm = isAlgorithm;
    }

    public HashMap<String, List<String>> getAlgorithmInfo() {
        return mAlgorithmInfo;
    }

    public void setAlgorithmInfo(HashMap<String, List<String>> algorithmInfo) {
        mAlgorithmInfo = algorithmInfo;
    }
}




