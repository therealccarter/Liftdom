package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentModelClass;
import com.liftdom.liftdom.utils.UserNameIdModelClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 5/6/2017.
 */

public class CompletedWorkoutModelClass {

    private String mUserId;
    private String mUserName;
    private String mPublicDescription;
    private String mDateTime;
    private HashMap<String, List<String>> mWorkoutInfoMap;
    private boolean mIsImperial;
    private String mUserLevel;
    private String mRef;
    private Map<String, PostCommentModelClass> mCommentMap;
    private String mMediaRef;
    private List<String> bonusList;
    private int mCommentCount;
    private Map<String, PostCommentModelClass> recentCommentsMap;
    private int mRepCount;
    private List<UserNameIdModelClass> mRepUserList;
    private boolean mHasRepped;
    private List<String> mHasReppedUserList;

    public CompletedWorkoutModelClass(){
        // necessary for Firebase
    }

    public CompletedWorkoutModelClass(String userId, String userName, String publicDescription, String dateTime,
                                      boolean isImperial, String ref, String mediaRef,
                                      HashMap<String, List<String>> workoutInfoMap, Map<String,
            PostCommentModelClass> commentMap){
        mUserId = userId;
        mUserName = userName;
        mPublicDescription = publicDescription;
        mDateTime = dateTime;
        mIsImperial = isImperial;
        mRef = ref;
        mMediaRef = mediaRef;
        mWorkoutInfoMap = workoutInfoMap;
        mCommentMap = commentMap;
    }

    public void addToHasReppedList(String uid){
        if(getHasReppedUserList() != null && !getHasReppedUserList().isEmpty()){
            if(!getHasReppedUserList().contains(uid)){
                mHasReppedUserList.add(uid);
            }
        }else{
            mHasReppedUserList.add(uid);
        }
    }

    public void removeFromHasReppedList(String uid){
        if(getHasReppedUserList() != null && !getHasReppedUserList().isEmpty()){
            if(getHasReppedUserList().contains(uid)){
                getHasReppedUserList().remove(uid);
            }
        }
    }

    public List<String> getHasReppedUserList(){
        return this.mHasReppedUserList;
    }

    public boolean isHasRepped() {
        return mHasRepped;
    }

    public void setHasRepped(boolean hasRepped) {
        this.mHasRepped = hasRepped;
    }

    public int getRepCount() {
        return mRepCount;
    }

    public void setRepCount(int repCount) {
        this.mRepCount = repCount;
    }

    public List<UserNameIdModelClass> getRepUserList() {
        return mRepUserList;
    }

    public void setRepUserList(List<UserNameIdModelClass> repUserList) {
        this.mRepUserList = repUserList;
    }

    public Map<String, PostCommentModelClass> getRecentCommentsMap() {
        return recentCommentsMap;
    }

    public void setRecentCommentsMap(Map<String, PostCommentModelClass> mRecentCommentsMap) {
        this.recentCommentsMap = mRecentCommentsMap;
    }

    public List<String> getBonusList() {
        return bonusList;
    }

    public void addToCommentCount(){
        mCommentCount++;
    }

    public void setCommentCount(int commentCount){
        mCommentCount = commentCount;
    }

    public int getCommentCount(){
        return mCommentCount;
    }

    public void setBonusList(List<String> bonusList) {
        if(this.bonusList == null){
            this.bonusList = bonusList;
        }else{
            for(String string : bonusList){
                this.bonusList.add(string);
            }
        }

    }

    public Map<String, PostCommentModelClass> getCommentMap() {
        return mCommentMap;
    }

    public void setCommentMap(Map<String, PostCommentModelClass> mCommentMap) {
        this.mCommentMap = mCommentMap;
    }

    public HashMap<String, List<String>> getWorkoutInfoMap() {
        return mWorkoutInfoMap;
    }

    public void setWorkoutInfoMap(HashMap<String, List<String>> mWorkoutInfoMap) {
        this.mWorkoutInfoMap = mWorkoutInfoMap;
    }

    public boolean isIsImperial() {
        return mIsImperial;
    }

    public void setIsImperial(boolean mIsImperial) {
        this.mIsImperial = mIsImperial;
    }

    public String getRef() {
        return mRef;
    }

    public void setRef(String mRef) {
        this.mRef = mRef;
    }

    public String getMediaRef() {
        return mMediaRef;
    }

    public void setMediaRef(String mMediaRef) {
        this.mMediaRef = mMediaRef;
    }

    //public HashMap<String, List<String>> getCommentMap() {
    //    return commentMap;
    //}

    //public void setCommentMap(HashMap<String, List<String>> commentMap) {
    //    this.commentMap = commentMap;
    //}

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


}
