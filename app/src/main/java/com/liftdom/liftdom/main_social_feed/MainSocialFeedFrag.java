package com.liftdom.liftdom.main_social_feed;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class MainSocialFeedFrag extends Fragment {

    public MainSocialFeedFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        public void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noPostsView) TextView noPostsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_main_social_feed, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        navChanger(0);
        headerChanger("Home");

        //if(!MainActivitySingleton.getInstance().isBannerViewInitialized){
        //    String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
        //    Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
        //    Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //    MainActivitySingleton.getInstance().isBannerViewInitialized = true;
        //}else{
        //    //Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        //}


        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), SignInActivity.class));
        }else{
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef = FirebaseDatabase.getInstance().getReference();
            final DatabaseReference socialRef = rootRef.child("feed").child(uid);
            socialRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        loadingView.setVisibility(View.GONE);
                        noPostsView.setVisibility(View.GONE);
                        setUpFirebaseAdapter(socialRef);
                    }else{
                        loadingView.setVisibility(View.GONE);
                        noPostsView.setVisibility(View.VISIBLE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

        return view;
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>
                (CompletedWorkoutModelClass.class, R.layout.completed_workout_list_item,
                        CompletedWorkoutViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(CompletedWorkoutViewHolder viewHolder,
                                              CompletedWorkoutModelClass model, int position) {
                if(loadingView.getVisibility() == View.VISIBLE){
                    loadingView.setVisibility(View.GONE);
                }
                //viewHolder.setPosition(position);
                viewHolder.setUserName(model.getUserName());
                viewHolder.setUserLevel(model.getUserId(), rootRef);
                viewHolder.setActivity(getActivity());
                viewHolder.setUserId(model.getUserId());
                viewHolder.setUpProfilePics(model.getUserId());
                viewHolder.setPublicDescription(model.getPublicDescription());
                viewHolder.setTimeStamp(model.getDateTime());
                viewHolder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
                        model.isIsImperial());
                viewHolder.setActivity(getActivity());
                viewHolder.setRefKey(model.getRef());
                viewHolder.setCommentRecycler(model.getRef());
                if(model.getBonusList() != null){
                    if(!model.getBonusList().isEmpty()){
                        viewHolder.setBonusView(model.getBonusList());
                    }
                }
            }
        };

        recyclerView.setAdapter(firebaseAdapter);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
