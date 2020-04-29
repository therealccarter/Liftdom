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
    private String mNotificationCount;
    private boolean mIsGDPR;

    public UserModelClass(){
        // necessary for firebase
    }

    public UserModelClass(String userName, String userId, String email, String age, boolean isImperial,
                          String feetInchesHeight, String cmHeight, String pounds,
                          String kgs, HashMap<String, String> maxList, String sex,
                          String repLevel, String powerLevel, String currentStreak, String currentFocus,
                          String activeTemplate, String notificationCount, boolean isGDPR){
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
        try{
            mNotificationCount = notificationCount;
            mIsGDPR = isGDPR;
        }catch (NullPointerException e){

        }

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

    public HashMap<String, String> generateXpMap(HashMap<String, List<String>> completedMap, boolean isRevised){
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



        // need to prevent user from gaining xp if from revised workout

        if(isRevised){
            resultsMap.put("currentStreak", getCurrentStreak());
            resultsMap.put("xpFromWorkout", "0");
            double multiplier = getMultiplier(Integer.parseInt(getCurrentStreak()));
            resultsMap.put("streakMultiplier", String.valueOf(multiplier));
            resultsMap.put("totalXpGained", "0");
        }else{
            // set up last completed day/streak stuff
            if(getLastCompletedDay() == null){
                setCurrentStreak("1");
                resultsMap.put("currentStreak", "1");
                setLastCompletedDay(LocalDate.now().toString());
            }else{
                //LocalDate lastCompletedDay = LocalDate.parse(getLastCompletedDay());
                if(getLastCompletedDay().equals(LocalDate.now().minusDays(1).toString())){
                    int currentStreak = Integer.parseInt(getCurrentStreak());
                    currentStreak++;
                    setCurrentStreak(String.valueOf(currentStreak));
                    resultsMap.put("currentStreak", getCurrentStreak());
                }else{
                    setCurrentStreak("1");
                    resultsMap.put("currentStreak", "1");
                }
                setLastCompletedDay(LocalDate.now().toString());
            }

            boolean hasNoCompletedExercises = true;
            if(completedMap != null){
                for(Map.Entry<String, List<String>> mapEntry : completedMap.entrySet()){
                    if(mapEntry.getValue().size() > 1){
                        hasNoCompletedExercises = false;
                    }
                }
            }

            // get xp from workout
            if(completedMap == null || hasNoCompletedExercises){
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

            if(getCurrentXpWithinLevel() == null){
                setCurrentXpWithinLevel("0");
            }

            setPowerLevelWithXp(totalXpGained, Integer.parseInt(getPowerLevel()));
        }

        // return map
        return resultsMap;
    }

    private void setPowerLevelWithXp(int totalXpGained, int currentPowerLevel){

        int newCurrentXpWithinLevel = 0;

        for(int i = 0; i < 50; i++){
            int goalXp = generateGoalXp(currentPowerLevel);
            if(i == 0){
                if(Integer.parseInt(getCurrentXpWithinLevel()) + totalXpGained >= goalXp){
                    currentPowerLevel++;
                    int xpLeftInCurrent = goalXp - Integer.parseInt(getCurrentXpWithinLevel()); // 1
                    newCurrentXpWithinLevel = totalXpGained - xpLeftInCurrent;

                }else{
                    int newXpWithinLevel = Integer.parseInt(getCurrentXpWithinLevel()) + totalXpGained;
                    setCurrentXpWithinLevel(String.valueOf(newXpWithinLevel));
                    break;
                }
            }else{
                if(newCurrentXpWithinLevel >= goalXp){
                    currentPowerLevel++;
                    int xpLeftInCurrent = goalXp - newCurrentXpWithinLevel; // 1
                    newCurrentXpWithinLevel = totalXpGained - xpLeftInCurrent;
                }else{
                    setCurrentXpWithinLevel(String.valueOf(newCurrentXpWithinLevel));
                    setPowerLevel(String.valueOf(currentPowerLevel));
                    break;
                }
            }
        }
    }

    private int generateGoalXp(int powerLevel){
        //int powerLevelInt = Integer.parseInt(powerLevel);
        double powerXP = (powerLevel * powerLevel) * 1.3;
        powerXP = powerXP * 100;
        return (int) Math.round(powerXP);
    }

    private double getScalingBonus(){
        int powerLevel = Integer.parseInt(getPowerLevel());
        double bonus = 0;

        bonus = generateGoalXp(powerLevel) * 0.03;

        return bonus;
    }

    private int getXpFromMap(HashMap<String, List<String>> completedMap){
        int xpFromWorkout = 0;
        // So eventually we'll have both an objective and subjective metric
        // objective being max/poundage compared to greats,
        // subjective being max/poundage compared to your past.

        // handle sets
        double dub = getTotalSets(completedMap) + (getTotalReps(completedMap) / 2) + (getTotalPoundage(completedMap) *
                0.09) + getScalingBonus();

        xpFromWorkout = (int) Math.round(dub);

        // handle reps

        return xpFromWorkout;
    }

    private int getTotalSets(HashMap<String, List<String>> map){
        int totalSets = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String string : entry.getValue()) {
                if (!isExerciseName(string)) {
                    totalSets++;
                }
            }
        }

        return totalSets;
    }

    private int getTotalReps(HashMap<String, List<String>> map){
        int totalReps = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            for (String string : entry.getValue()) {
                if (!isExerciseName(string)) {
                    String delims = "[@,_]";
                    String tokens[] = string.split(delims);

                    int int1 = Integer.parseInt(tokens[0]);

                    totalReps = totalReps + int1;
                }
            }
        }

        return totalReps;
    }

    private int getTotalPoundage(HashMap<String, List<String>> map){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            for(String string : entry.getValue()){
                if(!isExerciseName(string)){
                    String delims = "[@,_]";
                    String tokens[] = string.split(delims);
                    int int1 = Integer.parseInt(tokens[0]);
                    double int2;
                    if(tokens[1].equals("B.W.")){
                        if(isIsImperial()){
                            int2 = Integer.parseInt(getPounds()) / 4;
                        }else{
                            int2 = Integer.parseInt(getKgs()) / 2;
                        }
                    }else{
                        int2 = Double.parseDouble(tokens[1]);
                    }
                    int int3 = int1 * (int) int2;
                    totalPoundage = totalPoundage + int3;
                }
            }

        }

        return totalPoundage;
    }

    private int getTotalPoundageForEx(HashMap<String, List<String>> map, String exName){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            if(entry.getValue().get(0).equals(exName)){
                for(String string : entry.getValue()){
                    if(!isExerciseName(string)){
                        String delims = "[@,_]";
                        String tokens[] = string.split(delims);
                        int int1 = Integer.parseInt(tokens[0]);
                        int int2;
                        try{
                            int2 = Integer.parseInt(tokens[1]);
                        }catch (NumberFormatException e){
                            int2 = 0;
                        }

                        //if(tokens[1].equals("B.W.")){
                        //    if(userModelClass.isIsImperial()){
                        //        int2 = Integer.parseInt(userModelClass.getPounds());
                        //    }else{
                        //        int2 = Integer.parseInt(userModelClass.getKgs());
                        //    }
                        //}else{
                        //    int2 = Integer.parseInt(tokens[1]);
                        //}
                        int int3 = int1 * int2;
                        totalPoundage = totalPoundage + int3;
                    }
                }
            }
        }

        return totalPoundage;
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;
    }


    private double getMultiplier(int streak){
        double multiplier = 0;

        if(streak > 0 && streak < 3){
            multiplier = 1.0;
        }else if(streak > 2 && streak < 6){
            multiplier = 1.5;
        }else if(streak > 5 && streak < 9){
            multiplier = 2.0;
        }else if(streak > 8 && streak < 12){
            multiplier = 2.5;
        }else if(streak > 11 && streak < 18){
            multiplier = 3.0;
        }else if(streak > 17 && streak < 21){
            multiplier = 3.5;
        }else if(streak > 20 && streak < 24){
            multiplier = 4.0;
        }else if(streak > 23 && streak < 27){
            multiplier = 4.5;
        }else if(streak > 26 && streak < 30){
            multiplier = 5.0;
        }else if(streak > 32){
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


    public boolean isIsGDPR() {
        return mIsGDPR;
    }

    public void setIsGDPR(boolean mIsGDPR) {
        this.mIsGDPR = mIsGDPR;
    }

    public String getNotificationCount() {
        return mNotificationCount;
    }

    public void setNotificationCount(String mNotificationCount) {
        this.mNotificationCount = mNotificationCount;
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

    public String getCurrentFocus() {
        return mCurrentFocus;
    }

    public void setCurrentFocus(String mCurrentFocus) {
        this.mCurrentFocus = mCurrentFocus;
    }
}
