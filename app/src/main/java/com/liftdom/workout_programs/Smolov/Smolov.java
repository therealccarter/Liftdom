package com.liftdom.workout_programs.Smolov;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Chris on 2/8/2017.
 */

public class Smolov {

    double oneRM;
    String exerciseName;
    boolean isOneRepMaxDay = false;
    boolean isLastDay = false;
    String weekDayString = "";
    boolean isTakeOff10;
    boolean isRoundToNearest5;

    public Smolov(String exName, String max){
        exerciseName = exName;
        oneRM = Double.parseDouble(max);
    }

    public HashMap<String, List<String>> generateSmolovWorkoutMap(String beginDateString,
                                                                  boolean round){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
         * So we'd have to get the begin date and today's date.
         * From there we'd calculate how many days have passed.
         * We can then set the week and day numbers accordingly
         */

        LocalDate beginDate = LocalDate.parse(beginDateString);
        LocalDate todaysDate = LocalDate.now();

        if(beginDate.isAfter(todaysDate)){
            List<String> workoutList = new ArrayList<>();
            workoutList.add(exerciseName);
            workoutList.add("rest");
            map.put("1_key", workoutList);
        }else{
            int daysBetween = Days.daysBetween(beginDate, todaysDate).getDays();

            int week = daysBetween / 7;
            int days = daysBetween % 7;

            week++;
            days++;

            if(week == 13 && days == 4){
                String normal = "Week " + week + ", day " + days + " of Smolov";
                weekDayString = "Smolov Completed!\n" + normal;
            }else{
                weekDayString = "Week " + week + ", day " + days + " of Smolov";
            }


            List<String> workoutList = getWorkout(week, days, round);

            map.put("1_key", workoutList);

        }

        return map;
    }

    public boolean isTakeOff10() {
        return isTakeOff10;
    }

    public void setTakeOff10(boolean takeOff10) {
        isTakeOff10 = takeOff10;
    }

    public boolean isRoundToNearest5() {
        return isRoundToNearest5;
    }

    public void setRoundToNearest5(boolean roundToNearest5) {
        isRoundToNearest5 = roundToNearest5;
    }

    public HashMap<String, List<String>> generateSpecific(int week, int day, boolean round){
        HashMap<String, List<String>> map = new HashMap<>();

        List<String> workoutList = getWorkout(week, day, round);

        map.put("1_key", workoutList);

        return map;
    }

    public ArrayList<LocalDate> getSmolovDates(String beginDateString, boolean round){
        ArrayList<LocalDate> smolovDates = new ArrayList<>();

        LocalDate beginDate = LocalDate.parse(beginDateString);

        for(int i = 0; i < 100; i++){
            LocalDate newDate = beginDate.plusDays(i);
            int daysBetween = Days.daysBetween(beginDate, newDate).getDays();

            int week = daysBetween / 7;
            int days = daysBetween % 7;

            week++;
            days++;

            List<String> workoutList = getWorkout(week, days, round);
            if(!workoutList.get(1).equals("rest")){
                if(week == 1 && days == 1){
                    smolovDates.add(newDate);
                }else{
                    if(newDate.isAfter(LocalDate.now())){
                        smolovDates.add(newDate);
                    }
                }
            }
        }


        return smolovDates;
    }

    public HashMap<String, List<String>> getMapForSpecificDay(String beginDateString,
                                                              String specificDateString,
                                                              boolean round){
        HashMap<String, List<String>> map = new HashMap<>();

        LocalDate beginDate = LocalDate.parse(beginDateString);
        LocalDate specificDate = LocalDate.parse(specificDateString);

        int daysBetween = Days.daysBetween(beginDate, specificDate).getDays();

        int week = daysBetween / 7;
        int days = daysBetween % 7;

        week++;
        days++;

        List<String> workoutList = getWorkout(week, days, round);
        List<String> dummyList = new ArrayList<>();
        dummyList.add("Monday");

        map.put("1_key", workoutList);
        map.put("0_key", dummyList);

        return map;
    }

    public String getWeekDayString() {
        return weekDayString;
    }

    public boolean isLastDay() {
        return isLastDay;
    }

    public void setLastDay(boolean lastDay) {
        isLastDay = lastDay;
    }

    public boolean getIsOneRepMaxDay() {
        return isOneRepMaxDay;
    }

    public void setOneRepMaxDay(boolean oneRepMaxDay) {
        isOneRepMaxDay = oneRepMaxDay;
    }

    public List<String> getWorkout(int week, int day, boolean round){
        List<String> workout = new ArrayList<>();

        workout.add(exerciseName);

        if(week == 1 && day == 1){
            String one = "3x8@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70, round);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75, round);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80, round);
            workout.add(four);
        }else if(week == 1 && day == 2){
            String one = "3x8@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70, round);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75, round);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80, round);
            workout.add(four);
        }else if(week == 1 && day == 4){
            String one = "4x5@" + percentToWeight(70, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75, round);
            workout.add(two);
            String three = "2x2@" + percentToWeight(80, round);
            workout.add(three);
            String four = "1x1@" + percentToWeight(90, round);
            workout.add(four);
        }else if(week == 2 && day == 1){
            String one = "1x5@" + percentToWeight(80, round);
            workout.add(one);
        }else if(week == 2 && day == 2){
            String one = "1x5@" + percentToWeight(83, round);
            workout.add(one);
        }else if(week == 2 && day == 4){
            String one = "1x5@" + percentToWeight(85, round);
            workout.add(one);
        }else if(week == 3 && day == 1){
            String one = "4x9@" + percentToWeight(70, round);
            workout.add(one);
        }else if(week == 3 && day == 2){
            String one = "5x7@" + percentToWeight(75, round);
            workout.add(one);
        }else if(week == 3 && day == 4){
            String one = "7x5@" + percentToWeight(80, round);
            workout.add(one);
        }else if(week == 3 && day == 6){
            String one = "10x3@" + percentToWeight(85, round);
            workout.add(one);
        }else if(week == 4 && day == 1){
            String one = "4x9@" + percentToWeight(70, 20, round);
            workout.add(one);
        }else if(week == 4 && day == 2){
            String one = "5x7@" + percentToWeight(75, 20, round);
            workout.add(one);
        }else if(week == 4 && day == 4){
            String one = "7x5@" + percentToWeight(80, 20, round);
            workout.add(one);
        }else if(week == 4 && day == 6){
            String one = "10x3@" + percentToWeight(85, 20, round);
            workout.add(one);
        }else if(week == 5 && day == 1){
            String one = "4x9@" + percentToWeight(70, 30, round);
            workout.add(one);
        }else if(week == 5 && day == 2){
            String one = "5x7@" + percentToWeight(75, 30, round);
            workout.add(one);
        }else if(week == 5 && day == 4){
            String one = "7x5@" + percentToWeight(80, 30, round);
            workout.add(one);
        }else if(week == 5 && day == 6) {
            String one = "10x3@" + percentToWeight(85, 30, round);
            workout.add(one);
        }else if(week == 6 && day == 1){
            String one = "rest";
            workout.add(one);
        }else if(week == 6 && day == 2){
            String one = "rest";
            workout.add(one);
        }else if(week == 6 && day == 4){
            String one = "1x1@" + percentToWeight(0, round);
            workout.add(one);
            setOneRepMaxDay(true);
        }else if(week == 6 && day == 6){
            String one = "1x1@" + percentToWeight(0, round);
            workout.add(one);
            setOneRepMaxDay(true);
        }else if(week == 7 && day == 1){
            workout.clear();
            workout.add("Squat (Negative)"); // ex name
            workout.add("1x1@" + percentToWeight(80, round)); // set scheme
        }else if(week == 7 && day == 3){
            workout.clear();
            workout.add("Power Clean (Barbell)"); // ex name
            workout.add("8x3@" + percentToWeight(50, round)); // set scheme
        }else if(week == 7 && day == 5){
            workout.clear();
            workout.add("Squat (Box)"); // ex name
            workout.add("6x2@" + percentToWeight(60, round)); // set scheme
        }else if(week == 8 && day == 1){
            workout.clear();
            workout.add("Squat (Negative)"); // ex name
            workout.add("1x1@" + percentToWeight(85, round)); // set scheme
        }else if(week == 8 && day == 3){
            workout.clear();
            workout.add("Power Clean (Barbell)"); // ex name
            workout.add("8x3@" + percentToWeight(55, round)); // set scheme
        }else if(week == 8 && day == 5){
            workout.clear();
            workout.add("Squat (Box)"); // ex name
            workout.add("6x2@" + percentToWeight(65, round)); // set scheme
        }else if(week == 9 && day == 1){
            String one = "1x3@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x4@" + percentToWeight(75, round);
            workout.add(two);
            String three = "3x4@" + percentToWeight(85, round);
            workout.add(three);
            String four = "1x5@" + percentToWeight(90, round);
            workout.add(four);
        }else if(week == 9 && day == 2){
            String one = "1x3@" + percentToWeight(60, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70, round);
            workout.add(two);
            String three = "1x4@" + percentToWeight(80, round);
            workout.add(three);
            String four = "1x3@" + percentToWeight(90, round);
            workout.add(four);
            String five = "2x5@" + percentToWeight(85, round);
            workout.add(five);
        }else if(week == 9 && day == 4){
            String one = "1x4@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x4@" + percentToWeight(70, round);
            workout.add(two);
            String three = "5x4@" + percentToWeight(80, round);
            workout.add(three);
        }else if(week == 10 && day == 1){
            String one = "1x4@" + percentToWeight(60, round);
            workout.add(one);
            String two = "1x4@" + percentToWeight(70, round);
            workout.add(two);
            String three = "1x4@" + percentToWeight(80, round);
            workout.add(three);
            String four = "1x3@" + percentToWeight(90, round);
            workout.add(four);
            String five = "2x4@" + percentToWeight(90, round);
            workout.add(five);
        }else if(week == 10 && day == 2){
            String one = "1x3@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75, round);
            workout.add(two);
            String three = "1x3@" + percentToWeight(85, round);
            workout.add(three);
            String four = "3x3@" + percentToWeight(90, round);
            workout.add(four);
            String five = "1x3@" + percentToWeight(95, round);
            workout.add(five);
        }else if(week == 10 && day == 4){
            String one = "1x3@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75, round);
            workout.add(two);
            String three = "1x4@" + percentToWeight(85, round);
            workout.add(three);
            String four = "4x5@" + percentToWeight(90, round);
            workout.add(four);
        }else if(week == 11 && day == 1){
            String one = "1x3@" + percentToWeight(60, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70, round);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80, round);
            workout.add(three);
            String four = "5x5@" + percentToWeight(90, round);
            workout.add(four);
        }else if(week == 11 && day == 2){
            String one = "1x3@" + percentToWeight(60, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70, round);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80, round);
            workout.add(three);
            String four = "2x3@" + percentToWeight(95, round);
            workout.add(four);
        }else if(week == 11 && day == 4){
            String one = "1x3@" + percentToWeight(65, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75, round);
            workout.add(two);
            String three = "1x3@" + percentToWeight(85, round);
            workout.add(three);
            String four = "4x3@" + percentToWeight(95, round);
            workout.add(four);
        }else if(week == 12 && day == 1){
            String one = "1x3@" + percentToWeight(70, round);
            workout.add(one);
            String two = "1x4@" + percentToWeight(80, round);
            workout.add(two);
            String three = "5x5@" + percentToWeight(90, round);
            workout.add(three);
        }else if(week == 12 && day == 2){
            String one = "1x3@" + percentToWeight(70, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80, round);
            workout.add(two);
            String three = "4x3@" + percentToWeight(95, round);
            workout.add(three);
        }else if(week == 12 && day == 4){
            String one = "1x3@" + percentToWeight(75, round);
            workout.add(one);
            String two = "1x4@" + percentToWeight(90, round);
            workout.add(two);
            String three = "3x4@" + percentToWeight(80, round);
            workout.add(three);
        }else if(week == 13 && day == 1){
            String one = "1x3@" + percentToWeight(70, round);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80, round);
            workout.add(two);
            String three = "2x5@" + percentToWeight(90, round);
            workout.add(three);
            String four = "3x4@" + percentToWeight(95, round);
            workout.add(four);
        }else if(week == 13 && day == 2){
            String one = "1x4@" + percentToWeight(75, round);
            workout.add(one);
            String two = "4x4@" + percentToWeight(85, round);
            workout.add(two);
        }else if(week == 13 && day == 4){
            String one = "1x1@" + percentToWeight(0, round);
            workout.add(one);
            setOneRepMaxDay(true);
            setLastDay(true);
        }else{
            //need to alert that it's the end.
            String one = "rest";
            workout.add(one);
        }

        return workout;
    }

    int percentToWeight(int percent, boolean round){
        double weight;
        int weight2;

        if(percent == 0){
            weight2 = 0;
        }else{
            double percentage = (double)percent/(double)100;

            weight = oneRM * percentage;

            if(round){
                weight = 5 * (Math.round(weight/5));
            }

            weight2 = (int) Math.round(weight);

        }


        return weight2;
    }

    int percentToWeight(int percent, int addExtra, boolean round){
        double weight;
        //int weight2;
        double percentage = (double)percent/(double)100;

        weight = oneRM * percentage;

        if(round){
            weight = 5 * (Math.round(weight/5));
        }

        int weight2 = (int) Math.round(weight) + addExtra;

        //weight = weight + addExtra;

        return weight2;
    }

}


