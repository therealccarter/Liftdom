package com.liftdom.charts_stats_tools.ex_history_chart;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorSingleton;
import com.liftdom.workout_assistor.CompletedExercisesModelClass;
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

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.lineChart) LineChart lineChart;
    @BindView(R.id.graphingSelectorButton) Button graphingSelector;
    @BindView(R.id.overallRadioButton) RadioButton overallRadioButton;
    @BindView(R.id.maxWeightRadioButton) RadioButton maxWeightRadioButton;
    @BindView(R.id.itemsBeingGraphed) TextView itemsTextView;
    @BindView(R.id.reloadChartButton) Button reloadChart;
    @BindView(R.id.clearChartButton) Button clearChart;

    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts_tab, container, false);

        ButterKnife.bind(this, view);

        maxWeightRadioButton.setChecked(true);


        DatabaseReference completedExs = mRootRef.child("completedExercises").child(uid);
        completedExs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //TODO: Let user know they have no completed exercises
                }else{
                    CompletedExercisesModelClass completedExercisesModelClass = dataSnapshot.getValue
                            (CompletedExercisesModelClass.class);
                    ExSelectorSingleton.getInstance().completedExercises.addAll(completedExercisesModelClass
                            .getCompletedExercisesList());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        graphingSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        clearChart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                ExSelectorSingleton.getInstance().clearArrayLists();
                lineChart.clear();
                dataSets.clear();
                itemsTextView.setText("");
                highest = 0;
                lowest = 0;
                itemsTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));


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

                dataSets.clear();

                boolean isOverall = false;

                if (overallRadioButton.isChecked()) {
                    isOverall = true;
                }

                YAxis leftAxis = lineChart.getAxisLeft();
                leftAxis.resetAxisMaximum();
                leftAxis.resetAxisMinimum();

                ArrayList<String> upperBodyItems = ExSelectorSingleton.getInstance().upperBodyItems;
                ArrayList<String> lowerBodyItems = ExSelectorSingleton.getInstance().lowerBodyItems;
                ArrayList<String> otherItems = ExSelectorSingleton.getInstance().otherItems;

                for (String itemName : upperBodyItems) {
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.isOverall = isOverall;
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }
                for (String itemName : lowerBodyItems) {
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.isOverall = isOverall;
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }
                for (String itemName : otherItems) {
                    SpecificExerciseChartClass exerciseChartClass = new SpecificExerciseChartClass();
                    exerciseChartClass.isOverall = isOverall;
                    exerciseChartClass.getValueList(itemName, StatChartsFrag.this);
                }
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            String exNames = "";

            ArrayList<String> upperBodyItems = ExSelectorSingleton.getInstance().upperBodyItems;
            ArrayList<String> lowerBodyItems = ExSelectorSingleton.getInstance().lowerBodyItems;
            ArrayList<String> otherItems = ExSelectorSingleton.getInstance().otherItems;
            ArrayList<String> customItems = ExSelectorSingleton.getInstance().customItems;

            for (String string : upperBodyItems) {
                if (string != null) {
                    exNames = exNames + string + "\n";
                }
            }
            for (String string : lowerBodyItems) {
                if (string != null) {
                    exNames = exNames + string + "\n";
                }
            }
            for (String string : otherItems) {
                if (string != null) {
                    exNames = exNames + string + "\n";
                }
            }
            for (String string : customItems) {
                if (string != null) {
                    exNames = exNames + string + "\n";
                }
            }

            TextView textView = (TextView) getView().findViewById(R.id.itemsBeingGraphed);

            try {
                textView.setText(exNames);
                textView.setBackgroundColor(Color.parseColor("#cccccc"));
            } catch (NullPointerException e) {

            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ExSelectorSingleton.getInstance().clearArrayLists();
        ExSelectorSingleton.getInstance().clearCompletedExercisesList();
        dataSets.clear();
    }


    public void valueConverter(final ArrayList<ValueAndDateObject> valueAndDateArrayList, String exName, boolean
            isOverall) {
        List<Entry> entries = new ArrayList<Entry>();

        ArrayList<String> datesStrings = new ArrayList<>();

        long reference_timestamp = 0;

        int inc = 0;

        for (ValueAndDateObject data : valueAndDateArrayList) {

            if (!data.getValueX().equals("private_journal") && !data.getValueX().equals("restDay")) {
                DateTime dateTime = new DateTime(data.getValueX());
                Date date = dateTime.toDate();

                long mills = date.getTime();

                if (inc == 0) {
                    reference_timestamp = mills;
                }

                float newStamp = mills - reference_timestamp;

                entries.add(new Entry(newStamp, ((float) data.getValueY())));

                ++inc;

                if (inc == valueAndDateArrayList.size()) {
                    lineDataCreator(entries, reference_timestamp, exName, isOverall);
                }
            }

        }

    }

    float highest = 0;
    float lowest = 0;


    public void lineDataCreator(List<Entry> entries, long reference_timestamp, String exName, boolean isOverall) {

        ChartDateFormatter chartDateFormatter = new ChartDateFormatter(reference_timestamp);

        // x-axis stuff
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(chartDateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(75);
        xAxis.setAxisMinimum(1483228860000f - (float) reference_timestamp); // january 2017
        DateTime now = DateTime.now();
        DateTime plusMonth = now.plusMonths(3);
        long millies = plusMonth.getMillis();
        xAxis.setAxisMaximum((float) millies - (float) reference_timestamp);

        // y-axis stuff
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);

        YAxis leftAxis = lineChart.getAxisLeft();
        if (!isOverall) {
            for (Entry entry : entries) {
                float yValue = entry.getY();
                if (yValue > highest) {
                    highest = yValue;
                }
                if (lowest == 0) {
                    lowest = yValue;
                } else {
                    if (yValue < lowest) {
                        lowest = yValue;
                    }
                }
            }
        }

        // legend stuff
        Legend legend = lineChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_RIGHT);
        legend.setDrawInside(true);

        LineDataSet dataSet = new LineDataSet(entries, exName);
        //dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.BLACK);
        dataSet.setCircleColor(Color.parseColor("#D1B91D"));

        //LineData lineData = new LineData(dataSet);

        dataSets.add(dataSet);

        if(dataSets.size() == 3){
            setLineChart();
        }

        if (dataSets.size() == getItemCount()) {
            setLineChart();
            if (!isOverall) {
                if (dataSets.size() == 1) {
                    leftAxis.setAxisMaximum(highest + 25);
                    leftAxis.setAxisMinimum(lowest - 20);
                } else {
                    leftAxis.setAxisMaximum(highest + 75);
                    leftAxis.setAxisMinimum(lowest - 50);
                }
            }
        }

    }


    private void setLineChart() {

        LineData data = new LineData(dataSets);

        lineChart.setData(data);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.invalidate();
            }
        });

    }

    private int getItemCount() {
        int count = 0;

        count += ExSelectorSingleton.getInstance().upperBodyItems.size();
        count += ExSelectorSingleton.getInstance().lowerBodyItems.size();
        count += ExSelectorSingleton.getInstance().otherItems.size();

        return count;
    }

    public long getCurrentDay() {
        float currentDateInMilli;

        long intermediate;

        DateTime dateTime = (DateTime.now());

        intermediate = dateTime.getMillisOfDay();

        return intermediate;
    }




}
