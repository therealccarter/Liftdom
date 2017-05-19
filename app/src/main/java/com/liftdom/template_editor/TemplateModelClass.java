package com.liftdom.template_editor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/9/2017.
 */

public class TemplateModelClass {

    private String mTemplateName;
    private String mUserId;
    private String mUserName;
    private boolean mIsPublic;
    private String mDateCreated;
    private String mDescription;
    private HashMap<String, List<String>> mMapOne;
    private HashMap<String, List<String>> mMapTwo;
    private HashMap<String, List<String>> mMapThree;
    private HashMap<String, List<String>> mMapFour;
    private HashMap<String, List<String>> mMapFive;
    private HashMap<String, List<String>> mMapSix;
    private HashMap<String, List<String>> mMapSeven;
    private boolean mIsAlgorithm;
    private HashMap<String, List<String>> mAlgorithmInfo;

    public TemplateModelClass(){
        //necessary for Firebase
    }

    public TemplateModelClass(String templateName, String userId, String userName, boolean isPublic,
                              String dateCreated, String description,
                              HashMap<String, List<String>> mapOne,
                              HashMap<String, List<String>> mapTwo,
                              HashMap<String, List<String>> mapThree,
                              HashMap<String, List<String>> mapFour,
                              HashMap<String, List<String>> mapFive,
                              HashMap<String, List<String>> mapSix,
                              HashMap<String, List<String>> mapSeven,
                              boolean isAlgorithm, HashMap<String, List<String>> algorithmInfo){

        mTemplateName = templateName;
        mUserId = userId;
        mUserName = userName;
        mDescription = description;
        mIsPublic = isPublic;
        mDateCreated = dateCreated;
        mMapOne = mapOne;
        mMapTwo = mapTwo;
        mMapThree = mapThree;
        mMapFour = mapFour;
        mMapFive = mapFive;
        mMapSix = mapSix;
        mMapSeven = mapSeven;
        mIsAlgorithm = isAlgorithm;
        mAlgorithmInfo = algorithmInfo;
    }

    public String getDays(){
        String days = "";
        if(!mMapOne.isEmpty()){
            String daysOne = mMapOne.get("0_key").get(0);
            days = days + "_" + daysOne;
        }
        if(mMapTwo != null){
            if(mMapTwo.get("0_key") != null){
                String daysOne = mMapTwo.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }
        if(mMapThree != null){
            if(mMapThree.get("0_key") != null){
                String daysOne = mMapThree.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }
        if(mMapFour != null){
            if(mMapFour.get("0_key") != null){
                String daysOne = mMapFour.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }
        if(mMapFive != null){
            if(mMapFive.get("0_key") != null){
                String daysOne = mMapFive.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }
        if(mMapSix != null){
            if(mMapSix.get("0_key") != null){
                String daysOne = mMapSix.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }
        if(mMapSeven != null){
            if(mMapSeven.get("0_key") != null){
                String daysOne = mMapSeven.get("0_key").get(0);
                days = days + "_" + daysOne;
            }
        }

        return days;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public String getTemplateName() {
        return mTemplateName;
    }

    public void setTemplateName(String templateName) {
        mTemplateName = templateName;
    }

    public String getUserId() {
        return mUserId;
    }

    public void setUserId(String userId) {
        mUserId = userId;
    }

    public String getUserName() {
        return mUserName;
    }

    public void setUserName(String userName) {
        mUserName = userName;
    }

    public boolean isPublic() {
        return mIsPublic;
    }

    public void setIsPublic(boolean isPublic) {
        mIsPublic = isPublic;
    }

    public String getDateCreated() {
        return mDateCreated;
    }

    public void setDateCreated(String dateCreated) {
        mDateCreated = dateCreated;
    }

    public HashMap<String, List<String>> getMapOne() {
        return mMapOne;
    }

    public void setMapOne(HashMap<String, List<String>> mapOne) {
        mMapOne = mapOne;
    }

    public HashMap<String, List<String>> getMapTwo() {
        return mMapTwo;
    }

    public void setMapTwo(HashMap<String, List<String>> mapTwo) {
        mMapTwo = mapTwo;
    }

    public HashMap<String, List<String>> getMapThree() {
        return mMapThree;
    }

    public void setMapThree(HashMap<String, List<String>> mapThree) {
        mMapThree = mapThree;
    }

    public HashMap<String, List<String>> getMapFour() {
        return mMapFour;
    }

    public void setMapFour(HashMap<String, List<String>> mapFour) {
        mMapFour = mapFour;
    }

    public HashMap<String, List<String>> getMapFive() {
        return mMapFive;
    }

    public void getMapFive(HashMap<String, List<String>> mapFive) {
        mMapFive = mapFive;
    }

    public HashMap<String, List<String>> getMapSix() {
        return mMapSix;
    }

    public void setMapSix(HashMap<String, List<String>> mapSix) {
        mMapSix = mapSix;
    }

    public HashMap<String, List<String>> getMapSeven() {
        return mMapSeven;
    }

    public void setMapSeven(HashMap<String, List<String>> mapSeven) {
        mMapSeven = mapSeven;
    }

    public boolean getIsAlgorithm() {
        return mIsAlgorithm;
    }

    public void setIsAlgorithm(boolean isAlgorithm) {
        mIsAlgorithm = isAlgorithm;
    }

    public HashMap<String, List<String>> getAlgorithmInfo() {
        return mAlgorithmInfo;
    }

    public void setAlgorithmInfo(HashMap<String, List<String>> algorithmInfo) {
        mAlgorithmInfo = algorithmInfo;
    }
}




