package com.liftdom.knowledge_center.exercise_library;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.liftdom.utils.exercise_selector.ExPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesDetailFrag extends Fragment {


    public ExercisesDetailFrag() {
        // Required empty public constructor
    }

    String exName = "null";

    @BindView(R.id.testText) TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises_detail, container, false);

        textView.setText(exName);

        return view;
    }

}
