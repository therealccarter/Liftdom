package com.liftdom.liftdom.main_social_feed.comment_post;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
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
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.single_user_profile.UserProfileFullActivity;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;

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
    //private final ImageButton mRepButton;
    //private final ImageView mUserProfilePic;
    private final ImageView mDeleteCommentButton;
    private String mRefKey;
    private int mRepNumber;
    private String mDateString;
    private String parentUid;
    private String parentRefKey;
    private String commentUid;
    private Context mContext;
    private FragmentActivity mActivity;
    private String mUserName;
    private HashMap<String, List<String>> mInfoMap;


    public PostCommentViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mCommentView = (TextView) itemView.findViewById(R.id.commentTextView);
        //mRepButton = (ImageButton) itemView.findViewById(R.id.repButton);
        //mUserProfilePic = (ImageView) itemView.findViewById(R.id.userProfilePic);
        mDeleteCommentButton = (ImageView) itemView.findViewById(R.id.deleteCommentButton);

        mDeleteCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteComment();
            }
        });

        mUserNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserProfileFullActivity.class);
                if(getCurrentUid().equals(commentUid)){
                    getActivity().startActivity(intent);
                } else {
                    intent.putExtra("xUid", commentUid);
                    getActivity().startActivity(intent);
                    //FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                    //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
                    //OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    //otherUserProfileFrag.userName = getUserName();
                    //otherUserProfileFrag.xUid = commentUid;
//
                    //fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    //fragmentTransaction.addToBackStack(null);
                    //fragmentTransaction.commit();
                }
            }
        });
    }

    public HashMap<String, List<String>> getInfoMap() {
        return mInfoMap;
    }

    public void setInfoMap(HashMap<String, List<String>> mInfoMap) {
        this.mInfoMap = mInfoMap;
    }

    public FragmentActivity getActivity() {
        return mActivity;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    private void deleteComment(){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(getParentUid());

        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map fanoutObject = new HashMap<>();
                    int inc = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String key = dataSnapshot1.getKey();
                        fanoutObject.put("/feed/" + key + "/" + parentRefKey + "/commentMap/" + mRefKey,
                                null);
                        inc++;
                        if(inc == dataSnapshot.getChildrenCount()){
                            fanoutObject.put("/selfFeed/" + getParentUid() + "/" + parentRefKey + "/commentMap/" +
                                            mRefKey, null);
                            fanoutObject.put("/feed/" + getParentUid() + "/" + parentRefKey + "/commentMap/" + mRefKey,
                                    null);
                            if(getInfoMap() != null){
                                fanoutObject.put("/globalFeed/" + parentRefKey + "/commentMap/" + mRefKey,
                                        null);
                            }
                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                            rootRef.updateChildren(fanoutObject);
                        }
                    }
                }else{
                    DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                            (getParentUid()).child(parentRefKey).child("commentMap").child(mRefKey);
                    DatabaseReference commentSelfRef = FirebaseDatabase.getInstance().getReference().child("selfFeed")
                            .child(getParentUid()).child(parentRefKey).child("commentMap").child(mRefKey);
                    DatabaseReference commentGlobalRef = FirebaseDatabase.getInstance().getReference().child
                            ("globalFeed").child(parentRefKey).child("commentMap").child(mRefKey);
                    commentRef.setValue(null);
                    commentSelfRef.setValue(null);
                    if(getInfoMap() != null){
                        commentGlobalRef.setValue(null);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        final DatabaseReference parentNotificationRef = FirebaseDatabase.getInstance().getReference().child
                ("notifications").child(parentUid).child(mRefKey);
        parentNotificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final DatabaseReference parentUserRef = FirebaseDatabase.getInstance().getReference().child("user").child
                        (parentUid).child("notificationCount");
                parentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int currentCount = Integer.parseInt(dataSnapshot.getValue(String.class));
                            currentCount--;
                            parentUserRef.setValue(String.valueOf(currentCount));
                        }else{
                            parentUserRef.setValue("0");
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                if(dataSnapshot.exists()){
                    parentNotificationRef.setValue(null);
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

        //StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
        //        commentUid + "/profilePic.png");
//
        //profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        //    @Override
        //    public void onSuccess(Uri uri) {
        //        Log.i("glide", "success");
        //        Glide.with(getContext()).load(uri).crossFade().into(mUserProfilePic);
        //    }
        //}).addOnFailureListener(new OnFailureListener() {
        //    @Override
        //    public void onFailure(@NonNull Exception e) {
        //        Log.i("glide", "failure");
        //        mUserProfilePic.setImageResource(R.drawable.usertest);
        //    }
        //});
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
        mUserName = username;
    }

    public String getUserName(){
        return mUserName;
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
