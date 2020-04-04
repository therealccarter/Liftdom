package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import androidx.fragment.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelSSFrag extends android.app.Fragment {


    public SetsLevelSSFrag() {
        // Required empty public constructor
    }

    boolean isEdit;
    String isEditSetScheme;

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView units;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.weightLL) LinearLayout weightLL;
    @BindView(R.id.percentageLL) LinearLayout percentageLL;
    @BindView(R.id.percentageEditText) EditText percentageEditText;
    @BindView(R.id.percentageWeightButton) Button percentageWeightButton;
    @BindView(R.id.amrap) TextView amrap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_s, container, false);

        ButterKnife.bind(this, view);

        if(TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            units.setText("lbs");
        }else{
            units.setText("kgs");
        }

        if(isEdit){
            if(isEditSetScheme.equals("")){
                setsEditText.setText("");
                repsEditText.setText("");
                weightEditText.setText("");
                setToDefaultRepsBoolean();
                setToDefaultWeightBoolean();
            }else{
                String delims = "[x,@]";
                String[] tokens = isEditSetScheme.split(delims);
                setsEditText.setText(tokens[0]);

                if(isAmrap(isEditSetScheme)){
                    setAmrap(isEditSetScheme);
                    setToAmrapBoolean();
                }else{
                    if(tokens[1].equals("T.F.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        repsEditText.setFilters(filterArray);
                        repsEditText.setText("T.F.");
                        repsEditText.setEnabled(false);
                        setToFailureBoolean();
                    }else{
                        repsEditText.setText(tokens[1]);
                        setToDefaultRepsBoolean();
                    }
                }



                // weight
                if(isPercentage(isEditSetScheme)){
                    String delims2 = "[@]";
                    String[] tokens2 = isEditSetScheme.split(delims2);
                    String delimsP = "[_]";
                    String[] tokensP = tokens2[1].split(delimsP);
                    setWeightToPercentAndSetWeightText(tokensP[3]);
                    percentageEditText.setText(tokensP[1]);
                    setToPercentageBoolean();
                }else{
                    String weightWithSpaces = tokens[2];
                    String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
                    if(weightWithoutSpaces.equals("B.W.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        weightEditText.setText("B.W.");
                        weightEditText.setEnabled(false);
                        setToBWBoolean();
                    }else{
                        weightEditText.setText(handleUnitConversion(weightWithoutSpaces));
                        setToDefaultWeightBoolean();
                    }
                }
            }
        }else{
            if(isBW){
                setWeightToBW();
            }else if(isPercentage){
                setWeightToPercentAndSetWeightText(percentageWeight);
            }

            if(isToFailure){
                setRepsToFailure();
            }else if(isAmrap){
                setAmrapEmpty();
            }
        }

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsDialog.class);
                if(percentageLL.getVisibility() == View.VISIBLE){
                    intent.putExtra("isPercentageString", "true");
                }else{
                    intent.putExtra("isPercentageString", "false");
                }
                if(amrap.getVisibility() == View.VISIBLE){
                    intent.putExtra("isAmrap", "true");
                }else{
                    intent.putExtra("isAmrap", "false");
                }
                String weightText = weightEditText.getText().toString();
                String repsText = repsEditText.getText().toString();
                intent.putExtra("repsText", repsText);
                intent.putExtra("weightText", weightText);
                startActivityForResult(intent, 3);
            }
        });

        percentageWeightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), PercentageOptionsDialog.class);
                intent.putExtra("isImperial", String.valueOf(TemplateEditorSingleton.getInstance()
                        .isCurrentUserImperial));
                intent.putExtra("currentPercentOf", percentageWeightButton.getText().toString());
                intent.putExtra("isFrom", "setsLevel");
                startActivityForResult(intent, 4);
            }
        });

        return view;
    }

    public void setAmrapEmpty(){
        amrap.setVisibility(View.VISIBLE);
    }

    void setToDefaultRepsBoolean(){
        isAmrap = false;
        isToFailure = false;
    }

    void setToFailureBoolean(){
        isToFailure = true;
        isAmrap = false;
    }

    void setToAmrapBoolean(){
        isAmrap = true;
        isToFailure = false;
    }

    void setToPercentageBoolean(){
        isBW = false;
        isPercentage = true;
        changeRepsIMEtoNext();
    }

    void setToBWBoolean(){
        isBW = true;
        isPercentage = false;
        percentageWeight = "";
        changeRepsIMEtoFinish();
    }

    void setToDefaultWeightBoolean(){
        isBW = false;
        isPercentage = false;
        percentageWeight = "";
        changeRepsIMEtoNext();
    }

    public boolean isBW = false; // weight
    public boolean isToFailure = false; // reps
    public boolean isAmrap = false; // reps
    public boolean isPercentage = false; // weight
    public String percentageWeight = ""; // weight

    public void changeRepsIMEtoFinish(){
        repsEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void changeRepsIMEtoNext(){
        repsEditText.setImeOptions(EditorInfo.IME_ACTION_NEXT);
    }

    public void setAmrap(String setSchemeEdited){
        String delims = "[x,@,_]";
        String[] setSchemesEachArray = setSchemeEdited.split(delims);
        String repsWithSpaces = setSchemesEachArray[1];
        String repsWithout = repsWithSpaces.replaceAll("\\s+","");

        repsEditText.setText(repsWithout);
        amrap.setVisibility(View.VISIBLE);
    }

    public boolean isAmrap(String setScheme){
        boolean amrap = false;

        String delims1 = "[x,_]";
        String[] tokens1 = setScheme.split(delims1);

        if(tokens1.length > 2){
            char c = tokens1[2].charAt(0);
            String cString = String.valueOf(c);
            if(cString.equals("a")){
                amrap = true;
            }
        }

        return amrap;
    }

    public boolean isPercentage(String setScheme){
        boolean percentage = false;

        String delims1 = "[@]";
        String[] tokens1 = setScheme.split(delims1);

        char c = tokens1[1].charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            percentage = true;
        }


        return percentage;
    }

    private String handleUnitConversion(String oldValue){
        String newValue;
        if(TemplateEditorSingleton.getInstance().isTemplateImperial
                && !TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is imperial, but the user is metric
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble * 0.45359237);
            newValue = String.valueOf(valueInt);
        }else if(!TemplateEditorSingleton.getInstance().isTemplateImperial
                && TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is metric, but the user is imperial
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble / 0.45359237);
            newValue = String.valueOf(valueInt);
        }else{
            newValue = oldValue;
        }
        return newValue;
    }

    private String reHandleUnitConversion(String oldValue){
        String newValue;
        if(TemplateEditorSingleton.getInstance().isTemplateImperial
                && !TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is imperial, but the user is metric
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble / 0.45359237);
            newValue = String.valueOf(valueInt);
        }else if(!TemplateEditorSingleton.getInstance().isTemplateImperial
                && TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is metric, but the user is imperial
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble * 0.45359237);
            newValue = String.valueOf(valueInt);
        }else{
            newValue = oldValue;
        }
        return newValue;
    }

    public void setRepsToFailure(){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(4);
        repsEditText.setFilters(filterArray);
        repsEditText.setText("T.F.");
        repsEditText.setEnabled(false);
        setToFailureBoolean();
    }

    public void setRepsToDefault(){
        if(!isNumber(repsEditText.getText().toString())){
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            repsEditText.setFilters(filterArray);
            repsEditText.setText("");
            repsEditText.setEnabled(true);
            repsEditText.setHint("R");
        }
        amrap.setVisibility(View.GONE);
        setToDefaultRepsBoolean();
    }

    public void setWeightToBW(){
        percentageLL.setVisibility(View.GONE);
        weightLL.setVisibility(View.VISIBLE);
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(4);
        weightEditText.setFilters(filterArray);
        weightEditText.setText("B.W.");
        units.setVisibility(View.GONE);
        weightEditText.setEnabled(false);
        changeRepsIMEtoFinish();
        setToBWBoolean();
    }

    public void setWeightToDefault(){
        if(!isNumber(weightEditText.getText().toString())){
            percentageLL.setVisibility(View.GONE);
            weightLL.setVisibility(View.VISIBLE);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(3);
            weightEditText.setFilters(filterArray);
            weightEditText.setText("");
            weightEditText.setEnabled(true);
            weightEditText.setHint("W");
            units.setVisibility(View.VISIBLE);
            weightEditText.setEnabled(true);
            setToDefaultWeightBoolean();
        }
    }

    public void setPercentageWeight(String weight){
        percentageWeightButton.setText(weight);
        setToPercentageBoolean();
    }

    public void setWeightToPercentAndSetWeightText(String weight){
        weightLL.setVisibility(View.GONE);
        percentageLL.setVisibility(View.VISIBLE);
        percentageWeightButton.setText(weight);
        setToPercentageBoolean();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(data != null){
            if(requestCode == 3){
                if(data.getStringExtra("MESSAGE1") != null && data != null ) {
                    String message = data.getStringExtra("MESSAGE1");
                    if(message.equals("bodyweight")){
                        setWeightToBW();
                        changeRepsIMEtoFinish();
                        setToBWBoolean();
                    }else if(message.equals("defaultWeight")){
                        if(!isNumber(weightEditText.getText().toString())){
                            setWeightToDefault();
                            setToDefaultWeightBoolean();
                        }
                    }else if(message.equals("percentage")){
                        weightLL.setVisibility(View.GONE);
                        percentageLL.setVisibility(View.VISIBLE);
                        setToPercentageBoolean();
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null && data != null ) {
                    String message = data.getStringExtra("MESSAGE2");
                    if(message.equals("to failure")){
                        setRepsToFailure();
                        setToFailureBoolean();
                    }else if(message.equals("defaultReps")){
                        if(!isNumber(repsEditText.getText().toString())){
                            setRepsToDefault();
                            setToDefaultRepsBoolean();
                        }
                    }else if(message.equals("amrap")){
                        amrap.setVisibility(View.VISIBLE);
                        setToAmrapBoolean();
                    }
                }
            }else if(requestCode == 4){
                if(data != null){
                    setPercentageWeight(data.getStringExtra("weightResult"));
                    percentageWeight = data.getStringExtra("weightResult");
                }
            }
        }
    }

    public String getInfoList(){
        String setSchemeString = "";
        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString;
        if(isEdit){
            if(percentageLL.getVisibility() == View.VISIBLE){
                weightString = "p_" + percentageEditText.getText().toString() + "_a_" + reHandleUnitConversion(percentageWeightButton
                        .getText().toString());
            }else{
                weightString = reHandleUnitConversion(weightEditText.getText().toString());
            }
        }else{
            if(percentageLL.getVisibility() == View.VISIBLE){
                weightString = "p_" + percentageEditText.getText().toString() + "_a_" + percentageWeightButton
                        .getText().toString();
            }else{
                weightString = weightEditText.getText().toString();
            }
        }
        if(setsString.isEmpty()){
            setsString = "0";
        }
        if(repsString.isEmpty()){
            repsString = "0";
        }
        if(weightString.isEmpty()){
            weightString = "0";
        }

        if(amrap.getVisibility() == View.VISIBLE){
            repsString = repsString.toString() + "_a";
        }

        setSchemeString = setsString + "x" + repsString + "@" + weightString;
        return setSchemeString;
    }

    boolean isNumber(String input){
        boolean isNumber = false;

        String inputWithout = input.replaceAll("\\s+","");

        //if(inputWithout.equals("")){
        //    isNumber = true;
        //} else{
            try {
                int num = Integer.parseInt(input);
                Log.i("",num+" is a number");
                isNumber = true;
            } catch (NumberFormatException e) {
                isNumber = false;
            }
        //}

        return isNumber;
    }

}
