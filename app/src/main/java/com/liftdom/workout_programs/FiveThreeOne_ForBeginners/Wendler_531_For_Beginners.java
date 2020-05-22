package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;

import com.liftdom.helper_classes.ConvertWithUnits;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/18/2018.
 */
public class Wendler_531_For_Beginners {

    private String squatMax;
    private String benchMax;
    private String deadliftMax;
    private String ohpMax;
    private String pushSetScheme;
    private String pullSetScheme;
    private String legCoreSetScheme;
    private String lastUsedPush;
    private String lastUsedPull;
    private String lastUsedLegCore;

    private String beginDateString;
    private boolean isRoundToNearest5;

    private boolean isTMIncreaseWeek;
    private boolean isSpecialWeek;
    private boolean isWarmup;

    private HashMap<String, String> mExtraInfo;

    private double squatTM;
    private double benchTM;
    private double deadliftTM;
    private double ohpTM;

    private String whichDay;

    private LocalDate beginDate;
    private LocalDate todaysDate;

    boolean isTemplateImperial;
    boolean isUserImperial;

    public HashMap<String, String> exercisesAndTMs = new HashMap<>();

    public Wendler_531_For_Beginners(HashMap<String, String> extraInfo){

        mExtraInfo = extraInfo;

        setValues(extraInfo);

        beginDate = LocalDate.parse(beginDateString);
        todaysDate = LocalDate.now();
    }

    private void setValues(HashMap<String, String> extraInfo){
        beginDateString = extraInfo.get("beginDate");
        squatMax = extraInfo.get("squatMax");
        benchMax = extraInfo.get("benchMax");
        deadliftMax = extraInfo.get("deadliftMax");
        ohpMax = extraInfo.get("ohpMax");
        pushSetScheme = extraInfo.get("pushSetScheme");
        pullSetScheme = extraInfo.get("pullSetScheme");
        legCoreSetScheme = extraInfo.get("legCoreSetScheme");
        isWarmup = Boolean.parseBoolean(extraInfo.get("isWarmup"));
        if(extraInfo.get("lastUsedPush") != null){
            lastUsedPush = extraInfo.get("lastUsedPush");
        }
        if(extraInfo.get("lastUsedPull") != null){
            lastUsedPull = extraInfo.get("lastUsedPull");
        }
        if(extraInfo.get("lastUsedLegCore") != null){
            lastUsedLegCore = extraInfo.get("lastUsedLegCore");
        }
    }

    public HashMap<String, List<String>> generateWorkoutMap() {
        HashMap<String, List<String>> map = new HashMap<>();

        if(beginDate.isAfter(todaysDate)){
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            map.put("1_key", workoutList);
        }else{

            //Days: 1 3 5

            int daysBetween = Days.daysBetween(beginDate, todaysDate).getDays();

            int week = daysBetween / 7;
            int days = daysBetween % 7;

            week++;
            days++;

            setTrainingMaxes();

            int weekType = getWeekType(week);

            if(weekType == 0){
                // get special workout with the days var
                map = getWorkoutSpecial(days);
            }else{
                map = getWorkout(weekType, days);
            }

        }

        return map;
    }

    public HashMap<String, List<String>> generateSpecificWorkoutMap(int week, int day){
        HashMap<String, List<String>> map = new HashMap<>();

        setTrainingMaxes();

        int weekType = getWeekType(week);

        if(weekType == 0){
            // get special workout with the days var
            map = getWorkoutSpecial(day);
        }else{
            map = getWorkout(weekType, day);
        }

        return map;
    }

    public HashMap<String, List<String>> generateSpecificWithDates(String specificDateString,
                                                                   boolean isTempImperial,
                                                                   boolean isCurrentImperial){
        isTemplateImperial = isTempImperial;
        isUserImperial = isCurrentImperial;

        HashMap<String, List<String>> map = new HashMap<>();

        LocalDate specificDate = LocalDate.parse(specificDateString);

        int daysBetween = Days.daysBetween(beginDate, specificDate).getDays();

        int week = daysBetween / 7;
        int days = daysBetween % 7;

        week++;
        days++;

        // Here we need to increase our maxes by the correct amounts

        processMaxIncreasesForWeeks(week);

        List<String> dummyList = new ArrayList<>();
        dummyList.add("Monday");
        map.put("0_key", dummyList);
        map.putAll(generateSpecificWorkoutMap(week, days));

        return map;
    }

    private void processMaxIncreasesForWeeks(int week){
        int mod = week % 10;
        int firstDigit;
        if(week > 9){
            firstDigit = Integer.parseInt(Integer.toString(week).substring(0, 1));
        }else{
            firstDigit = 0;
        }

        firstDigit = firstDigit * 3;

        convertMaxesUnits();

        increaseMaxesBy(firstDigit);

        int amount;

        if(mod == 4 || mod == 5 || mod == 6){
            amount = 1;
        }else if(mod == 7 || mod == 8 || mod == 9){
            amount = 2;
        }else{
            amount = 0;
        }

        increaseMaxesBy(amount);
    }

    private void convertMaxesUnits(){
        ConvertWithUnits squatConvert = new ConvertWithUnits(squatMax, isTemplateImperial,
                isUserImperial, true);
        ConvertWithUnits benchConvert = new ConvertWithUnits(benchMax, isTemplateImperial,
                isUserImperial, true);
        ConvertWithUnits deadliftConvert = new ConvertWithUnits(deadliftMax, isTemplateImperial,
                isUserImperial, true);
        ConvertWithUnits ohpConvert = new ConvertWithUnits(ohpMax, isTemplateImperial,
                isUserImperial, true);

        squatMax = String.valueOf(squatConvert.getNumber());
        benchMax = String.valueOf(benchConvert.getNumber());
        deadliftMax = String.valueOf(deadliftConvert.getNumber());
        ohpMax = String.valueOf(ohpConvert.getNumber());
    }

    private void increaseMaxesBy(int amount){
        int xLbs = 5;
        int yLbs = 10;
        double xKgs = 2.5;
        double yKgs = 5;

        if(isUserImperial){
            squatMax = String.valueOf(Double.parseDouble(squatMax) + (amount * yLbs));
            deadliftMax = String.valueOf(Double.parseDouble(deadliftMax) + (amount * yLbs));
            benchMax = String.valueOf(Double.parseDouble(benchMax) + (amount * xLbs));
            ohpMax = String.valueOf(Double.parseDouble(ohpMax) + (amount * xLbs));
        }else{
            squatMax = String.valueOf(Double.parseDouble(squatMax) + (amount * yKgs));
            deadliftMax = String.valueOf(Double.parseDouble(deadliftMax) + (amount * yKgs));
            benchMax = String.valueOf(Double.parseDouble(benchMax) + (amount * xKgs));
            ohpMax = String.valueOf(Double.parseDouble(ohpMax) + (amount * xKgs));
        }
    }

    public ArrayList<LocalDate> getW531fBDates(){
        ArrayList<LocalDate> w531fBDates = new ArrayList<>();

        /**
         * How do we get all the dates and then how will we know how to update the weights?
         * Ofc we'll be doing ideal updated weights (so as if they hit all their reps).
         * All we'd really need is to know the begin date in the specific day generator and
         * then we can subtract days and figure out the weeks (and we know 3 weeks increase, but
         * also have to account for 10th week...may be same as when we normally generate, where
         * it's based on the last number.)
         *
         * So for what we do here specifically:
         * We need to iterate through dates from now until the end of the year.
         * Each day we'll check to see what the workout is and if it's not a rest day, we add
         * that day to the list.
         */

        DateTime endOfYear = new DateTime("2020-12-31");

        int daysBetween = Days.daysBetween(beginDate, endOfYear.toLocalDate()).getDays();

        for(int i = 0; i < daysBetween; i++){
            LocalDate newDate = beginDate.plusDays(i);

            int daysBetween2 = Days.daysBetween(beginDate, newDate).getDays();

            int week = daysBetween / 7;
            int days = daysBetween % 7;

            week++;
            days++;

            HashMap<String, List<String>> workoutMap = generateSpecificWorkoutMap(week, days);

            if(!workoutMap.get("1_key").get(1).equals("rest")){
                //if(week == 1 && days == 1){
                //    w531fBDates.add(newDate);
                //}else{
                if(newDate.isAfter(LocalDate.now())){
                    w531fBDates.add(newDate);
                }
                //}
            }
        }

        return w531fBDates;
    }

    /*
     * What do we need to do here?
     *
     * We need to alert AS that we have reached the 3rd week and to increase the TMs accordingly.
     * We can do this like how we alert AS in Smolov (setOneRepMaxDay())
     * After 9 weeks/on the 10th week, special workout for each day. On the last set, you enter in
     * how many reps you did.
     * If >= 3: TM doesn't increase (because we already increased it at
     *  the end of the last 3 week block).
     * If it's 2: decrease by normal increase amount.
     * If it's 1: decrease by normal increase amount x 2.
     *
     * So we'll need to send that info from AH to AS and then process it knowing that it's a
     * special week.
     *
     * Special week:
     * 5 reps at 70%
     * 5 reps at 80%
     * 3 reps at at 90%
     * 3-5 reps at 100%
     * Day 1 (Monday): OHP TM Test first, then Squat TM Test
     * Day 2 (Wednesday): Bench TM Test
     * Day 3 (Friday): Deadlift TM Test
     *
     * SO what do we really need?
     * 1. Know when it's TM increase time. (Any week that is a multiple of 3)
     * 2. Know when it's special week. (Any week that is a multiple of 10)
     * Actually, it's not multiples/math based. Well, it is.
     * But what we have to do is take the last digit on the right,
     *  and then compare it to a list to get the right workout.
     *
     *  If the last right digit is:
     *  1,4,7 - it is the 1st week
     *  2,5,8 - it is the 2nd week
     *  3,6,9 - it is the 3rd week (isTMIncrease)
     *  0 - it is Special Week (isSpecialWeek)
     *
     * "It says 3+, which is the goal, but enter the amount of reps you actually did."
     * "Old TM, current TM"
     */

    private int getWeekType(int week){
        int weekType;

        int mod = week % 10;

        if(mod == 1 || mod == 4 || mod == 7){
            weekType = 1;
        }else if(mod == 2 || mod == 5 || mod == 8){
            weekType = 2;
        }else if(mod == 3 || mod == 6 || mod == 9){
            weekType = 3;
            setTMIncreaseWeek(true);
        }else{
            weekType = 0;
            setSpecialWeek(true);
        }

        return weekType;
    }

    private HashMap<String, List<String>> getWorkout(int week, int day){

        HashMap<String, List<String>> workoutMap = new HashMap<>();

        if(day == 1){
            // monday
            workoutMap = getFirstWorkout(week);
            setTMIncreaseWeek(false);
        }else if(day == 3){
            // wednesday
            workoutMap = getSecondWorkout(week);
        }else if(day == 5){
            // friday
            workoutMap = getThirdWorkout(week);
        }else{
            // rest day
            setTMIncreaseWeek(false);
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            workoutMap.put("1_key", workoutList);
        }

        return workoutMap;
    }

    private HashMap<String, List<String>> getWorkoutSpecial(int day){

        HashMap<String, List<String>> workoutMap = new HashMap<>();

        if(day == 1){
            workoutMap = getFirstWorkoutSpecial();
        }else if(day == 3){
            workoutMap = getSecondWorkoutSpecial();
        }else if(day == 5){
            workoutMap = getThirdWorkoutSpecial();
        }else{
            // rest day
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            workoutMap.put("1_key", workoutList);
            setSpecialWeek(false);
        }

        return workoutMap;
    }

    public String getLastUsedAssistance(int type){
        String lastUsed = "null";

        if(type == 1){
            if(lastUsedPush != null){
                lastUsed = lastUsedPush;
            }
        }else if(type == 2){
            if(lastUsedPull != null){
                lastUsed = lastUsedPull;
            }
        }else if(type == 3){
            if(lastUsedLegCore != null){
                lastUsed = lastUsedLegCore;
            }
        }

        return lastUsed;
    }

    /**
     * Current problem:
     * We have to predict or create the correct Special Week 100% weight.
     * Instead of generating it here, we could just "get" it somehow in AH.
     * This is probably preferable bc there are too many possibilities of what happens when it's
     * generated. The only issue would be updating it if they switch units midway.
     *
     * What we could do is send both the generated value and the value in the other unit.
     *
     * 120, 365
     * 55, 165
     *
     */

    public String getPushSetScheme() {
        return pushSetScheme;
    }

    public String getPullSetScheme() {
        return pullSetScheme;
    }

    public String getLegCoreSetScheme() {
        return legCoreSetScheme;
    }

    public String getWhichDay() {
        return whichDay;
    }

    public void setWhichDay(String whichDay) {
        this.whichDay = whichDay;
    }

    public boolean isWarmup() {
        return isWarmup;
    }

    public void setWarmup(boolean warmup) {
        isWarmup = warmup;
    }

    public HashMap<String, String> getExtraInfo() {
        return mExtraInfo;
    }

    private String roundNumberToNearest5(double weight){
        String rounded;

        int weight2;

        weight2 = (int) (5 * (Math.round(weight / 5)));

        rounded = String.valueOf(weight2);

        return rounded;
    }

    /*
     Before any lifting, choose one of the following and do 10-15 total reps over 2-3 sets:
     Box Jumps
     Broad Jumps
     Medicine Ball Throws

     For your main lifts, you can warm up with the following protocol:
     1x5 @ 40%
     1x5 @ 50%
     1x3 @ 60%
     */

    private HashMap<String, List<String>> getFirstWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /*
         Squats: 5/3/1 sets/reps, then 5x5 FSL
         Bench Press: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

        List<String> warmupList = new ArrayList<>();
        warmupList.add("Box Jumps (Bodyweight)");
        warmupList.add("3x4@B.W.");

        // squat
        List<String> squatList = new ArrayList<>();
        squatList.add("Squat (Barbell - Back)");
        if(isWarmup){
            squatList.add("1x5@p_40_a_" + squatTM);
            squatList.add("1x5@p_50_a_" + squatTM);
            squatList.add("1x3@p_60_a_" + squatTM);
        }
        // done with warmup
        if(weekType == 1){
            squatList.add("1x5@p_65_a_" + squatTM);
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x5_a@p_85_a_" + squatTM); // T.F. but goal is 5
            squatList.add("5x5@p_65_a_" + squatTM);
        }else if(weekType == 2){
            squatList.add("1x3@p_70_a_" + squatTM);
            squatList.add("1x3@p_80_a_" + squatTM);
            squatList.add("1x3_a@p_90_a_" + squatTM); // T.F. but goal is 3
            squatList.add("5x5@p_70_a_" + squatTM);
        }else if(weekType == 3){
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x3@p_85_a_" + squatTM);
            squatList.add("1x1_a@p_95_a_" + squatTM); // T.F. but goal is 1
            squatList.add("5x5@p_75_a_" + squatTM);
        }
        //exercisesAndTMs.put("Squat (Barbell - Back)", String.valueOf(squatTM));

        // bench
        List<String> benchList = new ArrayList<>();
        benchList.add("Bench Press (Barbell - Flat)");
        if(isWarmup){
            benchList.add("1x5@p_40_a_" + benchTM);
            benchList.add("1x5@p_50_a_" + benchTM);
            benchList.add("1x3@p_60_a_" + benchTM);
        }
        // done with warmup
        if(weekType == 1){
            benchList.add("1x5@p_65_a_" + benchTM);
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x5_a@p_85_a_" + benchTM); // T.F. but goal is 5
            benchList.add("5x5@p_65_a_" + benchTM);
        }else if(weekType == 2){
            benchList.add("1x3@p_70_a_" + benchTM);
            benchList.add("1x3@p_80_a_" + benchTM);
            benchList.add("1x3_a@p_90_a_" + benchTM); // T.F. but goal is 3
            benchList.add("5x5@p_70_a_" + benchTM);
        }else if(weekType == 3){
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x3@p_85_a_" + benchTM);
            benchList.add("1x1_a@p_95_a_" + benchTM); // T.F. but goal is 1
            benchList.add("5x5@p_75_a_" + benchTM);
        }
        //exercisesAndTMs.put("Bench Press (Barbell - Flat)", String.valueOf(benchTM));

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", squatList);
        map.put("3_key", benchList);

        return map;
    }

    private HashMap<String, List<String>> getFirstWorkoutSpecial(){
        HashMap<String, List<String>> map = new HashMap<>();

        // ohp
        List<String> ohpList = new ArrayList<>();
        ohpList.add("Overhead Press (Barbell)");
        ohpList.add("1x5@p_70_a_" + ohpTM);
        ohpList.add("1x5@p_80_a_" + ohpTM);
        ohpList.add("1x3@p_90_a_" + ohpTM);
        ohpList.add("1x3_a@p_100_a_" + ohpTM);
        //exercisesAndTMs.put("Overhead Press (Barbell)", String.valueOf(ohpTM));

        // squat
        List<String> squatList = new ArrayList<>();
        squatList.add("Squat (Barbell - Back)");
        squatList.add("1x5@p_70_a_" + squatTM);
        squatList.add("1x5@p_80_a_" + squatTM);
        squatList.add("1x3@p_90_a_" + squatTM);
        squatList.add("1x3_a@p_100_a_" + squatTM);
        //exercisesAndTMs.put("Squat (Barbell - Back)", String.valueOf(squatTM));

        setWhichDay("first");

        map.put("1_key", ohpList);
        map.put("2_key", squatList);

        return map;
    }

    private HashMap<String, List<String>> getSecondWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /*
         Deadlift: 5/3/1 sets/reps, then 5x5 FSL
         Overhead Press: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

        List<String> warmupList = new ArrayList<>();
        warmupList.add("Broad Jumps");
        warmupList.add("3x4@B.W.");

        // deadlift
        List<String> deadliftList = new ArrayList<>();
        deadliftList.add("Deadlift (Barbell - Conventional)");
        if(isWarmup){
            deadliftList.add("1x5@p_40_a_" + deadliftTM);
            deadliftList.add("1x5@p_50_a_" + deadliftTM);
            deadliftList.add("1x3@p_60_a_" + deadliftTM);
        }
        // done with warmup
        if(weekType == 1){
            deadliftList.add("1x5@p_65_a_" + deadliftTM);
            deadliftList.add("1x5@p_75_a_" + deadliftTM);
            deadliftList.add("1x5_a@p_85_a_" + deadliftTM); // T.F. but goal is 5
            deadliftList.add("5x5@p_65_a_" + deadliftTM);
        }else if(weekType == 2){
            deadliftList.add("1x3@p_70_a_" + deadliftTM);
            deadliftList.add("1x3@p_80_a_" + deadliftTM);
            deadliftList.add("1x3_a@p_90_a_" + deadliftTM); // T.F. but goal is 3
            deadliftList.add("5x5@p_70_a_" + deadliftTM);
        }else if(weekType == 3){
            deadliftList.add("1x5@p_75_a_" + deadliftTM);
            deadliftList.add("1x3@p_85_a_" + deadliftTM);
            deadliftList.add("1x1_a@p_95_a_" + deadliftTM); // T.F. but goal is 1
            deadliftList.add("5x5@p_75_a_" + deadliftTM);
        }
        //exercisesAndTMs.put("Deadlift (Barbell - Conventional)", String.valueOf(deadliftTM));

        // ohp
        List<String> ohpList = new ArrayList<>();
        ohpList.add("Overhead Press (Barbell)");
        if(isWarmup){
            ohpList.add("1x5@p_40_a_" + ohpTM);
            ohpList.add("1x5@p_50_a_" + ohpTM);
            ohpList.add("1x3@p_60_a_" + ohpTM);
        }
        // done with warmup
        if(weekType == 1){
            ohpList.add("1x5@p_65_a_" + ohpTM);
            ohpList.add("1x5@p_75_a_" + ohpTM);
            ohpList.add("1x5_a@p_85_a_" + ohpTM); // T.F. but goal is 5
            ohpList.add("5x5@p_65_a_" + ohpTM);
        }else if(weekType == 2){
            ohpList.add("1x3@p_70_a_" + ohpTM);
            ohpList.add("1x3@p_80_a_" + ohpTM);
            ohpList.add("1x3_a@p_90_a_" + ohpTM); // T.F. but goal is 3
            ohpList.add("5x5@p_70_a_" + ohpTM);
        }else if(weekType == 3){
            ohpList.add("1x5@p_75_a_" + ohpTM);
            ohpList.add("1x3@p_85_a_" + ohpTM);
            ohpList.add("1x1_a@p_95_a_" + ohpTM); // T.F. but goal is 1
            ohpList.add("5x5@p_75_a_" + ohpTM);
        }
        //exercisesAndTMs.put("Overhead Press (Barbell)", String.valueOf(ohpTM));

        setWhichDay("second");

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", deadliftList);
        map.put("3_key", ohpList);

        return map;
    }

    private HashMap<String, List<String>> getSecondWorkoutSpecial(){
        HashMap<String, List<String>> map = new HashMap<>();

        // bench
        List<String> benchList = new ArrayList<>();
        benchList.add("Bench Press (Barbell - Flat)");
        benchList.add("1x5@p_70_a_" + benchTM);
        benchList.add("1x5@p_80_a_" + benchTM);
        benchList.add("1x3@p_90_a_" + benchTM);
        benchList.add("1x3_a@p_100_a_" + benchTM);
        //exercisesAndTMs.put("Bench Press (Barbell - Flat)", String.valueOf(benchTM));

        setWhichDay("second");

        map.put("1_key", benchList);

        return map;
    }

    private HashMap<String, List<String>> getThirdWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /*
         Bench Press: 5/3/1 sets/reps, then 5x5 FSL
         Squats: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

        List<String> warmupList = new ArrayList<>();
        warmupList.add("Medicine Ball Throws");
        warmupList.add("3x4@15");

        // bench
        List<String> benchList = new ArrayList<>();
        benchList.add("Bench Press (Barbell - Flat)");
        if(isWarmup){
            benchList.add("1x5@p_40_a_" + benchTM);
            benchList.add("1x5@p_50_a_" + benchTM);
            benchList.add("1x3@p_60_a_" + benchTM);
        }
        // done with warmup
        if(weekType == 1){
            benchList.add("1x5@p_65_a_" + benchTM);
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x5_a@p_85_a_" + benchTM); // T.F. but goal is 5
            benchList.add("5x5@p_65_a_" + benchTM);
        }else if(weekType == 2){
            benchList.add("1x3@p_70_a_" + benchTM);
            benchList.add("1x3@p_80_a_" + benchTM);
            benchList.add("1x3_a@p_90_a_" + benchTM); // T.F. but goal is 3
            benchList.add("5x5@p_70_a_" + benchTM);
        }else if(weekType == 3){
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x3@p_85_a_" + benchTM);
            benchList.add("1x1_a@p_95_a_" + benchTM); // T.F. but goal is 1
            benchList.add("5x5@p_75_a_" + benchTM);
        }
        //exercisesAndTMs.put("Bench Press (Barbell - Flat)", String.valueOf(benchTM));

        // squat
        List<String> squatList = new ArrayList<>();
        squatList.add("Squat (Barbell - Back)");
        if(isWarmup){
            squatList.add("1x5@p_40_a_" + squatTM);
            squatList.add("1x5@p_50_a_" + squatTM);
            squatList.add("1x3@p_60_a_" + squatTM);
        }
        // done with warmup
        if(weekType == 1){
            squatList.add("1x5@p_65_a_" + squatTM);
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x5_a@p_85_a_" + squatTM); // T.F. but goal is 5
            squatList.add("5x5@p_65_a_" + squatTM);
        }else if(weekType == 2){
            squatList.add("1x3@p_70_a_" + squatTM);
            squatList.add("1x3@p_80_a_" + squatTM);
            squatList.add("1x3_a@p_90_a_" + squatTM); // T.F. but goal is 3
            squatList.add("5x5@p_70_a_" + squatTM);
        }else if(weekType == 3){
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x3@p_85_a_" + squatTM);
            squatList.add("1x1_a@p_95_a_" + squatTM); // T.F. but goal is 1
            squatList.add("5x5@p_75_a_" + squatTM);
        }
        //exercisesAndTMs.put("Squat (Barbell - Back)", String.valueOf(squatTM));

        setWhichDay("third");

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", benchList);
        map.put("3_key", squatList);

        return map;
    }

    private HashMap<String, List<String>> getThirdWorkoutSpecial(){
        HashMap<String, List<String>> map = new HashMap<>();

        // bench
        List<String> deadliftList = new ArrayList<>();
        deadliftList.add("Deadlift (Barbell - Conventional)");
        deadliftList.add("1x5@p_70_a_" + deadliftTM);
        deadliftList.add("1x5@p_80_a_" + deadliftTM);
        deadliftList.add("1x3@p_90_a_" + deadliftTM);
        deadliftList.add("1x3_a@p_100_a_" + deadliftTM);
        //exercisesAndTMs.put("Deadlift (Barbell - Conventional)", String.valueOf(deadliftTM));

        setWhichDay("third");

        map.put("1_key", deadliftList);

        return map;
    }

    public boolean isTMIncreaseWeek() {
        return isTMIncreaseWeek;
    }

    public void setTMIncreaseWeek(boolean TMIncreaseWeek) {
        isTMIncreaseWeek = TMIncreaseWeek;
    }

    public boolean isSpecialWeek() {
        return isSpecialWeek;
    }

    public void setSpecialWeek(boolean specialWeek) {
        isSpecialWeek = specialWeek;
    }

    private void setTrainingMaxes(){
        squatTM = round((Double.parseDouble(squatMax) * 0.9), 2);
        benchTM = round((Double.parseDouble(benchMax) * 0.9), 2);
        deadliftTM = round((Double.parseDouble(deadliftMax) * 0.9), 2);
        ohpTM = round((Double.parseDouble(ohpMax) * 0.9), 2);
    }

    private double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
