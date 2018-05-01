package com.liftdom.charts_stats_tools.tools;

import java.util.ArrayList;

/**
 * Created by Brodin on 4/6/2017.
 */

public class CalorieCalculatorClass {

    ArrayList<ValueAndCaloriesObect> valueAndCaloriesArrayList = new ArrayList<>();

    public CalorieCalculatorClass(int age, Boolean isMale, double heightCm,
                                  double weight, int spinnerPosition){

        // metric units
        buildDataSetsMet(age, isMale, heightCm, weight, spinnerPosition);
    }

    public ArrayList<ValueAndCaloriesObect> getDataSets(){
        return valueAndCaloriesArrayList;
    }


    // METRIC CALCULATOR
    public void buildDataSetsMet(int age, Boolean isMale, double heightCm, double weight,
                                 int spinnerPosition){

        double bmrInitial = bmrInitialCalcMet(age, isMale, weight, heightCm, spinnerPosition);

        ValueAndCaloriesObect vACO1 = new ValueAndCaloriesObect();
        //vACO1.setDietType("Lose 2lbs/week");
        vACO1.setDietType(1);
        vACO1.setCalories(bmrInitial - 1000);
        valueAndCaloriesArrayList.add(vACO1);

        ValueAndCaloriesObect vACO2 = new ValueAndCaloriesObect();
        //vACO2.setDietType("Lose 1lbs/week");
        vACO2.setDietType(2);
        vACO2.setCalories(bmrInitial - 500);
        valueAndCaloriesArrayList.add(vACO2);

        ValueAndCaloriesObect vACO3 = new ValueAndCaloriesObect();
        //vACO3.setDietType("Maintenance");
        vACO3.setDietType(3);
        vACO3.setCalories(bmrInitial);
        valueAndCaloriesArrayList.add(vACO3);

        ValueAndCaloriesObect vACO4 = new ValueAndCaloriesObect();
        //vACO4.setDietType("Gain 1lbs/week");
        vACO4.setDietType(4);
        vACO4.setCalories(bmrInitial + 500);
        valueAndCaloriesArrayList.add(vACO4);

        ValueAndCaloriesObect vACO5 = new ValueAndCaloriesObect();
        //vACO5.setDietType("Gain 2lbs/week");
        vACO5.setDietType(5);
        vACO5.setCalories(bmrInitial + 1000);
        valueAndCaloriesArrayList.add(vACO5);

    }

    private double bmrInitialCalcMet(int age, Boolean isMale, double height, double weight, int spinnerPosition){
        double bmr = 0;

        if(isMale){
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        }else{
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        bmr = bmr - 250;

        if(spinnerPosition == 1){
            bmr = bmr * 1.2;
        }else if(spinnerPosition == 2){
            bmr = bmr * 1.5;
        }else if(spinnerPosition == 3){
            bmr = bmr * 1.8;
        }else if(spinnerPosition == 4){
            bmr = bmr * 2.0;
        }

        return bmr;
    }




}
