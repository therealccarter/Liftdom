package com.liftdom.user_profile.other_profile;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.liftdom.user_profile.UserModelClass;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherUserProfileHeaderFrag extends Fragment {


    public OtherUserProfileHeaderFrag() {
        // Required empty public constructor
    }

    Boolean isFriends = false;

    public String userName;
    public String xUid;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    @BindView(R.id.userName) TextView userNameTextView;
    @BindView(R.id.currentLevel) TextView currentLevel;
    @BindView(R.id.bodyWeight) TextView bodyWeight;
    @BindView(R.id.currentFocus) TextView currentFocus;
    @BindView(R.id.followUserButton) Button followUserButton;
    @BindView(R.id.unFollowUserButton) Button unfollowUserButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_user_profile_info, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        userNameTextView.setText(userName);

        DatabaseReference profileRef = mRootRef.child("user").child(xUid);

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                if(MainActivitySingleton.getInstance().isImperial){
                    bodyWeight.setText(userModelClass.getPounds());
                }else{
                    bodyWeight.setText(userModelClass.getKgs());
                }
                currentLevel.setText(userModelClass.getRepLevel());
                currentFocus.setText(userModelClass.getCurrentFocus());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference followingUsersRef = mRootRef.child("following").child(uid);
        final DatabaseReference followerUsersRef = mRootRef.child("followers").child(xUid);
        followingUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int inc = 0;
                    ArrayList<String> followingUsers = new ArrayList<String>();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        followingUsers.add(dataSnapshot1.getKey());

                        ++inc;
                        if(inc == dataSnapshot.getChildrenCount()){
                            if(followingUsers.contains(xUid)){
                                unfollowUserButton.setVisibility(View.VISIBLE);
                            }else{
                                followUserButton.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                } else {
                    followUserButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        followUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                UserNameIdModelClass otherUserModel = new UserNameIdModelClass(userName, xUid);
                //followingUsersRef.child(xUid).setValue(userName);
                // currentUser is following xUid
                followingUsersRef.child(xUid).setValue(otherUserModel);

                SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);

                UserNameIdModelClass currentUserModel = new UserNameIdModelClass(sharedPref.getString("userName", "loading..."), uid);
                // xUid is being followed by currentUser
                followerUsersRef.child(uid).setValue(currentUserModel);
                //followerUsersRef.child(uid).setValue(mFirebaseUser.getDisplayName());

                followUserButton.setVisibility(View.GONE);
                unfollowUserButton.setVisibility(View.VISIBLE);
            }
        });

        unfollowUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                followingUsersRef.child(xUid).setValue(null);
                followerUsersRef.child(uid).setValue(null);
                unfollowUserButton.setVisibility(View.GONE);
                followUserButton.setVisibility(View.VISIBLE);
            }
        });
        return view;
    }

}
