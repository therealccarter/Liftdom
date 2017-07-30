package com.liftdom.user_profile;

import org.joda.time.LocalDate;

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
    private String mCurrentXpWithinLevel;

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

    // =================================== begin level up stuff ===================================


    public String getCurrentXpWithinLevel() {
        return mCurrentXpWithinLevel;
    }

    public void setCurrentXpWithinLevel(String mCurrentXpWithinLevel) {
        this.mCurrentXpWithinLevel = mCurrentXpWithinLevel;
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

    public HashMap<String, String> generateXpMap(HashMap<String, List<String>> completedMap){
        // will also set related values, so we'll only have to call one method

        /**
         * So what are we trying to do here?
         * We need to take the daily streak and the completed ex info
         * and generate the XP gain accordingly.
         * Then, we'll return the:
         * Daily completion streak, streak multiplier,
         * XP from workout, and full XP gain.
         */

        HashMap<String, String> resultsMap = new HashMap<>();
        int xpFromWorkout = 0;

        // set up last completed day/streak stuff
        if(getLastCompletedDay() == null){
            setCurrentStreak("1");
            resultsMap.put("currentStreak", "1");
        }else{
            LocalDate lastCompletedDay = LocalDate.parse(getLastCompletedDay());

            if(lastCompletedDay == LocalDate.now().minusDays(1)){
                int currentStreak = Integer.parseInt(getCurrentStreak());
                currentStreak++;
                setCurrentStreak(String.valueOf(currentStreak));
                resultsMap.put("currentStreak", getCurrentStreak());
            }else{
                setCurrentStreak("1");
                resultsMap.put("currentStreak", "1");
            }
        }

        // get xp from workout
        if(completedMap == null){
            double constant = 0.023;
            double xpFromWorkoutDouble = Double.parseDouble(getPowerLevel()) * Double.parseDouble(getPowerLevel()) *
                    constant;
            xpFromWorkoutDouble = xpFromWorkoutDouble * 100;
            xpFromWorkout = (int) Math.round(xpFromWorkoutDouble);
            resultsMap.put("xpFromWorkout", String.valueOf(xpFromWorkout));
        }else{
            // call method to get xp based on completed ex map
            xpFromWorkout = getXpFromMap(completedMap);
            resultsMap.put("xpFromWorkout", String.valueOf(xpFromWorkout));
        }

        // call method to get multiplier
        double multiplier = getMultiplier(Integer.parseInt(getCurrentStreak()));
        resultsMap.put("streakMultiplier", String.valueOf(multiplier));

        // call method to apply multiplier to xp from workout
        int totalXpGained = generateTotalXpGained(xpFromWorkout, multiplier);
        resultsMap.put("totalXpGained", String.valueOf(totalXpGained));

        // return map
        return resultsMap;
    }

    private int getXpFromMap(HashMap<String, List<String>> completedMap){
        int xpFromWorkout = 0;

        //int xpFromWorkoutInt = (int) Math.round(xpFromWorkout);

        return xpFromWorkout;
    }

    private double getMultiplier(int streak){
        double multiplier = 0;

        if(streak > 0 && streak < 5){
            multiplier = 1.0;
        }else if(streak > 4 && streak < 10){
            multiplier = 1.5;
        }else if(streak > 9 && streak < 15){
            multiplier = 2.0;
        }else if(streak > 14 && streak < 20){
            multiplier = 2.5;
        }else if(streak > 19 && streak < 25){
            multiplier = 3.0;
        }else if(streak > 24 && streak < 30){
            multiplier = 3.5;
        }else if(streak > 29 && streak < 35){
            multiplier = 4.0;
        }else if(streak > 34 && streak < 40){
            multiplier = 4.5;
        }else if(streak > 39 && streak < 45){
            multiplier = 5.0;
        }else if(streak > 44){
            multiplier = 5.5;
        }


        return multiplier;
    }

    private int generateTotalXpGained(int xpFromWorkout, double multiplier){
        int totalXpGained = 0;

        double totalGainedDouble = xpFromWorkout * multiplier;

        totalXpGained = (int) Math.round(totalGainedDouble);

        return totalXpGained;
    }


    // =================================== end level up stuff ===================================

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

    public String getCurrentFocus() {
        return mCurrentFocus;
    }

    public void setCurrentFocus(String mCurrentFocus) {
        this.mCurrentFocus = mCurrentFocus;
    }
}
