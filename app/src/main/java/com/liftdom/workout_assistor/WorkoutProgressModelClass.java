package com.liftdom.workout_assistor;

import android.content.Intent;
import android.util.Log;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/31/2017.
 */

public class WorkoutProgressModelClass {

    private String mDate;
    private boolean mCompletedBool;
    private HashMap<String, HashMap<String, List<String>>> mExInfoHashMap;
    private String mPrivateJournal;
    private String mPublicComment;
    private String mMediaResource;
    private boolean mIsTemplateImperial;
    private String mViewCursor; // the current set we're on

    public WorkoutProgressModelClass(){
        // necessary for Firebase
    }

    public WorkoutProgressModelClass(String date, boolean completedBool, HashMap<String, HashMap<String, List<String>>> exInfoHashMap,
                                     String privateJournal, String publicComment, String mediaResource, boolean
                                             isTemplateImperial){
        mDate = date;
        mCompletedBool = completedBool;
        mExInfoHashMap = exInfoHashMap;
        mPrivateJournal = privateJournal;
        mPublicComment = publicComment;
        mMediaResource = mediaResource;
        mIsTemplateImperial = isTemplateImperial;
    }

    public String exNameForCursor(){
        String exName;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        exName = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(0);

        return exName;
    }

    public String setForCursor(){
        String set;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        set = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt(tokens[2]));

        return set;
    }

    public void next(){
        /**
         * So, what we need to do is take the current cursor and try to first increment [2] by 1.
         * First we need to see if this is a superset map.
         * Case 1: (!superset)
         *  We'll need to do a check to see if incrementing by 1 goes past the limit.
         *  If it does, we'll go to the next map, [0] + 1, [1] = 1. Go back through loop until it sticks.
         *  If it doesn't, and is within bounds, we increment and end.
         * Case 2: (superset)
         *  We increment [1] + 1, and if it isn't the last one, we stay at the same [2], but move cursor there.
         */

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);
        String cursor = getViewCursor();
        String key = "_key";

        if(getExInfoHashMap().get(tokens[0] + key).size() < 2){
            // not superset
            Log.i("progressModel", "80, cursor = " + cursor);
            if(getExInfoHashMap().get(tokens[0] + key).get(tokens[1] + key).size() > (Integer.parseInt
                    (tokens[2]) + 1)){
                Log.i("progressModel", "83, cursor = " + cursor);
                int newValue = Integer.parseInt(tokens[2]) + 1;
                setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(newValue));
            }else{
                // go to next super-map
                Log.i("progressModel", "88, cursor = " + cursor);
                if(getExInfoHashMap().size() >= (Integer.parseInt(tokens[0]) + 1)){
                    Log.i("progressModel", "90, cursor = " + cursor);
                    int newValue = Integer.parseInt(tokens[0]) + 1;
                    setViewCursor(String.valueOf(newValue) + "_0_1");
                }else{
                    Log.i("progressModel", "94, cursor = " + cursor);
                    // workout done
                    Log.i("progressModel", "workout done (non-superset)");
                    setViewCursor("workoutDone");
                }
            }
        }else{
            Log.i("progressModel", "hello superset");
            // superset
            /**
             * This one will be a bit harder. We're going to have to jump between each list,
             * so [1] = 0, [1] = 1, [1] = 2, [1] = 0, etc
             */
            if(getExInfoHashMap().get(tokens[0] + key).size() > (Integer.parseInt(tokens[1]) + 1)){
                // if room to increase ([1])
                Log.i("progressModel", "107, cursor = " + cursor);
                int addedKey = Integer.parseInt(tokens[1]) + 1;
                if(getExInfoHashMap().get(tokens[0] + key).get(String.valueOf(addedKey) + key).size() > Integer
                        .parseInt(tokens[2])){
                    // if room to increase ([2]) of next list
                    //int newValue = Integer.parseInt(tokens[1]) + 1;
                    Log.i("progressModel", "111, cursor = " + cursor);
                    setViewCursor(tokens[0] + "_" + String.valueOf(addedKey) + "_" + tokens[2]);
                }else{
                    Log.i("progressModel", "116, cursor = " + cursor);
                    // keep increasing in original?
                    if(getExInfoHashMap().get(tokens[0] + key).get(tokens[1] + key).size() > (Integer.parseInt
                            (tokens[2]) + 1)){
                        // increase
                        Log.i("progressModel", "121, cursor = " + cursor);
                        int newValue = Integer.parseInt(tokens[2]) + 1;
                        setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(newValue));
                    }else{
                        // move forward
                        int newValue = Integer.parseInt(tokens[0]) + 1;
                        if(getExInfoHashMap().size() >= newValue){
                            // next map
                            setViewCursor(String.valueOf(newValue) + "_0_1");
                            Log.i("progressModel", "126, cursor = " + cursor);
                            Log.i("progressModel", "move forward (superset - else end 1)");
                        }else{
                            // done with workout
                            setViewCursor("workoutDone");
                        }
                    }
                }
            }else{
                // if need to loop back
                int newValue = Integer.parseInt(tokens[2]) + 1;

                if(getExInfoHashMap().get(tokens[0] + key).get("0_key").size() > newValue){
                    // increase
                    Log.i("progressModel", "138, cursor = " + cursor);
                    setViewCursor(tokens[0] + "_0_" + String.valueOf(newValue));
                }else{
                    // move forward
                    Log.i("progressModel", "142, cursor = " + cursor);
                    Log.i("progressModel", "move forward (superset - else end 2)");
                }
            }
        }
    }

    public void previous(){

        if(getViewCursor().equals("workoutDone")){
            // go to last place
        }else{
            String delims = "[_]";
            String[] tokens = getViewCursor().split(delims);
            String cursor = getViewCursor();
            String key = "_key";

            if(getExInfoHashMap().get(tokens[0] + key).size() < 2){
                // not superset
                //Log.i("progressModel", "80, cursor = " + cursor);
                if((Integer.parseInt(tokens[2]) - 1) != 0){
                    //Log.i("progressModel", "83, cursor = " + cursor);
                    int newValue = Integer.parseInt(tokens[2]) - 1;
                    setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(newValue));
                }else{
                    // go to next super-map
                    //Log.i("progressModel", "88, cursor = " + cursor);
                    if((Integer.parseInt(tokens[0]) - 1 >= 0)){
                        //Log.i("progressModel", "90, cursor = " + cursor);
                        int newValue = Integer.parseInt(tokens[0]) - 1;
                        setViewCursor(String.valueOf(newValue) + "_0_1");
                    }else{
                        //Log.i("progressModel", "94, cursor = " + cursor);
                        // workout done
                        //Log.i("progressModel", "workout done (non-superset)");
                        //setViewCursor("workoutDone");
                    }
                }
            }else{
                // superset



                /**
                 * This one will be a bit harder. We're going to have to jump between each list,
                 * so [1] = 0, [1] = 1, [1] = 2, [1] = 0, etc                 *
                 */
                //if(getExInfoHashMap().get(tokens[0] + key).size() > (Integer.parseInt(tokens[1]) + 1)){
                //    // if room to increase ([1])
                //    //Log.i("progressModel", "107, cursor = " + cursor);
                //    int addedKey = Integer.parseInt(tokens[1]) + 1;
                //    if(getExInfoHashMap().get(tokens[0] + key).get(String.valueOf(addedKey) + key).size() > Integer
                //            .parseInt(tokens[2])){
                //        // if room to increase ([2]) of next list
                //        //int newValue = Integer.parseInt(tokens[1]) + 1;
                //        //Log.i("progressModel", "111, cursor = " + cursor);
                //        setViewCursor(tokens[0] + "_" + String.valueOf(addedKey) + "_" + tokens[2]);
                //    }else{
                //        //Log.i("progressModel", "116, cursor = " + cursor);
                //        // keep increasing in original?
                //        if(getExInfoHashMap().get(tokens[0] + key).get(tokens[1] + key).size() > (Integer.parseInt
                //                (tokens[2]) + 1)){
                //            // increase
                //            //Log.i("progressModel", "121, cursor = " + cursor);
                //            int newValue = Integer.parseInt(tokens[2]) + 1;
                //            setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(newValue));
                //        }else{
                //            // move forward
                //            int newValue = Integer.parseInt(tokens[0]) + 1;
                //            if(getExInfoHashMap().size() >= newValue){
                //                // next map
                //                setViewCursor(String.valueOf(newValue) + "_0_1");
                //                //Log.i("progressModel", "126, cursor = " + cursor);
                //                //Log.i("progressModel", "move forward (superset - else end 1)");
                //            }else{
                //                // done with workout
                //                //setViewCursor("workoutDone");
                //            }
                //        }
                //    }
                //}else{
                //    // if need to loop back
                //    int newValue = Integer.parseInt(tokens[2]) + 1;
//
                //    if(getExInfoHashMap().get(tokens[0] + key).get("0_key").size() > newValue){
                //        // increase
                //        //Log.i("progressModel", "138, cursor = " + cursor);
                //        setViewCursor(tokens[0] + "_0_" + String.valueOf(newValue));
                //    }else{
                //        // move forward
                //        //Log.i("progressModel", "142, cursor = " + cursor);
                //        //Log.i("progressModel", "move forward (superset - else end 2)");
                //    }
                //}
            }
        }
    }

    public void toggleCheck(){
        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        String currentSet = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt(tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet);

        String[] tokens2 = currentSet.split(delims);

        if(tokens2[1].equals("checked")){
            tokens2[1] = "unchecked";
        }else{
            tokens2[1] = "checked";
        }

        String newString = "";

        for(int i = 0; i < tokens2.length; i++){
            if(i == 0){
                newString = tokens2[i];
            }else{
                newString = newString + "_" + tokens2[i];
            }
        }

        getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").set(Integer.parseInt(tokens[2]), newString);
        String currentSet2 = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt
                (tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet2);


    }

    public String getViewCursor() {
        return mViewCursor;
    }

    public void setViewCursor(String mViewCursor) {
        this.mViewCursor = mViewCursor;
    }

    public boolean isIsTemplateImperial() {
        return mIsTemplateImperial;
    }

    public void setIsTemplateImperial(boolean mIsTemplateImperial) {
        this.mIsTemplateImperial = mIsTemplateImperial;
    }

    public String getMediaResource() {
        return mMediaResource;
    }

    public void setMediaResource(String mMediaResource) {
        this.mMediaResource = mMediaResource;
    }

    public String getDate() {
        return mDate;
    }

    public void setDate(String mDate) {
        this.mDate = mDate;
    }

    public boolean isCompletedBool() {
        return mCompletedBool;
    }

    public void setCompletedBool(boolean mCompletedBool) {
        this.mCompletedBool = mCompletedBool;
    }

    public HashMap<String, HashMap<String, List<String>>> getExInfoHashMap() {
        return mExInfoHashMap;
    }

    public void setExInfoHashMap(HashMap<String, HashMap<String, List<String>>> mExInfoHashMap) {
        this.mExInfoHashMap = mExInfoHashMap;
    }

    public String getPrivateJournal() {
        return mPrivateJournal;
    }

    public void setPrivateJournal(String mPrivateJournal) {
        this.mPrivateJournal = mPrivateJournal;
    }

    public String getPublicComment() {
        return mPublicComment;
    }

    public void setPublicComment(String mPublicComment) {
        this.mPublicComment = mPublicComment;
    }

}
