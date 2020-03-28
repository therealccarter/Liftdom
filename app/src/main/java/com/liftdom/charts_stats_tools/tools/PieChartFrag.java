package com.liftdom.charts_stats_tools.tools;

import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

        return view;
    }

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

        List<PieEntry> entries = new ArrayList<>();

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

    private void setUpPieChart(List<PieEntry> entries){

        PieDataSet dataSet = new PieDataSet(entries, "Exercises");
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
        pieChart.getDescription().setText("");
        pieChart.invalidate();

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
