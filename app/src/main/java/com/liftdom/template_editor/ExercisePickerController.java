package com.liftdom.template_editor;



/**
 * Created by Chris on 11/10/2016.
 */

public class ExercisePickerController {

    // Singleton boilerplate
    private static ExercisePickerController controller;
    public static ExercisePickerController getInstance() {
        if (controller == null) {
            controller = new ExercisePickerController();
        }
        return controller;
    }

    public int exID = 0;

    public String fragTag;

    public String exName;
}
