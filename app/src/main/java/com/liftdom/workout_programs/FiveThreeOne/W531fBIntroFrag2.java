package com.liftdom.workout_programs.FiveThreeOne;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBIntroFrag2 extends SlideFragment {


    public W531fBIntroFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.squatEditText) EditText squatEditText;
    @BindView(R.id.unitsView1) TextView unitsView1;
    @BindView(R.id.benchPressEditText) EditText benchPressEditText;
    @BindView(R.id.unitsView1) TextView unitsView2;
    @BindView(R.id.deadliftEditText) EditText deadliftEditText;
    @BindView(R.id.unitsView1) TextView unitsView3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_w531f_bintro_frag2, container, false);

        /**
         * I'd feel best about this program if it is in normal formatting.
         * So the user can edit to their heart's content.
         */

        return view;
    }

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!squatEditText.getText().toString().isEmpty()
                && !benchPressEditText.getText().toString().isEmpty()
                && !deadliftEditText.getText().toString().isEmpty()){
            W531fBSingleton.getInstance().squatMax = squatEditText.getText().toString();
            W531fBSingleton.getInstance().benchMax = benchPressEditText.getText().toString();
            W531fBSingleton.getInstance().deadliftMax = deadliftEditText.getText().toString();
            valuesEntered = true;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidSmolovFields);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmgreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
