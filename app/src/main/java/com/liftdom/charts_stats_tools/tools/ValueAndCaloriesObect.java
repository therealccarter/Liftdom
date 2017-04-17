package com.liftdom.charts_stats_tools.tools;

/**
 * Created by Brodin on 4/6/2017.
 */

public class ValueAndCaloriesObect {

    private double dietType;
    private double calorieValue;

    public double getValueX(){
        return dietType;
    }

    public double getValueY(){
        return calorieValue;
    }

    public void setDietType(double position){
        dietType = position;
    }

    public void setCalories(double valueDouble){
        calorieValue = valueDouble;
    }
}
