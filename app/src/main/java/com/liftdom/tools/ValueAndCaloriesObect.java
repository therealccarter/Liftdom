package com.liftdom.tools;

/**
 * Created by Brodin on 4/6/2017.
 */

public class ValueAndCaloriesObect {

    private String dietType;
    private double calorieValue;

    public String getValueX(){
        return dietType;
    }

    public double getValueY(){
        return calorieValue;
    }

    public void setDietType(String dietTypeString){
        dietType = dietTypeString;
    }

    public void setCalories(double valueDouble){
        calorieValue = valueDouble;
    }
}
