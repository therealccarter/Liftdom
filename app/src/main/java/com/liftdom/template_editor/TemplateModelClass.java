package com.liftdom.template_editor;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.*;

/**
 * Created by Brodin on 5/9/2017.
 */

public class TemplateModelClass {

    private String mTemplateName;
    private String mDays;
    private String mUserId;
    private String mUserName;
    private String mUserId2;
    private String mUserName2;
    private String mDateCreated;
    private String mDateUpdated;
    private String mWorkoutType;
    private String mDescription;
    private HashMap<String, List<String>> mMapOne;
    private HashMap<String, List<String>> mMapTwo;
    private HashMap<String, List<String>> mMapThree;
    private HashMap<String, List<String>> mMapFour;
    private HashMap<String, List<String>> mMapFive;
    private HashMap<String, List<String>> mMapSix;
    private HashMap<String, List<String>> mMapSeven;
    private boolean mIsAlgorithm;
    private boolean mIsAlgoApplyToAll;
    private HashMap<String, List<String>> mAlgorithmInfo;
    private HashMap<String, List<String>> mAlgorithmDateMap;
    private HashMap<String, String> mExtraInfo;
    private String publicTemplateKeyId;
    private boolean mIsImperial;
    private List<String> mHasReppedList;
    private String mRestTime;
    private boolean mIsActiveRestTimer;
    private boolean mIsPublic;
    private String mVibrationTime;
    private boolean mIsRestTimerAlert;
    private boolean mIsEdit;


    public TemplateModelClass(){
        //necessary for Firebase
    }

    public TemplateModelClass(String templateName, String days, String userId, String userName,
                              String userId2, String userName2, boolean isPublic,
                              String dateCreated, String dateUpdated,
                              String workoutType, String description,
                              HashMap<String, List<String>> mapOne,
                              HashMap<String, List<String>> mapTwo,
                              HashMap<String, List<String>> mapThree,
                              HashMap<String, List<String>> mapFour,
                              HashMap<String, List<String>> mapFive,
                              HashMap<String, List<String>> mapSix,
                              HashMap<String, List<String>> mapSeven,
                              boolean isAlgorithm, boolean isAlgoApplyToAll,
                              HashMap<String, List<String>> algorithmInfo,
                              HashMap<String, List<String>> algorithmDateMap,
                              boolean isImperial,
                              List<String> hasReppedList,
                              String restTime,
                              boolean isActiveRestTimer,
                              String vibrationTime,
                              boolean isRestTimerAlert){

        mTemplateName = templateName;
        mDays = days;
        mUserId = userId;
        mUserName = userName;
        mUserId2 = userId2;
        mUserName2 = userName2;
        mDescription = description;
        mIsPublic = isPublic;
        mDateCreated = dateCreated;
        mDateUpdated = dateUpdated;
        mWorkoutType = workoutType;
        mMapOne = mapOne;
        mMapTwo = mapTwo;
        mMapThree = mapThree;
        mMapFour = mapFour;
        mMapFive = mapFive;
        mMapSix = mapSix;
        mMapSeven = mapSeven;
        mIsAlgorithm = isAlgorithm;
        mIsAlgoApplyToAll = isAlgoApplyToAll;
        mAlgorithmInfo = algorithmInfo;
        mAlgorithmDateMap = algorithmDateMap;
        mIsImperial = isImperial;
        mRestTime = restTime;
        mIsActiveRestTimer = isActiveRestTimer;
        mVibrationTime = vibrationTime;
        mIsRestTimerAlert = isRestTimerAlert;
        try{
            mHasReppedList = hasReppedList;
        }catch (NullPointerException e){

        }
    }

    public int getMapCount(){
        int count = 0;
        if(getMapOne() != null){
            if(!getMapOne().isEmpty()){
                count = 1;
            }
        }
        if(getMapTwo() != null){
            if(!getMapTwo().isEmpty()){
                count = 2;
            }
        }
        if(getMapThree() != null){
            if(!getMapThree().isEmpty()){
                count = 3;
            }
        }
        if(getMapFour() != null){
            if(!getMapFour().isEmpty()){
                count = 4;
            }
        }
        if(getMapFive() != null){
            if(!getMapFive().isEmpty()){
                count = 5;
            }
        }
        if(getMapSix() != null){
            if(!getMapSix().isEmpty()){
                count = 6;
            }
        }
        if(getMapSeven() != null){
            if(!getMapSeven().isEmpty()){
                count = 7;
            }
        }
        return count;
    }

    public boolean isIsEdit() {
        return mIsEdit;
    }

    public void setIsEdit(boolean mIsEdit) {
        this.mIsEdit = mIsEdit;
    }

    public String getVibrationTime() {
        return mVibrationTime;
    }

    public void setVibrationTime(String mVibrationTime) {
        this.mVibrationTime = mVibrationTime;
    }

    public boolean isIsRestTimerAlert() {
        return mIsRestTimerAlert;
    }

    public void setIsRestTimerAlert(boolean mIsRestTimerAlert) {
        this.mIsRestTimerAlert = mIsRestTimerAlert;
    }

    public String getRestTime() {
        return mRestTime;
    }

    public void setRestTime(String mRestTime) {
        this.mRestTime = mRestTime;
    }

    public boolean isIsActiveRestTimer() {
        return mIsActiveRestTimer;
    }

    public void setIsActiveRestTimer(boolean mIsActiveRestTimer) {
        this.mIsActiveRestTimer = mIsActiveRestTimer;
    }

    public HashMap<String, String> getExtraInfo() {
        return mExtraInfo;
    }

    public void setExtraInfo(HashMap<String, String> mExtraInfo) {
        this.mExtraInfo = mExtraInfo;
    }

    public boolean isIsImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
    }

    public String getPublicTemplateKeyId() {
        return publicTemplateKeyId;
    }

    public void setPublicTemplateKeyId(String publicTemplateKeyId) {
        this.publicTemplateKeyId = publicTemplateKeyId;
    }

    public HashMap<String, List<String>> getAlgorithmDateMap() {
        return mAlgorithmDateMap;
    }

    public void setAlgorithmDateMap(HashMap<String, List<String>> mAlgorithmDateMap) {
        this.mAlgorithmDateMap = mAlgorithmDateMap;
    }

    public void setNewDateMapValues(String exName, String isCompareOldDateBool, String daysUnformatted){
        String key = null;
        String oldDate = null;

        if(Boolean.parseBoolean(isCompareOldDateBool)){
            for(Map.Entry<String, List<String>> map : getAlgorithmDateMap().entrySet()){
                if(isToday(map.getValue().get(3))) {
                    if (map.getValue().get(0).equals(exName)) {
                        if (!Boolean.parseBoolean(map.getValue().get(2))) {
                            key = map.getKey();
                            oldDate = map.getValue().get(1);
                        }
                    }
                }
            }

            if(key != null){
                HashMap<String, List<String>> newMap = new HashMap<>();
                newMap.putAll(getAlgorithmDateMap());
                List<String> newList = new ArrayList<>();
                newList.add(exName);
                newList.add(oldDate);
                newList.add("true");
                newList.add(daysUnformatted);
                newList.add(LocalDate.now().toString());
                newMap.put(key, newList);
                setAlgorithmDateMap(newMap);
            }else{
                HashMap<String, List<String>> newMap = new HashMap<>();
                newMap.putAll(getAlgorithmDateMap());
                List<String> newList = new ArrayList<>();
                newList.add(exName);
                if(oldDate == null){
                    newList.add(LocalDate.now().toString());
                }else{
                    newList.add(oldDate);
                }
                newList.add("true");
                newList.add(daysUnformatted);
                newList.add(LocalDate.now().toString());
                newMap.put(getAlgorithmDateMap().size() + "_key", newList);
                setAlgorithmDateMap(newMap);
            }
        }
    }

    public String getDateUpdated() {
        return mDateUpdated;
    }

    public void setDateUpdated(String mDateUpdated) {
        this.mDateUpdated = mDateUpdated;
    }

    public String getWorkoutType() {
        return mWorkoutType;
    }

    public void setWorkoutType(String mWorkoutType) {
        this.mWorkoutType = mWorkoutType;
    }

    public void updateAlgorithmDateMap(String key, String exName, String isCompareOldDateBool){
        /**
         * if the bool is true and WAS false, we set a new date.
         * if the bool is true and WAS true, we keep the date.
         *
         */
        String string = "ayo";
        if(Boolean.parseBoolean(isCompareOldDateBool)){
            // we must compare if it's CURRENTLY false or true
            boolean changeDate = false;
            String subKey = "";
            String oldDate = "";
            for(Map.Entry<String, List<String>> map : getAlgorithmDateMap().entrySet()){
                try {
                    if(isToday(map.getValue().get(3))) {
                        if (map.getValue().get(0).equals(exName)) {
                            if (!Boolean.parseBoolean(map.getValue().get(2))) {
                                // WAS false
                                subKey = map.getKey();
                                changeDate = true;
                                oldDate = map.getValue().get(1);
                            } else {
                                // WAS true
                            }
                        }
                    }
                } catch (IndexOutOfBoundsException e){

                }
            }

            if(changeDate){
                // if the bool is true and WAS false, we keep the date.
                LocalDate newDate = LocalDate.now();
                List<String> subList = new ArrayList<>();
                subList.add(exName);
                subList.add(newDate.toString());
                subList.add("true");
                subList.add(key);
                HashMap<String, List<String>> newMap = new HashMap<>();
                newMap.putAll(getAlgorithmDateMap());
                newMap.remove(subKey);
                if(subList.size() < 5){
                    subList.add(newDate.toString());
                }else{
                    subList.set(4, newDate.toString());
                }
                newMap.put(subKey, subList);
                setAlgorithmDateMap(newMap);
            }else{
                // if the bool is true and WAS true, we keep the date, but update the key.
                LocalDate newDate = LocalDate.now();
                List<String> subList = new ArrayList<>();
                HashMap<String, List<String>> newMap = new HashMap<>();
                newMap.putAll(getAlgorithmDateMap());

                String newKey = "";

                for(Map.Entry<String, List<String>> map : newMap.entrySet()){
                    try{
                        if(isToday(map.getValue().get(3))) {
                            if (map.getValue().get(0).equals(exName)) {
                                newKey = map.getKey();
                                subList.addAll(map.getValue());
                                subList.set(3, key);
                            }
                        }
                    } catch (IndexOutOfBoundsException e){

                    }

                }

                newMap.remove(subKey);
                if(subList.size() < 5){
                    subList.add(newDate.toString());
                }else{
                    subList.set(4, newDate.toString());
                }
                newMap.put(subKey, subList);
                setAlgorithmDateMap(newMap);
            }
        }
    }

    private String intToWeekday(int inc){
        String weekday = "";

        if(inc == 1){
            weekday = "Monday";
        }else if(inc == 2){
            weekday = "Tuesday";
        }else if(inc == 3){
            weekday = "Wednesday";
        }else if(inc == 4){
            weekday = "Thursday";
        }else if(inc == 5){
            weekday = "Friday";
        }else if(inc == 6){
            weekday = "Saturday";
        }else if(inc == 7){
            weekday = "Sunday";
        }

        return weekday;
    }

    private boolean isToday(String dayUnformatted){
        boolean todayBool = false;

        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();

        String today = intToWeekday(currentWeekday);

        String delims = "[_]";
        String[] tokens = dayUnformatted.split(delims);

        for(String string : tokens){
            if(string.equals(today)){
                todayBool = true;
            }
        }

        return todayBool;
    }

    private boolean containsToday(String dayUnformatted, String day){
        boolean contains = false;
        String[] tokens = dayUnformatted.split("_");

        try{
            for(String string : tokens){
                if(string.equals(day)){
                    contains = true;
                }
            }
        } catch (IndexOutOfBoundsException e){

        }

        return contains;
    }

    public String getMapNameForDay(String day){
        String mapName = "";

        if(mMapOne != null){
            if(!mMapOne.isEmpty()){
                if(containsToday(mMapOne.get("0_key").get(0), day)){
                    mapName = "mMapOne";
                }
            }
        }
        if(mMapTwo != null){
            if(!mMapTwo.isEmpty()){
                if(containsToday(mMapTwo.get("0_key").get(0), day)){
                    mapName = "mMapTwo";
                }
            }
        }
        if(mMapThree != null){
            if(!mMapThree.isEmpty()){
                if(containsToday(mMapThree.get("0_key").get(0), day)){
                    mapName = "mMapThree";
                }
            }
        }
        if(mMapFour != null){
            if(!mMapFour.isEmpty()){
                if(containsToday(mMapFour.get("0_key").get(0), day)){
                    mapName = "mMapFour";
                }
            }
        }
        if(mMapFive != null){
            if(!mMapFive.isEmpty()){
                if(containsToday(mMapFive.get("0_key").get(0), day)){
                    mapName = "mMapFive";
                }
            }
        }
        if(mMapSix != null){
            if(!mMapSix.isEmpty()){
                if(containsToday(mMapSix.get("0_key").get(0), day)){
                    mapName = "mMapSix";
                }
            }
        }
        if(mMapSeven != null){
            if(!mMapSeven.isEmpty()){
                if(containsToday(mMapSeven.get("0_key").get(0), day)){
                    mapName = "mMapSeven";
                }
            }
        }

        return mapName;
    }

    public HashMap<String, List<String>> getMapForDay(String day){

        HashMap<String, List<String>> returnMap = new HashMap<>();

        if(mMapOne != null){
            if(!mMapOne.isEmpty()){
                if(containsToday(mMapOne.get("0_key").get(0), day)){
                    returnMap = mMapOne;
                }
            }
        }
        if(mMapTwo != null){
            if(!mMapTwo.isEmpty()){
                if(containsToday(mMapTwo.get("0_key").get(0), day)){
                    returnMap = mMapTwo;
                }
            }
        }
        if(mMapThree != null){
            if(!mMapThree.isEmpty()){
                if(containsToday(mMapThree.get("0_key").get(0), day)){
                    returnMap = mMapThree;
                }
            }
        }
        if(mMapFour != null){
            if(!mMapFour.isEmpty()){
                if(containsToday(mMapFour.get("0_key").get(0), day)){
                    returnMap = mMapFour;
                }
            }
        }
        if(mMapFive != null){
            if(!mMapFive.isEmpty()){
                if(containsToday(mMapFive.get("0_key").get(0), day)){
                    returnMap = mMapFive;
                }
            }
        }
        if(mMapSix != null){
            if(!mMapSix.isEmpty()){
                if(containsToday(mMapSix.get("0_key").get(0), day)){
                    returnMap = mMapSix;
                }
            }
        }
        if(mMapSeven != null){
            if(!mMapSeven.isEmpty()){
                if(containsToday(mMapSeven.get("0_key").get(0), day)){
                    returnMap = mMapSeven;
                }
            }
        }

        return returnMap;
    }

    public String getDays(){
        return mDays;
    }

    public void setDays(String days){
        mDays = days;
    }

    public String getDescription() {
        return mDescription;
    }

    public boolean getIsAlgoApplyToAll() {
        return mIsAlgoApplyToAll;
    }

    public void setIsAlgoApplyToAll(boolean isAlgoApplyToAll){
        mIsAlgoApplyToAll = isAlgoApplyToAll;
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

    public String getUserId2() {
        return mUserId2;
    }

    public void setUserId2(String userId2) {
        mUserId2 = userId2;
    }

    public String getUserName2() {
        return mUserName2;
    }

    public void setUserName2(String userName2) {
        mUserName2 = userName2;
    }

    public boolean isIsPublic() {
        return mIsPublic;
    }

    public void setIsPublic(boolean isPublic) {
        this.mIsPublic = isPublic;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public HashMap<String, List<String>> getMapOne() {
        return mMapOne;
    }

    public void setMapOne(HashMap<String, List<String>> mapOne) {
        mMapOne = mapOne;
    }

    public HashMap<String, List<String>> getMapTwo() {
        return mMapTwo;
    }

    public void setMapTwo(HashMap<String, List<String>> mapTwo) {
        mMapTwo = mapTwo;
    }

    public HashMap<String, List<String>> getMapThree() {
        return mMapThree;
    }

    public void setMapThree(HashMap<String, List<String>> mapThree) {
        mMapThree = mapThree;
    }

    public HashMap<String, List<String>> getMapFour() {
        return mMapFour;
    }

    public void setMapFour(HashMap<String, List<String>> mapFour) {
        mMapFour = mapFour;
    }

    public HashMap<String, List<String>> getMapFive() {
        return mMapFive;
    }

    public void setMapFive(HashMap<String, List<String>> mapFive) {
        mMapFive = mapFive;
    }

    public HashMap<String, List<String>> getMapSix() {
        return mMapSix;
    }

    public void setMapSix(HashMap<String, List<String>> mapSix) {
        mMapSix = mapSix;
    }

    public HashMap<String, List<String>> getMapSeven() {
        return mMapSeven;
    }

    public void setMapSeven(HashMap<String, List<String>> mapSeven) {
        mMapSeven = mapSeven;
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




