package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.liftdom.liftdom.R;

import java.util.HashMap;
import java.util.List;

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

    public void setUpView(){
        String[] tokens = getInfoString().split("_");

        if(tokens.length > 1){
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

                mSetSchemeViewSS.setText(tokens[0]);
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

                mSetSchemeView.setText(tokens[0]);
            }
        }
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

    public String getInfoString() {
        return mInfoString;
    }

    public void setInfoString(String infoString) {
        this.mInfoString = infoString;
    }
}
