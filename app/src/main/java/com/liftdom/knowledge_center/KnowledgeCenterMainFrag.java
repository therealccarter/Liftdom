package com.liftdom.knowledge_center;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.github.dubulee.coordinatorlayouthelper.CoordinatorLayoutHelperViewPager;
import com.github.dubulee.coordinatorlayouthelper.HeaderLayout;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeCenterMainFrag extends Fragment {


    public KnowledgeCenterMainFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.articlesButton) Button articlesButton;
    @BindView(R.id.exercisesButton) Button exercisesButton;
    @BindView(R.id.disciplinesButton) Button disciplinesButton;
    @BindView(R.id.hallOfFameButton) Button hallOfFameButton;
    @BindView(R.id.knowledgeOfTheDayHolder) LinearLayout kOTDHolder;
    @BindView(R.id.toolbar1) Toolbar toolbar;
    @BindView(R.id.collapsingToolbar1) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbarImage1) ImageView toolbarImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knowledge_center_main, container, false);

        ButterKnife.bind(this, view);



        return view;
    }

}
