package com.liftdom.template_editor;


import android.content.Intent;
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
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelChildFrag extends android.app.Fragment {


    public SetsLevelChildFrag() {
        // Required empty public constructor
    }

    Boolean isEdit = false;
    String setSchemeEdited;
    Boolean isEditFirstEdit = false;

    // Callback
    public interface setSchemesCallback{
        String getExerciseValueFormatted();
        String getDoWValue();
    }

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private setSchemesCallback callback;
    private removeFragCallback fragCallback;

    public String fragTag;

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView units;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.weightLL) LinearLayout weightLL;
    @BindView(R.id.percentageLL) LinearLayout percentageLL;
    @BindView(R.id.percentageEditText) EditText percentageEditText;
    @BindView(R.id.percentageWeightButton) Button percentageWeightButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();

        String delims = "[x,@]";

        if(TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            units.setText("lbs");
        }else{
            units.setText("kgs");
        }

        if(isEdit){
            if(setSchemeEdited.equals("")){
                setsEditText.setText("");
                repsEditText.setText("");
                weightEditText.setText("");
            }else{
                String[] setSchemesEachArray = setSchemeEdited.split(delims);

                // sets
                String setsWithSpaces = setSchemesEachArray[0];
                String setsWithoutSpaces = setsWithSpaces.replaceAll("\\s+","");
                //int setsInt = Integer.parseInt(setsWithoutSpaces);
                setsEditText.setText(setsWithoutSpaces);

                // reps
                String repsWithSpaces = setSchemesEachArray[1];
                String repsWithout = repsWithSpaces.replaceAll("\\s+","");
                if(repsWithout.equals("T.F.")){
                    InputFilter[] filterArray = new InputFilter[1];
                    filterArray[0] = new InputFilter.LengthFilter(4);
                    repsEditText.setFilters(filterArray);
                    repsEditText.setText("T.F.");
                    repsEditText.setEnabled(false);
                }else{
                    repsEditText.setText(repsWithout);
                }

                // weight
                if(isPercentage(setSchemeEdited)){
                    String delims2 = "[@]";
                    String[] tokens2 = setSchemeEdited.split(delims2);
                    String delimsP = "[_]";
                    String[] tokensP = tokens2[1].split(delimsP);
                    setWeightToPercentAndSetWeightText(tokensP[3]);
                    percentageEditText.setText(tokensP[1]);
                }else{
                    String weightWithSpaces = setSchemesEachArray[2];
                    String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
                    if(weightWithoutSpaces.equals("B.W.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        weightEditText.setText("B.W.");
                        weightEditText.setEnabled(false);
                    }else{
                        weightEditText.setText(handleUnitConversion(weightWithoutSpaces));
                    }
                }
            }

        }

        fragCallback = (removeFragCallback) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragCallback.removeFrag(fragTag);
            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsDialog.class);
                if(percentageLL.getVisibility() == View.VISIBLE){
                    intent.putExtra("isPercentageString", "true");
                }else{
                    intent.putExtra("isPercentageString", "false");
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

    public String formatPercentageWeight(String unFormatted){
        String formatted;

        String delims = "[_]";
        String[] tokens = unFormatted.split(delims);

        if(tokens[2].equals("a")){
            double weight;
            int weight2;

            double percentage = Double.parseDouble(tokens[3])/(double)100;

            weight = Double.parseDouble(tokens[1]) * percentage;

            weight2 = (int) Math.round(weight);

            formatted = String.valueOf(weight2);
        }else{
            formatted = unFormatted;
        }

        return formatted;
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
    }

    public void setWeightToDefault(){
        if(!isNumber(weightEditText.getText().toString())){
            percentageLL.setVisibility(View.GONE);
            weightLL.setVisibility(View.VISIBLE);
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(2);
            weightEditText.setFilters(filterArray);
            weightEditText.setText("");
            weightEditText.setEnabled(true);
            weightEditText.setHint("W");
            units.setVisibility(View.VISIBLE);
            weightEditText.setEnabled(true);
        }
    }

    public void setPercentageWeight(String weight){
        percentageWeightButton.setText(weight);
    }

    public void setWeightToPercentAndSetWeightText(String weight){
        weightLL.setVisibility(View.GONE);
        percentageLL.setVisibility(View.VISIBLE);
        percentageWeightButton.setText(weight);
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
                        setWeightToBW();
                    }else if(message.equals("defaultWeight")){
                        setWeightToDefault();
                    }else if(message.equals("percentage")){
                        weightLL.setVisibility(View.GONE);
                        percentageLL.setVisibility(View.VISIBLE);
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null) {
                    String message = data.getStringExtra("MESSAGE2");
                    if(message.equals("to failure")){
                        setRepsToFailure();
                    }else if(message.equals("defaultReps")){
                        setRepsToDefault();
                    }
                }
            }
        }else if(requestCode == 4){
            if(data != null){
                setPercentageWeight(data.getStringExtra("weightResult"));
            }
        }
    }

    public String getSetSchemeString(){
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
            //weightString = reHandleUnitConversion(weightEditText.getText().toString());
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

        setSchemeString = setsString + "x" + repsString + "@" + weightString;

        return setSchemeString;
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
