package com.liftdom.charts_stats_tools.tools;

import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class PieChartFrag extends Fragment {

    public PieChartFrag() {
        // Required empty public constructor
    }

    private boolean firstLoad = false;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.allTimeRB) RadioButton allTimeRB;
    @BindView(R.id.last6RB) RadioButton last6MonthRB;
    @BindView(R.id.pieChart) PieChart pieChart;
    @BindView(R.id.loadChartButton) Button loadChartButton;
    @BindView(R.id.previousFive) Button previousFive;
    @BindView(R.id.nextFive) Button nextFive;
    @BindView(R.id.buttonsLL) LinearLayout buttonsLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pie_chart, container, false);

        ButterKnife.bind(this, view);

        allTimeRB.setChecked(true);

        allTimeRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(firstLoad){
                    retrieveExerciseData();
                }
            }
        });

        loadChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(uid != null){
                    retrieveExerciseData();
                }else{
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    retrieveExerciseData();
                }
            }
        });

        previousFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entries.size() != 0){
                    //Drawable background = nextFive.getBackground();
                    //int color = 0;
                    ////if (background instanceof ColorDrawable) {
                    //color = ((ColorDrawable)background).getColor();
                    ////}
                    if(previousIsBlack){
                        previous5();
                    }
                }
            }
        });

        nextFive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(entries.size() != 0){
                    //Drawable background = nextFive.getBackground();
                    //int color = 0;
                    ////if (background instanceof ColorDrawable) {
                    //    color = ((ColorDrawable)background).getColor();
                    ////}
                    if(nextIsBlack){
                        next5();
                    }
                }
            }
        });

        return view;
    }

    private boolean nextIsBlack = true;
    private boolean previousIsBlack = false;

    private List<PieEntry> entries = new ArrayList<>();

    private void retrieveExerciseData(){

        firstLoad = true;

        /**
         * What's the data set look like?
         * We'll run through every WorkoutHistoryModelClass in /workoutHistory.
         * In each one, we'll get a list of the exercises done that day.
         * Then we'll compare it to a hashmap. If it's not in the hashmap, we add the exercise as
         * the key, and an int as the value. If it is in the hashmap, we increase the int value
         * of that key by 1.
         * Given that entries are added value/label, we can just iterate through the hashmap,
         * adding PieEntries as we go.
         */

        entries.clear();
        index = 0;
        previousFive.setBackgroundColor(getResources().getColor(R.color.lessDarkGrey));
        previousIsBlack = false;

        DatabaseReference workoutHistoryRef =
                FirebaseDatabase.getInstance().getReference().child("workoutHistory").child(uid);

        workoutHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int inc = 0;

                HashMap<String, Integer> exerciseHashMap = new HashMap<>();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    WorkoutHistoryModelClass historyModelClass = dataSnapshot1.getValue(WorkoutHistoryModelClass.class);

                    inc++;

                    if(historyModelClass.containsWorkoutInfoMap()){
                        DateTime historyDate = new DateTime(historyModelClass.getDate())
                                .withTime(0,0, 0, 0);
                        String todaysDateString = LocalDate.now().toString();
                        DateTime currentDate = new DateTime(todaysDateString)
                                .withTime(0, 0, 0, 0);

                        int monthsBetween = Months.monthsBetween(historyDate, currentDate).getMonths();

                        if(last6MonthRB.isChecked()){
                            if(monthsBetween <= 6){
                                ArrayList<String> exList = historyModelClass.getExerciseList();
                                for(String exercise : exList){
                                    if(exerciseHashMap.containsKey(exercise)){
                                        // exercise has been added
                                        int amount = exerciseHashMap.get(exercise);
                                        amount++;
                                        exerciseHashMap.put(exercise, amount);
                                    }else{
                                        // new exercise
                                        exerciseHashMap.put(exercise, 1);
                                    }
                                }
                            }
                        }else{
                            ArrayList<String> exList = historyModelClass.getExerciseList();
                            for(String exercise : exList){
                                if(exerciseHashMap.containsKey(exercise)){
                                    // exercise has been added
                                    int amount = exerciseHashMap.get(exercise);
                                    amount++;
                                    exerciseHashMap.put(exercise, amount);
                                }else{
                                    // new exercise
                                    exerciseHashMap.put(exercise, 1);
                                }
                            }
                        }
                    }

                    if(inc == dataSnapshot.getChildrenCount()){
                        for(Map.Entry<String, Integer> entry : exerciseHashMap.entrySet()){
                            entries.add(new PieEntry((float) entry.getValue(), entry.getKey()));
                        }
                        if(entries.size() < 6){
                            nextFive.setBackgroundColor(getResources().getColor(R.color.lessDarkGrey));
                            nextIsBlack = false;
                        }
                        setUpPieChart(entries);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //PieData data = new PieData(year, dataSet);
        //pieChart.setData(data);
        //dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        //pieChart.animateXY(5000, 5000);
    }

    private int index = 0;

    private void previous5(){
        List<PieEntry> newEntries = new ArrayList<>();

        int secondInc = 0;
        int thirdInc = index;
        int fourthInc = index;

        /**
         * Ok, so let's say we're at 20.
         * 1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24
         * 0,1,2,3,4,5, 6,7,8,9,10, 11,12,13,14,15, 16,17,18,19,20, 21,22,23
         */

        if((index + 1) % 5 == 0){
            //for(int i = index; i > index - 5;i--){
            //    if(i < index && i >= index - 5){
            //        newEntries.add(secondInc, entries.get(i));
            //        secondInc++;
            //        thirdInc--;
            //    }
            //}
            index = index - 5;
            for(int i = 0; i < entries.size();i++){
                if(i > index - 5 && i <= index){
                    newEntries.add(secondInc, entries.get(i));
                    secondInc++;
                    thirdInc--;
                }
            }
        }else{
            /**
             * It's showing 24-28. And we want it showing 19-24.
             *
             */

            int leftOver = index % 5; // leftover = 3
            leftOver++; // leftover = 4
            index = index - leftOver; // index (28) - (4) = 24
            thirdInc = index; // thirdInc = 24
            for(int i = 0; i < entries.size();i++){
                if(i > index - 5 && i <= index){
                    newEntries.add(secondInc, entries.get(i));
                    secondInc++;

                }
            }
        }


        index = thirdInc;

        //String infoString = String.valueOf(fourthInc) + ", " + String.valueOf(index);//
        //Snackbar.make(getView(),infoString, Snackbar.LENGTH_SHORT).show();

        nextFive.setBackgroundColor(getResources().getColor(R.color.black));
        nextIsBlack = true;
        if(index == 4){
            previousFive.setBackgroundColor(getResources().getColor(R.color.lessDarkGrey));
            previousIsBlack = false;
        }

        setUpPieChartNew(newEntries);
    }

    private void next5(){
        int size = entries.size();

        /**
         * What do we need to do?
         * We need to know the current last index.
         * Then we for loop through the main entries, and if i is > current last index and <
         * index + 5.
         * Then pass that new list to setUpPieChartNew.
         */

        List<PieEntry> newEntries = new ArrayList<>();

        int secondInc = 0;
        int thirdInc = index;
        int fourthInc = index;

        for(int i = 0; i < entries.size();i++){
            if(i > index && i <= index + 5){
                newEntries.add(secondInc, entries.get(i));
                secondInc++;
                thirdInc++;
            }
        }

        index = thirdInc;

        //String infoString = String.valueOf(fourthInc) + ", " + String.valueOf(index);//
        //Snackbar.make(getView(),infoString, Snackbar.LENGTH_SHORT).show();

        previousFive.setBackgroundColor(getResources().getColor(R.color.black));
        previousIsBlack = true;
        if(index == entries.size() - 1){
            nextFive.setBackgroundColor(getResources().getColor(R.color.lessDarkGrey));
            nextIsBlack = false;
        }

        setUpPieChartNew(newEntries);

    }

    private void setUpPieChartNew(List<PieEntry> entries){
        PieDataSet dataSet = new PieDataSet(entries, "Exercises");
        dataSet.setColors(getColors(), getContext());
        PieData data = new PieData(dataSet);
        data.setValueTextColor(getResources().getColor(R.color.black));
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.getDescription().setText(((PieEntry) e).getLabel());
            }
//
            @Override
            public void onNothingSelected() {
//
            }
        });
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleColor(getResources().getColor(R.color.backgroundgrey1));
        pieChart.setEntryLabelColor(getResources().getColor(R.color.white));
        pieChart.setData(data);
        pieChart.getDescription().setTextColor(getResources().getColor(R.color.white));
        pieChart.getDescription().setText("");
        pieChart.invalidate();

        /**
         * Where we at: need to make it stop when no more entries, then need to do next. Also
         * grey out when no more left.
         */
    }

    private void setUpPieChart(List<PieEntry> entries){

        index = 0;

        Collections.sort(entries, new Comparator<PieEntry>() {
            @Override
            public int compare(PieEntry o1, PieEntry o2) {
                return Double.compare(o1.getY(), o2.getY());
            }
        });

        Collections.reverse(entries);

        List<PieEntry> newEntries = new ArrayList<>();

        for(int i = 0; i < entries.size();i++){
            if(i < 5){
                newEntries.add(i, entries.get(i));
                index = i;
            }
        }

        if(entries.size() <= 5){
            nextFive.setBackgroundColor(getResources().getColor(R.color.lessDarkGrey));
        }

        PieDataSet dataSet = new PieDataSet(newEntries, "Exercises");
        dataSet.setColors(getColors(), getContext());
        PieData data = new PieData(dataSet);
        data.setValueTextColor(getResources().getColor(R.color.black));
        pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
            @Override
            public void onValueSelected(Entry e, Highlight h) {
                pieChart.getDescription().setText(((PieEntry) e).getLabel());
            }

            @Override
            public void onNothingSelected() {

            }
        });
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);
        pieChart.setHoleColor(getResources().getColor(R.color.backgroundgrey1));
        pieChart.setEntryLabelColor(getResources().getColor(R.color.white));
        pieChart.setData(data);
        pieChart.getDescription().setTextColor(getResources().getColor(R.color.white));
        pieChart.getDescription().setText("");
        pieChart.invalidate();
        buttonsLL.setVisibility(View.VISIBLE);

        /**
         * Where we at: need to add some sort of button system to tab between datasets 5
         * at a time. It's just too crowded. Also thinking about loading statCharts automatically.
         */

    }

    private int[] getColors(){
        int[] colorList = new int[]{
                R.color.confirmGreenDark,
                R.color.liftrGold1,
                R.color.darkred,
                R.color.confirmGreen,
                R.color.backgroundgrey2,
        };

        return colorList;
    }

}
