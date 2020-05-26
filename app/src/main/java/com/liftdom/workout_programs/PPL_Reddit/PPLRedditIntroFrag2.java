package com.liftdom.workout_programs.PPL_Reddit;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag2 extends SlideFragment {


    public PPLRedditIntroFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.todayRadioButton) RadioButton todayRadioButton;
    @BindView(R.id.mondayRadioButton) RadioButton mondayRadioButton;
    @BindView(R.id.pushplpplrRadioButton) RadioButton pushplpplrRadioButton;
    @BindView(R.id.pullplpplrRadioButton) RadioButton pullplpplrRadioButton;
    @BindView(R.id.pushplrpplRadioButton) RadioButton pushplrpplRadioButton;
    @BindView(R.id.pullplrpplRadioButton) RadioButton pullplrpplRadioButton;
    @BindView(R.id.warmupCheckbox) CheckBox warmupCheckbox;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        // We'll collect the data into the singleton on program name evaluation.
        todayRadioButton.setChecked(true);
        pullplpplrRadioButton.setChecked(true);
        warmupCheckbox.setChecked(true);

        return view;
    }

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        /**
         * We need to add the strength/high rep versions. Radio group with normal version there as well.
         * Also the last slide looks like shit.
         */

        if(pushplpplrRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().format = "pushplpplr";
            valuesEntered = true;
        }else if(pullplpplrRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().format = "pullplpplr";
            valuesEntered = true;
        }else if(pushplrpplRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().format = "pushplrppl";
            valuesEntered = true;
        }else if(pullplrpplRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().format = "pullplrppl";
            valuesEntered = true;
        }

        if(todayRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().isBeginToday = true;
            valuesEntered = true;
        }else if(mondayRadioButton.isChecked()){
            PPLRedditSingleton.getInstance().isBeginToday = false;
            valuesEntered = true;
        }

        if(warmupCheckbox.isChecked()){
            PPLRedditSingleton.getInstance().isWarmup = true;
        }else{
            PPLRedditSingleton.getInstance().isWarmup = false;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.makeSureAllChecked);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmGreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
