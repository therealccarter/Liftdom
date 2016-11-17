package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.workout_assistor.ExerciseNameFrag;
import com.liftdom.workout_assistor.RepsWeightFrag;
import com.liftdom.workout_assistor.WorkoutAssistorActivity;
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

public class TemplateEditorActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    int fragIdCount = 0;


    String activeTemplateName = null;
    String selectedTemplateDayValue = null;
    String activeTemplateToday = null;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // butterknife
    @BindView(R.id.addDay)
    Button addDay;
    @BindView(R.id.removeDay)
    Button removeDay;
    @BindView(R.id.saveButton)
    Button onSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_editor);

        //TODO: Edit template adds setScheme from other exercise...


        ButterKnife.bind(this);

        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mAuth = FirebaseAuth.getInstance();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(TemplateEditorActivity.this, SignInActivity.class));
                }

            }
        };
        // [END auth_state_listener]


        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Template Editor");

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //Handle Profile changes
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
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("test1"),
                        new SecondaryDrawerItem().withName("test2")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(TemplateEditorActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(TemplateEditorActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(TemplateEditorActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (intent != null) {
                                TemplateEditorActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        // Later
        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(this, R.drawable.usertest))
                        .withName
                                ("Username").withEmail(email),
                0);

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================

        if (getIntent().getExtras().getString("isEdit") != null) {
            if (getIntent().getExtras().getString("isEdit").equals("yes")) {

                EditTemplateAssemblerClass.getInstance().clearAllLists();

                final String templateName = getIntent().getExtras().getString("templateName");

                /**
                 * Alright, this is what we have to do:
                 * 1. Get a path to the selected template
                 * 2. Get a path to each DayGroup
                 * 3. For each DayGroup, create a DoWLevel frag, passing in its selected days and each exercise/value
                 */

                // 1. Get a path to the selected template
                // "realll"
                final DatabaseReference selectedTemplateDataRef = mRootRef.child("templates").child(uid).child(templateName);

                // find the matching day
                selectedTemplateDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                            // Here we're looking at each DoW entry ("Monday_Thursday", "Tuesday_Saturday", etc)
                            // Thursday_
                            selectedTemplateDayValue = templateSnapshot.getKey();

                            String[] dayStringArray = selectedTemplateDayValue.split("_");

                            ++fragIdCount;
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            String fragString = Integer.toString(fragIdCount);

                            DayOfWeekChildFrag dayOfWeekChildFrag = new DayOfWeekChildFrag();
                            dayOfWeekChildFrag.isEdit = true;
                            dayOfWeekChildFrag.daysArray = dayStringArray.clone();
                            dayOfWeekChildFrag.selectedDaysReference = selectedTemplateDayValue;
                            dayOfWeekChildFrag.templateName = templateName;

                            fragmentTransaction.add(R.id.templateFragmentLayout, dayOfWeekChildFrag, fragString);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }



        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);
                DayOfWeekChildFrag frag2 = new DayOfWeekChildFrag();
                fragmentTransaction.add(R.id.templateFragmentLayout, frag2, fragString);
                fragmentTransaction.commit();
            }
        });

        removeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);
                if (fragIdCount != 0) {
                    fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                    --fragIdCount;
                }
            }
        });


        onSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaveTemplateDialog.class);

                if(getIntent().getExtras().getString("isEdit") != null) {
                    if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                        String templateName = getIntent().getExtras().getString("templateName");
                        intent.putExtra("isEdit", "yes");
                        intent.putExtra("templateName", templateName);
                        startActivity(intent);
                    }
                }else{
                    intent.putExtra("isEdit", "no");
                    startActivity(intent);
                }
            }
        });

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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
