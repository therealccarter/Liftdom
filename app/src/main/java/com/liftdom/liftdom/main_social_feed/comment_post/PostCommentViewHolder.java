package com.liftdom.liftdom.main_social_feed.comment_post;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.FollowersModelClass;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 6/26/2017.
 */

public class PostCommentViewHolder extends RecyclerView.ViewHolder{

    private final TextView mUserNameView;
    private final TextView mCommentView;
    private final ImageButton mRepButton;
    private final ImageView mUserProfilePic;
    private final ImageView mDeleteCommentButton;
    private String mRefKey;
    private int mRepNumber;
    private String mDateString;
    private String parentUid;
    private String parentRefKey;
    private String commentUid;
    private Context mContext;

    public PostCommentViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mCommentView = (TextView) itemView.findViewById(R.id.commentTextView);
        mRepButton = (ImageButton) itemView.findViewById(R.id.repButton);
        mUserProfilePic = (ImageView) itemView.findViewById(R.id.userProfilePic);
        mDeleteCommentButton = (ImageView) itemView.findViewById(R.id.deleteCommentButton);

        mDeleteCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment();
            }
        });
    }

    private void deleteComment(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(getParentUid());

        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);

                    List<String> userList = new ArrayList<>();

                    if(followersModelClass.getUserIdList() != null){
                        userList.addAll(followersModelClass.getUserIdList());

                        Map fanoutObject = new HashMap<>();

                        //if(!getCurrentUid().equals(getParentUid())){
                            userList.add(getParentUid());
                        //}

                        for(String user : userList){
                            //if(!user.equals(getCurrentUid())){
                                fanoutObject.put("/feed/" + user + "/" + parentRefKey + "/commentMap/" + mRefKey,
                                        null);
                            //}
                        }

                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                        rootRef.updateChildren(fanoutObject);
                    }
                }else{
                    DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                            (getParentUid()).child(parentRefKey).child("commentMap").child(mRefKey);
                    commentRef.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getCommentUid() {
        return commentUid;
    }

    public void setCommentUid(String commentUid) {
        this.commentUid = commentUid;
        if(commentUid.equals(getCurrentUid())){
            mDeleteCommentButton.setVisibility(View.VISIBLE);
        }

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                commentUid + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(getContext()).load(uri).crossFade().into(mUserProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                mUserProfilePic.setImageResource(R.drawable.usertest);
            }
        });
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getParentRefKey() {
        return parentRefKey;
    }

    public void setParentRefKey(String parentRefKey) {
        this.parentRefKey = parentRefKey;
    }

    private String getCurrentUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public String getParentUid() {
        return parentUid;
    }

    public void setParentUid(String parentUid) {
        this.parentUid = parentUid;
    }

    public void setUsername(String username){
        mUserNameView.setText(username);
    }

    public void setComment(String comment){
        mCommentView.setText(comment);
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public int getRepNumber() {
        return mRepNumber;
    }

    public void setRepNumber(int mRepNumber) {
        this.mRepNumber = mRepNumber;
    }

    public String getDateString() {
        return mDateString;
    }

    public void setDateString(String mDateString) {
        this.mDateString = mDateString;
    }
}
