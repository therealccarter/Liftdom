package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;


import android.text.InputFilter;
import android.text.InputType;
import android.widget.CheckBox;
import android.widget.RadioButton;
import androidx.annotation.NonNull;
import com.liftdom.charts_stats_tools.DigitsInputFilter;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import io.github.dreierf.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBIntroFrag2 extends SlideFragment {


    public W531fBIntroFrag2() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.squatEditText) EditText squatEditText;
    @BindView(R.id.unitsView1) TextView unitsView1;
    @BindView(R.id.benchPressEditText) EditText benchPressEditText;
    @BindView(R.id.unitsView2) TextView unitsView2;
    @BindView(R.id.deadliftEditText) EditText deadliftEditText;
    @BindView(R.id.unitsView3) TextView unitsView3;
    @BindView(R.id.ohpEditText) EditText ohpEditText;
    @BindView(R.id.unitsView4) TextView unitsView4;
    @BindView(R.id.warmupCheckbox) CheckBox warmupCheckbox;
    @BindView(R.id.todayRadioButton) RadioButton todayRadioButton;
    @BindView(R.id.mondayRadioButton) RadioButton mondayRadioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_w531f_bintro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        todayRadioButton.setChecked(true);
        //roundToNearest5.setChecked(true);

        /*
         * I'd feel best about this program if it is in normal formatting.
         * So the user can edit to their heart's content.
         * Orrr for each premade we can have a bespoke editor that allows modification of
         * exercises. Won't be viable for every program though.
         */

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                W531fBSingleton.getInstance().isImperial = userModelClass.isIsImperial();
                setUnitInfo(userModelClass.isIsImperial());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setUnitInfo(boolean isImperial){
        if(isImperial){
            checkMaxes(isImperial);
            unitsView1.setText("lbs");
            unitsView2.setText("lbs");
            unitsView3.setText("lbs");
            unitsView4.setText("lbs");

            squatEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            squatEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});

            benchPressEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            benchPressEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});

            deadliftEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            deadliftEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});

            ohpEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            ohpEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
        }else{
            checkMaxes(isImperial);
            unitsView1.setText("kgs");
            unitsView2.setText("kgs");
            unitsView3.setText("kgs");
            unitsView4.setText("kgs");

            squatEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            squatEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});

            benchPressEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            benchPressEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});

            deadliftEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            deadliftEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});

            ohpEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            ohpEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
        }
    }

    private String convert(boolean isImperialPOV, boolean isValueImperial, String oldValue){
        String converted;

        if(isImperialPOV && !isValueImperial){
            // pov is lbs, value is kgs
            // convert from kgs to lbs
            converted = metricToImperial(oldValue);
        }else if(!isImperialPOV && isValueImperial){
            // pov is kgs, value is lbs
            // convert from lbs to kgs
            converted = imperialToMetric(oldValue);
        }else{
            converted = oldValue;
        }

        return converted;
    }

    private String metricToImperial(String input){
        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        return String.valueOf(lbsInt);
    }

    private String imperialToMetric(String input){
        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        return String.valueOf(kgInt);
    }

    private void checkMaxes(boolean isImperialPOV){
        DatabaseReference squatRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(
                        "Squat (Barbell - Back)");
        squatRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass =
                            dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    try{
                        squatEditText.setText(convert(isImperialPOV, modelClass.isIsImperial(),
                                modelClass.getMaxValue()));
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference benchRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(
                        "Bench Press (Barbell - Flat)");
        benchRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass =
                            dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    try{
                        benchPressEditText.setText(convert(isImperialPOV, modelClass.isIsImperial(),
                                modelClass.getMaxValue()));
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference deadliftRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(
                        "Deadlift (Barbell - Conventional)");
        deadliftRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass =
                            dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    try{
                        deadliftEditText.setText(convert(isImperialPOV, modelClass.isIsImperial(),
                                modelClass.getMaxValue()));
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference ohpRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(
                        "Overhead Press (Barbell)");
        ohpRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass =
                            dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    try{
                        ohpEditText.setText(convert(isImperialPOV, modelClass.isIsImperial(),
                                modelClass.getMaxValue()));
                    }catch (NullPointerException e){

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!squatEditText.getText().toString().isEmpty()
                && !benchPressEditText.getText().toString().isEmpty()
                && !deadliftEditText.getText().toString().isEmpty()
                && !ohpEditText.getText().toString().isEmpty()){
            W531fBSingleton.getInstance().squatMax = squatEditText.getText().toString();
            W531fBSingleton.getInstance().benchMax = benchPressEditText.getText().toString();
            W531fBSingleton.getInstance().deadliftMax = deadliftEditText.getText().toString();
            W531fBSingleton.getInstance().ohpMax = ohpEditText.getText().toString();
            //W531fBSingleton.getInstance().isRoundToNearest5 = roundToNearest5.isChecked();
            W531fBSingleton.getInstance().isBeginToday = todayRadioButton.isChecked();
            W531fBSingleton.getInstance().isWarmup = warmupCheckbox.isChecked();
            valuesEntered = true;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidSmolovFields);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmGreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
