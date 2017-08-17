package com.liftdom.liftdom.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
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
    public boolean isPastDate;
    public String exerciseName;

    @BindView(R.id.setsView) TextView setsView;
    @BindView(R.id.repsView) TextView repsView;
    @BindView(R.id.weightView) TextView weightView;
    @BindView(R.id.xView) TextView xView;
    @BindView(R.id.unitsView) TextView unitsView;
    @BindView(R.id.atView) TextView atView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_set_scheme, container, false);

        ButterKnife.bind(this, view);

        if(!isPastDate){
            String delims = "[x, @]";

            String[] tokens = setSchemeString.split(delims);

            setsView.setText(tokens[0]);
            repsView.setText(tokens[1]);
            weightView.setText(tokens[2]);
        }else{
            setsView.setVisibility(View.GONE);
            xView.setVisibility(View.GONE);
            atView.setText(" reps @ ");

            String delims = "[@]";

            String[] tokens = setSchemeString.split(delims);
            if(tokens[0].equals("10x8")){
                Log.i("kiss my ass", "bitch");
            }
            repsView.setText(tokens[0]);
            weightView.setText(tokens[1]);
        }

        return view;
    }

}
