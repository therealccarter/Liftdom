package com.liftdom.liftdom.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 6/24/2017.
 */

public class FollowersModelClass {

    private HashMap<String, String> mFollowerMap;

    public FollowersModelClass(){
        // necessary for Firebase
    }

    public FollowersModelClass(String userId, String userName){
        if(mFollowerMap == null){
            HashMap<String, String> map = new HashMap<>();
            map.put(userId, userName);
            setFollowerMap(map);
        }else{
            addFollowerToMap(userId, userName);
        }
    }

    public HashMap<String, String> getFollowerMap() {
        return mFollowerMap;
    }

    public void setFollowerMap(HashMap<String, String> mFollowerMap) {
        this.mFollowerMap = mFollowerMap;
    }

    public List<String> getUserIdList(){
        List<String> userIdList = new ArrayList<>();

        if(mFollowerMap != null){
            for(Map.Entry<String, String> entry : mFollowerMap.entrySet()){
                userIdList.add(entry.getKey());
            }
        }

        return userIdList;
    }

    public void addFollowerToMap(String userId, String userName){
        HashMap<String, String> map = new HashMap<>();
        map.putAll(getFollowerMap());
        map.put(userId, userName);
        setFollowerMap(map);
    }

    public void removeFollowerFromMap(String userId){
        HashMap<String, String> map = new HashMap<>();
        map.putAll(getFollowerMap());
        map.remove(userId);
        setFollowerMap(map);
    }
}
