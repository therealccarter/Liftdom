package com.liftdom.charts_stats_tools.tools;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liftdom.liftdom.R;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Brodin on 5/2/2018.
 */
public class ExerciseMaxesViewHolder extends RecyclerView.ViewHolder {

    private final TextView mExerciseNameView;
    private final TextView mMaxView;
    private final TextView mDateView;
    private String mExerciseName;
    private String mMaxValue;
    private boolean mIsImperial;
    private String mDate;
    private boolean mIsImperialPOV;

    public ExerciseMaxesViewHolder(View itemView){
        super(itemView);
        mExerciseNameView = (TextView) itemView.findViewById(R.id.exerciseNameView);
        mMaxView = (TextView) itemView.findViewById(R.id.maxView);
        mDateView = (TextView) itemView.findViewById(R.id.dateTimeView);
    }

    public String getExerciseName() {
        return mExerciseName;
    }

    public void setExerciseName(String mExerciseName) {
        this.mExerciseName = mExerciseName;
        String exName = mExerciseName + ":";
        mExerciseNameView.setText(exName);
    }

    public String getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(String mMaxValue) {
        this.mMaxValue = mMaxValue;
        if(isIsImperial() == isIsImperialPOV()){
            if(isIsImperial()){
                String val = mMaxValue + " lbs";
                mMaxView.setText(val);
            }else{
                String val = mMaxValue + " kgs";
                mMaxView.setText(val);
            }
        }else{
            if(isIsImperial() && !isIsImperialPOV()){
                // convert from imperial to metric
                String val = imperialToMetric(mMaxValue) + " kgs";
                mMaxView.setText(val);
            }else if(!isIsImperial() && isIsImperialPOV()){
                // convert from metric to imperial
                String val = metricToImperial(mMaxValue) + " lbs";
                mMaxView.setText(val);
            }
        }
    }

    public boolean isIsImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
        DateTime dateTimeOriginal = DateTime.parse(mDate);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");
        mDateView.setText(formattedLocalDate);
    }

    public boolean isIsImperialPOV() {
        return mIsImperialPOV;
    }

    public void setIsImperialPOV(boolean mIsImperialPOV) {
        this.mIsImperialPOV = mIsImperialPOV;
    }

    private String metricToImperial(String input){

        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        String newString = String.valueOf(lbsInt);

        return newString;
    }

    private String imperialToMetric(String input){

        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        String newString = String.valueOf(kgInt);

        return newString;
    }
}
