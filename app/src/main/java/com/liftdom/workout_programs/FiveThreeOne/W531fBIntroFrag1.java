package com.liftdom.workout_programs.FiveThreeOne;


import agency.tango.materialintroscreen.SlideFragment;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
public class W531fBIntroFrag1 extends SlideFragment {


    public W531fBIntroFrag1() {
        // Required empty public constructor
    }

    @BindView(R.id.W531fBTitleView1) TextView W531fBTitleView1;
    @BindView(R.id.W531fBTitleView2) TextView W531fBTitleView2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w531f_bintro_frag1, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        W531fBTitleView1.setTypeface(lobster);
        W531fBTitleView2.setTypeface(lobster);

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
