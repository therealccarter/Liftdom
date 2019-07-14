package com.liftdom.user_profile;


import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.misc_activities.followers_following.FollowersFollowingDialogActivity;
import com.liftdom.user_profile.single_user_profile.ProfileActionsDialogActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileOptionsTabFrag extends Fragment {


    public ProfileOptionsTabFrag() {
        // Required empty public constructor
    }

    public String uid;
    public String xUid;
    public boolean isOtherUser;

    @BindView(R.id.followersCountTextView) TextView followerCountView;
    @BindView(R.id.followingCountTextView) TextView followingCountView;
    @BindView(R.id.followersTitleView) TextView followersTitleView;
    @BindView(R.id.followingTitleView) TextView followingTitleView;
    @BindView(R.id.sendMessageLinearLayout) LinearLayout sendMessageLayout;
    @BindView(R.id.sendProgramLinearLayout) LinearLayout sendProgramLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_options_tab, container, false);

        ButterKnife.bind(this, view);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(xUid.equals(uid)){
            // your profile
            //xUid = uid;
        }else{

        }

        //if(!isOtherUser){
        //    // is you
        //    xUid = uid;
        //}else{
        //    // uncomment this to get message view up
        //    //sendMessageLayout.setVisibility(View.VISIBLE);
        //}

        //TODO: really need to get that back arrow going..

        sendMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPrivateMessage();
            }
        });

        sendProgramLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startProgramMessage();
            }
        });

        followerCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowersActivity();
            }
        });

        followersTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowersActivity();
            }
        });

        followingCountView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowingActivity();
            }
        });

        followingTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startFollowingActivity();
            }
        });


        return view;
    }

    private void startPrivateMessage(){
        Intent intent = new Intent(getContext(), ProfileActionsDialogActivity.class);
        intent.putExtra("action", "1");
        intent.putExtra("uidFromOutside", xUid);
        startActivity(intent);
    }

    private void startProgramMessage(){
        Intent intent = new Intent(getContext(), ProfileActionsDialogActivity.class);
        intent.putExtra("action", "2");
        intent.putExtra("uidFromOutside", xUid);
        startActivity(intent);
    }

    private void startFollowersActivity(){
        Intent intent = new Intent(getContext(), FollowersFollowingDialogActivity.class);
        intent.putExtra("type", "followers");
        intent.putExtra("uid", xUid);
        startActivity(intent);
    }

    private void startFollowingActivity(){
        Intent intent = new Intent(getContext(), FollowersFollowingDialogActivity.class);
        intent.putExtra("type", "following");
        intent.putExtra("uid", xUid);
        startActivity(intent);
    }

    @Override
    public void onStart(){
        super.onStart();

        DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("followers").
                child(xUid);
        followerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int size = (int) dataSnapshot.getChildrenCount();
                    followerCountView.setText(String.valueOf(size));
                }else{
                    followerCountView.setText("0");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").
                child(xUid);
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int size = (int) dataSnapshot.getChildrenCount();
                    followingCountView.setText(String.valueOf(size));
                }else{
                    followingCountView.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}
