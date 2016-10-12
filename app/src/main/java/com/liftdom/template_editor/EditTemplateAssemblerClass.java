package com.liftdom.template_editor;
// Created: 9/20/2016


import java.util.LinkedList;
import java.util.LinkedList;
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

    // DoW LinkedLists!
    public LinkedList<LinkedList> MasterEditTemplateAL = new LinkedList<>();
    private LinkedList<String> DoWAL1 = new LinkedList<>();
    private LinkedList<String> DoWAL2 = new LinkedList<>();
    private LinkedList<String> DoWAL3 = new LinkedList<>();
    private LinkedList<String> DoWAL4 = new LinkedList<>();
    private LinkedList<String> DoWAL5 = new LinkedList<>();
    private LinkedList<String> DoWAL6 = new LinkedList<>();
    private LinkedList<String> DoWAL7 = new LinkedList<>();

    // DoW Setter
    void setDoW(String doWValue){
        if(doWValue != null){
            if(DoWAL1.isEmpty()){
                DoWAL1.add(0, doWValue);
            } else if(DoWAL2.isEmpty()){
                DoWAL2.add(0, doWValue);
            } else if(DoWAL3.isEmpty()){
                DoWAL3.add(0, doWValue);
            } else if(DoWAL4.isEmpty()){
                DoWAL4.add(0, doWValue);
            } else if(DoWAL5.isEmpty()){
                DoWAL5.add(0, doWValue);
            } else if(DoWAL6.isEmpty()){
                DoWAL6.add(0, doWValue);
            } else if(DoWAL7.isEmpty()){
                DoWAL7.add(0, doWValue);
            }
        }
    }

    /**
     *  Add spinner values indiscriminately, and later, when adding setsXreps, search for a matching spinner value
     * and add onto that. Thinking of the way to make sure sXr is in order...
     *
     *
     *
     */

    // Exercise spinner setters
    void setExSpinnerValue(String spinnerValue, String doWValue){
        if(doWValue != null){
            if(doWValue.equals(DoWAL1.get(0)) && !DoWAL1.contains(spinnerValue)){
                DoWAL1.add(spinnerValue);
            } else if(doWValue.equals(DoWAL2.get(0)) && !DoWAL2.contains(spinnerValue)){
                DoWAL2.add(spinnerValue);
            } else if(doWValue.equals(DoWAL3.get(0)) && !DoWAL3.contains(spinnerValue)){
                DoWAL3.add(spinnerValue);
            } else if(doWValue.equals(DoWAL4.get(0)) && !DoWAL4.contains(spinnerValue)){
                DoWAL4.add(spinnerValue);
            } else if(doWValue.equals(DoWAL5.get(0)) && !DoWAL5.contains(spinnerValue)){
                DoWAL5.add(spinnerValue);
            } else if(doWValue.equals(DoWAL6.get(0)) && !DoWAL6.contains(spinnerValue)){
                DoWAL6.add(spinnerValue);
            } else if(doWValue.equals(DoWAL7.get(0)) && !DoWAL7.contains(spinnerValue)){
                DoWAL7.add(spinnerValue);
            }
        }
    }

    // Set scheme setter
    void setSetSchemeValue(String setSchemeValue, String spinnerValue, String doWValue){
        if(doWValue != null){
            if(doWValue.equals(DoWAL1.get(0))){
                if(DoWAL1.contains(spinnerValue)){
                    int spinnerIndex = DoWAL1.indexOf(spinnerValue);
                    DoWAL1.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL2.get(0))){
                if(DoWAL2.contains(spinnerValue)){
                    int spinnerIndex = DoWAL2.indexOf(spinnerValue);
                    DoWAL2.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL3.get(0))){
                if(DoWAL3.contains(spinnerValue)){
                    int spinnerIndex = DoWAL3.indexOf(spinnerValue);
                    DoWAL3.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL4.get(0))){
                if(DoWAL4.contains(spinnerValue)){
                    int spinnerIndex = DoWAL4.indexOf(spinnerValue);
                    DoWAL4.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL5.get(0))){
                if(DoWAL5.contains(spinnerValue)){
                    int spinnerIndex = DoWAL5.indexOf(spinnerValue);
                    DoWAL5.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL6.get(0))){
                if(DoWAL6.contains(spinnerValue)){
                    int spinnerIndex = DoWAL6.indexOf(spinnerValue);
                    DoWAL6.add(spinnerIndex + 1, setSchemeValue);
                }
            } else if(doWValue.equals(DoWAL7.get(0))){
                if(DoWAL7.contains(spinnerValue)){
                    int spinnerIndex = DoWAL7.indexOf(spinnerValue);
                    DoWAL7.add(spinnerIndex + 1, setSchemeValue);
                }
            }
        }
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
    }

}






