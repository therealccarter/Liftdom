package com.liftdom.user_profile.other_profile;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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


        final DatabaseReference followingUsersRef = mRootRef.child("following").child(uid);
        final DatabaseReference followerUsersRef = mRootRef.child("followers").child(xUid);
        followingUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FollowingModelClass followingModelClass = dataSnapshot.getValue(FollowingModelClass.class);
                    if(followingModelClass.getFollowingMap().containsKey(xUid)){
                        unfollowUserButton.setVisibility(View.VISIBLE);
                        followUserButton.setVisibility(View.GONE);
                    }else{
                        unfollowUserButton.setVisibility(View.GONE);
                        followUserButton.setVisibility(View.VISIBLE);
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
                // first, the OTHER person gains YOU as a follower
                final DatabaseReference otherFollowerRef = mRootRef.child("followers").child(xUid);
                otherFollowerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                            FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);
                            followersModelClass.addFollowerToMap(uid, sharedPref.getString("userName", "loading..."));
                            otherFollowerRef.setValue(followersModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // second, YOU gain the OTHER person as "following"
                                    final DatabaseReference myFollowingList = mRootRef.child("following").child(uid);
                                    myFollowingList.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                FollowingModelClass followingModelClass = dataSnapshot.getValue(FollowingModelClass.class);
                                                followingModelClass.addFollowingToMap(xUid, userName);
                                                myFollowingList.setValue(followingModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        followUserButton.setVisibility(View.GONE);
                                                        unfollowUserButton.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }else{
                                                FollowingModelClass followingModelClass = new FollowingModelClass(xUid, userName);
                                                myFollowingList.setValue(followingModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        followUserButton.setVisibility(View.GONE);
                                                        unfollowUserButton.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }else{
                            SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                            FollowersModelClass followersModelClass = new FollowersModelClass(uid, sharedPref.getString("userName", "loading..."));
                            otherFollowerRef.setValue(followersModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // second, YOU gain the OTHER person as "following"
                                    final DatabaseReference myFollowingList = mRootRef.child("following").child(uid);
                                    myFollowingList.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if(dataSnapshot.exists()){
                                                FollowingModelClass followingModelClass = dataSnapshot.getValue(FollowingModelClass.class);
                                                followingModelClass.addFollowingToMap(xUid, userName);
                                                myFollowingList.setValue(followingModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        followUserButton.setVisibility(View.GONE);
                                                        unfollowUserButton.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }else{
                                                FollowingModelClass followingModelClass = new FollowingModelClass(xUid, userName);
                                                myFollowingList.setValue(followingModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        followUserButton.setVisibility(View.GONE);
                                                        unfollowUserButton.setVisibility(View.VISIBLE);
                                                    }
                                                });
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                //UserNameIdModelClass otherUserModel = new UserNameIdModelClass(userName, xUid);
                ////followingUsersRef.child(xUid).setValue(userName);
                //// currentUser is following xUid
                //followingUsersRef.child(xUid).setValue(otherUserModel);
                //UserNameIdModelClass currentUserModel = new UserNameIdModelClass(sharedPref.getString("userName",
                //        "loading..."), uid);
                //// xUid is being followed by currentUser
                //followerUsersRef.child(uid).setValue(currentUserModel);
                ////followerUsersRef.child(uid).setValue(mFirebaseUser.getDisplayName());

            }
        });

        unfollowUserButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                followingUsersRef.child(xUid).child(uid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        followerUsersRef.child(uid).child(xUid).setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                unfollowUserButton.setVisibility(View.GONE);
                                followUserButton.setVisibility(View.VISIBLE);
                            }
                        });
                    }
                });


            }
        });
        return view;
    }

}
