package com.liftdom.template_editor;

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

    //TODO: at the end, if a map is empty, but a dummy value in there so we can later update the child

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

    // gets called for each paused set scheme frag
    public void setValues(String daysOfWeek, String exerciseValue, String setSchemeValue){
        String exName = exNameFormatter(exerciseValue);
        List<String> dayEntry = new ArrayList<>();
        dayEntry.add(daysOfWeek);

        if(mapOne.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && mapTwo.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && !mapTwo.isEmpty() && mapThree.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && !mapTwo.isEmpty() && !mapThree.isEmpty()
                && mapFour.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && !mapTwo.isEmpty() && !mapThree.isEmpty()
                && !mapFour.isEmpty() && mapFive.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && !mapTwo.isEmpty() && !mapThree.isEmpty()
                && !mapFour.isEmpty() && !mapFive.isEmpty() && mapSix.isEmpty()){
            mapOne.put("0", dayEntry);
        }else if(!mapOne.isEmpty() && !mapTwo.isEmpty() && !mapThree.isEmpty()
                && !mapFour.isEmpty() && !mapFive.isEmpty() && !mapSix.isEmpty()
                    && mapSeven.isEmpty()){
            mapOne.put("0", dayEntry);
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

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    temp.put(String.valueOf(inc), mapList);
                }else {
                    List<String> xValueList = temp.get(entry.getKey());
                    xValueList.add(setScheme);
                    temp.put(entry.getKey(), xValueList);
                }
            }

            mapOne.clear();
            mapOne.putAll(temp);

        }else if(mapTwo.get("0").contains(days)){
            int inc = mapTwo.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapTwo.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapTwo.put(String.valueOf(inc), mapList);
                }else {
                    mapTwo.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }else if(mapThree.get("0").contains(days)){
            int inc = mapThree.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapThree.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapThree.put(String.valueOf(inc), mapList);
                }else {
                    mapThree.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }else if(mapFour.get("0").contains(days)){
            int inc = mapFour.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapFour.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapFour.put(String.valueOf(inc), mapList);
                }else {
                    mapFour.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }else if(mapFive.get("0").contains(days)){
            int inc = mapFive.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapFive.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapFive.put(String.valueOf(inc), mapList);
                }else {
                    mapFive.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }else if(mapSix.get("0").contains(days)){
            int inc = mapSix.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapSix.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapSix.put(String.valueOf(inc), mapList);
                }else {
                    mapSix.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }else if(mapSeven.get("0").contains(days)){
            int inc = mapSeven.size();
            inc++;

            Iterator<Map.Entry<String, List<String>>> iterator = mapSeven.entrySet().iterator();
            while(iterator.hasNext()){
                Map.Entry<String, List<String>> pair = (Map.Entry<String, List<String>>) iterator.next();

                List<String> valueList = pair.getValue();

                if(!containsEx(valueList, exName)){
                    List<String> mapList = new ArrayList<>();
                    mapList.add(exName);
                    mapList.add(setScheme);
                    mapSeven.put(String.valueOf(inc), mapList);
                }else {
                    mapSeven.get(pair.getKey()).add(setScheme);
                }

                //iterator.remove();
            }
        }
    }

    private boolean containsEx(List<String> list, String exName){
        if(list.contains(exName)){
            return true;
        }else{
            return false;
        }
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










