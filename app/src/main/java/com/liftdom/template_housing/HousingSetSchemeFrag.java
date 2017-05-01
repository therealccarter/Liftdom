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

    public String setSchemeString = "error";
    boolean differentType = false;

    @BindView(R.id.setSchemeString) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_housing_set_scheme, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            setSchemeString = savedInstanceState.getString("set_scheme_string");
            setSchemesView.setText(setSchemeString);
        }

        if(differentType){
            pounds.setVisibility(View.GONE);
        }

        setSchemesView.setText(setSchemeString);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("set_scheme_string", setSchemeString);

        super.onSaveInstanceState(savedInstanceState);
    }

}
