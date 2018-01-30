package com.liftdom.liftdom.main_social_feed.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.w3c.dom.Text;

import java.util.HashMap;

/**
 * Created by Brodin on 1/28/2018.
 */

public class RandomUsersBannerViewHolder extends RecyclerView.ViewHolder {

    private final TextView mUserNameView;
    private final TextView mPowerLevelView;
    private final ImageView mProfilePicView;
    private final Button mFollowUserButton;
    private final Button mUnFollowUserButton;
    private final AVLoadingIndicatorView loadingView;
    private String xUid;
    private String xUserName;
    private String uid;
    Context mContext;

    public RandomUsersBannerViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mPowerLevelView = (TextView) itemView.findViewById(R.id.powerLevelView);
        mProfilePicView = (ImageView) itemView.findViewById(R.id.profilePicView);
        mFollowUserButton = (Button) itemView.findViewById(R.id.followUserButton);
        mUnFollowUserButton = (Button) itemView.findViewById(R.id.unFollowUserButton);
        loadingView = (AVLoadingIndicatorView) itemView.findViewById(R.id.loadingView);

        mFollowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                followUser();
            }
        });

        mUnFollowUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfollowUser();
            }
        });


    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    private void followUser(){
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mFollowUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        String userName = getxUserName();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        SharedPreferences sharedPref = getContext().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
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
                        mUnFollowUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    private void unfollowUser(){
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        mUnFollowUserButton.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        //String userName = getxUserName();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference followerRef = mRootRef.child("followers").child(xUid).child(uid);
        followerRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference followingRef = mRootRef.child("following").child(uid).child(xUid);
                followingRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        loadingView.setVisibility(View.GONE);
                        mFollowUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public String getxUid() {
        return xUid;
    }

    public void setxUid(String xUid) {
        this.xUid = xUid;

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                xUid + "/profilePic.png");
        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(mContext).load(uri).placeholder(R.drawable.usertest).crossFade().into(mProfilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                mProfilePicView.setImageResource(R.drawable.usertest);
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("following")
                .child(getUid()).child(getxUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    mUnFollowUserButton.setVisibility(View.VISIBLE);
                    mFollowUserButton.setVisibility(View.GONE);
                }else{
                    mFollowUserButton.setVisibility(View.VISIBLE);
                    mUnFollowUserButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getxUserName() {
        return xUserName;
    }

    public void setxUserName(String xUserName) {
        this.xUserName = xUserName;
        mUserNameView.setText(xUserName);
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context context) {
        this.mContext = context;
    }
}
