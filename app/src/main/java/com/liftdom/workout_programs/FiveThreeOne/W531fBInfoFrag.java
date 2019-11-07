package com.liftdom.workout_programs.FiveThreeOne;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBInfoFrag extends Fragment {

    /**
     * This is for the SelectedTemplatePage btw
     */


    public W531fBInfoFrag() {
        // Required empty public constructor
    }

    public HashMap<String, String> extraInfoMap = new HashMap<>();
    public boolean isImperial;

    @BindView(R.id.beginDateView) TextView beginDateView;
    @BindView(R.id.squat1rm) TextView squat1rm;
    @BindView(R.id.benchPress1rm) TextView benchPress1rm;
    @BindView(R.id.deadlift1rm) TextView deadlift1rm;
    @BindView(R.id.ohp1rm) TextView ohp1rm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w531f_binfo, container, false);

        ButterKnife.bind(this, view);

        String squatMax;
        String benchPressMax;
        String deadliftMax;
        String ohpMax;
        if(isImperial){
            squatMax = extraInfoMap.get("squatMax") + " lbs";
            benchPressMax = extraInfoMap.get("benchMax") + " lbs";
            deadliftMax = extraInfoMap.get("deadliftMax") + " lbs";
            ohpMax = extraInfoMap.get("ohpMax") + " lbs";
        }else{
            squatMax = extraInfoMap.get("squatMax") + " kgs";
            benchPressMax = extraInfoMap.get("benchMax") + " kgs";
            deadliftMax = extraInfoMap.get("deadliftMax") + " kgs";
            ohpMax = extraInfoMap.get("ohpMax") + " kgs";
        }

        beginDateView.setText(extraInfoMap.get("beginDate"));
        squat1rm.setText(squatMax);
        benchPress1rm.setText(benchPressMax);
        deadlift1rm.setText(deadliftMax);
        ohp1rm.setText(ohpMax);

        return view;
    }

}
