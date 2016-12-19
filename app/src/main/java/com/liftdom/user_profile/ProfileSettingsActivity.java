package com.liftdom.user_profile;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.text.TextUtilsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;

public class ProfileSettingsActivity extends AppCompatActivity {

    @BindView(R.id.usernameEditText) EditText usernameEditText;
    @BindView(R.id.usernameTextView) TextView usernameTextView;
    @BindView(R.id.bodyWeightEditText) EditText bodyWeightEditText;
    @BindView(R.id.saveButtonProfileSettings) Button saveButton;

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
        setContentView(R.layout.activity_profile_settings);

        ButterKnife.bind(this);


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
                    startActivity(new Intent(ProfileSettingsActivity.this, SignInActivity.class));
                }
            }
        };
        // [END auth_state_listener]

        final Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        final DatabaseReference usernameRef = mRootRef.child("users").child(uid);

        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean hasUsername = false;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    if(key.equals("username")){
                        hasUsername = true;
                        String uName = dataSnapshot1.getValue(String.class);
                        usernameTextView.setText(uName);
                        usernameTextView.setTypeface(lobster);
                        usernameTextView.setVisibility(View.VISIBLE);
                    }
                }
                if(!hasUsername){
                    usernameEditText.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!TextUtils.isEmpty(usernameEditText.getText())){
                    DatabaseReference setUserNameRef = usernameRef.child("username");
                    setUserNameRef.setValue(usernameEditText.getText().toString());


                    Intent intent = new Intent(v.getContext(), CurrentUserProfile.class);
                    startActivity(intent);
                }else{

                    Intent intent = new Intent(v.getContext(), CurrentUserProfile.class);
                    startActivity(intent);
                }

                // TODO: add bodyweight

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
