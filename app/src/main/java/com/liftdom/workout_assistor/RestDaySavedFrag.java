package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestDaySavedFrag extends Fragment {


    public RestDaySavedFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.goBackHome) Button goHomeButton;
    @BindView(R.id.streakTextView) TextView streakTextView;
    @BindView(R.id.powerLevelIncreaseTextView) TextView powerLevelIncreaseView;
    @BindView(R.id.finishedTextView) TextView finishedTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        ButterKnife.bind(this, view);

        finishedTextView.setText("REST DAY FINISHED");



        return view;
    }

}
