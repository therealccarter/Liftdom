package com.liftdom.liftdom.utils;

import java.util.HashMap;

/**
 * Created by Brodin on 6/24/2017.
 */

public class FollowingModelClass {

    private HashMap<String, String> mFollowingMap;

    public FollowingModelClass(){
        // necessary for Firebase
    }

    public FollowingModelClass(String userId, String userName){
        if(mFollowingMap == null){
            HashMap<String, String> map = new HashMap<>();
            map.put(userId, userName);
            setFollowingMap(map);
        }else{
            addFollowingToMap(userId, userName);
        }
    }

    public HashMap<String, String> getFollowingMap() {
        return mFollowingMap;
    }

    public void setFollowingMap(HashMap<String, String> mFollowerMap) {
        this.mFollowingMap = mFollowerMap;
    }

    public void addFollowingToMap(String userId, String userName){
        HashMap<String, String> map = new HashMap<>();
        map.putAll(getFollowingMap());
        map.put(userId, userName);
        setFollowingMap(map);
    }

    public void removeFollowingFromMap(String userId){
        HashMap<String, String> map = new HashMap<>();
        map.putAll(getFollowingMap());
        map.remove(userId);
        setFollowingMap(map);
    }
}
