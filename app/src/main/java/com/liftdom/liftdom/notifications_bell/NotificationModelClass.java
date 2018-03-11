package com.liftdom.liftdom.notifications_bell;

/**
 * Created by Brodin on 3/11/2018.
 */

public class NotificationModelClass {

    /**
     * So what do we need here?
     * Going to need a database refKey of whatever was acted upon.
     *  If you liked or commented on a post, we'll use that post's refKey and then also check the type.
     *  Whether it was a rep or a comment.
     * Going to need a follower type.
     * So String Type. (Followed, Commented, Repped, Sent Program, Sent Message)
     * Definitely a date time.
     * Uid/Username of the person who did the action.
     */

    private String mType;
    private String mUidFromOutside;
    private String mUserNameFromOutside;
    private String mRefKey;
    private String mDateTime;

    public NotificationModelClass(){
        // necessary for Firebase
    }

    public NotificationModelClass(String type, String uidFromOutside, String userNameFromOutside, String refKey,
                                  String dateTime){
        mType = type;
        mUidFromOutside = uidFromOutside;
        mUserNameFromOutside = userNameFromOutside;
        mRefKey = refKey;
        mDateTime = dateTime;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public String getUidFromOutside() {
        return mUidFromOutside;
    }

    public void setUidFromOutside(String mUidFromOutside) {
        this.mUidFromOutside = mUidFromOutside;
    }

    public String getUserNameFromOutside() {
        return mUserNameFromOutside;
    }

    public void setUserNameFromOutside(String mUserNameFromOutside) {
        this.mUserNameFromOutside = mUserNameFromOutside;
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String mDateTime) {
        this.mDateTime = mDateTime;
    }
}
