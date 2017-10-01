package com.liftdom.liftdom.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 6/12/2017.
 */

public class WorkoutHistoryModelClass {

    private String mUserId;
    private String mUserName;
    private String mPublicDescription;
    private String mPrivateJournal;
    private String mDate;
    private String mMediaRef;
    private HashMap<String, List<String>> mWorkoutInfoMap;
    private boolean mIsImperial;

    public WorkoutHistoryModelClass(){
        // necessary for Firebase
    }

    public WorkoutHistoryModelClass(String userId, String userName, String publicDescription, String privateJournal,
                                    String date, String mediaRef,
                                    HashMap<String, List<String>> workoutInfoMap, boolean isImperial){
        mPublicDescription = publicDescription;
        mPrivateJournal = privateJournal;
        mDate = date;
        mMediaRef = mediaRef;
        mWorkoutInfoMap = workoutInfoMap;
        mIsImperial = isImperial;
        mUserId = userId;
        mUserName = userName;
    }

    public double getExPoundage(String exName){
        double poundage = 0.0;

        int listInc = 0;
        ArrayList<List<String>> exInfoList = new ArrayList<>();
        for(Map.Entry<String, List<String>> mapEntry : getWorkoutInfoMap().entrySet()){
            boolean isExBool = false;
            for(String string : mapEntry.getValue()){
                if(isExerciseName(string)){
                    if(string.equals(exName)){
                        isExBool = true;
                        List<String> list = new ArrayList<>();
                        list.add(string);
                        exInfoList.add(list);
                        listInc++;
                    }else{
                        isExBool = false;
                    }
                }else{
                    if(isExBool){
                        exInfoList.get(listInc - 1).add(string);
                    }
                }
            }
        }

        for(List<String> list : exInfoList){
            for(String string : list){
                if(!isExerciseName(string)){
                    String[] tokens = string.split("@");
                    double pounds = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]);
                    poundage = poundage + pounds;
                }
            }
        }

        return poundage;
    }

    public double getExMaxWeightLifted(String exName){
        double maxWeight = 0.0;

        int listInc = 0;
        ArrayList<List<String>> exInfoList = new ArrayList<>();
        for(Map.Entry<String, List<String>> mapEntry : getWorkoutInfoMap().entrySet()){
            boolean isExBool = false;
            for(String string : mapEntry.getValue()){
                if(isExerciseName(string)){
                    if(string.equals(exName)){
                        isExBool = true;
                        List<String> list = new ArrayList<>();
                        list.add(string);
                        exInfoList.add(list);
                        listInc++;
                    }else{
                        isExBool = false;
                    }
                }else{
                    if(isExBool){
                        exInfoList.get(listInc - 1).add(string);
                    }
                }
            }
        }

        for(List<String> list : exInfoList){
            for(String string : list){
                if(!isExerciseName(string)){
                    String[] tokens = string.split("@");
                    double weight = Double.parseDouble(tokens[1]);
                    if(weight > maxWeight){
                        maxWeight = weight;
                    }
                }
            }
        }

        return maxWeight;
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

    public boolean containsEx(String exName){
        boolean contains = false;

        if(getWorkoutInfoMap() != null){
            if(!getWorkoutInfoMap().isEmpty()){
                for(Map.Entry<String, List<String>> mapEntry : getWorkoutInfoMap().entrySet()){
                    if(mapEntry.getValue().contains(exName)){
                        contains = true;
                    }
                }
            }
        }

        return contains;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public boolean isIsImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
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
