package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
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
import com.liftdom.helper_classes.DigitsInputFilter;
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
    boolean isSupersets = false;

    // Callback
    public interface setSchemesCallback{
        String getExerciseValueFormatted();
        String getDoWValue();
    }

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    public interface updateCallback{
        void updateNode();
    }

    public interface withinCallback{
        void fromWithin();
    }

    private withinCallback fromWithinCallback;
    updateCallback mUpdate;
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
    @BindView(R.id.amrap) TextView amrap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();
        mUpdate = (updateCallback) getParentFragment();
        fromWithinCallback = (withinCallback) getParentFragment();

        String delims = "[x,@]";

        setsEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 20)});

        if(TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            units.setText("lbs");
            weightEditText.setText("");
            weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
        }else{
            units.setText("kgs");
            weightEditText.setText("");
            weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
        }

        if(isEdit){
            if(setSchemeEdited.equals("")){
                setsEditText.setText("");
                repsEditText.setText("");
                weightEditText.setText("");
                setToDefaultRepsBoolean();
                setToDefaultWeightBoolean();
            }else{
                String[] setSchemesEachArray = setSchemeEdited.split(delims);

                // sets
                String setsWithSpaces = setSchemesEachArray[0];
                String setsWithoutSpaces = setsWithSpaces.replaceAll("\\s+","");
                //int setsInt = Integer.parseInt(setsWithoutSpaces);
                if(setsWithoutSpaces.equals("0")){
                    setsEditText.setText("");
                }else{
                    setsEditText.setText(setsWithoutSpaces);
                }


                // reps
                if(isAmrap(setSchemeEdited)){
                    setAmrap(setSchemeEdited);
                    setToAmrapBoolean();
                }else{
                    String repsWithSpaces = setSchemesEachArray[1];
                    String repsWithout = repsWithSpaces.replaceAll("\\s+","");
                    if(repsWithout.equals("T.F.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        repsEditText.setFilters(filterArray);
                        repsEditText.setText("T.F.");
                        repsEditText.setEnabled(false);
                        setToFailureBoolean();
                    }else{
                        if(repsWithout.equals("0")){
                            repsEditText.setText("");
                        }else{
                            repsEditText.setText(repsWithout);
                        }
                        setToDefaultRepsBoolean();
                    }
                }


                // weight
                if(isPercentage(setSchemeEdited)){
                    String delims2 = "[@]";
                    String[] tokens2 = setSchemeEdited.split(delims2);
                    String delimsP = "[_]";
                    String[] tokensP = tokens2[1].split(delimsP);
                    setWeightToPercentAndSetWeightText(tokensP[3]);
                    percentageEditText.setText(tokensP[1]);
                    setToPercentageBoolean();
                }else{
                    String weightWithSpaces = setSchemesEachArray[2];
                    String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
                    if(weightWithoutSpaces.equals("B.W.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        weightEditText.setText("B.W.");
                        weightEditText.setEnabled(false);
                        setToBWBoolean();
                    }else{
                        if(weightWithoutSpaces.equals("0")){
                            weightEditText.setText("");
                        }else{
                            weightEditText.setText(handleUnitConversion(weightWithoutSpaces));
                        }
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

            if(!isSupersets){
                mUpdate.updateNode();
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
                fromWithinCallback.fromWithin();
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
                fromWithinCallback.fromWithin();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(requestCode == 3){
            if(data != null){
                fromWithinCallback.fromWithin();
                if(data.getStringExtra("MESSAGE1") != null) {
                    String message = data.getStringExtra("MESSAGE1");
                    if(message.equals("bodyweight")){
                        setWeightToBW();
                        changeRepsIMEtoFinish();
                        setToBWBoolean();
                    }else if(message.equals("defaultWeight")){
                        setWeightToDefault();
                        setToDefaultWeightBoolean();
                    }else if(message.equals("percentage")){
                        weightLL.setVisibility(View.GONE);
                        percentageLL.setVisibility(View.VISIBLE);
                        setToPercentageBoolean();
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null) {
                    String message = data.getStringExtra("MESSAGE2");
                    if(message.equals("to failure")){
                        setRepsToFailure();
                        setToFailureBoolean();
                    }else if(message.equals("defaultReps")){
                        setRepsToDefault();
                        setToDefaultRepsBoolean();
                    }else if(message.equals("amrap")){
                        amrap.setVisibility(View.VISIBLE);
                        setToAmrapBoolean();
                    }
                }
            }
        }else if(requestCode == 4){
            if(data != null){
                fromWithinCallback.fromWithin();
                if(data.getStringExtra("empty") == null){
                    setPercentageWeight(data.getStringExtra("weightResult"));
                    percentageWeight = data.getStringExtra("weightResult");
                }
            }
        }
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
        setToAmrapBoolean();
    }

    public void setAmrapEmpty(){
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
            if(TemplateEditorSingleton.getInstance().isCurrentUserImperial){
                units.setText("lbs");
                //weightEditText.setText("");
                weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
            }else{
                units.setText("kgs");
                //weightEditText.setText("");
                weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
            }
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
        percentageWeightButton.setText(handleUnitConversion(weight));
        setToPercentageBoolean();
    }

    public String getSetSchemeString(){
        String setSchemeString = "";
        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString;
        if(isEdit){
            if(percentageLL.getVisibility() == View.VISIBLE){
                //weightString =
                //        "p_" + percentageEditText.getText().toString() + "_a_" +
                //        reHandleUnitConversion(percentageWeightButton
                //        .getText().toString());
                String percentage = percentageEditText.getText().toString();
                String percentageWeight = percentageWeightButton.getText().toString();

                if(percentage.isEmpty()){
                    percentage = "0";
                }

                if(percentageWeight.isEmpty()){
                    percentageWeight = "0";
                }

                weightString = "p_" + percentage + "_a_" + percentageWeight;
            }else{
                //weightString = reHandleUnitConversion(weightEditText.getText().toString());
                weightString = weightEditText.getText().toString();
            }
            //weightString = reHandleUnitConversion(weightEditText.getText().toString());
        }else{
            if(percentageLL.getVisibility() == View.VISIBLE){

                String percentage = percentageEditText.getText().toString();
                String percentageWeight = percentageWeightButton.getText().toString();

                if(percentage.isEmpty()){
                    percentage = "0";
                }

                if(percentageWeight.isEmpty()){
                    percentageWeight = "0";
                }

                weightString = "p_" + percentage + "_a_" + percentageWeight;
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
