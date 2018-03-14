package com.liftdom.liftdom.notifications_bell;

import android.content.Intent;
import android.provider.ContactsContract;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.WorkoutPostSingleActivity;
import com.liftdom.user_profile.single_user_profile.UserProfileDialogActivity;
import com.liftdom.user_profile.single_user_profile.UserProfileFullActivity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

/**
 * Created by Brodin on 3/11/2018.
 */

public class NotificationViewHolder extends RecyclerView.ViewHolder{

    private String mCurrentUserId;
    private String mOtherUserId;
    private String mType;
    private String mDateTime;
    private String mRefKey;
    private final TextView userNameView;
    private final TextView hasFollowedYouView;
    private final TextView hasCommentedView;
    private final TextView hasReppedView;
    private final TextView hasSentProgramView;
    private final TextView dateTimeView;
    private FragmentActivity mActivity;

    public NotificationViewHolder(View itemView){
        super(itemView);
        userNameView = (TextView) itemView.findViewById(R.id.userNameView);

        hasFollowedYouView = (TextView) itemView.findViewById(R.id.hasFollowedYou);
        hasCommentedView = (TextView) itemView.findViewById(R.id.hasCommentedView);
        hasReppedView = (TextView) itemView.findViewById(R.id.hasReppedView);
        hasSentProgramView = (TextView) itemView.findViewById(R.id.hasSentProgram);

        dateTimeView = (TextView) itemView.findViewById(R.id.dateTimeView);

    }

    public String getOtherUserId() {
        return mOtherUserId;
    }

    public void setOtherUserId(final String mOtherUserId) {
        this.mOtherUserId = mOtherUserId;
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                .child(mOtherUserId).child("userName");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                userNameView.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        userNameView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, UserProfileDialogActivity.class);
                if(getCurrentUserId().equals(mOtherUserId)){
                    mActivity.startActivity(intent);
                } else {
                    intent.putExtra("xUid", mOtherUserId);
                    mActivity.startActivity(intent);
                }
            }
        });
    }

    public String getCurrentUserId() {
        return mCurrentUserId;
    }

    public FragmentActivity getActivity() {
        return mActivity;
    }

    public void setActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setCurrentUserId(String mCurrentUserId) {
        this.mCurrentUserId = mCurrentUserId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
        if(mType.equals("follower")){
            String text = mActivity.getResources().getString(R.string.hasFollowedYou);
            hasFollowedYouView.setText(text);
            hasFollowedYouView.setVisibility(View.VISIBLE);
        }else if(mType.equals("comment")){
            String text = mActivity.getResources().getString(R.string.hasCommentedOnYourPost);
            hasCommentedView.setText(text);
            hasCommentedView.setVisibility(View.VISIBLE);
            hasCommentedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, WorkoutPostSingleActivity.class);
                    intent.putExtra("refKey", mRefKey);
                    mActivity.startActivity(intent);
                }
            });
            // post 1
        }else if(mType.equals("rep")){
            String text = mActivity.getResources().getString(R.string.hasReppedYourPost);
            hasReppedView.setText(text);
            hasReppedView.setVisibility(View.VISIBLE);
            hasReppedView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mActivity, WorkoutPostSingleActivity.class);
                    intent.putExtra("refKey", mRefKey);
                    mActivity.startActivity(intent);
                }
            });
            // post 2
        }else if(mType.equals("programSent")){
            String text = mActivity.getResources().getString(R.string.hasSentYouAProgram);
            hasSentProgramView.setText(text);
            hasSentProgramView.setVisibility(View.VISIBLE);
        }
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
        DateTime dateTimeOriginal = DateTime.parse(mDateTime);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");
        dateTimeView.setText(formattedLocalDate);
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }
}
