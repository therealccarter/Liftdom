package com.liftdom.workout_assistor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.charts.ChartsAndStatsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.*;

import com.liftdom.liftdom.R;
import com.liftdom.settings.SettingsListActivity;
import com.liftdom.template_editor.SaveTemplateDialog;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.tools.ToolsMainActivity;
import com.liftdom.user_profile.CurrentUserProfile;
import com.liftdom.workout_programs.Smolov.Smolov;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class WorkoutAssistorActivity extends AppCompatActivity {

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

    int ArrayListIterator = 0;



    ArrayList<ExerciseNameFrag> exerciseNameFragList = new ArrayList<>();
    ArrayList<RepsWeightFrag> repsWeightFragList = new ArrayList<>();
    ArrayList<RunningAssistorClass> runningAssistorList = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.journalAndSave) LinearLayout journalAndSave;
    @BindView(R.id.currentTemplateView) TextView currentTemplateView;
    @BindView(R.id.WATitleView) TextView WATitleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_assistor);
        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        ButterKnife.bind(this);


        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        currentTemplateView.setTypeface(lobster);
        WATitleView.setTypeface(lobster);

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, LoginActivity.class));
        }

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    email = user.getEmail();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(WorkoutAssistorActivity.this, LoginActivity.class));
                }

            }
        };
        // [END auth_state_listener]

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Today's Workout");

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //Handle Profile changes
                        return false;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(WorkoutAssistorActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        // create the drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Charts & Stats").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Premium Features").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Tools").withIdentifier(7),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(8)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(WorkoutAssistorActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(WorkoutAssistorActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(WorkoutAssistorActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(WorkoutAssistorActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(WorkoutAssistorActivity.this, ChartsAndStatsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(WorkoutAssistorActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(WorkoutAssistorActivity.this, ToolsMainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 8) {
                                intent = new Intent(WorkoutAssistorActivity.this, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                WorkoutAssistorActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        // Later
        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(this, R.drawable.usertest))
                        .withName
                                (mFirebaseUser.getDisplayName()).withEmail(email),
                0);

        // [END AUTH AND NAV-DRAWER BOILERPLATE]




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
        EditText privateJournalView = (EditText) findViewById(R.id.privateJournal);

        String privateJournal = privateJournalView.getText().toString();

        WorkoutAssistorAssemblerClass.getInstance().privateJournal = privateJournal;

        //TODO: Look at ramifications of below line
        isSavedInstanceBool = false;

    }



    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

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
                                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                        String exName = dataSnapshot1.getKey();

                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                        exerciseNameFrag.exerciseName = exName;
                                        exerciseString = exName;
                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                exerciseNameFrag);
                                        exerciseNameFragList.add(exerciseNameFrag);

                                        if (!isFinishing()) {
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }

                                        DatabaseReference setSchemeRef = runningAssistorRef.child(exName);
                                        setSchemeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                                    String snapshotString = dataSnapshot2.getValue(String.class);

                                                    String stringSansSpaces = snapshotString.replaceAll("\\s+", "");

                                                    String delims = "[x,@,_]";

                                                    String[] tokens = stringSansSpaces.split(delims);

                                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                            .beginTransaction();
                                                    RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                    repsWeightFrag.parentExercise = exerciseString;
                                                    repsWeightFrag.reps = tokens[1];
                                                    repsWeightFrag.weight = tokens[2];
                                                    repsWeightFrag.fullString = snapshotString;
                                                    boolean isCheckedBool = Boolean.parseBoolean(tokens[3]);
                                                    repsWeightFrag.isCheckbox = isCheckedBool;

                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                            repsWeightFrag);

                                                    repsWeightFragList.add(repsWeightFrag);

                                                    if (!isFinishing()) {
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
                                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                if (!isFinishing()) {
                                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                                }
                                                                            } else if (routine.equals("build to 1rm")) {

                                                                            } else if (routine.equals("switching phase")) {

                                                                            } else {
                                                                                if (isExerciseName(routine)) {
                                                                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                                            .beginTransaction();
                                                                                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            exerciseNameFrag);
                                                                                    if (!isFinishing()) {
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
                                                                                                getSupportFragmentManager();
                                                                                        FragmentTransaction fragmentTransaction =
                                                                                                fragmentManager
                                                                                                        .beginTransaction();
                                                                                        RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                                repsWeightFrag);
                                                                                        if (!isFinishing()) {
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
                                                                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                                            .beginTransaction();
                                                                                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                    exerciseNameFrag.exerciseName = snapshotString;
                                                                                    exerciseString = snapshotString;
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            exerciseNameFrag);
                                                                                    exerciseNameFragList.add(exerciseNameFrag);

                                                                                    if (!isFinishing()) {
                                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                                    }
                                                                                } else {
                                                                                    String stringSansSpaces = snapshotString.replaceAll("\\s+", "");

                                                                                    String delims = "[x,@]";

                                                                                    String[] tokens = stringSansSpaces.split(delims);

                                                                                    int setAmount = Integer.parseInt(tokens[0]);

                                                                                    for (int i = 0; i < setAmount; i++) {
                                                                                        FragmentManager fragmentManager = getSupportFragmentManager();
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

                                                                                        if (!isFinishing()) {
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
                                                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                                            .beginTransaction();
                                                                    RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                            exerciseNameFrag);
                                                                    if (!isFinishing()) {
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
                                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                                    FragmentTransaction fragmentTransaction = fragmentManager
                                                            .beginTransaction();
                                                    NoActiveTemplateFrag exerciseNameFrag = new NoActiveTemplateFrag();
                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                            exerciseNameFrag);
                                                    fragmentTransaction.commitAllowingStateLoss();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                    } else {
                                        FragmentManager fragmentManager = getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager
                                                .beginTransaction();
                                        WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();

                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                workoutFinishedFrag);
                                        if (!isFinishing()) {
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
                                                                            FragmentManager fragmentManager = getSupportFragmentManager();
                                                                            FragmentTransaction fragmentTransaction = fragmentManager
                                                                                    .beginTransaction();
                                                                            RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                            fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                    exerciseNameFrag);
                                                                            if (!isFinishing()) {
                                                                                fragmentTransaction.commitAllowingStateLoss();
                                                                            }
                                                                        } else if (routine.equals("build to 1rm")) {

                                                                        } else if (routine.equals("switching phase")) {

                                                                        } else {
                                                                            if (isExerciseName(routine)) {
                                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                if (!isFinishing()) {
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
                                                                                            getSupportFragmentManager();
                                                                                    FragmentTransaction fragmentTransaction =
                                                                                            fragmentManager
                                                                                                    .beginTransaction();
                                                                                    RepsWeightFrag repsWeightFrag = new RepsWeightFrag();
                                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                            repsWeightFrag);
                                                                                    if (!isFinishing()) {
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
                                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                                        .beginTransaction();
                                                                                ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                                                                                exerciseNameFrag.exerciseName = snapshotString;
                                                                                exerciseString = snapshotString;
                                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                        exerciseNameFrag);
                                                                                exerciseNameFragList.add(exerciseNameFrag);

                                                                                if (!isFinishing()) {
                                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                                }
                                                                            } else {
                                                                                String stringSansSpaces = snapshotString.replaceAll("\\s+", "");

                                                                                String delims = "[x,@]";

                                                                                String[] tokens = stringSansSpaces.split(delims);

                                                                                int setAmount = Integer.parseInt(tokens[0]);

                                                                                for (int i = 0; i < setAmount; i++) {
                                                                                    FragmentManager fragmentManager = getSupportFragmentManager();
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

                                                                                    if (!isFinishing()) {
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
                                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                                        .beginTransaction();
                                                                RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                        exerciseNameFrag);
                                                                if (!isFinishing()) {
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
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                        .beginTransaction();
                                                NoActiveTemplateFrag exerciseNameFrag = new NoActiveTemplateFrag();
                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                        exerciseNameFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                } else {
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager
                                            .beginTransaction();
                                    WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();

                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                            workoutFinishedFrag);
                                    if (!isFinishing()) {
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

        DatabaseReference runningBoolRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                ("isRunningBoolDate");
        runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());

        //DatabaseReference runningDateRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
        //        ("isRunningDate");
//
        //String date = LocalDate.now().toString();
//
        //runningDateRef.setValue(date);

        //TODO: Debug the repeating boolean concat...may be solved simply by erasing current data

        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                ("isRunningInfo");

        for(ExerciseNameFrag exNameFrag : exerciseNameFragList){
            String exName = exNameFrag.exerciseName;
            DatabaseReference exNameRef = runningAssistorRef.child(exName);
            int inc = 0;
            ArrayList<String> exerciseStringList = new ArrayList<>();

            for(RepsWeightFrag repsWeightFrag : repsWeightFragList){
                if (repsWeightFrag.getParentExercise().equals(exName)) {
                    String infoString = repsWeightFrag.fullString + "_" + repsWeightFrag.getCheckedStatus();
                    exerciseStringList.add(infoString);
                }

                inc++;

                if(inc == repsWeightFragList.size()){
                    List<String> list = exerciseStringList;
                    exNameRef.setValue(list);
                }


            }
        }

        //AsyncTask.execute(new Runnable() {
        //    @Override
        //    public void run() {
        //
        //    }
        //});




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
