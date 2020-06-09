package com.liftdom.workout_programs.Smolov;


import android.text.InputFilter;
import android.widget.*;
import androidx.annotation.NonNull;
import com.liftdom.helper_classes.DigitsInputFilter;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.template_editor.TemplateEditorSingleton;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import io.github.dreierf.materialintroscreen.SlideFragment;
import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovIntroFrag2 extends SlideFragment {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public SmolovIntroFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.exerciseButton) Button exerciseButton;
    @BindView(R.id.todayRadioButton) RadioButton todayRadioButton;
    @BindView(R.id.mondayRadioButton) RadioButton mondayRadioButton;
    @BindView(R.id.maxWeightEditText) EditText maxWeightEditText;
    @BindView(R.id.unitsView) TextView unitsView;
    @BindView(R.id.takeOff10) CheckBox takeOff10Checkbox;
    @BindView(R.id.roundToNearest5) CheckBox roundToNearest5;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_intro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        exerciseButton.setText("Squat (Barbell - Back)");

        todayRadioButton.setChecked(true);
        takeOff10Checkbox.setChecked(true);
        roundToNearest5.setChecked(true);

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                int exID = exerciseButton.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });

        DatabaseReference userRef =
                FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                if(!userModelClass.isIsImperial()){
                    unitsView.setText("kgs");
                    SmolovSetupSingleton.getInstance().isImperial = false;
                    setFromMax(exerciseButton.getText().toString());
                    maxWeightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
                }else{
                    unitsView.setText("lbs");
                    SmolovSetupSingleton.getInstance().isImperial = true;
                    setFromMax(exerciseButton.getText().toString());
                    maxWeightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void setFromMax(String exerciseName){
        DatabaseReference maxRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(exerciseName);
        maxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    ExerciseMaxesModelClass modelClass =
                            dataSnapshot.getValue(ExerciseMaxesModelClass.class);
                    try{
                        convertUnitsAndPost(modelClass.getMaxValue(), modelClass.isIsImperial());
                    }catch (NullPointerException e){

                    }
                }else{
                    maxWeightEditText.setText("");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void convertUnitsAndPost(String maxValue, boolean maxImperial){
        /*
         * We need the units of the max, and the units we currently are.
         */

        boolean currentPOV = SmolovSetupSingleton.getInstance().isImperial;

        // maxImperial = what the max is
        // currentPOV = what the user currently is (this will be what the template is saved as)

        if(maxImperial == currentPOV){
            maxWeightEditText.setText(maxValue);
        }else{
            if(maxImperial && !currentPOV){
                // convert from imperial to metric
                // max is imperial, POV is kg
                String val = imperialToMetric(maxValue);
                maxWeightEditText.setText(val);
            }else if(!maxImperial && currentPOV){
                // convert from metric to imperial
                // max is kg, POV is imperial
                String val = metricToImperial(maxValue);
                maxWeightEditText.setText(val);
            }
        }

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if (requestCode == 2) {
                if (data.getStringExtra("MESSAGE") != null) {
                    String message = data.getStringExtra("MESSAGE");
                    //String newMessage = newLineExname(message);
                    if (isBodyweight(message)) {
                        Snackbar.make(getView(), "Bodyweight exercise not allowed for Smolov", Snackbar.LENGTH_SHORT).show();
                    } else {
                        setFromMax(message);
                        exerciseButton.setText(message);
                    }
                }
            }
        }
    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!maxWeightEditText.getText().toString().equals("") && !maxWeightEditText.getText().toString().equals(" ")){
            SmolovSetupSingleton.getInstance().isBeginToday = todayRadioButton.isChecked();
            SmolovSetupSingleton.getInstance().exName = exerciseButton.getText().toString();
            SmolovSetupSingleton.getInstance().isTakeOff10 = takeOff10Checkbox.isChecked();
            SmolovSetupSingleton.getInstance().isRoundToNearest5 = roundToNearest5.isChecked();
            if(takeOff10Checkbox.isChecked()){
                double weight1 = Double.parseDouble(maxWeightEditText.getText().toString());
                weight1 = weight1 * .9;
                weight1 = 5 * (Math.round(weight1/5));
                int weight2 = (int) weight1;
                SmolovSetupSingleton.getInstance().maxWeight = String.valueOf(weight2);
            }else{
                SmolovSetupSingleton.getInstance().maxWeight = maxWeightEditText.getText().toString();
            }
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
