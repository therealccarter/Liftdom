package com.liftdom.liftdom.main_social_feed;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class GlobalFeedFrag extends Fragment {


    public GlobalFeedFrag() {
        // Required empty public constructor
    }

    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_global_feed, container, false);

        ButterKnife.bind(this, view);

        Log.i("feedInfo", "onCreate");

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), SignInActivity.class));
        }else{
            //showTutorialChoice();
            uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
            rootRef = FirebaseDatabase.getInstance().getReference();
            if(savedInstanceState == null){
                //checkForRandomUsersBanner();
            }
            final DatabaseReference socialRef = rootRef.child("globalFeed");
            socialRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        DatabaseReference userRef = rootRef.child("user").child(uid);
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                if(userModelClass.isIsImperial()){
                                    loadingView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, true);
                                }else{
                                    loadingView.setVisibility(View.GONE);
                                    setUpFirebaseAdapter(socialRef, false);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }else{
                        loadingView.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        return view;
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        Query query = databaseReference.orderByChild("dateTime");

        FirebaseRecyclerOptions<CompletedWorkoutModelClass> options = new FirebaseRecyclerOptions
                .Builder<CompletedWorkoutModelClass>()
                .setQuery(query, CompletedWorkoutModelClass.class)
                .build();

        firebaseAdapter = new FirebaseRecyclerAdapter<CompletedWorkoutModelClass, CompletedWorkoutViewHolder>(options) {

            @Override
            protected void onBindViewHolder(@NonNull CompletedWorkoutViewHolder holder, int position,
                                            @NonNull CompletedWorkoutModelClass model) {

                holder.setCurrentUserId(uid);
                holder.setImperialPOV(isImperial);
                holder.setActivity(getActivity());
                holder.setRefKey(model.getRef());
                holder.setUserId(model.getUserId());
                holder.setPostInfo(model.getWorkoutInfoMap(), getActivity(), getContext(),
                        model.isIsImperial());
                holder.setUpProfilePics(model.getUserId());
                holder.setCommentRecycler(model.getRef());
                holder.setUserName(model.getUserName());
                holder.setUserLevel(model.getUserId(), rootRef);
                holder.setPublicDescription(model.getPublicDescription());
                //holder.setBonusView(model.getBonusList());
                holder.setTimeStamp(model.getDateTime());
                holder.setHasReppedList(model.getHasReppedList());
                //viewHolder.setReppedCount(model.getRepCount());
                //viewHolder.setRepsCounterView(model.getRepCount());
                //viewHolder.setIsRepped(model.isHasRepped(), false);
                //viewHolder.setActivity(getActivity());
                //try{
                //    viewHolder.mBonusView.setText(model.getBonusList().get(0));
                //}catch (NullPointerException e){
                //}
                //viewHolder.setBonusView(model.getBonusList());
                //}
                //}else{
                //    viewHolder.hideLayout();
                //}

            }

            @Override
            public CompletedWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.completed_workout_list_item2,
                                parent, false);

                return new CompletedWorkoutViewHolder(view);
            }
        };

        //firebaseAdapter.setHasStableIds(true);
        loadingView.setVisibility(View.GONE);
        firebaseAdapter.startListening();
        recyclerView.setAdapter(firebaseAdapter);
    }


    @Override
    public void onStart(){
        super.onStart();
        Log.i("feedInfo", "onStart");
        if(firebaseAdapter != null && firebaseAdapter.getItemCount() == 0){
            firebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if(firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }
}
