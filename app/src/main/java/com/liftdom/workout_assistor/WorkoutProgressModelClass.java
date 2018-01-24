package com.liftdom.workout_assistor;

import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String exIndex(){
        String exIndex;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        int x = 0;
        int y = 0;
        String key = "_key";
        boolean keepGoing = true;
        /**
         * x/y
         * so for the x value we need to go through each [0] and add the amount of [1]'s to an incrementer. Then we
         * stop once the values [0]_[1] = the one we're in.
         */
        for(Map.Entry<String, HashMap<String, List<String>>> entry1 : mExInfoHashMap.entrySet()){
            String entry1Key = entry1.getKey();
            y++;
            if(keepGoing){
                x++;
                if(entry1Key.equals(tokens[0] + key)){
                    keepGoing = false;
                }
            }
        }

        exIndex = "(" + x + "/" + y + ")";

        return exIndex;
    }

    public String setIndex(){
        String setIndex;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        int x = 0;
        int y = 0;
        String key = "_key";
        boolean keepGoing = true;
        /**
         * x/y
         * so for the x value we need to go through each [0] and add the amount of [1]'s to an incrementer. Then we
         * stop once the values [0]_[1] = the one we're in.
         *
         * Try math maybe?
         */
        for(Map.Entry<String, HashMap<String, List<String>>> entry1 : mExInfoHashMap.entrySet()){
            String entry1Key = entry1.getKey();
            for(Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()){
                if(entry2.getValue().size() == 1){

                }else{

                }
                y++;
                if(keepGoing){
                    x++;
                    if(entry2.getKey().equals(tokens[1] + key) && entry1Key.equals(tokens[0] + key)){
                        keepGoing = false;
                    }
                }
            }
        }

        setIndex = "(" + x + "/" + y + ")";

        return setIndex;
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

        if(!getViewCursor().equals("1_0_1")){
            if(getViewCursor().equals("workoutDone")){
                // go to last place
                //Log.i("progressModel", "90, cursor = " + cursor);
                Log.i("previousMethod", "187 " + getViewCursor());
                int decremented0 = getExInfoHashMap().size();
                String key = "_key";
                Log.i("previousMethod", "decremented0 = " + decremented0);
                int sizex = getExInfoHashMap().get(String.valueOf(decremented0) + key).size();
                Log.i("previousMethod", "sizex = " + sizex);
                if(sizex == 1){
                    // previous is not a superset map
                    Log.i("previousMethod", "194 " + getViewCursor());
                    int size1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).size() - 1;
                    int size2 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get(String
                            .valueOf(size1) + key).size();
                    setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(size1) + "_" +
                            String.valueOf(size2 - 1));
                }else{
                    // previous is a superset map
                    Log.i("previousMethod", "202 " + getViewCursor());
                    int size1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).size();
                    int size1_0 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get("0_key")
                            .size();
                    int size1_1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get(String
                            .valueOf(size1 - 1) + key).size();
                    Log.i("previousMethod", "size1 = " + size1 + ", size1_0 = " + size1_0 + ", size1_1 " +
                            "= " + size1_1);
                    if(size1_0 != size1_1){
                        Log.i("previousMethod", "209 " + getViewCursor());
                        // if overflow map
                        setViewCursor(String.valueOf(decremented0) + "_0_" +
                                String.valueOf(size1_0 - 1));
                    }else{
                        // if not overflow map
                        Log.i("previousMethod", "215 " + getViewCursor());
                        setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(size1 - 1) + "_" +
                                String.valueOf(size1_1 - 1));
                    }
                }
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
                            Log.i("previousMethod", "187 " + getViewCursor());
                            int decremented0 = Integer.parseInt(tokens[0]) - 1;
                            Log.i("previousMethod", "decremented0 = " + decremented0);
                            int sizex = getExInfoHashMap().get(String.valueOf(decremented0) + key).size();
                            Log.i("previousMethod", "sizex = " + sizex);
                            if(sizex == 1){
                                // previous is not a superset map
                                Log.i("previousMethod", "194 " + getViewCursor());
                                int size1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).size() - 1;
                                int size2 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get(String
                                        .valueOf(size1) + key).size();
                                setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(size1) + "_" +
                                        String.valueOf(size2 - 1));
                            }else{
                                // previous is a superset map
                                Log.i("previousMethod", "202 " + getViewCursor());
                                int size1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).size();
                                int size1_0 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get("0_key")
                                        .size();
                                int size1_1 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get(String
                                        .valueOf(size1 - 1) + key).size();
                                Log.i("previousMethod", "size1 = " + size1 + ", size1_0 = " + size1_0 + ", size1_1 " +
                                        "= " + size1_1);
                                if(size1_0 != size1_1){
                                    Log.i("previousMethod", "209 " + getViewCursor());
                                    // if overflow map
                                    setViewCursor(String.valueOf(decremented0) + "_0_" +
                                            String.valueOf(size1_0 - 1));
                                }else{
                                    // if not overflow map
                                    Log.i("previousMethod", "215 " + getViewCursor());
                                    setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(size1 - 1) + "_" +
                                            String.valueOf(size1_1 - 1));
                                }
                            }
                            //int newValue = Integer.parseInt(tokens[0]) - 1;
                            //setViewCursor(String.valueOf(newValue) + "_0_1");
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
                     * Let's just say I'm at 2_0_5. Here would be the correct sequence of previous button clicks
                     * 2_0_5
                     * 2_0_4
                     * 2_1_3
                     * 2_0_3
                     * 2_1_2
                     * 2_0_2
                     * 2_1_1
                     * 2_0_1
                     * How do we produce that?
                     *
                     * Ok so the problem was on the second 5@125, aka 2_0_2 -> 2_1_1
                     *
                     * New problem, jumping in an overflow map to the previous overflow item jumped to a non-existent
                     * item in [1] = 1
                     */
                    int minusValue = Integer.parseInt(tokens[1]) - 1;

                    if(minusValue < 0){
                        // we're in the first list
                        Log.i("progressModel", "206, previous, cursor = " + cursor + "; minus value = " + minusValue);
                        //jumpToPreviousMap();
                        if((tokens[1] + "_" + tokens[2]).equals("0_1")){
                            jumpToPreviousMap();
                        }else{
                            int decremented2 = Integer.parseInt(tokens[2]) - 1;
                            int maxList = getExInfoHashMap().get(tokens[0] + key).size() - 1;
                            if(getExInfoHashMap().get(tokens[0] + key).get(String.valueOf(maxList) + key).size() <
                                    Integer.parseInt(tokens[2])){
                                setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(decremented2));
                            }else{
                                setViewCursor(tokens[0] + "_" + String.valueOf(maxList) + "_" + String.valueOf(decremented2));
                            }
                        }
                    }else{
                        if(getExInfoHashMap().get(tokens[0] + key).get(String.valueOf(minusValue) + key).size() < Integer
                                .parseInt(tokens[2]) - 1){
                            Log.i("progressModel", "211, previous, cursor = " + cursor);
                            // overflow
                            // guarantees we're in [1] = 0 because overflow lists are inherently first in their map
                            if(tokens[2].equals("1")){
                                Log.i("progressModel", "215, previous, cursor = " + cursor);
                                // jump to previous map
                                /**
                                 * If the current map is 1_key, we're back at the beginning. Do nothing.
                                 * If the previous map is not a superset, just jump to the size of that map - 1.
                                 * If the previous map is a superset:
                                 *  Find out if the first one has extra sets. If it does, jump to the last of that first one.
                                 *  If they're all the same length, jump to the last index of the last list
                                 */
                                jumpToPreviousMap();
                            }else{
                                // decrement [2]
                                int newValue = Integer.parseInt(tokens[2]) - 1;
                                setViewCursor(tokens[0] + "_" + tokens[1] + "_" + String.valueOf(newValue));
                                Log.i("progressModel", "229, previous, cursor = " + getViewCursor());
                            }
                        }else{

                            /**
                             * not overflow, can go to previous list
                             * let's just say I'm in 2_1_2, aka 3@55. Previous should be 2_0_1
                             * OK so if [1] == 0 then we go to the highest [1] with [2] - 1
                             * we're still going to have to jump to previous map if we end up at x_0_1
                             */
                            Log.i("progressModel", "239, previous, cursor = " + cursor);
                            if((tokens[1] + "_" + tokens[2]).equals("0_1")){
                                // jump to previous map
                                jumpToPreviousMap();
                            }else{
                                // subtract em
                                Log.i("progressModel", "245, previous, cursor = " + cursor);
                                /**
                                 * Let's really think this one out.
                                 * 2_1_3
                                 * 2_0_3
                                 * 2_1_2
                                 * 2_0_2
                                 * 2_1_1
                                 * 2_0_1
                                 */

                                int decremented1 = Integer.parseInt(tokens[1]) - 1;
                                int decremented2 = Integer.parseInt(tokens[2]) - 1;

                                if(tokens[1].equals("0")){
                                    // in first list, jump to max list and [2] - 1
                                    int maxList = getExInfoHashMap().get(tokens[0] + key).size() - 1;
                                    setViewCursor(tokens[0] + "_" + String.valueOf(maxList) + "_" + String.valueOf(decremented2));
                                    Log.i("progressModel", "263, previous, cursor = " + getViewCursor());
                                }else{
                                    // decrease [1] only
                                    setViewCursor(tokens[0] + "_" + String.valueOf(decremented1) + "_" + tokens[2]);
                                    Log.i("progressModel", "267, previous, cursor = " + getViewCursor());
                                }
                                //setViewCursor(tokens[0] + "_" + String.valueOf(decremented1) + "_" + String.valueOf
                                //        (decremented2));
                            }
                        }

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
        }
    }

    private void jumpToPreviousMap(){

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);
        String cursor = getViewCursor();
        String key = "_key";

        Log.i("progressModel", "jumpToPreviousMap");

        if(!tokens[0].equals("1")){
            int decremented0 = Integer.parseInt(tokens[0]) - 1;
            if(getExInfoHashMap().get(String.valueOf(decremented0) + key).size() < 2){
                // not superset
                int size = getExInfoHashMap().get(String.valueOf(decremented0) + key).get("0_key")
                        .size();
                setViewCursor(String.valueOf(decremented0) + "_" + "0_" + String.valueOf(size - 1));
            }else{
                // superset
                if(getExInfoHashMap().get(String.valueOf(decremented0) + key).get("0_key").size()
                        > getExInfoHashMap().get(String.valueOf(decremented0) + key).get("1_key").size()){
                    // has overflow
                    int size2 = getExInfoHashMap().get(String.valueOf(decremented0) + key).get
                            ("0_key").size();
                    setViewCursor(String.valueOf(decremented0) + "_" + "0_" + String.valueOf(size2 -
                            1));
                }else{
                    // has no overflow
                    int numberOfLists = getExInfoHashMap().get(String.valueOf(decremented0) + key).size();
                    int numberOfItems = getExInfoHashMap().get(String.valueOf(decremented0) + key)
                            .get("0_key").size();
                    setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(numberOfLists)
                            + "_" + String.valueOf(numberOfItems));
                }
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
