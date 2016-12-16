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
public class HousingDoWFrag extends Fragment {


    public HousingDoWFrag() {
        // Required empty public constructor
    }

    String dOWString = "error";

    @BindView(R.id.doWName) TextView doWStringView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_housing_do_w, container, false);

        ButterKnife.bind(this, view);

        doWStringView.setText(dOWString);

        return view;
    }

}
