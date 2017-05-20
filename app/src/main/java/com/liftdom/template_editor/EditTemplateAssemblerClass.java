package com.liftdom.template_editor;
// Created: 9/20/2016


import java.util.ArrayList;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

class EditTemplateAssemblerClass {

    // Singleton boilerplate
    private static EditTemplateAssemblerClass controller;
    static EditTemplateAssemblerClass getInstance() {
        if (controller == null) {
            controller = new EditTemplateAssemblerClass();
        }
        return controller;
    }

    HashMap<String, List<String>> tempAlgoInfo = new HashMap<>();
    HashMap<String, List<String>> tempAlgoInfo2 = new HashMap<>();

    Boolean isOnSaveClick = false;

    boolean isEditAndFirstTime = false;

    void clearAll(){
        tempAlgoInfo.clear();
        tempAlgoInfo2.clear();
        isOnSaveClick = false;
        isEditAndFirstTime = false;
    }

}


