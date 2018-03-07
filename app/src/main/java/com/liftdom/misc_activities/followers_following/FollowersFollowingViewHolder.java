package com.liftdom.misc_activities.followers_following;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * Created by Brodin on 10/11/2017.
 */

public class FollowersFollowingViewHolder extends RecyclerView.ViewHolder{

    private String xUid;
    private String uid;
    private String userName;
    private final ImageView mProfilePicView;
    private final TextView mUserNameView;
    private final Button mFollowUserButton;
    private final Button mUnFollowUserButton;
    private final AVLoadingIndicatorView mLoadingView;
    Context mContext;
    FragmentActivity mActivity;
    private final LinearLayout parentLL;
    private String yourUserName;


    public FollowersFollowingViewHolder(View itemView){
        super(itemView);
        mProfilePicView = (ImageView) itemView.findViewById(R.id.profilePicView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mFollowUserButton = (Button) itemView.findViewById(R.id.followUserButton);
        mUnFollowUserButton = (Button) itemView.findViewById(R.id.unFollowUserButton);
        mLoadingView = (AVLoadingIndicatorView) itemView.findViewById(R.id.loadingView);
        parentLL = (LinearLayout) itemView.findViewById(R.id.parentLL);

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

    private void followUser(){
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mFollowUserButton.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);

        UserNameIdModelClass meModelClass = new UserNameIdModelClass(yourUserName, uid);
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
                        mLoadingView.setVisibility(View.GONE);
                        mUnFollowUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public String getYourUserName() {
        return yourUserName;
    }

    public void setYourUserName(String yourUserName) {
        this.yourUserName = yourUserName;
    }

    private void unfollowUser(){
        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mUnFollowUserButton.setVisibility(View.GONE);
        mLoadingView.setVisibility(View.VISIBLE);

        DatabaseReference followerRef = mRootRef.child("followers").child(xUid).child(uid);
        followerRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                DatabaseReference followingRef = mRootRef.child("following").child(uid).child(xUid);
                followingRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        mLoadingView.setVisibility(View.GONE);
                        mFollowUserButton.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
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
                Glide.with(mContext).load(uri).placeholder(R.drawable.usertest).into(mProfilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                mProfilePicView.setImageResource(R.drawable.usertest);
            }
        });
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String username) {
        this.userName = username;
        mUserNameView.setText(username);
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
        DatabaseReference followingSpecificRef = FirebaseDatabase.getInstance().getReference().child("following")
                .child(uid).child(xUid);
        followingSpecificRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    if(!uid.equals(xUid)){
                        mLoadingView.setVisibility(View.GONE);
                        mFollowUserButton.setVisibility(View.GONE);
                        mUnFollowUserButton.setVisibility(View.VISIBLE);
                    }
                }else{
                    mLoadingView.setVisibility(View.GONE);
                    mFollowUserButton.setVisibility(View.VISIBLE);
                    mUnFollowUserButton.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void hideLayout(){
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, 0);
        parentLL.setLayoutParams(layoutParams);
    }
}