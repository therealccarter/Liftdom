package com.liftdom.misc_activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import androidx.annotation.NonNull;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.widget.Toolbar;
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
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class SettingsListActivity extends BaseActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    //private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

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
    @BindView(R.id.userConsentButton) Button userConsentButton;
    @BindView(R.id.deleteAccountButton) Button deleteAccountButton;

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

        setUpNavDrawer(SettingsListActivity.this, toolbar);
        setNavDrawerSelection(9);

        if(isEuUser(getApplicationContext())){
            userConsentButton.setVisibility(View.VISIBLE);
        }

        DatabaseReference userRef = mRootRef.child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                if(userModelClass.isIsImperial()){
                    initialIsImperial = true;
                    poundsWeight.setChecked(true);
                    kiloWeight.setChecked(false);
                }else{
                    kiloWeight.setChecked(true);
                    poundsWeight.setChecked(false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(getApplicationContext(), DeleteAccountDialog.class));
            }
        });

        userConsentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid).child
                        ("isGDPR");

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        boolean isGDPR;
                        if(dataSnapshot.exists()){
                            isGDPR = dataSnapshot.getValue(Boolean.class);
                            Intent intent = new Intent(SettingsListActivity.this, ConsentFormDialogActivity.class);
                            intent.putExtra("consent", isGDPR);
                            startActivityForResult(intent, 1);
                        }else{
                            isGDPR = false;
                            Intent intent = new Intent(SettingsListActivity.this, ConsentFormDialogActivity.class);
                            intent.putExtra("consent", isGDPR);
                            startActivityForResult(intent, 1);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


            }
        });


        // [START configure_signin]
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        //GoogleSignInOptions gso =
        //        new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        //        .requestEmail()
        //        .build();
        // [END configure_signin]

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        //mGoogleApiClient = new GoogleApiClient.Builder(this)
        //        .enableAutoManage(this /* FragmentActivity */, this /*
        //OnConnectionFailedListener */)
        //        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        //        .build();
        //// [END build_client]

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                signOut();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

            final DatabaseReference userRef = mRootRef.child("user").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    UserModelClass userModelClass2 = dataSnapshot.getValue(UserModelClass.class);
                    if(poundsWeight.isChecked()){
                        // is now imperial
                        userModelClass2.setIsImperial(true);
                        MainActivitySingleton.getInstance().isImperial = true;
                    }else{
                        // is now metric
                        userModelClass2.setIsImperial(false);
                        MainActivitySingleton.getInstance().isImperial = false;
                    }

                    userRef.setValue(userModelClass2).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            //MainActivitySingleton.getInstance().userModelClass = userModelClass2;
                            //TODO: Check if it fails
                            CharSequence toastText = "Settings Saved";
                            int duration = Snackbar.LENGTH_SHORT;
                            Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
                            snackbar.show();

                            if(poundsWeight.isChecked()){
                                // is now imperial
                                MainActivitySingleton.getInstance().isImperial = true;
                            }else{
                                // is now metric
                                MainActivitySingleton.getInstance().isImperial = false;
                            }
                        }
                    });
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            }
        });
    }

    public static boolean isEuUser(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String country = tm != null ? tm.getSimCountryIso() : null;
        country = country != null ? country : Locale.getDefault().getCountry();
        String[] euCountries = {
                "BE", "EL", "LT", "PT", "BG", "ES", "LU", "RO", "CZ", "FR", "HU", "SI", "DK", "HR",
                "MT", "SK", "DE", "IT", "NL", "FI", "EE", "CY", "AT", "SE", "IE", "LV", "PL", "UK",
                "CH", "NO", "IS", "LI"
        };
        return Arrays.asList(euCountries).contains(country.toUpperCase());
    }

    //@Override
    //public void onConnectionFailed(ConnectionResult connectionResult) {
    //    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    //    // be available.
    //    Log.d(TAG, "onConnectionFailed:" + connectionResult);
    //}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if(resultCode == 1){
                boolean isGDPR = data.getBooleanExtra("consentBool", true);
                SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                sharedPref.edit().putBoolean("consent", isGDPR).apply();
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                        .child("isGDPR");
                userRef.setValue(isGDPR).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        try{
                            Snackbar.make(getCurrentFocus(), "Settings saved!", Snackbar.LENGTH_SHORT).show();
                        }catch (NullPointerException e){

                        }
                    }
                });
            }
        }
    }

    private void signOut(){
        // Firebase sign out
        if(MainActivitySingleton.getInstance().isWorkoutFinished){
            MainActivitySingleton.getInstance().isWorkoutFinished = false;
        }
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new
        // ResultCallback<Status>() {
        //    @Override
        //    public void onResult(@NonNull Status status) {
        //        mAuth.signOut();
        //    }
        //});
        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(SettingsListActivity.this, SignInActivity.class);
                        startActivity(intent);
                    }
                });

    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();

        //if(initialIsImperial == poundsWeight.isChecked()){
        //    SettingsListActivity.super.onBackPressed();
        //}else{
        //    AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
        //    // set title
        //    builder.setTitle("Discard changes?");
//
        //    // set dialog message
        //    builder
        //            .setMessage("Are you sure you want to discard these changes?")
        //            .setCancelable(false)
        //            .setPositiveButton("Discard",new DialogInterface.OnClickListener() {
        //                public void onClick(DialogInterface dialog,int id) {
//
        //                    SettingsListActivity.super.onBackPressed();
//
        //                    finish();
        //                }
        //            })
        //            .setNegativeButton("Continue",new DialogInterface.OnClickListener() {
        //                public void onClick(DialogInterface dialog,int id) {
        //                    // if this button is clicked, just close
        //                    // the dialog box and do nothing
        //                    dialog.cancel();
        //                }
        //            });
//
        //    // create alert dialog
        //    AlertDialog alertDialog = builder.create();
//
        //    // show it
        //    alertDialog.show();
        //}

    }
}
