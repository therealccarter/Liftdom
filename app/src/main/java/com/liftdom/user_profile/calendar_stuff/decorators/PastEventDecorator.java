package com.liftdom.user_profile.calendar_stuff.decorators;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.Collection;
import java.util.HashSet;

/**
 * Created by Chris on 3/7/2017.
 */

public class PastEventDecorator implements DayViewDecorator {

    private int color;
    private HashSet<CalendarDay> dates;
    private int mSpanSize = 10;

    public PastEventDecorator(int color, Collection<CalendarDay> dates) {
        this.color = color;
        this.dates = new HashSet<>(dates);
    }

    public PastEventDecorator(int color, Collection<CalendarDay> dates, int spanSize) {
        this.color = color;
        this.dates = new HashSet<>(dates);
        mSpanSize = spanSize;
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(mSpanSize, color));

    }
}
