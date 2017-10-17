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

    double OneRM;

    public Smolov(){

    }

    public HashMap<String, List<String>> generateSmolovWorkoutMap(String beginDateString, String max){
        HashMap<String, List<String>> map = new HashMap<>();

        /**
         * So we'd have to get the begin date and today's date.
         * From there we'd calculate how many days have passed.
         * We can then set the week and day numbers accordingly
         */

        LocalDate beginDate = LocalDate.parse(beginDateString);
        LocalDate todaysDate = LocalDate.now();

        int daysBetween = Days.daysBetween(beginDate, todaysDate).getDays();



        return map;
    }

    public List<String> getWorkout(int week, int day){
        List<String> workout = new ArrayList<>();

        if(week == 1 && day == 1){
            String one = "3x8@" + percentToWeight(65);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80);
            workout.add(four);
        }else if(week == 1 && day == 2){
            String one = "3x8@" + percentToWeight(65);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80);
            workout.add(four);
        }else if(week == 1 && day == 3){
            String one = "4x5@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "2x2@" + percentToWeight(80);
            workout.add(three);
            String four = "1x1@" + percentToWeight(90);
            workout.add(four);
        }else if(week == 2 && day == 1){
            String one = "1x5@" + percentToWeight(80);
            workout.add(one);
        }else if(week == 2 && day == 2){
            String one = "1x5@" + percentToWeight(83);
            workout.add(one);
        }else if(week == 2 && day == 3){
            String one = "1x5@" + percentToWeight(85);
            workout.add(one);
        }else if(week == 3 && day == 1){
            String one = "4x9@" + percentToWeight(70);
            workout.add(one);
        }else if(week == 3 && day == 2){
            String one = "5x7@" + percentToWeight(75);
            workout.add(one);
        }else if(week == 3 && day == 3){
            String one = "7x5@" + percentToWeight(80);
            workout.add(one);
        }else if(week == 3 && day == 4){
            String one = "10x3@" + percentToWeight(85);
            workout.add(one);
        }else if(week == 4 && day == 1){
            String one = "4x9@" + percentToWeight(70, 20);
            workout.add(one);
        }else if(week == 4 && day == 2){
            String one = "5x7@" + percentToWeight(75, 20);
            workout.add(one);
        }else if(week == 4 && day == 3){
            String one = "7x5@" + percentToWeight(80, 20);
            workout.add(one);
        }else if(week == 4 && day == 4){
            String one = "10x3@" + percentToWeight(85, 20);
            workout.add(one);
        }else if(week == 5 && day == 1){
            String one = "4x9@" + percentToWeight(70, 30);
            workout.add(one);
        }else if(week == 5 && day == 2){
            String one = "5x7@" + percentToWeight(75, 30);
            workout.add(one);
        }else if(week == 5 && day == 3){
            String one = "7x5@" + percentToWeight(80, 30);
            workout.add(one);
        }else if(week == 5 && day == 4) {
            String one = "10x3@" + percentToWeight(85, 30);
            workout.add(one);
        }else if(week == 6 && day == 1){
            String one = "rest";
            workout.add(one);
        }else if(week == 6 && day == 2){
            String one = "rest";
            workout.add(one);
        }else if(week == 6 && day == 3){
            String one = "build to 1rm";
            workout.add(one);
        }else if(week == 6 && day == 4){
            String one = "build to 1rm";
            workout.add(one);
        }else if(week == 7 || week == 8){
            String one = "switching phase";
            workout.add(one);
        }else if(week == 9 && day == 1){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x4@" + percentToWeight(75);
            workout.add(two);
            String three = "3x4@" + percentToWeight(85);
            workout.add(three);
            String four = "1x5@" + percentToWeight(90);
            workout.add(four);
        }else if(week == 9 && day == 2){
            String one = "1x3@" + percentToWeight(60);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70);
            workout.add(two);
            String three = "1x4@" + percentToWeight(80);
            workout.add(three);
            String four = "1x3@" + percentToWeight(90);
            workout.add(four);
            String five = "2x5@" + percentToWeight(85);
            workout.add(five);
        }else if(week == 9 && day == 3){
            String one = "1x4@" + percentToWeight(65);
            workout.add(one);
            String two = "1x4@" + percentToWeight(70);
            workout.add(two);
            String three = "5x4@" + percentToWeight(80);
            workout.add(three);
        }else if(week == 10 && day == 1){
            String one = "1x4@" + percentToWeight(60);
            workout.add(one);
            String two = "1x4@" + percentToWeight(70);
            workout.add(two);
            String three = "1x4@" + percentToWeight(80);
            workout.add(three);
            String four = "1x3@" + percentToWeight(90);
            workout.add(four);
            String five = "2x4@" + percentToWeight(90);
            workout.add(five);
        }else if(week == 10 && day == 2){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "1x3@" + percentToWeight(85);
            workout.add(three);
            String four = "3x3@" + percentToWeight(90);
            workout.add(four);
            String five = "1x3@" + percentToWeight(95);
            workout.add(five);
        }else if(week == 10 && day == 3){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "1x4@" + percentToWeight(85);
            workout.add(three);
            String four = "4x5@" + percentToWeight(90);
            workout.add(four);
        }else if(week == 11 && day == 1){
            String one = "1x3@" + percentToWeight(60);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80);
            workout.add(three);
            String four = "5x5@" + percentToWeight(90);
            workout.add(four);
        }else if(week == 11 && day == 2){
            String one = "1x3@" + percentToWeight(60);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80);
            workout.add(three);
            String four = "2x3@" + percentToWeight(95);
            workout.add(four);
        }else if(week == 11 && day == 3){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "1x3@" + percentToWeight(85);
            workout.add(three);
            String four = "4x3@" + percentToWeight(95);
            workout.add(four);
        }else if(week == 12 && day == 1){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x4@" + percentToWeight(80);
            workout.add(two);
            String three = "5x5@" + percentToWeight(90);
            workout.add(three);
        }else if(week == 12 && day == 2){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80);
            workout.add(two);
            String three = "4x3@" + percentToWeight(95);
            workout.add(three);
        }else if(week == 12 && day == 3){
            String one = "1x3@" + percentToWeight(75);
            workout.add(one);
            String two = "1x4@" + percentToWeight(90);
            workout.add(two);
            String three = "3x4@" + percentToWeight(80);
            workout.add(three);
        }else if(week == 13 && day == 1){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80);
            workout.add(two);
            String three = "2x5@" + percentToWeight(90);
            workout.add(three);
            String four = "3x4@" + percentToWeight(95);
            workout.add(four);
        }else if(week == 13 && day == 2){
            String one = "1x4@" + percentToWeight(75);
            workout.add(one);
            String two = "4x4@" + percentToWeight(85);
            workout.add(two);
        }else if(week == 13 && day == 3){
            String one = "build to 1rm";
            workout.add(one);
        }else{
            String one = "rest";
            workout.add(one);
        }

        return workout;
    }

    double percentToWeight(int percent){
        double weight;
        //int weight2;

        double percentage = (double)percent/(double)100;

        weight = OneRM * percentage;

        //weight2 = (int) Math.round(weight);

        return weight;
    }

    double percentToWeight(int percent, int addExtra){
        double weight;
        //int weight2;
        double percentage = (double)percent/(double)100;

        weight = OneRM * percentage;

        //weight2 = (int) Math.round(weight);

        weight = weight + addExtra;

        return weight;
    }

}























