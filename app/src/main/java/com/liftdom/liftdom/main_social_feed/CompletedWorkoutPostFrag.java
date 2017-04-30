package com.liftdom.liftdom.main_social_feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedWorkoutPostFrag extends Fragment {


    public CompletedWorkoutPostFrag() {
        // Required empty public constructor
    }

    public String userId;
    public String userName;
    public String publicComment;
    public List workoutInfoList;


    @BindView(R.id.userName) TextView userNameView;
    @BindView(R.id.workoutContents) TextView workoutContentsView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_workout_post, container, false);

        ButterKnife.bind(this, view);

        String workoutInfoTestString = "";

        for(int i = 0; i < workoutInfoList.size(); i++){
            workoutInfoTestString = workoutInfoTestString + workoutInfoList.get(i) + "\n";
        }

        userNameView.setText(userName);

        workoutContentsView.setText(workoutInfoTestString);

        return view;
    }

}
