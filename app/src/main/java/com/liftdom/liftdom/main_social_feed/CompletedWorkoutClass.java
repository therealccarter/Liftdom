package com.liftdom.liftdom.main_social_feed;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 4/29/2017.
 */

@IgnoreExtraProperties
public class CompletedWorkoutClass {

    public String userId;
    public String userName;
    public String publicComment;
    public List workoutInfoList;
    public String dateAndTime;
    public HashMap<String, Boolean> repsMap;

    public CompletedWorkoutClass(){
        // Default constructor
    }

    public CompletedWorkoutClass(String userId, String userName, String publicComment, List workoutInfoList, String
            dateAndTime){
        this.userId = userId;
        this.userName = userName;
        this.publicComment = publicComment;
        this.workoutInfoList = workoutInfoList;
        this.dateAndTime = dateAndTime;
    }

    @Exclude
    public Map<String, Object> toMap(){

        HashMap<String, Object> result = new HashMap<>();

        result.put("userId", userId);
        result.put("userName", userName);
        result.put("publicComment", publicComment);
        result.put("workoutInfoList", workoutInfoList);
        result.put("dateTime", dateAndTime);
        result.put("repsMap", repsMap);

        return result;
    }

}
