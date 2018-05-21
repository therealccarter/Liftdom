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

        int weekType = 0;

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

        return map;
    }

    private HashMap<String, List<String>> getWednesdayWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
         Deadlift: 5/3/1 sets/reps, then 5x5 FSL
         Overhead Press: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

        return map;
    }

    private HashMap<String, List<String>> getFridayWorkout(int weekType){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
         Bench Press: 5/3/1 sets/reps, then 5x5 FSL
         Squats: 5/3/1 sets/reps, then 5x5 FSL
         Assistance Work
         */

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
