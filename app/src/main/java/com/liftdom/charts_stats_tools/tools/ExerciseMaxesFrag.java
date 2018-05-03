package com.liftdom.charts_stats_tools.tools;


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
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseMaxesFrag extends Fragment {


    public ExerciseMaxesFrag() {
        // Required empty public constructor
    }

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.noMaxesFound) TextView noMaxesFound;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_maxes, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null){
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        final boolean imperialPOV = userModelClass.isIsImperial();

                        final DatabaseReference maxesRef = FirebaseDatabase.getInstance().getReference().child("maxes")
                                .child(uid);
                        maxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    setUpFirebaseAdapter(maxesRef, imperialPOV);
                                }else{
                                    loadingView.setVisibility(View.GONE);
                                    noMaxesFound.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

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
        recyclerView.setLayoutManager(linearLayoutManager);

        Query query = databaseReference.orderByChild("maxValue");


        loadingView.setVisibility(View.GONE);
    }

}
