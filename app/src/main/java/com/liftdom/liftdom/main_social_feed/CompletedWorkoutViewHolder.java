package com.liftdom.liftdom.main_social_feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/6/2017.
 */

public class CompletedWorkoutViewHolder extends RecyclerView.ViewHolder{

    private final TextView mUserNameView;
    private final TextView mUserLevelView;
    private final TextView mPublicDescriptionView;
    private final TextView mTimestampView;
    private final LinearLayout mPostInfoHolder;

    public CompletedWorkoutViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userName);
        mUserLevelView = (TextView) itemView.findViewById(R.id.userLevel);
        mTimestampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mPublicDescriptionView = (TextView) itemView.findViewById(R.id.publicDescription);
        mPostInfoHolder = (LinearLayout) itemView.findViewById(R.id.exContentsHolder);
    }

    public void setUserName(String userName){
        mUserNameView.setText(userName);
    }

    public void setUserLevel(String xUid){
        DatabaseReference levelRef = FirebaseDatabase.getInstance().getReference().child("users").child(xUid).child("userLevel");
        levelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userLevel = dataSnapshot.getValue(String.class);
                mUserLevelView.setText(userLevel);
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
        mTimestampView.setText(timeStamp);
    }

    public void setPostInfo(List<String> postInfo){

    }
}
