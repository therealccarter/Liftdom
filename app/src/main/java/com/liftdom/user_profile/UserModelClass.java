package com.liftdom.user_profile;

import java.util.HashMap;

/**
 * Created by Brodin on 6/18/2017.
 */

public class UserModelClass {

    private String mUserName;
    private String mUserId;
    private String mAge;
    private boolean mIsImperial;
    private String mFeetInchesHeight;
    private String mCmHeight;
    private String mPounds;
    private String mKgs;
    private HashMap<String, String> mMaxList;
    private String mSex;
    private String mRepLevel;
    private String mPowerLevel;
    private String mCurrentFocus;

    public UserModelClass(){
        // necessary for firebase
    }

    public UserModelClass(String userName, String userId, String age, boolean isImperial,
                          String feetInchesHeight, String cmHeight, String pounds,
                          String kgs, HashMap<String, String> maxList, String sex,
                          String repLevel, String powerLevel, String currentFocus){

    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getAge() {
        return mAge;
    }

    public void setAge(String mAge) {
        this.mAge = mAge;
    }

    public boolean isImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
    }

    public String getFeetInchesHeight() {
        return mFeetInchesHeight;
    }

    public void setFeetInchesHeight(String mFeetInchesHeight) {
        this.mFeetInchesHeight = mFeetInchesHeight;
    }

    public String getCmHeight() {
        return mCmHeight;
    }

    public void setCmHeight(String mCmHeight) {
        this.mCmHeight = mCmHeight;
    }

    public String getPounds() {
        return mPounds;
    }

    public void setPounds(String mPounds) {
        this.mPounds = mPounds;
    }

    public String getKgs() {
        return mKgs;
    }

    public void setKgs(String mKgs) {
        this.mKgs = mKgs;
    }

    public HashMap<String, String> getMaxList() {
        return mMaxList;
    }

    public void setMaxList(HashMap<String, String> mMaxList) {
        this.mMaxList = mMaxList;
    }

    public String getSex() {
        return mSex;
    }

    public void setSex(String mSex) {
        this.mSex = mSex;
    }

    public String getRepLevel() {
        return mRepLevel;
    }

    public void setRepLevel(String mRepLevel) {
        this.mRepLevel = mRepLevel;
    }

    public String getPowerLevel() {
        return mPowerLevel;
    }

    public void setPowerLevel(String mPowerLevel) {
        this.mPowerLevel = mPowerLevel;
    }

    public String getCurrentFocus() {
        return mCurrentFocus;
    }

    public void setCurrentFocus(String mCurrentFocus) {
        this.mCurrentFocus = mCurrentFocus;
    }
}
