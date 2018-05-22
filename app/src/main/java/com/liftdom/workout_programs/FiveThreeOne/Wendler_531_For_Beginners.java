package com.liftdom.workout_programs.FiveThreeOne;

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

    String squatMax;
    String benchMax;
    String deadliftMax;
    String ohpMax;
    boolean autoDeload;
    String beginDateString;

    double squatTM;
    double benchTM;
    double deadliftTM;
    double ohpTM;

    LocalDate beginDate;
    LocalDate todaysDate;

    public Wendler_531_For_Beginners(HashMap<String, String> extraInfo){
        beginDateString = extraInfo.get("beginDate");
        squatMax = extraInfo.get("squatMax");
        benchMax = extraInfo.get("benchMax");
        deadliftMax = extraInfo.get("deadliftMax");
        ohpMax = extraInfo.get("ohpMax");
        autoDeload = Boolean.parseBoolean(extraInfo.get("autoDeload"));

        beginDate = LocalDate.parse(beginDateString);
        todaysDate = LocalDate.now();
    }

    public boolean isIncreaseDate(){
        boolean increase = false;



        return increase;
    }

    public HashMap<String, List<String>> generateWorkoutMap() {
        HashMap<String, List<String>> map = new HashMap<>();

        if(beginDate.isAfter(todaysDate)){
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            map.put("1_key", workoutList);
        }else{
            int daysBetween = Days.daysBetween(beginDate, todaysDate).getDays();

            double week = daysBetween / 7;

            week++;

            setTrainingMaxes();

            map = getWorkout(week);
        }

        return map;
    }

    private HashMap<String, List<String>> getWorkout(double week){
        HashMap<String, List<String>> workoutMap = new HashMap<>();

        int weekType;

        if(week % 3 == 0){
            weekType = 3;
        }else if(week % 2 == 0){
            weekType = 2;
        }else{
            weekType = 1;
        }

        int dayOfWeek = todaysDate.getDayOfWeek();

        if(dayOfWeek == 1){
            // monday
            workoutMap = getMondayWorkout(weekType);
        }else if(dayOfWeek == 3){
            // wednesday
            workoutMap = getWednesdayWorkout(weekType);
        }else if(dayOfWeek == 5){
            // friday
            workoutMap = getFridayWorkout(weekType);
        }else{
            // rest day
            List<String> workoutList = new ArrayList<>();
            workoutList.add("Bench Press");
            workoutList.add("rest");
            workoutMap.put("1_key", workoutList);
        }

        return workoutMap;
    }

    /**
     Before any lifting, choose one of the following and do 10-15 total reps over 2-3 sets:
     Box Jumps
     Broad Jumps
     Medicine Ball Throws

     For your main lifts, you can warm up with the following protocol:
     1x5 @ 40%
     1x5 @ 50%
     1x3 @ 60%
     */

    private HashMap<String, List<String>> getMondayWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
         Squats: 5/3/1 sets/reps, then 5x5 FSL
         Bench Press: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

        List<String> warmupList = new ArrayList<>();
        warmupList.add("Box Jumps");
        warmupList.add("3x4@B.W.");

        // squat
        List<String> squatList = new ArrayList<>();
        squatList.add("Squat (Barbell - Back)");
        squatList.add("1x5@p_40_a_" + squatTM);
        squatList.add("1x5@p_50_a_" + squatTM);
        squatList.add("1x3@p_60_a_" + squatTM);
        // done with warmup
        if(weekType == 1){
            squatList.add("1x5@p_65_a_" + squatTM);
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1xT.F.@p_85_a_" + squatTM); // T.F. but goal is 5
            squatList.add("5x5@p_65_a_" + squatTM);
        }else if(weekType == 2){
            squatList.add("1x3@p_70_a_" + squatTM);
            squatList.add("1x3@p_80_a_" + squatTM);
            squatList.add("1xT.F.@p_90_a_" + squatTM); // T.F. but goal is 3
            squatList.add("5x5@p_70_a_" + squatTM);
        }else if(weekType == 3){
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x3@p_85_a_" + squatTM);
            squatList.add("1xT.F.@p_95_a_" + squatTM); // T.F. but goal is 1
            squatList.add("5x5@p_75_a_" + squatTM);
        }

        // bench
        List<String> benchList = new ArrayList<>();
        benchList.add("Bench Press (Barbell - Flat)");
        benchList.add("1x5@p_40_a_" + benchTM);
        benchList.add("1x5@p_50_a_" + benchTM);
        benchList.add("1x3@p_60_a_" + benchTM);
        // done with warmup
        if(weekType == 1){
            benchList.add("1x5@p_65_a_" + benchTM);
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1xT.F.@p_85_a_" + benchTM); // T.F. but goal is 5
            benchList.add("5x5@p_65_a_" + benchTM);
        }else if(weekType == 2){
            benchList.add("1x3@p_70_a_" + benchTM);
            benchList.add("1x3@p_80_a_" + benchTM);
            benchList.add("1xT.F.@p_90_a_" + benchTM); // T.F. but goal is 3
            benchList.add("5x5@p_70_a_" + benchTM);
        }else if(weekType == 3){
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x3@p_85_a_" + benchTM);
            benchList.add("1xT.F.@p_95_a_" + benchTM); // T.F. but goal is 1
            benchList.add("5x5@p_75_a_" + benchTM);
        }

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", squatList);
        map.put("3_key", benchList);

        return map;
    }

    private HashMap<String, List<String>> getWednesdayWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
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
        deadliftList.add("1x5@p_40_a_" + deadliftTM);
        deadliftList.add("1x5@p_50_a_" + deadliftTM);
        deadliftList.add("1x3@p_60_a_" + deadliftTM);
        // done with warmup
        if(weekType == 1){
            deadliftList.add("1x5@p_65_a_" + deadliftTM);
            deadliftList.add("1x5@p_75_a_" + deadliftTM);
            deadliftList.add("1xT.F.@p_85_a_" + deadliftTM); // T.F. but goal is 5
            deadliftList.add("5x5@p_65_a_" + deadliftTM);
        }else if(weekType == 2){
            deadliftList.add("1x3@p_70_a_" + deadliftTM);
            deadliftList.add("1x3@p_80_a_" + deadliftTM);
            deadliftList.add("1xT.F.@p_90_a_" + deadliftTM); // T.F. but goal is 3
            deadliftList.add("5x5@p_70_a_" + deadliftTM);
        }else if(weekType == 3){
            deadliftList.add("1x5@p_75_a_" + deadliftTM);
            deadliftList.add("1x3@p_85_a_" + deadliftTM);
            deadliftList.add("1xT.F.@p_95_a_" + deadliftTM); // T.F. but goal is 1
            deadliftList.add("5x5@p_75_a_" + deadliftTM);
        }

        // ohp
        List<String> ohpList = new ArrayList<>();
        ohpList.add("Overhead Press (Barbell)");
        ohpList.add("1x5@p_40_a_" + ohpTM);
        ohpList.add("1x5@p_50_a_" + ohpTM);
        ohpList.add("1x3@p_60_a_" + ohpTM);
        // done with warmup
        if(weekType == 1){
            ohpList.add("1x5@p_65_a_" + ohpTM);
            ohpList.add("1x5@p_75_a_" + ohpTM);
            ohpList.add("1xT.F.@p_85_a_" + ohpTM); // T.F. but goal is 5
            ohpList.add("5x5@p_65_a_" + ohpTM);
        }else if(weekType == 2){
            ohpList.add("1x3@p_70_a_" + ohpTM);
            ohpList.add("1x3@p_80_a_" + ohpTM);
            ohpList.add("1xT.F.@p_90_a_" + ohpTM); // T.F. but goal is 3
            ohpList.add("5x5@p_70_a_" + ohpTM);
        }else if(weekType == 3){
            ohpList.add("1x5@p_75_a_" + ohpTM);
            ohpList.add("1x3@p_85_a_" + ohpTM);
            ohpList.add("1xT.F.@p_95_a_" + ohpTM); // T.F. but goal is 1
            ohpList.add("5x5@p_75_a_" + ohpTM);
        }

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", deadliftList);
        map.put("3_key", ohpList);

        return map;
    }

    private HashMap<String, List<String>> getFridayWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
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
        benchList.add("1x5@p_40_a_" + benchTM);
        benchList.add("1x5@p_50_a_" + benchTM);
        benchList.add("1x3@p_60_a_" + benchTM);
        // done with warmup
        if(weekType == 1){
            benchList.add("1x5@p_65_a_" + benchTM);
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1xT.F.@p_85_a_" + benchTM); // T.F. but goal is 5
            benchList.add("5x5@p_65_a_" + benchTM);
        }else if(weekType == 2){
            benchList.add("1x3@p_70_a_" + benchTM);
            benchList.add("1x3@p_80_a_" + benchTM);
            benchList.add("1xT.F.@p_90_a_" + benchTM); // T.F. but goal is 3
            benchList.add("5x5@p_70_a_" + benchTM);
        }else if(weekType == 3){
            benchList.add("1x5@p_75_a_" + benchTM);
            benchList.add("1x3@p_85_a_" + benchTM);
            benchList.add("1xT.F.@p_95_a_" + benchTM); // T.F. but goal is 1
            benchList.add("5x5@p_75_a_" + benchTM);
        }

        // squat
        List<String> squatList = new ArrayList<>();
        squatList.add("Squat (Barbell - Back)");
        squatList.add("1x5@p_40_a_" + squatTM);
        squatList.add("1x5@p_50_a_" + squatTM);
        squatList.add("1x3@p_60_a_" + squatTM);
        // done with warmup
        if(weekType == 1){
            squatList.add("1x5@p_65_a_" + squatTM);
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1xT.F.@p_85_a_" + squatTM); // T.F. but goal is 5
            squatList.add("5x5@p_65_a_" + squatTM);
        }else if(weekType == 2){
            squatList.add("1x3@p_70_a_" + squatTM);
            squatList.add("1x3@p_80_a_" + squatTM);
            squatList.add("1xT.F.@p_90_a_" + squatTM); // T.F. but goal is 3
            squatList.add("5x5@p_70_a_" + squatTM);
        }else if(weekType == 3){
            squatList.add("1x5@p_75_a_" + squatTM);
            squatList.add("1x3@p_85_a_" + squatTM);
            squatList.add("1xT.F.@p_95_a_" + squatTM); // T.F. but goal is 1
            squatList.add("5x5@p_75_a_" + squatTM);
        }

        // assistance

        map.put("1_key", warmupList);
        map.put("2_key", benchList);
        map.put("3_key", squatList);

        return map;
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
