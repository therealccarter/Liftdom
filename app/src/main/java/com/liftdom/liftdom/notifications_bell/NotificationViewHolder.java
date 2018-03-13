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
    private final LinearLayout followedLL;
    private final LinearLayout commentedLL;
    private final TextView postView1;
    private final LinearLayout reppedLL;
    private final TextView postView2;
    private final TextView dateTimeView;
    private FragmentActivity mActivity;

    public NotificationViewHolder(View itemView){
        super(itemView);
        userNameView = (TextView) itemView.findViewById(R.id.userNameView);
        followedLL = (LinearLayout) itemView.findViewById(R.id.followedLL);
        commentedLL = (LinearLayout) itemView.findViewById(R.id.commentedLL);
        postView1 = (TextView) itemView.findViewById(R.id.postView1);
        reppedLL = (LinearLayout) itemView.findViewById(R.id.reppedLL);
        postView2 = (TextView) itemView.findViewById(R.id.postView2);
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
                Intent intent = new Intent(mActivity, UserProfileFullActivity.class);
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
            followedLL.setVisibility(View.VISIBLE);
        }else if(mType.equals("comment")){
            commentedLL.setVisibility(View.VISIBLE);
            // post 1
        }else if(mType.equals("rep")){
            reppedLL.setVisibility(View.VISIBLE);
            // post 2
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
