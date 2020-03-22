package com.liftdom.charts_stats_tools.ex_history_chart;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.Date;

/**
 * Created by Brodin on 3/20/2017.
 */

public class ChartDateFormatter extends ValueFormatter {

    private long referenceTimestamp; // minimum timestamp in your data set
    private DateTime dateTimeFormat;
    private Date date;

    public ChartDateFormatter(long referenceTimestamp){
        this.referenceTimestamp = referenceTimestamp;

    }

    @Override
    public String getFormattedValue(float value){
        // from float to long
        long convertedTimestamp = ((long) value);

        // Retrieve original timestamp
        long originalTimestamp = referenceTimestamp + convertedTimestamp;

        return getDate(originalTimestamp);
    }


    private String getDate(long timestamp){
        String dateString = "null";

        DateTime dateTime = new DateTime(timestamp);

        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM-dd-yyyy");

        dateString = dtfOut.print(dateTime);

        return dateString;
    }

}
