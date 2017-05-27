package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightWAFrag extends Fragment {


    public RepsWeightWAFrag() {
        // Required empty public constructor
    }

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView pounds;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight_wa, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

}
