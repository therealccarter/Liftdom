package com.liftdom.tools;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class BMICalcFrag extends Fragment {


    public BMICalcFrag() {
        // Required empty public constructor
    }

    //TODO: Just a thought, but it'd probably be more addictive if things like this run once upon creation

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmicalc, container, false);

        return view;
    }

}
