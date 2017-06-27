package com.liftdom.liftdom.main_social_feed.comment_post;

/**
 * Created by Brodin on 6/26/2017.
 */

public class PostCommentModelClass {

    private String mUserId;
    private String mUserName;
    private String mCommentText;
    private int mRepNumber;
    private String mDateString;
    private String mRefKey;

    public PostCommentModelClass(){
        // necessary for Firebase
    }

    public PostCommentModelClass(String userId, String userName, String commentText, int repNumber, String
            dateString, String refKey){
        mUserId = userId;
        mUserName = userName;
        mCommentText = commentText;
        mRepNumber = repNumber;
        mDateString = dateString;
        mRefKey = refKey;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String mUserId) {
        this.mUserId = mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String mUserName) {
        this.mUserName = mUserName;
    }

    public String getCommentText() {
        return mCommentText;
    }

    public void setCommentText(String mCommentText) {
        this.mCommentText = mCommentText;
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

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }
}
