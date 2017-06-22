package com.liftdom.liftdom;

import com.liftdom.user_profile.UserModelClass;

/**
 * Created by Brodin on 4/29/2017.
 */

public class MainActivitySingleton {
    // Singleton boilerplate
    private static MainActivitySingleton controller;

    public static MainActivitySingleton getInstance() {
        if (controller == null) {
            controller = new MainActivitySingleton();
        }
        return controller;
    }

    public boolean isWorkoutFinished = false;

    public String currentActiveTemplate = "null";

    public UserModelClass userModelClass;
}
