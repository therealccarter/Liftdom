package com.liftdom.workout_assistor;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
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
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_editor.SaveTemplateDialog;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.user_profile.CurrentUserProfile;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
            startActivity(new Intent(this, SignInActivity.class));
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
                    startActivity(new Intent(WorkoutAssistorActivity.this, SignInActivity.class));
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
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Tools").withIdentifier(4),
                        new SecondaryDrawerItem().withName("Exercise Academy (Info)").withIdentifier(5)
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
        }


        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaveAssistorDialog.class);
                startActivity(intent);
            }
        });
    }


    boolean isToday(String dayString){
        boolean today = false;
        if(dayString != null) {
            if(!dayString.equals("algorithm") || !dayString.equals("algorithmExercises")){
                SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                Date d = new Date();
                String currentDoW = sdf.format(d);

                String delims = "[_]";
                String[] tokens = dayString.split(delims);

                for (String day : tokens) {
                    if (day.equals(currentDoW)) {
                        today = true;
                    }
                }
            }
        }
        return today;

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

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        if(isSavedInstanceBool) {

            DateFormat dateFormat = new SimpleDateFormat("MM:dd:yyyy");
            dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
            String gmtTime = dateFormat.format(new Date());


            DatabaseReference workoutHistoryRef = mRootRef.child("workout_history").child(uid);

            DatabaseReference specificDate = workoutHistoryRef.child(gmtTime);

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

                                    WorkoutAssistorAssemblerClass.getInstance().templateName = activeTemplateName;
                                    // now we're in "yeee."
                                    final DatabaseReference activeTemplateDataRef = mRootRef.child("templates").child(uid)
                                            .child(activeTemplateName);

                                    // find the matching day
                                    activeTemplateDataRef.addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                                                // Here we're looking at each DoW entry ("Monday_Thursday", "Tuesday_Saturday", etc)

                                                activeTemplateDayValue = templateSnapshot.getKey();

                                                if (isToday(activeTemplateDayValue)) {
                                                    // see if any DoW entries match the
                                                    activeTemplateToday = activeTemplateDayValue;

                                                    DatabaseReference activeDayRef = activeTemplateDataRef.child(activeTemplateDayValue);
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
                                                                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                            exerciseNameFrag);
                                                                    if (!isFinishing()) {
                                                                        fragmentTransaction.commitAllowingStateLoss();
                                                                    }
                                                                    exerciseNameFrag.exerciseName = snapshotString;
                                                                    exerciseString = snapshotString;
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
                                                                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                                                repsWeightFrag);
                                                                        if (!isFinishing()) {
                                                                            fragmentTransaction.commitAllowingStateLoss();
                                                                        }
                                                                        repsWeightFrag.parentExercise = exerciseString;
                                                                        repsWeightFrag.reps = tokens[1];
                                                                        repsWeightFrag.weight = tokens[2];
                                                                        repsWeightFrag.fullString = snapshotString;
                                                                    }
                                                                }
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(DatabaseError databaseError) {

                                                        }
                                                    });
                                                    journalAndSave.setVisibility(View.VISIBLE);
                                                }
                                            }
                                            if (!isToday(activeTemplateDayValue)) {
                                                FragmentManager fragmentManager = getSupportFragmentManager();
                                                FragmentTransaction fragmentTransaction = fragmentManager
                                                        .beginTransaction();
                                                RestDayFrag exerciseNameFrag = new RestDayFrag();
                                                fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                                        exerciseNameFrag);
                                                if(!isFinishing()) {
                                                    fragmentTransaction.commitAllowingStateLoss();
                                                }
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
                        if(!isFinishing()){
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
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]
}
