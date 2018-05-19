package com.liftdom.workout_programs.FiveThreeOne;

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

    String squatMax = "";
    String benchMax = "";
    String deadliftMax = "";
}
