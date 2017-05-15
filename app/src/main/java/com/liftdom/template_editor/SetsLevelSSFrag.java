package com.liftdom.template_editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelSSFrag extends android.app.Fragment {


    public SetsLevelSSFrag() {
        // Required empty public constructor
    }

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_s, container, false);

        ButterKnife.bind(this, view);

        return view;
    }

    public String getInfoList(){
        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString = weightEditText.getText().toString();

        String infoList = setsString + "x" + repsString + "@" +
                weightString;

        return infoList;
    }

}
