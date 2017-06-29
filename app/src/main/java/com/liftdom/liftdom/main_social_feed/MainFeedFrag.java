package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompleteWorkoutRecyclerAdapter;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.wang.avi.AVLoadingIndicatorView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainFeedFrag extends Fragment{


    public MainFeedFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        public void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    private DatabaseReference mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed")
            .child(uid);
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    //@BindView(R.id.loadingView1) AVLoadingIndicatorView loadingView;
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

        headerChanger("Home");

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    setUpRecycler();
                }else{
                    AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                            .loadingView1);
                    loadingView.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //TODO: So I think all we have to do now is implement what's done in the fragments in the viewholder.

        return view;
    }

    private void setUpRecycler(){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    List<CompletedWorkoutModelClass> list = new ArrayList<>();
                    int inc = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        CompletedWorkoutModelClass modelClass = dataSnapshot1.getValue(CompletedWorkoutModelClass.class);
                        list.add(modelClass);
                        inc++;
                        if(inc == dataSnapshot.getChildrenCount()){
                            AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
                                    .loadingView1);
                            loadingView.setVisibility(View.GONE);
                            CompleteWorkoutRecyclerAdapter adapter = new CompleteWorkoutRecyclerAdapter(list, getContext(),
                                    getActivity());
                            mRecyclerView.setAdapter(adapter);
                            mRecyclerView.setHasFixedSize(false);
                            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, true));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private List<CompletedWorkoutModelClass> getPosts(){
        final List<CompletedWorkoutModelClass> list = new ArrayList<>();

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    //setUpFirebaseAdapter();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        CompletedWorkoutModelClass modelClass = dataSnapshot1.getValue(CompletedWorkoutModelClass.class);
                        list.add(modelClass);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return list;
    }

    //private void setUpFirebaseAdapter(){
    //    mFirebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
    //            (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item, CompletedWorkoutViewHolder
    //            .class, mFeedRef) {
    //        @Override
    //        protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder, CompletedWorkoutModelClass
    //                model, int position) {
    //            viewHolder.setUserName(model.getUserName());
    //            viewHolder.setUserLevel(model.getUserId(), mRootRef);
    //            viewHolder.setActivity(getActivity());
    //            viewHolder.setUserId(model.getUserId());
    //            viewHolder.setPublicDescription(model.getPublicDescription());
    //            viewHolder.setTimeStamp(model.getDateTime());
    //            viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity());
    //            viewHolder.setActivity(getActivity());
    //            viewHolder.setRefKey(model.getRef());
    //            viewHolder.setCommentFrag(getActivity());
//
    //            if(position == 0){
    //                AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id
    //                        .loadingView1);
    //                if(loadingView != null){
    //                    loadingView.setVisibility(View.GONE);
    //                }
    //            }
    //        }
    //    };
//
//
    //    mRecyclerView.setAdapter(mFirebaseAdapter);
    //    //((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
//
    //}

    @Override
    public void onDestroy(){
        super.onDestroy();
        //mFirebaseAdapter.cleanup();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
