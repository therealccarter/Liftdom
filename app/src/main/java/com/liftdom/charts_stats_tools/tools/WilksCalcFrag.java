package com.liftdom.charts_stats_tools.tools;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class WilksCalcFrag extends Fragment {

    public WilksCalcFrag() {
        // Required empty public constructor
    }

    double[] maleVariables = {
            -216.0475144,
            16.2606339,
            -0.002388645,
            -0.00113732,
            0.00000701863,
            -0.00000001291
    };

    double[] femaleVariables = {
            594.31747775582,
            -27.23842536447,
            0.82112226871,
            -0.00930733913,
            0.00004731582,
            -0.00000009054
    };

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean isImperialPOV = false;
    String bodyWeightKgs;

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.resultView) TextView resultsView;
    @BindView(R.id.squatEditText) EditText squatEditText;
    @BindView(R.id.squatUnit) TextView squatUnit;
    @BindView(R.id.benchPressEditText) EditText benchPressEditText;
    @BindView(R.id.benchPressUnit) TextView benchPressUnit;
    @BindView(R.id.deadliftEditText) EditText deadliftEditText;
    @BindView(R.id.deadliftUnit) TextView deadliftUnit;
    @BindView(R.id.calculateButton) Button calculateButton;
    @BindView(R.id.maleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.femaleRadioButton) RadioButton femaleRadioButton;
    @BindView(R.id.bodyWeightEditText) EditText bodyWeightEditText;
    @BindView(R.id.bodyWeightUnit) TextView bodyWeightUnit;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wilks_calc, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);

        if(savedInstanceState == null){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        if(userModelClass.isIsImperial()){
                            isImperialPOV = true;
                            setToLbs();
                            bodyWeightEditText.setText(userModelClass.getPounds());
                        }else{
                            isImperialPOV = false;
                            setToKgs();
                            bodyWeightEditText.setText(userModelClass.getKgs());
                        }

                        if(userModelClass.getSex().equals("male")){
                            maleRadioButton.setChecked(true);
                            femaleRadioButton.setChecked(false);
                        }else{
                            femaleRadioButton.setChecked(true);
                            maleRadioButton.setChecked(false);
                        }

                        checkForMaxes();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateAndSetWilks();
            }
        });


        return view;
    }

    private void calculateAndSetWilks(){
        if(!squatEditText.getText().toString().isEmpty()
                && !benchPressEditText.getText().toString().isEmpty()
                && !deadliftEditText.getText().toString().isEmpty()
                && !bodyWeightEditText.getText().toString().isEmpty()){

            double bw = 0;

            if(isImperialPOV){
                // is imperial
                String val = imperialToMetric(bodyWeightEditText.getText().toString());
                bw = Double.parseDouble(val);
            }else{
                bw = Double.parseDouble(bodyWeightEditText.getText().toString());
            }

            double coefficient = getCoeff(bw, maleRadioButton.isChecked());

            double total = getTotal();

            double wilks = total * coefficient;

            int wilksInt = (int) wilks;

            resultsView.setText(String.valueOf(wilksInt));

        }else{
            Snackbar.make(getView(), "Fill in all text fields first.", Snackbar.LENGTH_SHORT).show();
        }
    }

    private double getCoeff(double bodyWeight, boolean isMale){
        double coefficent;

        if(isMale){
            coefficent = maleVariables[0]
                    + (maleVariables[1] * bodyWeight)
                    + (maleVariables[2] * (Math.pow(bodyWeight, 2)))
                    + (maleVariables[3] * (Math.pow(bodyWeight, 3)))
                    + (maleVariables[4] * (Math.pow(bodyWeight, 4)))
                    + (maleVariables[5] * (Math.pow(bodyWeight, 5)));

            coefficent = 500 / coefficent;
        }else{
            coefficent = femaleVariables[0]
                    + (femaleVariables[1] * bodyWeight)
                    + (femaleVariables[2] * (Math.pow(bodyWeight, 2)))
                    + (femaleVariables[3] * (Math.pow(bodyWeight, 3)))
                    + (femaleVariables[4] * (Math.pow(bodyWeight, 4)))
                    + (femaleVariables[5] * (Math.pow(bodyWeight, 5)));

            coefficent = 500 / coefficent;
        }


        return coefficent;
    }

    private double getTotal(){
        double total;

        total = Double.parseDouble(squatEditText.getText().toString())
                + Double.parseDouble(benchPressEditText.getText().toString())
                + Double.parseDouble(deadliftEditText.getText().toString());

        if(isImperialPOV){
            total = Double.parseDouble(imperialToMetric(String.valueOf(total)));
        }

        return total;
    }

    private void setToLbs(){
        squatUnit.setText(" lbs");
        benchPressUnit.setText(" lbs");
        deadliftUnit.setText(" lbs");
        bodyWeightUnit.setText(" lbs");
    }

    private void setToKgs(){
        squatUnit.setText(" kgs");
        benchPressUnit.setText(" kgs");
        deadliftUnit.setText(" kgs");
        bodyWeightUnit.setText(" kgs");
    }

    private void checkForMaxes(){
        DatabaseReference maxRef = FirebaseDatabase.getInstance().getReference().child("maxes").child(uid);
        maxRef.child("Squat (Barbell - Back)").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass = dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    if(modelClass.isIsImperial() != isImperialPOV){
                        if(modelClass.isIsImperial() && !isImperialPOV){
                            // imperial to metric
                            String val = imperialToMetric(modelClass.getMaxValue());
                            squatEditText.setText(val);
                        }else if(!modelClass.isIsImperial() && isImperialPOV){
                            // metric to imperial
                            String val = metricToImperial(modelClass.getMaxValue());
                            squatEditText.setText(val);
                        }
                    }else{
                        squatEditText.setText(modelClass.getMaxValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        maxRef.child("Bench Press (Barbell - Flat)").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass = dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    if(modelClass.isIsImperial() != isImperialPOV){
                        if(modelClass.isIsImperial() && !isImperialPOV){
                            // imperial to metric
                            String val = imperialToMetric(modelClass.getMaxValue());
                            benchPressEditText.setText(val);
                        }else if(!modelClass.isIsImperial() && isImperialPOV){
                            // metric to imperial
                            String val = metricToImperial(modelClass.getMaxValue());
                            benchPressEditText.setText(val);
                        }
                    }else{
                        benchPressEditText.setText(modelClass.getMaxValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        maxRef.child("Deadlift (Barbell - Conventional)").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass = dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    if(modelClass.isIsImperial() != isImperialPOV){
                        if(modelClass.isIsImperial() && !isImperialPOV){
                            // imperial to metric
                            String val = imperialToMetric(modelClass.getMaxValue());
                            deadliftEditText.setText(val);
                        }else if(!modelClass.isIsImperial() && isImperialPOV){
                            // metric to imperial
                            String val = metricToImperial(modelClass.getMaxValue());
                            deadliftEditText.setText(val);
                        }
                    }else{
                        deadliftEditText.setText(modelClass.getMaxValue());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String metricToImperial(String input){

        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        String newString = String.valueOf(lbsInt);

        return newString;
    }

    private String imperialToMetric(String input){

        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        String newString = String.valueOf(kgInt);

        return newString;
    }
}
