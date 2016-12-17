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
public class HousingExNameFrag extends Fragment {


    public HousingExNameFrag() {
        // Required empty public constructor
    }

    String exNameString = "error";

    @BindView(R.id.exerciseName) TextView exerciseName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_housing_ex_name, container, false);

        ButterKnife.bind(this, view);


        if(savedInstanceState != null){
            exNameString = savedInstanceState.getString("ex_name");
            exerciseName.setText(exNameString);
        }

        exerciseName.setText(exNameString);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("ex_name", exNameString);

        super.onSaveInstanceState(savedInstanceState);
    }


}
