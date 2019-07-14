package com.liftdom.workout_assistor;


import android.app.FragmentTransaction;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseNameFrag extends Fragment {


    public ExerciseNameFrag() {
        // Required empty public constructor
    }

    public String exerciseName = "fail";

    @BindView(R.id.exerciseName) TextView exerciseNameView;
    @BindView(R.id.repsWeightContainer) LinearLayout repsWeightContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_name, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            exerciseName = savedInstanceState.getString("exercise_name");
            exerciseNameView.setText(exerciseName);
        }

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        exerciseNameView.setTypeface(lobster);

        exerciseNameView.setText(exerciseName);

        return view;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("exercise_name", exerciseName);

        super.onSaveInstanceState(savedInstanceState);
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }


}
