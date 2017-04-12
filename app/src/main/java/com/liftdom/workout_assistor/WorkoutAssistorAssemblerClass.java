package com.liftdom.workout_assistor;


import org.joda.time.LocalDate;

import java.util.ArrayList;


public class WorkoutAssistorAssemblerClass {

    // Singleton boilerplate
    private static WorkoutAssistorAssemblerClass controller;
    static WorkoutAssistorAssemblerClass getInstance() {
        if (controller == null) {
            controller = new WorkoutAssistorAssemblerClass();
        }
        return controller;
    }

    public ArrayList<String> DoWAL1 = new ArrayList<>();

    private int indexIncrementor = 0;

    String templateName = null;

    String privateJournal = null;

    //SMOLOV STUFF
    double oneRM;
    int week;
    int day;
    String exName;
    LocalDate localDate;

    public void setRepsWeight(String exercise, String repsWeight){
        if(DoWAL1.contains(exercise)){
            int exIndex = DoWAL1.indexOf(exercise);
            DoWAL1.add(exIndex + indexIncrementor, repsWeight);
            indexIncrementor++;
        }else{
            DoWAL1.add(exercise);
            DoWAL1.add(repsWeight);
            indexIncrementor = 2;
        }
    }
}
