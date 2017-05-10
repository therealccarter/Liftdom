package com.liftdom.template_editor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    //TODO: at the end, if a map is empty, but a dummy value in there so we can later update the child

    String mTemplateName;
    String mUserId;
    String mUserName;
    boolean mIsPublic = false;
    String mDateCreated;
    String mDescription;
    HashMap<String, List<String>> mMondayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mTuesdayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mWednesdayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mThursdayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mFridayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mSaturdayMap = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mSundayMap = new HashMap<String, List<String>>();
    boolean mIsAlgorithm = false;
    HashMap<String, List<String>> mAlgorithmInfo = new HashMap<String, List<String>>();

    public void setValues(String dayOfWeek, String exerciseValue, String setSchemeValue){
        String[] days = doWFormatter(dayOfWeek);
        String exName = exNameFormatter(exerciseValue);
        for(String day : days){
            setMapValue(day, exName, setSchemeValue);
        }
    }

    private void setMapValue(String day, String exName, String setScheme){
        if(day.equals("Monday")){
            int inc = mMondayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mMondayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mMondayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Tuesday")){
            int inc = mTuesdayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mTuesdayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mTuesdayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Wednesday")){
            int inc = mWednesdayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mWednesdayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mWednesdayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Thursday")){
            int inc = mThursdayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mThursdayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mThursdayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Friday")){
            int inc = mFridayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mFridayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mFridayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Saturday")){
            int inc = mSaturdayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mSaturdayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mSaturdayMap.get(split[1]).add(setScheme);
            }
        }else if(day.equals("Sunday")){
            int inc = mSundayMap.size();
            inc++;
            if(containsEx(day, exName).equals("false")){
                List<String> mapList = new ArrayList<>();
                mapList.add(exName);
                mapList.add(setScheme);
                mSundayMap.put(String.valueOf(inc), mapList);
            }else {
                String delims = "[_]";
                String info = containsEx(day, exName);
                String[] split = info.split(delims);
                mSundayMap.get(split[1]).add(setScheme);
            }
        }
    }

    private String containsEx(String day, String exName){
        String containsEx = "false";

        if(day.equals("Monday")){
            for(Map.Entry<String, List<String>> entry : mMondayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Tuesday")){
            for(Map.Entry<String, List<String>> entry : mTuesdayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Wednesday")){
            for(Map.Entry<String, List<String>> entry : mWednesdayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Thursday")){
            for(Map.Entry<String, List<String>> entry : mThursdayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Friday")){
            for(Map.Entry<String, List<String>> entry : mFridayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Saturday")){
            for(Map.Entry<String, List<String>> entry : mSaturdayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }else if(day.equals("Sunday")){
            for(Map.Entry<String, List<String>> entry : mSundayMap.entrySet()){
                List<String> mapList = entry.getValue();
                if(mapList.contains(exName)){
                    containsEx = "true_" + String.valueOf(entry.getKey());
                }
            }
        }

        return containsEx;
    }

    private String exNameFormatter(String exNameUn){
        return exNameUn.replaceAll("\n", "");
    }

    private String[] doWFormatter(String dowUn){
        String delims = "[_]";
        String[] split = dowUn.split(delims);
        return split;
    }

    public void clearAll(){
        mTemplateName = null;
        mUserId = null;
        mUserName = null;
        mIsPublic = false;
        mDateCreated = null;
        mDescription = null;
        mMondayMap = null;
        mTuesdayMap = null;
        mWednesdayMap = null;
        mThursdayMap = null;
        mFridayMap = null;
        mSaturdayMap = null;
        mSundayMap = null;
        mIsAlgorithm = false;
        mAlgorithmInfo = null;
    }

    public void makeDummies(){

        List<String> dummy = new ArrayList<>();
        dummy.add("null");

        if(mMondayMap.isEmpty()){
            mMondayMap.put(String.valueOf(0), dummy);
        }else if(mTuesdayMap.isEmpty()){
            mTuesdayMap.put(String.valueOf(0), dummy);
        }else if(mWednesdayMap.isEmpty()){
            mWednesdayMap.put(String.valueOf(0), dummy);
        }else if(mThursdayMap.isEmpty()){
            mThursdayMap.put(String.valueOf(0), dummy);
        }else if(mFridayMap.isEmpty()){
            mFridayMap.put(String.valueOf(0), dummy);
        }else if(mSaturdayMap.isEmpty()){
            mSaturdayMap.put(String.valueOf(0), dummy);
        }else if(mSundayMap.isEmpty()){
            mSundayMap.put(String.valueOf(0), dummy);
        }
    }
}










