package com.liftdom.liftdom;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class SignInActivity2 extends AppCompatActivity {

    @BindView(R.id.signin_email) EditText signInEmail;
    @BindView(R.id.signin_password) EditText signInPassword;
    @BindView(R.id.signup_email) EditText signUpEmail;
    @BindView(R.id.signup_password) EditText signUpPassword;
    @BindView(R.id.signup_username) EditText signUpUsername;
    @BindView(R.id.signInTitle) TextView signInTitle;
    @BindView(R.id.email_sign_up_button) TextView signUpButton;
    @BindView(R.id.email_sign_in_button) TextView signInButton;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in2);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        signInTitle.setTypeface(lobster);
        signInButton.setTypeface(lobster);
        signUpButton.setTypeface(lobster);

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
                }
                // [START_EXCLUDE]
                //updateUI(user);
                // [END_EXCLUDE]
            }
        };
        // [END auth_state_listener]

        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = signInEmail.getText().toString();
                String password = signInPassword.getText().toString();

                if(email == null || password == null){
                    CharSequence toastText = "Email/Password fields are required";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                    toast.show();
                }else{
                    mAuth.signInWithEmailAndPassword(email, password);

                    Intent intent = new Intent(SignInActivity2.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });


        signUpButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getApplicationContext());

                // set title
                alertDialogBuilder.setTitle("Note:");

                // set dialog message
                alertDialogBuilder
                        .setMessage("You'll be taken to the sign in page. Sign in, and your account will be ready!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, close
                                // current activity
                                String email = signUpEmail.getText().toString();
                                String password = signUpPassword.getText().toString();
                                String username = signUpUsername.getText().toString();

                                if(email == null || password == null || username == null){
                                    CharSequence toastText = "Email/Password/Username fields are required";
                                    int duration = Toast.LENGTH_SHORT;
                                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                                    toast.show();
                                    finish();
                                }else{
                                    createAccount(email, username, password);
                                    Intent intent = new Intent(SignInActivity2.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();

            }
        });
    }

    private void createAccount(final String email, final String username, final String password) {

        Log.d(TAG, "createAccount:" + email);

        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new
                OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        //TODO: invalid password
                        final FirebaseUser user = task.getResult().getUser();

                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(username)
                                .build();

                        mAuth.getCurrentUser().updateProfile(changeRequest).addOnCompleteListener(getParent(), new
                                OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                //this is needed for display name to show up in auth listener
                                user.reload();
                                mAuth.signOut();
                                mAuth.signInWithEmailAndPassword(email, password);
                            }
                        });
                    }
                });
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }
    // [END on_start_add_listener]

    @Override
    public void onResume(){
        super.onResume();
        mAuth.addAuthStateListener(mAuthListener);
    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
}
