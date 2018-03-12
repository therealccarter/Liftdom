package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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
import com.liftdom.liftdom.WorkoutPostSingleActivity;
import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentModelClass;
import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentViewHolder;
import com.liftdom.liftdom.notifications_bell.NotificationModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.single_user_profile.UserProfileFullActivity;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private final Button mGoToAllCommentsButton;
    private int mCommentCount;
    private Query recentMessages;
    private boolean isFullComments;
    private final LinearLayout mAllCommentsLL;
    private boolean isImperialPOV;
    private final TextView mRepsCounterView;
    private final ImageView mRepsIconWhite;
    private final ImageView mRepsIconGold;
    //private final LinearLayout mPostInfoHolderLL;
    //private final BannerView mBannerView;
    private int mPosition;
    //private final LinearLayout mCommentFragHolder;
    private final CardView mCardViewParent;
    private boolean isRepped;
    private int reppedCount;
    private final AVLoadingIndicatorView mLoadingReppedView;
    final CardView.LayoutParams params;
    private boolean mIsSelfFeed;
    private List<String> mHasReppedList;
    private String mCurrentUserId;

    public CompletedWorkoutViewHolder(View itemView){
        super(itemView);
        mCardViewParent = (CardView) itemView.findViewById(R.id.cardViewParent);
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
        mGoToAllCommentsButton = (Button) itemView.findViewById(R.id.goToAllCommentsButton);
        //mCommentFragHolder = (LinearLayout) itemView.findViewById(R.id.commentFragHolder);
        mAllCommentsLL = (LinearLayout) itemView.findViewById(R.id.allCommentsLinearLayout);
        mRepsCounterView = (TextView) itemView.findViewById(R.id.repsCountTextView);
        mRepsIconWhite = (ImageView) itemView.findViewById(R.id.repsImageWhite);
        mRepsIconGold = (ImageView) itemView.findViewById(R.id.repsImageGold);
        mLoadingReppedView = (AVLoadingIndicatorView) itemView.findViewById(R.id.loadingReppedView);
        //mBannerView = (BannerView) itemView.findViewById(R.id.appodealBannerView);
        //mPostInfoHolderLL = (LinearLayout) itemView.findViewById(R.id.postInfoHolderLL);

        params = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();

        mPostInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserProfileFullActivity.class);
                if(getCurrentUid().equals(xUid)){
                    mActivity.startActivity(intent);
                } else {
                    intent.putExtra("xUid", xUid);
                    mActivity.startActivity(intent);
                    //FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                    //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
                    //OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    //otherUserProfileFrag.userName = mUserName;
                    //otherUserProfileFrag.xUid = xUid;
//
                    //fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    //fragmentTransaction.addToBackStack(null);
                    //fragmentTransaction.commit();
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
                            DatabaseReference parentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                                    (getCurrentUid()).child(mRefKey).child("commentCount");

                            final String refKey = commentRef.push().getKey();

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

                            final DatabaseReference otherUserRef = FirebaseDatabase.getInstance().getReference().child
                                    ("user").child(xUid).child("notificationCount");
                            otherUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {

                                    NotificationModelClass notificationModelClass = new NotificationModelClass(
                                            "comment", getCurrentUid(), getRefKey(), DateTime.now(DateTimeZone.UTC)
                                            .toString(), refKey);

                                    DatabaseReference otherUserNotificationRef = FirebaseDatabase.getInstance()
                                            .getReference().child("notifications").child(xUid).child(refKey);
                                    otherUserNotificationRef.setValue(notificationModelClass);

                                    if(dataSnapshot.exists()){
                                        int currentCount = Integer.parseInt(dataSnapshot.getValue(String.class));
                                        currentCount++;
                                        otherUserRef.setValue(String.valueOf(currentCount));
                                    }else{
                                        otherUserRef.setValue("1");
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });



                            //mCommentRecyclerView.refreshDrawableState();

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        mGoToAllCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, WorkoutPostSingleActivity.class);
                intent.putExtra("refKey", mRefKey);
                mActivity.startActivity(intent);
            }
        });

        mRepsIconWhite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // add rep
                addRepToPost();
            }
        });

        mRepsIconGold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // remove rep
                removeRepFromPost();
            }
        });
    }

    public void hideLayout(){
        params.height = 0;
        mCardViewParent.setLayoutParams(params);
    }

    public void setIsRepped(boolean isRepped1, boolean isSelfFeed){
        if(!isSelfFeed){
            // initial
            if(isRepped1){
                mRepsIconWhite.setVisibility(View.GONE);
                mLoadingReppedView.setVisibility(View.GONE);
                mRepsIconGold.setVisibility(View.VISIBLE);
            }else{
                mRepsIconWhite.setVisibility(View.VISIBLE);
                mLoadingReppedView.setVisibility(View.GONE);
                mRepsIconGold.setVisibility(View.GONE);
            }
        }
    }

    private Map fanoutReppedRemoveObject = new HashMap<>();

    private int reppedRemoveInc1 = 0;

    private Map fanoutReppedObject = new HashMap<>();

    private int reppedInc1 = 0;

    public List<String> getHasReppedList() {
        return mHasReppedList;
    }

    public void setHasReppedList(List<String> mHasReppedList) {
        this.mHasReppedList = mHasReppedList;

        if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
            int repsCount = getHasReppedList().size();
            mRepsCounterView.setVisibility(View.VISIBLE);
            mRepsCounterView.setText(String.valueOf(repsCount));
            if(getHasReppedList().contains(getCurrentUid())){
                mRepsIconWhite.setVisibility(View.GONE);
                mLoadingReppedView.setVisibility(View.GONE);
                mRepsIconGold.setVisibility(View.VISIBLE);
            }else{
                mRepsIconWhite.setVisibility(View.VISIBLE);
                mLoadingReppedView.setVisibility(View.GONE);
                mRepsIconGold.setVisibility(View.GONE);
            }
        }else{
            mRepsIconWhite.setVisibility(View.VISIBLE);
            mLoadingReppedView.setVisibility(View.GONE);
            mRepsIconGold.setVisibility(View.GONE);
            mRepsCounterView.setVisibility(View.GONE);
            mRepsCounterView.setText(String.valueOf(getReppedCount()));
        }

    }

    private void removeRepFromPost(){
        mRepsIconGold.setVisibility(View.GONE);
        mLoadingReppedView.setVisibility(View.VISIBLE);
        final int newReppedCount = getReppedCount() - 1;

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(xUid).child
                ("notificationCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child
                        ("notifications").child(xUid);

                notificationRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                NotificationModelClass notificationModelClass = dataSnapshot1.getValue
                                        (NotificationModelClass.class);
                                if(notificationModelClass.getRefKey().equals(mRefKey) && notificationModelClass
                                        .getType().equals("rep")){
                                    notificationRef.child(dataSnapshot1.getKey()).setValue(null);
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                notificationRef.push().setValue(null);

                if(dataSnapshot.exists()){
                    int currentCount = Integer.parseInt(dataSnapshot.getValue(String.class));
                    currentCount--;
                    userRef.setValue(String.valueOf(currentCount));
                }else{
                    userRef.setValue("0");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(xUid);
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    final int childCount = (int) dataSnapshot.getChildrenCount();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        reppedRemoveInc1++;
                        final String key = dataSnapshot1.getKey();
                        //if(!key.equals(getCurrentUid())){
                        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child
                                ("feed").child(key).child(mRefKey);
                        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(!key.equals(getCurrentUid())){
                                        if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                                            List<String> hasReppedList;
                                            hasReppedList = getHasReppedList();
                                            hasReppedList.remove(getCurrentUid());
                                            fanoutReppedRemoveObject.put("/feed/" + key +
                                                    "/" + mRefKey + "/hasReppedList/", hasReppedList);
                                        }
                                        //fanoutReppedRemoveObject.put("/feed/" + key +
                                        //        "/" + mRefKey + "/repCount/", newReppedCount);
                                    }
                                    if(reppedRemoveInc1 == childCount){
                                        List<String> hasReppedList = new ArrayList<>();
                                        if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                                            hasReppedList = getHasReppedList();
                                            hasReppedList.remove(getCurrentUid());
                                        }

                                        if(!getCurrentUid().equals(xUid)){
                                            fanoutReppedRemoveObject.put("/feed/" + xUid +
                                                    "/" + mRefKey + "/hasReppedList/", hasReppedList);
                                        }

                                        fanoutReppedRemoveObject.put("/feed/" + getCurrentUid() +
                                                "/" + mRefKey + "/hasReppedList/", hasReppedList);

                                        fanoutReppedRemoveObject.put("/selfFeed/" + xUid +
                                                "/" + mRefKey + "/hasReppedList/", hasReppedList);

                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                        rootRef.updateChildren(fanoutReppedRemoveObject).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                mLoadingReppedView.setVisibility(View.GONE);
                                                mRepsIconWhite.setVisibility(View.VISIBLE);
                                                //mRepsCounterView.setText(String.valueOf(reppedCount));
                                                //mRepsCounterView.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        fanoutReppedRemoveObject.clear();
                                        reppedRemoveInc1 = 0;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //}
                    }
                }else{
                    //fanoutReppedRemoveObject.put("/feed/" + getCurrentUid() +
                    //        "/" + mRefKey + "/repCount/", newReppedCount);
//
                    //fanoutReppedRemoveObject.put("/feed/" + getCurrentUid() +
                    //        "/" + mRefKey + "/hasRepped/", false);
//
                    //fanoutReppedRemoveObject.put("/selfFeed/" + xUid +
                    //        "/" + mRefKey + "/repCount/", newReppedCount);
//
//
                    //if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                    //    List<String> hasReppedList;
                    //    hasReppedList = getHasReppedList();
                    //    hasReppedList.remove(getCurrentUid());
                    //    fanoutReppedRemoveObject.put("/selfFeed/" + xUid +
                    //            "/" + mRefKey + "/hasReppedList/", hasReppedList);
                    //}

                    List<String> hasReppedList = new ArrayList<>();
                    if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                        hasReppedList = getHasReppedList();
                        hasReppedList.remove(getCurrentUid());
                    }

                    fanoutReppedRemoveObject.put("/feed/" + getCurrentUid() +
                            "/" + mRefKey + "/hasReppedList/", hasReppedList);

                    fanoutReppedRemoveObject.put("/selfFeed/" + xUid +
                            "/" + mRefKey + "/hasReppedList/", hasReppedList);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.updateChildren(fanoutReppedRemoveObject).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            mLoadingReppedView.setVisibility(View.GONE);
                            mRepsIconWhite.setVisibility(View.VISIBLE);
                            //mRepsCounterView.setText(String.valueOf(reppedCount));
                            //mRepsCounterView.setVisibility(View.VISIBLE);
                        }
                    });
                    fanoutReppedRemoveObject.clear();
                    reppedRemoveInc1 = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addRepToPost(){
        // first we need the followers of the OP. Put em in a list.
        // so we need to get the value of repCount (if repCount > 0), and increment it. Then we put it in a map and
        // upload it.
        mRepsIconWhite.setVisibility(View.GONE);
        mLoadingReppedView.setVisibility(View.VISIBLE);
        final int newRepCount = getReppedCount() + 1;

        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(xUid).child
                ("notificationCount");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference().child
                        ("notifications").child(xUid);
                NotificationModelClass notificationModelClass = new NotificationModelClass("rep", xUid, mRefKey,
                        DateTime.now(DateTimeZone.UTC).toString(), null);
                notificationRef.push().setValue(notificationModelClass);

                if(dataSnapshot.exists()){
                    int currentCount = Integer.parseInt(dataSnapshot.getValue(String.class));
                    currentCount++;
                    userRef.setValue(String.valueOf(currentCount));
                }else{
                    userRef.setValue("1");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(xUid);
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    // if the poster has followers
                    final int childCount = (int) dataSnapshot.getChildrenCount();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        reppedInc1++;
                        final String key = dataSnapshot1.getKey();
                        //if(!key.equals(getCurrentUid())){
                        DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child
                                ("feed").child(key).child(mRefKey);
                        postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    if(!key.equals(getCurrentUid())){
                                        List<String> hasReppedList;
                                        if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                                            hasReppedList = getHasReppedList();
                                            hasReppedList.add(getCurrentUid());
                                        }else{
                                            hasReppedList = new ArrayList<>();
                                            hasReppedList.add(getCurrentUid());
                                        }
                                        fanoutReppedObject.put("/feed/" + key +
                                                "/" + mRefKey + "/hasReppedList/", hasReppedList);
                                    }
                                    if(reppedInc1 == childCount){
                                        List<String> hasReppedList;
                                        if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                                            hasReppedList = getHasReppedList();
                                            hasReppedList.add(getCurrentUid());
                                        }else{
                                            hasReppedList = new ArrayList<>();
                                            hasReppedList.add(getCurrentUid());
                                        }

                                        if(!getCurrentUid().equals(xUid)){
                                            fanoutReppedObject.put("/feed/" + xUid +
                                                    "/" + mRefKey + "/hasReppedList/", hasReppedList);
                                        }

                                        fanoutReppedObject.put("/feed/" + getCurrentUid() +
                                                "/" + mRefKey + "/hasReppedList/", hasReppedList);

                                        //fanoutReppedObject.put("/selfFeed/" + xUid +
                                        //        "/" + mRefKey + "/repCount/", newRepCount);

                                        fanoutReppedObject.put("/selfFeed/" + xUid +
                                                "/" + mRefKey + "/hasReppedList/", hasReppedList);

                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                        rootRef.updateChildren(fanoutReppedObject).addOnCompleteListener(new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {
                                                mLoadingReppedView.setVisibility(View.GONE);
                                                mRepsIconGold.setVisibility(View.VISIBLE);
                                                //mRepsCounterView.setText(String.valueOf(reppedCount));
                                                //mRepsCounterView.setVisibility(View.VISIBLE);
                                            }
                                        });
                                        fanoutReppedObject.clear();
                                        reppedInc1 = 0;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        //}
                    }
                }else{
                    // if the poster has no friends
                    //fanoutReppedObject.put("/feed/" + getCurrentUid() +
                    //        "/" + mRefKey + "/repCount/", newRepCount);

                    //fanoutReppedObject.put("/feed/" + getCurrentUid() +
                    //        "/" + mRefKey + "/hasRepped/", true);

                    //fanoutReppedObject.put("/selfFeed/" + xUid +
                    //        "/" + mRefKey + "/repCount/", newRepCount);

                    List<String> hasReppedList;
                    if(getHasReppedList() != null && !getHasReppedList().isEmpty()){
                        hasReppedList = getHasReppedList();
                        hasReppedList.add(getCurrentUid());
                    }else{
                        hasReppedList = new ArrayList<>();
                        hasReppedList.add(getCurrentUid());
                    }

                    fanoutReppedObject.put("/feed/" + getCurrentUid() +
                            "/" + mRefKey + "/hasReppedList/", hasReppedList);

                    fanoutReppedObject.put("/selfFeed/" + xUid +
                            "/" + mRefKey + "/hasReppedList/", hasReppedList);

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.updateChildren(fanoutReppedObject).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            mLoadingReppedView.setVisibility(View.GONE);
                            mRepsIconGold.setVisibility(View.VISIBLE);
                            //mRepsCounterView.setText(String.valueOf(reppedCount));
                            //mRepsCounterView.setVisibility(View.VISIBLE);
                        }
                    });
                    fanoutReppedObject.clear();
                    reppedInc1 = 0;
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // because the isRepped value is local, we only need our id and the reference of the OP.

    }

    public void setReppedCount(int repCount){
        reppedCount = repCount;
    }

    public int getReppedCount(){
        return this.reppedCount;
    }

    public void setRepsCounterView(int repsCount){
        reppedCount = repsCount;

        if(repsCount != 0){
            mRepsCounterView.setVisibility(View.VISIBLE);
            mRepsCounterView.setText(String.valueOf(repsCount));
        }else{
            Log.i("reppedCount", "(Setter TextView method) reppedCount:" + getReppedCount());
            mRepsCounterView.setVisibility(View.GONE);
            mRepsCounterView.setText(String.valueOf(getReppedCount()));
        }
    }

    public void setGone(){
        mCardViewParent.setVisibility(View.GONE);
    }

    public boolean getIsImperialPOV() {
        return isImperialPOV;
    }

    public void setImperialPOV(boolean imperialPOV) {
        isImperialPOV = imperialPOV;
    }

    public void setPostInfo(HashMap<String, List<String>> workoutInfoMap, FragmentActivity activity, Context context,
                            boolean isImperial){

        /**
         * Maybe if we do the computations here?
         */

        WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(workoutInfoMap, context);
        //adapter.setInfoList(workoutInfoMap);
        adapter.setIsOriginallyImperial(isImperial);
        adapter.setImperialPOV(getIsImperialPOV());
        mInfoRecyclerView.setAdapter(adapter);
        mInfoRecyclerView.setHasFixedSize(false);
        mInfoRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
    }

    public void setPosition(int position){
        if((double) position % (double) 5 == 0.0){
            //mBannerView.setVisibility(View.VISIBLE);
            //Appodeal.setBannerViewId(mBannerView.getId());
            //Appodeal.show(mActivity, Appodeal.BANNER_VIEW);
        }
    }

    public boolean getIsFullComments() {
        return isFullComments;
    }

    public void setIsFullComments(boolean fullComments) {
        isFullComments = fullComments;
    }

    public int getCommentCount() {
        return mCommentCount;
    }

    public void setCommentCount(int mCommentCount) {
        this.mCommentCount = mCommentCount;
    }

    private Map fanoutCommentObject = new HashMap<>();

    private int commentInc1 = 0;

    private void fanoutCommentPost(final String commentRefKey, final PostCommentModelClass commentModelClass){
        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child(xUid);

        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)  {
                if(dataSnapshot.exists()){
                    final int childCount = (int) dataSnapshot.getChildrenCount();
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        commentInc1++;
                        final String key = dataSnapshot1.getKey();
                        //if(!key.equals(getCurrentUid())){
                            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child
                                    ("feed").child(key).child(mRefKey);
                            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        if(!key.equals(getCurrentUid())){
                                            fanoutCommentObject.put("/feed/" + key +
                                                    "/" + mRefKey + "/commentMap/" + commentRefKey, commentModelClass);
                                        }
                                        if(commentInc1 == childCount){
                                            if(!getCurrentUid().equals(xUid)){
                                                // if you're commenting on someone else's post, put it in their feed.
                                                // so if you're commenting on your own post, it doesn't put it in
                                                //  yours because it already did!
                                                fanoutCommentObject.put("/feed/" + xUid +
                                                        "/" + mRefKey + "/commentMap/" + commentRefKey, commentModelClass);
                                            }

                                            fanoutCommentObject.put("/selfFeed/" + xUid + "/" + mRefKey +
                                                            "/commentMap/" + commentRefKey, commentModelClass);

                                            DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                            rootRef.updateChildren(fanoutCommentObject);
                                            fanoutCommentObject.clear();
                                            commentInc1 = 0;
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        //}

                    }
                    //FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);
//
                    //final List<String> userList = new ArrayList<>();
//
                    //if(followersModelClass.getUserIdList() != null){
                    //    userList.addAll(followersModelClass.getUserIdList());
//
//
                    //    if(!getCurrentUid().equals(xUid)){
                    //        userList.add(xUid);
                    //    }
//
//
                    //    for(int i = 0; i < userList.size(); i++){
                    //        final int inc = i;
                    //        if(!userList.get(i).equals(getCurrentUid())){
                    //            DatabaseReference postRef = FirebaseDatabase.getInstance().getReference().child
                    //                    ("feed").child(userList.get(i)).child(mRefKey);
                    //            postRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    //                @Override
                    //                public void onDataChange(DataSnapshot dataSnapshot) {
                    //                    if(dataSnapshot.exists()){fanoutCommentObject.put("/feed/" + userList.get(inc) +
                    //                            "/" + mRefKey + "/commentMap/" + commentRefKey, commentModelClass);
                    //                    }
                    //                    if(inc == userList.size() - 1){
                    //                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    //                        rootRef.updateChildren(fanoutCommentObject);
                    //                        fanoutCommentObject.clear();
                    //                    }
                    //                }
//
                    //                @Override
                    //                public void onCancelled(DatabaseError databaseError) {
//
                    //                }
                    //            });
                    //        }
                    //    }
                    //}
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

        //StorageReference currentProfilePicRef = storageRef.child("images/user/" + getCurrentUid() + "/profilePic
        // .png");
        //currentProfilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
        //    @Override
        //    public void onSuccess(Uri uri) {
        //        Glide.with(mActivity).load(uri).crossFade().into(mUserProfilePic);
        //    }
        //}).addOnFailureListener(new OnFailureListener() {
        //    @Override
        //    public void onFailure(@NonNull Exception e) {
        //        mUserProfilePic.setImageResource(R.drawable.usertest);
        //    }
        //});

        StorageReference posterProfilePicRef = storageRef.child("images/user/" + postUid + "/profilePic.png");
        posterProfilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try{
                    Glide.with(mActivity).load(uri).into(xProfilePic);
                }catch (IllegalArgumentException e){

                }
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

        recentMessages = mFeedRef.limitToLast(2);

        mFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() > 2){
                    mAllCommentsLL.setVisibility(View.VISIBLE);
                }else{
                    mAllCommentsLL.setVisibility(View.GONE);
                }
                //if(dataSnapshot.exists()){
                    mFirebaseAdapter = new FirebaseRecyclerAdapter<PostCommentModelClass, PostCommentViewHolder>
                            (PostCommentModelClass.class, R.layout.post_comment_list_item, PostCommentViewHolder.class, recentMessages) {
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

    public void setFullCommentRecycler(String refKey){
        mAllCommentsLL.setVisibility(View.GONE);
        mCommentEditText.setTextColor(Color.parseColor("#000000"));
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
        return mCurrentUserId;
    }

    public void setCurrentUserId(String userId){
        this.mCurrentUserId = userId;
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
                //String repsLevel = otherUserModelClass.getRepLevel();
                String powerLevel = otherUserModelClass.getPowerLevel();
                mUserLevelView.setText(powerLevel);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setPublicDescription(String publicDescription){
        if(publicDescription == null || publicDescription.equals("")){
            mPublicDescriptionView.setVisibility(View.GONE);
        }else{
            mPublicDescriptionView.setVisibility(View.VISIBLE);
            mPublicDescriptionView.setText(publicDescription);
        }
    }

    public void setTimeStamp(String timeStamp){
        DateTime dateTimeOriginal = DateTime.parse(timeStamp);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");
        mTimestampView.setText(formattedLocalDate);
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
