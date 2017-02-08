package com.liftdom.workout_programs;

import java.util.ArrayList;

/**
 * Created by Chris on 2/8/2017.
 */

public class Smolov {

    int Week;
    int Day;
    int OneRM;

    public Smolov(int week, int day, int oneRM){
        setWeek(week);
        setDay(day);
        setOneRM(oneRM);
    }

    void setWeek(int week){
        Week = week;
    }

    void setDay(int day){
        Day = day;
    }

    void setOneRM(int oneRM){
        OneRM = oneRM;
    }

    ArrayList<String> getWorkout(){
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
        }

        return workout;
    }

    int percentToWeight(int percent){
        double weight;
        int weight2;
        double percentage = percent/100;

        weight = OneRM * percentage;

        weight2 = (int) Math.round(weight);

        return weight2;
    }

    int percentToWeight(int percent, int addExtra){
        double weight;
        int weight2;
        double percentage = percent/100;

        weight = OneRM * percentage;

        weight2 = (int) Math.round(weight);

        weight2 = weight2 + addExtra;

        return weight2;
    }

}























