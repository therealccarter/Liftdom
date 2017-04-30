package com.liftdom.liftdom.main_social_feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchResultFrag;
import com.wang.avi.AVLoadingIndicatorView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment {


    public MainFeedFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.postsHolder) LinearLayout postsHolder;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_activity_feed, container, false);

        BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        bottomNavigation.setSelectedIndex(1, false);

        ButterKnife.bind(this, view);

        DatabaseReference feedRef = mRootRef.child("feed").child(uid);
        feedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    loadingView.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                } else {
                    int inc = 0;

                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                        if (inc == 0) {
                            loadingView.setVisibility(View.GONE);
                        }

                        //CompletedWorkoutClass completedWorkoutClass = (CompletedWorkoutClass) dataSnapshot1.getValue();
                        Map<String, Object> map = (Map<String, Object>) dataSnapshot1.getValue();

                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        CompletedWorkoutPostFrag completedWorkoutPostFrag = new CompletedWorkoutPostFrag();
                        completedWorkoutPostFrag.userId = (String) map.get("userId");
                        completedWorkoutPostFrag.userName = (String) map.get("userName");
                        completedWorkoutPostFrag.publicComment = (String) map.get("publicComment");
                        completedWorkoutPostFrag.workoutInfoList = (List) map.get("workoutInfoList");

                        fragmentTransaction.add(R.id.postsHolder, completedWorkoutPostFrag);
                        fragmentTransaction.commit();

                        inc++;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

}
