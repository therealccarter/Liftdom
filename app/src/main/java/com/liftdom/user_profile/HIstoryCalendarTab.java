package com.liftdom.user_profile;


import android.graphics.Color;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CalendarView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.decorators.OneDayDecorator;
import com.liftdom.liftdom.decorators.PastEventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */

public class HistoryCalendarTab extends Fragment implements OnDateSelectedListener {


    public HistoryCalendarTab() {
        // Required empty public constructor
    }

    private final OneDayDecorator oneDayDecorator = new OneDayDecorator();

    @BindView(R.id.calendarView) MaterialCalendarView widget;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_calendar_tab, container, false);

        ButterKnife.bind(this, view);

        // things that only happen upon action

        widget.setOnDateChangedListener(this);
        widget.setShowOtherDates(MaterialCalendarView.SHOW_ALL);

        // Future dates draw from active template.
        // Past dates draw from history, obviously.

        LocalDate currentDate = LocalDate.now();
        widget.setSelectedDate(currentDate.toDate());

        DateTime minDate = new DateTime("2017-01-01");
        DateTime maxDate = new DateTime("2017-12-31");

        widget.state().edit()
                .setMinimumDate(minDate.toDate())
                .setMaximumDate(maxDate.toDate())
                .commit();

        DateTime testDate1 = new DateTime("2017-03-01");
        DateTime testDate2 = new DateTime("2017-03-02");
        DateTime testDate3 = new DateTime("2017-03-03");

        ArrayList<CalendarDay> pastDates = new ArrayList<>();

        CalendarDay converted1 = CalendarDay.from(testDate1.toDate());
        CalendarDay converted2 = CalendarDay.from(testDate2.toDate());
        CalendarDay converted3 = CalendarDay.from(testDate3.toDate());

        pastDates.add(converted1);
        pastDates.add(converted2);
        pastDates.add(converted3);

        widget.addDecorator(new PastEventDecorator(Color.RED, pastDates));

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
        //If you change a decorate, you need to invalidate decorators
        oneDayDecorator.setDate(date.getDate());
        widget.invalidateDecorators();
    }


}












