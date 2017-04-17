package com.liftdom.user_profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.LoginActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;


public class ProfileInfoActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText) EditText usernameEditText;
    @BindView(R.id.usernameTextView) TextView usernameTextView;
    @BindView(R.id.bodyWeightEditText) EditText bodyWeightEditText;
    @BindView(R.id.heightFeet) EditText heightFeet;
    @BindView(R.id.heightInches) EditText heightInches;
    @BindView(R.id.benchPress1rm) EditText benchPress1rm;
    @BindView(R.id.squat1rm) EditText squat1rm;
    @BindView(R.id.deadlift1rm) EditText deadlift1rm;
    @BindView(R.id.saveButtonProfileSettings) Button saveButton;
    @BindView(R.id.currentFocus) Spinner currentFocusSpinner;
    @BindView(R.id.ageYears) EditText ageEditText;
    @BindView(R.id.maleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.femaleRadioButton) RadioButton femaleRadioButton;

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String email = "error";
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_stats);

        ButterKnife.bind(this);

        //TODO: Make sure this is using identical units as set in Settings

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

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
                    startActivity(new Intent(ProfileInfoActivity.this, LoginActivity.class));
                }
            }
        };
        // [END auth_state_listener]

        //currentFocusSpinner.setOnItemSelectedListener(this);

        List<String> focuses = new ArrayList<String>();
        focuses.add("Bodybuilding");
        focuses.add("Powerlifting");
        focuses.add("Powerbuilding");
        focuses.add("General Weightlifting");
        focuses.add("General Fitness");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, R.layout.spinner_style_new_1,
                focuses);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        currentFocusSpinner.setAdapter(dataAdapter);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        usernameTextView.setText(mFirebaseUser.getDisplayName());
        usernameTextView.setTypeface(lobster);

        final DatabaseReference userRef = mRootRef.child("users").child(uid);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals("currentFocus")){
                        String value = dataSnapshot1.getValue(String.class);
                        if(value.equals("Bodybuilding")){
                            currentFocusSpinner.setSelection(0);
                        }else if(value.equals("Powerlifting")){
                            currentFocusSpinner.setSelection(1);
                        }else if(value.equals("Powerbuilding")){
                            currentFocusSpinner.setSelection(2);
                        }else if(value.equals("General Weightlifting")){
                            currentFocusSpinner.setSelection(3);
                        }else if(value.equals("General Fitness")){
                            currentFocusSpinner.setSelection(4);
                        }
                    }else if(dataSnapshot1.getKey().equals("bodyweight")){
                        String value = dataSnapshot1.getValue(String.class);
                        bodyWeightEditText.setText(value);
                    }else if(dataSnapshot1.getKey().equals("age")){
                        String value = dataSnapshot1.getValue(String.class);
                        ageEditText.setText(value);
                    }else if(dataSnapshot1.getKey().equals("sex")){
                        String value = dataSnapshot1.getValue(String.class);
                        if(value.equals("male")){
                            maleRadioButton.setChecked(true);
                            femaleRadioButton.setChecked(false);
                        }else if(value.equals("female")){
                            maleRadioButton.setChecked(false);
                            femaleRadioButton.setChecked(true);
                        }
                    }else if(dataSnapshot1.getKey().equals("height")){
                        String value = dataSnapshot1.getValue(String.class);
                        String delims = "[_]";
                        String[] values = value.split(delims);
                        heightFeet.setText(values[0]);
                        heightInches.setText(values[1]);
                    }else if(dataSnapshot1.getKey().equals("maxes")){
                        DatabaseReference maxesRef = userRef.child("maxes");
                        maxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot2 : dataSnapshot.getChildren()){
                                    if(dataSnapshot2.getKey().equals("benchMax")){
                                        String value = dataSnapshot2.getValue(String.class);
                                        benchPress1rm.setText(value);

                                    }else if(dataSnapshot2.getKey().equals("squatMax")){
                                        String value = dataSnapshot2.getValue(String.class);
                                        squat1rm.setText(value);

                                    }else if(dataSnapshot2.getKey().equals("deadliftMax")){
                                        String value = dataSnapshot2.getValue(String.class);
                                        deadlift1rm.setText(value);

                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String currentFocus = currentFocusSpinner.getSelectedItem().toString();
                String bodyweight = bodyWeightEditText.getText().toString();
                String height = heightFeet.getText().toString() + "_" + heightInches.getText().toString();
                String age = ageEditText.getText().toString();

                String benchMax = benchPress1rm.getText().toString();
                String squatMax = squat1rm.getText().toString();
                String deadliftMax = deadlift1rm.getText().toString();

                userRef.child("currentFocus").setValue(currentFocus);
                userRef.child("bodyweight").setValue(bodyweight);
                userRef.child("height").setValue(height);
                userRef.child("age").setValue(age);
                userRef.child("maxes").child("benchMax").setValue(benchMax);
                userRef.child("maxes").child("squatMax").setValue(squatMax);
                userRef.child("maxes").child("deadliftMax").setValue(deadliftMax);

                if(maleRadioButton.isChecked()){
                    userRef.child("sex").setValue("male");
                }else if(femaleRadioButton.isChecked()){
                    userRef.child("sex").setValue("female");
                }

                Intent intent = new Intent(v.getContext(), CurrentUserProfile.class);
                startActivity(intent);

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

                        Intent intent = new Intent(ProfileInfoActivity.this, CurrentUserProfile.class);
                        startActivity(intent);

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
