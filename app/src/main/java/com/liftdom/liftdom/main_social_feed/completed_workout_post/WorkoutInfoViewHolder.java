package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.graphics.Color;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

/**
 * Created by Brodin on 6/29/2017.
 */

public class WorkoutInfoViewHolder extends RecyclerView.ViewHolder{

    //private final LinearLayout mExNameLL;
    //private final LinearLayout mExNameSSLL;
    //private final LinearLayout mSetSchemeLL;
    //private final LinearLayout mSetSchemeSSLL;
    private final TextView mExNameTextView;
    private final View mExNameView1;
    private final View mExNameView2;
    private final View mExNameView3;
    private final View mExNameView4;
    private final TextView mSetSchemeTextView;
    private final View mSetSchemeView1;
    private final View mSetSchemeView2;
    private final ConstraintLayout mParentLayout;
    private String mInfoString;
    private boolean isImperialPOV;
    private boolean isOriginallyImperial;

    public WorkoutInfoViewHolder(View itemView){
        super(itemView);
        //mExNameLL = (LinearLayout) itemView.findViewById(R.id.exNameLL);
        //mExNameSSLL = (LinearLayout) itemView.findViewById(R.id.exNameSSLL);
        //mSetSchemeLL = (LinearLayout) itemView.findViewById(R.id.setSchemeLL);
        //mSetSchemeSSLL = (LinearLayout) itemView.findViewById(R.id.setSchemeSSLL);
        mExNameTextView = (TextView) itemView.findViewById(R.id.exNameTextView);
        mExNameView1 = (View) itemView.findViewById(R.id.exNameView1);
        mExNameView2 = (View) itemView.findViewById(R.id.exNameView2);
        mExNameView3 = (View) itemView.findViewById(R.id.exNameView3);
        mExNameView4 = (View) itemView.findViewById(R.id.exNameView4);
        mSetSchemeTextView = (TextView) itemView.findViewById(R.id.setSchemeTextView);
        mSetSchemeView1 = (View) itemView.findViewById(R.id.setSchemeView1);
        mSetSchemeView2 = (View) itemView.findViewById(R.id.setSchemeView2);
        mParentLayout = (ConstraintLayout) itemView.findViewById(R.id.parentLayout);
    }

    public boolean getIsImperialPOV() {
        return isImperialPOV;
    }

    public void setImperialPOV(boolean imperialPOV) {
        isImperialPOV = imperialPOV;
    }

    private void setExNameVisible(){
        mExNameView1.setVisibility(View.VISIBLE);
        mExNameView4.setVisibility(View.VISIBLE);
        mExNameTextView.setVisibility(View.VISIBLE);
    }

    private void setExNameSSVisible(){
        mExNameView1.setVisibility(View.VISIBLE);
        mExNameView2.setVisibility(View.VISIBLE);
        mExNameView3.setVisibility(View.VISIBLE);
        mExNameView4.setVisibility(View.VISIBLE);
        mExNameTextView.setVisibility(View.VISIBLE);
    }

    private void setSetSchemeVisible(){
        mExNameView1.setVisibility(View.VISIBLE);
        mSetSchemeView2.setVisibility(View.VISIBLE);
        mSetSchemeTextView.setVisibility(View.VISIBLE);
    }

    private void setSetSchemeSSVisible(){
        mExNameView1.setVisibility(View.VISIBLE);
        mExNameView2.setVisibility(View.INVISIBLE);
        mSetSchemeView1.setVisibility(View.VISIBLE);
        mSetSchemeView2.setVisibility(View.VISIBLE);
        mSetSchemeTextView.setVisibility(View.VISIBLE);
    }

    public void setLighterShade(){
        mParentLayout.setBackgroundColor(Color.parseColor("#303030"));;
    }

    public void setIsLastItem(){
        // well that didn't work
        //if(mSetSchemeView1.getVisibility() == View.VISIBLE){
        //    mSetSchemeView1.setLayoutParams(new ConstraintLayout.LayoutParams(1, 11));
        //}else if(mSetSchemeSSView1.getVisibility() == View.VISIBLE){
        //    mSetSchemeSSView1.setLayoutParams(new ConstraintLayout.LayoutParams(1, 11));
        //}
    }

    public void setUpView(){
        String[] tokens = getInfoString().split("!");
        ArrayList<String> arrayList = new ArrayList<>();

        for(String string : tokens){
            arrayList.add(string);
        }

        if(arrayList.contains("ss")){
            if(isExerciseName(tokens[0]) && !isPercentage(tokens[0])){
                // is superset exname
                setExNameSSVisible();
                mExNameTextView.setText(tokens[0]);
            }else{
                // is superset set scheme
                setSetSchemeSSVisible();

                String setSchemeString;

                if(isPercentage(tokens[0])){
                    setSchemeString = formatPercentToString(tokens[0]);
                }else{
                    setSchemeString = tokens[0];
                }

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(setSchemeString);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(setSchemeString);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = setSchemeString;
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = setSchemeString;
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }
            }
        }else{
            if(isExerciseName(tokens[0]) && !isPercentage(tokens[0])){
                // is normal exname
                setExNameVisible();

                mExNameTextView.setText(tokens[0]);
            }else{
                // is normal set scheme
                setSetSchemeVisible();

                String setSchemeString;

                if(isPercentage(tokens[0])){
                    setSchemeString = formatPercentToString(tokens[0]);
                }else{
                    setSchemeString = tokens[0];
                }

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(setSchemeString);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(setSchemeString);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = setSchemeString;
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs"); // crash here
                    mSetSchemeTextView.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = setSchemeString;
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }
            }
        }
    }

    public String formatPercentToString(String unFormatted){
        String formatted;

        String delims = "[_]";
        String[] tokens = unFormatted.split(delims);

        if(tokens[2].equals("a")){
            formatted = tokens[1] + " % of " + tokens[3];
        }else{
            formatted = unFormatted;
        }

        String delims2 = "[@]";
        String[] tokens2 = unFormatted.split(delims2);

        formatted = tokens2[0] + "@" + formatted;

        return formatted;
    }

    public boolean isPercentage(String setScheme){
        boolean percentage = false;

        String delims1 = "[@]";
        String[] tokens1 = setScheme.split(delims1);

        try{
            char c = tokens1[1].charAt(0);
            String cString = String.valueOf(c);
            if(cString.equals("p")){
                percentage = true;
            }
        }catch (IndexOutOfBoundsException e){
            Log.i("e", "e");
        }
        return percentage;
    }

    public boolean isOriginallyImperial() {
        return isOriginallyImperial;
    }

    public void setIsOriginallyImperial(boolean originallyImperial) {
        isOriginallyImperial = originallyImperial;
    }

    private String repsTextAdder(String input, String unit){
        String delims = "[x,@,_]";
        String[] tokens = input.split(delims);
        String newString = "";
        try {
            if(tokens[2].equals("a")){
                newString = tokens[0] + " x " + tokens[1] + " + @ " + tokens[3]; // crash here too
                if(!tokens[1].equals("B.W.")){
                    newString = tokens[0] + " x " + tokens[1] + " + @ " + tokens[3] + unit;
                }
            }else{
                newString = tokens[0] + " x " + tokens[1] + " @ " + tokens[2]; // crash here too
                if(!tokens[1].equals("B.W.")){
                    newString = tokens[0] + " x " + tokens[1] + " @ " + tokens[2] + unit;
                }
            }
        }catch (ArrayIndexOutOfBoundsException e){
            Log.i("ayo", "ayo");
        }

        return newString;
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

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
            if(input.length() > 5){
                String string = input.substring(0, 4);
                if(string.equals("T.F.")){
                    isExercise = false;
                }
            }
        }

        return isExercise;

    }

    public String getInfoString() {
        return mInfoString;
    }

    public void setInfoString(String infoString) {
        this.mInfoString = infoString;
    }
}
