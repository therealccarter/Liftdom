package com.liftdom.liftdom.intro;

/**
 * Created by Brodin on 6/20/2017.
 */

public class IntroSingleton {
    // Singleton boilerplate
    private static IntroSingleton controller;
    static IntroSingleton getInstance() {
        if (controller == null) {
            controller = new IntroSingleton();
        }
        return controller;
    }

    String userId;

    String defaultDisplayName;

    String displayName;

    boolean isImperial = true;
    boolean isMale = true;

    String weightImperial;
    String weightMetric;

    String feet;
    String inches;
    String cm;

    String age;


}
