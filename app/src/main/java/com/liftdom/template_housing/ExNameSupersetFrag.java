package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExNameSupersetFrag extends Fragment {


    public ExNameSupersetFrag() {
        // Required empty public constructor
    }

    public String exNameString = "error";

    @BindView(R.id.exerciseName) TextView exerciseName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_superset, container, false);

        ButterKnife.bind(this, view);

        exerciseName.setText(exNameString);

        return view;
    }

}