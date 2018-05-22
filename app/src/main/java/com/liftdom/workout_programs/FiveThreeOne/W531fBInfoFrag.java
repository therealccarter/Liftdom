package com.liftdom.workout_programs.FiveThreeOne;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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


    public W531fBInfoFrag() {
        // Required empty public constructor
    }

    public HashMap<String, String> extraInfoMap = new HashMap<>();

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

        beginDateView.setText(extraInfoMap.get("beginDate"));
        squat1rm.setText(extraInfoMap.get("squatMax"));
        benchPress1rm.setText(extraInfoMap.get("benchMax"));
        deadlift1rm.setText(extraInfoMap.get("deadliftMax"));
        ohp1rm.setText(extraInfoMap.get("ohpMax"));

        return view;
    }

}
