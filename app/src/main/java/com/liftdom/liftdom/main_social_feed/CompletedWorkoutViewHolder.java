package com.liftdom.liftdom.main_social_feed;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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

    public CompletedWorkoutViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userName);
        mUserLevelView = (TextView) itemView.findViewById(R.id.userLevel);
        mTimestampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mPublicDescriptionView = (TextView) itemView.findViewById(R.id.publicDescription);
        exContentsHolder = (LinearLayout) itemView.findViewById(R.id.exContentsHolder);
        mPostInfoHolder = (LinearLayout) itemView.findViewById(R.id.postInfoHolder);

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

        for(Map.Entry<String, List<String>> mapEntry : workoutInfoMap.entrySet()){

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
