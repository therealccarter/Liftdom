package com.liftdom.user_profile.stat_chart_stuff;

import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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

                            ArrayList<String> exValueArrayList = new ArrayList<String>();

                            rx.Observable.fromIterable(dataSnapshot.getChildren())
                                    //First we will need to map our DataSnapshot to a String value..
                                    .map(new Function<DataSnapshot, String>() {
                                        @Override
                                        public String apply(@NonNull DataSnapshot dataSnapshot)
                                            throws Exception {
                                            return dataSnapshot.getKey();
                                        }
                                    })
                                    // We're going to do our calculations on the computation thread
                                    .observeOn(Schedulers.computation())
                                    // And our results will be published onto the Main Thread
                                    .subscribeOn(AndroidSchedulers.mainThread())
                                    // Once we have that set we will take our Strings and turn it into a list
                                    .toList()
                                    // Finally we will subscribe to our list and watch for results
                                    .subscribe(new SingleObserver<List<String>>() {
                                        @Override
                                        public void onSubscribe(Disposable d) {

                                        }

                                        @Override
                                        public void onSuccess(List<String> value) {
                                            System.out.println("String Size: " + value.size());
                                        }

                                        @Override
                                        public void onError(Throwable e) {

                                        }
                                    });
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

    /**
     *
     * @param value
     * @param exName
     * @param key
     * @param exValueArrayList
     */

    private void setArrayList(String value, String exName, String key, ArrayList<String> exValueArrayList){

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
