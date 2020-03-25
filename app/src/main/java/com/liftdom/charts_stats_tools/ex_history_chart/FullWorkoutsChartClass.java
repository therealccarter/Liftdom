package com.liftdom.charts_stats_tools.ex_history_chart;

import androidx.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import com.liftdom.user_profile.UserModelClass;

import java.util.ArrayList;

/**
 * Created by Brodin on 3/25/2020.
 */
public class FullWorkoutsChartClass {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    long incrementor = 0;

    public boolean isOverall = true;

    private ArrayList<ValueAndDateObject> FullWorkoutsValueList = new ArrayList<>();

    public void getValueList(StatChartsFrag statChartsFrag){
        setFullWorkoutsValueList(statChartsFrag);
    }

    private void setFullWorkoutsValueList(StatChartsFrag statChartsFrag){
        DatabaseReference historyRef = mRootRef.child("workoutHistory").child(uid);

        DatabaseReference userRef = mRootRef.child("user").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                            incrementor++;

                            WorkoutHistoryModelClass historyModelClass = dataSnapshot1.getValue(WorkoutHistoryModelClass.class);

                            if(historyModelClass.containsWorkoutInfoMap()){
                                if(isOverall){
                                    ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                    valueAndDateObject.setDate(historyModelClass.getDate());
                                    valueAndDateObject.setValue(historyModelClass.getPoundage(userModelClass.isIsImperial(),
                                            userModelClass.getPounds(), userModelClass.getKgs()));

                                    FullWorkoutsValueList.add(valueAndDateObject);

                                    if(incrementor == dataSnapshot.getChildrenCount()){
                                        if(!FullWorkoutsValueList.isEmpty()){
                                            statChartsFrag.valueConverter(FullWorkoutsValueList,
                                                    "Workouts", true);
                                        }
                                    }
                                }else{
                                    ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                    valueAndDateObject.setDate(historyModelClass.getDate());
                                    valueAndDateObject.setValue(historyModelClass.getMaxWeightLifted(userModelClass.isIsImperial(),
                                            userModelClass.getPounds(), userModelClass.getKgs()));

                                    FullWorkoutsValueList.add(valueAndDateObject);

                                    if(incrementor == dataSnapshot.getChildrenCount()){
                                        if(!FullWorkoutsValueList.isEmpty()){
                                            statChartsFrag.valueConverter(FullWorkoutsValueList,
                                                    "Workouts", false);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
