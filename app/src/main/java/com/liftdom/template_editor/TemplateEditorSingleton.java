package com.liftdom.template_editor;

import android.util.Log;

import java.util.*;

/**
 * Created by Brodin on 5/9/2017.
 */

public class TemplateEditorSingleton {

    // Singleton boilerplate
    private static TemplateEditorSingleton controller;
    static TemplateEditorSingleton getInstance() {
        if (controller == null) {
            controller = new TemplateEditorSingleton();
        }
        return controller;
    }

    String mTemplateName;
    String mUserId;
    String mUserName;
    String mUserId2;
    String mUserName2;
    boolean mIsPublic = false;
    boolean isFromPublic = false;
    String mDateCreated;
    String mDescription;
    boolean isAlgoApplyToAll = false;
    HashMap<String, List<String>> mapOne = new HashMap<>();
    HashMap<String, List<String>> mapTwo = new HashMap<>();
    HashMap<String, List<String>> mapThree = new HashMap<>();
    HashMap<String, List<String>> mapFour = new HashMap<>();
    HashMap<String, List<String>> mapFive = new HashMap<>();
    HashMap<String, List<String>> mapSix = new HashMap<>();
    HashMap<String, List<String>> mapSeven = new HashMap<>();
    boolean mIsAlgorithm = false;
    HashMap<String, List<String>> mAlgorithmInfo = new HashMap<>();
    HashMap<String, List<String>> mAlgorithmDateMap = new HashMap<>();
    String publicTemplateKeyId;
    boolean isImperial;

    public void setValues2(String daysOfWeek, List<String> infoList){
        List<String> dayEntry = new ArrayList<>();
        dayEntry.add(daysOfWeek);

        if(mapOne != null &&
                mapTwo != null &&
                mapThree != null &&
                mapFour != null &&
                mapFive != null &&
                mapSix != null &&
                mapSeven != null
                ){
            if(mapOne.isEmpty() &&
                    mapTwo.isEmpty() &&
                    mapThree.isEmpty() &&
                    mapFour.isEmpty() &&
                    mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                Log.i("info", "Map one added: " + daysOfWeek);
                mapOne.put("0_key", dayEntry);
                setMapValues(daysOfWeek, infoList);
            } else if(!mapOne.isEmpty() &&
                    mapTwo.isEmpty() &&
                    mapThree.isEmpty() &&
                    mapFour.isEmpty() &&
                    mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0))){
                    Log.i("info", "Map two added: " + daysOfWeek);
                    mapTwo.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            } else if(!mapOne.isEmpty() &&
                    !mapTwo.isEmpty() &&
                    mapThree.isEmpty() &&
                    mapFour.isEmpty() &&
                    mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapTwo.get("0_key").get(0))){
                    Log.i("info", "Map three added: " + daysOfWeek);
                    mapThree.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            } else if(!mapOne.isEmpty() &&
                    !mapTwo.isEmpty() &&
                    !mapThree.isEmpty() &&
                    mapFour.isEmpty() &&
                    mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapTwo.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapThree.get("0_key").get(0))){
                    Log.i("info", "Map four added: " + daysOfWeek);
                    mapFour.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            } else if(!mapOne.isEmpty() &&
                    !mapTwo.isEmpty() &&
                    !mapThree.isEmpty() &&
                    !mapFour.isEmpty() &&
                    mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapTwo.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapThree.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapFour.get("0_key").get(0))){
                    Log.i("info", "Map five added: " + daysOfWeek);
                    mapFive.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            } else if(!mapOne.isEmpty() &&
                    !mapTwo.isEmpty() &&
                    !mapThree.isEmpty() &&
                    !mapFour.isEmpty() &&
                    !mapFive.isEmpty() &&
                    mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapTwo.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapThree.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapFour.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapFive.get("0_key").get(0))){
                    Log.i("info", "Map six added: " + daysOfWeek);
                    mapSix.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            } else if(!mapOne.isEmpty() &&
                    !mapTwo.isEmpty() &&
                    !mapThree.isEmpty() &&
                    !mapFour.isEmpty() &&
                    !mapFive.isEmpty() &&
                    !mapSix.isEmpty() &&
                    mapSeven.isEmpty()
                    ){
                if(!daysOfWeek.equals(mapOne.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapTwo.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapThree.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapFour.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapFive.get("0_key").get(0)) &&
                        !daysOfWeek.equals(mapSix.get("0_key").get(0))){
                    Log.i("info", "Map seven added: " + daysOfWeek);
                    mapSeven.put("0_key", dayEntry);
                    setMapValues(daysOfWeek, infoList);
                }else{
                    setMapValues(daysOfWeek, infoList);
                }
            }
        }

    }

    public void setMapValues(String days, List<String> infoList){
        if(mapOne != null){
            if(!mapOne.isEmpty()){
                if(mapOne.get("0_key").contains(days)){
                    mapOne.put(String.valueOf(mapOne.size()) + "_key", infoList);
                }
            }
        }
        if(mapTwo != null){
            if(!mapTwo.isEmpty()){
                if(mapTwo.get("0_key").contains(days)){
                    mapTwo.put(String.valueOf(mapTwo.size()) + "_key", infoList);
                }
            }
        }
        if(mapThree != null){
            if(!mapThree.isEmpty()){
                if(mapThree.get("0_key").contains(days)){
                    mapThree.put(String.valueOf(mapThree.size()) + "_key", infoList);
                }
            }
        }
        if(mapFour != null){
            if(!mapFour.isEmpty()){
                if(mapFour.get("0_key").contains(days)){
                    mapFour.put(String.valueOf(mapFour.size()) + "_key", infoList);
                }
            }
        }
        if(mapFive != null){
            if(!mapFive.isEmpty()){
                if(mapFive.get("0_key").contains(days)){
                    mapFive.put(String.valueOf(mapFive.size()) + "_key", infoList);
                }
            }
        }
        if(mapSix != null){
            if(!mapSix.isEmpty()){
                if(mapSix.get("0_key").contains(days)){
                    mapSix.put(String.valueOf(mapSix.size()) + "_key", infoList);
                }
            }
        }
        if(mapSeven != null){
            if(!mapSeven.isEmpty()){
                if(mapSeven.get("0_key").contains(days)){
                    mapSeven.put(String.valueOf(mapSeven.size()) + "_key", infoList);
                }
            }
        }
    }

    public void setAlgorithmList(String exName, List<String> algoList){
        int listSize = mAlgorithmInfo.size();
        String key = String.valueOf(listSize + 1) + "_key";

        algoList.set(0, exName);

        mAlgorithmInfo.put(key, algoList);
    }

    public void clearAll(){
        mTemplateName = null;
        mUserId = null;
        mUserName = null;
        mIsPublic = false;
        mDateCreated = null;
        mDescription = null;
        mapOne.clear();
        mapTwo.clear();
        mapThree.clear();
        mapFour.clear();
        mapFive.clear();
        mapSix.clear();
        mapSeven.clear();
        mIsAlgorithm = false;
        mAlgorithmInfo.clear();
    }
}





