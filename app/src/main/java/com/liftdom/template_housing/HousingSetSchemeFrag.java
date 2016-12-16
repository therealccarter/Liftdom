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
public class HousingSetSchemeFrag extends Fragment {


    public HousingSetSchemeFrag() {
        // Required empty public constructor
    }

    String setSchemeString = "error";

    @BindView(R.id.setSchemeString) TextView setSchemesView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_housing_set_scheme, container, false);

        ButterKnife.bind(this, view);

        setSchemesView.setText(setSchemeString);

        return view;
    }

}
