package com.liftdom.liftdom.forum;

/**
 * Created by Brodin on 6/2/2018.
 */
public class ThreadPostModelClass {

    private String mUserId;
    private String mUserName;
    private String mDateTime;
    private String mThreadTitle;
    private String mThreadText;
    private String mThreadPicRef;
    private String mThreadRef;
    private int mReplyCount;

    public ThreadPostModelClass(){
        // necessary for Firebase
    }

    public ThreadPostModelClass(String userId, String userName, String dateTime, String threadTitle,
                                String threadText, String threadPicRef, String threadRef){
        mUserId = userId;
        mUserName = userName;
        mDateTime = dateTime;
        mThreadTitle = threadTitle;
        mThreadText = threadText;
        mThreadPicRef = threadPicRef;
        mThreadRef = threadRef;

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

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }

    public String getThreadTitle() {
        return mThreadTitle;
    }

    public void setThreadTitle(String mThreadTitle) {
        this.mThreadTitle = mThreadTitle;
    }

    public String getThreadText() {
        return mThreadText;
    }

    public void setThreadText(String mThreadText) {
        this.mThreadText = mThreadText;
    }

    public String getThreadPicRef() {
        return mThreadPicRef;
    }

    public void setThreadPicRef(String mThreadPicRef) {
        this.mThreadPicRef = mThreadPicRef;
    }

    public String getThreadRef() {
        return mThreadRef;
    }

    public void setThreadRef(String mThreadRef) {
        this.mThreadRef = mThreadRef;
    }

    public int getReplyCount() {
        return mReplyCount;
    }

    public void setReplyCount(int mReplyCount) {
        this.mReplyCount = mReplyCount;
    }
}
