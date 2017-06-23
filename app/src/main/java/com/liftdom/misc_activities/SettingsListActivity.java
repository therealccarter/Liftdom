package com.liftdom.misc_activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

public class SettingsListActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private GoogleApiClient mGoogleApiClient;

    boolean initialIsImperial = false;

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.sign_out_button) Button signOutButton;
    @BindView(R.id.lbsWeight) RadioButton poundsWeight;
    @BindView(R.id.kgsWeight) RadioButton kiloWeight;
    @BindView(R.id.checkBoxRound) CheckBox checkBoxRound;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.title) TextView title;

    ArrayList<String> settingsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        title.setTypeface(lobster);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
//
        //mainActivityTitle.setTypeface(lobster);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        Intent intent = new Intent(SettingsListActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(SettingsListActivity.this, CurrentUserProfile.class);
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
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Charts/Stats/Tools").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Premium Features").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(7)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(SettingsListActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(SettingsListActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 2);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(SettingsListActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 0);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(SettingsListActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(SettingsListActivity.this, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(SettingsListActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(SettingsListActivity.this, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                SettingsListActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);

        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(getApplicationContext(),
                R.drawable.usertest))
                .withName
                        (sharedPref.getString("userName", "loading...")).withEmail
                        (sharedPref.getString("email", "loading...")), 0);



        UserModelClass userModelClass = MainActivitySingleton.getInstance().userModelClass;

        if(userModelClass.isImperial()){
            initialIsImperial = true;
            poundsWeight.setChecked(true);
            kiloWeight.setChecked(false);
        }else{
            kiloWeight.setChecked(true);
            poundsWeight.setChecked(false);
        }

        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // [END build_client]

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(MainActivitySingleton.getInstance().isWorkoutFinished){
                    MainActivitySingleton.getInstance().isWorkoutFinished = false;
                }
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        mAuth.signOut();
                    }
                });
                Intent intent = new Intent(v.getContext(), SignInActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(initialIsImperial != poundsWeight.isChecked()){
                    final UserModelClass userModelClass1 = MainActivitySingleton.getInstance().userModelClass;
                    userModelClass1.setIsImperial(poundsWeight.isChecked());
                    if(poundsWeight.isChecked()){
                        // is now imperial
                        userModelClass1.setIsImperial(true);
                    }else{
                        // is now metric
                        userModelClass1.setIsImperial(true);
                    }
                    DatabaseReference userRef = mRootRef.child("user").child(uid);
                    userRef.setValue(userModelClass1).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            MainActivitySingleton.getInstance().userModelClass = userModelClass1;
                        }
                    });
                }

                //TODO: Check if it fails
                CharSequence toastText = "Settings Saved";
                int duration = Snackbar.LENGTH_SHORT;
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
                snackbar.show();
            }
        });
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    void firebaseSetter(String key, String value){
        DatabaseReference dataRef = mRootRef.child("users").child(uid).child(key);
        dataRef.setValue(value);
    }

    @Override
    public void onBackPressed(){

        //TODO: Have these things only called if changes are made
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle("Discard changes?");

        // set dialog message
        builder
                .setMessage("Are you sure you want to discard these changes?")
                .setCancelable(false)
                .setPositiveButton("Discard",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        SettingsListActivity.super.onBackPressed();

                        finish();
                    }
                })
                .setNegativeButton("Continue",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }
}
