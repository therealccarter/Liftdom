package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
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
    String publicDescription = null;
    String privateJournal = null;
    String mediaRef = null;
    HashMap<String, HashMap<String, List<String>>> completedMap;
    HashMap<String, List<String>> modelMapFormatted;
    HashMap<String, List<String>> completedMapFormatted;
    HashMap<String, List<String>> originalHashmap = new HashMap<>();

    @BindView(R.id.goBackHome) Button goHomeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        ButterKnife.bind(this, view);

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        if(templateClass.getIsAlgorithm()){
            if(templateClass.getIsAlgoApplyToAll()){
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

                ArrayList<String> exercisesAlreadyGenerated = new ArrayList<>();

                for(Map.Entry<String, List<String>> map1 : modelMapFormatted.entrySet()){
                    // For each list in the model/expected maps
                    String exName = map1.getValue().get(0);
                    int totalPoundage = getTotalPoundage(modelMapFormatted, exName);
                    for(Map.Entry<String, List<String>> map2 : completedMapFormatted.entrySet()){
                        // For each list in the completed/actual maps

                        String delims = "[_]";
                        String[] tokens = map2.getValue().get(0).split(delims);
                        String splitExName = tokens[0];

                        if(splitExName.equals("Bench Press (Barbell - Incline)")){
                            Log.i("info", "info");
                        }

                        String exNameCompleted = map2.getValue().get(0);

                        if(exName.equals(exNameCompleted)){
                            // same ex names
                            if(tokens.length > 2 && tokens[1].equals("p")){
                                // is parent superset ex
                                int modelTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                        .length - 1], modelMapFormatted);
                                int completedTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                        .length - 1], completedMapFormatted);
                                if(completedTotalPoundageSS >= modelTotalPoundageSS){
                                    // superset completed, increase the algo
                                    generateAlgoForSuperset2(splitExName, tokens[tokens.length - 1], map2.getValue()
                                            .get(0));
                                }else{
                                    // set to false
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                            ("0_key").get(0);
                                    String bool = "false";
                                    templateClass.setNewDateMapValues(todayString, exName, bool);
                                }
                            }else if(tokens.length < 3){
                                int totalPoundage2 = getTotalPoundage(completedMapFormatted, exName);
                                if(!exercisesAlreadyGenerated.contains(exName)){
                                    if(totalPoundage2 >= totalPoundage){
                                        // algo
                                        generateAlgo2(exName, false);
                                        exercisesAlreadyGenerated.add(exName);
                                    }else{
                                        // set to false
                                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                ("0_key")
                                                .get(0);
                                        String bool = "false";
                                        templateClass.setNewDateMapValues(todayString, exName, bool);
                                    }
                                }
                            }
                        }
                    }
                }
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

                ArrayList<String> exercisesAlreadyGenerated = new ArrayList<>();

                for(Map.Entry<String, List<String>> map1 : modelMapFormatted.entrySet()){
                    // For each list in the model/expected maps
                    String exName = map1.getValue().get(0);
                    int totalPoundage = getTotalPoundage(modelMapFormatted, exName);
                    for(Map.Entry<String, List<String>> map2 : completedMapFormatted.entrySet()){
                        // For each list in the completed/actual maps

                        String delims = "[_]";
                        String[] tokens = map2.getValue().get(0).split(delims);
                        String splitExName = tokens[0];

                        if(splitExName.equals("Bench Press (Barbell - Incline)")){
                            Log.i("info", "info");
                        }

                        String exNameCompleted = map2.getValue().get(0);

                        if(exName.equals(exNameCompleted)){
                            // same ex names
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
                                    templateClass.setNewDateMapValues(todayString, exName, bool);
                                }
                            }else if(tokens.length < 3){
                                int totalPoundage2 = getTotalPoundage(completedMapFormatted, exName);
                                if(!exercisesAlreadyGenerated.contains(exName)){
                                    if(totalPoundage2 >= totalPoundage){
                                        // algo
                                        generateAlgo(exName, false);
                                        exercisesAlreadyGenerated.add(exName);
                                    }else{
                                        // set to false
                                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                ("0_key")
                                                .get(0);
                                        String bool = "false";
                                        templateClass.setNewDateMapValues(todayString, exName, bool);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        String date = LocalDate.now().toString();
        HashMap<String, List<String>> workoutInfoMap = getMapForHistory(completedMap);
        WorkoutHistoryModelClass historyModelClass = new WorkoutHistoryModelClass(publicDescription, privateJournal,
                date, mediaRef, workoutInfoMap);

        DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateClass.getTemplateName());
        DatabaseReference workoutHistoryRef = mRootRef.child("workout_history").child(uid).child(LocalDate.now()
                .toString());
        //DatabaseReference feedRef = mRootRef.child("feed") we're going to need to look into this hard

        templateRef.setValue(templateClass);
        workoutHistoryRef.setValue(historyModelClass);


        return view;
    }

    //TODO: check if today's been done you retard

    private HashMap<String, List<String>> getMapForHistory(HashMap<String, HashMap<String, List<String>>> completedMap){
        HashMap<String, List<String>> historyMap = new HashMap<>();
        for(int i = 0; i < completedMap.size(); i++){
            for(Map.Entry<String, HashMap<String, List<String>>> mapEntry1 : completedMap.entrySet()){
                List<String> exerciseList = new ArrayList<>();
                String[] keyTokens = mapEntry1.getKey().split("_");
                int keyInc = Integer.parseInt(keyTokens[0]);
                if(keyInc == i + 1){
                    for(int j = 0; j < mapEntry1.getValue().size(); j++){
                        for(Map.Entry<String, List<String>> mapEntry2 : mapEntry1.getValue().entrySet()){
                            String[] keyTokens2 = mapEntry2.getKey().split("_");
                            int keyInc2 = Integer.parseInt(keyTokens2[0]);
                            if(keyInc2 == j){
                                for(String string : mapEntry2.getValue()){
                                    String[] exTokens = string.split("_");
                                    if(isExerciseName(string)){
                                        exerciseList.add(string);
                                    }else{
                                        if(isChecked(string)){
                                            exerciseList.add(exTokens[0]);
                                            // TODO: unit here. Units everywhere that we see "@"
                                        }
                                    }
                                }
                            }
                        }
                    }
                    historyMap.put(historyMap.size() + "_key", exerciseList);
                }
            }
        }

        return historyMap;
    }

    private boolean isChecked(String string){
        boolean bool = false;

        String[] tokens = string.split("_");

        if(tokens[1].equals("checked")){
            bool = true;
        }

        return bool;
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

    private void generateAlgoForSuperset2(String exName, String tag, String exNameUnformatted){

        HashMap<String, List<String>> hashMapCopy = new HashMap<>();
        hashMapCopy.putAll(originalHashmap);

        String delims1 = "[_]";
        String[] exNameTokens = exNameUnformatted.split(delims1);
        String exNameFormatted = "";
        for(int i = 0; i < exNameTokens.length - 1; i++){
            exNameFormatted = exNameFormatted + exNameTokens[i] + "_";
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"

        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else{
            for(Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if(isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                        if(algoDateMap.getValue().size() > 4){
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }else{
                            // check with today
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }
                    }
                }
            }
        }



        boolean firstLoopBool = true;

        LocalDate newDate = LocalDate.now();
        for(Map.Entry<String, List<String>> algoMap : templateClass.getAlgorithmInfo().entrySet()){
            if(algoMap.getValue().get(0).equals("all")){
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
                                        if(isRunning){
                                            try {
                                                if(isToday(algoDateMap.getValue().get(3))) {
                                                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                                                        if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                            // compare, keep everything the same, set to true.
                                                            hasEx = true;
                                                            int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                            int sets = 0;
                                                            int reps = 0;
                                                            int weight = 0;
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(1))) {
                                                                sets = Integer.parseInt(tokens[0]);
                                                                sets += Integer.parseInt(algoMap.getValue().get(2));
                                                            } else {
                                                                sets = Integer.parseInt(tokens[0]);
                                                            }
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(3))) {
                                                                reps = Integer.parseInt(tokens[1]);
                                                                reps += Integer.parseInt(algoMap.getValue().get(4));
                                                            } else {
                                                                reps = Integer.parseInt(tokens[1]);
                                                            }
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(5))) {
                                                                if (!isExerciseName(tokens[2])) {
                                                                    weight = Integer.parseInt(tokens[2]);
                                                                    weight += Integer.parseInt(algoMap.getValue().get(6));
                                                                    if (Boolean.parseBoolean(algoMap.getValue().get(7))) {
                                                                        /**
                                                                         *
                                                                         */
                                                                        if(Integer.parseInt(algoMap.getValue().get(1)
                                                                        ) == Integer.parseInt(algoMap.getValue().get
                                                                                (5)) || Integer.parseInt(algoMap
                                                                                .getValue().get(3)) == Integer
                                                                                .parseInt(algoMap.getValue().get(5))){

                                                                        }else{
                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                sets = sets - Integer.parseInt(algoMap.getValue().get(2));
                                                                                reps = reps - Integer.parseInt(algoMap.getValue().get(4));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (!isExerciseName(tokens[2])) {
                                                                    weight = Integer.parseInt(tokens[2]);
                                                                }
                                                            }

                                                            String concat = Integer.toString
                                                                    (sets) + "x" + Integer.toString(reps)
                                                                    + "@" + Integer.toString
                                                                    (weight);

                                                            newValueList.add(concat);
                                                        } else {
                                                            newValueList.add(string);
                                                        }
                                                    }
                                                }
                                            } catch (IndexOutOfBoundsException e){
                                                Log.i("info", "out of bounds");
                                            }

                                        }else{
                                            // if is not running
                                            instantiateList = true;
                                        }
                                        if(!hasEx){
                                            // no recorded instance of this ex on this day
                                            instantiateList = true;
                                        }
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

                            if(firstLoopBool){
                                DateTime dateTime = new DateTime();
                                int currentWeekday = dateTime.getDayOfWeek();

                                if(templateClass.getAlgorithmDateMap() == null){
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put("0_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }else{
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    newHashMap.putAll(templateClass.getAlgorithmDateMap());
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }

                                //templateClass.updateRunningDate(exNameFormatted);

                                firstLoopBool = false;
                            }



                        }else{
                            // UPDATE maps
                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            String bool = "true";

                            originalHashmap.put(key, newValueList);
                            //templateClass.setNewDateMapValues(exNameFormatted, bool, todayString);

                            String oldDate = null;
                            String dateKey = null;

                            for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                    ()){
                                if(isToday(dateMap2.getValue().get(3))) {
                                    if (dateMap2.getValue().get(0).equals(exNameFormatted)) {
                                        dateKey = dateMap2.getKey();
                                        oldDate = dateMap2.getValue().get(1);
                                    }
                                }
                            }

                            if(dateKey != null){
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                newList.add(oldDate);
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(dateKey, newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }else{
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(oldDate == null){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }


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

                            firstLoopBool = false;
                        }
                    }
                }
            }
        }

        Log.i("info", "String");

    }

    private void generateAlgo2(String exName, boolean isSuperset) {

        isSuperset = false;

        /**
         * What I want it to do:
         * Increase the sets/reps/weight for each exercise with the ex name of "exName"
         */

        HashMap<String, List<String>> valueMap = new HashMap<>();
        HashMap<String, List<String>> newValueMap = new HashMap<>();

        List<String> valueList = new ArrayList<>();
        String key = null;
        for (Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()) {
            if (map1.getValue().get(0).equals(exName)) {
                if (isSuperset == isSupersetList(map1.getValue())) {
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                    valueMap.put(map1.getKey(), map1.getValue());
                }
            }
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else {
            for (Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if (isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exName)) {
                        if (algoDateMap.getValue().size() > 4) {
                            if (getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        } else {
                            // check with today
                            if (getWeeksBetween(algoDateMap.getValue().get(1), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        }
                    }
                }
            }
        }

        newValueMap.putAll(valueMap);

        boolean goneThroughOnce = false;

        LocalDate newDate = LocalDate.now();
        for (Map.Entry<String, List<String>> map2 : templateClass.getAlgorithmInfo().entrySet()) {
            //if(map2.getValue().size() < 12){
            if(!goneThroughOnce){
                if(map2.getValue().get(0).equals("all")) {
                    goneThroughOnce = true;
                    //List<String> newValueList = new ArrayList<>();
                    //newValueList.add(exName);
                    boolean instantiateList = false;
                    for (Map.Entry<String, List<String>> valueMapEntry : valueMap.entrySet()) {
                        List<String> subList = new ArrayList<>();
                        subList.addAll(valueMapEntry.getValue());
                        List<String> newValueList = new ArrayList<>();
                        newValueList.add(exName);
                        for (String string : subList) {
                            if (!isExerciseName(string)) {
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if (templateClass.getAlgorithmDateMap() != null) {
                                    boolean hasEx = false;
                                    // need to check for the case of if empty or if no entries were found
                                    for (Map.Entry<String, List<String>> map : templateClass.getAlgorithmDateMap().entrySet()) {
                                        if(isRunning) {
                                            if (isToday(map.getValue().get(3))) {
                                                if (map.getValue().get(0).equals(exName)) {
                                                    if (Boolean.parseBoolean(map.getValue().get(2))) {
                                                        // compare, keep everything the same, set to true.
                                                        hasEx = true;
                                                        int weeksSinceLast = getWeeksSinceLast(map.getValue().get(1));

                                                        int sets = 0;
                                                        int reps = 0;
                                                        int weight = 0;
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(1))) {
                                                            sets = Integer.parseInt(tokens[0]);
                                                            sets += Integer.parseInt(map2.getValue().get(2));
                                                        } else {
                                                            sets = Integer.parseInt(tokens[0]);
                                                        }
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(3))) {
                                                            reps = Integer.parseInt(tokens[1]);
                                                            reps += Integer.parseInt(map2.getValue().get(4));
                                                        } else {
                                                            reps = Integer.parseInt(tokens[1]);
                                                        }
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(5))) {
                                                            if (!isExerciseName(tokens[2])) {
                                                                weight = Integer.parseInt(tokens[2]);
                                                                weight += Integer.parseInt(map2.getValue().get(6));
                                                                if (Boolean.parseBoolean(map2.getValue().get(7))) {
                                                                    /**
                                                                     *
                                                                     */
                                                                    if(Integer.parseInt(map2.getValue().get(1)
                                                                    ) == Integer.parseInt(map2.getValue().get
                                                                            (5)) || Integer.parseInt(map2
                                                                            .getValue().get(3)) == Integer
                                                                            .parseInt(map2.getValue().get(5))){

                                                                    }else{
                                                                        for (int j = 1; j < weeksSinceLast; j++) {
                                                                            sets = sets - Integer.parseInt(map2.getValue
                                                                                    ().get(2));
                                                                            reps = reps - Integer.parseInt(map2.getValue
                                                                                    ().get(4));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (!isExerciseName(tokens[2])) {
                                                                weight = Integer.parseInt(tokens[2]);
                                                            }
                                                        }

                                                        String concat = Integer.toString
                                                                (sets) + "x" + Integer.toString(reps)
                                                                + "@" + Integer.toString
                                                                (weight);

                                                        newValueList.add(concat);
                                                        //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                    } else {
                                                        // don't compare, set the date to today, and set bool to true.
                                                        newValueList.add(string);
                                                        //newValueMap.get(valueMapEntry.getKey()).add(string);
                                                    }
                                                }
                                            }
                                        } else {
                                            // is not running
                                            instantiateList = true;
                                        }

                                        newValueMap.put(valueMapEntry.getKey(), newValueList);
                                    }
                                    if (!hasEx) {
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                } else {
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }
                        }
                    }



                    if (instantiateList) {
                        // instantiate datemap with this exercise

                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();

                        if (templateClass.getAlgorithmDateMap() == null) {
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put("0_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        } else {
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            newHashMap.putAll(templateClass.getAlgorithmDateMap());
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }

                    } else {
                        // UPDATE maps (increment)

                        //originalHashmap.put(key, newValueList);
                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();
                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                .get(0);
                        String bool = "true";

                        originalHashmap.putAll(newValueMap);

                        String oldDate = null;
                        String dateKey = null;

                        for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                ()){
                            if(isToday(dateMap2.getValue().get(3))) {
                                if (dateMap2.getValue().get(0).equals(exName)) {
                                    dateKey = dateMap2.getKey();
                                    oldDate = dateMap2.getValue().get(1);
                                }
                            }
                        }

                        if(dateKey != null){
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(oldDate);
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(dateKey, newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }else{
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(oldDate == null){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }

                        String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                        if (mapName.equals("mMapOne")) {
                            templateClass.setMapOne(originalHashmap);
                        } else if (mapName.equals("mMapTwo")) {
                            templateClass.setMapTwo(originalHashmap);
                        } else if (mapName.equals("mMapThree")) {
                            templateClass.setMapThree(originalHashmap);
                        } else if (mapName.equals("mMapFour")) {
                            templateClass.setMapFour(originalHashmap);
                        } else if (mapName.equals("mMapFive")) {
                            templateClass.setMapFive(originalHashmap);
                        } else if (mapName.equals("mMapSix")) {
                            templateClass.setMapSix(originalHashmap);
                        } else if (mapName.equals("mMapSeven")) {
                            templateClass.setMapSeven(originalHashmap);
                        }

                        Log.i("info", "String");
                    }
                }
                //}
            }
        }
    }

    private void generateAlgoForSuperset(String exName, String tag, String exNameUnformatted){

        HashMap<String, List<String>> hashMapCopy = new HashMap<>();
        hashMapCopy.putAll(originalHashmap);

        String delims1 = "[_]";
        String[] exNameTokens = exNameUnformatted.split(delims1);
        String exNameFormatted = "";
        for(int i = 0; i < exNameTokens.length - 1; i++){
            exNameFormatted = exNameFormatted + exNameTokens[i] + "_";
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else{
            for(Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if(isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                        if(algoDateMap.getValue().size() > 4){
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }else{
                            // check with today
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }
                    }
                }
            }
        }



        boolean firstLoopBool = true;

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
                                            if(isRunning){
                                                try {
                                                if(isToday(algoDateMap.getValue().get(3))) {
                                                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                                                        if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                            // compare, keep everything the same, set to true.
                                                            hasEx = true;
                                                            int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                            int sets = 0;
                                                            int reps = 0;
                                                            int weight = 0;
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(1))) {
                                                                sets = Integer.parseInt(tokens[0]);
                                                                sets += Integer.parseInt(algoMap.getValue().get(2));
                                                            } else {
                                                                sets = Integer.parseInt(tokens[0]);
                                                            }
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(3))) {
                                                                reps = Integer.parseInt(tokens[1]);
                                                                reps += Integer.parseInt(algoMap.getValue().get(4));
                                                            } else {
                                                                reps = Integer.parseInt(tokens[1]);
                                                            }
                                                            if (weeksSinceLast >= Integer.parseInt(algoMap.getValue().get(5))) {
                                                                if (!isExerciseName(tokens[2])) {
                                                                    weight = Integer.parseInt(tokens[2]);
                                                                    weight += Integer.parseInt(algoMap.getValue().get(6));
                                                                    if (Boolean.parseBoolean(algoMap.getValue().get(7))) {
                                                                        /**
                                                                         *
                                                                         */
                                                                        if(Integer.parseInt(algoMap.getValue().get(1)
                                                                        ) == Integer.parseInt(algoMap.getValue().get
                                                                                (5)) || Integer.parseInt(algoMap
                                                                                .getValue().get(3)) == Integer
                                                                                .parseInt(algoMap.getValue().get(5))){

                                                                        }else{
                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                sets = sets - Integer.parseInt(algoMap.getValue().get(2));
                                                                                reps = reps - Integer.parseInt(algoMap.getValue().get(4));
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            } else {
                                                                if (!isExerciseName(tokens[2])) {
                                                                    weight = Integer.parseInt(tokens[2]);
                                                                }
                                                            }

                                                            String concat = Integer.toString
                                                                    (sets) + "x" + Integer.toString(reps)
                                                                    + "@" + Integer.toString
                                                                    (weight);

                                                            newValueList.add(concat);
                                                        } else {
                                                            newValueList.add(string);
                                                        }
                                                    }
                                                }
                                            } catch (IndexOutOfBoundsException e){
                                                    Log.i("info", "out of bounds");
                                            }

                                        }else{
                                                // if is not running
                                                instantiateList = true;
                                            }
                                        if(!hasEx){
                                            // no recorded instance of this ex on this day
                                            instantiateList = true;
                                        }
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

                            if(firstLoopBool){
                                DateTime dateTime = new DateTime();
                                int currentWeekday = dateTime.getDayOfWeek();

                                if(templateClass.getAlgorithmDateMap() == null){
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put("0_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }else{
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    newHashMap.putAll(templateClass.getAlgorithmDateMap());
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }

                                //templateClass.updateRunningDate(exNameFormatted);

                                firstLoopBool = false;
                            }



                        }else{
                            // UPDATE maps
                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            String bool = "true";

                            originalHashmap.put(key, newValueList);
                            //templateClass.setNewDateMapValues(exNameFormatted, bool, todayString);

                            String oldDate = null;
                            String dateKey = null;

                            for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                    ()){
                                if(isToday(dateMap2.getValue().get(3))) {
                                    if (dateMap2.getValue().get(0).equals(exNameFormatted)) {
                                        dateKey = dateMap2.getKey();
                                        oldDate = dateMap2.getValue().get(1);
                                    }
                                }
                            }

                            if(dateKey != null){
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                newList.add(oldDate);
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(dateKey, newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }else{
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(oldDate == null){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }


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

                            firstLoopBool = false;
                        }
                    }
                }
            }
        }

        Log.i("info", "String");

    }

    private void generateAlgo(String exName, boolean isSuperset) {

        isSuperset = false;

        /**
         * What I want it to do:
         * Increase the sets/reps/weight for each exercise with the ex name of "exName"
         */

        HashMap<String, List<String>> valueMap = new HashMap<>();
        HashMap<String, List<String>> newValueMap = new HashMap<>();

        List<String> valueList = new ArrayList<>();
        String key = null;
        for (Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()) {
            if (map1.getValue().get(0).equals(exName)) {
                if (isSuperset == isSupersetList(map1.getValue())) {
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                    valueMap.put(map1.getKey(), map1.getValue());
                }
            }
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else {
            for (Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if (isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exName)) {
                        if (algoDateMap.getValue().size() > 4) {
                            if (getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        } else {
                            // check with today
                            if (getWeeksBetween(algoDateMap.getValue().get(1), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        }
                    }
                }
            }
        }

        newValueMap.putAll(valueMap);

        boolean goneThroughOnce = false;

        LocalDate newDate = LocalDate.now();
        for (Map.Entry<String, List<String>> map2 : templateClass.getAlgorithmInfo().entrySet()) {
            //if(map2.getValue().size() < 12){
                if(!goneThroughOnce){
                    if(map2.getValue().get(0).equals(exName)) {
                    goneThroughOnce = true;
                    //List<String> newValueList = new ArrayList<>();
                    //newValueList.add(exName);
                    boolean instantiateList = false;
                    for (Map.Entry<String, List<String>> valueMapEntry : valueMap.entrySet()) {
                        List<String> subList = new ArrayList<>();
                        subList.addAll(valueMapEntry.getValue());
                        List<String> newValueList = new ArrayList<>();
                        newValueList.add(exName);
                        for (String string : subList) {
                            if (!isExerciseName(string)) {
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if (templateClass.getAlgorithmDateMap() != null) {
                                        boolean hasEx = false;
                                        // need to check for the case of if empty or if no entries were found
                                        for (Map.Entry<String, List<String>> map : templateClass.getAlgorithmDateMap().entrySet()) {
                                            if(isRunning) {
                                            if (isToday(map.getValue().get(3))) {
                                                if (map.getValue().get(0).equals(exName)) {
                                                    if (Boolean.parseBoolean(map.getValue().get(2))) {
                                                        // compare, keep everything the same, set to true.
                                                        hasEx = true;
                                                        int weeksSinceLast = getWeeksSinceLast(map.getValue().get(1));

                                                        int sets = 0;
                                                        int reps = 0;
                                                        int weight = 0;
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(1))) {
                                                            sets = Integer.parseInt(tokens[0]);
                                                            sets += Integer.parseInt(map2.getValue().get(2));
                                                        } else {
                                                            sets = Integer.parseInt(tokens[0]);
                                                        }
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(3))) {
                                                            reps = Integer.parseInt(tokens[1]);
                                                            reps += Integer.parseInt(map2.getValue().get(4));
                                                        } else {
                                                            reps = Integer.parseInt(tokens[1]);
                                                        }
                                                        if (weeksSinceLast >= Integer.parseInt(map2.getValue().get(5))) {
                                                            if (!isExerciseName(tokens[2])) {
                                                                weight = Integer.parseInt(tokens[2]);
                                                                weight += Integer.parseInt(map2.getValue().get(6));
                                                                if (Boolean.parseBoolean(map2.getValue().get(7))) {
                                                                    /**
                                                                     *
                                                                     */
                                                                    if(Integer.parseInt(map2.getValue().get(1)
                                                                    ) == Integer.parseInt(map2.getValue().get
                                                                            (5)) || Integer.parseInt(map2
                                                                            .getValue().get(3)) == Integer
                                                                            .parseInt(map2.getValue().get(5))){

                                                                    }else{
                                                                        for (int j = 1; j < weeksSinceLast; j++) {
                                                                            sets = sets - Integer.parseInt(map2.getValue
                                                                                    ().get(2));
                                                                            reps = reps - Integer.parseInt(map2.getValue
                                                                                    ().get(4));
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        } else {
                                                            if (!isExerciseName(tokens[2])) {
                                                                weight = Integer.parseInt(tokens[2]);
                                                            }
                                                        }

                                                        String concat = Integer.toString
                                                                (sets) + "x" + Integer.toString(reps)
                                                                + "@" + Integer.toString
                                                                (weight);

                                                        newValueList.add(concat);
                                                        //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                    } else {
                                                        // don't compare, set the date to today, and set bool to true.
                                                        newValueList.add(string);
                                                        //newValueMap.get(valueMapEntry.getKey()).add(string);
                                                    }
                                                }
                                            }
                                        } else {
                                            // is not running
                                            instantiateList = true;
                                        }

                                        newValueMap.put(valueMapEntry.getKey(), newValueList);
                                    }
                                    if (!hasEx) {
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                } else {
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }
                        }
                    }



                    if (instantiateList) {
                        // instantiate datemap with this exercise

                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();

                        if (templateClass.getAlgorithmDateMap() == null) {
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put("0_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        } else {
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            newHashMap.putAll(templateClass.getAlgorithmDateMap());
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }

                    } else {
                        // UPDATE maps (increment)

                        //originalHashmap.put(key, newValueList);
                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();
                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                .get(0);
                        String bool = "true";

                        originalHashmap.putAll(newValueMap);

                        String oldDate = null;
                        String dateKey = null;

                        for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                ()){
                            if(isToday(dateMap2.getValue().get(3))) {
                                if (dateMap2.getValue().get(0).equals(exName)) {
                                    dateKey = dateMap2.getKey();
                                    oldDate = dateMap2.getValue().get(1);
                                }
                            }
                        }

                        if(dateKey != null){
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(oldDate);
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(dateKey, newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }else{
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(oldDate == null){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }

                        String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                        if (mapName.equals("mMapOne")) {
                            templateClass.setMapOne(originalHashmap);
                        } else if (mapName.equals("mMapTwo")) {
                            templateClass.setMapTwo(originalHashmap);
                        } else if (mapName.equals("mMapThree")) {
                            templateClass.setMapThree(originalHashmap);
                        } else if (mapName.equals("mMapFour")) {
                            templateClass.setMapFour(originalHashmap);
                        } else if (mapName.equals("mMapFive")) {
                            templateClass.setMapFive(originalHashmap);
                        } else if (mapName.equals("mMapSix")) {
                            templateClass.setMapSix(originalHashmap);
                        } else if (mapName.equals("mMapSeven")) {
                            templateClass.setMapSeven(originalHashmap);
                        }

                        Log.i("info", "String");
                    }
                }
                //}
            }
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

    private double getWeeksBetween(String date1, String date2){
        double weeksInt = 0;

        LocalDate firstDate = LocalDate.parse(date1);
        LocalDate secondDate = LocalDate.parse(date2);

        int daysBetween = Days.daysBetween(firstDate, secondDate).getDays();

        weeksInt = daysBetween / 7.0;

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


}




















