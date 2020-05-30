package com.liftdom.workout_programs.PPL_Reddit;

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
    private String barbellCalfRaises;
    private String dips;
    private String abs1;
    private String abs2;
    private String rdl;

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
        barbellCalfRaises = extraInfo.get("barbellCalfRaises");
        dips = extraInfo.get("dips");
        abs1 = extraInfo.get("abs1");
        abs2 = extraInfo.get("abs2");
        rdl = extraInfo.get("rdl");
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

    String normalDefault = "8@0";
    String strengthDefault = "4@0";
    String highRepsDefault = "15@0";
    String normalDefaultBW = "8@B.W.";
    String strengthDefaultBW = "4@B.W.";
    String highRepsDefaultBW = "15@B.W.";

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

    public HashMap<String, List<String>> getPull(int type) {
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