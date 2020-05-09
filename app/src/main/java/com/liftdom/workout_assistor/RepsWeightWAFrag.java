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
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.ExtraOptionsDialog;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.template_editor.SetsLevelChildFrag;
import com.wang.avi.AVLoadingIndicatorView;

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

    public interface updateWorkoutStateFastCallback{
        void updateWorkoutStateFast();
        //void updateWorkoutState();
    }

    private updateStateCallback updateWorkoutState;
    private updateWorkoutStateFastCallback updateWorkoutStateFast;

    public interface updateStateForResultCallback{
        void updateWorkoutStateForResult(String tag);
    }

    private updateStateForResultCallback updateWorkoutStateForResult;

    private void updateWorkoutStateForResult(){
        updateWorkoutStateForResult.updateWorkoutStateForResult(fragTag1);
    }



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
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.amrap) TextView amrap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight_wa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();
        updateWorkoutState = (updateStateCallback) getParentFragment();
        updateWorkoutStateForResult = (updateStateForResultCallback) getParentFragment();
        updateWorkoutStateFast = (updateWorkoutStateFastCallback) getParentFragment();

        Log.i("deadInfo", "repsWeightString: " + repsWeightString);

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
            //Intent intent = new Intent(getActivity(), MainActivity.class);
            //intent.putExtra("fragID",  0);
            //startActivity(intent);
        }else{
            if(isEdit){
                //setJustLoadingView();
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
                }else{
                    if(tokens[1].equals("B.W.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        unitView.setVisibility(View.GONE);
                        changeRepsIMEtoFinish();
                        if(amrapBool){
                            weightEditText.setText(tokens[2]);
                        }else{
                            weightEditText.setText(tokens[1]);
                        }
                        weightEditText.setEnabled(false);
                    }else{
                        // normal
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

                if(!tokens[1].equals("B.W.")){
                    if(isBodyweight(exName)){
                        setBW();
                        changeRepsIMEtoFinish();
                    }else{
                        setNotBW();
                    }
                }

                /*
                 * Next up is the superset version. Also maybe do something about extra sets in
                 * parent giving supersets empty shit. idk.
                 */


            }else{

                setUnCheckedView();
                //loadingView.setVisibility(View.INVISIBLE);

                String[] tokens = repsWeightString.split("@");

                if(repsWeightString.equals("10@B.W.")){

                }

                Log.i("deadInfo", "tokens[0] = " + tokens[0]);

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
                    }else if(tokens[0].equals(" ")){
                        repsEditText.setText("");
                        repsEditText.setEnabled(true);
                    }else{
                        repsEditText.setText(tokens[0]);
                        repsEditText.setEnabled(true);
                    }
                }

                if(isPercentage(repsWeightString)){
                    if(amrapBool){
                        //weightEditText.setText(tokens[2]);
                        String converted = convertUnitsToUser(tokens[1]);
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
                }else{
                    if(tokens[1].equals("B.W.")){
                        InputFilter[] filterArray = new InputFilter[1];
                        filterArray[0] = new InputFilter.LengthFilter(4);
                        weightEditText.setFilters(filterArray);
                        unitView.setVisibility(View.GONE);
                        changeRepsIMEtoFinish();
                        if(amrapBool){
                            weightEditText.setText(tokens[1]);
                        }else{
                            weightEditText.setText(tokens[1]);
                        }
                        weightEditText.setEnabled(false);
                    }else{
                        if(amrapBool){
                            if(tokens[1].equals(" ")){
                                weightEditText.setText("");
                                weightEditText.setEnabled(true);
                            }else{
                                weightEditText.setText(convertUnitsToUser(tokens[1]));
                                weightEditText.setEnabled(true);
                            }
                        }else{
                            if(tokens[1].equals(" ")){
                                weightEditText.setText("");
                                weightEditText.setEnabled(true);
                            }else{
                                // normal
                                weightEditText.setText(convertUnitsToUser(tokens[1]));
                                weightEditText.setEnabled(true);
                            }
                        }

                    }
                }

                if(!tokens[1].equals("B.W.")){
                    if(isBodyweight(exName)){
                        setBW();
                        changeRepsIMEtoFinish();
                    }else{
                        setNotBW();
                    }
                }
            }
        }

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
        //updateWorkoutState.updateWorkoutState();
        holderView.setBackgroundColor(Color.parseColor("#1d1d1d"));
        isCheckedBool = true;
        setTextColors(true);
    }

    public void setUnCheckedView(){
        checkedImage.setVisibility(View.GONE);
        unCheckedImage.setVisibility(View.VISIBLE);
        //updateWorkoutState.updateWorkoutState();
        holderView.setBackgroundColor(Color.parseColor("#454545"));
        isCheckedBool = false;
        setTextColors(false);
    }

    public void setLoadingView(){
        loadingView.setVisibility(View.VISIBLE);
    }

    public void setJustLoadingView(){
        checkedImage.setVisibility(View.GONE);
        unCheckedImage.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
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

    public boolean isPercentage(String setScheme){
        boolean percentage = false;

        try{
            String delims1 = "[@]";
            String[] tokens1 = setScheme.split(delims1);

            char c = tokens1[1].charAt(0);
            String cString = String.valueOf(c);
            if(cString.equals("p")){
                percentage = true;
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.i("error", "out of bounds repsWA");
        }


        return percentage;
    }

    private String convertUnitsToUser(String unConverted){
        String converted; // p_85_a_345

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

    public String exName = "";

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

    public String getWeight(){
        String weightText = weightEditText.getText().toString();

        if(weightText.isEmpty()){
            weightText = "0";
        }else{
            if(!weightText.equals("B.W.")){
                //weightText = convertUnitsBackToTemplate(weightText);
                weightText = weightText;
            }else{
                weightText = "0";
            }
        }

        return weightText;
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
            if(!weightText.equals("B.W.")){
                //weightText = convertUnitsBackToTemplate(weightText);
                weightText = weightText;
            }
        }

        if(amrap.getVisibility() == View.VISIBLE){
            repsText = repsText.toString() + "_a";
        }

        String info = repsText + "@" + weightText;

        if(checkedImage.getVisibility() == View.VISIBLE || checkedImage.getVisibility() == View.INVISIBLE){
            info = info + "_checked";
        }else if(unCheckedImage.getVisibility() == View.VISIBLE || unCheckedImage.getVisibility() == View.INVISIBLE){
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

    public void setBW(){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(4);
        weightEditText.setFilters(filterArray);
        weightEditText.setText("B.W.");
        unitView.setVisibility(View.GONE);
        weightEditText.setEnabled(false);
    }

    public void setNotBW(){
        InputFilter[] filterArray = new InputFilter[1];
        filterArray[0] = new InputFilter.LengthFilter(3);
        weightEditText.setFilters(filterArray);
        if(weightEditText.getText().toString().equals("B.W.")){
            weightEditText.setText("");
        }
        if(isUserImperial){
            unitView.setText("lbs");
            weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
        }else{
            unitView.setText("kgs");
            weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
            weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
        }
        weightEditText.setEnabled(true);
        weightEditText.setHint("W");
        unitView.setVisibility(View.VISIBLE);
        weightEditText.setEnabled(true);
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
            weightEditText.setText("");
            weightEditText.setEnabled(true);
            weightEditText.setHint("W");
            unitView.setVisibility(View.VISIBLE);
            weightEditText.setEnabled(true);
        }
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
