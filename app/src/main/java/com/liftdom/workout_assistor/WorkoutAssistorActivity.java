package com.liftdom.workout_assistor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_housing.TemplateHousingActivity;
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

import java.util.ArrayList;
import java.util.List;

public class WorkoutAssistorActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String email = "error";

    String activeTemplateName = null;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout_assistor);
        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

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
                                ("Username").withEmail(email),
                0);

        // [END AUTH AND NAV-DRAWER BOILERPLATE]

        // Get the name of the active template


        final DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");
        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                activeTemplateName = dataSnapshot.getValue(String.class);

                DatabaseReference activeTemplateDataRef = mRootRef.child("users").child(uid).child("templates").child(activeTemplateName);

                activeTemplateDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot templateSnapshot : dataSnapshot.getChildren()){
                            //List what = new List<ArrayList>();


                                    templateSnapshot.getValue(String.class);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

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
