package com.liftdom.liftdom.main_social_feed;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedWorkoutPostFrag extends Fragment {


    public CompletedWorkoutPostFrag() {
        // Required empty public constructor
    }

    public String userId;
    public String userName;
    public String publicComment;
    public List workoutInfoList;
    public String dateAndTime;
    public HashMap<String, Boolean> repsMap;
    private String userLevel;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

    @BindView(R.id.userName) TextView userNameView;
    @BindView(R.id.workoutContents) TextView workoutContentsView;
    @BindView(R.id.userLevel) TextView userLevelView;
    @BindView(R.id.publicComment) TextView publicCommentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_workout_post, container, false);

        ButterKnife.bind(this, view);

        String workoutInfoTestString = "";

        for(int i = 0; i < workoutInfoList.size(); i++){
            workoutInfoTestString = workoutInfoTestString + workoutInfoList.get(i) + "\n";
        }

        DatabaseReference userLevelRef = mRootRef.child("users").child(userId).child("userLevel");
        userLevelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLevel = dataSnapshot.getValue(String.class);
                String cat = "Level " + userLevel;
                userLevelView.setText(cat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userNameView.setText(userName);

        workoutContentsView.setText(workoutInfoTestString);

        publicCommentView.setText(publicComment);

        return view;
    }

}
