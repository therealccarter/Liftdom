package com.liftdom.workout_programs.PPL_Reddit;


import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag4 extends SlideFragment {


    public PPLRedditIntroFrag4() {
        // Required empty public constructor
    }

    @BindView(R.id.importantNotes) TextView importantNotes;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag4, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        importantNotes.setTypeface(lobster);

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
