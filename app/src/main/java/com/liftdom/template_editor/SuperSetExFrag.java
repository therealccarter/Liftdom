package com.liftdom.template_editor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SuperSetExFrag extends Fragment {


    public SuperSetExFrag() {
        // Required empty public constructor
    }

    //TODO: Callback here to get parent ex on pause


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_set_ex, container, false);

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();


    }

}
