package com.liftdom.workout_assistor;


import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
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
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import nl.dionsegijn.konfetti.KonfettiView;
import nl.dionsegijn.konfetti.models.Shape;
import nl.dionsegijn.konfetti.models.Size;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorSavedFrag extends android.app.Fragment {


    public AssistorSavedFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TemplateModelClass templateClass;
    CompletedExercisesModelClass exercisesModelClass;
    CompletedWorkoutModelClass completedWorkoutModelClass;
    UserModelClass userModelClass;
    String publicDescription = null;
    String privateJournal = null;
    String mediaRef = null; 
    HashMap<String, HashMap<String, List<String>>> completedMap;
    HashMap<String, List<String>> modelMapFormatted;
    HashMap<String, List<String>> completedMapFormatted;
    HashMap<String, List<String>> originalHashmap = new HashMap<>();
    List<String> completedExerciseList;
    String smolovWeekDayString;
    String redoRefKey;
    boolean isRevisedWorkout;
    boolean isFromRestDay;

    boolean isFirstTimeFirstTime;

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
    @BindView(R.id.loadingText) TextView loadingText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_saved, container, false);

        ButterKnife.bind(this, view);

        Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);

        finishedTextView.setText("WORKOUT COMPLETED");

        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.hideBadge(3);

        //powerLevelXpView1.setText("0");

        totalXpGainedLL.setAlpha(0);
        xpFromWorkoutLL.setAlpha(0);
        streakMultiplierLL.setAlpha(0);
        dailyStreakLL.setAlpha(0);

        goHomeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);
            }
        });

        if(savedInstanceState == null) {
            if (templateClass.getIsAlgorithm() && !isFromRestDay) {
                if (templateClass.getIsAlgoApplyToAll()) {
                    boolean applyAlgoWeightCheck = false;
                    for(Map.Entry<String, List<String>> allInfo : templateClass.getAlgorithmInfo().entrySet()){
                        if(allInfo.getKey().equals("0_key")){
                            if(allInfo.getValue().get(0).equals("all")){
                                if(Boolean.parseBoolean(allInfo.getValue().get(12))){
                                    applyAlgoWeightCheck = true;
                                }
                            }
                        }
                    }
                    DateTime dateTime = new DateTime();
                    int currentWeekday = dateTime.getDayOfWeek();
                    if (templateClass.getMapForDay(intToWeekday(currentWeekday)) != null) {
                        if (!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()) {
                            modelMapFormatted = formatModelClass(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                            originalHashmap.putAll(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                        }
                    }

                    completedMapFormatted = formatCompletedMap(completedMap);

                    HashMap<String, List<String>> completedMapForAlgo;

                    if(applyAlgoWeightCheck){
                        completedMapForAlgo = formatCompletedMap(completedMap);
                    }else{
                        completedMapForAlgo = formatCompletedMapNoWeightCheck(completedMap);
                    }

                    completedExerciseList = getCompletedExercises();

                    // init done

                    ArrayList<String> exercisesAlreadyGenerated = new ArrayList<>();

                    for (Map.Entry<String, List<String>> map1 : modelMapFormatted.entrySet()) {
                        // For each list in the model/expected maps
                        String exName = map1.getValue().get(0);
                        if(exName.equals("Overhead Press (Dumbbell)")){
                            Log.i("i", "i");
                        }
                        int totalPoundage = getTotalPoundage(modelMapFormatted, exName);
                        for (Map.Entry<String, List<String>> map2 : completedMapForAlgo.entrySet()) {
                            // For each list in the completed/actual maps
                            String delims = "[_]";
                            String[] tokens = map2.getValue().get(0).split(delims);
                            String splitExName = tokens[0];
                            completedExerciseList.add(splitExName);

                            String exNameCompleted = map2.getValue().get(0);

                            if (exName.equals(exNameCompleted)) {
                                // same ex names
                                if (tokens.length > 2 && tokens[1].equals("p")) {
                                    if(applyAlgoWeightCheck){
                                        // is parent superset ex
                                        int modelTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                                .length - 1], modelMapFormatted);
                                        int completedTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                                .length - 1], completedMapForAlgo);
                                        if (completedTotalPoundageSS >= modelTotalPoundageSS) {
                                            // superset completed, increase the algo
                                            generateAlgoForSupersetAll(splitExName, tokens[tokens.length - 1], map2.getValue()
                                                    .get(0));
                                        } else {
                                            // set to false
                                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                    ("0_key").get(0);
                                            String bool = "false";
                                            templateClass.setNewDateMapValues(todayString, exName, bool);
                                        }
                                    }else{
                                        generateAlgoForSupersetAll(splitExName, tokens[tokens.length - 1], map2.getValue()
                                                .get(0));
                                    }
                                } else if (tokens.length < 3) {
                                    if(applyAlgoWeightCheck){
                                        int totalPoundage2 = getTotalPoundage(completedMapForAlgo, exName);
                                        if(exName.equals("Overhead Press (Dumbbell)")){
                                            Log.i("i", "i");
                                        }
                                        if (!exercisesAlreadyGenerated.contains(exName)) {
                                            if (totalPoundage2 >= totalPoundage) {
                                                // algo
                                                generateAlgoAll(exName, false);
                                                exercisesAlreadyGenerated.add(exName);
                                            } else {
                                                // set to false
                                                String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                        ("0_key")
                                                        .get(0);
                                                String bool = "false";
                                                templateClass.setNewDateMapValues(todayString, exName, bool);
                                            }
                                        }
                                    }else{
                                        if(!exercisesAlreadyGenerated.contains(exName)){
                                            generateAlgoAll(exName, false);
                                            exercisesAlreadyGenerated.add(exName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    // not algo all
                    DateTime dateTime = new DateTime();
                    int currentWeekday = dateTime.getDayOfWeek();
                    if (templateClass.getMapForDay(intToWeekday(currentWeekday)) != null) {
                        if (!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()) {
                            modelMapFormatted = formatModelClass(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                            originalHashmap.putAll(templateClass.getMapForDay(intToWeekday(currentWeekday)));
                        }
                    }

                    completedMapFormatted = formatCompletedMap(completedMap);
                    // completedMapFormatted = formatCompletedMap(completedMap);
                    completedExerciseList = getCompletedExercises();

                    // init done

                    ArrayList<String> exercisesAlreadyGenerated = new ArrayList<>();

                    for (Map.Entry<String, List<String>> map1 : modelMapFormatted.entrySet()) {
                        // For each list in the model/expected maps
                        String exName = map1.getValue().get(0);
                        int totalPoundage = getTotalPoundage(modelMapFormatted, exName);

                        boolean applyAlgoWeightCheck = false;

                        for(Map.Entry<String, List<String>> algoEntry : templateClass.getAlgorithmInfo().entrySet()){
                            if(algoEntry.getValue().get(0).equals(exName)){
                                applyAlgoWeightCheck = Boolean.parseBoolean(algoEntry.getValue().get(12));
                            }
                        }

                        HashMap<String, List<String>> completedMapForAlgo;

                        if(applyAlgoWeightCheck){
                            completedMapForAlgo = formatCompletedMap(completedMap);
                        }else{
                            completedMapForAlgo = formatCompletedMapNoWeightCheck(completedMap);
                        }

                        for (Map.Entry<String, List<String>> map2 : completedMapForAlgo.entrySet()) {
                            // For each list in the completed/actual maps

                            String delims = "[_]";
                            String[] tokens = map2.getValue().get(0).split(delims);
                            String splitExName = tokens[0];

                            String exNameCompleted = map2.getValue().get(0);

                            if (exName.equals(exNameCompleted)) {
                                // same ex names
                                if (tokens.length > 2 && tokens[1].equals("p")) {
                                    if(applyAlgoWeightCheck){
                                        // is parent superset ex
                                        int modelTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                                .length - 1], modelMapFormatted);
                                        int completedTotalPoundageSS = getPoundageForModelSuperset(splitExName, tokens[tokens
                                                .length - 1], completedMapForAlgo);
                                        if (completedTotalPoundageSS >= modelTotalPoundageSS) {
                                            // superset completed, increase the algo
                                            generateAlgoForSuperset(splitExName, tokens[tokens.length - 1], map2.getValue()
                                                    .get(0));
                                        } else {
                                            // set to false
                                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                    ("0_key").get(0);
                                            String bool = "false";
                                            templateClass.setNewDateMapValues(todayString, exName, bool);
                                        }
                                    }else{
                                        // superset completed, increase the algo
                                        generateAlgoForSuperset(splitExName, tokens[tokens.length - 1], map2.getValue()
                                                .get(0));
                                    }
                                } else if (tokens.length < 3) {
                                    if(applyAlgoWeightCheck){
                                        int totalPoundage2 = getTotalPoundage(completedMapForAlgo, exName);
                                        if (!exercisesAlreadyGenerated.contains(exName)) {
                                            if (totalPoundage2 >= totalPoundage) {
                                                // algo
                                                generateAlgo(exName, false);
                                                exercisesAlreadyGenerated.add(exName);
                                            } else {
                                                // set to false
                                                String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get
                                                        ("0_key").get(0);
                                                String bool = "false";
                                                templateClass.setNewDateMapValues(todayString, exName, bool);
                                            }
                                        }
                                    }else{
                                        if (!exercisesAlreadyGenerated.contains(exName)) {
                                            generateAlgo(exName, false);
                                            exercisesAlreadyGenerated.add(exName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            } else {
                completedMapFormatted = formatCompletedMap(completedMap);
                completedExerciseList = getCompletedExercises();
            }

            DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateClass.getTemplateName());
            final DatabaseReference workoutHistoryRef = mRootRef.child("workoutHistory").child(uid).child(LocalDate.now()
                    .toString());
            final DatabaseReference completedExercisesRef = mRootRef.child("completedExercises").child(uid);
            final DatabaseReference userRef = mRootRef.child("user").child(uid);

            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    userModelClass = dataSnapshot.getValue(UserModelClass.class);

                    String mediaRef = null; // TODO: add media ref

                    String date = LocalDate.now().toString();
                    String dateUTC = new DateTime(DateTimeZone.UTC).toString();
                    HashMap<String, List<String>> workoutInfoMap = getMapForHistory(completedMap);
                    HashMap<String, List<String>> workoutInfoMapProcessed = processWorkoutInfoMap(workoutInfoMap);
                    boolean isImperial = false;
                    if (userModelClass.isIsImperial()) {
                        isImperial = true;
                    }

                    processUserClassPowerLevel(userModelClass);

                    DatabaseReference followerRef = mRootRef.child("followers").child(uid);

                    // posting to main feed
                    DatabaseReference myFeedRef = mRootRef.child("feed").child(uid);
                    //DatabaseReference selfFeedRef = mRootRef.child("selfFeed").child(uid);

                    String refKey;
                    if(redoRefKey != null){
                        refKey = redoRefKey;
                    }else{
                        refKey = myFeedRef.push().getKey();
                    }

                    final String REFKEY = refKey;

                    Map<String, PostCommentModelClass> commentModelClassMap = new HashMap<String, PostCommentModelClass>();

                    List<String> bonusList = new ArrayList<>();

                    if (isFirstTimeFirstTime) {
                        bonusList.add(userModelClass.getUserName() + "'s first post!");
                    }

                    if (isLevelUp) {
                        bonusList.add("Level up!");
                    }

                    if (smolovWeekDayString != null) {
                        bonusList.add(smolovWeekDayString);
                    }

                    completedWorkoutModelClass = new CompletedWorkoutModelClass(userModelClass.getUserId(),
                            userModelClass.getUserName(), publicDescription, dateUTC, isImperial, refKey, mediaRef,
                            workoutInfoMapProcessed, commentModelClassMap, null, bonusList);

                    myFeedRef.child(refKey).setValue(completedWorkoutModelClass);
                    //selfFeedRef.child(refKey).setValue(completedWorkoutModelClass);
                    feedFanOut(refKey, completedWorkoutModelClass);

                    //runningRef.child("refKey").setValue(refKey);
                    //runningRef.child("isRevise").setValue(false);
                    //runningRef.child("completedBool").setValue(true);

                    dontLeavePage.setVisibility(View.GONE);

                    // workout history
                    WorkoutHistoryModelClass historyModelClass = new WorkoutHistoryModelClass(userModelClass.getUserId(),
                            userModelClass.getUserName(), publicDescription, privateJournal, date, mediaRef,
                            workoutInfoMapProcessed, isImperial);
                    if (!isFirstTimeFirstTime) {
                        workoutHistoryRef.setValue(historyModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
                                if (manager.getRunningServices(Integer.MAX_VALUE) != null) {
                                    for (ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)) {
                                        if (AssistorServiceClass.class.getName().equals(serviceInfo.service
                                                .getClassName())) {
                                            Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);
                                            getActivity().stopService(stopIntent);

                                            DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child
                                                    ("runningAssistor").child(uid).child("assistorModel");

                                            Map runningMa = new HashMap<>();

                                            runningMa.put("/refKey", REFKEY);
                                            runningMa.put("/isRevise", false);
                                            runningMa.put("/completedBool", true);

                                            runningRef.updateChildren(runningMa);
                                        }
                                    }
                                }
                            }
                        });
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            completedExercisesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(!completedExerciseList.isEmpty()){
                        if (!completedExerciseList.get(0).equals("Example Exercise")) {
                            if(dataSnapshot.exists()){
                                exercisesModelClass = dataSnapshot.getValue(CompletedExercisesModelClass.class);
                                exercisesModelClass.addItems(completedExerciseList);
                                completedExercisesRef.setValue(exercisesModelClass);
                            }else{
                                exercisesModelClass = new CompletedExercisesModelClass();
                                exercisesModelClass.addItems(completedExerciseList);
                                completedExercisesRef.setValue(exercisesModelClass);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            templateRef.setValue(templateClass);

        }

        return view;
    }

    HashMap<String, List<String>> processWorkoutInfoMap(HashMap<String, List<String>> infoMap){
        HashMap<String, List<String>> processedMap = new HashMap<>();

        //List<List<String>>

        /**
         * So what do we need to do? For each key, we will check to see if the next value is the same as the one
         * we're currently on. Let's break it down some more.
         * Ex:
         *      3@5
         *      3@5
         *      3@5
         *      4@8
         *      2@7
         *      2@7
         *      3@5
         *
         * If the value we get is not identical to its neighbor
         */

        for(int i = 0; i < infoMap.size(); i++){
            List<String> processedList = new ArrayList<>();
            for(Map.Entry<String, List<String>> mapEntry : infoMap.entrySet()){
                String[] keyTokens = mapEntry.getKey().split("_");
                boolean hasGoneThrough = false;
                if(Integer.parseInt(keyTokens[0]) == i){
                    List<String> list = new ArrayList<>();
                    list.addAll(mapEntry.getValue());
                    boolean isFirstEx = true;
                    boolean isFirstRepsWeight = true;
                    String runningValue = "";
                    boolean isFirstSetScheme = true;
                    for(String string : list){
                        if(isExerciseName(string) && isFirstEx){
                            // is first ex name
                            //mWorkoutInfoList.add(string);
                            processedList.add(string);
                            isFirstEx = false;
                            hasGoneThrough = true;
                        }else if(isExerciseName(string) && !isFirstEx){
                            // is superset ex name
                            //mWorkoutInfoList.add(string + "_ss");
                            processedList.add(string);
                            runningValue = "";
                            isFirstRepsWeight = false;
                            isFirstSetScheme = true;
                        }else if(!isExerciseName(string) && isFirstRepsWeight){
                            // is first ex set scheme
                            //mWorkoutInfoList.add(string);
                            if(isFirstSetScheme){
                                runningValue = string;
                                String newValue = "1x" + string;
                                processedList.add(newValue);
                                isFirstSetScheme = false;
                            }else{
                                if(string.equals(runningValue)){
                                    String processedValue = processedList.get(processedList.size() - 1);
                                    String delims = "[x]";
                                    String[] tokens = processedValue.split(delims);
                                    int sets = Integer.valueOf(tokens[0]);
                                    sets++;
                                    String newValue = sets + "x" + string;
                                    processedList.set(processedList.size() - 1, newValue);
                                    runningValue = string;
                                }else{
                                    String newValue = "1x" + string;
                                    processedList.add(newValue);
                                    runningValue = string;
                                }
                            }
                        }else if(!isExerciseName(string) && !isFirstRepsWeight){
                            // is superset ex set scheme
                            //mWorkoutInfoList.add(string );
                            if(isFirstSetScheme){
                                runningValue = string;
                                String newValue = "1x" + string;
                                processedList.add(newValue);
                                isFirstSetScheme = false;
                            }else{
                                if(string.equals(runningValue)){
                                    String processedValue = processedList.get(processedList.size() - 1);
                                    String delims = "[x]";
                                    String[] tokens = processedValue.split(delims);
                                    int sets = Integer.valueOf(tokens[0]);
                                    sets++;
                                    String newValue = sets + "x" + string;
                                    processedList.set(processedList.size() - 1, newValue);
                                    runningValue = string;
                                }else{
                                    String newValue = "1x" + string;
                                    processedList.add(newValue);
                                    runningValue = string;
                                }
                            }
                        }
                    }
                }
                if(hasGoneThrough){
                    List<String> newList = new ArrayList<>();
                    newList.addAll(processedList);
                    processedMap.put(mapEntry.getKey(), newList);
                    processedList.clear();
                }
            }
        }
        return processedMap;
    }

    private void beginProcesses(){

    }

    @Override
    public void onStart(){
        super.onStart();
        //final DatabaseReference firstTimeRef = mRootRef.child("firstTime").child(uid).child
        //        ("isTemplateMenuSecondTime");
        //firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        if(dataSnapshot.exists()){
        //            FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
        //                    .title("Great job! \n \n" +
        //                            "Here's where you'll receive Power Level XP and level up. " +
        //                            "\n \n Your XP gain is based off of the workout you did, as well as " +
        //                            "how many consecutive days you've checked off your workouts/rest days.")
        //                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
        //                    .build();
//
        //            FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
        //                    .title("Not only will your Completion Streak (consecutive days) add a multiplier " +
        //                            "to the XP you gain, but your Completion Streak can also be used to unlock " +
        //                            "rewards and premium features." +
        //                            "\n \n Remember, check off rest days too to keep the Streak up!" +
        //                            "\n \n Good luck!")
        //                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER)
        //                    .build();
//
        //            new FancyShowCaseQueue()
        //                    .add(fancyShowCaseView1)
        //                    .add(fancyShowCaseView2)
        //                    .show();
//
        //            //DatabaseReference firstTimeActiveRef = mRootRef.child("user").child(uid).child
        // ("activeTemplate");
        //            firstTimeRef.setValue(null);
        //            //firstTimeActiveRef.setValue(null);
        //        }else{
        //            //Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
        //        }
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});
    }

    @Override
    public void onResume(){
        if(templateClass == null){
            Log.i("deadInfo", "templateClass is null (onResume/AssistorSavedFrag)");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("fragID",  0);
            startActivity(intent);
            super.onResume();
        }else{
            Log.i("deadInfo", "AssistorSavedFrag (onResume)");
            super.onResume();
        }
    }

    private boolean animationsFirstTime = true;
    private boolean isFirstKonfetti = true;

    private String completionStreak;
    private String streakMultiplier;
    private String xpFromWorkout;
    private String totalXpGained;
    private int currentXp;
    private String currentPowerLevel;
    private boolean isLevelUp;

    public boolean isFromAd;

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
                            fanoutObject.put("/globalFeed/" + refKey, completedWorkoutModelClass);
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

    private void processUserClassPowerLevel(UserModelClass userModelClass){
        if(animationsFirstTime){
            final DatabaseReference userModelRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);

            powerLevelTextView.setText(userModelClass.getPowerLevel());
            currentPowerLevel = userModelClass.getPowerLevel();

            if(userModelClass.getCurrentXpWithinLevel() == null){
                currentXp = 0;
                powerLevelXpView1.setText("0");
            }else{
                currentXp = Integer.parseInt(userModelClass.getCurrentXpWithinLevel());
                powerLevelXpView1.setText(userModelClass.getCurrentXpWithinLevel());
            }

            powerLevelXpView2.setText(String.valueOf(generateGoalXp(currentPowerLevel)));

            HashMap<String, String> xpInfoMap = userModelClass.generateXpMap(completedMapFormatted, isRevisedWorkout);
            // day v days
            completionStreak = xpInfoMap.get("currentStreak");

            streakMultiplier = xpInfoMap.get("streakMultiplier");
            xpFromWorkout = xpInfoMap.get("xpFromWorkout");
            totalXpGained = xpInfoMap.get("totalXpGained");

            streakView.setText(completionStreak);
            streakMultiplierView.setText(streakMultiplier);
            xpFromWorkoutView.setText(xpFromWorkout);
            totalXpGainedView.setText("0");

            if(!currentPowerLevel.equals(userModelClass.getPowerLevel())){
                isLevelUp = true;
            }

            userModelRef.setValue(userModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    loadingView.setVisibility(View.GONE);
                    loadingText.setVisibility(View.GONE);
                    mainLinearLayout.setVisibility(View.VISIBLE);
                    if(isFromAd){
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                fadeInViews();
                            }
                        }, 5000);
                    }else{
                        fadeInViews();
                    }
                }
            });
        }
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
        Animation animation = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_scale_1);
        textView.startAnimation(animation);
    }

    private void generateXpCalculator(){
        int xpGained = Integer.parseInt(totalXpGained);
        generateLevelUpAnimation(xpGained, currentXp);
    }

    int powerLevelInc = 0;

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
    }

    int newXpGained = 0;

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

    private List<String> getCompletedExercises(){
        List<String> completedExList = new ArrayList<>();

        for(Map.Entry<String, HashMap<String, List<String>>> mapEntry1 : completedMap.entrySet()){
            for(Map.Entry<String, List<String>> mapEntry2 : mapEntry1.getValue().entrySet()){
                List<String> newList = new ArrayList<>();
                newList.addAll(mapEntry2.getValue());
                for(String string : newList){
                    if(isExerciseName(string)){
                        completedExList.add(string);
                    }
                }
            }
        }

        return completedExList;
    }

    private HashMap<String, List<String>> getMapForHistory(HashMap<String, HashMap<String, List<String>>> completedMap){
        HashMap<String, List<String>> historyMap = new HashMap<>();
        for(int i = 0; i < completedMap.size(); i++){
            for(Map.Entry<String, HashMap<String, List<String>>> mapEntry1 : completedMap.entrySet()){
                List<String> exerciseList = new ArrayList<>();
                String[] keyTokens = mapEntry1.getKey().split("_");
                int keyInc = Integer.parseInt(keyTokens[0]);
                if(keyInc == i + 1){
                    for(int j = 0; j < mapEntry1.getValue().size(); j++){
                        for(Map.Entry<String, List<String>> mapEntry2 : mapEntry1.getValue().entrySet()){
                            String[] keyTokens2 = mapEntry2.getKey().split("_");
                            int keyInc2 = Integer.parseInt(keyTokens2[0]);
                            if(keyInc2 == j){
                                for(String string : mapEntry2.getValue()){
                                    String[] exTokens = string.split("_");
                                    if(isExerciseName(string)){
                                        exerciseList.add(string);
                                    }else{
                                        if(isChecked(string)){
                                            exerciseList.add(exTokens[0]);
                                            // TODO: unit here. Units everywhere that we see "@"
                                        }
                                    }
                                }
                            }
                        }
                    }
                    historyMap.put(historyMap.size() + "_key", exerciseList);
                }
            }
        }

        return historyMap;
    }

    private boolean isChecked(String string){
        boolean bool = false;

        String[] tokens = string.split("_");

        if(tokens[1].equals("checked")){
            bool = true;
        }

        return bool;
    }

    private int getPoundageForModelSuperset(String exName, String tag, HashMap<String, List<String>> map){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
            String delims = "[_]";
            String[] tokens = mapEntry.getValue().get(0).split(delims);
            if(tokens.length > 2){
                if(tokens[1].equals("p")){
                    // parent ex
                    if(tokens[0].equals(exName)){
                        if(tokens[tokens.length - 1].equals(tag)){
                            // correct parent ex
                            for(String string : mapEntry.getValue()){
                                if(!isExerciseName(string)){
                                    String delims2 = "[@]";
                                    String[] tokens2 = string.split(delims2);
                                    int reps;
                                    try {
                                        reps =Integer.parseInt(tokens2[0]);
                                    } catch (NumberFormatException e){
                                        reps = 1;
                                    }
                                    int weight;
                                    try{
                                        weight = Integer.parseInt(tokens[1]);
                                    }catch (NumberFormatException e){
                                        weight = 1;
                                    }

                                    //if(tokens[1].equals("B.W.")){
                                    //    if(userModelClass.isIsImperial()){
                                    //        int2 = Integer.parseInt(userModelClass.getPounds());
                                    //    }else{
                                    //        int2 = Integer.parseInt(userModelClass.getKgs());
                                    //    }
                                    //}else{
                                    //    int2 = Integer.parseInt(tokens[1]);
                                    //}
                                    int poundage = reps * weight;
                                    totalPoundage = totalPoundage + poundage;
                                }
                            }
                        }
                    }
                }else{
                    // child ex
                    if(tokens[1].equals(exName)){
                        if(tokens[tokens.length - 1].equals(tag)){
                            // correct child ex
                            for(String string : mapEntry.getValue()){
                                if(!isExerciseName(string)){
                                    String delims2 = "[@]";
                                    String[] tokens2 = string.split(delims2);
                                    int reps;
                                    try {
                                        reps =Integer.parseInt(tokens2[0]);
                                    } catch (NumberFormatException e){
                                        reps = 1;
                                    }
                                    int weight;
                                    try{
                                        weight = Integer.parseInt(tokens[1]);
                                    }catch (NumberFormatException e){
                                        weight = 1;
                                    }

                                    //if(tokens[1].equals("B.W.")){
                                    //    if(userModelClass.isIsImperial()){
                                    //        int2 = Integer.parseInt(userModelClass.getPounds());
                                    //    }else{
                                    //        int2 = Integer.parseInt(userModelClass.getKgs());
                                    //    }
                                    //}else{
                                    //    int2 = Integer.parseInt(tokens[1]);
                                    //}
                                    int poundage = reps * weight;
                                    totalPoundage = totalPoundage + poundage;
                                }
                            }
                        }
                    }
                }
            }
        }

        return totalPoundage;
    }

    private boolean isSupersetList(List<String> list){
        boolean isSS = false;

        int i = 0;
        for(String string : list){
            if(i != 0){
                if(isExerciseName(string)){
                    isSS = true;
                }
            }
            i++;
        }

        return isSS;
    }

    public boolean isPercentage(String setScheme){
        boolean percentage = false;

        String delims1 = "[@]";
        String[] tokens1 = setScheme.split(delims1);

        char c = tokens1[1].charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            percentage = true;
        }


        return percentage;
    }

    private void generateAlgoForSupersetAll(String exName, String tag, String exNameUnformatted){

        HashMap<String, List<String>> hashMapCopy = new HashMap<>();
        hashMapCopy.putAll(originalHashmap);

        String delims1 = "[_]";
        String[] exNameTokens = exNameUnformatted.split(delims1);
        String exNameFormatted = "";
        for(int i = 0; i < exNameTokens.length - 1; i++){
            exNameFormatted = exNameFormatted + exNameTokens[i] + "_";
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"

        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else{
            for(Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if(isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                        if(algoDateMap.getValue().size() > 4){
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }else{
                            // check with today
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }
                    }
                }
            }
        }



        boolean firstLoopBool = true;

        LocalDate newDate = LocalDate.now();
        for(Map.Entry<String, List<String>> algoInfoMap : templateClass.getAlgorithmInfo().entrySet()){
            if(algoInfoMap.getValue().get(0).equals("all")){
                // so now we're in the correct algorithm map
                List<String> newValueList = new ArrayList<>();
                boolean updateOldDate = false;
                boolean instantiateList = false;
                for(Map.Entry<String, List<String>> map : hashMapCopy.entrySet()){
                    String key = tag + "_key";
                    if(map.getKey().equals(key)){
                        // correct entry in original hashmap
                        List<String> valueList = new ArrayList<>();
                        valueList.addAll(map.getValue());
                        for(String string : valueList){
                            if(!isExerciseName(string)){
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if(templateClass.getAlgorithmDateMap() != null){
                                    boolean hasEx = false;
                                    for(Map.Entry<String, List<String>> algoDateMap : templateClass
                                            .getAlgorithmDateMap().entrySet()){
                                        try {
                                            if (isToday(algoDateMap.getValue().get(3))) {
                                                if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                                                    if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                        // compare, keep everything the same, set to true.
                                                        hasEx = true;
                                                        if(isRunning){
                                                            int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                            int sets = 0;
                                                            int reps = 0;
                                                            int weight = 0;
                                                            String weightPercentString = "";

                                                            boolean isNonIntWeight = false;
                                                            boolean isNonIntRep = false;
                                                            boolean isPercentWeight = false;

                                                            if(isExerciseName(tokens[2])){
                                                                isNonIntWeight = true;
                                                            }

                                                            if(tokens[1].equals("T.F.")){
                                                                isNonIntRep = true;
                                                            }

                                                            if(isPercentage(string)){
                                                                isPercentWeight = true;
                                                                weightPercentString = tokens[2];

                                                                if(!algoInfoMap.getValue().get(7).equals("") && !
                                                                        algoInfoMap.getValue().get(8).equals("")) {
                                                                    // map2.getValue().get(5) == ""
                                                                    // below is an important line, that's the conditional
                                                                    if (weeksSinceLast % Integer.parseInt
                                                                            (algoInfoMap.getValue().get(7)) == 0) {
                                                                        //if (!isExerciseName(tokens[2])) {
                                                                        String delimsP = "[_]";
                                                                        String[] tokensP =weightPercentString.split(delimsP);
                                                                        int weightPercentInt = Integer.parseInt(tokensP[1]);
                                                                        weightPercentInt += Integer.parseInt(algoInfoMap.getValue().get(8));
                                                                        weightPercentString = "p_" + weightPercentInt
                                                                                + "_a_" + tokensP[3];
                                                                        if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                            // loop == true

                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            if(!isNonIntRep){
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }

                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                        && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                    int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                    if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                        int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        if(j % setsWeek == 0){
                                                                                            if(sets - setsInc > 0){
                                                                                                sets = sets - setsInc;
                                                                                            }else{
                                                                                                sets = 1;
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!isNonIntRep){
                                                                                    if(!algoInfoMap.getValue().get(3).isEmpty()
                                                                                            && !algoInfoMap.getValue().get(4).isEmpty()){
                                                                                        int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                        if(repsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                            int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            if(j % repsWeek == 0){
                                                                                                if(reps - repsInc > 0){
                                                                                                    reps = reps - repsInc;
                                                                                                }else{
                                                                                                    reps = 1;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                // reset algoDateMap(1) to today
                                                                                // string to avoid taking away
                                                                                // too much.
                                                                            }
                                                                            //}
                                                                            updateOldDate = true;
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }else {
                                                                        if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }else{
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(1)) == 0) {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                            } else {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }
                                                                        }

                                                                        if(!isNonIntRep) {
                                                                            if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                    (4).equals("")) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            } else {
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(3)) == 0) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                    reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                } else {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }
                                                                            }
                                                                        }

                                                                        if (!isExerciseName(tokens[2])) {
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                        }
                                                                    }
                                                                }else{
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }else{

                                                                //TODO: Alert user that looping only works if you set weight increases

                                                                if(!algoInfoMap.getValue().get(5).equals("")
                                                                        && !algoInfoMap.getValue().get(6).equals("")) {
                                                                    // map2.getValue().get(5) == ""
                                                                    // below is an important line, that's the conditional
                                                                    if (weeksSinceLast % Integer.parseInt
                                                                            (algoInfoMap.getValue().get(5)) == 0) {
                                                                        //if (!isExerciseName(tokens[2])) {//
                                                                            if(!isNonIntWeight){
                                                                                weight = Integer.parseInt(tokens[2]);
                                                                                weight += Integer.parseInt(algoInfoMap.getValue().get(6));
                                                                            }
                                                                            if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                                // loop == true

                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                if(!isNonIntRep) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }

                                                                                for (int j = 1; j < weeksSinceLast; j++) {
                                                                                    if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                            && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                        int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                        if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))){
                                                                                            int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                            if(j % setsWeek == 0){
                                                                                                if(sets - setsInc > 0){
                                                                                                    sets = sets - setsInc;
                                                                                                }else{
                                                                                                    sets = 1;
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    if(!isNonIntRep) {
                                                                                        if (!algoInfoMap.getValue().get(3).isEmpty()
                                                                                                && !algoInfoMap.getValue().get(4).isEmpty()) {
                                                                                            int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                            if (repsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))) {
                                                                                                int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                                if (j % repsWeek == 0) {
                                                                                                    if (reps - repsInc > 0) {
                                                                                                        reps = reps - repsInc;
                                                                                                    } else {
                                                                                                        reps = 1;
                                                                                                    }

                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                    // reset algoDateMap(1) to today
                                                                                    // string to avoid taking away
                                                                                    // too much.
                                                                                }
                                                                                //}
                                                                                updateOldDate = true;
                                                                            }else{
                                                                                if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }else{
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(1)) == 0) {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                        sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                    } else {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                    }
                                                                                }

                                                                                if(!isNonIntRep) {
                                                                                    if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                            (4).equals("")) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    } else {
                                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                .get(3)) == 0) {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                            reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                        } else {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        //}
                                                                        //}else if(weeksSinceLast == Integer.parseInt
                                                                        //(map2.getValue().get(5))){
                                                                        //   if (!isExerciseName(tokens[2])) {
                                                                        //       if(!isNonIntWeight){
                                                                        //           weight = Integer.parseInt(tokens[2]);
                                                                        //           weight += Integer.parseInt
                                                                        //   (map2.getValue().get(6));
                                                                        //       }
                                                                        //   }
                                                                    }else {
                                                                        if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }else{
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(1)) == 0) {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                            } else {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }
                                                                        }

                                                                        if(!isNonIntRep) {
                                                                            if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                    (4).equals("")) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            } else {
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(3)) == 0) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                    reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                } else {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }
                                                                            }
                                                                        }

                                                                        if (!isExerciseName(tokens[2])) {
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                        }
                                                                    }
                                                                }else{
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }

                                                                    if (!isExerciseName(tokens[2])) {
                                                                        weight = Integer.parseInt(tokens[2]);
                                                                    }
                                                                }

                                                                //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                            }

                                                            String concat;

                                                            if(isPercentWeight){
                                                                if(isNonIntRep){
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + tokens[1]
                                                                            + "@" + weightPercentString;
                                                                }else{
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + Integer.toString(reps)
                                                                            + "@" + weightPercentString;
                                                                }
                                                            }else{
                                                                if(isNonIntRep){
                                                                    if(!isNonIntWeight){
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + tokens[1]
                                                                                + "@" + Integer.toString(weight);
                                                                    }else{
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + tokens[1]
                                                                                + "@" + tokens[2];
                                                                    }
                                                                }else{
                                                                    if(!isNonIntWeight){
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + Integer.toString(reps)
                                                                                + "@" + Integer.toString(weight);
                                                                    }else{
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + Integer.toString(reps)
                                                                                + "@" + tokens[2];
                                                                    }
                                                                }
                                                            }


                                                            newValueList.add(concat);
                                                        } else {
                                                            newValueList.add(string);
                                                        }
                                                    }
                                                }
                                            }
                                        } catch (IndexOutOfBoundsException e){
                                            Log.i("info", "out of bounds");
                                        }

                                        //}else{
                                        //        // if is not running
                                        //        instantiateList = true;
                                        //    }

                                    }
                                    if(!hasEx){
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                }else{
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }else {
                                newValueList.add(string);
                            }
                        }


                        if(instantiateList){
                            // CREATE map

                            if(firstLoopBool){
                                DateTime dateTime = new DateTime();
                                int currentWeekday = dateTime.getDayOfWeek();

                                if (templateClass.getAlgorithmDateMap() == null) {
                                    // creates new (first) date map
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put("0_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                } else {
                                    // creates new algoDateMap at algoDateMap.size + 1
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    newHashMap.putAll(templateClass.getAlgorithmDateMap());
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }

                                //mTemplateClass.updateRunningDate(exNameFormatted);

                                firstLoopBool = false;
                            }



                        }else{
                            // UPDATE maps
                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            String bool = "true";

                            originalHashmap.put(key, newValueList);
                            //mTemplateClass.setNewDateMapValues(exNameFormatted, bool, todayString);

                            String oldDate = null;
                            String dateKey = null;

                            for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                    ()){
                                if(isToday(dateMap2.getValue().get(3))) {
                                    if (dateMap2.getValue().get(0).equals(exNameFormatted)) {
                                        dateKey = dateMap2.getKey();
                                        oldDate = dateMap2.getValue().get(1);
                                    }
                                }
                            }

                            if(dateKey != null){
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(!isRunning || oldDate == null || updateOldDate){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(dateKey, newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }else{
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(!isRunning || oldDate == null || updateOldDate){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                //if(oldDate == null){
                                //    newList.add(LocalDate.now().toString());
                                //}else{
                                //    newList.add(oldDate);
                                //}
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }


                            String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                            if(mapName.equals("mMapOne")){
                                templateClass.setMapOne(originalHashmap);
                            }else if(mapName.equals("mMapTwo")){
                                templateClass.setMapTwo(originalHashmap);
                            }else if(mapName.equals("mMapThree")){
                                templateClass.setMapThree(originalHashmap);
                            }else if(mapName.equals("mMapFour")){
                                templateClass.setMapFour(originalHashmap);
                            }else if(mapName.equals("mMapFive")){
                                templateClass.setMapFive(originalHashmap);
                            }else if(mapName.equals("mMapSix")){
                                templateClass.setMapSix(originalHashmap);
                            }else if(mapName.equals("mMapSeven")){
                                templateClass.setMapSeven(originalHashmap);
                            }

                            firstLoopBool = false;
                        }
                    }
                }
            }
        }

        Log.i("info", "String");

    }

    private void generateAlgoAll(String exName, boolean isSuperset) {

        isSuperset = false;

        /**
         * What I want it to do:
         * Increase the sets/reps/weight for each exercise with the ex name of "exName"
         */

        HashMap<String, List<String>> valueMap = new HashMap<>();
        HashMap<String, List<String>> newValueMap = new HashMap<>();

        List<String> valueList = new ArrayList<>();
        String key = null;
        for (Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()) {
            if (map1.getValue().get(0).equals(exName)) {
                if (isSuperset == isSupersetList(map1.getValue())) {
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                    valueMap.put(map1.getKey(), map1.getValue());
                }
            }
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else {
            for (Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if (isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exName)) {
                        if (algoDateMap.getValue().size() > 4) {
                            if (getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        } else {
                            // check with today
                            if (getWeeksBetween(algoDateMap.getValue().get(1), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        }
                    }
                }
            }
        }

        newValueMap.putAll(valueMap);

        boolean goneThroughOnce = false;

        LocalDate newDate = LocalDate.now();
        for (Map.Entry<String, List<String>> algoInfoMap : templateClass.getAlgorithmInfo().entrySet()) {
            //if(map2.getValue().size() < 12){
            if(!goneThroughOnce){
                if(algoInfoMap.getValue().get(0).equals("all")) {
                    goneThroughOnce = true;
                    //List<String> newValueList = new ArrayList<>();
                    //newValueList.add(exName);
                    boolean instantiateList = false;
                    boolean updateOldDate = false;
                    for (Map.Entry<String, List<String>> valueMapEntry : valueMap.entrySet()) {
                        List<String> subList = new ArrayList<>();
                        subList.addAll(valueMapEntry.getValue());
                        List<String> newValueList = new ArrayList<>();
                        newValueList.add(exName);
                        for (String string : subList) {
                            if (!isExerciseName(string)) {
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if (templateClass.getAlgorithmDateMap() != null) {
                                    boolean hasEx = false;
                                    // need to check for the case of if empty or if no entries were found
                                    for (Map.Entry<String, List<String>> algoDateMap : templateClass.getAlgorithmDateMap().entrySet()) {
                                        if (isToday(algoDateMap.getValue().get(3))) {
                                            if (algoDateMap.getValue().get(0).equals(exName)) {
                                                if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                    // compare, keep everything the same, set to true.
                                                    hasEx = true;
                                                    if(isRunning) {
                                                        int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                        int sets = 0;
                                                        int reps = 0;
                                                        int weight = 0;
                                                        String weightPercentString = "";

                                                        boolean isNonIntWeight = false;
                                                        boolean isNonIntRep = false;
                                                        boolean isPercentWeight = false;

                                                        if(isExerciseName(tokens[2])){
                                                            isNonIntWeight = true;
                                                        }

                                                        if(tokens[1].equals("T.F.")){
                                                            isNonIntRep = true;
                                                        }

                                                        if(isPercentage(string)){
                                                            isPercentWeight = true;
                                                            weightPercentString = tokens[2];

                                                            if(!algoInfoMap.getValue().get(7).equals("") && !
                                                                    algoInfoMap.getValue().get(8).equals("")) {
                                                                // map2.getValue().get(5) == ""
                                                                // below is an important line, that's the conditional
                                                                if (weeksSinceLast % Integer.parseInt
                                                                        (algoInfoMap.getValue().get(7)) == 0) {
                                                                    //if (!isExerciseName(tokens[2])) {
                                                                    String delimsP = "[_]";
                                                                    String[] tokensP =weightPercentString.split(delimsP);
                                                                    int weightPercentInt = Integer.parseInt(tokensP[1]);
                                                                    weightPercentInt += Integer.parseInt(algoInfoMap.getValue().get(8));
                                                                    weightPercentString = "p_" + weightPercentInt
                                                                            + "_a_" + tokensP[3];
                                                                    if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                        // loop == true

                                                                        sets = Integer.parseInt(tokens[0]);
                                                                        if(!isNonIntRep){
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        }

                                                                        for (int j = 1; j < weeksSinceLast; j++) {
                                                                            if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                    && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                    int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                    if(j % setsWeek == 0){
                                                                                        if(sets - setsInc > 0){
                                                                                            sets = sets - setsInc;
                                                                                        }else{
                                                                                            sets = 1;
                                                                                        }

                                                                                    }
                                                                                }
                                                                            }
                                                                            if(!isNonIntRep){
                                                                                if(!algoInfoMap.getValue().get(3).isEmpty()
                                                                                        && !algoInfoMap.getValue().get(4).isEmpty()){
                                                                                    int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                    if(repsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                        int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                        if(j % repsWeek == 0){
                                                                                            if(reps - repsInc > 0){
                                                                                                reps = reps - repsInc;
                                                                                            }else{
                                                                                                reps = 1;
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                            // reset algoDateMap(1) to today
                                                                            // string to avoid taking away
                                                                            // too much.
                                                                        }
                                                                        //}
                                                                        updateOldDate = true;
                                                                    }else{
                                                                        if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }else{
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(1)) == 0) {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                            } else {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }
                                                                        }

                                                                        if(!isNonIntRep) {
                                                                            if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                    (4).equals("")) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            } else {
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(3)) == 0) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                    reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                } else {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }else {
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }

                                                                    if (!isExerciseName(tokens[2])) {
                                                                        weight = Integer.parseInt(tokens[2]);
                                                                    }
                                                                }
                                                            }else{
                                                                if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                    sets = Integer.parseInt(tokens[0]);
                                                                }else{
                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                            .get(1)) == 0) {
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                        sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                    } else {
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }
                                                                }

                                                                if(!isNonIntRep) {
                                                                    if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                            (4).equals("")) {
                                                                        reps = Integer.parseInt(tokens[1]);
                                                                    } else {
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(3)) == 0) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                            reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                        } else {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }else{

                                                            //TODO: Alert user that looping only works if you set weight increases

                                                            if(!algoInfoMap.getValue().get(5).equals("")
                                                                    && !algoInfoMap.getValue().get(6).equals("")) {
                                                                // map2.getValue().get(5) == ""
                                                                // below is an important line, that's the conditional
                                                                if (weeksSinceLast % Integer.parseInt
                                                                        (algoInfoMap.getValue().get(5)) == 0) {
                                                                    //if (!isExerciseName(tokens[2])) {//
                                                                        if(!isNonIntWeight){
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                            weight += Integer.parseInt(algoInfoMap.getValue().get(6));
                                                                        }
                                                                        if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                            // loop == true

                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            if(!isNonIntRep) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }

                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                        && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                    int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                    if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))){
                                                                                        int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        if(j % setsWeek == 0){
                                                                                            if(sets - setsInc > 0){
                                                                                                sets = sets - setsInc;
                                                                                            }else{
                                                                                                sets = 1;
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!isNonIntRep) {
                                                                                    if (!algoInfoMap.getValue().get(3).isEmpty()
                                                                                            && !algoInfoMap.getValue().get(4).isEmpty()) {
                                                                                        int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                        if (repsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))) {
                                                                                            int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            if (j % repsWeek == 0) {
                                                                                                if (reps - repsInc > 0) {
                                                                                                    reps = reps - repsInc;
                                                                                                } else {
                                                                                                    reps = 1;
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                // reset algoDateMap(1) to today
                                                                                // string to avoid taking away
                                                                                // too much.
                                                                            }
                                                                            //}
                                                                            updateOldDate = true;
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    //}
                                                                    //}else if(weeksSinceLast == Integer.parseInt
                                                                    //(map2.getValue().get(5))){
                                                                    //   if (!isExerciseName(tokens[2])) {
                                                                    //       if(!isNonIntWeight){
                                                                    //           weight = Integer.parseInt(tokens[2]);
                                                                    //           weight += Integer.parseInt
                                                                    //   (map2.getValue().get(6));
                                                                    //       }
                                                                    //   }
                                                                }else {
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }

                                                                    if (!isExerciseName(tokens[2])) {
                                                                        weight = Integer.parseInt(tokens[2]);
                                                                    }
                                                                }
                                                            }else{
                                                                if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                    sets = Integer.parseInt(tokens[0]);
                                                                }else{
                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                            .get(1)) == 0) {
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                        sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                    } else {
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }
                                                                }

                                                                if(!isNonIntRep) {
                                                                    if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                            (4).equals("")) {
                                                                        reps = Integer.parseInt(tokens[1]);
                                                                    } else {
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(3)) == 0) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                            reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                        } else {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        }
                                                                    }
                                                                }

                                                                if (!isExerciseName(tokens[2])) {
                                                                    weight = Integer.parseInt(tokens[2]);
                                                                }
                                                            }

                                                            //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                        }

                                                        String concat;

                                                        if(isPercentWeight){
                                                            if(isNonIntRep){
                                                                concat = Integer.toString(sets)
                                                                        + "x" + tokens[1]
                                                                        + "@" + weightPercentString;
                                                            }else{
                                                                concat = Integer.toString(sets)
                                                                        + "x" + Integer.toString(reps)
                                                                        + "@" + weightPercentString;
                                                            }
                                                        }else{
                                                            if(isNonIntRep){
                                                                if(!isNonIntWeight){
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + tokens[1]
                                                                            + "@" + Integer.toString(weight);
                                                                }else{
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + tokens[1]
                                                                            + "@" + tokens[2];
                                                                }
                                                            }else{
                                                                if(!isNonIntWeight){
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + Integer.toString(reps)
                                                                            + "@" + Integer.toString(weight);
                                                                }else{
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + Integer.toString(reps)
                                                                            + "@" + tokens[2];
                                                                }
                                                            }
                                                        }


                                                        newValueList.add(concat);
                                                    } else {
                                                        // is not running
                                                        //instantiateList = true;
                                                        newValueList.add(string);
                                                    }
                                                    //newValueMap.put(valueMapEntry.getKey(), newValueList);
                                                } else {
                                                    // don't compare, set the date to today, and set bool to true.
                                                    newValueList.add(string);
                                                    //newValueMap.get(valueMapEntry.getKey()).add(string);
                                                }
                                            }
                                        }

                                        newValueMap.put(valueMapEntry.getKey(), newValueList);
                                    }
                                    if(exName.equals("Overhead Press (Dumbbell)")){
                                        Log.i("i", "i");
                                    }
                                    if (!hasEx) {
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                } else {
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }
                        }
                    }



                    if (instantiateList) {
                        // instantiate datemap with this exercise

                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();

                        if (templateClass.getAlgorithmDateMap() == null) {
                            // creates new (first) date map
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put("0_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        } else {
                            // creates new algoDateMap at algoDateMap.size + 1
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            newHashMap.putAll(templateClass.getAlgorithmDateMap());
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }

                    } else {
                        // UPDATE maps (increment)

                        //originalHashmap.put(key, newValueList);
                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();
                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                .get(0);
                        String bool = "true";

                        originalHashmap.putAll(newValueMap);

                        String oldDate = null;
                        String dateKey = null;

                        for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                ()){
                            if(isToday(dateMap2.getValue().get(3))) {
                                if (dateMap2.getValue().get(0).equals(exName)) {
                                    dateKey = dateMap2.getKey();
                                    oldDate = dateMap2.getValue().get(1);
                                }
                            }
                        }

                        if(dateKey != null){
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(!isRunning || oldDate == null || updateOldDate){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(dateKey, newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }else{
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(!isRunning || oldDate == null || updateOldDate){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            //if(oldDate == null){
                            //    newList.add(LocalDate.now().toString());
                            //}else{
                            //    newList.add(oldDate);
                            //}
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }

                        String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                        if (mapName.equals("mMapOne")) {
                            templateClass.setMapOne(originalHashmap);
                        } else if (mapName.equals("mMapTwo")) {
                            templateClass.setMapTwo(originalHashmap);
                        } else if (mapName.equals("mMapThree")) {
                            templateClass.setMapThree(originalHashmap);
                        } else if (mapName.equals("mMapFour")) {
                            templateClass.setMapFour(originalHashmap);
                        } else if (mapName.equals("mMapFive")) {
                            templateClass.setMapFive(originalHashmap);
                        } else if (mapName.equals("mMapSix")) {
                            templateClass.setMapSix(originalHashmap);
                        } else if (mapName.equals("mMapSeven")) {
                            templateClass.setMapSeven(originalHashmap);
                        }

                        Log.i("info", "String");
                    }
                }
                //}
            }
        }
    }

    private void generateAlgoForSuperset(String exName, String tag, String exNameUnformatted){

        HashMap<String, List<String>> hashMapCopy = new HashMap<>();
        hashMapCopy.putAll(originalHashmap);

        String delims1 = "[_]";
        String[] exNameTokens = exNameUnformatted.split(delims1);
        String exNameFormatted = "";
        for(int i = 0; i < exNameTokens.length - 1; i++){
            exNameFormatted = exNameFormatted + exNameTokens[i] + "_";
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else{
            for(Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if(isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                        if(algoDateMap.getValue().size() > 4){
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }else{
                            // check with today
                            double weeks = getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString());
                            if(weeks > 1.0){
                                // it's been longer than a week
                                isRunning = false;
                            }else{
                                isRunning = true;
                            }
                        }
                    }
                }
            }
        }



        boolean firstLoopBool = true;

        LocalDate newDate = LocalDate.now();
        for(Map.Entry<String, List<String>> algoInfoMap : templateClass.getAlgorithmInfo().entrySet()){
            if(algoInfoMap.getValue().get(0).equals(exName)){
                // so now we're in the correct algorithm map
                List<String> newValueList = new ArrayList<>();
                boolean updateOldDate = false;
                boolean instantiateList = false;
                for(Map.Entry<String, List<String>> map : hashMapCopy.entrySet()){
                    String key = tag + "_key";
                    if(map.getKey().equals(key)){
                        // correct entry in original hashmap
                        List<String> valueList = new ArrayList<>();
                        valueList.addAll(map.getValue());
                        for(String string : valueList){
                            if(!isExerciseName(string)){
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if(templateClass.getAlgorithmDateMap() != null){
                                        boolean hasEx = false;
                                        for(Map.Entry<String, List<String>> algoDateMap : templateClass
                                                .getAlgorithmDateMap().entrySet()){
                                                try {
                                                    if (isToday(algoDateMap.getValue().get(3))) {
                                                        if (algoDateMap.getValue().get(0).equals(exNameFormatted)) {
                                                            if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                                // compare, keep everything the same, set to true.
                                                                hasEx = true;
                                                                if(isRunning){
                                                                    int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                                    int sets = 0;
                                                                    int reps = 0;
                                                                    int weight = 0;
                                                                    String weightPercentString = "";

                                                                    boolean isNonIntWeight = false;
                                                                    boolean isNonIntRep = false;
                                                                    boolean isPercentWeight = false;

                                                                    if(isExerciseName(tokens[2])){
                                                                        isNonIntWeight = true;
                                                                    }

                                                                    if(tokens[1].equals("T.F.")){
                                                                        isNonIntRep = true;
                                                                    }

                                                                    if(isPercentage(string)){
                                                                        isPercentWeight = true;
                                                                        weightPercentString = tokens[2];

                                                                        if(!algoInfoMap.getValue().get(7).equals("") && !
                                                                                algoInfoMap.getValue().get(8).equals("")) {
                                                                            // map2.getValue().get(5) == ""
                                                                            // below is an important line, that's the conditional
                                                                            if (weeksSinceLast % Integer.parseInt
                                                                                    (algoInfoMap.getValue().get(7)) == 0) {
                                                                                //if (!isExerciseName(tokens[2])) {
                                                                                String delimsP = "[_]";
                                                                                String[] tokensP =weightPercentString.split(delimsP);
                                                                                int weightPercentInt = Integer.parseInt(tokensP[1]);
                                                                                weightPercentInt += Integer.parseInt(algoInfoMap.getValue().get(8));
                                                                                weightPercentString = "p_" + weightPercentInt
                                                                                        + "_a_" + tokensP[3];
                                                                                if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                                    // loop == true

                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    if(!isNonIntRep){
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }

                                                                                    for (int j = 1; j < weeksSinceLast; j++) {
                                                                                        if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                                && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                            int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                            if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                                int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                                if(j % setsWeek == 0){
                                                                                                    if(sets - setsInc > 0){
                                                                                                        sets = sets - setsInc;
                                                                                                    }else{
                                                                                                        sets = 1;
                                                                                                    }

                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(!isNonIntRep){
                                                                                            if(!algoInfoMap.getValue().get(3).isEmpty()
                                                                                                    && !algoInfoMap.getValue().get(4).isEmpty()){
                                                                                                int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                                if(repsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                                    int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                                    if(j % repsWeek == 0){
                                                                                                        if(reps - repsInc > 0){
                                                                                                            reps = reps - repsInc;
                                                                                                        }else{
                                                                                                            reps = 1;
                                                                                                        }
                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        // reset algoDateMap(1) to today
                                                                                        // string to avoid taking away
                                                                                        // too much.
                                                                                    }
                                                                                    //}
                                                                                    updateOldDate = true;
                                                                                }else{
                                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                    }else{
                                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                .get(1)) == 0) {
                                                                                            sets = Integer.parseInt(tokens[0]);
                                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        } else {
                                                                                            sets = Integer.parseInt(tokens[0]);
                                                                                        }
                                                                                    }

                                                                                    if(!isNonIntRep) {
                                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                                (4).equals("")) {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                        } else {
                                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                    .get(3)) == 0) {
                                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            } else {
                                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }else {
                                                                                if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }else{
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(1)) == 0) {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                        sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                    } else {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                    }
                                                                                }

                                                                                if(!isNonIntRep) {
                                                                                    if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                            (4).equals("")) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    } else {
                                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                .get(3)) == 0) {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                            reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                        } else {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                        }
                                                                                    }
                                                                                }

                                                                                if (!isExerciseName(tokens[2])) {
                                                                                    weight = Integer.parseInt(tokens[2]);
                                                                                }
                                                                            }
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }else{

                                                                        //TODO: Alert user that looping only works if you set weight increases

                                                                        if(!algoInfoMap.getValue().get(5).equals("")
                                                                                && !algoInfoMap.getValue().get(6).equals("")) {
                                                                            // map2.getValue().get(5) == ""
                                                                            // below is an important line, that's the conditional
                                                                            if (weeksSinceLast % Integer.parseInt
                                                                                    (algoInfoMap.getValue().get(5)) == 0) {
                                                                                //if (!isExerciseName(tokens[2])) {//
                                                                                if(!isNonIntWeight){
                                                                                    weight = Integer.parseInt(tokens[2]);
                                                                                    weight += Integer.parseInt(algoInfoMap.getValue().get(6));
                                                                                }
                                                                                if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                                    // loop == true

                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    if(!isNonIntRep) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }

                                                                                    for (int j = 1; j < weeksSinceLast; j++) {
                                                                                        if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                                && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                            int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                            if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))){
                                                                                                int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                                if(j % setsWeek == 0){
                                                                                                    if(sets - setsInc > 0){
                                                                                                        sets = sets - setsInc;
                                                                                                    }else{
                                                                                                        sets = 1;
                                                                                                    }

                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        if(!isNonIntRep) {
                                                                                            if (!algoInfoMap.getValue().get(3).isEmpty()
                                                                                                    && !algoInfoMap.getValue().get(4).isEmpty()) {
                                                                                                int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                                if (repsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))) {
                                                                                                    int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                                    if (j % repsWeek == 0) {
                                                                                                        if (reps - repsInc > 0) {
                                                                                                            reps = reps - repsInc;
                                                                                                        } else {
                                                                                                            reps = 1;
                                                                                                        }

                                                                                                    }
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                        // reset algoDateMap(1) to today
                                                                                        // string to avoid taking away
                                                                                        // too much.
                                                                                    }
                                                                                    //}
                                                                                    updateOldDate = true;
                                                                                }else{
                                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                    }else{
                                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                .get(1)) == 0) {
                                                                                            sets = Integer.parseInt(tokens[0]);
                                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        } else {
                                                                                            sets = Integer.parseInt(tokens[0]);
                                                                                        }
                                                                                    }

                                                                                    if(!isNonIntRep) {
                                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                                (4).equals("")) {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                        } else {
                                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                    .get(3)) == 0) {
                                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            } else {
                                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                //}
                                                                                //}else if(weeksSinceLast == Integer.parseInt
                                                                                //(map2.getValue().get(5))){
                                                                                //   if (!isExerciseName(tokens[2])) {
                                                                                //       if(!isNonIntWeight){
                                                                                //           weight = Integer.parseInt(tokens[2]);
                                                                                //           weight += Integer.parseInt
                                                                                //   (map2.getValue().get(6));
                                                                                //       }
                                                                                //   }
                                                                            }else {
                                                                                if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }else{
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(1)) == 0) {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                        sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                    } else {
                                                                                        sets = Integer.parseInt(tokens[0]);
                                                                                    }
                                                                                }

                                                                                if(!isNonIntRep) {
                                                                                    if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                            (4).equals("")) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    } else {
                                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                                .get(3)) == 0) {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                            reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                        } else {
                                                                                            reps = Integer.parseInt(tokens[1]);
                                                                                        }
                                                                                    }
                                                                                }

                                                                                if (!isExerciseName(tokens[2])) {
                                                                                    weight = Integer.parseInt(tokens[2]);
                                                                                }
                                                                            }
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }

                                                                            if (!isExerciseName(tokens[2])) {
                                                                                weight = Integer.parseInt(tokens[2]);
                                                                            }
                                                                        }

                                                                        //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                                    }

                                                                    String concat;

                                                                    if(isPercentWeight){
                                                                        if(isNonIntRep){
                                                                            concat = Integer.toString(sets)
                                                                                    + "x" + tokens[1]
                                                                                    + "@" + weightPercentString;
                                                                        }else{
                                                                            concat = Integer.toString(sets)
                                                                                    + "x" + Integer.toString(reps)
                                                                                    + "@" + weightPercentString;
                                                                        }
                                                                    }else{
                                                                        if(isNonIntRep){
                                                                            if(!isNonIntWeight){
                                                                                concat = Integer.toString(sets)
                                                                                        + "x" + tokens[1]
                                                                                        + "@" + Integer.toString(weight);
                                                                            }else{
                                                                                concat = Integer.toString(sets)
                                                                                        + "x" + tokens[1]
                                                                                        + "@" + tokens[2];
                                                                            }
                                                                        }else{
                                                                            if(!isNonIntWeight){
                                                                                concat = Integer.toString(sets)
                                                                                        + "x" + Integer.toString(reps)
                                                                                        + "@" + Integer.toString(weight);
                                                                            }else{
                                                                                concat = Integer.toString(sets)
                                                                                        + "x" + Integer.toString(reps)
                                                                                        + "@" + tokens[2];
                                                                            }
                                                                        }
                                                                    }


                                                                    newValueList.add(concat);
                                                                } else {
                                                                newValueList.add(string);
                                                            }
                                                        }
                                                    }
                                                }
                                            } catch (IndexOutOfBoundsException e){
                                                    Log.i("info", "out of bounds");
                                            }

                                        //}else{
                                        //        // if is not running
                                        //        instantiateList = true;
                                        //    }

                                    }
                                    if(!hasEx){
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                }else{
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }else {
                                newValueList.add(string);
                            }
                        }


                        if(instantiateList){
                            // CREATE map

                            if(firstLoopBool){
                                DateTime dateTime = new DateTime();
                                int currentWeekday = dateTime.getDayOfWeek();

                                if (templateClass.getAlgorithmDateMap() == null) {
                                    // creates new (first) date map
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put("0_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                } else {
                                    // creates new algoDateMap at algoDateMap.size + 1
                                    HashMap<String, List<String>> newHashMap = new HashMap<>();
                                    newHashMap.putAll(templateClass.getAlgorithmDateMap());
                                    String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                            .get(0);
                                    List<String> newList = new ArrayList<>();
                                    newList.add(exNameFormatted);
                                    newList.add(newDate.toString());
                                    newList.add("true");
                                    newList.add(todayString);
                                    newList.add(newDate.toString());
                                    newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                    templateClass.setAlgorithmDateMap(newHashMap);
                                }

                                //mTemplateClass.updateRunningDate(exNameFormatted);

                                firstLoopBool = false;
                            }



                        }else{
                            // UPDATE maps
                            DateTime dateTime = new DateTime();
                            int currentWeekday = dateTime.getDayOfWeek();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            String bool = "true";

                            originalHashmap.put(key, newValueList);
                            //mTemplateClass.setNewDateMapValues(exNameFormatted, bool, todayString);

                            String oldDate = null;
                            String dateKey = null;

                            for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                    ()){
                                if(isToday(dateMap2.getValue().get(3))) {
                                    if (dateMap2.getValue().get(0).equals(exNameFormatted)) {
                                        dateKey = dateMap2.getKey();
                                        oldDate = dateMap2.getValue().get(1);
                                    }
                                }
                            }

                            if(dateKey != null){
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(!isRunning || oldDate == null || updateOldDate){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(dateKey, newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }else{
                                HashMap<String, List<String>> newMap = new HashMap<>();
                                newMap.putAll(templateClass.getAlgorithmDateMap());
                                List<String> newList = new ArrayList<>();
                                newList.add(exNameFormatted);
                                if(!isRunning || oldDate == null || updateOldDate){
                                    newList.add(LocalDate.now().toString());
                                }else{
                                    newList.add(oldDate);
                                }
                                //if(oldDate == null){
                                //    newList.add(LocalDate.now().toString());
                                //}else{
                                //    newList.add(oldDate);
                                //}
                                newList.add("true");
                                newList.add(todayString);
                                newList.add(LocalDate.now().toString());
                                newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                                templateClass.setAlgorithmDateMap(newMap);
                            }


                            String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                            if(mapName.equals("mMapOne")){
                                templateClass.setMapOne(originalHashmap);
                            }else if(mapName.equals("mMapTwo")){
                                templateClass.setMapTwo(originalHashmap);
                            }else if(mapName.equals("mMapThree")){
                                templateClass.setMapThree(originalHashmap);
                            }else if(mapName.equals("mMapFour")){
                                templateClass.setMapFour(originalHashmap);
                            }else if(mapName.equals("mMapFive")){
                                templateClass.setMapFive(originalHashmap);
                            }else if(mapName.equals("mMapSix")){
                                templateClass.setMapSix(originalHashmap);
                            }else if(mapName.equals("mMapSeven")){
                                templateClass.setMapSeven(originalHashmap);
                            }

                            firstLoopBool = false;
                        }
                    }
                }
            }
        }

        Log.i("info", "String");

    }

    private void generateAlgo(String exName, boolean isSuperset)  {

        isSuperset = false;

        /**
         * What I want it to do:
         * Increase the sets/reps/weight for each exercise with the ex name of "exName"
         */

        HashMap<String, List<String>> valueMap = new HashMap<>();
        HashMap<String, List<String>> newValueMap = new HashMap<>();

        List<String> valueList = new ArrayList<>();
        String key = null;
        for (Map.Entry<String, List<String>> map1 : originalHashmap.entrySet()) {
            if (map1.getValue().get(0).equals(exName)) {
                if (isSuperset == isSupersetList(map1.getValue())) {
                    valueList.addAll(map1.getValue());
                    key = map1.getKey();
                    valueMap.put(map1.getKey(), map1.getValue());
                }
            }
        }

        boolean isRunning = true; // isRunning aka "is your last completed workout under 2 weeks ago?"



        if(templateClass.getAlgorithmDateMap() == null){
            isRunning = false;
        }else {
            for (Map.Entry<String, List<String>> algoDateMap : templateClass
                    .getAlgorithmDateMap().entrySet()) {
                if (isToday(algoDateMap.getValue().get(3))) {
                    if (algoDateMap.getValue().get(0).equals(exName)) {
                        if (algoDateMap.getValue().size() > 4) {
                            // for some reason we're comparing to (4)
                            if (getWeeksBetween(algoDateMap.getValue().get(4), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        } else {
                            // check with today
                            if (getWeeksBetween(algoDateMap.getValue().get(1), LocalDate.now().toString()) > 1.0) {
                                // it's been longer than a week
                                isRunning = false;
                            } else {

                            }
                        }
                    }
                }
            }
        }

        /**
         * OK, so (4) simply tells us if we're on a "roll." If we haven't missed a week. If we should even run the
         * algo check.
         * (1) is what we use to actually get the weeksSinceLast!
         *
         * If we are NOT on a roll (!isRunning), then we need to update both to the current day.
         */

        newValueMap.putAll(valueMap);

        boolean goneThroughOnce = false;

        LocalDate newDate = LocalDate.now();
        for (Map.Entry<String, List<String>> algoInfoMap : templateClass.getAlgorithmInfo().entrySet()) {
            //if(map2.getValue().size() < 12){
                if(!goneThroughOnce){
                    if(algoInfoMap.getValue().get(0).equals(exName)) {
                    goneThroughOnce = true;
                    //List<String> newValueList = new ArrayList<>();
                    //newValueList.add(exName);
                    boolean instantiateList = false;
                    boolean updateOldDate = false;
                    for (Map.Entry<String, List<String>> valueMapEntry : valueMap.entrySet()) {
                        List<String> subList = new ArrayList<>();
                        subList.addAll(valueMapEntry.getValue());
                        List<String> newValueList = new ArrayList<>();
                        newValueList.add(exName);
                        for (String string : subList) {
                            if (!isExerciseName(string)) {
                                String delims = "[x,@]";
                                String[] tokens = string.split(delims);
                                if (templateClass.getAlgorithmDateMap() != null) {
                                        boolean hasEx = false;
                                        // need to check for the case of if empty or if no entries were found
                                        for (Map.Entry<String, List<String>> algoDateMap : templateClass.getAlgorithmDateMap().entrySet()) {
                                            if (isToday(algoDateMap.getValue().get(3))) {
                                                if (algoDateMap.getValue().get(0).equals(exName)) {
                                                    if (Boolean.parseBoolean(algoDateMap.getValue().get(2))) {
                                                        // compare, keep everything the same, set to true.
                                                        hasEx = true;
                                                        if(isRunning) {
                                                            int weeksSinceLast = getWeeksSinceLast(algoDateMap.getValue().get(1));

                                                            int sets = 0;
                                                            int reps = 0;
                                                            int weight = 0;
                                                            String weightPercentString = "";

                                                            boolean isNonIntWeight = false;
                                                            boolean isNonIntRep = false;
                                                            boolean isPercentWeight = false;

                                                            if(isExerciseName(tokens[2])){
                                                                isNonIntWeight = true;
                                                            }

                                                            if(tokens[1].equals("T.F.")){
                                                                isNonIntRep = true;
                                                            }

                                                            if(isPercentage(string)){
                                                                isPercentWeight = true;
                                                                weightPercentString = tokens[2];

                                                                if(!algoInfoMap.getValue().get(7).equals("") && !
                                                                        algoInfoMap.getValue().get(8).equals("")) {
                                                                    // map2.getValue().get(5) == ""
                                                                    // below is an important line, that's the conditional
                                                                    if (weeksSinceLast % Integer.parseInt
                                                                            (algoInfoMap.getValue().get(7)) == 0) {
                                                                        //if (!isExerciseName(tokens[2])) {
                                                                        String delimsP = "[_]";
                                                                        String[] tokensP =weightPercentString.split(delimsP);
                                                                        int weightPercentInt = Integer.parseInt(tokensP[1]);
                                                                        weightPercentInt += Integer.parseInt(algoInfoMap.getValue().get(8));
                                                                        weightPercentString = "p_" + weightPercentInt
                                                                                + "_a_" + tokensP[3];
                                                                        if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                            // loop == true

                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            if(!isNonIntRep){
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }

                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                        && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                    int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                    if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                        int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        if(j % setsWeek == 0){
                                                                                            if(sets - setsInc > 0){
                                                                                                sets = sets - setsInc;
                                                                                            }else{
                                                                                                sets = 1;
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!isNonIntRep){
                                                                                    if(!algoInfoMap.getValue().get(3).isEmpty()
                                                                                            && !algoInfoMap.getValue().get(4).isEmpty()){
                                                                                        int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                        if(repsWeek < Integer.parseInt(algoInfoMap.getValue().get(7))){
                                                                                            int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            if(j % repsWeek == 0){
                                                                                                if(reps - repsInc > 0){
                                                                                                    reps = reps - repsInc;
                                                                                                }else{
                                                                                                    reps = 1;
                                                                                                }
                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                // reset algoDateMap(1) to today
                                                                                // string to avoid taking away
                                                                                // too much.
                                                                            }
                                                                            //}
                                                                            updateOldDate = true;
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }else {
                                                                        if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }else{
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(1)) == 0) {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                            } else {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }
                                                                        }

                                                                        if(!isNonIntRep) {
                                                                            if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                    (4).equals("")) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            } else {
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(3)) == 0) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                    reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                } else {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }
                                                                            }
                                                                        }

                                                                        if (!isExerciseName(tokens[2])) {
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                        }
                                                                    }
                                                                }else{
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }else{

                                                                //TODO: Alert user that looping only works if you set weight increases

                                                                if(!algoInfoMap.getValue().get(5).equals("")
                                                                        && !algoInfoMap.getValue().get(6).equals("")) {
                                                                    // map2.getValue().get(5) == ""
                                                                    // below is an important line, that's the conditional
                                                                    if (weeksSinceLast % Integer.parseInt
                                                                            (algoInfoMap.getValue().get(5)) == 0) {
                                                                        //if (!isExerciseName(tokens[2])) {//
                                                                        if(!isNonIntWeight){
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                            weight += Integer.parseInt(algoInfoMap.getValue().get(6));
                                                                        }
                                                                        if (Boolean.parseBoolean(algoInfoMap.getValue().get(9))) {
                                                                            // loop == true

                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            if(!isNonIntRep) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }

                                                                            for (int j = 1; j < weeksSinceLast; j++) {
                                                                                if(!algoInfoMap.getValue().get(1).isEmpty()
                                                                                        && !algoInfoMap.getValue().get(2).isEmpty()){
                                                                                    int setsWeek = Integer.parseInt(algoInfoMap.getValue().get(1));
                                                                                    if(setsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))){
                                                                                        int setsInc = Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                        if(j % setsWeek == 0){
                                                                                            if(sets - setsInc > 0){
                                                                                                sets = sets - setsInc;
                                                                                            }else{
                                                                                                sets = 1;
                                                                                            }

                                                                                        }
                                                                                    }
                                                                                }
                                                                                if(!isNonIntRep) {
                                                                                    if (!algoInfoMap.getValue().get(3).isEmpty()
                                                                                            && !algoInfoMap.getValue().get(4).isEmpty()) {
                                                                                        int repsWeek = Integer.parseInt(algoInfoMap.getValue().get(3));
                                                                                        if (repsWeek < Integer.parseInt(algoInfoMap.getValue().get(5))) {
                                                                                            int repsInc = Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                            if (j % repsWeek == 0) {
                                                                                                if (reps - repsInc > 0) {
                                                                                                    reps = reps - repsInc;
                                                                                                } else {
                                                                                                    reps = 1;
                                                                                                }

                                                                                            }
                                                                                        }
                                                                                    }
                                                                                }
                                                                                // reset algoDateMap(1) to today
                                                                                // string to avoid taking away
                                                                                // too much.
                                                                            }
                                                                            //}
                                                                            updateOldDate = true;
                                                                        }else{
                                                                            if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }else{
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(1)) == 0) {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                    sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                                } else {
                                                                                    sets = Integer.parseInt(tokens[0]);
                                                                                }
                                                                            }

                                                                            if(!isNonIntRep) {
                                                                                if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                        (4).equals("")) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                } else {
                                                                                    if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                            .get(3)) == 0) {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                        reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                    } else {
                                                                                        reps = Integer.parseInt(tokens[1]);
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }else {
                                                                        if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }else{
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(1)) == 0) {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                                sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                            } else {
                                                                                sets = Integer.parseInt(tokens[0]);
                                                                            }
                                                                        }

                                                                        if(!isNonIntRep) {
                                                                            if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                    (4).equals("")) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            } else {
                                                                                if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                        .get(3)) == 0) {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                    reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                                } else {
                                                                                    reps = Integer.parseInt(tokens[1]);
                                                                                }
                                                                            }
                                                                        }

                                                                        if (!isExerciseName(tokens[2])) {
                                                                            weight = Integer.parseInt(tokens[2]);
                                                                        }
                                                                    }
                                                                }else{
                                                                    if(algoInfoMap.getValue().get(1).equals("") || algoInfoMap.getValue().get(2).equals("")){
                                                                        sets = Integer.parseInt(tokens[0]);
                                                                    }else{
                                                                        if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                .get(1)) == 0) {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                            sets += Integer.parseInt(algoInfoMap.getValue().get(2));
                                                                        } else {
                                                                            sets = Integer.parseInt(tokens[0]);
                                                                        }
                                                                    }

                                                                    if(!isNonIntRep) {
                                                                        if (algoInfoMap.getValue().get(3).equals("") || algoInfoMap.getValue().get
                                                                                (4).equals("")) {
                                                                            reps = Integer.parseInt(tokens[1]);
                                                                        } else {
                                                                            if (weeksSinceLast % Integer.parseInt(algoInfoMap.getValue()
                                                                                    .get(3)) == 0) {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                                reps += Integer.parseInt(algoInfoMap.getValue().get(4));
                                                                            } else {
                                                                                reps = Integer.parseInt(tokens[1]);
                                                                            }
                                                                        }
                                                                    }

                                                                    if (!isExerciseName(tokens[2])) {
                                                                        weight = Integer.parseInt(tokens[2]);
                                                                    }
                                                                }

                                                                //newValueMap.get(valueMapEntry.getKey()).add(concat);
                                                            }

                                                            String concat;

                                                            if(isPercentWeight){
                                                                if(isNonIntRep){
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + tokens[1]
                                                                            + "@" + weightPercentString;
                                                                }else{
                                                                    concat = Integer.toString(sets)
                                                                            + "x" + Integer.toString(reps)
                                                                            + "@" + weightPercentString;
                                                                }
                                                            }else{
                                                                if(isNonIntRep){
                                                                    if(!isNonIntWeight){
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + tokens[1]
                                                                                + "@" + Integer.toString(weight);
                                                                    }else{
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + tokens[1]
                                                                                + "@" + tokens[2];
                                                                    }
                                                                }else{
                                                                    if(!isNonIntWeight){
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + Integer.toString(reps)
                                                                                + "@" + Integer.toString(weight);
                                                                    }else{
                                                                        concat = Integer.toString(sets)
                                                                                + "x" + Integer.toString(reps)
                                                                                + "@" + tokens[2];
                                                                    }
                                                                }
                                                            }


                                                            newValueList.add(concat);
                                                        } else {
                                                            // is not running
                                                            //instantiateList = true;
                                                            newValueList.add(string);
                                                        }
                                                        //newValueMap.put(valueMapEntry.getKey(), newValueList);
                                                    } else {
                                                        // don't compare, set the date to today, and set bool to true.
                                                        newValueList.add(string);
                                                        //newValueMap.get(valueMapEntry.getKey()).add(string);
                                                    }
                                                }
                                            }

                                        newValueMap.put(valueMapEntry.getKey(), newValueList);
                                    }
                                    if (!hasEx) {
                                        // no recorded instance of this ex on this day
                                        instantiateList = true;
                                    }
                                } else {
                                    // no algo completed info at all
                                    instantiateList = true;
                                }
                            }
                        }
                    }



                    if (instantiateList) {
                        // instantiate datemap with this exercise

                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();

                        if (templateClass.getAlgorithmDateMap() == null) {
                            // creates new (first) date map
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put("0_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        } else {
                            // creates new algoDateMap at algoDateMap.size + 1
                            HashMap<String, List<String>> newHashMap = new HashMap<>();
                            newHashMap.putAll(templateClass.getAlgorithmDateMap());
                            String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                    .get(0);
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            newList.add(newDate.toString());
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(newDate.toString());
                            newHashMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newHashMap);
                        }

                    } else {
                        // UPDATE maps (increment)

                        //originalHashmap.put(key, newValueList);
                        DateTime dateTime = new DateTime();
                        int currentWeekday = dateTime.getDayOfWeek();
                        String todayString = templateClass.getMapForDay(intToWeekday(currentWeekday)).get("0_key")
                                .get(0);
                        String bool = "true";

                        originalHashmap.putAll(newValueMap);

                        String oldDate = null;
                        String dateKey = null;

                        for(Map.Entry<String, List<String>> dateMap2 : templateClass.getAlgorithmDateMap().entrySet
                                ()){
                            if(isToday(dateMap2.getValue().get(3))) {
                                if (dateMap2.getValue().get(0).equals(exName)) {
                                    dateKey = dateMap2.getKey();
                                    oldDate = dateMap2.getValue().get(1);
                                }
                            }
                        }

                        if(dateKey != null){
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(!isRunning || oldDate == null || updateOldDate){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(dateKey, newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }else{
                            HashMap<String, List<String>> newMap = new HashMap<>();
                            newMap.putAll(templateClass.getAlgorithmDateMap());
                            List<String> newList = new ArrayList<>();
                            newList.add(exName);
                            if(!isRunning || oldDate == null || updateOldDate){
                                newList.add(LocalDate.now().toString());
                            }else{
                                newList.add(oldDate);
                            }
                            //if(oldDate == null){
                            //    newList.add(LocalDate.now().toString());
                            //}else{
                            //    newList.add(oldDate);
                            //}
                            newList.add("true");
                            newList.add(todayString);
                            newList.add(LocalDate.now().toString());
                            newMap.put(templateClass.getAlgorithmDateMap().size() + "_key", newList);
                            templateClass.setAlgorithmDateMap(newMap);
                        }

                        String mapName = templateClass.getMapNameForDay(intToWeekday(currentWeekday));

                        if (mapName.equals("mMapOne")) {
                            templateClass.setMapOne(originalHashmap);
                        } else if (mapName.equals("mMapTwo")) {
                            templateClass.setMapTwo(originalHashmap);
                        } else if (mapName.equals("mMapThree")) {
                            templateClass.setMapThree(originalHashmap);
                        } else if (mapName.equals("mMapFour")) {
                            templateClass.setMapFour(originalHashmap);
                        } else if (mapName.equals("mMapFive")) {
                            templateClass.setMapFive(originalHashmap);
                        } else if (mapName.equals("mMapSix")) {
                            templateClass.setMapSix(originalHashmap);
                        } else if (mapName.equals("mMapSeven")) {
                            templateClass.setMapSeven(originalHashmap);
                        }

                        Log.i("info", "String");
                    }
                }
                //}
            }
        }
    }

    private int getWeeksSinceLast(String dateString){
        int weeksInt = 0;

        LocalDate oldDate = LocalDate.parse(dateString);
        LocalDate newDate = LocalDate.now();

        int daysBetween = Days.daysBetween(oldDate, newDate).getDays();
        weeksInt = (int) Math.round(daysBetween / 7);

        return weeksInt;
    }

    private double getWeeksBetween(String date1, String date2){
        double weeksInt = 0;

        LocalDate firstDate = LocalDate.parse(date1);
        LocalDate secondDate = LocalDate.parse(date2);

        int daysBetween = Days.daysBetween(firstDate, secondDate).getDays();

        weeksInt = daysBetween / 7.0;

        return weeksInt;
    }

    private boolean isToday(String dayUnformatted){
        boolean todayBool = false;

        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();

        String today = intToWeekday(currentWeekday);

        String delims = "[_]";
        String[] tokens = dayUnformatted.split(delims);

        for(String string : tokens){
            if(string.equals(today)){
                todayBool = true;
            }
        }

        return todayBool;
    }

    private int getTotalPoundage(HashMap<String, List<String>> map, String exName){
        int totalPoundage = 0;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            if(entry.getValue().get(0).equals(exName)){
                for(String string : entry.getValue()){
                    if(!isExerciseName(string)){
                        String delims = "[@,_]";
                        String tokens[] = string.split(delims);

                        int int1;  // java.lang.NumberFormatException: For input
                        // string: "T.F."
                        try{
                            int1 = Integer.parseInt(tokens[0]);
                        }catch (NumberFormatException e){
                            int1 = 1;
                        }
                        int int2;
                        try{
                            int2 = Integer.parseInt(tokens[1]);
                        }catch (NumberFormatException e){
                            int2 = 1;
                        }

                        //if(tokens[1].equals("B.W.")){
                        //    if(userModelClass.isIsImperial()){
                        //        int2 = Integer.parseInt(userModelClass.getPounds());
                        //    }else{
                        //        int2 = Integer.parseInt(userModelClass.getKgs());
                        //    }
                        //}else{
                        //    int2 = Integer.parseInt(tokens[1]);
                        //}
                        int int3 = int1 * int2;
                        totalPoundage = totalPoundage + int3;
                    }
                }
            }
        }

        return totalPoundage;
    }

    private String getExercisesInSupersetList(List<String> list){
        String cat = "";

        int inc = 0;
        for(String string : list){
            if(isExerciseName(string) && inc != 0){
                cat = cat + "_" + string;
            }
            inc++;
        }

        return cat;
    }

    private String getExercisesInSupersetMap(Map<String, List<String>> map){
        String cat = "";

        for(Map.Entry<String, List<String>> subMap : map.entrySet()){
            if(!subMap.getKey().equals("0_key")){
                cat = cat + "_" + subMap.getValue().get(0);
            }
        }

        return cat;
    }

    private String getNamesWithoutInt(String unformatted){
        String name = "";

        String delims = "[_]";
        String[] tokens = unformatted.split(delims);

        for(int i = 0; i < tokens.length - 1; i++){
            name = "_" + name + tokens[i];
        }

        return name;
    }

    private HashMap<String, List<String>> formatModelClass(HashMap<String, List<String>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();

        for(Map.Entry<String, List<String>> entry : map.entrySet()) {
            ArrayList<List<String>> subList = new ArrayList<>();
            if(!entry.getKey().equals("0_key")){
                if(isSupersetList(entry.getValue())){
                    // superset list
                    int inc = 0;
                    int numberOfInstances = 0;
                    String[] tokens = entry.getKey().split("[_]");
                    String parent = "";
                    List<String> childList = new ArrayList<>();
                    for(String string : entry.getValue()){
                        if(isExerciseName(string)){
                            if(inc == 0){
                                // parent ex of superset list
                                List<String> list = new ArrayList<>();
                                String extraExercises = getExercisesInSupersetList(entry.getValue());
                                parent = string;
                                for(Map.Entry<String, List<String>> map2 : formattedMap.entrySet()){
                                    if(getNamesWithoutInt(map2.getValue().get(0)).equals(string + extraExercises)){
                                        numberOfInstances++;
                                    }
                                }
                                list.add(string + extraExercises + String.valueOf(tokens[0]));
                                subList.add(list);
                            }else{
                                // child ex of superset list
                                List<String> list = new ArrayList<>();
                                list.add(string + "_" + parent + "_" + String.valueOf(tokens[0]));
                                subList.add(list);
                                childList.add(string);
                            }
                        }else{
                            List<String> list = expandList(string);
                            subList.get(subList.size() - 1).addAll(list);
                        }
                        inc++;
                    }
                    String newString = "";
                    for(String string : childList){
                        newString = newString + "_" + string;
                    }
                    subList.get(0).set(0, parent + "_" + "p" + newString + "_" + tokens[0]);
                }else{
                    // normal list
                    for(String string : entry.getValue()){
                        if(isExerciseName(string)){
                            List<String> list = new ArrayList<>();
                            list.add(string);
                            subList.add(list);
                        }else{
                            List<String> list = expandList(string);
                            subList.get(subList.size() - 1).addAll(list);
                        }
                    }
                }
            }
            for(List<String> list : subList){
                formattedMap.put(formattedMap.size() + "_key", list);
            }
        }

        return formattedMap;
    }

    private HashMap<String, List<String>> formatCompletedMap(HashMap<String, HashMap<String, List<String>>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();
        ArrayList<String> instanceList = new ArrayList<>();

        for(Map.Entry<String, HashMap<String, List<String>>> subMap : map.entrySet()){
            if(subMap.getValue().size() > 1){
                String[] tokens2 = subMap.getKey().split("[_]");
                String parent = subMap.getValue().get("0_key").get(0);
                List<String> childList = new ArrayList<>();
                int instanceInc = 0;
                for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                    // is superset map
                    boolean isSuperset = false;
                    List<String> subList = new ArrayList<>();
                    for(String string : subHashMap.getValue()){
                        if(isExerciseName(string)){

                            if(subHashMap.getKey().equals("0_key")){
                                String extraExercises = getExercisesInSupersetMap(subMap.getValue());
                                if(instanceList.contains(string + extraExercises)){
                                    instanceInc++;
                                }
                                subList.add(string + "_" + "p" + extraExercises + "_" + String.valueOf(tokens2[0]));
                            }else{
                                subList.add(string + "_" + parent + "_" + String.valueOf(tokens2[0]));
                                childList.add(string);
                            }
                        }else{
                            String delims = "[_]";
                            String[] tokens = string.split(delims);
                            if(tokens[1].equals("checked")){
                                subList.add(tokens[0]);
                            }
                            if(tokens.length > 2){
                                if(tokens[2].equals("ss")){
                                    isSuperset = true;
                                }
                            }
                        }
                    }
                    formattedMap.put(formattedMap.size() + "_key", subList);
                }

            }else{
                // not superset map
                for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                    boolean isSuperset = false;
                    List<String> subList = new ArrayList<>();
                    for(String string : subHashMap.getValue()){
                        if(isExerciseName(string)){
                            subList.add(string);
                        }else{
                            String delims = "[_]";
                            String[] tokens = string.split(delims);
                            if(tokens[1].equals("checked")){
                                subList.add(tokens[0]);
                            }
                            if(tokens.length > 2){
                                if(tokens[2].equals("ss")){
                                    isSuperset = true;
                                }
                            }
                        }
                    }
                    if(isSuperset){
                        String newName = subList.get(0) + "_ss";
                        subList.set(0, newName);
                    }
                    formattedMap.put(formattedMap.size() + "_key", subList);
                }
            }
        }

        return formattedMap;
    }

    private HashMap<String, List<String>> formatCompletedMapNoWeightCheck(HashMap<String, HashMap<String,
            List<String>>> map){
        HashMap<String, List<String>> formattedMap = new HashMap<>();
        ArrayList<String> instanceList = new ArrayList<>();

        for(Map.Entry<String, HashMap<String, List<String>>> subMap : map.entrySet()){
            if(subMap.getValue().size() > 1){
                String[] tokens2 = subMap.getKey().split("[_]");
                String parent = subMap.getValue().get("0_key").get(0);
                List<String> childList = new ArrayList<>();
                int instanceInc = 0;
                for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                    // is superset map
                    boolean isSuperset = false;
                    List<String> subList = new ArrayList<>();
                    for(String string : subHashMap.getValue()){
                        if(isExerciseName(string)){

                            if(subHashMap.getKey().equals("0_key")){
                                String extraExercises = getExercisesInSupersetMap(subMap.getValue());
                                if(instanceList.contains(string + extraExercises)){
                                    instanceInc++;
                                }
                                subList.add(string + "_" + "p" + extraExercises + "_" + String.valueOf(tokens2[0]));
                            }else{
                                subList.add(string + "_" + parent + "_" + String.valueOf(tokens2[0]));
                                childList.add(string);
                            }
                        }else{
                            String delims = "[_]";
                            String[] tokens = string.split(delims);
                            subList.add(tokens[0]);
                            //if(tokens[1].equals("checked")){
                            //    subList.add(tokens[0]);
                            //}
                            //if(tokens.length > 2){
                            //    if(tokens[2].equals("ss")){
                            //        isSuperset = true;
                            //    }
                            //}
                        }
                    }
                    formattedMap.put(formattedMap.size() + "_key", subList);
                }

            }else{
                // not superset map
                for(Map.Entry<String, List<String>> subHashMap : subMap.getValue().entrySet()){
                    boolean isSuperset = false;
                    List<String> subList = new ArrayList<>();
                    for(String string : subHashMap.getValue()){
                        if(isExerciseName(string)){
                            subList.add(string);
                        }else{
                            String delims = "[_]";
                            String[] tokens = string.split(delims);
                            subList.add(tokens[0]);
                            //if(tokens[1].equals("checked")){
                            //    subList.add(tokens[0]);
                            //}
                            //if(tokens.length > 2){
                            //    if(tokens[2].equals("ss")){
                            //        isSuperset = true;
                            //    }
                            //}
                        }
                    }
                    if(isSuperset){
                        String newName = subList.get(0) + "_ss";
                        subList.set(0, newName);
                    }
                    formattedMap.put(formattedMap.size() + "_key", subList);
                }
            }
        }

        return formattedMap;
    }

    private List<String> expandList(String data){
        List<String> returnList = new ArrayList<>();

        String delims = "[x]";
        String tokens[] = data.split(delims);
        int inc = Integer.valueOf(tokens[0]);
        for(int i = 0; i < inc; i++){
            returnList.add(tokens[1]);
        }

        return returnList;
    }

    String intToWeekday(int inc){
        String weekday = "";

        if(inc == 1){
            weekday = "Monday";
        }else if(inc == 2){
            weekday = "Tuesday";
        }else if(inc == 3){
            weekday = "Wednesday";
        }else if(inc == 4){
            weekday = "Thursday";
        }else if(inc == 5){
            weekday = "Friday";
        }else if(inc == 6){
            weekday = "Saturday";
        }else if(inc == 7){
            weekday = "Sunday";
        }

        return weekday;
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
            if(input.length() > 5){
                String string = input.substring(0, 4);
                //String string2 = input.substring(0, 2);
                if(string.equals("T.F.")){
                    isExercise = false;
                }
            }
        }

        return isExercise;
    }


}
