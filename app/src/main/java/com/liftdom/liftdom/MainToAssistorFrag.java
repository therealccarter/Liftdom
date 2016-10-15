package com.liftdom.liftdom;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.liftdom.workout_assistor.WorkoutAssistorActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainToAssistorFrag extends Fragment {


    public MainToAssistorFrag() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_to_assistor, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(getContext(), WorkoutAssistorActivity.class));
            }
        });

        return view;
    }

}
