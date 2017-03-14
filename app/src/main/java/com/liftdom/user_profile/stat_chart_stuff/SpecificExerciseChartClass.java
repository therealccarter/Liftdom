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

    private ArrayList<ValueAndDateObject> SpecificExerciseValueList = new ArrayList<>();

    public ArrayList<ValueAndDateObject> getSpecificExerciseValueList(String exName){

        DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                    String key = dataSnapshot1.getKey();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return SpecificExerciseValueList;
    }

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
}
