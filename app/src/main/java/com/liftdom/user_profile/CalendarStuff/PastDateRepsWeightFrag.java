package com.liftdom.user_profile.CalendarStuff;


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
public class PastDateRepsWeightFrag extends Fragment {

    String repsWeight = "null";


    public PastDateRepsWeightFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.repsWeightTextView) TextView repsWeightTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_date_reps_weight, container, false);

        ButterKnife.bind(this, view);

        repsWeightTextView.setText(repsWeight);

        return view;
    }

}
