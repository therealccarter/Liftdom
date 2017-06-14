package com.liftdom.workout_assistor;

import java.util.List;

/**
 * Created by Brodin on 6/14/2017.
 */

public class CompletedExercisesModelClass {

    private List<String> mCompletedExercisesList;

    public CompletedExercisesModelClass(){
        // necessary for Firebase
    }

    public CompletedExercisesModelClass(List<String> completedExercisesList){
        mCompletedExercisesList = completedExercisesList;
    }

    public List<String> getCompletedExercisesList() {
        return mCompletedExercisesList;
    }

    public void setCompletedExercisesList(List<String> mCompletedExercisesList) {
        this.mCompletedExercisesList = mCompletedExercisesList;
    }

    public void addItems(List<String> list){
        List<String> completedList = getCompletedExercisesList();
        completedList.addAll(list);
    }
}
