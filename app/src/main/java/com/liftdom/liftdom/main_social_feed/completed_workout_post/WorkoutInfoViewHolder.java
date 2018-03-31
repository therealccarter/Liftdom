package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
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
    private final TextView mExNameTextViewSS;
    private final View mExNameSSView1;
    private final View mExNameSSView2;
    private final View mExNameSSView3;
    private final View mExNameSSView4;
    private final TextView mSetSchemeTextView;
    private final View mSetSchemeView1;
    private final View mSetSchemeView2;
    private final TextView mSetSchemeTextViewSS;
    private final View mSetSchemeSSView1;
    private final View mSetSchemeSSView2;
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
        mExNameTextViewSS = (TextView) itemView.findViewById(R.id.exNameTextViewSS);
        mExNameSSView1 = (View) itemView.findViewById(R.id.exNameSSView1);
        mExNameSSView2 = (View) itemView.findViewById(R.id.exNameSSView2);
        mExNameSSView3 = (View) itemView.findViewById(R.id.exNameSSView3);
        mExNameSSView4 = (View) itemView.findViewById(R.id.exNameSSView4);
        mSetSchemeTextView = (TextView) itemView.findViewById(R.id.setSchemeTextView);
        mSetSchemeView1 = (View) itemView.findViewById(R.id.setSchemeView1);
        mSetSchemeView2 = (View) itemView.findViewById(R.id.setSchemeView2);
        mSetSchemeTextViewSS = (TextView) itemView.findViewById(R.id.setSchemeTextViewSS);
        mSetSchemeSSView1 = (View) itemView.findViewById(R.id.setSchemeSSView1);
        mSetSchemeSSView2 = (View) itemView.findViewById(R.id.setSchemeSSView2);
    }

    public boolean getIsImperialPOV() {
        return isImperialPOV;
    }

    public void setImperialPOV(boolean imperialPOV) {
        isImperialPOV = imperialPOV;
    }

    private void setExNameVisible(){
        mExNameView1.setVisibility(View.VISIBLE);
        mExNameView2.setVisibility(View.VISIBLE);
        mExNameTextView.setVisibility(View.VISIBLE);
    }

    private void setExNameSSVisible(){
        mExNameSSView1.setVisibility(View.VISIBLE);
        mExNameSSView2.setVisibility(View.VISIBLE);
        mExNameSSView3.setVisibility(View.VISIBLE);
        mExNameSSView4.setVisibility(View.VISIBLE);
        mExNameTextViewSS.setVisibility(View.VISIBLE);
    }

    private void setSetSchemeVisible(){
        mSetSchemeView1.setVisibility(View.VISIBLE);
        mSetSchemeView2.setVisibility(View.VISIBLE);
        mSetSchemeTextView.setVisibility(View.VISIBLE);
    }

    private void setSetSchemeSSVisible(){
        mSetSchemeSSView1.setVisibility(View.VISIBLE);
        mSetSchemeSSView2.setVisibility(View.VISIBLE);
        mSetSchemeTextViewSS.setVisibility(View.VISIBLE);
    }

    public void setUpView(){
        String[] tokens = getInfoString().split("_");
        ArrayList<String> arrayList = new ArrayList<>();

        for(String string : tokens){
            arrayList.add(string);
        }

        if(arrayList.contains("ss")){
            if(isExerciseName(tokens[0])){
                // is superset exname
                setExNameSSVisible();
                mExNameTextViewSS.setText(tokens[0]);
            }else{
                // is superset set scheme
                setSetSchemeSSVisible();

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextViewSS.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextViewSS.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextViewSS.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextViewSS.setText(newRepsWeight);
                }
            }
        }else{
            if(isExerciseName(tokens[0])){
                // is normal exname
                setExNameVisible();

                mExNameTextView.setText(tokens[0]);
            }else{
                // is normal set scheme
                setSetSchemeVisible();

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeTextView.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeTextView.setText(newRepsWeight);
                }
            }
        }
    }

    public boolean isOriginallyImperial() {
        return isOriginallyImperial;
    }

    public void setIsOriginallyImperial(boolean originallyImperial) {
        isOriginallyImperial = originallyImperial;
    }

    private String repsTextAdder(String input, String unit){
        String delims = "[@]";
        String[] tokens = input.split(delims);
        String newString = tokens[0] + " reps @ " + tokens[1];
        if(!tokens[1].equals("B.W.")){
            newString = tokens[0] + " reps @ " + tokens[1] + unit;
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
