package com.liftdom.liftdom.utils.exercise_selector;

import java.util.ArrayList;

/**
 * Created by Brodin on 3/25/2017.
 */

public class ExSelectorSingleton {

    // Singleton boilerplate
    private static ExSelectorSingleton controller;

    public static ExSelectorSingleton getInstance() {
        if (controller == null) {
            controller = new ExSelectorSingleton();
        }
        return controller;
    }

    public ArrayList<String> upperBodyItems = new ArrayList<>();
    public ArrayList<String> lowerBodyItems = new ArrayList<>();
    public ArrayList<String> fullBodyItems = new ArrayList<>();

    public void clearArrayList(){
        upperBodyItems.clear();
        lowerBodyItems.clear();
        fullBodyItems.clear();
    }

}