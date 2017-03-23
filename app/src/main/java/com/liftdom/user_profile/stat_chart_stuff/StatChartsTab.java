package com.liftdom.user_profile.stat_chart_stuff;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.liftdom.liftdom.R;
import io.reactivex.Observable;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class  StatChartsTab extends Fragment {


    public StatChartsTab() {
        // Required empty public constructor
    }

    @BindView(R.id.lineChart) LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts_tab, container, false);

        ButterKnife.bind(this, view);

        // we could have an average weighted set that is able to show general progress
        // that main chart will be the workout history + future workouts
        // we'll also have charts for each exercise you've done at least once
        // also bodyweight/bf%/etc

        // we'll have a button to choose an exercise, which then we'll crawl through the past and active template
        // (future) to chart it.

        // so, depending on which view of the exercise we choose is, we'll have to somehow pass in the view we want
        // ie, either overall load or max weight used...either way, we'll be getting back values and dates.

        //StatOverviewChartClass statOverviewChartClass = new StatOverviewChartClass();
//
        //ArrayList<ValueAndDateObject> overviewValueAndDateList = statOverviewChartClass.getOverviewStatValues();


        SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
        exerciseChartClass.getValueList("Barbell Row", StatChartsTab.this);



        return view;
    }

    public void setUpUI(final ArrayList<ValueAndDateObject> valueAndDateArrayList){
        List<Entry> entries = new ArrayList<Entry>();

        ArrayList<String> datesStrings = new ArrayList<>();

        long reference_timestamp = 0;

        int inc = 0;

        for(ValueAndDateObject data : valueAndDateArrayList){

            if(!data.getValueX().equals("private_journal")){
                DateTime dateTime = new DateTime(data.getValueX());
                Date date = dateTime.toDate();

                long mills = date.getTime();

                if(inc == 1){
                    reference_timestamp = mills;
                }

                float newStamp = mills - reference_timestamp;

                entries.add(new Entry(newStamp, ((float)data.getValueY())));

                ++inc;

                if(inc == valueAndDateArrayList.size()){
                    updateUI(entries, reference_timestamp);
                }
            }

        }

    }

    public void updateUI(List<Entry> entries, long reference_timestamp){

        ChartDateFormatter chartDateFormatter = new ChartDateFormatter(reference_timestamp);

        // x-axis stuff
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(chartDateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(90);


        LineDataSet dataSet = new LineDataSet(entries, "Label");
        dataSet.setColor(Color.YELLOW);
        dataSet.setValueTextColor(Color.BLACK);

        LineData lineData = new LineData(dataSet);
        lineChart.setData(lineData);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.invalidate();
            }
        });
    }




}
