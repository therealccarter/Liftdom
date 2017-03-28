package com.liftdom.knowledge_center;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knowledge_center_main, container, false);

        ButterKnife.bind(this, view);


        return view;
    }

}
