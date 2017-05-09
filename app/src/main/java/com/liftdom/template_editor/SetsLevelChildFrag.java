package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import com.liftdom.template_editor.EditTemplateAssemblerClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelChildFrag extends android.app.Fragment {

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    //@BindView(R.id.extraOptions) Spinner extraOptions;
    @BindView(R.id.lbs) TextView pounds;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

    public SetsLevelChildFrag() {
        // Required empty public constructor
    }

    Boolean isEdit = false;
    String setSchemeEdited;
    Boolean isEditFirstEdit = false;


    // Callback
    public interface setSchemesCallback{
        String getExerciseValue();
        String getDoWValue();
        Boolean getCheckBoxValue();
    }

    private setSchemesCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();

        String delims = "[x,@]";

        if(isEdit){
            String[] setSchemesEachArray = setSchemeEdited.split(delims);

            // sets
            String setsWithSpaces = setSchemesEachArray[0];
            String setsWithoutSpaces = setsWithSpaces.replaceAll("\\s+","");
            //int setsInt = Integer.parseInt(setsWithoutSpaces);
            setsEditText.setText(setsWithoutSpaces);

            // reps
            String repsWithSpaces = setSchemesEachArray[1];
            String repsWithout = repsWithSpaces.replaceAll("\\s+","");
            repsEditText.setText(repsWithout);

            String weightWithSpaces = setSchemesEachArray[2];
            String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
            weightEditText.setText(weightWithoutSpaces);

        }


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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(requestCode == 3)
        {
            if(data.getStringExtra("MESSAGE1") != null && data != null ) {
                String message = data.getStringExtra("MESSAGE1");
                if(message.equals("bodyweight")){
                    weightEditText.setText("B.W.");
                    pounds.setVisibility(View.GONE);
                    weightEditText.setEnabled(false);
                } else if(message.equals("defaultWeight")){
                    if(!isNumber(weightEditText.getText().toString())){
                        weightEditText.setText("");
                        weightEditText.setEnabled(true);
                        weightEditText.setHint("W");
                        pounds.setVisibility(View.VISIBLE);
                        weightEditText.setEnabled(true);
                    }
                }
            }
            if(data.getStringExtra("MESSAGE2") != null && data != null ) {
                String message = data.getStringExtra("MESSAGE2");
                if(message.equals("to failure")){
                    repsEditText.setText("T.F.");
                    repsEditText.setEnabled(false);
                } else if(message.equals("defaultReps")){
                    if(!isNumber(repsEditText.getText().toString())){
                        repsEditText.setText("");
                        repsEditText.setEnabled(true);
                        repsEditText.setHint("R");
                    }
                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        if(!callback.getExerciseValue().equals("Click to add exercise") && EditTemplateAssemblerClass.getInstance().isOnSaveClick) {
            String parentSpinnerValue = callback.getExerciseValue();
            String doWSelected = callback.getDoWValue();
            Boolean isChecked = callback.getCheckBoxValue();
            EditTemplateAssemblerClass.getInstance().setDoW(doWSelected);
            EditTemplateAssemblerClass.getInstance().setExerciseValue(parentSpinnerValue, doWSelected, isChecked);

            String setsString = setsEditText.getText().toString();
            String repsString = repsEditText.getText().toString();
            String weightString = weightEditText.getText().toString();

            String value = null;

            if (!setsString.equals("") && !repsString.equals("") && !weightString.equals("")) {
                value = setsString + " x " + repsString + " @ " +
                        weightString;
            }

            if (value != null && EditTemplateAssemblerClass.getInstance().isOnSaveClick) {
                EditTemplateAssemblerClass.getInstance().setSetSchemeValue(value, parentSpinnerValue, doWSelected);
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
