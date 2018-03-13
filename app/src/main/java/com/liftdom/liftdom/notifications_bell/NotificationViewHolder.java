package com.liftdom.liftdom.notifications_bell;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.liftdom.liftdom.R;

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

    public void setOtherUserId(String mOtherUserId) {
        this.mOtherUserId = mOtherUserId;
    }

    public String getCurrentUserId() {
        return mCurrentUserId;
    }

    public void setCurrentUserId(String mCurrentUserId) {
        this.mCurrentUserId = mCurrentUserId;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }
}
