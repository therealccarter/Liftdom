package com.liftdom.charts_stats_tools.ex_history_chart;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorSingleton;
import com.liftdom.user_profile.calendar_stuff.SelectedPastDateDialog;
import com.liftdom.workout_assistor.CompletedExercisesModelClass;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.exercisesRB) RadioButton exercisesRadioButton;
    @BindView(R.id.workoutsRB) RadioButton workoutsRadioButton;
    @BindView(R.id.chartWorkoutsButton) Button chartWorkoutsButton;

    List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts_tab, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);

        maxWeightRadioButton.setChecked(true);
        exercisesRadioButton.setChecked(true);

        overallRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.i("statsChats", "checkedChanged");
                if(!graphingSelector.getText().toString().equals("Choose exercise to graph")){
                    clearChartMethod();
                    String exercise = graphingSelector.getText().toString();
                    ExSelectorSingleton.getInstance().upperBodyItems.add(exercise);
                    graphingSelector.setText(exercise);
                    startChartLoad();
                }
            }
        });

        workoutsRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    clearChartMethod();
                    graphingSelector.setVisibility(View.GONE);
                    chartWorkoutsButton.setVisibility(View.VISIBLE);

                }
            }
        });

        chartWorkoutsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /**
                 * Where we are now: this will mimic the whole process for specific exercises,
                 * the only thing different is we'll be calculating for every exercise in the
                 * workout.
                 */
            }
        });

        DatabaseReference completedExs = mRootRef.child("completedExercises").child(uid);
        completedExs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    //TODO: Let user know they have no completed exercises
                }else{
                    CompletedExercisesModelClass completedExercisesModelClass = dataSnapshot.getValue
                            (CompletedExercisesModelClass.class);
                    try{
                        ExSelectorSingleton.getInstance().completedExercises.addAll(completedExercisesModelClass
                                .getCompletedExercisesList());
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        graphingSelector.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = graphingSelector.getId();
                intent.putExtra("exID", exID);
                intent.putExtra("exclusive", "true");
                startActivityForResult(intent, 2);
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

    private void clearChartMethod(){
        ExSelectorSingleton.getInstance().clearArrayLists();
        lineChart.clear();
        dataSets.clear();
        //itemsTextView.setText("");
        highest = 0;
        lowest = 0;
        dateMap.clear();
        //itemsTextView.setBackgroundColor(Color.parseColor("#FFFFFF"));


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.invalidate();
            }
        });
    }

    private void startChartLoad(){
        //clearChartMethod();
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
        }else if(requestCode == 2){
            if(data != null){
                if(data.getStringExtra("MESSAGE") != null){
                    clearChartMethod();
                    String exercise = data.getStringExtra("MESSAGE");
                    ExSelectorSingleton.getInstance().upperBodyItems.add(exercise);
                    graphingSelector.setText(exercise);
                    startChartLoad();
                }
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

    long mTimestamp = 0;

    /**
     * Where we are: we're considering changing the x values to the straight dates sans the dashes
     * instead of long/float millis versions due to an issue with converting and re-converting
     * giving the wrong value.
     *
     */

    public void valueConverter(final ArrayList<ValueAndDateObject> valueAndDateArrayList, String exName, boolean
            isOverall) {
        List<Entry> entries = new ArrayList<Entry>();

        ArrayList<String> datesStrings = new ArrayList<>();

        long reference_timestamp = 0;

        int inc = 0;

        for (ValueAndDateObject data : valueAndDateArrayList) {

            if (!data.getValueX().equals("private_journal") && !data.getValueX().equals("restDay")) {
                //DateTime dateTime = new DateTime(data.getValueX());
                //Date date = dateTime.toDate();
//
                //long mills = date.getTime();
//
                //if (inc == 0) {
                //    reference_timestamp = mills;
                //    mTimestamp = mills;
                //}
//
                //float newStamp = mills - reference_timestamp;

                // 2017-10-05

                //String delims = "[-]";
                //String[] tokens = data.getValueX().split(delims);
                //String newString = tokens[0] + tokens[1] + tokens[2];
                //float newStamp = Float.parseFloat(newString);

                //entries.add(new Entry(getFloatFromStringDate(data.getValueX()),((float) data.getValueY())));
                entries.add(new Entry((float) inc, ((float) data.getValueY())));
                dateMap.put((float) inc, data.getValueX());

                ++inc;

                if (inc == valueAndDateArrayList.size()) {
                    lineDataCreator(entries, reference_timestamp, exName, isOverall);
                }
            }

        }

    }

    HashMap<Float, String> dateMap = new HashMap<>();

    float highest = 0;
    float lowest = 0;

    private float getFloatFromStringDate(String oldString){
        //String[] tokens = String.valueOf(oldValue).split("");

        DateTime start = new DateTime("2017-08-29");
        DateTime end = new DateTime(oldString);
        int daysBetween = Days.daysBetween(start, end).getDays();

        //String delims = "[-]";
        //String[] tokens = oldString.split(delims);
        //String[] tokens2 = tokens[0].split("");
        //String shortYear = tokens2[tokens2.length - 2] + tokens2[tokens2.length - 1];
        //String newString = shortYear + tokens[1] + tokens[2];
        //int yearInt = Integer.parseInt(newString);
        //yearInt = yearInt - 170805;
        return (float) daysBetween;
    }


    public void lineDataCreator(List<Entry> entries, long reference_timestamp, String exName, boolean isOverall) {

        ChartDateFormatter chartDateFormatter = new ChartDateFormatter(dateMap);

        // x-axis stuff
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(chartDateFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(75);
        //xAxis.setAxisMinimum(1483228860000f - (float) reference_timestamp); // january 2017
        xAxis.setAxisMinimum(0);
        xAxis.setTextColor(getResources().getColor(R.color.grey));
        DateTime now = DateTime.now();
        DateTime plusMonth = now.plusMonths(3);
        DateTimeFormatter dtfOut = DateTimeFormat.forPattern("yyyy-MM-dd");
        String dateString = dtfOut.print(plusMonth);
        //long millies = plusMonth.getMillis();
        //xAxis.setAxisMaximum(getFloatFromStringDate(dateString));
        xAxis.setAxisMaximum(35);

        // y-axis stuff
        YAxis rightAxis = lineChart.getAxisRight();
        rightAxis.setDrawLabels(false);


        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(getResources().getColor(R.color.grey));
        if (!isOverall) {
            boolean firstTime = false;
            for (Entry entry : entries) {
                float yValue = entry.getY();
                if (yValue > highest) {
                    highest = yValue;
                }
                if (lowest == 0 && !firstTime) {
                    lowest = yValue;
                    firstTime = true;
                } else {
                    if (yValue < lowest) {
                        lowest = yValue;
                    }
                }
            }
        }

        // legend stuff
        Legend legend = lineChart.getLegend();
        legend.setEnabled(true);
        legend.setTextColor(Color.WHITE);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);

        LineDataSet dataSet = new LineDataSet(entries, exName);
        //dataSet.setColor(Color.BLACK);
        dataSet.setValueTextColor(Color.WHITE);
        dataSet.setColor(getResources().getColor(R.color.liftrGold1));
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
                    if(lowest <= 25){
                        leftAxis.setAxisMinimum(0);
                    }else{
                        leftAxis.setAxisMinimum(lowest - 25);
                    }

                } else {
                    leftAxis.setAxisMaximum(highest + 75);
                    if(lowest <= 50){
                        leftAxis.setAxisMinimum(0);
                    }else{
                        leftAxis.setAxisMinimum(lowest - 50);
                    }
                }
            }
        }

    }

    private void setLineChart() {

        LineData data = new LineData(dataSets);

        lineChart.setBackgroundColor(getResources().getColor(R.color.backgroundgrey1));

        lineChart.setData(data);

        lineChart.setOnChartValueSelectedListener(new com.github.mikephil.charting.listener.OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                showWorkoutForSelection(e.getX());
            }

            @Override
            public void onNothingSelected() {

            }
        });

        lineChart.getDescription().setEnabled(false);

        //lineChart.moveViewToX(getNewestDateMinusThree());
        lineChart.zoom(4.2f, 1f, getNewestX(), getNewestY(), YAxis.AxisDependency.LEFT);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                lineChart.invalidate();
            }
        });

    }

    private String getStringFromFloat(float timestamp){
        //String[] tokens = String.valueOf(timestamp).split("");
//
        //String newString = tokens[0] + tokens[1] + "-" + tokens[2] + tokens[3] +
        //                "-" + tokens[4] + tokens[5];
//
        //return newString;
        return dateMap.get(timestamp);
    }

    private void showWorkoutForSelection(float x){

        //long mills = date.getTime();

        //long timestamp = ((long) x);
//
        //DateTime dateTime = new DateTime(timestamp + mTimestamp);
//
        //DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");

        String formatted = getStringFromFloat(x);

        //formatted = "20" + formatted;

        Intent pastIntent = new Intent(getContext(), SelectedPastDateDialog.class);

        pastIntent.putExtra("date", formatted);
        startActivity(pastIntent);
    }

    private float getNewestX(){
        float value;

        int size = dataSets.get(dataSets.size() - 1).getEntryCount();

        value = dataSets.get(dataSets.size() - 1).getEntryForIndex(size - 1).getX();

        return value;
    }

    private float getNewestY(){
        float value;

        int size = dataSets.get(dataSets.size() - 1).getEntryCount();

        value = dataSets.get(dataSets.size() - 1).getEntryForIndex(size - 1).getY();

        return value;
    }

    private float getNewestDateMinusThree(){
        float dateMinusThreeMonths = 0;

        int size = dataSets.get(dataSets.size() - 1).getEntryCount();

        float newestDate = dataSets.get(dataSets.size() - 1).getEntryForIndex(size - 1).getX();

        long converted = ((long) newestDate);

        dateMinusThreeMonths = ((float) getDate(converted));

        return dateMinusThreeMonths;
    }

    private long getDate(long timestamp){
        long dateString = 0;

        DateTime dateTime = new DateTime(timestamp);

        dateTime = dateTime.minusMonths(3);

        //DateTimeFormatter dtfOut = DateTimeFormat.forPattern("MM-dd-yyyy");

        dateString = dateTime.getMillis();

        return dateString;
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
