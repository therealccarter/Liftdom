package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightFrag extends Fragment {


    public RepsWeightFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.repsEditText) EditText repsEditText;
    @BindView(R.id.weightEditText) EditText weightEditText;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

}
