package com.liftdom.workout_assistor;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chris on 10/21/2016.
 */

public class ActiveTemplateListObject {
    public ArrayList<String> activeTemplateList = new ArrayList<>();

    public ActiveTemplateListObject(){

    }

    public ActiveTemplateListObject(ArrayList<String> activeTemplateList){
        this.activeTemplateList = activeTemplateList;
    }

    public ArrayList<String> getActiveTemplateList(){
        return activeTemplateList;
    }
}
