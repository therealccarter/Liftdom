package com.liftdom.charts_stats_tools.ex_history_chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;
import java.util.HashMap;

/**
 * Created by Brodin on 3/20/2017.
 */

public class ChartDateFormatter extends ValueFormatter {

    private long referenceTimestamp; // minimum timestamp in your data set
    private DateTime dateTimeFormat;
    private Date date;
    private HashMap<Float, String> dateMap = new HashMap<>();

    public ChartDateFormatter(HashMap<Float, String> map){
        this.dateMap = map;
    }

    @Override
    public String getFormattedValue(float value){
        // from float to long
        //long convertedTimestamp = ((long) value);
//
        //// Retrieve original timestamp
        //long originalTimestamp = referenceTimestamp + convertedTimestamp;

        String date = dateMap.get(value);
        if(date == null){
            return "";
        }else{
            return date;
        }

        //return dateMap.get(value);
    }


    private String getDate(float timestamp){
        // timestamp == 20190903
        //String dateString = "null";

        //String[] tokens = String.valueOf(timestamp).split("");
//
        //String newString = tokens[0] + tokens[1] + "-" + tokens[2] + tokens[3] +
        //        "-" + tokens[4] + tokens[5];

        //DateTime dateTime = new DateTime(newString);
//
        //DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM-dd-yyyy");
//
        //dateString = dtfOut.print(dateTime);

        //return "20" + newString;
        //int intVersion = (int) timestamp;
        return dateMap.get(timestamp);
    }

}
