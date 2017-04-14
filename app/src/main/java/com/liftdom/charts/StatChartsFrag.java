package com.liftdom.charts;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.liftdom.liftdom.R;
import com.liftdom.charts.exercise_selector.ExSelectorActivity;
import com.liftdom.charts.exercise_selector.ExSelectorSingleton;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatChartsFrag extends Fragment {



    public StatChartsFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.lineChart) LineChart lineChart;
    @BindView(R.id.graphingSelectorButton) Button graphingSelector;
    @BindView(R.id.modeSelectorButton) Button modeSelector;
    @BindView(R.id.reloadChartButton) Button reloadChart;
    @BindView(R.id.clearChartButton) Button clearChart;

    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts_tab, container, false);

        ButterKnife.bind(this, view);


        //SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
        //exerciseChartClass.getValueList("Barbell Row", StatChartsFrag.this);


        graphingSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                startActivity(intent);
            }
        });

        clearChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ExSelectorSingleton.getInstance().clearArrayLists();
                lineChart.clear();
                dataSets.clear();

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineChart.invalidate();
                    }
                });


            }
        });


        reloadChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ArrayList<String> upperBodyItems = ExSelectorSingleton.getInstance().upperBodyItems;
                ArrayList<String> lowerBodyItems = ExSelectorSingleton.getInstance().lowerBodyItems;
                ArrayList<String> fullBodyItems = ExSelectorSingleton.getInstance().otherItems;

                for(String itemName : upperBodyItems){
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }for(String itemName : lowerBodyItems){
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }for(String itemName : fullBodyItems){
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }
            }
        });

        return view;
    }

    //TODO: create validator to make sure we're not graphing the same exercise twice
    public void valueConverter(final ArrayList<ValueAndDateObject> valueAndDateArrayList, String exName){
        List<Entry> entries = new ArrayList<Entry>();

        ArrayList<String> datesStrings = new ArrayList<>();

        long reference_timestamp = 0;

        int inc = 0;

        for(ValueAndDateObject data : valueAndDateArrayList){

            if(!data.getValueX().equals("private_journal") && !data.getValueX().equals("restDay")){
                DateTime dateTime = new DateTime(data.getValueX());
                Date date = dateTime.toDate();

                long mills = date.getTime();

                if(inc == 0){
                    reference_timestamp = mills;
                }

                float newStamp = mills - reference_timestamp;

                entries.add(new Entry(newStamp, ((float)data.getValueY())));

                ++inc;

                if(inc == valueAndDateArrayList.size()){
                    lineDataCreator(entries, reference_timestamp, exName);
                }
            }

        }

    }


    public void lineDataCreator(List<Entry> entries, long reference_timestamp, String exName){

        ChartDateFormatter chartDateFormatter = new ChartDateFormatter(reference_timestamp);

        // x-axis stuff
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(chartDateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(75);
        xAxis.setAxisMinimum(1483228860000f - (float) reference_timestamp); // january 2017
        xAxis.setAxisMaximum(1514848380000f - (float) reference_timestamp); // january 2018

        // y-axis stuff
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);

        // legend stuff
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setDrawInside(true);

        LineDataSet dataSet = new LineDataSet(entries, exName);
        dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.parseColor("#D1B91D"));

        //LineData lineData = new LineData(dataSet);

        dataSets.add(dataSet);

        if(dataSets.size() == getItemCount()){
            setLineChart();
        }

    }



    private void setLineChart(){

        LineData data = new LineData(dataSets);

        lineChart.setData(data);

        getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        lineChart.invalidate();
                    }
                });

    }

    private int getItemCount(){
        int count = 0;

        count += ExSelectorSingleton.getInstance().upperBodyItems.size();
        count += ExSelectorSingleton.getInstance().lowerBodyItems.size();
        count += ExSelectorSingleton.getInstance().otherItems.size();

        return count;
    }

    public long getCurrentDay(){
        float currentDateInMilli;

        long intermediate;

        DateTime dateTime = (DateTime.now());

        intermediate = dateTime.getMillisOfDay();

        return intermediate;
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();

        ExSelectorSingleton.getInstance().clearArrayLists();
        dataSets.clear();
    }


}
