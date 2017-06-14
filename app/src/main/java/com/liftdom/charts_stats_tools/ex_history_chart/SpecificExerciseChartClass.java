package com.liftdom.charts_stats_tools.ex_history_chart;

import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;

import java.util.ArrayList;


/**
 * Created by Brodin on 3/13/2017.
 */

public class SpecificExerciseChartClass {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    boolean isOfExName = false;

    // if true, we're looking for the overall load value. if false, we're looking for the max weight used.
    public boolean isOverall = true;

    private ArrayList<ValueAndDateObject> SpecificExerciseValueList = new ArrayList<>();

    long incrementor = 0;
    String runningKey = "null";

    public void getValueList(String exName, StatChartsFrag statChartsFrag){
        setSpecificExerciseValueList(exName, statChartsFrag);
    }

    int innerInc;

    private void setSpecificExerciseValueList(final String exName, final StatChartsFrag statChartsFrag){
        DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                    incrementor++;

                    if(dataSnapshot1.getKey().equals("2017-06-12")){

                        WorkoutHistoryModelClass historyModelClass = dataSnapshot1.getValue(WorkoutHistoryModelClass.class);

                        if (historyModelClass.containsEx(exName)) {
                            if (isOverall) {
                                ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                valueAndDateObject.setDate(historyModelClass.getDate());
                                valueAndDateObject.setValue(historyModelClass.getExPoundage(exName));

                                SpecificExerciseValueList.add(valueAndDateObject);

                                if (incrementor == dataSnapshot.getChildrenCount()) {
                                    if (!SpecificExerciseValueList.isEmpty()) {
                                        statChartsFrag.valueConverter(SpecificExerciseValueList, exName, true);
                                        Log.i("info", "completed!");
                                    }
                                }
                            } else {
                                ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                valueAndDateObject.setDate(historyModelClass.getDate());
                                valueAndDateObject.setValue(historyModelClass.getExMaxWeightLifted(exName));

                                SpecificExerciseValueList.add(valueAndDateObject);

                                if (incrementor == dataSnapshot.getChildrenCount()) {
                                    if (!SpecificExerciseValueList.isEmpty()) {
                                        statChartsFrag.valueConverter(SpecificExerciseValueList, exName, false);
                                        Log.i("info", "completed!");
                                    }
                                }
                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setSpecificExerciseValueList2(final String exName, final StatChartsFrag statChartsFrag){

        final DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final long childrenCount = dataSnapshot.getChildrenCount();

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    // for each date
                    final String key1 = dataSnapshot1.getKey();

                    // So the problem is that this loop is fast and gets to 8 before the first inner loop is completed
                    // which means when we get to the first inner date, inc is 8 so just that one gets published.

                    // the problem is that the incrementor is only getting incremented when the date contains the ex.

                    DatabaseReference specificDateRef = historyRef.child(key1);

                    specificDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            final ArrayList<String> exValueArrayList = new ArrayList<String>();

                            if(!runningKey.equals(key1)){
                                runningKey = key1;
                            }

                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()) {

                                // for each child within specific date
                                String key = "date";

                                if(!dataSnapshot2.getKey().equals("private_journal") && !dataSnapshot2.getKey()
                                        .equals("restDay")){
                                     key = dataSnapshot2.getKey();
                                }

                                String value = dataSnapshot2.getValue(String.class);

                                if(dataSnapshot2.getKey().equals("private_journal")){
                                    incrementor++;
                                    Log.i("info", String.valueOf(incrementor));

                                    if (isOverall) {
                                        // this returns a value of reps*weight. so for each valid
                                        // date we'll get an overall value...
                                        double exerciseValue = getExerciseValue(exValueArrayList);

                                        if(exerciseValue != 0.0){
                                            ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                            valueAndDateObject.setDate(key1);
                                            valueAndDateObject.setValue(exerciseValue);

                                            Log.i("info", key1);
                                            Log.i("info", String.valueOf(exerciseValue));

                                            SpecificExerciseValueList.add(valueAndDateObject);
                                        }

                                        if (incrementor == childrenCount) {
                                            Log.i("info", "inc == childrenCount");
                                            if (!SpecificExerciseValueList.isEmpty()) {
                                                statChartsFrag.valueConverter(SpecificExerciseValueList, exName, true);
                                                Log.i("info", "completed!");
                                            } else {
                                                Log.i("info", "empty");
                                            }
                                        }
                                    } else {
                                        // ...or the max weight lifted.
                                        double maxExWeight = getMaxExerciseWeight(exValueArrayList);

                                        if(maxExWeight != 0.0) {
                                            ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                            valueAndDateObject.setDate(key1);
                                            valueAndDateObject.setValue(maxExWeight);
                                            SpecificExerciseValueList.add(valueAndDateObject);
                                        }

                                        if (incrementor == childrenCount) {
                                            if (!SpecificExerciseValueList.isEmpty()) {
                                                statChartsFrag.valueConverter(SpecificExerciseValueList, exName, false);
                                                Log.i("info", "completed!");
                                            } else {
                                                Log.i("info", "empty");
                                            }
                                        }
                                    }
                                }



                                    if (isExerciseName(value)) {
                                        if (value.equals(exName)) {
                                            isOfExName = true;
                                        } else {
                                            isOfExName = false;
                                        }
                                    }

                                    if (isOfExName) {
                                        exValueArrayList.add(value);
                                    }else if (!isOfExName && !exValueArrayList.isEmpty()) {


                                    }

                                }
                        }



                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // We'll have two "views" of the exercises: one is overall load (reps * weight), and the other is max weight used

    // this is the overall load value
    private double getExerciseValue(ArrayList<String> exerciseStrings){
        double exerciseValue = 0;

        String delims = "[@]";

        for(String string : exerciseStrings){
            if(!isExerciseName(string)){
                String[] stringSplitArray = string.split(delims);

                double reps = (double) Integer.parseInt(stringSplitArray[0]);
                double weight = (double) Integer.parseInt(stringSplitArray[1]);

                double value = reps * weight;

                exerciseValue += value;
            }
        }

        return exerciseValue;
    }

    private double getMaxExerciseWeight(ArrayList<String> exerciseStrings){
        double maxExWeight = 0;

        String delims = "[@]";

        for(String string : exerciseStrings){
            if(!isExerciseName(string)) {
                String[] stringSplitArray = string.split(delims);

                double weight = (double) Integer.parseInt(stringSplitArray[1]);

                if (weight > maxExWeight) {
                    maxExWeight = weight;
                }
            }
        }

        return maxExWeight;
    }


    private boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

}










