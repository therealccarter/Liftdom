package com.liftdom.workout_programs.PPL_Reddit;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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

    @BindView(R.id.pplpplrRadioButton) RadioButton pplpplrRadioButton;
    @BindView(R.id.pplrpplRadioButton) RadioButton pplrpplRadioButton;


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

        pplpplrRadioButton.setChecked(true);

        return view;
    }


    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;


        valuesEntered = true;

        return valuesEntered;
    }

    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
