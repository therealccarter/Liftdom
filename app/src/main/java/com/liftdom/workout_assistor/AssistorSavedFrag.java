package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorSavedFrag extends android.app.Fragment {


    public AssistorSavedFrag() {
        // Required empty public constructor
    }

    TemplateModelClass templateClass;
    HashMap<String, HashMap<String, List<String>>> completedMap;
    HashMap<String, List<String>> modelMapFormatted;
    HashMap<String, List<String>> completedMapFormatted;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        if(templateClass.getIsAlgorithm()){
            if(templateClass.getIsAlgoApplyToAll()){

            }else{
                DateTime dateTime = new DateTime();
                int currentWeekday = dateTime.getDayOfWeek();
                if(templateClass.getMapForDay(intToWeekday(currentWeekday)) != null) {
                    if (!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()) {
                        modelMapFormatted = formatModelClass(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                    }
                }
                completedMapFormatted = formatCompletedMap(completedMap);

                // init done


            }
        }




        return view;
    }

    private HashMap<String, List<String>> formatModelClass(HashMap<String, List<String>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            ArrayList<List<String>> subList = new ArrayList<>();
            if(!entry.getKey().equals("0_key")){
                for(String string : entry.getValue()){
                    if(isExerciseName(string)){
                        List<String> list = new ArrayList<>();
                        list.add(string);
                        subList.add(list);
                    }else{
                        List<String> list = expandList(string);
                        subList.get(subList.size() - 1).addAll(list);
                    }
                }
            }
            for(List<String> list : subList){
                formattedMap.put(formattedMap.size() + "_key", list);
            }
        }

        return formattedMap;
    }

    private HashMap<String, List<String>> formatCompletedMap(HashMap<String, HashMap<String, List<String>>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();

        for(Map.Entry<String, HashMap<String, List<String>>> subMap : map.entrySet()){
            for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                List<String> subList = new ArrayList<>();
                for(String string : subHashMap.getValue()){
                    if(isExerciseName(string)){
                        subList.add(string);
                    }else{
                        String delims = "[_]";
                        String[] tokens = string.split(delims);
                        if(tokens[1].equals("checked")){
                            subList.add(tokens[0]);
                        }
                    }
                }
                formattedMap.put(formattedMap.size() + "_key", subList);
            }
        }

        return formattedMap;
    }

    private List<String> expandList(String data){
        List<String> returnList = new ArrayList<>();

        String delims = "[x]";
        String tokens[] = data.split(delims);
        int inc = Integer.valueOf(tokens[0]);
        for(int i = 0; i < inc; i++){
            returnList.add(tokens[1]);
        }

        return returnList;
    }

    String intToWeekday(int inc){
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

}




















