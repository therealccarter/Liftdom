package com.liftdom.workout_programs.FiveThreeOne;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBInfoFrag extends Fragment {


    public W531fBInfoFrag() {
        // Required empty public constructor
    }

    public HashMap<String, String> extraInfoMap = new HashMap<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w531f_binfo, container, false);

        return view;
    }

}
