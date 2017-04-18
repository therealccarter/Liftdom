package com.liftdom.charts_stats_tools.tools;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalorieCalcFrag extends Fragment {


    public CalorieCalcFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.calReloadButton) Button calReloadButton;
    @BindView(R.id.ageEditText) EditText ageEdit;
    @BindView(R.id.maleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.femaleRadioButton) RadioButton femaleRadioButton;
    @BindView(R.id.heightFeet) EditText heightFeetEdit;
    @BindView(R.id.heightInches) EditText heightInchesEdit;
    @BindView(R.id.feetTextView) TextView feetView;
    @BindView(R.id.inchesTextView) TextView inchesView;
    @BindView(R.id.weightEditText) EditText weightEdit;
    @BindView(R.id.activityLevelSpinner) Spinner activityLevelSpinner;
    @BindView(R.id.weightUnit) TextView weightUnit;
    @BindView(R.id.calorieCalcChart) BarChart barChart;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String bodyWeightUnit = "null";
    double bodyWeight = 0;
    String heightUnit = "null";
    String height;
    int age = 0;
    String sex = "null";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calorie_calc, container, false);

        ButterKnife.bind(this, view);

        //TODO: make sure that in the charts activity we convert over pounds and kg to be equal

        DatabaseReference settingsRef = mRootRef.child("users").child(uid);

        List<String> activityLevels = new ArrayList<String>();
        activityLevels.add("No Exercise");
        activityLevels.add("Light Exercise");
        activityLevels.add("Moderate Exercise");
        activityLevels.add("Heavy Exercise");
        activityLevels.add("Extreme Exercise");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style_new_2,
                activityLevels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        activityLevelSpinner.setAdapter(dataAdapter);

        if(savedInstanceState == null){

            settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int inc = 0;

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String key = dataSnapshot1.getKey();
                        if(!key.equals("maxes")){
                            if(key.equals("bodyWeightUnit")){
                                bodyWeightUnit = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("bodyweight")){
                                bodyWeight = (double) Integer.parseInt(dataSnapshot1.getValue(String.class));
                            }else if(key.equals("heightUnit")){
                                heightUnit = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("height")){
                                height = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("age")){
                                age = Integer.parseInt(dataSnapshot1.getValue(String.class));
                            }else if(key.equals("sex")){
                                sex = dataSnapshot1.getValue(String.class);
                            }
                        }
                        inc++;

                        if(inc == dataSnapshot.getChildrenCount()){
                            //TODO change units
                            weightEdit.setText(String.valueOf(bodyWeight));
                            if(heightUnit.equals("footInches")){
                                String delims = "[_]";
                                String[] tokens = height.split(delims);
                                heightFeetEdit.setText(tokens[0]);
                                heightInchesEdit.setText(tokens[1]);
                            }else{
                                //TODO use kilos?
                            }
                            ageEdit.setText(String.valueOf(age));

                            if(sex.equals("male")){
                                maleRadioButton.setChecked(true);
                            }else if(sex.equals("female")){
                                femaleRadioButton.setChecked(true);
                            }

                            if(savedInstanceState == null){
                                age = Integer.parseInt(ageEdit.getText().toString());
                                bodyWeight = Double.parseDouble(weightEdit.getText().toString());
                                if(heightUnit.equals("pounds")){
                                    height = heightFeetEdit.getText().toString() + "_" + heightInchesEdit.getText().toString();
                                }
                                if(maleRadioButton.isChecked()){
                                    sex = "male";
                                }else if(femaleRadioButton.isChecked()){
                                    sex = "female";
                                }

                                int spinnerPosition = activityLevelSpinner.getSelectedItemPosition();

                                setUpCalCalcClass(spinnerPosition);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

        calReloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                age = Integer.parseInt(ageEdit.getText().toString());
                bodyWeight = Double.parseDouble(weightEdit.getText().toString());
                if(heightUnit.equals("pounds")){
                    height = heightFeetEdit.getText().toString() + "_" + heightInchesEdit.getText().toString();
                }
                if(maleRadioButton.isChecked()){
                    sex = "male";
                }else if(femaleRadioButton.isChecked()){
                    sex = "female";
                }

                int spinnerPosition = activityLevelSpinner.getSelectedItemPosition();

                setUpCalCalcClass(spinnerPosition);
            }
        });

        return view;
    }

    public void setUpCalCalcClass(int spinnerPosition){
        boolean isMale = false;
        if(sex.equals("male")){
            isMale = true;
        }

        double heightCm;
        if(heightUnit.equals("footInches")){
            heightCm = heightConvertToMet(height);
        }else{
            heightCm = (double) Integer.parseInt(height);
        }

        double weightKg;
        if(bodyWeightUnit.equals("pounds")){
            weightKg = weightConvertToMet(bodyWeight);
        }else{
            weightKg = bodyWeight;
        }

        CalorieCalculatorClass calCalcClass = new CalorieCalculatorClass(
                age, isMale, heightCm, weightKg, spinnerPosition
                );

        ArrayList<ValueAndCaloriesObect> dataSets = calCalcClass.getDataSets();
        setUpBarChart(dataSets);
    }

    public void setUpBarChart(ArrayList<ValueAndCaloriesObect> valueAndCalArrayList){
        List<BarEntry> entries = new ArrayList<BarEntry>();

        int inc = 0;

        for(ValueAndCaloriesObect data : valueAndCalArrayList){

            entries.add(new BarEntry((float) data.getValueX(), (float) data.getValueY()));

            ++inc;

            if(inc == valueAndCalArrayList.size()){
                barDataCreator(entries);
            }
        }
    }

    //TODO: extra x-axis value added for some reason

    public void barDataCreator(List<BarEntry> entries){
        CalCalcStringFormatter stringFormatter = new CalCalcStringFormatter();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(stringFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(60);

        // y-axis stuff
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);

        // legend stuff
        Legend legend = barChart.getLegend();
        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setDrawInside(true);

        BarDataSet dataSet = new BarDataSet(entries, "Calorie Chart");
        dataSet.setColor(Color.parseColor("#D1B91D"));

        setBarChart(dataSet);
    }

    public void setBarChart(BarDataSet dataSet){
        BarData data = new BarData(dataSet);

        barChart.setData(data);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                barChart.invalidate();
            }
        });
    }

    public double weightConvertToMet(double weight){
        double kgs = weight * 0.453592;
        return kgs;
    }

    public double heightConvertToMet(String heightInches){
        String delims = "[_]";
        String[] tokens = height.split(delims);
        int inches = (Integer.parseInt(tokens[0]) * 12) + Integer.parseInt(tokens[1]);

        double cm = (double) inches * 2.54;
        return cm;
    }



}
