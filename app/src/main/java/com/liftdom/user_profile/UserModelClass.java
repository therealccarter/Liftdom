package com.liftdom.user_profile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 6/18/2017.
 */

public class UserModelClass {

    private String mUserName;
    private String mUserId;
    private String mEmail;
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
    private String mCurrentStreak;
    private String mCurrentFocus;
    private String mActiveTemplate;
    private List<String> followerList;
    private List<String> followingList;
    private String mLastCompletedDay;

    public UserModelClass(){
        // necessary for firebase
    }

    public UserModelClass(String userName, String userId, String email, String age, boolean isImperial,
                          String feetInchesHeight, String cmHeight, String pounds,
                          String kgs, HashMap<String, String> maxList, String sex,
                          String repLevel, String powerLevel, String currentStreak, String currentFocus, String
                                  activeTemplate){
        mUserName = userName;
        mUserId = userId;
        mEmail = email;
        mAge = age;
        mIsImperial = isImperial;
        mFeetInchesHeight = feetInchesHeight;
        mCmHeight = cmHeight;
        mPounds = pounds;
        mKgs = kgs;
        mMaxList = maxList;
        mSex = sex;
        mRepLevel = repLevel;
        mPowerLevel = powerLevel;
        mCurrentStreak = currentStreak;
        mCurrentFocus = currentFocus;
        mActiveTemplate = activeTemplate;

        updateUnits(isImperial);
    }



    public String getCurrentStreak() {
        return mCurrentStreak;
    }

    public void setCurrentStreak(String mCurrentStreak) {
        this.mCurrentStreak = mCurrentStreak;
    }

    public void addToCurrentStreak(){
        int streak1 = Integer.parseInt(getCurrentStreak());
        int streak2 = streak1 + 1;
        setCurrentStreak(String.valueOf(streak2));
    }

    public void resetCurrentStreak(){
        setCurrentStreak("1");
    }

    public String getLastCompletedDay() {
        return mLastCompletedDay;
    }

    public void setLastCompletedDay(String mLastCompletedDay) {
        this.mLastCompletedDay = mLastCompletedDay;
    }

    public void updateUnits(boolean isImperial){
        if(isImperial){
            // convert imperials to metrics
            String[] heightTokens = mFeetInchesHeight.split("_");
            int fullInches = (Integer.parseInt(heightTokens[0]) * 12) + Integer.parseInt(heightTokens[1]);
            double unCm = (double) fullInches * 2.54;
            int newCm = (int) Math.round(unCm);
            mCmHeight = String.valueOf(newCm);

            double unKg = (double) Integer.parseInt(mPounds) * 0.453592;
            int newKg = (int) Math.round(unKg);
            mKgs = String.valueOf(newKg);
        }else{
            // convert metrics to imperials
            double cm = Double.parseDouble(mCmHeight);
            double unInches = cm * 0.393701;
            int newInches = (int) Math.round(unInches);

            int feet = newInches / 12;
            int inches = newInches % 12;
            mFeetInchesHeight = String.valueOf(feet) + "_" + String.valueOf(inches);

            double kgDouble = Double.parseDouble(mKgs);
            double unPounds = kgDouble * 2.2;
            int newPounds = (int) Math.round(unPounds);
            mPounds = String.valueOf(newPounds);
        }
    }

    public List<String> getFollowerList() {
        return followerList;
    }

    public void setFollowerList(List<String> followerList) {
        this.followerList = followerList;
    }

    public List<String> getFollowingList() {
        return followingList;
    }

    public void setFollowingList(List<String> followingList) {
        this.followingList = followingList;
    }

    public String getActiveTemplate() {
        return mActiveTemplate;
    }

    public void setActiveTemplate(String mActiveTemplate) {
        this.mActiveTemplate = mActiveTemplate;
    }

    public String getEmail() {
        return mEmail;
    }

    public void setEmail(String mEmail) {
        this.mEmail = mEmail;
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

    public boolean isIsImperial() {
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

    public void addToPowerLevel(int powerLevels){
        int current = Integer.parseInt(getPowerLevel());
        current = current + powerLevels;
        setPowerLevel(String.valueOf(current));
    }

    public String getCurrentFocus() {
        return mCurrentFocus;
    }

    public void setCurrentFocus(String mCurrentFocus) {
        this.mCurrentFocus = mCurrentFocus;
    }
}
