package com.liftdom.liftdom.main_social_feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchResultFrag;
import com.lovejjfg.powerrefresh.OnRefreshListener;
import com.lovejjfg.powerrefresh.PowerRefreshLayout;
import com.wang.avi.AVLoadingIndicatorView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment {


    public MainFeedFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed")
            .child(uid);
    private FirebaseRecyclerAdapter mFirebaseAdapter;


    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;
    @BindView(R.id.recycler_view_feed) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_feed, container, false);

        BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        bottomNavigation.setSelectedIndex(1, false);

        ButterKnife.bind(this, view);

        setUpFirebaseAdapter();

        return view;
    }

    private void setUpFirebaseAdapter(){
        if(loadingView.getVisibility() == View.VISIBLE){
            loadingView.setVisibility(View.GONE);
        }
        mFirebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
                (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item, CompletedWorkoutViewHolder.class
                , mFeedRef) {
            @Override
            protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder, CompletedWorkoutModelClass model, int position) {
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserLevel(model.getUserId());
                viewHolder.setPublicDescription(model.getPublicDescription());
                viewHolder.setTimeStamp(model.getDateTime());
                viewHolder.setPostInfo(model.getWorkoutInfoList());
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

}
