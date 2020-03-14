package com.liftdom.charts_stats_tools.tools;


import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;

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
    @BindView(R.id.heightCm) EditText heightCmEdit;
    @BindView(R.id.cmTextView) TextView cmTextView;
    @BindView(R.id.titleView) TextView titleView;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    boolean isImperial;
    double bodyWeight = 0;
    String height;
    int age = 0;
    String sex = "null";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_calorie_calc, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);

        List<String> activityLevels = new ArrayList<String>();
        activityLevels.add("No Exercise");      //0
        activityLevels.add("Light Exercise");   //1
        activityLevels.add("Moderate Exercise");//2
        activityLevels.add("Heavy Exercise");   //3
        activityLevels.add("Extreme Exercise"); //4

        // Creating adapter for spinner
        final ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_style_new_2,
                activityLevels);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        activityLevelSpinner.setAdapter(dataAdapter);

        DatabaseReference settingsRef = mRootRef.child("user").child(uid);

        if(savedInstanceState == null){

            settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    final UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                    if(userModelClass.getActiveTemplate() == null){
                        activityLevelSpinner.setSelection(0);
                        setValuesFromUserModelClass(userModelClass);
                    }else{
                        DatabaseReference activeTemplateRef = mRootRef.child("templates").child(uid).child
                                (userModelClass.getActiveTemplate());
                        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);
                                String delims = "[_]";
                                String[] daysTokens = templateModelClass.getDays().split(delims);
                                if(daysTokens.length == 1 || daysTokens.length == 2 || daysTokens.length == 3){
                                    activityLevelSpinner.setSelection(1);
                                }else if(daysTokens.length == 4 || daysTokens.length == 5){
                                    activityLevelSpinner.setSelection(2);
                                }else if(daysTokens.length == 6 || daysTokens.length == 7){
                                    activityLevelSpinner.setSelection(3);
                                }else{
                                    activityLevelSpinner.setSelection(4);
                                }

                                setValuesFromUserModelClass(userModelClass);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        calReloadButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startCalCalcChart();
            }
        });

        return view;
    }

    private void setValuesFromUserModelClass(UserModelClass userModelClass){
        if(userModelClass.isIsImperial()){
            // is imperial
            isImperial = true;
            String[] heightTokens = userModelClass.getFeetInchesHeight().split("_");
            heightFeetEdit.setText(heightTokens[0]);
            heightInchesEdit.setText(heightTokens[1]);
            weightEdit.setText(userModelClass.getPounds());
            heightInchesEdit.setVisibility(View.VISIBLE);
            heightFeetEdit.setVisibility(View.VISIBLE);
            feetView.setVisibility(View.VISIBLE);
            inchesView.setVisibility(View.VISIBLE);
        }else{
            // is metric
            isImperial = false;
            heightInchesEdit.setVisibility(View.GONE);
            heightFeetEdit.setVisibility(View.GONE);
            feetView.setVisibility(View.GONE);
            inchesView.setVisibility(View.GONE);
            weightUnit.setText("kg");

            cmTextView.setVisibility(View.VISIBLE);
            heightCmEdit.setVisibility(View.VISIBLE);
            heightCmEdit.setText(userModelClass.getCmHeight());
            weightEdit.setText(userModelClass.getKgs());
        }

        ageEdit.setText(userModelClass.getAge());
        age = Integer.parseInt(userModelClass.getAge());


        if(userModelClass.getSex().equals("male")){
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        }else{
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        }

        startCalCalcChart();
    }

    private void startCalCalcChart(){
        age = Integer.parseInt(ageEdit.getText().toString());
        bodyWeight = Double.parseDouble(weightEdit.getText().toString());
        if(isImperial){
            height = heightFeetEdit.getText().toString() + "_" + heightInchesEdit.getText().toString();
        }else{
            height = heightCmEdit.getText().toString();
        }
        if(maleRadioButton.isChecked()){
            sex = "male";
        }else if(femaleRadioButton.isChecked()){
            sex = "female";
        }

        int spinnerPosition = activityLevelSpinner.getSelectedItemPosition();

        setUpCalCalcClass(spinnerPosition);
    }

    public void setUpCalCalcClass(int spinnerPosition){
        boolean isMale = false;
        if(sex.equals("male")){
            isMale = true;
        }

        double heightCm;
        if(isImperial){
            heightCm = heightConvertToMet(height);
        }else{
            heightCm = (double) Integer.parseInt(height);
        }

        double weightKg;
        if(isImperial){
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


    public void barDataCreator(List<BarEntry> entries){
        barChart.setTouchEnabled(false);

        CalCalcStringFormatter stringFormatter = new CalCalcStringFormatter();

        XAxis xAxis = barChart.getXAxis();
        xAxis.setValueFormatter(stringFormatter);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setLabelRotationAngle(60);
        xAxis.setAxisMinimum(1);
        xAxis.setTextColor(Color.parseColor("#FFFFFF"));

        // y-axis stuff
        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);
        rightAxis.setTextColor(Color.parseColor("#FFFFFF"));

        // legend stuff
        Legend legend = barChart.getLegend();
        //legend.setPosition(Legend.LegendPosition.ABOVE_CHART_LEFT);
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        legend.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        legend.setDrawInside(true);
        legend.setTextColor(Color.parseColor("#FFFFFF"));

        BarDataSet dataSet = new BarDataSet(entries, "Calorie Chart");
        dataSet.setColor(Color.parseColor("#D1B91D"));
        dataSet.setValueTextColor(Color.parseColor("#FFFFFF"));

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
        String[] tokens = heightInches.split(delims);
        int inches = (Integer.parseInt(tokens[0]) * 12) + Integer.parseInt(tokens[1]);

        double cm = (double) inches * 2.54;
        return cm;
    }




}
