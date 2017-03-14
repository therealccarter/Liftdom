package com.liftdom.user_profile.stat_chart_stuff;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

import java.util.ArrayList;

/**
 * Created by Brodin on 3/13/2017.
 */

public class SpecificExerciseChartClass {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // if true, we're looking for the overall load value. if false, we're looking for the max weight used.
    private boolean isOverall = true;

    private ArrayList<ValueAndDateObject> SpecificExerciseValueList = new ArrayList<>();

    public SpecificExerciseChartClass (String exName){
        setSpecificExerciseValueList(exName);
    }

    public ArrayList<ValueAndDateObject> getSpecificExerciseValueList(){
        return SpecificExerciseValueList;
    }

    private void setSpecificExerciseValueList(final String exName){

        final DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    // Here, we're looking at each date (2017-02-04, 2017-02-01, etc)
                    final String key = dataSnapshot1.getKey();
                    // we'll need to have some sort of temporary array list to add to to get the exercise value...
                    DatabaseReference specificDateRef = historyRef.child(key);
                    specificDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            boolean isOfExName = false;
                            ArrayList<String> exValueArrayList = new ArrayList<String>();

                            for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){

                                String value = dataSnapshot2.getValue(String.class);

                                if(isExerciseName(value)){
                                    if(value.equals(exName)){
                                        isOfExName = true;
                                    }else {
                                        isOfExName = false;
                                    }
                                }

                                if(isOfExName){
                                    exValueArrayList.add(value);
                                }else if(!isOfExName && !exValueArrayList.isEmpty()){
                                    if(isOverall){
                                        double exerciseValue = getExerciseValue(exValueArrayList);
                                        ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                        valueAndDateObject.setDate(key);
                                        valueAndDateObject.setValue(exerciseValue);

                                        SpecificExerciseValueList.add(valueAndDateObject);
                                    }else{
                                        double maxExWeight = getMaxExerciseWeight(exValueArrayList);
                                        ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                        valueAndDateObject.setDate(key);
                                        valueAndDateObject.setValue(maxExWeight);

                                        SpecificExerciseValueList.add(valueAndDateObject);
                                    }
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
            String[] stringSplitArray = string.split(delims);

            double reps = Integer.parseInt(stringSplitArray[0]);
            double weight = Integer.parseInt(stringSplitArray[1]);

            double value = reps * weight;

            exerciseValue += value;
        }

        return exerciseValue;
    }

    private double getMaxExerciseWeight(ArrayList<String> exerciseStrings){
        double maxExWeight = 0;

        String delims = "[@]";

        for(String string : exerciseStrings){
            String[] stringSplitArray = string.split(delims);

            double weight = Integer.parseInt(stringSplitArray[1]);

            if(weight > maxExWeight){
                maxExWeight = weight;
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
