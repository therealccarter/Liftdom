package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompleteWorkoutRecyclerAdapter;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
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
    private LinearLayoutManager linearLayoutManager;
    private CompleteWorkoutRecyclerAdapter adapter;
    private List<CompletedWorkoutModelClass> postList;
    private int postInc = 0;

    //@BindView(R.id.loadingView1) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noResultsView) TextView noResultsView;
    @BindView(R.id.recycler_view_feed) RecyclerView mRecyclerView;
    @BindView(R.id.loadingView1) AVLoadingIndicatorView loadingView;
    @BindView(R.id.refreshView) MaterialRefreshLayout refreshView;
    //@BindView(R.id.loadMoreButton) Button loadMoreButton;

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
                    loadingView.setVisibility(View.GONE);
                    noResultsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //if(savedInstanceState != null){
        //    clearRecyclerView();
        //    setUpRecycler();
        // need to have it reload if on back press...
        //}


        refreshView.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                clearRecyclerView();
                setUpRecycler();
            }
        });

        return view;
    }

    private void clearRecyclerView(){
        postList.clear();
        postInc = 0;
        pastVisiblesItems = 0;
        visibleItemCount = 0;
        totalItemCount = 0;
        adapter.notifyDataSetChanged();
    }

    int pastVisiblesItems, visibleItemCount, totalItemCount;

    private void setUpRecycler(){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    postList = new ArrayList<>();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        CompletedWorkoutModelClass modelClass = dataSnapshot1.getValue(CompletedWorkoutModelClass.class);
                        postList.add(modelClass);
                        postInc++;
                        if(postInc == 10 || postInc == dataSnapshot.getChildrenCount()){
                            refreshView.finishRefreshing();
                            AVLoadingIndicatorView loadingView = (AVLoadingIndicatorView) getActivity().findViewById(R.id.loadingView1);
                            if(loadingView != null){
                                loadingView.setVisibility(View.GONE);
                                mRecyclerView.setHasFixedSize(false);
                                linearLayoutManager = new LinearLayoutManager(getActivity());
                                linearLayoutManager.setReverseLayout(true);
                                linearLayoutManager.setStackFromEnd(true);
                                linearLayoutManager.setSmoothScrollbarEnabled(true);
                                mRecyclerView.setLayoutManager(linearLayoutManager);
                                adapter = new CompleteWorkoutRecyclerAdapter(postList, getContext(),
                                        getActivity());
                                mRecyclerView.setAdapter(adapter);

                                mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                                    @Override
                                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                                        super.onScrolled(recyclerView, dx, dy);

                                        if(dy > 0){
                                            visibleItemCount = linearLayoutManager.getChildCount();
                                            totalItemCount = linearLayoutManager.getItemCount();
                                            pastVisiblesItems = linearLayoutManager.findFirstVisibleItemPosition();

                                            if ((visibleItemCount + pastVisiblesItems) >= totalItemCount){
                                                addOlderPosts(10);
                                            }
                                        }
                                    }
                                });
                            }
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addOlderPosts(final int olderPostsAmount){
        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int innerInc = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        innerInc++;
                        if(innerInc > postInc){
                            CompletedWorkoutModelClass modelClass = dataSnapshot1.getValue(CompletedWorkoutModelClass.class);
                            if(!postList.contains(modelClass)){
                                postList.add(modelClass);
                            }
                            if(innerInc == (olderPostsAmount + postInc) || innerInc == dataSnapshot.getChildrenCount()){
                                postInc = postInc + innerInc;
                                adapter.notifyItemInserted(postList.size() - 1);
                                break;
                            }
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
