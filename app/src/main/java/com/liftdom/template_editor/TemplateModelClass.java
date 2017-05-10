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
    private String mDescription;
    private HashMap<String, List<String>> mMondayMap;
    private HashMap<String, List<String>> mTuesdayMap;
    private HashMap<String, List<String>> mWednesdayMap;
    private HashMap<String, List<String>> mThursdayMap;
    private HashMap<String, List<String>> mFridayMap;
    private HashMap<String, List<String>> mSaturdayMap;
    private HashMap<String, List<String>> mSundayMap;
    private boolean mIsAlgorithm;
    private HashMap<String, List<String>> mAlgorithmInfo;


    public TemplateModelClass(){
        //necessary for Firebase
    }

    public TemplateModelClass(String templateName, String userId, String userName, boolean isPublic,
                              String dateCreated, String description,
                              HashMap<String, List<String>> mondayMap,
                              HashMap<String, List<String>> tuesdayMap,
                              HashMap<String, List<String>> wednesdayMap,
                              HashMap<String, List<String>> thursdayMap,
                              HashMap<String, List<String>> fridayMap,
                              HashMap<String, List<String>> saturdayMap,
                              HashMap<String, List<String>> sundayMap,
                              boolean isAlgorithm, HashMap<String, List<String>> algorithmInfo){

        mTemplateName = templateName;
        mUserId = userId;
        mUserName = userName;
        mDescription = description;
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

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
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

    public HashMap<String, List<String>> getMondayMap() {
        return mMondayMap;
    }

    public void setMondayMap(HashMap<String, List<String>> mondayMap) {
        mMondayMap = mondayMap;
    }

    public HashMap<String, List<String>> getTuesdayMap() {
        return mTuesdayMap;
    }

    public void setTuesdayMap(HashMap<String, List<String>> tuesdayMap) {
        mTuesdayMap = tuesdayMap;
    }

    public HashMap<String, List<String>> getWednesdayMap() {
        return mWednesdayMap;
    }

    public void setWednesdayMap(HashMap<String, List<String>> wednesdayMap) {
        mWednesdayMap = wednesdayMap;
    }

    public HashMap<String, List<String>> getThursdayMap() {
        return mThursdayMap;
    }

    public void setThursdayMap(HashMap<String, List<String>> thursdayMap) {
        mThursdayMap = thursdayMap;
    }

    public HashMap<String, List<String>> getFridayMap() {
        return mFridayMap;
    }

    public void setFridayMap(HashMap<String, List<String>> fridayMap) {
        mFridayMap = fridayMap;
    }

    public HashMap<String, List<String>> getSaturdayMap() {
        return mSaturdayMap;
    }

    public void setSaturdayMap(HashMap<String, List<String>> saturdayMap) {
        mSaturdayMap = saturdayMap;
    }

    public HashMap<String, List<String>> getSundayMap() {
        return mSundayMap;
    }

    public void setSundayMap(HashMap<String, List<String>> sundayMap) {
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




