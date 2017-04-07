package com.liftdom.tools;

import android.util.Log;

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

        double bmrInitial = bmrInitialCalcMet(age, isMale, weight, heightCm);

        ValueAndCaloriesObect vACO1 = new ValueAndCaloriesObect();
        //vACO1.setDietType("Lose 2lbs/week");
        vACO1.setDietType(0);
        vACO1.setCalories(bmrInitial - 1000);
        valueAndCaloriesArrayList.add(vACO1);

        ValueAndCaloriesObect vACO2 = new ValueAndCaloriesObect();
        //vACO2.setDietType("Lose 1lbs/week");
        vACO2.setDietType(1);
        vACO2.setCalories(bmrInitial - 500);
        valueAndCaloriesArrayList.add(vACO2);

        ValueAndCaloriesObect vACO3 = new ValueAndCaloriesObect();
        //vACO3.setDietType("Maintenance");
        vACO3.setDietType(2);
        vACO3.setCalories(bmrInitial);
        valueAndCaloriesArrayList.add(vACO3);

        ValueAndCaloriesObect vACO4 = new ValueAndCaloriesObect();
        //vACO4.setDietType("Gain 1lbs/week");
        vACO4.setDietType(3);
        vACO4.setCalories(bmrInitial + 500);
        valueAndCaloriesArrayList.add(vACO4);

        ValueAndCaloriesObect vACO5 = new ValueAndCaloriesObect();
        //vACO5.setDietType("Gain 2lbs/week");
        vACO5.setDietType(4);
        vACO5.setCalories(bmrInitial + 1000);
        valueAndCaloriesArrayList.add(vACO5);

    }

    private double bmrInitialCalcMet(int age, Boolean isMale, double height, double weight){
        double bmr = 0;

        if(isMale){
            bmr = (10 * weight) + (6.25 * height) - (5 * age) + 5;
        }else{
            bmr = (10 * weight) + (6.25 * height) - (5 * age) - 161;
        }

        return bmr;
    }




}
