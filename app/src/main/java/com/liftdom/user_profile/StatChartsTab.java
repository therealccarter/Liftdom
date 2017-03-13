package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatChartsTab extends Fragment {


    public StatChartsTab() {
        // Required empty public constructor
    }

    @BindView(R.id.lineChart) LineChart lineChart;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts_tab, container, false);

        ButterKnife.bind(this, view);

        // we could have an average weighted set that is able to show general progress
        // that main chart will be the workout history + future workouts
        // we'll also have charts for each exercise you've done at least once
        // also bodyweight/bf%/etc





        return view;
    }

}
