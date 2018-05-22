package com.liftdom.workout_programs.FiveThreeOne;

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

    String programName = "";
    String squatMax = "";
    String benchMax = "";
    String deadliftMax = "";
    String ohpMax = "";
    boolean autoDeload = false;

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
        LocalDate today = LocalDate.now();
        int old = today.getDayOfWeek();
        if(old == 1){
            beginDate = today.toString();
        }else{
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
        extraInfo.put("autoDeload", String.valueOf(autoDeload));

        return extraInfo;
    }
}
