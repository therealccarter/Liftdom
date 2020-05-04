package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;

import org.joda.time.LocalDate;

import java.util.HashMap;

/**
 * Created by Brodin on 5/18/2018.
 */
public class W531fBSingleton {
    // Singleton boilerplate
    private static W531fBSingleton controller;
    public static W531fBSingleton getInstance() {
        if (controller == null) {
            controller = new W531fBSingleton();
        }
        return controller;
    }

    String uid = "";
    String userName = "";
    String programName = "";
    String squatMax = "";
    String benchMax = "";
    String deadliftMax = "";
    String ohpMax = "";
    String pushSetScheme = "";
    String pullSetScheme = "";
    String legCoreSetScheme = "";
    //boolean autoDeload = false;
    boolean isImperial;
    boolean isActiveCheckbox;
    boolean isRoundToNearest5;
    boolean isBeginToday;
    String mRestTime;
    boolean mIsActiveRestTimer;
    String mVibrationTime;
    boolean mIsRestTimerAlert;

    public String getStartDateString(){
        String message;

        LocalDate today = LocalDate.now();
        int old = today.getDayOfWeek();
        if(old == 1){
            message = "This program starts on Monday, so you will be starting today.";
        }else{
            int monday = 1;
            if(monday <= old){
                monday += 7;
            }
            String beginDate = today.plusDays(monday - old).toString();
            message = "This program starts on Monday, so you will be starting this coming Monday (" + beginDate + ").";
        }

        return message;
    }

    public HashMap<String, String> assembleExtraInfoMap(){
        HashMap<String, String> extraInfo = new HashMap<>();

        String beginDate;
        if(isBeginToday){
            beginDate = LocalDate.now().toString();
        }else{
            LocalDate today = LocalDate.now();
            int old = today.getDayOfWeek();
            int monday = 1;

            if(monday <= old){
                monday += 7;
            }
            beginDate = today.plusDays(monday - old).toString();
        }

        extraInfo.put("beginDate", beginDate);
        extraInfo.put("squatMax", squatMax);
        extraInfo.put("benchMax", benchMax);
        extraInfo.put("deadliftMax", deadliftMax);
        extraInfo.put("ohpMax", ohpMax);
        extraInfo.put("squatMaxOG", squatMax);
        extraInfo.put("benchMaxOG", benchMax);
        extraInfo.put("deadliftMaxOG", deadliftMax);
        extraInfo.put("ohpMaxOG", ohpMax);
        //extraInfo.put("roundToNearest5", String.valueOf(isRoundToNearest5));

        return extraInfo;
    }
}
