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
    boolean mIsPublic = false;
    String mDateCreated;
    String mDescription;
    HashMap<String, List<String>> mapOne = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapTwo = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapThree = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapFour = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapFive = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapSix = new HashMap<String, List<String>>();
    HashMap<String, List<String>> mapSeven = new HashMap<String, List<String>>();
    boolean mIsAlgorithm = false;
    HashMap<String, List<String>> mAlgorithmInfo = new HashMap<String, List<String>>();

    public void setValues(String daysOfWeek, String exerciseValue, String setSchemeValue){
        String exName = exNameFormatter(exerciseValue);
        List<String> dayEntry = new ArrayList<>();
        dayEntry.add(daysOfWeek);

        if(mapOne.isEmpty() &&
           mapTwo.isEmpty() &&
           mapThree.isEmpty() &&
           mapFour.isEmpty() &&
           mapFive.isEmpty() &&
           mapSix.isEmpty() &&
           mapSeven.isEmpty()
           ){
            Log.i("info", "Map one added: " + daysOfWeek);
            mapOne.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                mapTwo.isEmpty() &&
                mapThree.isEmpty() &&
                mapFour.isEmpty() &&
                mapFive.isEmpty() &&
                mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map two added: " + daysOfWeek);
            mapTwo.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                !mapTwo.isEmpty() &&
                mapThree.isEmpty() &&
                mapFour.isEmpty() &&
                mapFive.isEmpty() &&
                mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map three added: " + daysOfWeek);
            mapThree.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                !mapTwo.isEmpty() &&
                !mapThree.isEmpty() &&
                mapFour.isEmpty() &&
                mapFive.isEmpty() &&
                mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map four added: " + daysOfWeek);
            mapFour.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                !mapTwo.isEmpty() &&
                !mapThree.isEmpty() &&
                !mapFour.isEmpty() &&
                mapFive.isEmpty() &&
                mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map five added: " + daysOfWeek);
            mapFive.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                !mapTwo.isEmpty() &&
                !mapThree.isEmpty() &&
                !mapFour.isEmpty() &&
                !mapFive.isEmpty() &&
                mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map six added: " + daysOfWeek);
            mapSix.put("0", dayEntry);
        } else if(!mapOne.isEmpty() &&
                !mapTwo.isEmpty() &&
                !mapThree.isEmpty() &&
                !mapFour.isEmpty() &&
                !mapFive.isEmpty() &&
                !mapSix.isEmpty() &&
                mapSeven.isEmpty()
                ){
            Log.i("info", "Map seven added: " + daysOfWeek);
            mapSeven.put("0", dayEntry);
        }
        setMapValue(daysOfWeek, exName, setSchemeValue);
    }

    private void setMapValue(String days, String exName, String setScheme){
        if(mapOne.get("0").contains(days)){
            int inc = mapOne.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapOne);

            for(Map.Entry<String, List<String>> entry : mapOne.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapOne.clear();
            mapOne.putAll(temp);

        }else if(mapTwo.get("0").contains(days)){
            int inc = mapTwo.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapTwo);

            for(Map.Entry<String, List<String>> entry : mapTwo.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapTwo.clear();
            mapTwo.putAll(temp);

        }else if(mapThree.get("0").contains(days)){
            int inc = mapThree.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapThree);

            for(Map.Entry<String, List<String>> entry : mapThree.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapThree.clear();
            mapThree.putAll(temp);

        }else if(mapFour.get("0").contains(days)){
            int inc = mapFour.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapFour);

            for(Map.Entry<String, List<String>> entry : mapFour.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapFour.clear();
            mapFour.putAll(temp);

        }else if(mapFive.get("0").contains(days)){
            int inc = mapFive.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapFive);

            for(Map.Entry<String, List<String>> entry : mapFive.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapFive.clear();
            mapFive.putAll(temp);

        }else if(mapSix.get("0").contains(days)){
            int inc = mapSix.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapSix);

            for(Map.Entry<String, List<String>> entry : mapSix.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapSix.clear();
            mapSix.putAll(temp);

        }else if(mapSeven.get("0").contains(days)){
            int inc = mapSeven.size();

            HashMap<String, List<String>> temp = new HashMap<>();
            temp.putAll(mapSeven);

            for(Map.Entry<String, List<String>> entry : mapSeven.entrySet()){
                List<String> valueList = entry.getValue();
                String exNameFromList = valueList.get(0);

                if(!containsEx(temp, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    // if ex name has already been added
                    if(exNameFromList.equals(exName)){
                        List<String> xValueList = temp.get(entry.getKey());
                        xValueList.add(setScheme);
                        temp.put(entry.getKey(), xValueList);
                    }
                }
            }

            mapSeven.clear();
            mapSeven.putAll(temp);

        }
    }

    private boolean containsEx(HashMap<String, List<String>> temp, String exName){
        boolean contains = false;
        for(Map.Entry<String, List<String>> entry : temp.entrySet()){
            List<String> valueList = entry.getValue();
            if(valueList.get(0).equals(exName)){
                contains = true;
            }
        }
        return contains;
    }

    private String exNameFormatter(String exNameUn){
        return exNameUn.replaceAll("\n", "");
    }

    public void clearAll(){
        mTemplateName = null;
        mUserId = null;
        mUserName = null;
        mIsPublic = false;
        mDateCreated = null;
        mDescription = null;
        mapOne = null;
        mapTwo = null;
        mapThree = null;
        mapFour = null;
        mapFive = null;
        mapSix = null;
        mapSeven = null;
        mIsAlgorithm = false;
        mAlgorithmInfo = null;
    }

}










