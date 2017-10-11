package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
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
public class SetSchemeSupersetFrag extends Fragment {


    public SetSchemeSupersetFrag() {
        // Required empty public constructor
    }

    public String setSchemeString = "error";
    public boolean isSmallerText;


    @BindView(R.id.setSchemeString) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_set_scheme_superset, container, false);

        ButterKnife.bind(this, view);

        if(isSmallerText){
            setSchemesView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen
                    .sixteen_sp));
        }

        setSchemesView.setText(setSchemeString);

        return view;
    }



}
