package com.liftdom.workout_programs;

import java.util.ArrayList;

/**
 * Created by Chris on 2/8/2017.
 */

public class Smolov {

    int Week;
    int Day;
    double OneRM;

    public Smolov(int week, int day, double oneRM){
        setWeek(week);
        setDay(day);
        setOneRM(oneRM);
    }

    // maybe ask when they want to start OR nearest monday.
    // be a real companion. personalize the shit...
    // tell them about eating and sleeping and stretching, foam rolling,etc
    // give them options for push notifications and reminders
    // make sure to show special on the calendar for routine so you can see bad

    void setWeek(int week){
        Week = week;
    }

    void setDay(int day){
        Day = day;
    }

    void setOneRM(double oneRM){
        OneRM = oneRM;
    }

    public ArrayList<String> getWorkout(){
        ArrayList<String> workout = new ArrayList<>();

        if(Week == 1 && Day == 1){
            String one = "3x8@" + percentToWeight(65);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80);
            workout.add(four);
        }else if(Week == 1 && Day == 2){
            String one = "3x8@" + percentToWeight(65);
            workout.add(one);
            String two = "1x5@" + percentToWeight(70);
            workout.add(two);
            String three = "2x2@" + percentToWeight(75);
            workout.add(three);
            String four = "1x1@" + percentToWeight(80);
            workout.add(four);
        }else if(Week == 1 && Day == 3){
            String one = "4x5@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "2x2@" + percentToWeight(80);
            workout.add(three);
            String four = "1x1@" + percentToWeight(90);
            workout.add(four);
        }else if(Week == 2 && Day == 1){
            String one = "1x5@" + percentToWeight(80);
            workout.add(one);
        }else if(Week == 2 && Day == 2){
            String one = "1x5@" + percentToWeight(83);
            workout.add(one);
        }else if(Week == 2 && Day == 3){
            String one = "1x5@" + percentToWeight(85);
            workout.add(one);
        }else if(Week == 3 && Day == 1){
            String one = "4x9@" + percentToWeight(70);
            workout.add(one);
        }else if(Week == 3 && Day == 2){
            String one = "5x7@" + percentToWeight(75);
            workout.add(one);
        }else if(Week == 3 && Day == 3){
            String one = "7x5@" + percentToWeight(80);
            workout.add(one);
        }else if(Week == 3 && Day == 4){
            String one = "10x3@" + percentToWeight(85);
            workout.add(one);
        }else if(Week == 4 && Day == 1){
            String one = "4x9@" + percentToWeight(70, 20);
            workout.add(one);
        }else if(Week == 4 && Day == 2){
            String one = "5x7@" + percentToWeight(75, 20);
            workout.add(one);
        }else if(Week == 4 && Day == 3){
            String one = "7x5@" + percentToWeight(80, 20);
            workout.add(one);
        }else if(Week == 4 && Day == 4){
            String one = "10x3@" + percentToWeight(85, 20);
            workout.add(one);
        }else if(Week == 5 && Day == 1){
            String one = "4x9@" + percentToWeight(70, 30);
            workout.add(one);
        }else if(Week == 5 && Day == 2){
            String one = "5x7@" + percentToWeight(75, 30);
            workout.add(one);
        }else if(Week == 5 && Day == 3){
            String one = "7x5@" + percentToWeight(80, 30);
            workout.add(one);
        }else if(Week == 5 && Day == 4) {
            String one = "10x3@" + percentToWeight(85, 30);
            workout.add(one);
        }else if(Week == 6 && Day == 1){
            String one = "rest";
            workout.add(one);
        }else if(Week == 6 && Day == 2){
            String one = "rest";
            workout.add(one);
        }else if(Week == 6 && Day == 3){
            String one = "build to 1rm";
            workout.add(one);
        }else if(Week == 6 && Day == 4){
            String one = "build to 1rm";
            workout.add(one);
        }else if(Week == 7 || Week == 8){
            String one = "switching phase";
            workout.add(one);
        }else if(Week == 9 && Day == 1){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x4@" + percentToWeight(75);
            workout.add(two);
            String three = "3x4@" + percentToWeight(85);
            workout.add(three);
            String four = "1x5@" + percentToWeight(90);
            workout.add(four);
        }else if(Week == 9 && Day == 2){
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
        }else if(Week == 9 && Day == 3){
            String one = "1x4@" + percentToWeight(65);
            workout.add(one);
            String two = "1x4@" + percentToWeight(70);
            workout.add(two);
            String three = "5x4@" + percentToWeight(80);
            workout.add(three);
        }else if(Week == 10 && Day == 1){
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
        }else if(Week == 10 && Day == 2){
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
        }else if(Week == 10 && Day == 3){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "1x4@" + percentToWeight(85);
            workout.add(three);
            String four = "4x5@" + percentToWeight(90);
            workout.add(four);
        }else if(Week == 11 && Day == 1){
            String one = "1x3@" + percentToWeight(60);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80);
            workout.add(three);
            String four = "5x5@" + percentToWeight(90);
            workout.add(four);
        }else if(Week == 11 && Day == 2){
            String one = "1x3@" + percentToWeight(60);
            workout.add(one);
            String two = "1x3@" + percentToWeight(70);
            workout.add(two);
            String three = "1x3@" + percentToWeight(80);
            workout.add(three);
            String four = "2x3@" + percentToWeight(95);
            workout.add(four);
        }else if(Week == 11 && Day == 3){
            String one = "1x3@" + percentToWeight(65);
            workout.add(one);
            String two = "1x3@" + percentToWeight(75);
            workout.add(two);
            String three = "1x3@" + percentToWeight(85);
            workout.add(three);
            String four = "4x3@" + percentToWeight(95);
            workout.add(four);
        }else if(Week == 12 && Day == 1){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x4@" + percentToWeight(80);
            workout.add(two);
            String three = "5x5@" + percentToWeight(90);
            workout.add(three);
        }else if(Week == 12 && Day == 2){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80);
            workout.add(two);
            String three = "4x3@" + percentToWeight(95);
            workout.add(three);
        }else if(Week == 12 && Day == 3){
            String one = "1x3@" + percentToWeight(75);
            workout.add(one);
            String two = "1x4@" + percentToWeight(90);
            workout.add(two);
            String three = "3x4@" + percentToWeight(80);
            workout.add(three);
        }else if(Week == 13 && Day == 1){
            String one = "1x3@" + percentToWeight(70);
            workout.add(one);
            String two = "1x3@" + percentToWeight(80);
            workout.add(two);
            String three = "2x5@" + percentToWeight(90);
            workout.add(three);
            String four = "3x4@" + percentToWeight(95);
            workout.add(four);
        }else if(Week == 13 && Day == 2){
            String one = "1x4@" + percentToWeight(75);
            workout.add(one);
            String two = "4x4@" + percentToWeight(85);
            workout.add(two);
        }else if(Week == 13 && Day == 3){
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























