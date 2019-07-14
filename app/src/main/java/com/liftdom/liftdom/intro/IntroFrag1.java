package com.liftdom.liftdom.intro;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag1 extends SlideFragment {


    public IntroFrag1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag1, container, false);

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.backgroundgrey1;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
