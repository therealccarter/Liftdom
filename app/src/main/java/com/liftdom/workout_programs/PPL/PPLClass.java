package com.liftdom.workout_programs.PPL;

import com.liftdom.liftdom.utils.PlateRounderClass;

import java.util.ArrayList;

/**
 * Created by Brodin on 4/10/2017.
 */

public class PPLClass {

    String BenchMax;
    String DeadMax;
    String SquatMax;

    ArrayList<String> PushA;
    ArrayList<String> PullA;
    ArrayList<String> LegsA;
    ArrayList<String> PushB;
    ArrayList<String> PullB;
    ArrayList<String> LegsB;

    public PPLClass(String benchMax, String deadMax, String squatMax){
        BenchMax = benchMax;
        DeadMax = deadMax;
        SquatMax = squatMax;

        PushA = new ArrayList<>();
        PullA = new ArrayList<>();
        LegsA = new ArrayList<>();
        PushB = new ArrayList<>();
        PullB = new ArrayList<>();
        LegsB = new ArrayList<>();

        constructPush();
        constructPull();
        constructLegs();
    }

    private void constructPush(){

        //PUSH A ============================================================
        PushA.add("Bench Press (Barbell - Flat)");
        PushA.add("5 x 10 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .65)));

        PushA.add("Overhead Press (Dumbbell)");
        PushA.add("3 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .10)));

        PushA.add("Tricep Extension (Barbell - Overhead)");
        PushA.add("5 x 10 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .25)));

        PushA.add("Lateral Raise (Dumbbell)");
        PushA.add("3 x 20 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .05)));

        //PUSH B ============================================================
        PushB.add("Overhead Press (Barbell)");
        PushB.add("4 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .40)));

        PushB.add("Bench Press (Dumbbell - Incline)");
        PushB.add("3 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .20)));

        PushB.add("Tricep Extension (Machine)");
        PushB.add("5 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .25)));

        PushB.add("Lateral Raise (Dumbbell - Bent-over)");
        PushB.add("3 x 20 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .05)));
    }

    private void constructPull(){

        //PUSH A ============================================================
        PullA.add("Pull-up (Bodyweight)");
        PullA.add("3 x 12 @ " + " B.W.");

        PullA.add("Deadlift (Barbell - Conventional)");
        PullA.add("5 x 8 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(DeadMax), .65)));

        PullA.add("Row (Dumbbell - Bent-over)");
        PullA.add("5 x 8 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .30)));

        PullA.add("Curl (Dumbbell - Standing)");
        PullA.add("3 x 15 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .15)));

        //PUSH B ============================================================
        PullB.add("Pull-up (Bodyweight)");
        PullB.add("3 x 12 @ " + " B.W.");

        PullB.add("Row (Barbell - Bent-over)");
        PullB.add("5 x 10 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .50)));

        PullB.add("Deadlift (Barbell - Stiff-legged)");
        PullB.add("3 x 10 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(DeadMax), .35)));

        PullB.add("Curl (Barbell - Standing)");
        PullB.add("5 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(BenchMax), .25)));
    }

    private void constructLegs(){

        //PUSH A ============================================================
        LegsA.add("Squat (Barbell - Back)");
        LegsA.add("5 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .65)));

        LegsA.add("Leg Press");
        LegsA.add("3 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .75)));

        LegsA.add("Leg Extension");
        LegsA.add("4 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .30)));

        LegsA.add("Leg Curl");
        LegsA.add("3 x 15 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .15)));

        //PUSH B ============================================================
        LegsB.add("Squat (Barbell - Front)");
        LegsB.add("5 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .65)));

        LegsB.add("Leg Press");
        LegsB.add("3 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .75)));

        LegsB.add("Lunges (Barbell - Back)");
        LegsB.add("4 x 12 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .35)));

        LegsB.add("Calf-raise (Barbell - Back)");
        LegsB.add("5 x 15 @ " + " " + String.valueOf(getRoundedPercent(Integer.parseInt(SquatMax), .25)));
    }


    //TODO: Highlight the selected exercise within the ex selector
    //TODO: Make exercises in WA selectable for info from KC

    private int getRoundedPercent(int max, double percent){
        double percentedMax = max * percent;
        int newNum = (int) percentedMax;

        PlateRounderClass rounderClass = new PlateRounderClass(newNum);

        return rounderClass.getNewWeight();
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
