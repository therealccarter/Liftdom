package com.liftdom.liftdom.chat.ChatSpecific;

/**
 * Created by Brodin on 5/4/2017.
 */

public class ChatMessageClass {

    private String mTextMessage;
    private String mUserId;
    private String mUserName;
    private String mTimeStamp;
    private String mMediaResource;
    private int mRepCount;

    public ChatMessageClass(){
        // necessary for Firebase
    }

    public ChatMessageClass(String textMessage, String userId, String userName, String timeStamp, int repCount, String
            mediaResource){
        mTextMessage = textMessage;
        mUserId = userId;
        mUserName = userName;
        mTimeStamp = timeStamp;
        mRepCount = repCount;
        mMediaResource = mediaResource;
    }

    public String getTextMessage(){
        return mTextMessage;
    }

    public void setTextMessage(String textMessage){
        mTextMessage = textMessage;
    }

    public String getUserId(){
        return mUserId;
    }

    public void setUserId(String userId){
        mUserId = userId;
    }

    public String getUserName(){
        return mUserName;
    }

    public void setUserName(String userName){
        mUserName = userName;
    }

    public String getTimeStamp(){
        return mTimeStamp;
    }

    public void setTimeStamp(String timeStamp){
        mTimeStamp = timeStamp;
    }

    public String getMediaResource(){
        return mMediaResource;
    }

    public void setMediaResource(String mediaResource){
        mMediaResource = mediaResource;
    }

    public int getRepCount(){
        return mRepCount;
    }

    public void setRepCount(int repCount){
        mRepCount = repCount;
    }
}
