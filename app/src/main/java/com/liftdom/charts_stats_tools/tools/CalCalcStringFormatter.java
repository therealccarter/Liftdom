package com.liftdom.charts_stats_tools.tools;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

/**
 * Created by Brodin on 4/7/2017.
 */

public class CalCalcStringFormatter extends ValueFormatter {

    //@Override
    //public String getFormattedValue(float value, AxisBase axis){
    //    return getTitle(value);
    //}
    @Override
    public String getFormattedValue(float value){
        return getTitle(value);
    }

    private String getTitle(float position){
        String title = "null";

        int positionInt = (int) position;

        if(positionInt == 1){
            title = "Lose 2lbs/week";
        }else if(positionInt == 2){
            title = "Lose 1lb/week";
        }else if(positionInt == 3){
            title = "Maintenance";
        }else if(positionInt == 4){
            title = "Gain 1lb/week";
        }else if(positionInt == 5){
            title = "Gain 2lbs/week";
        }

        return title;
    }
}
