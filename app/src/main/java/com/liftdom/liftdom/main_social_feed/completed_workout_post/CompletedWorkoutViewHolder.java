package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
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
    private final LinearLayout exContentsHolder;
    private final LinearLayout mPostInfoHolder;
    private final ImageButton mSendCommentButton;
    private final EditText mCommentEditText;
    private final ImageView mUserProfilePic;
    private String mRefKey;
    //private final RecyclerView mCommentRecyclerView;
    //private final FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef;
    private final LinearLayout mCommentFragHolder;

    public CompletedWorkoutViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userName);
        mUserLevelView = (TextView) itemView.findViewById(R.id.userLevel);
        mTimestampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mPublicDescriptionView = (TextView) itemView.findViewById(R.id.publicDescription);
        exContentsHolder = (LinearLayout) itemView.findViewById(R.id.exContentsHolder);
        mPostInfoHolder = (LinearLayout) itemView.findViewById(R.id.postInfoHolder);
        mSendCommentButton = (ImageButton) itemView.findViewById(R.id.sendCommentButton);
        mCommentEditText = (EditText) itemView.findViewById(R.id.commentEditText);
        mUserProfilePic = (ImageView) itemView.findViewById(R.id.currentUserProfilePic);
        //mCommentRecyclerView = (RecyclerView) itemView.findViewById(R.id.commentsRecyclerView);
        mCommentFragHolder = (LinearLayout) itemView.findViewById(R.id.commentFragHolder);

        final DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mPostInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                if(uid.equals(xUid)){
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

                    DatabaseReference userRef = mRootRef.child("user").child(uid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                            DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                                    (xUid).child("comments");

                            String refKey = commentRef.push().getKey();

                            PostCommentModelClass commentModelClass = new PostCommentModelClass(
                                    userModelClass.getUserId(), userModelClass.getUserName(), mCommentEditText
                                    .getText().toString(), 0, DateTime.now(DateTimeZone.UTC).toString(), refKey);

                            commentRef.child(refKey).setValue(commentModelClass);
                            fanoutCommentPost(refKey, commentModelClass, userModelClass);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }
        });


    }

    private void fanoutCommentPost(final String commentRefKey, final PostCommentModelClass commentModelClass,
                                   UserModelClass userModelClass){

        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("followers").child
                (xUid);
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);

                List<String> userList = new ArrayList<>();

                if(followersModelClass.getUserIdList() != null){
                    userList.addAll(followersModelClass.getUserIdList());

                    Map fanoutObject = new HashMap<>();

                    for(String user : userList){
                        fanoutObject.put("/feed/" + user + "/" + mRefKey + "/" + "comments/" + commentRefKey, commentModelClass);
                    }

                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                    rootRef.updateChildren(fanoutObject).addOnCompleteListener(new OnCompleteListener() {
                        @Override
                        public void onComplete(@NonNull Task task) {
                            mCommentEditText.setText("");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
                mUserLevelView.setText(repsLevel);
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

    public void setCommentFrag(FragmentActivity activity){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager
                .beginTransaction();
        CommentsHolderFrag commentsHolderFrag = new CommentsHolderFrag();
        commentsHolderFrag.parentRefKey = getRefKey();
        commentsHolderFrag.mActivity = activity;
        fragmentTransaction.replace(R.id.commentFragHolder, commentsHolderFrag);
        fragmentTransaction.commit();
    }

    public void setPostInfo(HashMap<String, List<String>> workoutInfoMap, FragmentActivity activity){

        for(int i = 0; i < workoutInfoMap.size(); i++){
            for(Map.Entry<String, List<String>> mapEntry : workoutInfoMap.entrySet()){
                String[] keyTokens = mapEntry.getKey().split("_");
                if(Integer.parseInt(keyTokens[0]) == i){
                    List<String> list = new ArrayList<>();
                    list.addAll(mapEntry.getValue());
                    boolean isFirstEx = true;
                    boolean isFirstRepsWeight = true;
                    for(String string : list){
                        if(isExerciseName(string) && isFirstEx){

                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            PostExNameFrag exNameFrag = new PostExNameFrag();
                            exNameFrag.exNameString = string;
                            fragmentTransaction.add(R.id.exContentsHolder, exNameFrag);
                            fragmentTransaction.commit();

                            isFirstEx = false;
                        }else if(isExerciseName(string) && !isFirstEx){

                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            PostExNameSSFrag exNameSSFrag = new PostExNameSSFrag();
                            exNameSSFrag.exNameString = string;
                            fragmentTransaction.add(R.id.exContentsHolder, exNameSSFrag);
                            fragmentTransaction.commit();

                        }else if(!isExerciseName(string) && isFirstRepsWeight){
                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            PostSetSchemeFrag setSchemesFrag = new PostSetSchemeFrag();
                            setSchemesFrag.setSchemeString = string;
                            fragmentTransaction.add(R.id.exContentsHolder, setSchemesFrag);
                            fragmentTransaction.commit();

                            isFirstRepsWeight = false;
                        }else if(!isExerciseName(string) && !isFirstRepsWeight){
                            FragmentManager fragmentManager = activity.getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager
                                    .beginTransaction();
                            PostSetSchemeSSFrag setSchemeSSFrag = new PostSetSchemeSSFrag();
                            setSchemeSSFrag.setSchemeString = string;
                            fragmentTransaction.add(R.id.exContentsHolder, setSchemeSSFrag);
                            fragmentTransaction.commit();
                        }
                    }
                }
            }
        }


        //for(String infoString : postInfo){
        //    if(isExerciseName(infoString)){
        //        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        //        FragmentTransaction fragmentTransaction = fragmentManager
        //                .beginTransaction();
        //        PostExNameFrag exNameFrag = new PostExNameFrag();
        //        exNameFrag.exNameString = infoString;
        //        fragmentTransaction.add(R.id.exContentsHolder, exNameFrag);
        //        fragmentTransaction.commit();
        //    }else{
        //        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        //        FragmentTransaction fragmentTransaction = fragmentManager
        //                .beginTransaction();
        //        PostSetSchemeFrag setSchemesFrag = new PostSetSchemeFrag();
        //        setSchemesFrag.setSchemeString = infoString;
        //        fragmentTransaction.add(R.id.exContentsHolder, setSchemesFrag);
        //        fragmentTransaction.commit();
        //    }
        //}
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
