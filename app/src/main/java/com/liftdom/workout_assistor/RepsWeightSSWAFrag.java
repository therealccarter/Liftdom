package com.liftdom.workout_assistor;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.Fragment;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.charts_stats_tools.DigitsInputFilter;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.ExtraOptionsDialog;
import com.liftdom.template_editor.InputFilterMinMax;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightSSWAFrag extends android.app.Fragment {


    public RepsWeightSSWAFrag() {
        // Required empty public constructor
    }

    String repsWeightString = "false";
    String tag;
    boolean inflateBottomView = false;
    boolean isEdit = false;
    public boolean isTemplateImperial;
    public boolean isUserImperial;

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private removeFragCallback removeFrag;

    public interface updateStateCallback{
        void updateWorkoutState();
    }

    public interface updateWorkoutStateFastCallback{
        void updateWorkoutStateFast();
        //void updateWorkoutState();
    }

    private updateStateCallback updateWorkoutState;
    private updateWorkoutStateFastCallback updateWorkoutStateFast;

    // Butterknife
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.unit) TextView unitView;
    @BindView(R.id.at) TextView atView;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.holderView) LinearLayout holderView;
    @BindView(R.id.checkedImage) ImageView checkedImage;
    @BindView(R.id.unCheckedImage) ImageView unCheckedImage;
    @BindView(R.id.bottomView) View bottomView;
    @BindView(R.id.amrap) TextView amrap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight_sswa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();
        updateWorkoutState = (updateStateCallback) getParentFragment();
        updateWorkoutStateFast = (updateWorkoutStateFastCallback) getParentFragment();

        if(isUserImperial){
            unitView.setText("lbs");
            weightEditText.setText("");
            weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
        }else{
            unitView.setText("kgs");
            weightEditText.setText("");
            weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
        }

        if(repsWeightString.equals("false")){

        }else{
            if(isEdit){
                String delims = "[@,_]";
                String[] tokens = repsWeightString.split(delims);

                boolean amrapBool = false;

                if(isAmrap(repsWeightString)){
                    setAmrap(repsWeightString);
                    amrapBool = true;
                }else{
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
                }


                if(isPercentage(repsWeightString)){
                    if(amrapBool){
                        // weightEditText.setText(tokens[2]);
                        String converted = convertUnitsToUser(tokens[2]);
                        String formatted = formatPercentageWeight(converted);
                        weightEditText.setText(formatted);
                        weightEditText.setEnabled(true);
                    }else{
                        //weightEditText.setText(tokens[1]);
                        String converted = convertUnitsToUser(tokens[1]);
                        String formatted = formatPercentageWeight(converted);
                        weightEditText.setText(formatted);
                        weightEditText.setEnabled(true);
                    }
                }else {
                    if (tokens[1].equals("B.W.")) {
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        unitView.setVisibility(View.GONE);
                        if(amrapBool){
                            weightEditText.setText(tokens[2]);
                        }else{
                            weightEditText.setText(tokens[1]);
                        }
                        weightEditText.setEnabled(false);
                        changeRepsIMEtoFinish();
                    } else {
                        if(amrapBool){
                            weightEditText.setText(convertUnitsToUser(tokens[2]));
                        }else{
                            weightEditText.setText(convertUnitsToUser(tokens[1]));
                        }
                        weightEditText.setEnabled(true);
                    }
                }

                if(amrapBool){
                    if(tokens[3].equals("checked")){
                        setCheckedView();
                        //loadingView.setVisibility(View.INVISIBLE);
                    }else{
                        setUnCheckedView();
                        //loadingView.setVisibility(View.INVISIBLE);
                    }
                }else{
                    if(tokens[2].equals("checked")){
                        setCheckedView();
                        //loadingView.setVisibility(View.INVISIBLE);
                    }else{
                        setUnCheckedView();
                        //loadingView.setVisibility(View.INVISIBLE);
                    }
                }

            }else{

                setUnCheckedView();

                String[] tokens = repsWeightString.split("@");

                boolean amrapBool = false;

                if(isAmrap(repsWeightString)){
                    setAmrap(repsWeightString);
                    amrapBool = true;
                }else{
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
                }


                if(isPercentage(repsWeightString)){
                    String converted = convertUnitsToUser(tokens[1]);
                    String formatted = formatPercentageWeight(converted);
                    weightEditText.setText(formatted);
                    weightEditText.setEnabled(true);
                }else{
                    if (tokens[1].equals("B.W.")) {
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        unitView.setVisibility(View.GONE);
                        weightEditText.setText(tokens[1]);
                        weightEditText.setEnabled(false);
                        changeRepsIMEtoFinish();
                    } else {
                        weightEditText.setText(convertUnitsToUser(tokens[1]));
                        weightEditText.setEnabled(true);
                    }
                }
            }
        }


        if(inflateBottomView){
            bottomView.setVisibility(View.VISIBLE);
        }

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFrag.removeFrag(tag);
            }
        });

        repsEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    try{
                        if(repsEditText.getText().toString().equals("0")){
                            repsEditText.setText("");
                            InputMethodManager imm =
                                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }catch (NullPointerException e){

                    }
                }
            }
        });

        weightEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b){
                    try{
                        if(weightEditText.getText().toString().equals("0")){
                            weightEditText.setText("");
                            InputMethodManager imm =
                                    (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                        }
                    }catch (NullPointerException e){

                    }
                }
            }
        });

        checkedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unCheckedImage.setVisibility(View.INVISIBLE);
                //checkedImage.setVisibility(View.GONE);
                setUnCheckedView();
                //setLoadingView();
                updateWorkoutState.updateWorkoutState();
            }
        });

        unCheckedImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //unCheckedImage.setVisibility(View.GONE);
                //checkedImage.setVisibility(View.INVISIBLE);
                setCheckedView();
                //setLoadingView();
                updateWorkoutState.updateWorkoutState();
            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsDialog.class);
                String weightText = weightEditText.getText().toString();
                String repsText = repsEditText.getText().toString();
                if(amrap.getVisibility() == View.VISIBLE){
                    intent.putExtra("isAmrap", "true");
                }else{
                    intent.putExtra("isAmrap", "false");
                }
                intent.putExtra("repsText", repsText);
                intent.putExtra("weightText", weightText);
                intent.putExtra("isPercentageString", "dontShow");
                startActivityForResult(intent, 3);
            }
        });

        return view;
    }

    private void changeRepsIMEtoFinish(){
        repsEditText.setImeOptions(EditorInfo.IME_ACTION_DONE);
    }

    public void updateExName(String exName){
        if(isBodyweight(exName)){
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(4);
            weightEditText.setFilters(filterArray);
            weightEditText.setText("B.W.");
            unitView.setVisibility(View.GONE);
            weightEditText.setEnabled(false);
        }else{
            InputFilter[] filterArray = new InputFilter[1];
            filterArray[0] = new InputFilter.LengthFilter(3);
            weightEditText.setFilters(filterArray);
            //if(weightEditText.getText().toString().equals("B.W.")){
                weightEditText.setText("");
            //}
            if(isUserImperial){
                unitView.setText("lbs");
                weightEditText.setText("");
                weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
            }else{
                unitView.setText("kgs");
                weightEditText.setText("");
                weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
            }
            weightEditText.setEnabled(true);
            weightEditText.setHint("W");
            unitView.setVisibility(View.VISIBLE);
            weightEditText.setEnabled(true);
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

    public void setAmrap(String setSchemeEdited){
        String delims = "[x,@,_]";
        String[] setSchemesEachArray = setSchemeEdited.split(delims);
        String repsWithSpaces = setSchemesEachArray[0];
        String repsWithout = repsWithSpaces.replaceAll("\\s+","");

        repsEditText.setText(repsWithout);
        amrap.setVisibility(View.VISIBLE);
    }

    public boolean isAmrap(String setScheme){
        boolean amrap = false;

        String delims1 = "[x,_]";
        String[] tokens1 = setScheme.split(delims1);

        if(tokens1.length > 1){
            char c = tokens1[1].charAt(0);
            String cString = String.valueOf(c);
            if(cString.equals("a")){
                amrap = true;
            }
        }

        return amrap;
    }

    boolean isCheckedBool;

    public boolean isChecked(){
        return isCheckedBool;
        //try{
        //    return (checkedImage.getVisibility() == View.VISIBLE);
        //}catch (NullPointerException e){
        //    return false;
        //}
    }

    public void setCheckedView(){
        checkedImage.setVisibility(View.VISIBLE);
        unCheckedImage.setVisibility(View.GONE);
        holderView.setBackgroundColor(Color.parseColor("#1d1d1d"));
        isCheckedBool = true;
        setTextColors(true);
        //updateWorkoutState.updateWorkoutState();
    }

    public void setUnCheckedView(){
        checkedImage.setVisibility(View.GONE);
        unCheckedImage.setVisibility(View.VISIBLE);
        holderView.setBackgroundColor(Color.parseColor("#353535"));
        isCheckedBool = false;
        setTextColors(false);
        //updateWorkoutState.updateWorkoutState();
    }

    private void setTextColors(boolean isChecked){
        if(isChecked){
            repsEditText.setTextColor(Color.parseColor("#595959"));
            weightEditText.setTextColor(Color.parseColor("#595959"));
            unitView.setTextColor(Color.parseColor("#595959"));
            atView.setTextColor(Color.parseColor("#595959"));
            amrap.setTextColor(Color.parseColor("#595959"));
        }else{
            repsEditText.setTextColor(Color.parseColor("#ededed"));
            weightEditText.setTextColor(Color.parseColor("#ededed"));
            unitView.setTextColor(Color.parseColor("#ededed"));
            atView.setTextColor(Color.parseColor("#ededed"));
            amrap.setTextColor(Color.parseColor("#ededed"));
        }
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

            weight2 = (int) (5 * (Math.round(weight / 5)));

            formatted = String.valueOf(weight2);
        }else{
            formatted = unFormatted;
        }

        return formatted;
    }

    private String roundNumberToNearest5(String weightString){
        String rounded;

        double weight;
        int weight2;

        weight = Double.parseDouble(weightString);

        weight2 = (int) (5 * (Math.round(weight / 5)));

        rounded = String.valueOf(weight2);

        return rounded;
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

    public boolean isPercentageShort(String string){
        boolean isPercentage;

        char c = string.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            isPercentage = true;
        }else{
            isPercentage = false;
        }

        return isPercentage;
    }

    private String convertUnitsToUser(String unConverted){
        String converted;

        if(isUserImperial && !isTemplateImperial){
            // user is lbs, template is kgs
            converted = metricToImperial(unConverted);
            if(!isPercentageShort(unConverted)){
                converted = roundNumberToNearest5(converted);
            }
        }else if(!isUserImperial && isTemplateImperial){
            // user is kgs, template is lbs
            converted = imperialToMetric(unConverted);
            if(!isPercentageShort(unConverted)){
                converted = roundNumberToNearest5(converted);
            }
        }else{
            converted = unConverted;
            if(isUserImperial){
                if(!isPercentageShort(unConverted)){
                    double doubleVersion = Double.parseDouble(unConverted);
                    int intVersion = (int) doubleVersion;
                    converted = String.valueOf(intVersion);
                }
            }
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

        String weight;
        String newString;

        char c = input.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            String delims = "[_]";
            String[] tokens = input.split(delims);
            weight = tokens[tokens.length - 1];
            double lbsDouble = Double.parseDouble(weight) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            newString =
                    tokens[0] + "_" + tokens[1] + "_" + tokens[2] + "_" + String.valueOf(lbsInt);
        }else{
            weight = input;
            double lbsDouble = Double.parseDouble(weight) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            newString = String.valueOf(lbsInt);
        }

        return newString;
    }

    private String imperialToMetric(String input){

        String weight;
        String newString;

        char c = input.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            String delims = "[_]";
            String[] tokens = input.split(delims);
            weight = tokens[tokens.length - 1];
            double kgDouble = Double.parseDouble(weight) / 2.2046;
            int kgInt = (int) Math.round(kgDouble);
            newString =
                    tokens[0] + "_" + tokens[1] + "_" + tokens[2] + "_" + String.valueOf(kgInt);
        }else{
            weight = input;
            double kgDouble = Double.parseDouble(weight) / 2.2046;
            int kgInt = (int) Math.round(kgDouble);
            newString = String.valueOf(kgInt);
        }

        return newString;
    }

    public String getInfo(){
        String repsText = "";
        String weightText = "";

        if(repsEditText != null && weightEditText != null){
            repsText = repsEditText.getText().toString();
            weightText = weightEditText.getText().toString();
        }

        if(repsText.isEmpty()){
            repsText = "0";
        }

        if(weightText.isEmpty()){
            weightText = "0";
        }else{
            if(!weightText.equals("B.W.")){
                //weightText = convertUnitsBackToTemplate(weightText);
                weightText = weightText;
            }
        }

        if(amrap.getVisibility() == View.VISIBLE){
            repsText = repsText.toString() + "_a";
        }

        String info = repsText + "@" + weightText;

        try{
            if(checkedImage.getVisibility() == View.VISIBLE || checkedImage.getVisibility() == View.INVISIBLE){
                info = info + "_checked";
            }else if(unCheckedImage.getVisibility() == View.VISIBLE || unCheckedImage.getVisibility() == View.INVISIBLE){
                info = info + "_unchecked";
            }
        }catch (NullPointerException e){
            info = info + "_checked";
        }


        info = info + "_ss";

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
                        updateWorkoutStateFast.updateWorkoutStateFast();
                    }else if(message.equals("defaultWeight")){
                        if(!isNumber(weightEditText.getText().toString())){
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(3);
                            weightEditText.setFilters(filterArray);
                            if(weightEditText.getText().toString().equals("B.W.")){
                                weightEditText.setText("");
                            }
                            weightEditText.setEnabled(true);
                            weightEditText.setHint("W");
                            unitView.setVisibility(View.VISIBLE);
                            weightEditText.setEnabled(true);
                            updateWorkoutStateFast.updateWorkoutStateFast();
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
                        updateWorkoutStateFast.updateWorkoutStateFast();
                    }else if(message.equals("defaultReps")){
                        amrap.setVisibility(View.GONE);
                        if(!isNumber(repsEditText.getText().toString())){
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(2);
                            repsEditText.setFilters(filterArray);
                            repsEditText.setText("");
                            repsEditText.setEnabled(true);
                            repsEditText.setHint("R");
                            updateWorkoutStateFast.updateWorkoutStateFast();
                        }else{
                            updateWorkoutStateFast.updateWorkoutStateFast();
                        }
                    }else if(message.equals("amrap")){
                        amrap.setVisibility(View.VISIBLE);
                        if(!isNumber(repsEditText.getText().toString())){
                            InputFilter[] filterArray = new InputFilter[1];
                            filterArray[0] = new InputFilter.LengthFilter(2);
                            repsEditText.setFilters(filterArray);
                            repsEditText.setText("");
                            repsEditText.setEnabled(true);
                            repsEditText.setHint("R");
                        }else{
                            updateWorkoutStateFast.updateWorkoutStateFast();
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
        }else{
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
