package com.liftdom.user_profile.stat_chart_stuff;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.ResourceObserver;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;


/**
 * Created by Brodin on 3/13/2017.
 */

public class SpecificExerciseChartClass {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    boolean isOfExName = false;

    // if true, we're looking for the overall load value. if false, we're looking for the max weight used.
    private boolean isOverall = true;

    private ArrayList<ValueAndDateObject> SpecificExerciseValueList = new ArrayList<>();


    public void getValueList(String exName, StatChartsTab statChartsTab){
        setSpecificExerciseValueList(exName, statChartsTab);
    }

    private void setSpecificExerciseValueList(final String exName, final StatChartsTab statChartsTab){

        final DatabaseReference historyRef = mRootRef.child("workout_history").child(uid);

        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Observable.fromIterable(dataSnapshot.getChildren())
                        .observeOn(Schedulers.computation())
                        .subscribeOn(AndroidSchedulers.mainThread())
                        .subscribe(new ResourceObserver<DataSnapshot>() {
                            @Override
                            public void onNext(DataSnapshot dataSnapshot1) {
                                final String key = dataSnapshot1.getKey();

                                DatabaseReference specificDateRef = historyRef.child(key);

                                specificDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        final ArrayList<String> exValueArrayList = new ArrayList<String>();

                                        Observable.fromIterable(dataSnapshot.getChildren())
                                                // We're going to do our calculations on the computation thread
                                                .observeOn(Schedulers.computation())
                                                // And our results will be published onto the Main Thread
                                                .subscribeOn(AndroidSchedulers.mainThread())
                                                // Finally we will subscribe to our list and watch for results
                                                .subscribe(new ResourceObserver<DataSnapshot>() {
                                                    @Override
                                                    public void onComplete() {
                                                        isOfExName = false;
                                                    }

                                                    @Override
                                                    public void onError(Throwable e) {

                                                    }

                                                    @Override
                                                    public void onNext(DataSnapshot dataSnapshot) {
                                                        String value = dataSnapshot.getValue(String.class);

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
                                                                // this returns a value of reps*weight. so for each valid
                                                                // date we'll get an overall value...
                                                                double exerciseValue = getExerciseValue(exValueArrayList);
                                                                ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                                                valueAndDateObject.setDate(key);
                                                                valueAndDateObject.setValue(exerciseValue);

                                                                SpecificExerciseValueList.add(valueAndDateObject);
                                                            }else{
                                                                // ...or the max weight lifted.
                                                                double maxExWeight = getMaxExerciseWeight(exValueArrayList);
                                                                ValueAndDateObject valueAndDateObject = new ValueAndDateObject();
                                                                valueAndDateObject.setDate(key);
                                                                valueAndDateObject.setValue(maxExWeight);

                                                                SpecificExerciseValueList.add(valueAndDateObject);
                                                            }
                                                        }
                                                    }
                                                });
                                    }



                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {
                                if(!SpecificExerciseValueList.isEmpty()){
                                    statChartsTab.setUpUI(SpecificExerciseValueList);
                                    Log.i("info", "completed!");
                                } else{
                                    Log.i("info", "empty");
                                }

                            }
                        });

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
