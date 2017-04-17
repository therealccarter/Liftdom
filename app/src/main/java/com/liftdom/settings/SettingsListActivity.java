package com.liftdom.settings;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.history_chart.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.LoginActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.PremiumFeaturesActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.user_profile.CurrentUserProfile;
import com.liftdom.user_profile.ProfileInfoActivity;
import com.liftdom.workout_assistor.WorkoutAssistorActivity;
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

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.sign_out_button) Button signOutButton;
    @BindView(R.id.settingsTitle) TextView settingsTitle;
    @BindView(R.id.lbsWeight) RadioButton poundsWeight;
    @BindView(R.id.kgsWeight) RadioButton kiloWeight;
    @BindView(R.id.lbsBodyWeight) RadioButton poundsBodyWeight;
    @BindView(R.id.kgsBodyWeight) RadioButton kiloBodyWeight;
    @BindView(R.id.footInchHeight) RadioButton footInchesHeight;
    @BindView(R.id.centimetersHeight) RadioButton centiHeight;
    @BindView(R.id.checkBoxRound) CheckBox checkBoxRound;
    @BindView(R.id.saveButton) Button saveButton;

    ArrayList<String> settingsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        settingsTitle.setTypeface(lobster);

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
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
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
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
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
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(SettingsListActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(SettingsListActivity.this, WorkoutAssistorActivity.class);
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

        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(getApplicationContext(), R
                        .drawable.usertest))
                        .withName
                                (mFirebaseUser.getDisplayName()).withEmail
                                (mFirebaseUser.getEmail()),
                0);

        DatabaseReference settingsRef = mRootRef.child("users").child(uid);
        settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    if(!key.equals("maxes")){
                        String value = dataSnapshot1.getValue(String.class);

                        if(key.equals("weightUnit")){
                            if(value.equals("pounds")){
                                poundsWeight.setChecked(true);
                            }else if(value.equals("kilos")){
                                kiloWeight.setChecked(true);
                            }
                        }else if(key.equals("bodyWeightUnit")){
                            if(value.equals("pounds")){
                                poundsBodyWeight.setChecked(true);
                            }else if(value.equals("kilos")){
                                kiloBodyWeight.setChecked(true);
                            }
                        }else if(key.equals("heightUnit")){
                            if(value.equals("footInches")){
                                footInchesHeight.setChecked(true);
                            }else if(value.equals("centimeters")){
                                centiHeight.setChecked(true);
                            }
                        }else if(key.equals("roundWeight")){
                            if(value.equals("yes")){
                                checkBoxRound.setChecked(true);
                            }else if(value.equals("no")){
                                checkBoxRound.setChecked(false);
                            }
                        }
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                mAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(poundsWeight.isChecked()){
                    firebaseSetter("weightUnit", "pounds");
                }else if(kiloWeight.isChecked()){
                    firebaseSetter("weightUnit", "kilos");
                }
                if(poundsBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "pounds");
                }else if(kiloBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "kilos");
                }
                if(footInchesHeight.isChecked()){
                    firebaseSetter("heightUnit", "footInches");
                }else if(centiHeight.isChecked()){
                    firebaseSetter("heightUnit", "centimeters");
                }
                if(checkBoxRound.isChecked()){
                    firebaseSetter("roundWeight", "yes");
                }else{
                    firebaseSetter("roundWeight", "no");
                }
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
