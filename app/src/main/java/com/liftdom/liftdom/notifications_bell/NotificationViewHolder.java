package com.liftdom.liftdom.notifications_bell;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.WorkoutPostSingleActivity;
import com.liftdom.user_profile.single_user_profile.UserProfileDialogActivity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Brodin on 3/11/2018.
 */

public class NotificationViewHolder extends RecyclerView.ViewHolder{

    private final TextView userNameView;
    private final TextView hasFollowedYouView;
    private final TextView hasCommentedView;
    private final TextView hasReppedView;
    private final TextView hasSentProgramView;
    private final TextView dateTimeView;
    private final View dividerView;
    private FragmentActivity mActivity;
    private String mCurrentUserId;
    private String mOtherUserId;
    private String mType;
    private String mDateTime;
    private String mRefKey;


    public NotificationViewHolder(View itemView){
        super(itemView);
        userNameView = (TextView) itemView.findViewById(R.id.userNameView);

        hasFollowedYouView = (TextView) itemView.findViewById(R.id.hasFollowedYou);
        hasCommentedView = (TextView) itemView.findViewById(R.id.hasCommentedView);
        hasReppedView = (TextView) itemView.findViewById(R.id.hasReppedView);
        hasSentProgramView = (TextView) itemView.findViewById(R.id.hasSentProgram);
        dividerView = (View) itemView.findViewById(R.id.dividerView);

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
            hasSentProgramView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference()
                            .child("templatesInbox").child(getCurrentUserId()).child(mRefKey);
                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                Intent intent = new Intent(mActivity, MainActivity.class);
                                intent.putExtra("fragIDAndExtras", "1_inbox_" + mRefKey);
                                mActivity.startActivity(intent);
                            }else{
                                Snackbar.make(mActivity.getCurrentFocus(), "This program no longer exists in the " +
                                        "inbox.\nYou probably saved it.", Snackbar.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });                    
                }
            });
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

    public void showDividerAbove(){
        dividerView.setVisibility(View.VISIBLE);
    }
}
