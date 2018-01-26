package com.liftdom.workout_assistor;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.ExtraOptionsDialog;
import com.liftdom.template_editor.SetsLevelChildFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightWAFrag extends android.app.Fragment {


    public RepsWeightWAFrag() {
        // Required empty public constructor
    }

    String repsWeightString = "false";
    String fragTag1;
    public boolean isTemplateImperial;
    public boolean isUserImperial;
    boolean isEdit = false;


    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private removeFragCallback removeFrag;

    public interface updateStateCallback{
        void updateWorkoutState();
    }

    private updateStateCallback updateWorkoutState;

    // Butterknife
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.unit) TextView unitView;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.holderView) LinearLayout holderView;
    @BindView(R.id.checkBox) CheckBox checkBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight_wa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();
        updateWorkoutState = (updateStateCallback) getParentFragment();

        Log.i("deadInfo", "repsWeightString: " + repsWeightString);

        if(isUserImperial){
            unitView.setText("lbs");
        }else{
            unitView.setText("kgs");
        }

        if(repsWeightString.equals("false")){
            //Intent intent = new Intent(getActivity(), MainActivity.class);
            //intent.putExtra("fragID",  0);
            //startActivity(intent);
        }else{
            if(isEdit){
                String delims = "[@,_]";
                String[] tokens = repsWeightString.split(delims);

                if(tokens[0].equals("T.F.")){
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    repsEditText.setFilters(filterArray);
                    repsEditText.setText("T.F.");
                    repsEditText.setEnabled(false);
                }else{
                    repsEditText.setText(tokens[0]);
                    repsEditText.setEnabled(true);
                }

                if(tokens[1].equals("B.W.")){
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    weightEditText.setFilters(filterArray);
                    unitView.setVisibility(View.GONE);
                    weightEditText.setText(tokens[1]);
                    weightEditText.setEnabled(false);
                }else{
                    // normal
                    weightEditText.setText(convertUnitsToUser(tokens[1]));
                    weightEditText.setEnabled(true);
                }

                if(tokens[2].equals("checked")){
                    checkBox.setChecked(true);
                    holderView.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    checkBox.setChecked(false);
                }
            }else{
                String[] tokens = repsWeightString.split("@");

                if(repsWeightString.equals("10@B.W.")){

                }

                Log.i("deadInfo", "tokens[0] = " + tokens[0]);

                if(tokens[0].equals("T.F.")){
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    repsEditText.setFilters(filterArray);
                    repsEditText.setText("T.F.");
                    repsEditText.setEnabled(false);
                }else if(tokens[0].equals(" ")){
                    repsEditText.setText("");
                    repsEditText.setEnabled(true);
                }else{
                    repsEditText.setText(tokens[0]);
                    repsEditText.setEnabled(true);
                }

                if(tokens[1].equals("B.W.")){
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    weightEditText.setFilters(filterArray);
                    unitView.setVisibility(View.GONE);
                    weightEditText.setText(tokens[1]);
                    weightEditText.setEnabled(false);
                }else if(tokens[1].equals(" ")){
                    weightEditText.setText("");
                    weightEditText.setEnabled(true);
                }
                else{
                    // normal
                    weightEditText.setText(convertUnitsToUser(tokens[1]));
                    weightEditText.setEnabled(true);
                }
            }
        }


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holderView.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    holderView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
                updateWorkoutState.updateWorkoutState();
            }
        });

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFrag.removeFrag(fragTag1);
            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsDialog.class);
                String weightText = weightEditText.getText().toString();
                String repsText = repsEditText.getText().toString();
                intent.putExtra("repsText", repsText);
                intent.putExtra("weightText", weightText);
                startActivityForResult(intent, 3);
            }
        });

        return view;
    }

    private String convertUnitsToUser(String unConverted){
        String converted;

        if(isUserImperial && !isTemplateImperial){
            // user is lbs, template is kgs
            converted = metricToImperial(unConverted);
        }else if(!isUserImperial && isTemplateImperial){
            // user is kgs, template is lbs
            converted = imperialToMetric(unConverted);
        }else{
            converted = unConverted;
        }

        return converted;
    }

    private String convertUnitsBackToTemplate(String unConverted){
        String converted;

        if(isUserImperial && !isTemplateImperial){
            // user is lbs, template is kgs
            converted = imperialToMetric(unConverted);
        }else if(!isUserImperial && isTemplateImperial){
            // user is kgs, template is lbs
            converted = metricToImperial(unConverted);
        }else{
            converted = unConverted;
        }

        return converted;
    }

    private String metricToImperial(String input){
        String delims = "[@]";
        String[] tokens = input.split(delims);
        if(tokens[1].equals("B.W.")){
            return input;
        }else{
            double lbsDouble = Double.parseDouble(tokens[1]) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            String newString = tokens[0] + "@" + String.valueOf(lbsInt);
            return newString;
        }
    }

    private String imperialToMetric(String input){
        String delims = "[@]";
        String[] tokens = input.split(delims);
        if(tokens[1].equals("B.W.")){
            return input;
        }else{
            double kgDouble = Double.parseDouble(tokens[1]) / 2.2046;
            int kgInt = (int) Math.round(kgDouble);
            String newString = tokens[0] + "@" + String.valueOf(kgInt);
            return newString;
        }
    }

    @Override
    public void onResume(){
        // Are we going to have to go through and check for every frag? Maybe...
        // or we'll have to do the saveInstanceState thing.
        if(repsWeightString.equals("false")){
            Log.i("deadInfo", "repsWeightString is false (onResume)");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("fragID",  0);
            startActivity(intent);
            super.onResume();
        }else{
            Log.i("deadInfo", "RepsWeightWAFrag (onResume)");
            super.onResume();
        }
    }

    public String getInfo(){

        String repsText = repsEditText.getText().toString();
        String weightText = weightEditText.getText().toString();

        if(repsText.isEmpty()){
            repsText = "0";
        }

        if(weightText.isEmpty()){
            weightText = "0";
        }else{
            weightText = convertUnitsBackToTemplate(weightText);
        }

        String info = repsText + "@" + weightText;
        if(checkBox.isChecked()){
            info = info + "_checked";
        }else{
            info = info + "_unchecked";
        }

        return info;
    }

    boolean isExerciseName(String input) {
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(requestCode == 3){
            if(data != null){
                if(data.getStringExtra("MESSAGE1") != null) {
                    String message = data.getStringExtra("MESSAGE1");
                    if(message.equals("bodyweight")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        weightEditText.setText("B.W.");
                        unitView.setVisibility(View.GONE);
                        weightEditText.setEnabled(false);
                    } else if(message.equals("defaultWeight")){
                        if(!isNumber(weightEditText.getText().toString())){
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(3);
                            weightEditText.setFilters(filterArray);
                            weightEditText.setText("");
                            weightEditText.setEnabled(true);
                            weightEditText.setHint("W");
                            unitView.setVisibility(View.VISIBLE);
                            weightEditText.setEnabled(true);
                        }
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null) {
                    String message = data.getStringExtra("MESSAGE2");
                    if(message.equals("to failure")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        repsEditText.setFilters(filterArray);
                        repsEditText.setText("T.F.");
                        repsEditText.setEnabled(false);
                    } else if(message.equals("defaultReps")){
                        if(!isNumber(repsEditText.getText().toString())){
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(2);
                            repsEditText.setFilters(filterArray);
                            repsEditText.setText("");
                            repsEditText.setEnabled(true);
                            repsEditText.setHint("R");
                        }
                    }
                }
            }
        }
    }

    boolean isNumber(String input){
        boolean isNumber = false;

        String inputWithout = input.replaceAll("\\s+","");

        if(inputWithout.equals("")){
            isNumber = true;
        } else{
            try {
                int num = Integer.parseInt(input);
                Log.i("",num+" is a number");
                isNumber = true;
            } catch (NumberFormatException e) {
                isNumber = false;
            }
        }

        return isNumber;
    }

}
