package com.liftdom.workout_programs.PPL_Reddit;


import android.os.Bundle;
import android.transition.Slide;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag3 extends SlideFragment {


    public PPLRedditIntroFrag3() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag3, container, false);

        return view;
    }


    @Override
    public int backgroundColor() {
        return R.color.confirmgreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
