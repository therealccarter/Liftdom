package com.liftdom.charts_stats_tools.ex_history_chart;

import android.util.Log;
import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import com.liftdom.user_profile.UserModelClass;

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
        DatabaseReference historyRef = mRootRef.child("workoutHistory").child(uid);

        DatabaseReference userRef = mRootRef.child("user").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                            incrementor++;
                            Log.i("statChartsInfo", String.valueOf(incrementor));

                            //if(dataSnapshot1.getKey().equals("2017-06-12")){

                            WorkoutHistoryModelClass historyModelClass = dataSnapshot1.getValue(WorkoutHistoryModelClass.class);

                            if (historyModelClass.containsEx(exName)) {
                                if (isOverall) {
                                    ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                    valueAndDateObject.setDate(historyModelClass.getDate());
                                    valueAndDateObject.setValue(historyModelClass.getExPoundage(exName, userModelClass.isIsImperial(),
                                            userModelClass.getPounds(), userModelClass.getKgs()));

                                    SpecificExerciseValueList.add(valueAndDateObject);

                                    if (incrementor == dataSnapshot.getChildrenCount()) {
                                        if (!SpecificExerciseValueList.isEmpty()) {
                                            statChartsFrag.valueConverter(SpecificExerciseValueList, exName, true);
                                            Log.i("statChartsInfo", "completed!");
                                        }
                                    }
                                }else{
                                    ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                    valueAndDateObject.setDate(historyModelClass.getDate());
                                    valueAndDateObject.setValue(historyModelClass.getExMaxWeightLifted(exName, userModelClass.isIsImperial(),
                                            userModelClass.getPounds(), userModelClass.getKgs()));

                                    SpecificExerciseValueList.add(valueAndDateObject);

                                    Log.i("statChartsInfo", "incrementor = " + String.valueOf(incrementor) + ", " +
                                            "childrenCount = " + String.valueOf(dataSnapshot.getChildrenCount()));
                                    if (incrementor == dataSnapshot.getChildrenCount()) {
                                        if (!SpecificExerciseValueList.isEmpty()) {
                                            statChartsFrag.valueConverter(SpecificExerciseValueList, exName, false);
                                            Log.i("statChartsInfo", "completed! (not overall)");
                                        }
                                    }
                                }
                            }else{
                                if(isOverall){
                                    if (incrementor == dataSnapshot.getChildrenCount()) {
                                        if (!SpecificExerciseValueList.isEmpty()) {
                                            statChartsFrag.valueConverter(SpecificExerciseValueList, exName, true);
                                            Log.i("statChartsInfo", "completed!");
                                        }
                                    }
                                }else{
                                    Log.i("statChartsInfo", "incrementor = " + String.valueOf(incrementor) + ", " +
                                            "childrenCount = " + String.valueOf(dataSnapshot.getChildrenCount()));
                                    if (incrementor == dataSnapshot.getChildrenCount()) {
                                        if (!SpecificExerciseValueList.isEmpty()) {
                                            statChartsFrag.valueConverter(SpecificExerciseValueList, exName, false);
                                            Log.i("statChartsInfo", "completed! (not overall)");
                                        }
                                    }
                                }
                            }
                            //}
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}










