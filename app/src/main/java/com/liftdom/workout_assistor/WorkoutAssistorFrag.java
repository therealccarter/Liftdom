package com.liftdom.workout_assistor;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.Smolov.Smolov;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutAssistorFrag extends Fragment {


    public WorkoutAssistorFrag() {
        // Required empty public constructor
    }

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String email = "error";

    String activeTemplateName = null;
    String activeTemplateDayValue = null;
    String activeTemplateToday = null;
    String exerciseString = "fail";

    Boolean isSavedInstanceBool;
    Boolean isRunningAssistor = false;
    Boolean isRunningDate = false;

    Boolean noActiveTemplateBool = false;

    int ArrayListIterator = 0;

    //boolean firstEx = true;
    //boolean setSchemesFinished = true;

    int assistorInfoInc = 0;
    ArrayList<ArrayList<String>> assistorInfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<String> assistorInfoRunningList = new ArrayList<>();

    ArrayList<ExerciseNameFrag> exerciseNameFragList = new ArrayList<>();
    ArrayList<RepsWeightFrag> repsWeightFragList = new ArrayList<>();
    ArrayList<RunningAssistorClass> runningAssistorList = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.journalAndSave) LinearLayout journalAndSave;
    @BindView(R.id.currentTemplateView) TextView currentTemplateView;
    //@BindView(R.id.WATitleView) TextView WATitleView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_assistor, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        currentTemplateView.setTypeface(lobster);

        if(savedInstanceState == null){
            isSavedInstanceBool = true;
        }else{
            isSavedInstanceBool = false;
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaveAssistorDialog.class);

                startActivity(intent);
            }
        });
        
        return view;
    }

    ArrayList<String> dayFormat(String dayString){
        ArrayList<String> daysFormmatted = new ArrayList<>();
        if(dayString != null) {
            if(!dayString.equals("algorithm") || !dayString.equals("algorithmExercises")){
                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                //Date d = new Date();
                //String currentDoW = sdf.format(d);

                String delims = "[_]";
                String[] tokens = dayString.split(delims);

                for (String day : tokens) {
                    daysFormmatted.add(day);
                }
            }
        }
        return daysFormmatted;

    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

    @Override
    public void onPause() {
        super.onPause();
        EditText privateJournalView = (EditText) getActivity().findViewById(R.id.privateJournal);

        String privateJournal = privateJournalView.getText().toString();

        WorkoutAssistorAssemblerClass.getInstance().privateJournal = privateJournal;

        //TODO: Look at ramifications of below line
        isSavedInstanceBool = false;

    }

    /**
     * So the problem is probably sequential issues. We should wrap the running info frags in incrementors
     *
     */


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();


        if(isSavedInstanceBool) {

            //DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy");
            //dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
            //String gmtTime = dateFormat.format(new Date());

            //TODO: Disallow certain official names like Smolov so we can easily do identification.

            String date = LocalDate.now().toString();

            DatabaseReference workoutHistoryRef = mRootRef.child("workout_history").child(uid);

            final DatabaseReference specificDate = workoutHistoryRef.child(date);

            // Perhaps we can implement a dynamic saving system that updates to the db?

            // So we'll need to set this to true onPause/onDestroy
            // And we'll check here if it's set true or not
            // if it is, then in onStart we'll divert to its node

            DatabaseReference runningBoolDateRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                    ("isRunningBoolDate");

            runningBoolDateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);

                    if(value != null){
                        String delims = "[_]";

                        String[] tokens1 = value.split(delims);

                        LocalDate localDate = LocalDate.now();
                        LocalDate convertedDate = new LocalDate(tokens1[1]);

                        if(Boolean.valueOf(tokens1[0]) && localDate.equals(convertedDate)){
                            final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                            activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    activeTemplateName = dataSnapshot.getValue(String.class);
                                    currentTemplateView.setText(activeTemplateName);
                                    WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                            journalAndSave.setVisibility(View.VISIBLE);

                            final DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                                    ("isRunningInfo");

                            runningAssistorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    for(final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){

                                        String exName = dataSnapshot1.getKey();

                                        DatabaseReference setSchemeRef = runningAssistorRef.child(exName);
                                        setSchemeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                    String snapshotString = dataSnapshot2.getValue(String.class);

                                                    String exName = dataSnapshot1.getKey();

                                                    assistorInfoRunningList.add(snapshotString);

                                                    ++assistorInfoInc;

                                                    if(assistorInfoInc == dataSnapshot.getChildrenCount()){

                                                        if(exName.equals("Leg Extension")){
                                                            Log.i("info", "what");
                                                        }

                                                        FragmentManager fragmentManager = 
                                                                getActivity().getSupportFragmentManager();
                                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                                .beginTransaction();
                                                        ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                        exerciseNameFrag.exerciseName = exName;
                                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                exerciseNameFrag);
                                                        exerciseNameFragList.add(exerciseNameFrag);

                                                        if (!getActivity().isFinishing()) {
                                                            fragmentTransaction.commitAllowingStateLoss();
                                                        }

                                                        for(String string : assistorInfoRunningList){
                                                            String stringSansSpaces = string.replaceAll("\\s+", "");

                                                            String delims = "[x,@,_]";

                                                            String[] tokens = stringSansSpaces.split(delims);

                                                            FragmentManager fragmentManager2 =
                                                                    getActivity().getSupportFragmentManager();
                                                            FragmentTransaction fragmentTransaction2 = fragmentManager2
                                                                    .beginTransaction();
                                                            RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                            repsWeightFrag.parentExercise = exName;
                                                            repsWeightFrag.reps = tokens[1];
                                                            repsWeightFrag.weight = tokens[2];
                                                            repsWeightFrag.fullString = tokens[0] + " x " + tokens[1] + " @ "
                                                                    + tokens[2];
                                                            boolean isCheckedBool = Boolean.parseBoolean(tokens[3]);
                                                            repsWeightFrag.isCheckbox = isCheckedBool;

                                                            fragmentTransaction2.add(R.id.eachExerciseFragHolder,
                                                                    repsWeightFrag);

                                                            repsWeightFragList.add(repsWeightFrag);

                                                            if (!getActivity().isFinishing()) {
                                                                fragmentTransaction2.commitAllowingStateLoss();

                                                            }
                                                        }
                                                        assistorInfoRunningList.clear();
                                                        assistorInfoInc = 0;
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
                        }else{
                            specificDate.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.getValue() == null) {
                                        // Get the name of the active template
                                        // child is "yeee"
                                        final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                                        activeTemplateRef.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {

                                                activeTemplateName = dataSnapshot.getValue(String.class);
                                                currentTemplateView.setText(activeTemplateName);

                                                if (activeTemplateName != null) {
                                                    if (smolovChecker(activeTemplateName)) {
                                                        // SMOLOV WORKOUT ASSISTOR
                                                        WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                                        final DatabaseReference smolovDataRef = mRootRef.child("templates").child(uid)
                                                                .child(activeTemplateName);

                                                        smolovDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                int incrementor = 0;
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                    if (dataSnapshot1.getKey().equals("1rm")) {
                                                                        WorkoutAssistorAssemblerClass.getInstance().oneRM = (double)
                                                                                Integer.parseInt
                                                                                        (dataSnapshot1.getValue(String
                                                                                                .class));
                                                                    } else if (dataSnapshot1.getKey().equals("exName")) {
                                                                        WorkoutAssistorAssemblerClass.getInstance().exName = dataSnapshot1
                                                                                .getValue(String.class);
                                                                    } else if (dataSnapshot1.getKey().equals("timeStamp")) {
                                                                        WorkoutAssistorAssemblerClass.getInstance().localDate = LocalDate
                                                                                .parse(dataSnapshot1.getValue(String
                                                                                        .class));
                                                                    }

                                                                    incrementor++;

                                                                    if (incrementor == 4) {
                                                                        ArrayList<Integer> weekAndDays = getWeekAndDaySmolov
                                                                                (WorkoutAssistorAssemblerClass.getInstance
                                                                                        ().localDate);

                                                                        int weeks = weekAndDays.get(0);
                                                                        int days = weekAndDays.get(1) + 1;
                                                                        double oneRM = WorkoutAssistorAssemblerClass.getInstance()
                                                                                .oneRM;

                                                                        Smolov smolov = new Smolov(weeks, days, oneRM);

                                                                        ArrayList<String> smolovWorkout = smolov.getWorkout();

                                                                        for (String routine : smolovWorkout) {
                                                                            if (routine.equals("rest")) {
                                                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                if (!getActivity().isFinishing()) {
                                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                                }
                                                                            } else if (routine.equals("build to 1rm")) {

                                                                            } else if (routine.equals("switching phase")) {

                                                                            } else {
                                                                                if (isExerciseName(routine)) {
                                                                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                                            .beginTransaction();
                                                                                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            exerciseNameFrag);
                                                                                    if (!getActivity().isFinishing()) {
                                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                                    }
                                                                                    exerciseNameFrag.exerciseName = WorkoutAssistorAssemblerClass.getInstance().exName;
                                                                                    exerciseString = WorkoutAssistorAssemblerClass.getInstance().exName;
                                                                                } else {
                                                                                    String delims = "[x,@]";

                                                                                    String[] tokens = routine.split(delims);

                                                                                    int setAmount = Integer.parseInt(tokens[0]);

                                                                                    for (int i = 0; i < setAmount; i++) {
                                                                                        FragmentManager fragmentManager =
                                                                                                getActivity().getSupportFragmentManager();
                                                                                        FragmentTransaction fragmentTransaction =
                                                                                                fragmentManager
                                                                                                        .beginTransaction();
                                                                                        RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                                repsWeightFrag);
                                                                                        if (!getActivity().isFinishing()) {
                                                                                            fragmentTransaction.commitAllowingStateLoss();
                                                                                        }
                                                                                        repsWeightFrag.parentExercise = exerciseString;
                                                                                        repsWeightFrag.reps = tokens[1];
                                                                                        repsWeightFrag.weight = tokens[2];
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


                                                    } else {

                                                        WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                                        // now we're in "yeee."
                                                        final DatabaseReference activeTemplateDataRef = mRootRef.child("templates").child(uid)
                                                                .child(activeTemplateName);

                                                        // find the matching day
                                                        activeTemplateDataRef.addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                ArrayList<String> allDays = new ArrayList<String>();
                                                                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                                                Date d = new Date();
                                                                String currentDay = sdf.format(d);

                                                                String activeTemplateToday = null;
                                                                boolean hasDay = false;


                                                                for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                                                                    // Here we're looking at each DoW entry ("Monday_Thursday", "Tuesday_Saturday", etc)

                                                                    // I think the problem is we're deciding for each one. What we need
                                                                    // to do is gather them all and compare.

                                                                    activeTemplateDayValue = templateSnapshot.getKey();
                                                                    allDays.add(activeTemplateDayValue);
                                                                    ++ArrayListIterator;
                                                                }

                                                                for (String dayUnFormatted : allDays) {
                                                                    ArrayList<String> daysFormatted = dayFormat(dayUnFormatted);

                                                                    for (String dayFormatted : daysFormatted) {
                                                                        if (dayFormatted.equals(currentDay)) {
                                                                            activeTemplateToday = dayUnFormatted;
                                                                            hasDay = true;
                                                                        }
                                                                    }
                                                                }

                                                                if (hasDay) {
                                                                    DatabaseReference activeDayRef = activeTemplateDataRef.child(activeTemplateToday);
                                                                    activeDayRef.addValueEventListener(new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                            for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                                                                                String snapshotString = daySnapshot.getValue(String.class);

                                                                                if (isExerciseName(snapshotString)) {
                                                                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                                            .beginTransaction();
                                                                                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                    exerciseNameFrag.exerciseName = snapshotString;
                                                                                    exerciseString = snapshotString;
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            exerciseNameFrag);
                                                                                    exerciseNameFragList.add(exerciseNameFrag);

                                                                                    if (!getActivity().isFinishing()) {
                                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                                    }
                                                                                } else {
                                                                                    String stringSansSpaces = snapshotString.replaceAll("\\s+", "");

                                                                                    String delims = "[x,@]";

                                                                                    String[] tokens = stringSansSpaces.split(delims);

                                                                                    int setAmount = Integer.parseInt(tokens[0]);

                                                                                    for (int i = 0; i < setAmount; i++) {
                                                                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                                                                .beginTransaction();
                                                                                        RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                        repsWeightFrag.parentExercise = exerciseString;
                                                                                        repsWeightFrag.reps = tokens[1];
                                                                                        repsWeightFrag.weight = tokens[2];
                                                                                        repsWeightFrag.fullString = snapshotString;
                                                                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                                repsWeightFrag);
                                                                                        repsWeightFragList.add(repsWeightFrag);

                                                                                        if (!getActivity().isFinishing()) {
                                                                                            fragmentTransaction.commitAllowingStateLoss();
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    });
                                                                    journalAndSave.setVisibility(View.VISIBLE);
                                                                } else {
                                                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                            .beginTransaction();
                                                                    RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                            exerciseNameFrag);
                                                                    if (!getActivity().isFinishing()) {
                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                    }

                                                                }

                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        });
                                                    }
                                                } else {
                                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                            .beginTransaction();
                                                    NoActiveTemplateFrag exerciseNameFrag = new NoActiveTemplateFrag();
                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                            exerciseNameFrag);
                                                    if (!getActivity().isFinishing()) {
                                                        fragmentTransaction.commitAllowingStateLoss();
                                                    }
                                                    noActiveTemplateBool = true;
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {

                                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid)
                                                .child("active_template");
                                        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                String value = dataSnapshot.getValue(String.class);
                                                if(value != null){
                                                    currentTemplateView.setText(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();

                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.replace(R.id.eachExerciseFragHolder,
                                                    workoutFinishedFrag);
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }


                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }else{
                        specificDate.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.getValue() == null) {
                                    // Get the name of the active template
                                    // child is "yeee"
                                    final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                                    activeTemplateRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                            activeTemplateName = dataSnapshot.getValue(String.class);
                                            currentTemplateView.setText(activeTemplateName);

                                            if (activeTemplateName != null) {
                                                if (smolovChecker(activeTemplateName)) {
                                                    // SMOLOV WORKOUT ASSISTOR
                                                    WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                                    final DatabaseReference smolovDataRef = mRootRef.child("templates").child(uid)
                                                            .child(activeTemplateName);

                                                    smolovDataRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            int incrementor = 0;
                                                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {

                                                                if (dataSnapshot1.getKey().equals("1rm")) {
                                                                    WorkoutAssistorAssemblerClass.getInstance().oneRM = (double)
                                                                            Integer.parseInt
                                                                                    (dataSnapshot1.getValue(String
                                                                                            .class));
                                                                } else if (dataSnapshot1.getKey().equals("exName")) {
                                                                    WorkoutAssistorAssemblerClass.getInstance().exName = dataSnapshot1
                                                                            .getValue(String.class);
                                                                } else if (dataSnapshot1.getKey().equals("timeStamp")) {
                                                                    WorkoutAssistorAssemblerClass.getInstance().localDate = LocalDate
                                                                            .parse(dataSnapshot1.getValue(String
                                                                                    .class));
                                                                }

                                                                incrementor++;

                                                                if (incrementor == 4) {
                                                                    ArrayList<Integer> weekAndDays = getWeekAndDaySmolov
                                                                            (WorkoutAssistorAssemblerClass.getInstance
                                                                                    ().localDate);

                                                                    int weeks = weekAndDays.get(0);
                                                                    int days = weekAndDays.get(1) + 1;
                                                                    double oneRM = WorkoutAssistorAssemblerClass.getInstance()
                                                                            .oneRM;

                                                                    Smolov smolov = new Smolov(weeks, days, oneRM);

                                                                    ArrayList<String> smolovWorkout = smolov.getWorkout();

                                                                    for (String routine : smolovWorkout) {
                                                                        if (routine.equals("rest")) {
                                                                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                                                    .beginTransaction();
                                                                            RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                            fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                    exerciseNameFrag);
                                                                            if (!getActivity().isFinishing()) {
                                                                                fragmentTransaction.commitAllowingStateLoss();
                                                                            }
                                                                        } else if (routine.equals("build to 1rm")) {

                                                                        } else if (routine.equals("switching phase")) {

                                                                        } else {
                                                                            if (isExerciseName(routine)) {
                                                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                if (!getActivity().isFinishing()) {
                                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                                }
                                                                                exerciseNameFrag.exerciseName = WorkoutAssistorAssemblerClass.getInstance().exName;
                                                                                exerciseString = WorkoutAssistorAssemblerClass.getInstance().exName;
                                                                            } else {
                                                                                String delims = "[x,@]";

                                                                                String[] tokens = routine.split(delims);

                                                                                int setAmount = Integer.parseInt(tokens[0]);

                                                                                for (int i = 0; i < setAmount; i++) {
                                                                                    FragmentManager fragmentManager =
                                                                                            getActivity().getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction =
                                                                                            fragmentManager
                                                                                                    .beginTransaction();
                                                                                    RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            repsWeightFrag);
                                                                                    if (!getActivity().isFinishing()) {
                                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                                    }
                                                                                    repsWeightFrag.parentExercise = exerciseString;
                                                                                    repsWeightFrag.reps = tokens[1];
                                                                                    repsWeightFrag.weight = tokens[2];
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


                                                } else {

                                                    WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                                    // now we're in "yeee."
                                                    final DatabaseReference activeTemplateDataRef = mRootRef.child("templates").child(uid)
                                                            .child(activeTemplateName);

                                                    // find the matching day
                                                    activeTemplateDataRef.addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                            ArrayList<String> allDays = new ArrayList<String>();
                                                            SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                                                            Date d = new Date();
                                                            String currentDay = sdf.format(d);

                                                            String activeTemplateToday = null;
                                                            boolean hasDay = false;


                                                            for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                                                                // Here we're looking at each DoW entry ("Monday_Thursday", "Tuesday_Saturday", etc)

                                                                // I think the problem is we're deciding for each one. What we need
                                                                // to do is gather them all and compare.

                                                                activeTemplateDayValue = templateSnapshot.getKey();
                                                                allDays.add(activeTemplateDayValue);
                                                                ++ArrayListIterator;
                                                            }

                                                            for (String dayUnFormatted : allDays) {
                                                                ArrayList<String> daysFormatted = dayFormat(dayUnFormatted);

                                                                for (String dayFormatted : daysFormatted) {
                                                                    if (dayFormatted.equals(currentDay)) {
                                                                        activeTemplateToday = dayUnFormatted;
                                                                        hasDay = true;
                                                                    }
                                                                }
                                                            }

                                                            if (hasDay) {
                                                                DatabaseReference activeDayRef = activeTemplateDataRef.child(activeTemplateToday);
                                                                activeDayRef.addValueEventListener(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        for (DataSnapshot daySnapshot : dataSnapshot.getChildren()) {
                                                                            String snapshotString = daySnapshot.getValue(String.class);

                                                                            if (isExerciseName(snapshotString)) {
                                                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                exerciseNameFrag.exerciseName = snapshotString;
                                                                                exerciseString = snapshotString;
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                exerciseNameFragList.add(exerciseNameFrag);

                                                                                if (!getActivity().isFinishing()) {
                                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                                }
                                                                            } else {
                                                                                String stringSansSpaces = snapshotString.replaceAll("\\s+", "");

                                                                                String delims = "[x,@]";

                                                                                String[] tokens = stringSansSpaces.split(delims);

                                                                                int setAmount = Integer.parseInt(tokens[0]);

                                                                                for (int i = 0; i < setAmount; i++) {
                                                                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                                            .beginTransaction();
                                                                                    RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                    repsWeightFrag.parentExercise = exerciseString;
                                                                                    repsWeightFrag.reps = tokens[1];
                                                                                    repsWeightFrag.weight = tokens[2];
                                                                                    repsWeightFrag.fullString = snapshotString;
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            repsWeightFrag);
                                                                                    repsWeightFragList.add(repsWeightFrag);

                                                                                    if (!getActivity().isFinishing()) {
                                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                });
                                                                journalAndSave.setVisibility(View.VISIBLE);
                                                            } else {
                                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                        .beginTransaction();
                                                                RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                        exerciseNameFrag);
                                                                if (!getActivity().isFinishing()) {
                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                }

                                                            }

                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                }
                                            } else {
                                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                        .beginTransaction();
                                                NoActiveTemplateFrag exerciseNameFrag = new NoActiveTemplateFrag();
                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                        exerciseNameFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                                noActiveTemplateBool = true;
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {

                                    DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid)
                                            .child("active_template");
                                    activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            String value = dataSnapshot.getValue(String.class);
                                            if(value != null){
                                                currentTemplateView.setText(value);
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();


                                    if (!getActivity().isFinishing()) {
                                        fragmentTransaction.replace(R.id.eachExerciseFragHolder,
                                                workoutFinishedFrag);
                                        fragmentTransaction.commitAllowingStateLoss();
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

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        if(!noActiveTemplateBool){

            final DatabaseReference runningBoolRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                    ("isRunningBoolDate");

            runningBoolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);


                    if (value == null) {
                        runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                    } else {
                        String delims = "[_]";

                        String[] tokens1 = value.split(delims);

                        LocalDate localDate = LocalDate.now();
                        LocalDate convertedDate = new LocalDate(tokens1[1]);

                        // (isEditing, isDate)
                        // (false, true) = workout has been finished - do nothing
                        // (true, true) = workout is being edited - do nothing
                        // (false, false) = workout has not been finished - set to (true, true)
                        // (true, false) = yesterday's workout not finished - set to (true, true)
                        if (!Boolean.valueOf(tokens1[0]) && !localDate.equals(convertedDate)) {
                            runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                        } else if (Boolean.valueOf(tokens1[0]) && !localDate.equals(convertedDate)) {
                            runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                    ("isRunningInfo");

            runningAssistorRef.setValue(null);

            for (ExerciseNameFrag exNameFrag : exerciseNameFragList) {
                String exName = exNameFrag.exerciseName;
                DatabaseReference exNameRef = runningAssistorRef.child(exName);
                int inc = 0;
                ArrayList<String> exerciseStringList = new ArrayList<>();

                for (RepsWeightFrag repsWeightFrag : repsWeightFragList) {
                    if (repsWeightFrag.getParentExercise().equals(exName)) {
                        String infoString = repsWeightFrag.fullString + "_" + repsWeightFrag.getCheckedStatus();
                        exerciseStringList.add(infoString);
                    }

                    inc++;

                    if (inc == repsWeightFragList.size()) {
                        List<String> list = exerciseStringList;
                        exNameRef.setValue(list);
                    }


                }
            }
        }
    }
    // [END on_stop_remove_listener]

    //TODO: Will have to decide whether to keep this method or do it via the type tag...
    boolean smolovChecker(String templateName){
        boolean isSmolov = false;

        try{
            if(templateName.substring(0,6).equals("Smolov")){
                isSmolov = true;
            }
        } catch (StringIndexOutOfBoundsException e){
            isSmolov = false;
        }


        return isSmolov;
    }

    ArrayList<Integer> getWeekAndDaySmolov(LocalDate oldDate){
        ArrayList<Integer> arrayList = new ArrayList<>();

        LocalDate newDate = LocalDate.now();

        int daysBetween = Days.daysBetween(oldDate, newDate).getDays();

        int weeks = (int) Math.round(daysBetween / 7);
        arrayList.add(weeks);
        int days = daysBetween % 7;
        arrayList.add(days);


        return arrayList;
    }
    

}