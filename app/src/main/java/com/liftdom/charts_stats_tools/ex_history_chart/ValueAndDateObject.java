package com.liftdom.charts_stats_tools.ex_history_chart;

/**
 * Created by Brodin on 3/13/2017.
 */

public class ValueAndDateObject {

    // Probably will also need different standards for exercise Y value?
    // obviously we can do the weighted general shit, but we'd also need a max weight hit or some shit

    private String date;
    private double value;

    public String getValueX(){
        return date;
    }

    public double getValueY(){
        return value;
    }

    public void setDate(String dateString){
        date = dateString;
    }

    public void setValue(double valueDouble){
        value = valueDouble;
    }
}