package com.liftdom.liftdom.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleSetSchemeFrag extends Fragment {


    public SimpleSetSchemeFrag() {
        // Required empty public constructor
    }

    public String setSchemeString;

    @BindView(R.id.setsView) TextView setsView;
    @BindView(R.id.repsView) TextView repsView;
    @BindView(R.id.weightView) TextView weightView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_set_scheme, container, false);

        ButterKnife.bind(this, view);

        String delims = "[x, @]";

        String[] tokens = setSchemeString.split(delims);
        setsView.setText(tokens[0]);
        repsView.setText(tokens[1]);
        weightView.setText(tokens[2]);

        return view;
    }

}
