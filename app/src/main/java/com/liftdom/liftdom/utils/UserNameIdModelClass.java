package com.liftdom.liftdom.utils;

/**
 * Created by Brodin on 5/5/2017.
 */

public class UserNameIdModelClass {

    private String mUserName;
    private String mUserId;

    public UserNameIdModelClass(){

    }

    public UserNameIdModelClass(String userName, String userId){
        mUserName = userName;
        mUserId = userId;
    }

    public String getUserName(){
        return mUserName;
    }

    public void setUserName(String userName){
        mUserName = userName;
    }

    public String getUserId(){
        return mUserId;
    }

    public void setUserId(String userId){
        mUserId = userId;
    }
}
