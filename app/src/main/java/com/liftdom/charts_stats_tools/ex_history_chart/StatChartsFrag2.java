package com.liftdom.charts_stats_tools.ex_history_chart;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.mikephil.charting.charts.LineChart;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatChartsFrag2 extends Fragment {

    public StatChartsFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.lineChart) LineChart lineChart;
    @BindView(R.id.graphingSelectorButton) Button graphingSelector;
    @BindView(R.id.overallRadioButton) RadioButton overallRadioButton;
    @BindView(R.id.maxWeightRadioButton) RadioButton maxWeightRadioButton;
    @BindView(R.id.itemsBeingGraphed) TextView itemsTextView;
    @BindView(R.id.reloadChartButton) Button reloadChart;
    @BindView(R.id.clearChartButton) Button clearChart;
    @BindView(R.id.titleView) TextView titleView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stat_charts, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public void valueConverter(final ArrayList<ValueAndDateObject> valueAndDateArrayList, String exName, boolean
            isOverall){

    }
}
