package com.liftdom.workout_programs.PPL_Reddit;

import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 10/31/2019.
 */
public class PPLRedditClass {
    /**
     * Pull/Push/Legs option as well..
     *
     *
     * "PULL"
     *  - Deadlifts 1x5+/Barbell Rows 4x5, 1x5+ (alternate, so if you did deadlifts on Monday, you
     *      would do rows on Thursday, and so on)
     *  - 3x8-12 Pulldowns OR Pullups OR chinups
     *  - 3x8-12 seated cable rows OR chest supported rows
     *  - 5x15-20 face pulls
     *  - 4x8-12 hammer curls
     *  - 4x8-12 dumbbell curls
     *  - abs on deadlift days
     *      - just do a few sets of (weighted) planks or a few sets of ab wheel and some hanging leg raises.
     *
     * "PUSH"
     *  - 4x5, 1x5+ bench press/4x5, 1x5+ overhead press (alternate in the same fashion as the
     *      rows and deadlifts)
     *  - 3x8-12 overhead press/3x8-12 bench press (do the opposite movement: if you bench pressed
     *      first, overhead press here)
     *  - 3x8-12 incline dumbbell press
     *  - 3x8-12 triceps pushdowns SS 3x15-20 lateral raises
     *  - 3x8-12 overhead triceps extensions SS 3x15-20 lateral raises
     *
     * "LEGS"
     *  - 2x5, 1x5+ squat
     *  - 3x8-12 Romanian Deadlift
     *  - 3x8-12 leg press
     *  - 3x8-12 leg curls
     *  - 5x8-12 calf raises
     *  - abs on squat days
     *      - just do a few sets of (weighted) planks or a few sets of ab wheel and some hanging leg raises.
     *
     * "How much weight per session?"
     *  - 2.5kg/5lbs for upper body lifts (bench press, row, overhead press)
     *  - 2.5kg/5lbs for squats
     *  - 5kg/10lbs for deadlifts
     *  - For accessories:
     *
     * "Warmup"
     *  - Empty bar x 10
     *  - 95lbs x10 - 47%
     *  - 135lbs x5 - 67%
     *  - 185lbs x3 - 92%
     *  - 200lbs 4x5, 1x5+
     *
     * "REST"
     *  - 3-5 minutes between your first exercise of the day
     *  - 1-3 minutes between all your other exercises
     *
     * "DELOADING"
     *  - "Take 10% off your working weights and work back up."
     *
     *  "Strength Version"
     *  - replace 3x8-12 RDLs with 3x5 RDLs.
     *  - Add weighted dips,
     *  - Weighted chins-ups/pull-ups in the 3x5 range (with bodyweight back off sets).
     *  - So if you want to lift heavy weights then feel free to alter the compound movement accessories
     *      (so your presses, your pulldowns or rows, your romanian deadlifts or your leg presses)
     *      to be in the lower rep ranges. 3x4-6 is perfect for things like this.
     *
     * "Endurance Version"
     *  - Keep the first exercise the same, up the rep range on everything else.
     *      15-20 reps is a great range for increasing endurance.
     *
     */

    /**
     * For weight progression, let's add checkmarks for stalling the weight increase and for
     * activating deload (dropping weights by 10%).
     *
     *
     *
     * So what does this all mean practically?
     *
     * We have specific workouts to do each day.
     * The exact main lifts change depending on day.
     * The accessories are fixed in set schemes, but can be changed.
     *  This means that they should have names like "push1accessory1" or something like that, and
     *  we change the value of that depending on what the user chooses. The weight will be
     *  changed depending on the value. This means we'll be storing the exercises as keys with
     *  the values being the weights used. Just like in W531fB.
     *
     * This whole thing is very similar to W531fB. Main lifts + hot swapped accessories.
     *
     * We need to collect begin date and we also need to know the format. This will determine
     * what is returned.
     *
     *
     *
     */

    //Remember rear delt flyes replacing face pulls must be supersetted with band pull aparts

    private HashMap<String, String> mExtraInfo;

    private String beginDateString;
    private LocalDate beginDate;
    private LocalDate todaysDate;

    private String pulldowns;
    private String seatedCableRows;
    private String facePulls;
    private String dumbbellCurls;
    private String hammerCurls;
    private String inclineDB;
    private String tricepsPushdowns;
    private String overheadTricepsExtensions;
    private String legPress;
    private String legCurls;
    private String calfRaises;
    private String dips;
    /**
     * Add dips to push
     */
    private String abs1;
    private String abs2;
    private String rdl;
    private String benchPressA;
    private String ohpA;

    private String lateralRaises = "Lateral Raise (Dumbbell)";

    private String benchPress;
    private String ohp;
    private String squat;
    private String deadlift;
    private String barbellRows;
    private String benchPressWeight;
    private String ohpWeight;
    private String squatWeight;
    private String deadliftWeight;
    private String barbellRowsWeight;

    private String format;
    private boolean isWarmup;
    private String version;

    String normalDefault = "8@0";
    String strengthDefault = "4@0";
    String highRepsDefault = "15@0";
    String normalDefaultBW = "8@B.W.";
    String strengthDefaultBW = "4@B.W.";
    String highRepsDefaultBW = "15@B.W.";

    private boolean isFirstWeek;


    public PPLRedditClass(HashMap<String, String> extraInfo){

        mExtraInfo = extraInfo;

        setValues(extraInfo);

        beginDate = LocalDate.parse(beginDateString);
        todaysDate = LocalDate.now();
    }

    private void setValues(HashMap<String, String> extraInfo){
        beginDateString = extraInfo.get("beginDate");
        format = extraInfo.get("format");
        pulldowns = extraInfo.get("pulldowns");
        seatedCableRows = extraInfo.get("seatedCableRows");
        facePulls = extraInfo.get("facePulls");
        dumbbellCurls = extraInfo.get("dumbbellCurls");
        hammerCurls = extraInfo.get("hammerCurls");
        inclineDB = extraInfo.get("inclineDB");
        tricepsPushdowns = extraInfo.get("tricepsPushdowns");
        overheadTricepsExtensions = extraInfo.get("overheadTricepsExtensions");
        legPress = extraInfo.get("legPress");
        legCurls = extraInfo.get("legCurls");
        calfRaises = extraInfo.get("calfRaises");
        dips = extraInfo.get("dips");
        abs1 = extraInfo.get("abs1");
        abs2 = extraInfo.get("abs2");
        rdl = extraInfo.get("rdl");
        benchPressA = extraInfo.get("benchPressA");
        ohpA = extraInfo.get("ohpA");
        isWarmup = Boolean.parseBoolean(extraInfo.get("isWarmup"));
        version = extraInfo.get("version");
        benchPress = extraInfo.get("benchPress");
        ohp = extraInfo.get("ohp");
        squat = extraInfo.get("squat");
        deadlift = extraInfo.get("deadlift");
        barbellRows = extraInfo.get("barbellRows");
        try{
            benchPressWeight = extraInfo.get("Bench Press (Barbell - Flat)");
        }catch (NullPointerException e){

        }
        try{
            ohpWeight = extraInfo.get("Overhead Press (Barbell)");
        }catch (NullPointerException e){

        }
        try{
            squatWeight = extraInfo.get("Squat (Barbell - Back)");
        }catch (NullPointerException e){

        }
        try{
            deadliftWeight = extraInfo.get("Deadlift (Barbell - Conventional)");
        }catch (NullPointerException e){

        }
        try{
            barbellRowsWeight = extraInfo.get("Row (Barbell - Bent-over)");
        }catch (NullPointerException e){

        }
    }

    public HashMap<String, List<String>> generateWorkoutMap(){
        HashMap<String, List<String>> map = new HashMap<>();

        if(beginDate.isAfter(todaysDate)){
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            map.put("1_key", workoutList);
        }else{
            int daysBetween = Days.daysBetween(beginDate, todaysDate).getDays();

            int week = daysBetween / 7;
            int days = daysBetween % 7;

            week++;
            days++;

            if(week == 1){
                isFirstWeek = true;
            }

            String type = getDayType(days);

            map = getWorkout(type);
        }

        return map;
    }

    public HashMap<String, List<String>> getWorkout(String dayType){
        HashMap<String, List<String>> map = new HashMap<>();

        String delims = "[_]";
        String[] tokens = dayType.split(delims);

        int type = Integer.parseInt(tokens[1]);

        if(tokens[0].equals("push")){
            map = getPushWorkout(type);
        }else if(tokens[0].equals("pull")){
            map = getPullWorkout(type);
        }else if(tokens[0].equals("legs")){
            map = getLegsWorkout();
        }else if(tokens[0].equals("rest")){
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            map.put("1_key", workoutList);
        }

        return map;
    }

    private String getDayType(int days){
        String dayType = "";

        if(format.equals("pushplpplr")){
            if(days == 1){
                dayType = "push_1";
            }else if(days == 2){
                dayType = "pull_1";
            }else if(days == 3){
                dayType = "legs_1";
            }else if(days == 4){
                dayType = "push_2";
            }else if(days == 5){
                dayType = "pull_2";
            }else if(days == 6){
                dayType = "legs_2";
            }else if(days == 7){
                dayType = "rest";
            }
        }else if(format.equals("pullplpplr")){
            if(days == 1){
                dayType = "pull_1";
            }else if(days == 2){
                dayType = "push_1";
            }else if(days == 3){
                dayType = "legs_1";
            }else if(days == 4){
                dayType = "pull_2";
            }else if(days == 5){
                dayType = "push_2";
            }else if(days == 6){
                dayType = "legs_2";
            }else if(days == 7){
                dayType = "rest";
            }
        }else if(format.equals("pushplrppl")){
            if(days == 1){
                dayType = "push_1";
            }else if(days == 2){
                dayType = "pull_1";
            }else if(days == 3){
                dayType = "legs_1";
            }else if(days == 4){
                dayType = "rest";
            }else if(days == 5){
                dayType = "push_2";
            }else if(days == 6){
                dayType = "pull_2";
            }else if(days == 7){
                dayType = "legs_2";
            }
        }else if(format.equals("pullplrppl")){
            if(days == 1){
                dayType = "pull_1";
            }else if(days == 2){
                dayType = "push_1";
            }else if(days == 3){
                dayType = "legs_1";
            }else if(days == 4){
                dayType = "rest";
            }else if(days == 5){
                dayType = "pull_2";
            }else if(days == 6){
                dayType = "push_2";
            }else if(days == 7){
                dayType = "legs_2";
            }
        }

        return dayType;
    }

    public HashMap<String, List<String>> getPullWorkout(int type) {
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> mainLiftList = new ArrayList<>();

        if(type == 1){
            if(deadliftWeight == null){
                deadliftWeight = "0";
            }else{
                if(deadliftWeight.isEmpty()){
                    deadliftWeight = "0";
                }
            }
            mainLiftList.add("Deadlift (Barbell - Conventional)");
            if(isWarmup){
                if(!isFirstWeek){
                    mainLiftList.addAll(getWarmupList(deadliftWeight));
                }
            }
            mainLiftList.add("1x5_a@" + deadliftWeight);
        }else if(type == 2){
            if(barbellRowsWeight == null){
                barbellRowsWeight = "0";
            }else{
                if(barbellRowsWeight.isEmpty()){
                    barbellRowsWeight = "0";
                }
            }
            mainLiftList.add("Row (Barbell - Bent-over)");
            if(isWarmup){
                if(!isFirstWeek){
                    mainLiftList.addAll(getWarmupList(barbellRowsWeight));
                }
            }
            mainLiftList.add("4x5@" + barbellRowsWeight);
            mainLiftList.add("1x5_a@" + barbellRowsWeight);
        }

        List<String> pulldownList = new ArrayList<>();
        List<String> seatedCableRowsList = new ArrayList<>();
        List<String> facePullsList = new ArrayList<>();
        List<String> hammerCurlsList = new ArrayList<>();
        List<String> dumbbellCurlsList = new ArrayList<>();
        List<String> absList = new ArrayList<>();

        /**
         * So, "pulldowns" gets us the ex name.
         * Then we take the ex name and look for it in extra info.
         * It's name in extra info will have the value reps@weight.
         * If it doesn't exist in extra info, it's defaultReps@0.
         */

        if(version.equals("normal")){
            if(mExtraInfo.get(pulldowns) == null){
                mExtraInfo.put(pulldowns, normalDefault);
            }
            if(mExtraInfo.get(seatedCableRows) == null){
                mExtraInfo.put(seatedCableRows, normalDefault);
            }
            if(facePulls.equals("Rear Delt Flyes and Band Pulls")){
                if(mExtraInfo.get("Rear Delt Fly (Dumbbell)") == null){
                    mExtraInfo.put("Rear Delt Fly (Dumbbell)", normalDefault);
                }
                if(mExtraInfo.get("Pull-Aparts (Banded)") == null){
                    mExtraInfo.put("Pull-Aparts (Banded)", normalDefault);
                }
            }else{
                if(mExtraInfo.get(facePulls) == null){
                    mExtraInfo.put(facePulls, highRepsDefault);
                }
            }
            if(mExtraInfo.get(hammerCurls) == null){
                mExtraInfo.put(hammerCurls, normalDefault);
            }
            if(mExtraInfo.get(dumbbellCurls) == null){
                mExtraInfo.put(dumbbellCurls, normalDefault);
            }
            if(mExtraInfo.get(abs1) == null){
                mExtraInfo.put(abs1, normalDefaultBW);
            }

            if(isBodyweight(pulldowns)){
                mExtraInfo.put(pulldowns, normalDefaultBW);
            }

        }else if(version.equals("strength")){
            if(mExtraInfo.get(pulldowns) == null){
                mExtraInfo.put(pulldowns, strengthDefault);
            }
            if(mExtraInfo.get(seatedCableRows) == null){
                mExtraInfo.put(seatedCableRows, strengthDefault);
            }
            if(facePulls.equals("Rear Delt Flyes and Band Pulls")){
                if(mExtraInfo.get("Rear Delt Fly (Dumbbell)") == null){
                    mExtraInfo.put("Rear Delt Fly (Dumbbell)", normalDefault);
                }
                if(mExtraInfo.get("Pull-Aparts (Banded)") == null){
                    mExtraInfo.put("Pull-Aparts (Banded)", normalDefault);
                }
            }else{
                if(mExtraInfo.get(facePulls) == null){
                    mExtraInfo.put(facePulls, highRepsDefault);
                }
            }
            if(mExtraInfo.get(hammerCurls) == null){
                mExtraInfo.put(hammerCurls, normalDefault);
            }
            if(mExtraInfo.get(dumbbellCurls) == null){
                mExtraInfo.put(dumbbellCurls, normalDefault);
            }
            if(mExtraInfo.get(abs1) == null){
                mExtraInfo.put(abs1, normalDefaultBW);
            }

            if(isBodyweight(pulldowns)){
                mExtraInfo.put(pulldowns, normalDefaultBW);
            }

        }else if(version.equals("highReps")){
            if(mExtraInfo.get(pulldowns) == null){
                mExtraInfo.put(pulldowns, highRepsDefault);
            }
            if(mExtraInfo.get(seatedCableRows) == null){
                mExtraInfo.put(seatedCableRows, highRepsDefault);
            }
            if(facePulls.equals("Rear Delt Flyes and Band Pulls")){
                if(mExtraInfo.get("Rear Delt Fly (Dumbbell)") == null){
                    mExtraInfo.put("Rear Delt Fly (Dumbbell)", highRepsDefault);
                }
                if(mExtraInfo.get("Pull-Aparts (Banded)") == null){
                    mExtraInfo.put("Pull-Aparts (Banded)", highRepsDefault);
                }
            }else{
                if(mExtraInfo.get(facePulls) == null){
                    mExtraInfo.put(facePulls, highRepsDefault);
                }
            }
            if(mExtraInfo.get(hammerCurls) == null){
                mExtraInfo.put(hammerCurls, highRepsDefault);
            }
            if(mExtraInfo.get(dumbbellCurls) == null){
                mExtraInfo.put(dumbbellCurls, highRepsDefault);
            }
            if(mExtraInfo.get(abs1) == null){
                mExtraInfo.put(abs1, normalDefaultBW);
            }

            if(isBodyweight(pulldowns)){
                mExtraInfo.put(pulldowns, normalDefaultBW);
            }
        }

        pulldownList.add(pulldowns);
        pulldownList.add("3x" + mExtraInfo.get(pulldowns));

        seatedCableRowsList.add(seatedCableRows);
        seatedCableRowsList.add("3x" + mExtraInfo.get(seatedCableRows));

        if(facePulls.equals("Rear Delt Flyes and Band Pulls")){
            facePullsList.add("Rear Delt Fly (Dumbbell)");
            facePullsList.add("3x" + mExtraInfo.get("Rear Delt Fly (Dumbbell)"));
            facePullsList.add("Pull-Aparts (Banded)");
            facePullsList.add("3x" + mExtraInfo.get("Pull-Aparts (Banded)"));
        }else{
            facePullsList.add(facePulls);
            facePullsList.add("5x" + mExtraInfo.get(facePulls));
        }

        hammerCurlsList.add(hammerCurls);
        hammerCurlsList.add("4x" + mExtraInfo.get(hammerCurls));

        dumbbellCurlsList.add(dumbbellCurls);
        dumbbellCurlsList.add("4x" + mExtraInfo.get(dumbbellCurls));

        absList.add(abs1);
        absList.add("3x" + mExtraInfo.get(abs1));

        map.put("1_key", mainLiftList);
        map.put("2_key", pulldownList);
        map.put("3_key", seatedCableRowsList);
        map.put("4_key", facePullsList);
        map.put("5_key", hammerCurlsList);
        map.put("6_key", dumbbellCurlsList);
        map.put("7_key", absList);

        return map;
    }

    public HashMap<String, List<String>> getPushWorkout(int type) {
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> mainLiftList1 = new ArrayList<>();
        List<String> mainLiftList2 = new ArrayList<>();

        if(type == 1){
            if(benchPressWeight == null){
                benchPressWeight = "0";
            }else{
                if(benchPressWeight.isEmpty()){
                    benchPressWeight = "0";
                }
            }
            mainLiftList1.add("Bench Press (Barbell - Flat)");
            if(isWarmup){
                if(!isFirstWeek){
                    mainLiftList1.addAll(getWarmupList(benchPressWeight));
                }
            }
            mainLiftList1.add("4x5@" + benchPressWeight);
            mainLiftList1.add("1x5_a@" + benchPressWeight);
        }else if(type == 2){
            if(ohpWeight == null){
                ohpWeight = "0";
            }else{
                if(ohpWeight.isEmpty()){
                    ohpWeight = "0";
                }
            }
            mainLiftList1.add("Overhead Press (Barbell)");
            if(isWarmup){
                if(!isFirstWeek){
                    mainLiftList1.addAll(getWarmupList(ohpWeight));
                }
            }
            mainLiftList1.add("4x5@" + ohpWeight);
            mainLiftList1.add("1x5_a@" + ohpWeight);
        }

        List<String> inclineDBList = new ArrayList<>();
        List<String> tricepPushdownsList = new ArrayList<>();
        List<String> overheadTricepsExtensionsList = new ArrayList<>();

        if(version.equals("normal")){
            if(mExtraInfo.get(inclineDB) == null){
                mExtraInfo.put(inclineDB, normalDefault);
            }
            if(mExtraInfo.get(tricepsPushdowns) == null){
                mExtraInfo.put(tricepsPushdowns, normalDefault);
            }
            if(mExtraInfo.get(lateralRaises) == null){
                mExtraInfo.put(lateralRaises, highRepsDefault);
            }
            if(mExtraInfo.get(overheadTricepsExtensions) == null){
                mExtraInfo.put(overheadTricepsExtensions, normalDefault);
            }
            if(mExtraInfo.get("ohpA") == null){
                mExtraInfo.put("ohpA", normalDefault);
            }
            if(mExtraInfo.get("benchPressA") == null){
                mExtraInfo.put("benchPressA", normalDefault);
            }
        }else if(version.equals("strength")){
            if(mExtraInfo.get(inclineDB) == null){
                mExtraInfo.put(inclineDB, strengthDefault);
            }
            if(mExtraInfo.get(tricepsPushdowns) == null){
                mExtraInfo.put(tricepsPushdowns, normalDefault);
            }
            if(mExtraInfo.get(lateralRaises) == null){
                mExtraInfo.put(lateralRaises, highRepsDefault);
            }
            if(mExtraInfo.get(overheadTricepsExtensions) == null){
                mExtraInfo.put(overheadTricepsExtensions, normalDefault);
            }
            if(mExtraInfo.get("ohpA") == null){
                mExtraInfo.put("ohpA", strengthDefault);
            }
            if(mExtraInfo.get("benchPressA") == null){
                mExtraInfo.put("benchPressA", strengthDefault);
            }
        }else if(version.equals("highReps")){
            if(mExtraInfo.get(inclineDB) == null){
                mExtraInfo.put(inclineDB, highRepsDefault);
            }
            if(mExtraInfo.get(tricepsPushdowns) == null){
                mExtraInfo.put(tricepsPushdowns, highRepsDefault);
            }
            if(mExtraInfo.get(lateralRaises) == null){
                mExtraInfo.put(lateralRaises, highRepsDefault);
            }
            if(mExtraInfo.get(overheadTricepsExtensions) == null){
                mExtraInfo.put(overheadTricepsExtensions, highRepsDefault);
            }
            if(mExtraInfo.get("ohpA") == null){
                mExtraInfo.put("ohpA", highRepsDefault);
            }
            if(mExtraInfo.get("benchPressA") == null){
                mExtraInfo.put("benchPressA", highRepsDefault);
            }
        }

        if(type == 1){
            mainLiftList2.add("Overhead Press (Barbell)");
            mainLiftList2.add("3x" + ohpA);
        }else if(type == 2){
            mainLiftList2.add("Bench Press (Barbell - Flat)");
            mainLiftList2.add("3x" + benchPressA);
        }

        inclineDBList.add(inclineDB);
        inclineDBList.add("3x" + mExtraInfo.get(inclineDB));

        tricepPushdownsList.add(tricepsPushdowns);
        tricepPushdownsList.add("3x" + mExtraInfo.get(tricepsPushdowns));
        tricepPushdownsList.add(lateralRaises);
        tricepPushdownsList.add("3x" + mExtraInfo.get(lateralRaises));

        overheadTricepsExtensionsList.add(overheadTricepsExtensions);
        overheadTricepsExtensionsList.add("3x" + mExtraInfo.get(overheadTricepsExtensions));
        overheadTricepsExtensionsList.add(lateralRaises);
        overheadTricepsExtensionsList.add("3x" + mExtraInfo.get(lateralRaises));

        map.put("1_key", mainLiftList1);
        map.put("2_key", mainLiftList2);
        map.put("3_key", inclineDBList);
        map.put("4_key", tricepPushdownsList);
        map.put("5_key", overheadTricepsExtensionsList);

        return map;
    }

    public HashMap<String, List<String>> getLegsWorkout() {
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> mainLiftList = new ArrayList<>();

        if(squatWeight == null){
            squatWeight = "0";
        }else{
            if(squatWeight.isEmpty()){
                squatWeight = "0";
            }
        }
        mainLiftList.add("Bench Press (Barbell - Flat)");
        if(isWarmup){
            if(!isFirstWeek){
                mainLiftList.addAll(getWarmupList(squatWeight));
            }
        }
        mainLiftList.add("2x5@" + squatWeight);
        mainLiftList.add("1x5_a@" + squatWeight);

        List<String> rdlList = new ArrayList<>();
        List<String> legPressList = new ArrayList<>();
        List<String> legCurlsList = new ArrayList<>();
        List<String> calfRaisesList = new ArrayList<>();
        List<String> absList = new ArrayList<>();

        if(version.equals("normal")){
            if(mExtraInfo.get(rdl) == null){
                mExtraInfo.put(rdl, normalDefault);
            }
            if(mExtraInfo.get(legPress) == null){
                mExtraInfo.put(legPress, normalDefault);
            }
            if(mExtraInfo.get(legCurls) == null){
                mExtraInfo.put(legCurls, normalDefault);
            }
            if(mExtraInfo.get(calfRaises) == null){
                mExtraInfo.put(calfRaises, normalDefault);
            }
            if(mExtraInfo.get(abs2) == null){
                mExtraInfo.put(abs2, normalDefaultBW);
            }

            if(isBodyweight(legCurls)){
                mExtraInfo.put(legCurls, normalDefaultBW);
            }
        }else if(version.equals("strength")){
            if(mExtraInfo.get(rdl) == null){
                mExtraInfo.put(rdl, strengthDefault);
            }
            if(mExtraInfo.get(legPress) == null){
                mExtraInfo.put(legPress, strengthDefault);
            }
            if(mExtraInfo.get(legCurls) == null){
                mExtraInfo.put(legCurls, normalDefault);
            }
            if(mExtraInfo.get(calfRaises) == null){
                mExtraInfo.put(calfRaises, normalDefault);
            }
            if(mExtraInfo.get(abs2) == null){
                mExtraInfo.put(abs2, normalDefaultBW);
            }

            if(isBodyweight(legCurls)){
                mExtraInfo.put(legCurls, normalDefaultBW);
            }
        }else if(version.equals("highReps")){
            if(mExtraInfo.get(rdl) == null){
                mExtraInfo.put(rdl, highRepsDefault);
            }
            if(mExtraInfo.get(legPress) == null){
                mExtraInfo.put(legPress, highRepsDefault);
            }
            if(mExtraInfo.get(legCurls) == null){
                mExtraInfo.put(legCurls, highRepsDefault);
            }
            if(mExtraInfo.get(calfRaises) == null){
                mExtraInfo.put(calfRaises, highRepsDefault);
            }
            if(mExtraInfo.get(abs2) == null){
                mExtraInfo.put(abs2, normalDefaultBW);
            }

            if(isBodyweight(legCurls)){
                mExtraInfo.put(legCurls, normalDefaultBW);
            }
        }

        rdlList.add(rdl);
        rdlList.add("3x" + mExtraInfo.get(rdl));

        legPressList.add(legPress);
        legPressList.add("3x" + mExtraInfo.get(legPress));

        legCurlsList.add(legCurls);
        legCurlsList.add("3x" + mExtraInfo.get(legCurls));


        calfRaisesList.add(calfRaises);
        calfRaisesList.add("5x" + mExtraInfo.get(calfRaises));

        absList.add(abs2);
        absList.add("3x" + mExtraInfo.get(abs2));

        map.put("1_key", mainLiftList);
        map.put("2_key", rdlList);
        map.put("3_key", legPressList);
        map.put("4_key", legCurlsList);
        map.put("5_key", calfRaisesList);
        map.put("6_key", absList);

        return map;
    }

    private List<String> getWarmupList(String weight){
        List<String> warmupList = new ArrayList<>();

        if(!weight.equals("0")){
            warmupList.add("1x10@45");
            warmupList.add("1x10@" + getPercentage("47", weight));
            warmupList.add("1x5@" + getPercentage("67", weight));
            warmupList.add("1x3@" + getPercentage("92", weight));
        }

        return warmupList;
    }

    private String getPercentage(String percent, String weightString){
        String formatted;

        double weight;
        int weight2;

        double percentage = Double.parseDouble(percent)/(double)100;

        weight = Double.parseDouble(weightString) * percentage;

        weight2 = (int) (5 * (Math.round(weight / 5)));

        formatted = String.valueOf(weight2);

        return formatted;
    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }

}

/**
 * When the weight is 0, we could darken everything in the exname frag and below and blink the
 * extra options button.
 *
 * Remember that bodyweight exercises don't have a weight so they don't increase in AS
 *
 * "Including super sets" checkmark in extra options
 */

/*
 * The best way would probably be to not save to default/empty template with extra info, but
 * actually make it somewhat like a regular template in that all the s/r/w stuff are saved, then
 * do extra operations on them in AssistorSaved.
 *
 * Will need a method here that takes in days and a map of what they did and increments what
 * is needed and returns the new map to be updated to fb.
 *
 *
 * We will auto generate some numbers, but let them know that they can edit the weights as
 * they need to. Also need to allow for changing out exercises.
 */