package com.liftdom.workout_assistor;

import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogRecord;

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
    private boolean mIsRunningImperial;
    private String mViewCursor; // the current set we're on
    private String mRefKey;
    private String mRestTime;
    private boolean mIsActiveRestTimer;
    private boolean mIsRevise;
    private boolean mIsFromRestDay;
    private String mVibrationTime;
    private boolean mIsRestTimerAlert;
    private HashMap<String, String> mPreMadeInfo;
    private String mTemplateName;

    public WorkoutProgressModelClass(){
        // necessary for Firebase
    }

    public WorkoutProgressModelClass(String date, boolean completedBool, HashMap<String, HashMap<String, List<String>>> exInfoHashMap,
                                     String privateJournal, String publicComment, String mediaResource,
                                     boolean isTemplateImperial, String refKey, boolean isRevise, boolean
                                             isFromRestDay){
        mDate = date;
        mCompletedBool = completedBool;
        mExInfoHashMap = exInfoHashMap;
        mPrivateJournal = privateJournal;
        mPublicComment = publicComment;
        mMediaResource = mediaResource;
        mIsTemplateImperial = isTemplateImperial;
        if(refKey != null){
            mRefKey = refKey;
        }
        mIsRevise = isRevise;
        mIsFromRestDay = isFromRestDay;
    }

    public String getTemplateName() {
        return mTemplateName;
    }

    public void setTemplateName(String mTemplateName) {
        this.mTemplateName = mTemplateName;
    }

    public boolean isIsRunningImperial() {
        return mIsRunningImperial;
    }

    public void setIsRunningImperial(boolean mIsRunningImperial) {
        this.mIsRunningImperial = mIsRunningImperial;
    }

    public HashMap<String, String> getPreMadeInfo() {
        return mPreMadeInfo;
    }

    public void setPreMadeInfo(HashMap<String, String> mPreMadeInfo) {
        this.mPreMadeInfo = mPreMadeInfo;
    }

    public String getVibrationTime() {
        return mVibrationTime;
    }

    public void setVibrationTime(String mVibrationTime) {
        this.mVibrationTime = mVibrationTime;
    }

    public boolean isIsRestTimerAlert() {
        return mIsRestTimerAlert;
    }

    public void setIsRestTimerAlert(boolean mIsRestTimerAlert) {
        this.mIsRestTimerAlert = mIsRestTimerAlert;
    }

    public boolean isIsActiveRestTimer() {
        return mIsActiveRestTimer;
    }

    public void setIsActiveRestTimer(boolean mIsActiveRestTimer) {
        this.mIsActiveRestTimer = mIsActiveRestTimer;
    }

    public String getRestTime() {
        return mRestTime;
    }

    public void setRestTime(String mRestTime) {
        this.mRestTime = mRestTime;
    }

    public boolean isIsFromRestDay() {
        return mIsFromRestDay;
    }

    public void setIsFromRestDay(boolean mIsFromRestDay) {
        this.mIsFromRestDay = mIsFromRestDay;
    }

    public boolean isIsRevise() {
        return mIsRevise;
    }

    public void setIsRevise(boolean mIsRevise) {
        this.mIsRevise = mIsRevise;
    }

    public String getRefKey() {
        return mRefKey;
    }

    public void setRefKey(String mRefKey) {
        this.mRefKey = mRefKey;
    }

    public String exIndex(){
        String exIndex;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        int x = 0;
        int y = 0;
        String key = "_key";
        boolean keepGoing = true;
        /*
         * x/y
         * so for the x value we need to go through each [0] and add the amount of [1]'s to an incrementer. Then we
         * stop once the values [0]_[1] = the one we're in.
         *
         * LOL NO JUST DO MATH 5HEAD
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

    public String checkForAdvancedViewCursor(){
        String viewCursor = "1_0_1";

        for(int i = 1; i <= getExInfoHashMap().size(); i++){
            for(Map.Entry<String, HashMap<String, List<String>>> entry : getExInfoHashMap().entrySet()){
                String delims = "[_]";
                String[] tokens = entry.getKey().split(delims);
                if(Integer.parseInt(tokens[0]) == i){
                    for(int j = 0; j < entry.getValue().size(); j++){
                        for(Map.Entry<String, List<String>> entry2 : entry.getValue().entrySet()){
                            String[] tokens2 = entry2.getKey().split(delims);
                            if(Integer.parseInt(tokens2[0]) == j){
                                for(int k = 0; k < entry2.getValue().size(); k++){
                                    String value = entry2.getValue().get(k);
                                    String[] tokens3 = value.split(delims);
                                    if(tokens3[tokens3.length - 1].equals("checked")){
                                        String one = tokens[0];
                                        String two = tokens2[0];
                                        String three = String.valueOf(k);
                                        viewCursor = one + "_" + two + "_" + three;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        return viewCursor;
    }

    public String setIndex(){
        String setIndex;

        int x = 0;
        int y = 0;
        int x2 = 0;
        int y2 = 0;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);
        int tokensInt0 = Integer.parseInt(tokens[0]);
        int tokensInt1 = Integer.parseInt(tokens[1]);
        int tokensInt2 = Integer.parseInt(tokens[2]);
        String key = "_key";

        /*
         * For each map less than [0], get size of all lists - amount of lists x 1
         * For the map that matches [0], get [2] - 1 and multiply it by [1]
         */

        for(int i = 1; i <= tokensInt0; i++){
            if(i != tokensInt0){
                // is a map before, get number of all items in here.
                if(getExInfoHashMap().get(String.valueOf(i) + key).size() == 1){
                    // non superset map
                    int size = getExInfoHashMap().get(String.valueOf(i) + key).get("0_key").size() - 1;
                    x = x + size;
                }else{
                    // superset map
                    for(Map.Entry<String, List<String>> entry : getExInfoHashMap().get(String.valueOf(i) + key).entrySet()){
                        int size = entry.getValue().size() - 1;
                        x = x + size;
                    }
                }
            }else{
                // is the current cursor map, get number of items leading up to. Refer to theory.
                if(getExInfoHashMap().get(String.valueOf(i) + key).size() == 1){
                    // non superset map
                    x = x + tokensInt2;
                    // view cursor: 1_0_2
                    y2 = getExInfoHashMap().get(tokensInt0 + key).get(tokensInt1 + key).size() - 1;
                    x2 = tokensInt2;
                }else{
                    // superset map
                    /*
                     * Let's say we're at 2_0_2 and there are 2 maps, 0_key and 1_key.
                     * That means that we're 3 in.
                     * 2_1_2 means we're 4 in.
                     * 2_0_3 means we're 5 in.
                     * 2_1_3 means we're 6 in.
                     *
                     * So I think the way to go is if [2] > 1 we do [2] - 1 * size + [2] * [1] + 1
                     *
                     * now we need to account for overflow
                     */

                    /*
                     * YOO so maybe here we can have the (1/3) type place within exercise
                     * 2_0_1 = 1
                     * 2_1_1 = 2
                     * 2_0_2 = 3
                     * 2_1_2 = 4
                     * 2_0_3 = 5
                     * 2_1_3 = 6
                     * 2_0_4 = 7
                     * 2_1_4 = 8
                     * 2_0_5 = 9
                     * 2_1_5 = 10
                     * 2_0_6 = 11
                     * 2_1_6 = 12
                     *
                     * 2_0_1 = 1
                     * 2_1_1 = 2
                     * 2_2_1 = 3
                     * 2_0_2 = 4
                     * 2_1_2 = 5
                     * 2_2_2 = 6
                     * 2_0_3 = 7
                     * 2_1_3 = 8
                     * 2_2_3 = 9
                     * 2_0_4 = 10
                     * 2_1_4 = 11
                     * 2_2_4 = 12
                     * 2_0_5 = 13
                     * 2_1_5 = 14
                     * 2_2_5 = 15
                     * 2_0_6 = 16
                     * 2_1_6 = 17
                     * 2_2_6 = 18
                     *
                     * if tokens1 = 0, it's tokens2 - 1 * getExInfoHashMap().get(String.valueOf(i) + key).size()
                     * it's tokens2 - 1 * getExInfoHashMap().get(String.valueOf(i) + key).size()
                     * + 1 + tokens1
                     *
                     * lmaoooo it actually works
                     */

                    int size = getExInfoHashMap().get(String.valueOf(i) + key).size();
                    int size1 = getExInfoHashMap().get(String.valueOf(i) + key).get("1_key").size() - 1;
                    for(Map.Entry<String, List<String>> entry :
                            getExInfoHashMap().get(String.valueOf(i) + key).entrySet()){
                        y2 = y2 + (entry.getValue().size() - 1);
                    }

                    if(tokensInt2 != 1){
                        x2 = ((tokensInt2 - 1) * getExInfoHashMap().get(String.valueOf(i) + key).size()) + 1 + tokensInt1;
                    }else{
                        x2 = tokensInt1 + 1;
                    }

                    if(tokensInt2 == 1){
                        // we're in a first pass through the superset map
                        // ok this seems to work
                        x = x + (tokensInt2 * (tokensInt1 + 1));
                    }else{
                        // we've gone through the superset loop
                        if(tokensInt1 == 0 && tokensInt2 > size1){
                            // if in overflow
                            x = x + (((tokensInt2 - (tokensInt2 - size1)) * size) + (tokensInt2 - size1));
                        }else{
                            x = x + (((tokensInt2 - 1) * size) + (tokensInt1 + 1));
                        }
                    }

                }
            }
        }

        for(Map.Entry<String, HashMap<String, List<String>>> entry1 : getExInfoHashMap().entrySet()){
            for(Map.Entry<String, List<String>> entry2 : entry1.getValue().entrySet()){
                y = y + (entry2.getValue().size() - 1);
            }
        }

        setIndex = "(" + x + "/" + y + ")(" + x2 + "/" + y2 + ")";

        return setIndex;
    }

    public void addExercise(String exName){
        String key;

        if(getExInfoHashMap() != null){
            key = String.valueOf(getExInfoHashMap().size() + 1) + "_key";
        }else{
            key = "1_key";
        }


        List<String> list = new ArrayList<>();
        list.add(0, exName);
        list.add(1, "0@0_unchecked");

        HashMap<String, List<String>> map1 = new HashMap<>();
        map1.put("0_key", list);

        if(getExInfoHashMap() != null){
            mExInfoHashMap.put(key, map1);
        }else{
            mExInfoHashMap = new HashMap<>();
            mExInfoHashMap.put(key, map1);
        }

    }

    public void setViewCursorToLast(){

        String key = "_key";

        int size0 = getExInfoHashMap().size();
        int size1 = getExInfoHashMap().get(String.valueOf(size0) + key).size();

        if(size1 == 1){
            // non-superset
            int size2 = getExInfoHashMap().get(String.valueOf(size0) + key).get(String.valueOf(size1 - 1) + key).size();
            Log.i("lastSetAction", size0 + "_" + size1 + "_" + size2);
            setViewCursor(size0 + "_" + String.valueOf(size1 - 1) + "_" + String.valueOf(size2 - 1));
        }else{
            // superset
            if(getExInfoHashMap().get(String.valueOf(size0) + key).get("0_key").size() != getExInfoHashMap().get
                    (String.valueOf(size0) + key).get("1_key").size()){
                // overflow
                int size2 = getExInfoHashMap().get(String.valueOf(size0) + key).get("0_key").size();
                setViewCursor(size0 + "_0_" + String.valueOf(size2 - 1));
            }else{
                // not overflow
                int size2 = getExInfoHashMap().get(String.valueOf(size0) + key).get("1_key").size();
                setViewCursor(size0 + "_" + String.valueOf(size1 - 1) + "_" + String.valueOf(size2 - 1));
            }
        }
    }

    public void reFindViewCursor(){
        /*
         * In case of exercise/set deletion, try to find if the old view cursor data matches itself,
         * if not,
         * First find identical data set (full data set, meaning exercise + superset value + set_checkedValue).
         * then we'd check if the exercise still exists. If it does, we try to find if it has a superset.
         * then try to find the exact set_checkedValue, then try to find just the set, then if all else fails
         * we'd just have to reset to 1_0_1. Also we'd have to have a case for the possibility of having no exercises.
         * If no exercises, we'd have to stop the service. And then update runningAssistor when an exercise is added.
         * That ties into how we need to update the process of adding exercises to the WA to guarantee a valid ex/sets.
         *
         * If the set being deleted is the view cursor, go back one. If eventually nothing left, reset.
         */
    }

    /*
     * We should, instead of deleting this on AssistorSaved, update this class with the ref keys necessary to
     * re-do/edit the workout we did that day.
     *
     */

    public String setIndex1(){
        String setIndex;

        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        int x = 0;
        int y = 0;
        String key = "_key";
        boolean keepGoing = true;
        /*
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
        /*
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
            /*
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
                    int newCursor = Integer.parseInt(tokens[0]) + 1;
                    if(getExInfoHashMap().get(newCursor + key) != null){
                        setViewCursor(newCursor + "_0_1");
                    }else{
                        setViewCursor("workoutDone");
                    }
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
                    /*
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
                                /*
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

                            /*
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
                                /*
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

        // needs to go to 2_1_3, is
        // on 3_0_1, tries to go to 2_2_4

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
                    int numberOfLists =
                            getExInfoHashMap().get(String.valueOf(decremented0) + key).size() - 1;
                    int numberOfItems = getExInfoHashMap().get(String.valueOf(decremented0) + key)
                            .get("0_key").size() - 1;
                    setViewCursor(String.valueOf(decremented0) + "_" + String.valueOf(numberOfLists)
                            + "_" + String.valueOf(numberOfItems));
                }
            }
        }
    }

    public boolean isAmrap(String string){
        // 8_a@230_unchecked
        boolean amrap = false;

        String delims = "[_,@]";
        String[] tokens = string.split(delims);

        try{
            if(tokens[1].equals("a")){
                amrap = true;
            }
        }catch (IndexOutOfBoundsException e){

        }

        return amrap;
    }

    public boolean toggleCheck(){
        boolean isChecking = false;
        String delims = "[_]";
        String[] tokens = getViewCursor().split(delims);

        String currentSet = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt(tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet);

        String newString = "";

        if(isAmrap(currentSet)){
            String[] tokens2 = currentSet.split(delims);

            if(tokens2[2].equals("checked")){
                tokens2[2] = "unchecked";
            }else{
                tokens2[2] = "checked";
                isChecking = true;
            }

            for(int i = 0; i < tokens2.length; i++){
                if(i == 0){
                    newString = tokens2[i];
                }else{
                    newString = newString + "_" + tokens2[i];
                }
            }
        }else{
            String[] tokens2 = currentSet.split(delims);

            if(tokens2[1].equals("checked")){
                tokens2[1] = "unchecked";
            }else{
                tokens2[1] = "checked";
                isChecking = true;
            }

            for(int i = 0; i < tokens2.length; i++){
                if(i == 0){
                    newString = tokens2[i];
                }else{
                    newString = newString + "_" + tokens2[i];
                }
            }
        }

        getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").set(Integer.parseInt(tokens[2]), newString);
        String currentSet2 = getExInfoHashMap().get(tokens[0] + "_key").get(tokens[1] + "_key").get(Integer.parseInt
                (tokens[2]));
        Log.i("serviceInfo", "currentSet = " + currentSet2);

        if(isChecking){
            //nextDelay();
        }

        return isChecking;
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
