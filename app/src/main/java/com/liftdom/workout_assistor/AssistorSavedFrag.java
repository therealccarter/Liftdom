package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
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

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TemplateModelClass templateClass;
    HashMap<String, HashMap<String, List<String>>> completedMap;
    HashMap<String, List<String>> modelMapFormatted;
    HashMap<String, List<String>> completedMapFormatted;
    HashMap<String, List<String>> originalHashmap = new HashMap<>();

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
                    // For each list in the model/expected maps
                    String exName = map1.getValue().get(0);
                    int totalPoundage = getTotalPoundage(modelMapFormatted, exName);
                    for(Map.Entry<String, List<String>> map2 : completedMapFormatted.entrySet()){
                        // For each list in the completed/actual maps

                        String delims = "[_]";
                        String[] tokens = map2.getValue().get(0).split(delims);
                        String splitExName = tokens[0];

                        String exNameCompleted = map2.getValue().get(0);

                        if(exName.equals(exNameCompleted)){
                            // same ex names
                            /**
                             * What do we need to do?
                             * if it's a parent exercise, we need to tally up its expected poundage, as well as the
                             * poundage of its children. Then we'll do the same for the completed version.
                             * Then if the completed poundage is >= the model poundage, we need to increment
                             * the parent and its children and update the original hashmap with those values.
                             */
                            if(tokens.length > 2 && tokens[1].equals("p")){
                                // is parent superset ex
                                int modelTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                        .length - 1], modelMapFormatted);
                                int completedTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                        .length - 1], completedMapFormatted);
                                if(completedTotalPoundageSS >= modelTotalPoundageSS){
                                    // superset completed, increase the algo
                                    generateAlgoForSuperset(splitExName, tokens[tokens.length - 1], map2.getValue()
                                            .get(0));
                                }else{
                                    // set to false
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                    ("0_key").get(0);
                                    String bool = "false";
                                    templateClass.updateAlgorithmDateMap(todayString, exName, bool);
                                }
                            }else{
                                int totalPoundage2 = getTotalPoundage(completedMapFormatted, exName);
                                if(totalPoundage2 >= totalPoundage){
                                    // algo
                                    generateAlgo(exName, false);
                                }else{
                                    // set to false
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                            ("0_key")
                                            .get(0);
                                    String bool = "false";
                                    templateClass.updateAlgorithmDateMap(todayString, exName, bool);
                                }
                            }
                        }
                    }
                }
            }
        }

        DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateClass.getTemplateName());
        templateRef.setValue(templateClass);

        return view;
    }

    private int getPoundageForModelSuperset(String exName, String tag, HashMap<String, List<String>> map){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
            String delims = "[_]";
            String[] tokens = mapEntry.getValue().get(0).split(delims);
            if(tokens.length > 2){
                if(tokens[1].equals("p")){
                    // parent ex
                    if(tokens[0].equals(exName)){
                        if(tokens[tokens.length - 1].equals(tag)){
                            // correct parent ex
                            for(String string : mapEntry.getValue()){
                                if(!isExerciseName(string)){
                                    String delims2 = "[@]";
                                    String[] tokens2 = string.split(delims2);
                                    int reps = Integer.parseInt(tokens2[0]);
                                    int weight = Integer.parseInt(tokens2[1]);
                                    int poundage = reps * weight;
                                    totalPoundage = totalPoundage + poundage;
                                }
                            }
                        }
                    }
                }else{
                    // child ex
                    if(tokens[1].equals(exName)){
                        if(tokens[tokens.length - 1].equals(tag)){
                            // correct child ex
                            for(String string : mapEntry.getValue()){
                                if(!isExerciseName(string)){
                                    String delims2 = "[@]";
                                    String[] tokens2 = string.split(delims2);
                                    int reps = Integer.parseInt(tokens2[0]);
                                    int weight = Integer.parseInt(tokens2[1]);
                                    int poundage = reps * weight;
                                    totalPoundage = totalPoundage + poundage;
                                }
                            }
                        }
                    }
                }
            }
        }

        return totalPoundage;
    }

    private boolean isSupersetList(List<String> list){
        boolean isSS = false;

        int i = 0;
        for(String string : list){
            if(i != 0){
                if(isExerciseName(string)){
                    isSS = true;
                }
            }
            i++;
        }

        return isSS;
    }

    //TODO: Template re-save got the map orders wrong somehow

    private void generateAlgoForSuperset(String exName, String tag, String exNameUnformatted){
        /**
         * What are we doing here?
         * We need to find the entry in the original hashmap that contains all of the exercises in the ex name,
         * then check to see if there is a corresponding entry in the algorithm date map.
         * If there is, we compare to its date and increment accordingly.
         * If there isn't, we create a new entry and add today's date.
         */

        HashMap<String, List<String>> hashMapCopy = new HashMap<>();
        hashMapCopy.putAll(originalHashmap);

        LocalDate newDate = LocalDate.now();
        for(Map.Entry<String, List<String>> algoMap : templateClass.getAlgorithmInfo().entrySet()){
            if(algoMap.getValue().get(0).equals(exName)){
                // so now we're in the correct algorithm map
                List<String> newValueList = new ArrayList<>();
                boolean instantiateList = false;
                for(Map.Entry<String, List<String>> map : hashMapCopy.entrySet()){
                    String key = tag + "_key";
                    if(map.getKey().equals(key)){
                        // correct entry in original hashmap
                        List<String> valueList = new ArrayList<>();
                        valueList.addAll(map.getValue());
                        for(String string : valueList){
                            if(!isExerciseName(string)){
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if(templateClass.getAlgorithmDateMap() != null){
                                    boolean hasEx = false;
                                    for(Map.Entry<String, List<String>> algoDateMap : templateClass
                                            .getAlgorithmDateMap().entrySet()){
                                        if(isToday(algoDateMap.getValue().get(3))){
                                            if(algoDateMap.getValue().get(0).equals(exNameUnformatted)){
                                                //if(Boolean.parseBoolean(algoDateMap.getValue().get(2))){
                                                    // compare, keep everything the same, set to true.
                                                    hasEx = true;
                                                    int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                    int sets = 0;
                                                    int reps = 0;
                                                    int weight = 0;
                                                    if(weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(1))){
                                                        sets = Integer.parseInt(tokens[0]);
                                                        sets += Integer.parseInt(algoMap.getValue().get(2));
                                                    } else{
                                                        sets = Integer.parseInt(tokens[0]);
                                                    }
                                                    if(weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(3))){
                                                        reps = Integer.parseInt(tokens[1]);
                                                        reps += Integer.parseInt(algoMap.getValue().get(4));
                                                    } else{
                                                        reps = Integer.parseInt(tokens[1]);
                                                    }
                                                    if(weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(5))) {
                                                        if(!isExerciseName(tokens[2])) {
                                                            weight = Integer.parseInt(tokens[2]);
                                                            weight += Integer.parseInt(algoMap.getValue().get(6));
                                                            if(Boolean.parseBoolean(algoMap.getValue().get(7))) {
                                                                /**
                                                                 *
                                                                 */
                                                                for (int j = 1; j < weeksSinceLast; j++) {
                                                                    sets = sets - Integer.parseInt(algoMap.getValue().get(2));
                                                                    reps = reps - Integer.parseInt(algoMap.getValue().get(4));
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
                                    if(!hasEx){
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                }else{
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }else {
                                newValueList.add(string);
                            }
                        }
                        if(instantiateList){
                            // CREATE map

                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();

                            if(templateClass.getAlgorithmDateMap() == null){
                                HashMap<String, List<String>> newHashMap = new HashMap<>();
                                String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                        .get(0);
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameUnformatted);
                                newList.add(newDate.toString());
                                newList.add("true");
                                newList.add(todayString);
                                newHashMap.put("0_key", newList);
                                templateClass.setAlgorithmDateMap(newHashMap);
                            }else{
                                HashMap<String, List<String>> newHashMap = new HashMap<>();
                                newHashMap.putAll(templateClass.getAlgorithmDateMap());
                                String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                        .get(0);
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameUnformatted);
                                newList.add(newDate.toString());
                                newList.add("true");
                                newList.add(todayString);
                                newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                templateClass.setAlgorithmDateMap(newHashMap);
                            }

                        }else{
                            // UPDATE maps
                            originalHashmap.put(key, newValueList);
                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            String bool = "true";
                            templateClass.updateAlgorithmDateMap(todayString, exNameUnformatted, bool);

                            String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                            if(mapName.equals("mMapOne")){
                                templateClass.setMapOne(originalHashmap);
                            }else if(mapName.equals("mMapTwo")){
                                templateClass.setMapTwo(originalHashmap);
                            }else if(mapName.equals("mMapThree")){
                                templateClass.setMapThree(originalHashmap);
                            }else if(mapName.equals("mMapFour")){
                                templateClass.setMapFour(originalHashmap);
                            }else if(mapName.equals("mMapFive")){
                                templateClass.setMapFive(originalHashmap);
                            }else if(mapName.equals("mMapSix")){
                                templateClass.setMapSix(originalHashmap);
                            }else if(mapName.equals("mMapSeven")){
                                templateClass.setMapSeven(originalHashmap);
                            }
                        }
                    }
                }
            }
        }

        Log.i("info", "String");

    }

    private void generateAlgo(String exName, boolean isSuperset){

        isSuperset = false;

        List<String> valueList = new ArrayList<>();
        String key = null;
        for(Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()){
            if(map1.getValue().get(0).equals(exName)){
                if(isSuperset == isSupersetList(map1.getValue())){
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                }
            }
        }
        LocalDate newDate = LocalDate.now();
        for(Map.Entry<String, List<String>> map2 : templateClass.getAlgorithmInfo().entrySet()){
            //if(map2.getValue().size() < 12){
                if(map2.getValue().get(0).equals(exName)){
                    List<String> newValueList = new ArrayList<>();
                    newValueList.add(exName);
                    boolean instantiateList = false;
                    for(String string : valueList){
                        if(!isExerciseName(string)){
                            String delims = "[x,@]";
                            String[] tokens = string.split(delims);
                            if(templateClass.getAlgorithmDateMap() != null){
                                boolean hasEx = false;
                                // need to check for the case of if empty or if no entries were found
                                for(Map.Entry<String, List<String>> map : templateClass.getAlgorithmDateMap().entrySet()){
                                    if(isToday(map.getValue().get(3))){
                                        if(map.getValue().get(0).equals(exName)){
                                            if(Boolean.parseBoolean(map.getValue().get(2))){
                                                // compare, keep everything the same, set to true.
                                                hasEx = true;
                                                int weeksSinceLast = getWeeksSinceLast(map.getValue().get(1));

                                                int sets = 0;
                                                int reps = 0;
                                                int weight = 0;
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(1))){
                                                    sets = Integer.parseInt(tokens[0]);
                                                    sets += Integer.parseInt(map2.getValue().get(2));
                                                } else{
                                                    sets = Integer.parseInt(tokens[0]);
                                                }
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(3))){
                                                    reps = Integer.parseInt(tokens[1]);
                                                    reps += Integer.parseInt(map2.getValue().get(4));
                                                } else{
                                                    reps = Integer.parseInt(tokens[1]);
                                                }
                                                if(weeksSinceLast >= Integer.parseInt(map2.getValue().get(5))) {
                                                    if(!isExerciseName(tokens[2])) {
                                                        weight = Integer.parseInt(tokens[2]);
                                                        weight += Integer.parseInt(map2.getValue().get(6));
                                                        if(Boolean.parseBoolean(map2.getValue().get(7))) {
                                                            /**
                                                             *
                                                             */
                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                sets = sets - Integer.parseInt(map2.getValue().get(2));
                                                                reps = reps - Integer.parseInt(map2.getValue().get(4));
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
                                            }else{
                                                // don't compare, set the date to today, and set bool to true.
                                                newValueList.add(string);
                                            }
                                        }
                                    }
                                }
                                if(!hasEx){
                                    // no recorded instance of this ex on this day
                                    instantiateList = true;

                                }
                            }else{
                                // no algo completed info at all
                                instantiateList = true;
                            }
                        }
                    }

                    if(instantiateList){
                        // instantiate datemap with this exercise

                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();

                        if(templateClass.getAlgorithmDateMap() == null){
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newHashMap.put("0_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }else{
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            newHashMap.putAll(templateClass.getAlgorithmDateMap());
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }

                    }else{
                        // UPDATE maps
                        originalHashmap.put(key, newValueList);
                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();
                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                .get(0);
                        String bool = "true";
                        templateClass.updateAlgorithmDateMap(todayString, exName, bool);

                        String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                        if(mapName.equals("mMapOne")){
                            templateClass.setMapOne(originalHashmap);
                        }else if(mapName.equals("mMapTwo")){
                            templateClass.setMapTwo(originalHashmap);
                        }else if(mapName.equals("mMapThree")){
                            templateClass.setMapThree(originalHashmap);
                        }else if(mapName.equals("mMapFour")){
                            templateClass.setMapFour(originalHashmap);
                        }else if(mapName.equals("mMapFive")){
                            templateClass.setMapFive(originalHashmap);
                        }else if(mapName.equals("mMapSix")){
                            templateClass.setMapSix(originalHashmap);
                        }else if(mapName.equals("mMapSeven")){
                            templateClass.setMapSeven(originalHashmap);
                        }

                        Log.i("info", "String");
                    }
                }
            //}
        }
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

    private String getExercisesInSupersetList(List<String> list){
        String cat = "";

        int inc = 0;
        for(String string : list){
            if(isExerciseName(string) && inc != 0){
                cat = cat + "_" + string;
            }
            inc++;
        }

        return cat;
    }

    private String getExercisesInSupersetMap(Map<String, List<String>> map){
        String cat = "";

        for(Map.Entry<String, List<String>> subMap : map.entrySet()){
            if(!subMap.getKey().equals("0_key")){
                cat = cat + "_" + subMap.getValue().get(0);
            }
        }

        return cat;
    }

    private String getNamesWithoutInt(String unformatted){
        String name = "";

        String delims = "[_]";
        String[] tokens = unformatted.split(delims);

        for(int i = 0; i < tokens.length - 1; i++){
            name = "_" + name + tokens[i];
        }

        return name;
    }

    //TODO: If multiple instances of single exercise, add instance inc

    private HashMap<String, List<String>> formatModelClass(HashMap<String, List<String>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            ArrayList<List<String>> subList = new ArrayList<>();
            if(!entry.getKey().equals("0_key")){
                if(isSupersetList(entry.getValue())){
                    // superset list
                    int inc = 0;
                    int numberOfInstances = 0;
                    String[] tokens = entry.getKey().split("[_]");
                    String parent = "";
                    List<String> childList = new ArrayList<>();
                    for(String string : entry.getValue()){
                        if(isExerciseName(string)){
                            if(inc == 0){
                                // parent ex of superset list
                                List<String> list = new ArrayList<>();
                                String extraExercises = getExercisesInSupersetList(entry.getValue());
                                parent = string;
                                for(Map.Entry<String, List<String>> map2 : formattedMap.entrySet()){
                                    if(getNamesWithoutInt(map2.getValue().get(0)).equals(string + extraExercises)){
                                        numberOfInstances++;
                                    }
                                }
                                list.add(string + extraExercises + String.valueOf(tokens[0]));
                                subList.add(list);
                            }else{
                                // child ex of superset list
                                List<String> list = new ArrayList<>();
                                list.add(string + "_" + parent + "_" + String.valueOf(tokens[0]));
                                subList.add(list);
                                childList.add(string);
                            }
                        }else{
                            List<String> list = expandList(string);
                            subList.get(subList.size() - 1).addAll(list);
                        }
                        inc++;
                    }
                    String newString = "";
                    for(String string : childList){
                        newString = newString + "_" + string;
                    }
                    subList.get(0).set(0, parent + "_" + "p" + newString + "_" + tokens[0]);
                }else{
                    // normal list
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
            }
            for(List<String> list : subList){
                formattedMap.put(formattedMap.size() + "_key", list);
            }
        }

        return formattedMap;
    }

    private HashMap<String, List<String>> formatCompletedMap(HashMap<String, HashMap<String, List<String>>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();
        ArrayList<String> instanceList = new ArrayList<>();

        for(Map.Entry<String, HashMap<String, List<String>>> subMap : map.entrySet()){
            if(subMap.getValue().size() > 1){
                String[] tokens2 = subMap.getKey().split("[_]");
                String parent = subMap.getValue().get("0_key").get(0);
                List<String> childList = new ArrayList<>();
                int instanceInc = 0;
                for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                    // is superset map
                    boolean isSuperset = false;
                    List<String> subList = new ArrayList<>();
                    for(String string : subHashMap.getValue()){
                        if(isExerciseName(string)){

                            if(subHashMap.getKey().equals("0_key")){
                                String extraExercises = getExercisesInSupersetMap(subMap.getValue());
                                if(instanceList.contains(string + extraExercises)){
                                    instanceInc++;
                                }
                                subList.add(string + "_" + "p" + extraExercises + "_" + String.valueOf(tokens2[0]));
                            }else{
                                subList.add(string + "_" + parent + "_" + String.valueOf(tokens2[0]));
                                childList.add(string);
                            }
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
                    formattedMap.put(formattedMap.size() + "_key", subList);
                }

            }else{
                // not superset map
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

    /**
     * Ok so we've decided that we should do a blanket incrementation for superset lists.
     * BUT we've got all this shit with separate exercises..we need to tally up everything in the
     * superset completed exercises and the expected superset exercises, and THEN pass it as isSuperset
     * into the generator. Read the chat logs to remember.
     *
     * What we need to do:
     * Add superset all superset exercises to parent exname string with number
     * Then for each superset exercise give it the tag of "ss" and the number
     * Now IF the exercise name can be split up, we find those child exercises
     * and tally them all up. Then IF they all tally up to what they should,
     * we send it to the algo generator.
     *
     * Ok, so now the maps are formatted correctly.
     */

    /**
     * Let's define our problem:
     * If you don't totally complete an exercise's requirements,
     * we need to know so that we can skip all this and keep the values
     * the same.
     * If you did complete the workout, we need to be comparing to the
     * OLDEST completed date, not the newest one.
     *  We should also somehow store the original values
     *  When do we set a new oldest date?
     *  The first time you miss a workout, the NEXT time you complete one
     *  will be the new oldest date. So if that boolean is false, we
     *  can add a new oldest date, today. If that boolean is true, we
     *  can compare with it.
     *
     *
     *  If you complete a workout and that boolean is false, you make
     *  this the new oldest date.
     *  If you complete a workout and that boolean is true,
     *  you compare with the old date.
     */

}




















