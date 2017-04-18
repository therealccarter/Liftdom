package com.liftdom.charts_stats_tools.exercise_selector;

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
    public ArrayList<String> otherItems = new ArrayList<>();

    public ArrayList<String> completedExercises = new ArrayList<>();

    public void clearArrayLists(){
        upperBodyItems.clear();
        lowerBodyItems.clear();
        otherItems.clear();
    }

}
