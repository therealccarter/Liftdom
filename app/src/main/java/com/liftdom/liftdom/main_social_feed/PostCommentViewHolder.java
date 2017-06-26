package com.liftdom.liftdom.main_social_feed;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 6/26/2017.
 */

public class PostCommentViewHolder extends RecyclerView.ViewHolder{

    private final TextView mUserNameView;
    private final TextView mCommentView;
    private final ImageButton mRepButton;
    private final ImageView mUserProfilePic;
    private String mRefKey;
    private int mRepNumber;
    private String mDateString;

    public PostCommentViewHolder(View itemView){
        super(itemView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mCommentView = (TextView) itemView.findViewById(R.id.commentTextView);
        mRepButton = (ImageButton) itemView.findViewById(R.id.repButton);
        mUserProfilePic = (ImageView) itemView.findViewById(R.id.userProfilePic);
    }

    public void setUsername(String username){
        mUserNameView.setText(username);
    }

    public void setComment(String comment){
        mCommentView.setText(comment);
    }


    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public int getRepNumber() {
        return mRepNumber;
    }

    public void setRepNumber(int mRepNumber) {
        this.mRepNumber = mRepNumber;
    }

    public String getDateString() {
        return mDateString;
    }

    public void setDateString(String mDateString) {
        this.mDateString = mDateString;
    }
}
