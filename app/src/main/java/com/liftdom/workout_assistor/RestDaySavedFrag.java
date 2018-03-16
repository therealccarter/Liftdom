package com.liftdom.workout_assistor;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.comment_post.PostCommentModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class RestDaySavedFrag extends Fragment {

    private boolean animationsFirstTime = true;

    private String completionStreak;
    private String streakMultiplier;
    private String xpFromWorkout;
    private String totalXpGained;
    private int currentXp;
    private String currentPowerLevel;
    private String oldPowerLevel;
    String publicDescription = null;
    String privateJournal = null;
    String mediaRef = null;
    CompletedWorkoutModelClass completedWorkoutModelClass;
    String redoRefKey;
    boolean isRevisedWorkout;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public RestDaySavedFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.goBackHome) Button goHomeButton;
    @BindView(R.id.finishedTextView) TextView finishedTextView;
    @BindView(R.id.powerLevelTextView) TextView powerLevelTextView;
    @BindView(R.id.currentPowerXpTextView) TextView powerLevelXpView1;
    @BindView(R.id.goalPowerXpTextView) TextView powerLevelXpView2;
    @BindView(R.id.xpGainedOverallView) TextView totalXpGainedView;
    @BindView(R.id.xpFromWorkoutView) TextView xpFromWorkoutView;
    @BindView(R.id.completionMultiplierView) TextView streakMultiplierView;
    @BindView(R.id.completionStreakView) TextView streakView;
    @BindView(R.id.totalXpGainedLL) LinearLayout totalXpGainedLL;
    @BindView(R.id.xpFromWorkoutLL) LinearLayout xpFromWorkoutLL;
    @BindView(R.id.streakMultiplierLL) LinearLayout streakMultiplierLL;
    @BindView(R.id.dailyStreakLL) LinearLayout dailyStreakLL;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.mainLinearLayout) LinearLayout mainLinearLayout;
    @BindView(R.id.dontLeavePage) TextView dontLeavePage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        ButterKnife.bind(this, view);

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideBadge(3);

        // get usermodel class

        finishedTextView.setText("REST DAY COMPLETED");

        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
        // it's not showing...

        //powerLevelXpView1.setText("0");

        totalXpGainedLL.setAlpha(0);
        xpFromWorkoutLL.setAlpha(0);
        streakMultiplierLL.setAlpha(0);
        dailyStreakLL.setAlpha(0);

        if(savedInstanceState == null){
            if(animationsFirstTime){
                final DatabaseReference userModelRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
                userModelRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                        powerLevelTextView.setText(userModelClass.getPowerLevel());
                        currentPowerLevel = userModelClass.getPowerLevel();
                        oldPowerLevel = userModelClass.getPowerLevel();

                        if(userModelClass.getCurrentXpWithinLevel() == null){
                            currentXp = 0;
                            powerLevelXpView1.setText("0");
                        }else{
                            currentXp = Integer.parseInt(userModelClass.getCurrentXpWithinLevel());
                            powerLevelXpView1.setText(userModelClass.getCurrentXpWithinLevel());
                        }

                        boolean isLevelUp = false;

                        powerLevelXpView2.setText(String.valueOf(generateGoalXp(currentPowerLevel)));


                        HashMap<String, String> xpInfoMap = userModelClass.generateXpMap(null);
                        // day v days
                        completionStreak = xpInfoMap.get("currentStreak");

                        streakMultiplier = xpInfoMap.get("streakMultiplier");
                        xpFromWorkout = xpInfoMap.get("xpFromWorkout");
                        totalXpGained = xpInfoMap.get("totalXpGained");

                        if((Integer.valueOf(totalXpGained) + currentXp) > Integer.valueOf(powerLevelXpView2.getText()
                                .toString())){
                            isLevelUp = true;
                        }

                        streakView.setText(completionStreak);
                        streakMultiplierView.setText(streakMultiplier);
                        xpFromWorkoutView.setText(xpFromWorkout);
                        totalXpGainedView.setText("0");

                        // feed fanout
                        DatabaseReference myFeedRef = mRootRef.child("feed").child(uid);
                        String refKey;

                        if(isRevisedWorkout){
                            refKey = redoRefKey;
                        }else{
                            refKey = myFeedRef.push().getKey();
                        }

                        String dateUTC = new DateTime(DateTimeZone.UTC).toString();
                        HashMap<String, List<String>> workoutInfoMap = new HashMap<>();
                        boolean isImperial1 = false;
                        if(userModelClass.isIsImperial()){
                            isImperial1 = true;
                        }

                        Map<String, PostCommentModelClass> commentModelClassMap = new HashMap<String, PostCommentModelClass>();

                        if(isRevisedWorkout){
                            setNewCompletedWorkoutModelClass();
                        }else{
                            completedWorkoutModelClass = new CompletedWorkoutModelClass(userModelClass.getUserId(),
                                    userModelClass.getUserName(), publicDescription, dateUTC, isImperial1, refKey, mediaRef,
                                    workoutInfoMap, commentModelClassMap, null);

                            if(isLevelUp){
                                List<String> bonusList = new ArrayList<>();
                                bonusList.add("Level up!");
                                completedWorkoutModelClass.setBonusList(bonusList);
                            }

                            List<String> bonusList = new ArrayList<>();
                            bonusList.add("Rest Day");
                            completedWorkoutModelClass.setBonusList(bonusList);

                            myFeedRef.child(refKey).setValue(completedWorkoutModelClass);
                            feedFanOut(refKey, completedWorkoutModelClass);
                        }

                        // workout history
                        String date = LocalDate.now().toString();
                        boolean isImperial = false;
                        if(userModelClass.isIsImperial()){
                            isImperial = true;
                        }
                        DatabaseReference workoutHistoryRef = FirebaseDatabase.getInstance().getReference().child
                                ("workoutHistory").child(uid).child(date);
                        WorkoutHistoryModelClass historyModelClass = new WorkoutHistoryModelClass(userModelClass.getUserId(),
                                userModelClass.getUserName(), publicDescription, privateJournal, date, mediaRef, null,
                                isImperial);
                        workoutHistoryRef.setValue(historyModelClass);

                        setRestDayModelClass(refKey);

                        dontLeavePage.setVisibility(View.GONE);

                        userModelRef.setValue(userModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                loadingView.setVisibility(View.GONE);
                                mainLinearLayout.setVisibility(View.VISIBLE);
                                fadeInViews();
                            }
                        });

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void setNewCompletedWorkoutModelClass(){
        final DatabaseReference selfFeedRef = FirebaseDatabase.getInstance().getReference()
                .child("selfFeed").child(uid).child(redoRefKey);
        selfFeedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                CompletedWorkoutModelClass workoutModelClass = dataSnapshot.getValue(CompletedWorkoutModelClass.class);
                workoutModelClass.setPublicDescription(publicDescription);
                //selfFeedRef.setValue(completedWorkoutModelClass);
                DatabaseReference myFeedRef = mRootRef.child("feed").child(uid);
                myFeedRef.child(redoRefKey).setValue(workoutModelClass);
                feedFanOut(redoRefKey, completedWorkoutModelClass);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        // starting to realize I need to possibly also update the workout history ref.
    }

    private void setRestDayModelClass(String refKey){
        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference()
                .child("runningAssistor").child(uid).child("assistorModel");

        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);

        RestDayModelClass restDayModelClass = new RestDayModelClass(true, dateTimeString, refKey, false);

        runningRef.setValue(restDayModelClass);
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    private void feedFanOut(final String refKey, final CompletedWorkoutModelClass completedWorkoutModelClass){

        DatabaseReference userListRef = mRootRef.child("followers").child(uid);
        userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map fanoutObject = new HashMap<>();
                    int inc = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                        fanoutObject.put("/feed/" + dataSnapshot1.getKey() + "/" + refKey, completedWorkoutModelClass);

                        inc++;

                        if(inc == dataSnapshot.getChildrenCount()){
                            fanoutObject.put("/selfFeed/" + uid + "/" + refKey, completedWorkoutModelClass);
                            DatabaseReference rootRef = mRootRef;
                            rootRef.updateChildren(fanoutObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    dontLeavePage.setVisibility(View.GONE);
                                }
                            });
                        }
                    }
                    //FollowersModelClass followersModelClass = dataSnapshot.getValue(FollowersModelClass.class);
//
                    //List<String> userList = new ArrayList<>();
//
                    //if(followersModelClass.getUserIdList() != null){
                    //    userList.addAll(followersModelClass.getUserIdList());
//
                    //    Map fanoutObject = new HashMap<>();
                    //    for(String user : userList){
                    //        fanoutObject.put("/feed/" + user + "/" + refKey, completedWorkoutModelClass);
                    //    }
//
                    //    DatabaseReference rootRef = mRootRef;
                    //    rootRef.updateChildren(fanoutObject).addOnCompleteListener(new OnCompleteListener<Void>() {
                    //        @Override
                    //        public void onComplete(@NonNull Task<Void> task) {
                    //            dontLeavePage.setVisibility(View.GONE);
                    //        }
                    //    });
                    //}
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    private void fadeInViews(){

        dailyStreakLL.animate().alpha(1).setDuration(1000).setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                //startCounterAnimation(0, 12, streakView);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        streakMultiplierLL.animate().alpha(1).setDuration(1300).start();
        xpFromWorkoutLL.animate().alpha(1).setDuration(1600).start();
        generateXpCalculator();
    }

    private int generateGoalXp(String powerLevel){
        int powerLevelInt = Integer.parseInt(powerLevel);

        double powerXP = (powerLevelInt * powerLevelInt) * 1.3;
        powerXP = powerXP * 100;
        return (int) Math.round(powerXP);
    }

    private int generateGoalXp(int powerLevel){
        double powerXP = (powerLevel * powerLevel) * 1.3;
        powerXP = powerXP * 100;
        return (int) Math.round(powerXP);
    }

    private void scaleXp1(TextView textView){
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.simple_scale_1);
        textView.startAnimation(animation);
    }

    private void generateXpCalculator(){

        int xpGained = Integer.parseInt(totalXpGained);
        int goalXp = generateGoalXp(currentPowerLevel);
        generateLevelUpAnimation(xpGained, currentXp);

    }

    /**
     * So, the current bug is that when you increase a power level, shit goes negative.
     */


    int initialCurrentXp = 0;

    private void generateLevelUpAnimation(final int totalXpGained, int currentXpWithinLevel){
        // increase the xp to the goal xp
        // increase power level
        // use the leftover xp to increase to new goal xp
        // increase power level
        // repeat

        final int initialGoalXp = generateGoalXp(currentPowerLevel);
        initialCurrentXp = currentXpWithinLevel;

        if(currentXpWithinLevel + totalXpGained >= initialGoalXp){

            final int initialPowerLevel = Integer.parseInt(currentPowerLevel);
            totalXpGainedLL.animate().alpha(1).setDuration(2000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    startCounterAnimation(0, totalXpGained, totalXpGainedView);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //scaleXp1(powerLevelXpView1);
                    startCounterAnimForLevelIncrease(initialCurrentXp, initialGoalXp, totalXpGained, initialPowerLevel);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });


        }else{
            final int newXpWithinLevel = currentXpWithinLevel + totalXpGained;
            totalXpGainedLL.animate().alpha(1).setDuration(2000).setListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    startCounterAnimation(0, totalXpGained, totalXpGainedView);
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    //scaleXp1(powerLevelXpView1);
                    startCounterAnimation(initialCurrentXp, newXpWithinLevel, powerLevelXpView1);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }

        //for(int i = 0; i < 50; i++){
        //    final int goalXp = generateGoalXp(currentPowerLevel);
        //    if(i == 0){
        //        if(currentXpWithinLevel + totalXpGained >= goalXp){
//
        //            currentPowerLevel = String.valueOf(Integer.parseInt(currentPowerLevel) + 1);
        //            int xpLeftInCurrent = goalXp - currentXpWithinLevel; // 1
        //            newCurrentXpWithinLevel = totalXpGained - xpLeftInCurrent;
//
        //            totalXpGainedLL.animate().alpha(1).setDuration(1200).setListener(new Animator.AnimatorListener() {
        //                @Override
        //                public void onAnimationStart(Animator animation) {
        //                    //powerLevelXpView1.setText(String.valueOf(initialCurrentXp));
        //                    startCounterAnimation(0, totalXpGained, totalXpGainedView);
        //                }
//
        //                @Override
        //                public void onAnimationEnd(Animator animation) {
        //                    //scaleXp1(powerLevelXpView1);
        //                    powerLevelInc++;
        //                    final String powerLevel = String.valueOf(Integer.parseInt(oldPowerLevel) + powerLevelInc);
        //                    final int xpWithinLevelSnapshot = newCurrentXpWithinLevel;
        //                    //startCounterAnimation(goalXp, goalXp, powerLevelXpView1);
//
        //                    //powerLevelTextView.setText(currentPowerLevel);
        //                    //startCounterAnimation(0, newCurrentXpWithinLevel , powerLevelXpView1, false, null, 0);
        //                    powerLevelXpView2.setText(String.valueOf(generateGoalXp(currentPowerLevel)));
//
        //                }
//
        //                @Override
        //                public void onAnimationCancel(Animator animation) {
//
        //                }
//
        //                @Override
        //                public void onAnimationRepeat(Animator animation) {
//
        //                }
        //            });
        //        }else{
        //            final int newXpWithinLevel = currentXpWithinLevel + totalXpGained;
        //            totalXpGainedLL.animate().alpha(1).setDuration(2000).setListener(new Animator.AnimatorListener() {
        //                @Override
        //                public void onAnimationStart(Animator animation) {
        //                    startCounterAnimation(0, totalXpGained, totalXpGainedView);
        //                }
//
        //                @Override
        //                public void onAnimationEnd(Animator animation) {
        //                    //scaleXp1(powerLevelXpView1);
        //                    startCounterAnimation(0, newXpWithinLevel, powerLevelXpView1);
        //                }
//
        //                @Override
        //                public void onAnimationCancel(Animator animation) {
//
        //                }
//
        //                @Override
        //                public void onAnimationRepeat(Animator animation) {
//
        //                }
        //            });
        //            break;
        //        }
        //    }else{
        //        if(newCurrentXpWithinLevel >= goalXp){
        //            currentPowerLevel = String.valueOf(Integer.parseInt(currentPowerLevel) + 1);
        //            int xpLeftInCurrent = goalXp - newCurrentXpWithinLevel; // 1
        //            newCurrentXpWithinLevel = totalXpGained - xpLeftInCurrent;
        //            powerLevelInc++;
        //            final String powerLevel = String.valueOf(Integer.parseInt(oldPowerLevel) + powerLevelInc);
        //            final int xpWithinLevelSnapshot = newCurrentXpWithinLevel;
        //            startCounterAnimation(0, goalXp, powerLevelXpView1);
        //            powerLevelXpView2.setText(String.valueOf(generateGoalXp(currentPowerLevel)));
        //            //startCounterAnimation(0, newCurrentXpWithinLevel, powerLevelXpView1, false, null);
        //        }else{
        //            startCounterAnimation(0, newCurrentXpWithinLevel, powerLevelXpView1);
        //            break;
        //        }
        //    }
        //}
    }

    int newXpGained = 0;
    int powerLevelInc = 0;
    int newCurrentXpWithinLevel = 0;
    private boolean isFirstKonfetti = true;

    private void startCounterAnimForLevelIncrease(int currentNumber1, int goalNumber1, int totalXpGained, final int initialPowerLevel){
        // purely for situation where we are EXCEEDING goal xp and leveling up


        int difference = goalNumber1 - currentNumber1;
        newXpGained = totalXpGained - difference;

        // from current xp to goal xp
        ValueAnimator animator1 = ValueAnimator.ofInt(currentNumber1, goalNumber1);
        animator1.setDuration(300);
        animator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                powerLevelXpView1.setText(animation.getAnimatedValue().toString());
            }
        });
        animator1.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                int newPowerLevel = initialPowerLevel + 1;
                int newGoalXp = generateGoalXp(newPowerLevel);
                powerLevelTextView.setText(String.valueOf(newPowerLevel));
                powerLevelXpView2.setText(String.valueOf(newGoalXp));
                konfetti();
                if(newXpGained >= newGoalXp){
                    for(int i = 0; i < 50; i++){
                        startCounterAnimForLevelIncrease(newXpGained, newGoalXp, newXpGained, newPowerLevel);
                    }
                }else{
                    startCounterAnimation(0, newXpGained, powerLevelXpView1);
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator1.start();
    }

    private void startCounterAnimation(int initialNumber, int finalNumber, final TextView textView){
        ValueAnimator animator = ValueAnimator.ofInt(initialNumber, finalNumber);
        animator.setDuration(2000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textView.setText(animation.getAnimatedValue().toString());
            }
        });
        animator.start();
    }

    private void konfetti(){
        KonfettiView konfettiView = (KonfettiView) getActivity().findViewById(R.id.viewKonfetti);
        if(isFirstKonfetti){
            konfettiView.build()
                    .addColors(Color.parseColor("#D1B91D"), Color.WHITE)
                    .setDirection(0.0, 359.0)
                    .setSpeed(1f, 3f)
                    .setFadeOutEnabled(true)
                    .setTimeToLive(2000L)
                    .addShapes(Shape.RECT)
                    .addSizes(new Size(12, 5f))
                    .setPosition(-50f, konfettiView.getWidth() + 50f, -50f, -50f)
                    .stream(300, 3000L);

            isFirstKonfetti = false;
        }
    }



}
