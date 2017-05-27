package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExNameSSWAFrag extends Fragment {


    public ExNameSSWAFrag() {
        // Required empty public constructor
    }

    String exName = "null";
    ArrayList<String> infoList = new ArrayList<>();
    int fragCount = 0;

    @BindView(R.id.exerciseName) TextView exNameView;
    @BindView(R.id.repsWeightContainerSS) LinearLayout repsWeightContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_sswa, container, false);

        ButterKnife.bind(this, view);

        exNameView.setText(infoList.get(0));

        for(int i = 1; i < infoList.size(); i++){
            fragCount++;
            String countString = String.valueOf(fragCount) + "ss";
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            RepsWeightSSWAFrag repsWeightFrag = new RepsWeightSSWAFrag();
            repsWeightFrag.repsWeightString = infoList.get(i);
            fragmentTransaction.add(R.id.repsWeightContainerSS, repsWeightFrag, countString);
            fragmentTransaction.commitAllowingStateLoss();
        }

        return view;
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
