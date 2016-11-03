package com.liftdom.user_profile;


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
public class HistoryExerciseNameFrag extends Fragment {


    public HistoryExerciseNameFrag() {
        // Required empty public constructor
    }

    public String exerciseName = "fail";

    @BindView(R.id.exerciseName) TextView exerciseNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_exercise_name, container, false);

        ButterKnife.bind(this, view);

        exerciseNameView.setText(exerciseName);

        return view;
    }

}
