package com.liftdom.liftdom.main_social_feed;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/6/2017.
 */

public class CompletedWorkoutModelClass {

    private String mUserId;
    private String mUserName;
    private String mPublicDescription;
    private String mDateTime;
    private List<String> mWorkoutInfoList;
    private String mUserLevel;
    private String mRef;

    public CompletedWorkoutModelClass(){
        // necessary for Firebase
    }

    public CompletedWorkoutModelClass(String userId, String userName, String publicDescription, String dateTime,
                                      List<String> workoutInfoList){
        mUserId = userId;
        mUserName = userName;
        mPublicDescription = publicDescription;
        mDateTime = dateTime;
        mWorkoutInfoList = workoutInfoList;
    }


    public String getUserName() {
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

    public String getPublicDescription() {
        return mPublicDescription;
    }

    public void setPublicDescription(String publicDescription){
        mPublicDescription = publicDescription;
    }

    public String getDateTime() {
        return mDateTime;
    }

    public void setDateTime(String dateTime){
        mDateTime = dateTime;
    }

    public List<String> getWorkoutInfoList() {
        return mWorkoutInfoList;
    }

    public void setWorkoutInfoList(List<String> workoutInfoList){
        mWorkoutInfoList = workoutInfoList;
    }

    //public String getUserLevel() {
    //    return mUserLevel;
    //}
    //
    //public void setUserLevel(String userLevel){
    //    mUserLevel = userLevel;
    //}

    public void setRef(String ref){
        mRef = ref;
    }

}
