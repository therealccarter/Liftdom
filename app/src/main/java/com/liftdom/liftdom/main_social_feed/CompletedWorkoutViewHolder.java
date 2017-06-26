package com.liftdom.liftdom.main_social_feed;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
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
    private final RecyclerView mCommentRecyclerView;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private DatabaseReference mFeedRef;

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
        mCommentRecyclerView = (RecyclerView) itemView.findViewById(R.id.commentsRecyclerView);



        mFeedRef = FirebaseDatabase.getInstance().getReference().child("feed").child(FirebaseAuth.getInstance()
                .getCurrentUser().getUid()).child(getRefKey()).child("comments");

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



                    PostCommentModelClass commentModelClass = new PostCommentModelClass();

                    DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child
                            (FirebaseAuth.getInstance()
                                    .getCurrentUser().getUid()).child(getRefKey()).child("comments");

                    String refKey = commentRef.push().getKey();


                }



            }
        });


    }

    private void fanoutCommentPost(final String refKey, final PostCommentModelClass commentModelClass){

    }

    public void setComments(HashMap<String, List<String>> commentMap, FragmentActivity activity){

        //DatabaseReference commentRef = FirebaseDatabase.getInstance().getReference().child("feed").child()

        for(int i = 0; i <  commentMap.size(); i++){
            for(Map.Entry<String, List<String>> mapEntry : commentMap.entrySet()){
                String[] keyTokens = mapEntry.getKey().split("_");
                if(Integer.parseInt(keyTokens[0]) == i){
                    // new comment frag
                    FragmentManager fragmentManager = activity.getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    //PostCommentFrag commentFrag = new PostCommentFrag();

                    //fragmentTransaction.add(R.id.commentsHolder, commentFrag);
                    fragmentTransaction.commit();
                }
            }
        }
    }

    private void setUpCommentFirebaseAdapter(FragmentActivity activity){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<PostCommentModelClass, PostCommentViewHolder>
                (PostCommentModelClass.class, R.layout.post_comment_list_item, PostCommentViewHolder.class, mFeedRef) {
            @Override
            protected void populateViewHolder(PostCommentViewHolder viewHolder, PostCommentModelClass model, int position) {

            }
        };

        mCommentRecyclerView.setHasFixedSize(false);
        mCommentRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
        mCommentRecyclerView.setAdapter(mFirebaseAdapter);
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

    public void setUserLevel(String xUid){
        DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference().child("user").child(xUid);
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
