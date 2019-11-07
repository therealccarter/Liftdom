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

    String programName = "";
    Boolean pplpplrBool;
    String inclineDB;
    String tricepsPushdowns;
    String overheadTricepsExtensions;
    String seatedCableRows;
    String facePulls;
    String dumbbellCurls;
    String hammerCurls;
    String legPress;
    String legCurls;
    String barbellCalfRaises;
    boolean isActiveCheckbox;
    boolean isImperial;
    String userName;
    String uid;

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
        extraInfo.put("pplpplrBool", pplpplrBool.toString());
        extraInfo.put("inclineDB", inclineDB);
        extraInfo.put("tricepsPushdowns", tricepsPushdowns);
        extraInfo.put("overheadTricepsExtensions", overheadTricepsExtensions);
        extraInfo.put("seatedCableRows", seatedCableRows);
        extraInfo.put("facePulls", facePulls);
        extraInfo.put("dumbbellCurls", dumbbellCurls);
        extraInfo.put("hammerCurls", hammerCurls);
        extraInfo.put("legPress", legPress);
        extraInfo.put("legCurls", legCurls);
        extraInfo.put("barbellCalfRaises", barbellCalfRaises);

        return extraInfo;
    }

}
