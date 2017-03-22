package com.liftdom.user_profile.stat_chart_stuff;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.Date;

/**
 * Created by Brodin on 3/20/2017.
 */

public class ChartDateFormatter implements IAxisValueFormatter {

    @Override
    public String getFormattedValue(float value, AxisBase axis) {
        // Simple version. You should use a DateFormatter to specify how you want to textually represent your date.
        long longValue = ((long) value);
        return new Date(longValue).toString();
    }
    // ...
}
