package com.liftdom.charts_stats_tools.tools;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liftdom.liftdom.R;

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
    }

    public String getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(String mMaxValue) {
        this.mMaxValue = mMaxValue;
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
    }

    public boolean isIsImperialPOV() {
        return mIsImperialPOV;
    }

    public void setIsImperialPOV(boolean mIsImperialPOV) {
        this.mIsImperialPOV = mIsImperialPOV;
    }
}
