package com.liftdom.liftdom.main_social_feed;

import java.util.List;

/**
 * Created by Brodin on 4/29/2017.
 */

public class CompletedWorkoutClass {

    public String userId;
    public String userName;
    public String publicComment;
    public List workoutInfoList;


    public CompletedWorkoutClass(){

    }

    public CompletedWorkoutClass(String xUserId, String xUserName, String xPublicComment, List xWorkoutInfoList){
        userId = xUserId;
        userName = xUserName;
        publicComment = xPublicComment;
        workoutInfoList = xWorkoutInfoList;
    }

}
