package com.liftdom.workout_programs.Smolov;

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



        return extraInfo;
    }
}
