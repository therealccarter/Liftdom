package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
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

    public void setIsLastItem(){
        // well that didn't work
        //if(mSetSchemeView1.getVisibility() == View.VISIBLE){
        //    mSetSchemeView1.setLayoutParams(new ConstraintLayout.LayoutParams(1, 11));
        //}else if(mSetSchemeSSView1.getVisibility() == View.VISIBLE){
        //    mSetSchemeSSView1.setLayoutParams(new ConstraintLayout.LayoutParams(1, 11));
        //}
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
                mExNameTextView.setText(tokens[0]);
            }else{
                // is superset set scheme
                setSetSchemeSSVisible();

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
        String delims = "[x,@]";
        String[] tokens = input.split(delims);
        String newString = tokens[0] + " x " + tokens[1] + " @ " + tokens[2];
        if(!tokens[1].equals("B.W.")){
            newString = tokens[0] + " x " + tokens[1] + " @ " + tokens[2] + unit;
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
