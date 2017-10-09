package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 6/29/2017.
 */

public class WorkoutInfoViewHolder extends RecyclerView.ViewHolder{

    private final LinearLayout mExNameLL;
    private final LinearLayout mExNameSSLL;
    private final LinearLayout mSetSchemeLL;
    private final LinearLayout mSetSchemeSSLL;
    private final TextView mExNameView;
    private final TextView mExNameViewSS;
    private final TextView mSetSchemeView;
    private final TextView mSetSchemeViewSS;
    private String mInfoString;
    private boolean isImperialPOV;
    private boolean isOriginallyImperial;

    public WorkoutInfoViewHolder(View itemView){
        super(itemView);
        mExNameLL = (LinearLayout) itemView.findViewById(R.id.exNameLL);
        mExNameSSLL = (LinearLayout) itemView.findViewById(R.id.exNameSSLL);
        mSetSchemeLL = (LinearLayout) itemView.findViewById(R.id.setSchemeLL);
        mSetSchemeSSLL = (LinearLayout) itemView.findViewById(R.id.setSchemeSSLL);
        mExNameView = (TextView) itemView.findViewById(R.id.exNameTextView);
        mExNameViewSS = (TextView) itemView.findViewById(R.id.exNameTextViewSS);
        mSetSchemeView = (TextView) itemView.findViewById(R.id.setSchemeTextView);
        mSetSchemeViewSS = (TextView) itemView.findViewById(R.id.setSchemeTextViewSS);

    }

    public boolean getIsImperialPOV() {
        return isImperialPOV;
    }

    public void setImperialPOV(boolean imperialPOV) {
        isImperialPOV = imperialPOV;
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
                mExNameLL.setVisibility(View.GONE);
                mExNameSSLL.setVisibility(View.VISIBLE);
                mSetSchemeLL.setVisibility(View.GONE);
                mSetSchemeSSLL.setVisibility(View.GONE);

                mExNameViewSS.setText(tokens[0]);
            }else{
                // is superset set scheme
                mExNameLL.setVisibility(View.GONE);
                mExNameSSLL.setVisibility(View.GONE);
                mSetSchemeLL.setVisibility(View.GONE);
                mSetSchemeSSLL.setVisibility(View.VISIBLE);

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeViewSS.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeViewSS.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeViewSS.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeViewSS.setText(newRepsWeight);
                }
            }
        }else{
            if(isExerciseName(tokens[0])){
                // is normal exname
                mExNameLL.setVisibility(View.VISIBLE);
                mExNameSSLL.setVisibility(View.GONE);
                mSetSchemeLL.setVisibility(View.GONE);
                mSetSchemeSSLL.setVisibility(View.GONE);

                mExNameView.setText(tokens[0]);
            }else{
                // is normal set scheme
                mExNameLL.setVisibility(View.GONE);
                mExNameSSLL.setVisibility(View.GONE);
                mSetSchemeLL.setVisibility(View.VISIBLE);
                mSetSchemeSSLL.setVisibility(View.GONE);

                if(getIsImperialPOV() && !isOriginallyImperial()){
                    // you are imperial, workout is kg
                    String newRepsWeight = metricToImperial(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeView.setText(newRepsWeight);
                }else if(!getIsImperialPOV() && isOriginallyImperial()){
                    // you are kg, workout is imperial
                    String newRepsWeight = imperialToMetric(tokens[0]);
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeView.setText(newRepsWeight);
                }else if(getIsImperialPOV() && isOriginallyImperial()){
                    // both imperial
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " lbs");
                    mSetSchemeView.setText(newRepsWeight);
                }else{
                    // both kg
                    String newRepsWeight = tokens[0];
                    newRepsWeight = repsTextAdder(newRepsWeight, " kg");
                    mSetSchemeView.setText(newRepsWeight);
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
        String newString = tokens[0] + " reps @ " + tokens[1] + unit;
        return newString;
    }

    private String metricToImperial(String input){
        if(input.equals("B.W.")){
            return input;
        }else{
            String delims = "[@]";
            String[] tokens = input.split(delims);
            double lbsDouble = Double.parseDouble(tokens[1]) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            String newString = tokens[0] + "@" + String.valueOf(lbsInt);
            return newString;
        }
    }

    private String imperialToMetric(String input){
        if(input.equals("B.W.")){
            return input;
        }else{
            String delims = "[@]";
            String[] tokens = input.split(delims);
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
