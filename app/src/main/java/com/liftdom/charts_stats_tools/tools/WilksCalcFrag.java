package com.liftdom.charts_stats_tools.tools;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WilksCalcFrag extends Fragment {


    public WilksCalcFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.squatEditText) EditText squatEditText;
    @BindView(R.id.squatUnit) TextView squatUnit;
    @BindView(R.id.benchPressEditText) EditText benchPressEditText;
    @BindView(R.id.benchPressUnit) TextView benchPressUnit;
    @BindView(R.id.deadliftEditText) EditText deadliftEditText;
    @BindView(R.id.deadliftUnit) TextView deadliftUnit;
    @BindView(R.id.calculateButton) Button calculateButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_wilks_calc, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        titleView.setTypeface(lobster);

        return view;
    }

}
