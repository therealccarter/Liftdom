package com.liftdom.user_profile.other_profile;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.FollowersModelClass;
import com.liftdom.liftdom.utils.FollowingModelClass;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.liftdom.misc_activities.SettingsListActivity;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

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
    @BindView(R.id.profilePicImageView) ImageView profilePicView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_user_profile_info, container, false);

        ButterKnife.bind(this, view);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        final StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                xUid + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(getActivity()).load(uri).placeholder(R.drawable.usertest).crossFade().into(profilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                profilePicView.setImageResource(R.drawable.usertest);
            }
        });

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
                currentLevel.setText(userModelClass.getPowerLevel());
                currentFocus.setText(userModelClass.getCurrentFocus());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference followingUsersRef = mRootRef.child("following").child(uid).child(xUid);
        followingUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                   unfollowUserButton.setVisibility(View.VISIBLE);
                   followUserButton.setVisibility(View.GONE);
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
               followUser();
            }
        });

        unfollowUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                unfollowUser();
            }
        });

        return view;
    }

    private void followUser(){
        followUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        UserNameIdModelClass meModelClass = new UserNameIdModelClass(sharedPref.getString("userName", "loading..."), uid);
        final UserNameIdModelClass otherModelClass = new UserNameIdModelClass(userName, xUid);
        // first, the OTHER person gains YOU as a follower
        DatabaseReference followerRef = mRootRef.child("followers").child(xUid).child(uid);
        followerRef.setValue(meModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // second, YOU gain the OTHER person as "following"
                DatabaseReference followingRef = mRootRef.child("following").child(uid).child(xUid);
                followingRef.setValue(otherModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingView.setVisibility(View.GONE);
                        unfollowUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void unfollowUser(){
        unfollowUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        DatabaseReference followerRef = mRootRef.child("followers").child(xUid).child(uid);
        followerRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference followingRef = mRootRef.child("following").child(uid).child(xUid);
                followingRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingView.setVisibility(View.GONE);
                        followUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

}
