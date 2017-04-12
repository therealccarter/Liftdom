package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFinishedFrag extends Fragment {


    public WorkoutFinishedFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.restAdviceButton) Button restAdviceButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_finished, container, false);

        ButterKnife.bind(this, view);

        restAdviceButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), KnowledgeCenterHolderActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
