package com.liftdom.user_profile.single_user_profile;


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
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.notifications_bell.NotificationModelClass;
import com.liftdom.liftdom.notifications_bell.NotificationsActivity;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.your_profile.ProfileInfoActivity;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFrag extends Fragment {


    public ProfileHeaderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uidPov = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String uidFromOutside;
    String mUserName;

    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.currentLevel) TextView currentLevel;
    @BindView(R.id.bodyWeight) TextView bodyWeight;
    @BindView(R.id.currentStreak) TextView currentStreak;
    @BindView(R.id.profileInfo) ImageView infoButton;
    @BindView(R.id.profilePicView) ImageView profilePicView;
    @BindView(R.id.followUserButton) Button followUserButton;
    @BindView(R.id.unFollowUserButton) Button unFollowUserButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.notificationsTextView) TextView notificationCountView;
    @BindView(R.id.notificationsLL) LinearLayout notificationLL;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);

        ButterKnife.bind(this, view);

        if(uidPov.equals(uidFromOutside)){
            notificationLL.setVisibility(View.VISIBLE);
            infoButton.setVisibility(View.VISIBLE);
        }else{
            notificationLL.setVisibility(View.GONE);
            infoButton.setVisibility(View.GONE);
        }

        DatabaseReference profileRef = mRootRef.child("user").child(uidFromOutside);
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                if(uidPov.equals(uidFromOutside)){
                    if(userModelClass.getNotificationCount() != null
                            && !userModelClass.getNotificationCount().isEmpty()
                            && !userModelClass.getNotificationCount().equals("")){
                        if(!userModelClass.getNotificationCount().equals("0")){
                            notificationCountView.setText(userModelClass.getNotificationCount());
                        }
                    }

                    notificationLL.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getContext(), NotificationsActivity.class);
                            startActivity(intent);
                        }
                    });
                }

                userName.setText(userModelClass.getUserName());
                mUserName = userModelClass.getUserName();
                currentLevel.setText(userModelClass.getPowerLevel());
                if(userModelClass.isIsImperial()){
                    String bw = userModelClass.getPounds() + " lbs";
                    bodyWeight.setText(bw);
                }else{
                    String bw = userModelClass.getKgs() + " kgs";
                    bodyWeight.setText(bw);
                }
                currentStreak.setText(userModelClass.getCurrentStreak());

                if(uidPov.equals(uidFromOutside)){
                    loadingView.setVisibility(View.GONE);
                    followUserButton.setVisibility(View.GONE);
                    unFollowUserButton.setVisibility(View.GONE);
                }else{
                    final DatabaseReference followingUsersRef = mRootRef.child("following").child(uidPov).child(uidFromOutside);
                    followingUsersRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                loadingView.setVisibility(View.GONE);
                                unFollowUserButton.setVisibility(View.VISIBLE);
                                followUserButton.setVisibility(View.GONE);
                            } else {
                                loadingView.setVisibility(View.GONE);
                                followUserButton.setVisibility(View.VISIBLE);
                                unFollowUserButton.setVisibility(View.GONE);
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

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                uidFromOutside + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(getActivity()).load(uri).into(profilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                profilePicView.setImageResource(R.drawable.usertest);
            }
        });

        followUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser();
            }
        });

        unFollowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfollowUser();
            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void followUser(){
        followUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        UserNameIdModelClass meModelClass = new UserNameIdModelClass(sharedPref.getString("userName", "loading...")
                , uidPov);
        final UserNameIdModelClass otherModelClass = new UserNameIdModelClass(mUserName, uidFromOutside);
        // first, the OTHER person gains YOU as a follower
        DatabaseReference followerRef = mRootRef.child("followers").child(uidFromOutside).child(uidPov);
        followerRef.setValue(meModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                // second, YOU gain the OTHER person as "following"
                DatabaseReference followingRef = mRootRef.child("following").child(uidPov).child(uidFromOutside);
                followingRef.setValue(otherModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingView.setVisibility(View.GONE);
                        unFollowUserButton.setVisibility(View.VISIBLE);
                    }
                });

                final DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference().child
                        ("user").child(uidFromOutside).child("notificationCount");
                otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        NotificationModelClass notificationModelClass = new NotificationModelClass(
                                "follower", uidPov, null, DateTime.now(DateTimeZone.UTC)
                                .toString(), null);

                        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child
                                ("notifications").child(uidFromOutside);

                        notificationRef.push().setValue(notificationModelClass);

                        if(dataSnapshot.exists()){
                            int count = Integer.parseInt(dataSnapshot.getValue(String.class));
                            count++;
                            otherUserRef.setValue(String.valueOf(count));
                        }else{
                            otherUserRef.setValue("1");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    private void unfollowUser(){
        unFollowUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        DatabaseReference followerRef = mRootRef.child("followers").child(uidFromOutside).child(uidPov);
        followerRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference followingRef = mRootRef.child("following").child(uidPov).child(uidFromOutside);
                followingRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingView.setVisibility(View.GONE);
                        followUserButton.setVisibility(View.VISIBLE);
                    }
                });

                final DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference().child
                        ("user").child(uidFromOutside).child("notificationCount");
                otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        final DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child
                                ("notifications").child(uidFromOutside);

                        notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        NotificationModelClass notificationModelClass = dataSnapshot1.getValue
                                                (NotificationModelClass.class);
                                        if(notificationModelClass.getType().equals("follower") &&
                                                notificationModelClass.getUidFromOutside().equals(uidPov)){
                                            notificationRef.child(dataSnapshot1.getKey()).setValue(null);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        if(dataSnapshot.exists()){
                            int count = Integer.parseInt(dataSnapshot.getValue(String.class));
                            count--;
                            otherUserRef.setValue(String.valueOf(count));
                        }else{
                            otherUserRef.setValue("0");
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
