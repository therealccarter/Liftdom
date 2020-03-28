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

    public ArrayList<String> getExerciseList(){
        ArrayList<String> exList = new ArrayList<>();

        for(Map.Entry<String, List<String>> entry : getWorkoutInfoMap().entrySet()){
            exList.add(entry.getValue().get(0));
        }

        return exList;
    }

    public double getPoundage(boolean isImperialPOV, String lbs, String kgs){
        double poundage = 0.0;

        int listInc = 0;
        ArrayList<List<String>> exInfoList = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        exInfoList.add(list1);
        for(Map.Entry<String, List<String>> mapEntry : getWorkoutInfoMap().entrySet()){
            boolean isExBool = false;
            for(String string : mapEntry.getValue()){
                if(!isExerciseName(string)){
                    exInfoList.get(0).add(string);
                }
            }
        }

        for(List<String> list : exInfoList){
            for(String string : list){
                if(!isExerciseName(string)){
                    try{
                        String[] tokens = string.split("@");
                        double pounds = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]);
                        poundage = poundage + pounds;
                    }catch (NumberFormatException e){
                        String delims = "[x,@]";
                        String[] tokens = string.split(delims);
                        for(int i = 0; i < tokens.length; i++){
                            String delims2 = "[_]";
                            String[] tokens2 = tokens[i].split(delims2);
                            if(tokens2.length > 1){
                                if(tokens2[1].equals("a")){
                                    tokens[i] = tokens2[0];
                                }
                            }else{
                                if(tokens[i].equals("T.F.")){
                                    tokens[i] = "1";
                                }else if(tokens[i].equals("B.W.")){
                                    if(mIsImperial){
                                        tokens[i] = String.valueOf(Integer.parseInt(lbs) / 2 );
                                    }else{
                                        tokens[i] = String.valueOf(Integer.parseInt(kgs) / 2 );
                                    }
                                }
                            }
                        }
                        double pounds = 1;
                        for(int i = 0; i < tokens.length; i++){
                            pounds = pounds * Double.parseDouble(tokens[i]);
                        }
                        //double pounds2 =
                        //        Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]) *
                        //        Double
                        //        .parseDouble(tokens[2]);
                        poundage = poundage + pounds;
                    }
                }
            }
        }

        return converter(poundage, isImperialPOV);
    }

    public double getExPoundage(String exName, boolean isImperialPOV, String lbs, String kgs){
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
                    try{
                        String[] tokens = string.split("@");
                        double pounds = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]);
                        poundage = poundage + pounds;
                    }catch (NumberFormatException e){
                        String delims = "[x,@]";
                        String[] tokens = string.split(delims);
                        for(int i = 0; i < tokens.length; i++){
                            String delims2 = "[_]";
                            String[] tokens2 = tokens[i].split(delims2);
                            if(tokens2.length > 1){
                                if(tokens2[1].equals("a")){
                                    tokens[i] = tokens2[0];
                                }
                            }else{
                                if(tokens[i].equals("T.F.")){
                                    tokens[i] = "1";
                                }else if(tokens[i].equals("B.W.")){
                                    if(mIsImperial){
                                        tokens[i] = lbs;
                                    }else{
                                        tokens[i] = kgs;
                                    }
                                }
                            }
                        }
                        double pounds = Double.parseDouble(tokens[0]) * Double.parseDouble(tokens[1]) * Double
                                .parseDouble(tokens[2]);
                        poundage = poundage + pounds;
                    }
                }
            }
        }

        return converter(poundage, isImperialPOV);
    }

    private double converter(double poundage, boolean isImperialPOV){
        double converted = 0.0;

        if(isImperialPOV && mIsImperial){
            converted = poundage;
        }else if(!isImperialPOV && mIsImperial){
            // user is currently kg, workout is in pounds. imperial to metric.
            double lbsDouble = poundage / 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            converted = lbsInt;
        }else if(isImperialPOV && !mIsImperial){
            // user is currently in pounds, workout is in kg. metric to imperial.
            double lbsDouble = poundage * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            converted = lbsInt;
        }


        return converted;
    }

    public double getMaxWeightLifted(boolean isImperialPOV, String lbs, String kgs){
        double maxWeight = 0.0;


        int listInc = 0;
        ArrayList<List<String>> exInfoList = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        exInfoList.add(list1);
        for(Map.Entry<String, List<String>> mapEntry : getWorkoutInfoMap().entrySet()){
            boolean isExBool = false;
            for(String string : mapEntry.getValue()){
                if(!isExerciseName(string)){
                    exInfoList.get(0).add(string);
                }
            }
        }

        for(List<String> list : exInfoList){
            for(String string : list){
                if(!isExerciseName(string)){
                    String[] tokens = string.split("@");
                    double weight;
                    if(tokens[1].equals("B.W.")){
                        if(mIsImperial){
                            weight = Integer.parseInt(lbs);
                        }else{
                            weight = Integer.parseInt(kgs);
                        }
                    }else{
                        weight = Double.parseDouble(tokens[1]);
                    }
                    if(weight > maxWeight){
                        maxWeight = weight;
                    }
                }
            }
        }

        return converter(maxWeight, isImperialPOV);
    }

    public double getExMaxWeightLifted(String exName, boolean isImperialPOV, String lbs, String kgs){
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
                    double weight;
                    if(tokens[1].equals("B.W.")){
                        if(mIsImperial){
                            weight = Integer.parseInt(lbs);
                        }else{
                            weight = Integer.parseInt(kgs);
                        }
                    }else{
                        weight = Double.parseDouble(tokens[1]);
                    }
                    if(weight > maxWeight){
                        maxWeight = weight;
                    }
                }
            }
        }

        return converter(maxWeight, isImperialPOV);
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

    public boolean containsWorkoutInfoMap(){
        boolean contains = false;

        if(getWorkoutInfoMap() != null){
            if(!getWorkoutInfoMap().isEmpty()){
                contains = true;
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
