package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileInfoFrag extends Fragment {


    public ProfileInfoFrag() {
        // Required empty public constructor
    }

    //TODO: In this and my templates view, try to get these values to load before inflation
    //@Override
    //public void onCreate(Bundle savedInstanceState){
    //    super.onCreate(savedInstanceState);
//
    //}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_info, container, false);
    }

}
