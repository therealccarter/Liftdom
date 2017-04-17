package com.liftdom.charts_stats_tools.tools;

/**
 * Created by Brodin on 4/7/2017.
 */

public class BMICalculatorClass {

    double weight;
    double height;

    public BMICalculatorClass(double weightKg, double heightCm){
        weight = weightKg;
        height = heightCm;
    }

    public double getBMI(){
        double BMI;

        BMI = (weight) / (height * height);

        BMI = BMI * 10000;

        BMI = (double) Math.round(BMI * 100) / 100;

        return BMI;
    }
}
