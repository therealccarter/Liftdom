package com.liftdom.workout_programs.Smolov;

import org.joda.time.LocalDate;

import java.util.HashMap;

/**
 * Created by Brodin on 10/13/2017.
 */

public class SmolovSetupSingleton {
    // Singleton boilerplate
    private static SmolovSetupSingleton controller;
    public static SmolovSetupSingleton getInstance() {
        if (controller == null) {
            controller = new SmolovSetupSingleton();
        }
        return controller;
    }

    boolean isBeginToday;
    String exName;
    String maxWeight;
    String programName;

    public HashMap<String, String> assembleSmolovMap(){
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
        extraInfo.put("exName", exName);
        extraInfo.put("maxWeight", maxWeight);

        return extraInfo;
    }
}
