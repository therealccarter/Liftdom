package com.liftdom.workout_programs.PPL;

import java.util.ArrayList;

/**
 * Created by Brodin on 4/10/2017.
 */

public class PPLClass {

    ArrayList<String> PushA;
    ArrayList<String> PullA;
    ArrayList<String> LegsA;
    ArrayList<String> PushB;
    ArrayList<String> PullB;
    ArrayList<String> LegsB;

    public PPLClass(){
        PushA.add("Monday");
        PullA.add("Tuesday");
        LegsA.add("Wednesday");
        PushB.add("Thursday");
        PullB.add("Friday");
        LegsB.add("Saturday");
    }

    public ArrayList<String> getPushA(){
        return PushA;
    }
    public ArrayList<String> getPullA(){
        return PullA;
    }
    public ArrayList<String> getLegsA(){
        return LegsA;
    }
    public ArrayList<String> getPushB(){
        return PushB;
    }
    public ArrayList<String> getPullB(){
        return PullB;
    }
    public ArrayList<String> getLegsB(){
        return LegsB;
    }
}
