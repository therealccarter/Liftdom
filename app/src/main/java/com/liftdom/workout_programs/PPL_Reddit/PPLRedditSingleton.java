package com.liftdom.workout_programs.PPL_Reddit;

import org.joda.time.LocalDate;

import java.util.HashMap;

/**
 * Created by Brodin on 10/31/2019.
 */
public class PPLRedditSingleton {

    private static PPLRedditSingleton controller;
    public static PPLRedditSingleton getInstance() {
        if (controller == null) {
            controller = new PPLRedditSingleton();
        }
        return controller;
    }

    String format;
    String programName = "";
    String inclineDB;
    String tricepsPushdowns;
    String overheadTricepsExtensions;
    String pulldowns;
    String seatedCableRows;
    String facePulls;
    String dumbbellCurls;
    String hammerCurls;
    String legPress;
    String legCurls;
    String barbellCalfRaises;
    String dips;
    String abs1;
    String abs2;
    String version;
    boolean isActiveCheckbox;
    boolean isImperial;
    String userName;
    String uid;
    boolean isBeginToday;
    boolean isWarmup;
    String mRestTime;
    boolean mIsActiveRestTimer;
    String mVibrationTime;
    boolean mIsRestTimerAlert;

    String getStartDateString(){
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

    HashMap<String, String> assembleExtraInfoMap(){
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
        extraInfo.put("format", format);
        extraInfo.put("pulldowns", pulldowns);
        extraInfo.put("seatedCableRows", seatedCableRows);
        extraInfo.put("facePulls", facePulls);
        extraInfo.put("dumbbellCurls", dumbbellCurls);
        extraInfo.put("hammerCurls", hammerCurls);
        extraInfo.put("inclineDB", inclineDB);
        extraInfo.put("tricepsPushdowns", tricepsPushdowns);
        extraInfo.put("overheadTricepsExtensions", overheadTricepsExtensions);
        extraInfo.put("legPress", legPress);
        extraInfo.put("legCurls", legCurls);
        extraInfo.put("calfRaises", barbellCalfRaises);
        extraInfo.put("dips", dips);
        extraInfo.put("abs1", abs1);
        extraInfo.put("abs2", abs2);
        extraInfo.put("rdl", "Romanian Deadlift (Barbell - Conventional)");
        extraInfo.put("benchPress", "Bench Press (Barbell - Flat)");
        extraInfo.put("ohp", "Overhead Press (Barbell)");
        extraInfo.put("squat", "Squat (Barbell - Back)");
        extraInfo.put("deadlift", "Deadlift (Barbell - Conventional)");
        extraInfo.put("barbellRows", "Row (Barbell - Bent-over)");
        extraInfo.put("isWarmup", String.valueOf(isWarmup));
        extraInfo.put("version", version);
        extraInfo.put("benchPressA", "Bench Press (Accessory)");
        extraInfo.put("ohpA", "Overhead Press (Accessory)");

        return extraInfo;
    }

}
