package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.*;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.comment_post.CommentsHolderFrag;
import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentModelClass;
import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentViewHolder;
import com.liftdom.liftdom.main_social_feed.utils.PostExNameFrag;
import com.liftdom.liftdom.main_social_feed.utils.PostExNameSSFrag;
import com.liftdom.liftdom.main_social_feed.utils.PostSetSchemeFrag;
import com.liftdom.liftdom.main_social_feed.utils.PostSetSchemeSSFrag;
import com.liftdom.liftdom.utils.FollowersModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Brodin on 5/6/2017.
 */

public class CompletedWorkoutViewHolder extends RecyclerView.ViewHolder{

    private final TextView mUserNameView;
    private final TextView mUserLevelView;
    private final TextView mPublicDescriptionView;
    private final TextView mTimestampView;
    private String xUid;
    private FragmentActivity mActivity;
    private String mUserName;
    private final RecyclerView mInfoRecyclerView;
    private final LinearLayout mPostInfoHolder;
    private final ImageButton mSendCommentButton;
    private final EditText mCommentEditText;
    private final ImageView mUserProfilePic;
    private final ImageView xProfilePic;
    private String mRefKey;
    private final RecyclerView mCommentRecyclerView;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef;
    private final TextView mBonusView;
    //private final LinearLayout mCommentFragHolder;

    public CompletedWorkoutViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userName);
        mUserLevelView = (TextView) itemView.findViewById(R.id.userLevel);
        mTimestampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mPublicDescriptionView = (TextView) itemView.findViewById(R.id.publicDescription);
        mInfoRecyclerView = (RecyclerView) itemView.findViewById(R.id.infoRecyclerView);
        mPostInfoHolder = (LinearLayout) itemView.findViewById(R.id.postInfoHolder);
        mSendCommentButton = (ImageButton) itemView.findViewById(R.id.sendCommentButton);
        mCommentEditText = (EditText) itemView.findViewById(R.id.commentEditText);
        mUserProfilePic = (ImageView) itemView.findViewById(R.id.currentUserProfilePic);
        xProfilePic = (ImageView) itemView.findViewById(R.id.profilePic);
        mCommentRecyclerView = (RecyclerView) itemView.findViewById(R.id.commentsRecyclerView);
        mBonusView = (TextView) itemView.findViewById(R.id.bonusView);
        //mCommentFragHolder = (LinearLayout) itemView.findViewById(R.id.commentFragHolder);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();


        mPostInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(getCurrentUid().equals(xUid)){
                    Intent intent = new Intent(mActivity, CurrentUserProfile.class);
                    mActivity.startActivity(intent);
                } else {
                    FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    otherUserProfileFrag.userName = mUserName;
                    otherUserProfileFrag.xUid = xUid;

                    fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        mSendCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!mCommentEditText.getText().toString().equals("")){

                    DatabaseReference userRef = mRootRef.child("user").child(getCurrentUid());
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                                    (getCurrentUid()).child(mRefKey).child("commentMap");

                            String refKey = commentRef.push().getKey();

                            PostCommentModelClass commentModelClass = new PostCommentModelClass(
                                    userModelClass.getUserId(), userModelClass.getUserName(), mCommentEditText
                                    .getText().toString(), 0, DateTime.now(DateTimeZone.UTC).toString(), refKey);

                            commentRef.child(refKey).setValue(commentModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    mCommentEditText.setText("");
                                }
                            });

                            fanoutCommentPost(refKey, commentModelClass);

                            //mCommentRecyclerView.refreshDrawableState();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    Map fanoutObject = new HashMap<>();

    private void fanoutCommentPost(final String commentRefKey, final PostCommentModelClass commentModelClass){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(xUid);

        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)  {
                if(dataSnapshot.exists()){
                    FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);

                    final List<String> userList = new ArrayList<>();

                    if(followersModelClass.getUserIdList() != null){
                        userList.addAll(followersModelClass.getUserIdList());


                        if(!getCurrentUid().equals(xUid)){
                            userList.add(xUid);
                        }


                        for(int i = 0; i < userList.size(); i++){
                            final int inc = i;
                            if(!userList.get(i).equals(getCurrentUid())){
                                DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child
                                        ("feed").child(userList.get(i)).child(mRefKey);
                                postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        if(dataSnapshot.exists()){fanoutObject.put("/feed/" + userList.get(inc) +
                                                "/" + mRefKey + "/commentMap/" + commentRefKey, commentModelClass);
                                        }
                                        if(inc == userList.size() - 1){
                                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                            rootRef.updateChildren(fanoutObject);
                                            fanoutObject.clear();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }
                        }


                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setBonusView(List<String> bonusList){
        String bonusString = "";
        for(String string : bonusList){
            bonusString = bonusString + "\n" + string;
        }
        mBonusView.setText(bonusString);
        mBonusView.setVisibility(View.VISIBLE);
    }

    public void setUpProfilePics(String postUid){
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

        StorageReference currentProfilePicRef = storageRef.child("images/user/" + getCurrentUid() + "/profilePic.png");
        currentProfilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mActivity).load(uri).crossFade().into(mUserProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                mUserProfilePic.setImageResource(R.drawable.usertest);
            }
        });

        StorageReference posterProfilePicRef = storageRef.child("images/user/" + postUid + "/profilePic.png");
        posterProfilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(mActivity).load(uri).crossFade().into(xProfilePic);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                //xProfilePic.setImageResource(R.drawable.usertest);
            }
        });
    }

    public void setCommentRecycler(String refKey){
        mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                (getCurrentUid()).child(refKey).child("commentMap");

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(dataSnapshot.exists()){
                    mFirebaseAdapter = new FirebaseRecyclerAdapter<PostCommentModelClass, PostCommentViewHolder>
                            (PostCommentModelClass.class, R.layout.post_comment_list_item, PostCommentViewHolder.class, mFeedRef) {
                        @Override
                        protected void populateViewHolder(PostCommentViewHolder viewHolder, PostCommentModelClass model, int position) {
                            viewHolder.setComment(model.getCommentText());
                            viewHolder.setDateString(model.getDateString());
                            viewHolder.setRepNumber(model.getRepNumber());
                            viewHolder.setRefKey(model.getRefKey());
                            viewHolder.setUsername(model.getUserName());
                            viewHolder.setParentUid(xUid);
                            viewHolder.setParentRefKey(getRefKey());
                            viewHolder.setCommentUid(model.getUserId());
                            viewHolder.setContext(mActivity);
                            viewHolder.setActivity(mActivity);
                        }
                    };

                    mCommentRecyclerView.setHasFixedSize(false);
                    mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
                    mCommentRecyclerView.setAdapter(mFirebaseAdapter);
                //}
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private String getCurrentUid(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }

    public void setRefKey(String refKey){
        mRefKey = refKey;
    }

    public String getRefKey(){
        return mRefKey;
    }

    public void setUserId(String userId){
        xUid = userId;
    }

    public void setActivity(FragmentActivity fragmentActivity){
        mActivity = fragmentActivity;
    }

    public void setUserName(String userName){
        mUserNameView.setText(userName);
        mUserName = userName;
    }

    public void setUserLevel(String userId, DatabaseReference mRootRef){
        DatabaseReference levelRef = mRootRef.child("user").child(userId);
        levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass otherUserModelClass = dataSnapshot.getValue(UserModelClass.class);
                String repsLevel = otherUserModelClass.getRepLevel();
                String powerLevel = otherUserModelClass.getPowerLevel();
                mUserLevelView.setText(powerLevel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setPublicDescription(String publicDescription){
        mPublicDescriptionView.setText(publicDescription);
    }

    public void setTimeStamp(String timeStamp){
        DateTime dateTimeOriginal = DateTime.parse(timeStamp);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");
        mTimestampView.setText(formattedLocalDate);
    }

    public void setPostInfo(HashMap<String, List<String>> workoutInfoMap, FragmentActivity activity, Context context,
                            boolean isImperial){
        WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(workoutInfoMap, context);
        //adapter.setInfoList(workoutInfoMap);
        adapter.setIsOriginallyImperial(isImperial);
        mInfoRecyclerView.setAdapter(adapter);
        mInfoRecyclerView.setHasFixedSize(false);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }
}
