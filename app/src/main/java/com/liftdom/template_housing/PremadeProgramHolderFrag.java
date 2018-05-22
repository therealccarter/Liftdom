package com.liftdom.template_housing;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.FiveThreeOne.W531fB_HolderActivity;
import com.liftdom.workout_programs.Smolov.SmolovHolderActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class PremadeProgramHolderFrag extends Fragment {


    public PremadeProgramHolderFrag() {
        // Required empty public constructor
    }

    String workoutType;

    @BindView(R.id.templateName) TextView templateNameView;
    @BindView(R.id.descriptionView) TextView descriptionView;
    @BindView(R.id.parentLL) LinearLayout parentLL;
    @BindView(R.id.experienceLevelView) TextView experienceLevelview;
    @BindView(R.id.workoutTypeView) TextView workoutTypeView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_premade_program_holder, container, false);

        ButterKnife.bind(this, view);

        if(workoutType != null){
            setUpViews();
        }

        parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(workoutType != null){
                    if(workoutType.equals("Smolov")){
                        Intent intent;
                        intent = new Intent(getContext(), SmolovHolderActivity.class);
                        startActivity(intent);
                    }else if(workoutType.equals("W531fB")){
                        Intent intent;
                        intent = new Intent(getContext(), W531fB_HolderActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });

        return view;
    }

    private void setUpViews(){
        if(workoutType.equals("Smolov")){
            setUpSmolov();
        }else if(workoutType.equals("W531fB")){
            setUpW531fB();
        }
    }

    private void setUpSmolov(){
        templateNameView.setText("Smolov");
        experienceLevelview.setText("Advanced");
        workoutTypeView.setText("Strength");
    }

    private void setUpW531fB(){
        templateNameView.setText("Wendler 5/3/1 For Beginners");
        experienceLevelview.setText("Intermediate");
        workoutTypeView.setText("Strength");
    }
}