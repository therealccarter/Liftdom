package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

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
    HashMap<String, List<String>> originalHashmap;

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
                        originalHashmap.putAll(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                    }
                }
                completedMapFormatted = formatCompletedMap(completedMap);

                // init done

                for(Map.Entry<String, List<String>> map1 : modelMapFormatted.entrySet()){
                    String exName = map1.getValue().get(0);
                    int totalPoundage = getTotalPoundage(modelMapFormatted, exName);
                    for(Map.Entry<String, List<String>> map2 : completedMapFormatted.entrySet()){
                        boolean isSuperset = false;
                        String delims = "[_]";
                        String[] tokens = map2.getValue().get(0).split(delims);
                        String splitExName = tokens[0];
                        if(tokens.length > 1){
                            isSuperset = true;
                        }
                        if(splitExName.equals(exName)){
                            int totalPoundage2 = getTotalPoundage(completedMapFormatted, exName);
                            if(totalPoundage2 >= totalPoundage){
                                // algo
                                generateAlgo(exName, isSuperset);
                            }
                        }
                    }
                }
            }
        }


        return view;
    }

    private void generateAlgo(String exName, boolean isSuperset){
        //HashMap<String, List<String>> newMap = new HashMap<>();
        List<String> algorithmList = new ArrayList<>();

        if(isSuperset){
            // superset
            for(Map.Entry<String, List<String>> map : templateClass.getAlgorithmInfo().entrySet()){
                if(map.getValue().size() > 11){

                }
            }
        }else{
            // not superset
            List<String> valueList = new ArrayList<>();
            /**
             * What do we need to do here?
             * We've got the correct algorithm.
             * We've got the exercise name.
             * We now need to apply the algorithm to the original values and replace that value in the template map
             */
            String key = null;
            for(Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()){
                if(map1.getKey().equals(exName)){
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                }
            }
            for(Map.Entry<String, List<String>> map2 : templateClass.getAlgorithmInfo().entrySet()){
                if(map2.getValue().size() < 12){
                    if(map2.getValue().get(0).equals(exName)){
                        List<String> newValueList = new ArrayList<>();
                        newValueList.add(exName);
                        for(String string : valueList){
                            if(!isExerciseName(string)){
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                // is looper
                                /**
                                 * Now, what must we do here?
                                 * If it's been weightInt weeks since the last completed workout of this exercise,
                                 * reset the setsInt and the repsInt to their original values. Keep
                                 */
                                if(templateClass.getAlgorithmDateMap() != null){
                                    // need to check for the case of if empty or if no entries were found
                                    for(Map.Entry<String, List<String>> map : templateClass.getAlgorithmDateMap().entrySet()){
                                        if(isToday(map.getKey())){
                                            if(map.getValue().get(0).equals(exName)){
                                                int weeksSinceLast = getWeeksSinceLast(map.getValue().get(1));

                                                int sets = 0;
                                                int reps = 0;
                                                int weight = 0;
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(0))){
                                                    sets = Integer.parseInt(tokens[0]);
                                                    sets += Integer.parseInt(map2.getValue().get(1));
                                                } else{
                                                    sets = Integer.parseInt(tokens[0]);
                                                }
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(2))){
                                                    reps = Integer.parseInt(tokens[1]);
                                                    reps += Integer.parseInt(map2.getValue().get(3));
                                                } else{
                                                    reps = Integer.parseInt(tokens[1]);
                                                }
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(4))) {
                                                    if(!isExerciseName(tokens[2])) {
                                                        weight = Integer.parseInt(tokens[2]);
                                                        weight += Integer.parseInt(map2.getValue().get(5));
                                                        if(Boolean.parseBoolean(map2.getValue().get(7))) {
                                                            // what if we just subtracted the
                                                            // algo shit from the weeks
                                                            // between?...
                                                            for (int j = 0; j < weeksSinceLast;
                                                                 j++) {
                                                                sets = sets - Integer.parseInt(map2.getValue().get(1));
                                                                reps = reps - Integer.parseInt(map2.getValue().get(3));
                                                            }
                                                        }
                                                    }
                                                }else{
                                                    if(!isExerciseName(tokens[2])){
                                                        weight = Integer.parseInt(tokens[2]);
                                                    }
                                                }

                                                String concat = Integer.toString
                                                        (sets) + "x" + Integer.toString(reps)
                                                        + "@" + Integer.toString
                                                        (weight);

                                                newValueList.add(concat);
                                            }
                                        }
                                    }
                                }else{
                                    // no algo completed info
                                }
                            }
                        }
                        originalHashmap.put(key, newValueList);
                    }
                }
            }
        }

        //return newMap;
    }

    private int getWeeksSinceLast(String dateString){
        int weeksInt = 0;

        LocalDate oldDate = LocalDate.parse(dateString);
        LocalDate newDate = LocalDate.now();

        int daysBetween = Days.daysBetween(oldDate, newDate).getDays();
        weeksInt = (int) Math.round(daysBetween / 7);

        return weeksInt;
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



    private int getTotalPoundage(HashMap<String, List<String>> map, String exName){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            if(entry.getValue().get(0).equals(exName)){
                for(String string : entry.getValue()){
                    if(!isExerciseName(string)){
                        String delims = "[@,_]";
                        String tokens[] = string.split(delims);
                        int int1 = Integer.parseInt(tokens[0]);
                        int int2 = Integer.parseInt(tokens[1]);
                        int int3 = int1 * int2;
                        totalPoundage = totalPoundage + int3;
                    }
                }
            }
        }


        return totalPoundage;
    }

    private int getTotalPoundage2(List<String> list){
        int totalPoundage = 0;

        for(String string : list){
            String delims = "[@,_]";
            String tokens[] = string.split(delims);
            int int1 = Integer.parseInt(tokens[0]);
            int int2 = Integer.parseInt(tokens[1]);
            int int3 = int1 * int2;
            totalPoundage = totalPoundage + int3;
        }

        return totalPoundage;
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
                boolean isSuperset = false;
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
                        if(tokens.length > 2){
                            if(tokens[2].equals("ss")){
                                isSuperset = true;
                            }
                        }
                    }
                }
                if(isSuperset){
                    String newName = subList.get(0) + "_ss";
                    subList.set(0, newName);
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




















