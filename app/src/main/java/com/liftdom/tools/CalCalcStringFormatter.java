package com.liftdom.tools;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

/**
 * Created by Brodin on 4/7/2017.
 */

public class CalCalcStringFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis){
        return getTitle(value);
    }

    private String getTitle(float position){
        String title = "null";

        if(position == 0){
            title = "Lose 2lbs/week";
        }else if(position == 1){
            title = "Lose 1lb/week";
        }else if(position == 2){
            title = "Maintenance";
        }else if(position == 3){
            title = "Gain 1lb/week";
        }else if(position == 4){
            title = "Gain 2lbs/week";
        }

        return title;
    }
}
