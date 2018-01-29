package com.liftdom.liftdom.main_social_feed.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RandomUsersBannerFrag extends Fragment {


    public RandomUsersBannerFrag() {
        // Required empty public constructor
    }

    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.followFellowLiftersRecycler) RecyclerView fellowLiftersRecyclerView;
    @BindView(R.id.fellowLiftersCloseButton) ImageView closeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_random_users_banner, container, false);

        ButterKnife.bind(this, view);



        return view;
    }

}
