package com.liftdom.workout_assistor;

import java.util.ArrayList;

/**
 * Created by Chris on 10/20/2016.
 */

public class WorkoutAssistorAssemblerClass {

    //Singleton boilerplate
    private static WorkoutAssistorAssemblerClass controller;
    static WorkoutAssistorAssemblerClass getInstance(){
        if(controller == null){
            controller = new WorkoutAssistorAssemblerClass();
        }
        return controller;
    }

    // DoW ArrayLists
    public ArrayList<String> DoWAL1 = new ArrayList<>();
    private ArrayList<String> DoWAL2 = new ArrayList<>();
    private ArrayList<String> DoWAL3 = new ArrayList<>();
    private ArrayList<String> DoWAL4 = new ArrayList<>();
    private ArrayList<String> DoWAL5 = new ArrayList<>();
    private ArrayList<String> DoWAL6 = new ArrayList<>();
    private ArrayList<String> DoWAL7 = new ArrayList<>();







}
