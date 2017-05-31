package com.liftdom.workout_assistor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brodin on 5/30/2017.
 */

public class AssistorSingleton {
    // Singleton boilerplate
    private static AssistorSingleton controller;
    static AssistorSingleton getInstance() {
        if (controller == null) {
            controller = new AssistorSingleton();
        }
        return controller;
    }

    List<String> endList = new ArrayList<>();

    /**
     *  What we need for saving of workout progress:
     *
     *
     *
     *  What we need for algorithm checking:
     *
     *
     */

}
