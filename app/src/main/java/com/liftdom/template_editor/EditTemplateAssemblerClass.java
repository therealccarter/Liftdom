package com.liftdom.template_editor;
// Created: 9/20/2016


import java.util.ArrayList;
import java.util.ArrayList;
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

    // DoW ArrayLists!
    public ArrayList<ArrayList> MasterEditTemplateAL = new ArrayList<>();
    private ArrayList<String> DoWAL1 = new ArrayList<>();
    private ArrayList<String> DoWAL2 = new ArrayList<>();
    private ArrayList<String> DoWAL3 = new ArrayList<>();
    private ArrayList<String> DoWAL4 = new ArrayList<>();
    private ArrayList<String> DoWAL5 = new ArrayList<>();
    private ArrayList<String> DoWAL6 = new ArrayList<>();
    private ArrayList<String> DoWAL7 = new ArrayList<>();

    ArrayList<String> algorithmExercisesAL = new ArrayList<>();

    ArrayList<ArrayList> algorithmMasterList = new ArrayList<>();

    String[] algorithmDataList = new String[7];

    Boolean isAlgoLooper = true;

    Boolean isAlgoFirstTime = true;

    Boolean isApplyAlgo = false;

    Boolean isOnSaveClick = false;

    // DoW Setter
    void setDoW(String doWValue){
        if(doWValue != null){
            if(DoWAL1.isEmpty()){
                DoWAL1.add(0, doWValue);
            } else if(DoWAL2.isEmpty() && !DoWAL1.contains(doWValue)){
                DoWAL2.add(0, doWValue);
            } else if(DoWAL3.isEmpty() && !DoWAL1.contains(doWValue) && !DoWAL2.contains(doWValue)){
                DoWAL3.add(0, doWValue);
            } else if(DoWAL4.isEmpty() && !DoWAL1.contains(doWValue) && !DoWAL2.contains(doWValue) && !DoWAL3.contains
                    (doWValue)){
                DoWAL4.add(0, doWValue);
            } else if(DoWAL5.isEmpty() && !DoWAL1.contains(doWValue) && !DoWAL2.contains(doWValue)&& !DoWAL3.contains
                    (doWValue) && !DoWAL4.contains(doWValue)){
                DoWAL5.add(0, doWValue);
            } else if(DoWAL6.isEmpty() && !DoWAL1.contains(doWValue) && !DoWAL2.contains(doWValue)&& !DoWAL3.contains
                    (doWValue)&& !DoWAL4.contains(doWValue) && !DoWAL5.contains(doWValue)){
                DoWAL6.add(0, doWValue);
            } else if(DoWAL7.isEmpty() && !DoWAL1.contains(doWValue) && !DoWAL2.contains(doWValue)&& !DoWAL3.contains
                    (doWValue)&& !DoWAL4.contains(doWValue) && !DoWAL5.contains(doWValue) && !DoWAL6.contains
                    (doWValue)){
                DoWAL7.add(0, doWValue);
            }
        }
    }

    /**
     *  Add spinner values indiscriminately, and later, when adding setsXreps, search for a matching spinner value
     * and add onto that. Thinking of the way to make sure sXr is in order...
     */

    // Exercise spinner setters
    void setExerciseValue(String exerciseValue, String doWValue, Boolean isChecked){
        if(doWValue != null && !exerciseValue.equals("Click to add an exercise")){
            if(doWValue.equals(DoWAL1.get(0)) && !doesExNameExist1(exerciseValue)){
                DoWAL1.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL2.size() != 0) && doWValue.equals(DoWAL2.get(0)) && !doesExNameExist2(exerciseValue)){
                DoWAL2.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL3.size() != 0) && doWValue.equals(DoWAL3.get(0)) && !doesExNameExist3(exerciseValue)){
                DoWAL3.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL4.size() != 0) && doWValue.equals(DoWAL4.get(0)) && !doesExNameExist4(exerciseValue)){
                DoWAL4.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL5.size() != 0) && doWValue.equals(DoWAL5.get(0)) && !doesExNameExist5(exerciseValue)){
                DoWAL5.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL6.size() != 0) && doWValue.equals(DoWAL6.get(0)) && !doesExNameExist6(exerciseValue)){
                DoWAL6.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            } else if((DoWAL7.size() != 0) && doWValue.equals(DoWAL7.get(0)) && !doesExNameExist7(exerciseValue)){
                DoWAL7.add(exerciseValue);
                if(isChecked){
                    algorithmExercisesAL.add(exerciseValue);
                }
            }
        }
    }

    /**
     * What are we trying to do here?
     * We need to search an arraylist for a string that has ExName as its first value
     * We then need to get the index of that value and append a setSchemeValue onto it
     * @param setSchemeValue
     * @param exerciseValue
     * @param doWValue
     */

    // Set scheme setter
    void setSetSchemeValue(String setSchemeValue, String exerciseValue, String doWValue){
        if(doWValue != null){
            if(doWValue.equals(DoWAL1.get(0))){
                if(doesExNameExist1(exerciseValue)){
                    int spinnerIndex = getExNameIndex1(exerciseValue);
                    DoWAL1.set(spinnerIndex, DoWAL1.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL2.get(0))){
                if(doesExNameExist2(exerciseValue)){
                    int spinnerIndex = getExNameIndex2(exerciseValue);
                    DoWAL2.set(spinnerIndex, DoWAL2.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL3.get(0))){
                if(doesExNameExist3(exerciseValue)){
                    int spinnerIndex = getExNameIndex3(exerciseValue);
                    DoWAL3.set(spinnerIndex, DoWAL3.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL4.get(0))){
                if(doesExNameExist4(exerciseValue)){
                    int spinnerIndex = getExNameIndex4(exerciseValue);
                    DoWAL4.set(spinnerIndex, DoWAL4.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL5.get(0))){
                if(doesExNameExist5(exerciseValue)){
                    int spinnerIndex = getExNameIndex5(exerciseValue);
                    DoWAL5.set(spinnerIndex, DoWAL5.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL6.get(0))){
                if(doesExNameExist6(exerciseValue)){
                    int spinnerIndex = getExNameIndex6(exerciseValue);
                    DoWAL6.set(spinnerIndex, DoWAL6.get(spinnerIndex) + "," + setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL7.get(0))){
                if(doesExNameExist7(exerciseValue)){
                    int spinnerIndex = getExNameIndex7(exerciseValue);
                    DoWAL7.set(spinnerIndex, DoWAL7.get(spinnerIndex) + "," + setSchemeValue);
                }
            }
        }
    }

    int getExNameIndex1(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL1.size(); i++){
            String[] array = DoWAL1.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex2(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL2.size(); i++){
            String[] array = DoWAL2.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex3(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL3.size(); i++){
            String[] array = DoWAL3.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex4(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL4.size(); i++){
            String[] array = DoWAL4.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex5(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL5.size(); i++){
            String[] array = DoWAL5.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex6(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL6.size(); i++){
            String[] array = DoWAL6.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    int getExNameIndex7(String exerciseName){
        int index = -1;
        String delims = "[,]";
        for(int i = 0; i < DoWAL7.size(); i++){
            String[] array = DoWAL7.get(i).split(delims);
            if(array[0].equals(exerciseName)){
                index = i;
            }
        }
        return index;
    }

    Boolean doesExNameExist1(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL1.size(); i++){
            String[] array = DoWAL1.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist2(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL2.size(); i++){
            String[] array = DoWAL2.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist3(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL3.size(); i++){
            String[] array = DoWAL3.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist4(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL4.size(); i++){
            String[] array = DoWAL4.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist5(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL5.size(); i++){
            String[] array = DoWAL5.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist6(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL6.size(); i++){
            String[] array = DoWAL6.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }

    Boolean doesExNameExist7(String exName){
        Boolean doesExist = false;
        String delims = "[,]";
        for(int i = 0; i < DoWAL7.size(); i++){
            String[] array = DoWAL7.get(i).split(delims);
            if(array[0].equals(exName)){
                doesExist = true;
            }
        }
        return doesExist;
    }





    void assembleMasterList(){
        if(!DoWAL1.isEmpty()){
            MasterEditTemplateAL.add(DoWAL1);
        }
        if(!DoWAL2.isEmpty()){
            MasterEditTemplateAL.add(DoWAL2);
        }
        if(!DoWAL3.isEmpty()){
            MasterEditTemplateAL.add(DoWAL3);
        }
        if(!DoWAL4.isEmpty()){
            MasterEditTemplateAL.add(DoWAL4);
        }
        if(!DoWAL5.isEmpty()){
            MasterEditTemplateAL.add(DoWAL5);
        }
        if(!DoWAL6.isEmpty()){
            MasterEditTemplateAL.add(DoWAL6);
        }
        if(!DoWAL7.isEmpty()){
            MasterEditTemplateAL.add(DoWAL7);
        }
        if(!algorithmExercisesAL.isEmpty()){
            MasterEditTemplateAL.add(algorithmExercisesAL);
        }
    }

    void clearAllLists(){
        DoWAL1.clear();
        DoWAL2.clear();
        DoWAL3.clear();
        DoWAL4.clear();
        DoWAL5.clear();
        DoWAL6.clear();
        DoWAL7.clear();
        MasterEditTemplateAL.clear();
        algorithmExercisesAL.clear();
        algorithmMasterList.clear();

        for(String stringData : algorithmDataList){
            stringData = "";
        }
    }

}




