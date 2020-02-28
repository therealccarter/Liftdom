package com.liftdom.liftdom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.*;
import com.google.firebase.database.*;
import com.liftdom.liftdom.intro.FirstTimeSetupActivity;
import com.liftdom.user_profile.UserModelClass;

public class SignInActivity extends BaseActivity implements View.OnClickListener{

    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    //private GoogleApiClient mGoogleApiClient;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        setProgressBar(R.id.progressBar);

        // Button listeners
        findViewById(R.id.sign_in_button).setOnClickListener(this);
        TextView titleText = (TextView) findViewById(R.id.signInTitle);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        titleText.setTypeface(lobster);

        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        //mGoogleApiClient = new GoogleApiClient.Builder(this)
        //        .enableAutoManage(this /* FragmentActivity */, this /*
        //OnConnectionFailedListener */)
        //        .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
        //        .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }

    // [START on_start_check_user]
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            //mAuth.signOut();
            //Intent intent = new Intent(SignInActivity.this, MainActivity.class);
            //startActivity(intent);
        }
        //updateUI(currentUser);
    }
    // [END on_start_check_user]

    // [START onactivityresult]
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "signInWithCredential:onActivityResult");
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Toast.makeText(SignInActivity.this, "Authentication failed. Trying again at " +
                                "least once may fix this.",
                        Toast.LENGTH_SHORT).show();
                Log.w(TAG, "Google sign in failed", e);
                // [START_EXCLUDE]
                updateUI(null);
                // [END_EXCLUDE]
            }
        }
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        //if (requestCode == RC_SIGN_IN) {
        //    GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
        //    if (result.isSuccess()) {
        //        // Google Sign In was successful, authenticate with Firebase
        //        GoogleSignInAccount account = result.getSignInAccount();
        //        firebaseAuthWithGoogle(account);
        //    } else {
        //        // Google Sign In failed, update UI appropriately
        //        // [START_EXCLUDE]
        //        Toast.makeText(SignInActivity.this, "Authentication failed. Trying again at " +
        //                        "least once may fix this.",
        //                Toast.LENGTH_SHORT).show();
        //        // [END_EXCLUDE]
        //    }
        //}
    }
    // [END onactivityresult]

    // [START auth_with_google]
    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        //showProgressDialog();
        showProgressBar();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            final String userName = user.getDisplayName();
                            final String userId = user.getUid();

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                                    .child(userId);

                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){

                                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                                        if(userModelClass.getUserName() == null){
                                            Intent intent = new Intent(SignInActivity.this, FirstTimeSetupActivity.class);
                                            intent.putExtra("uid", userId);
                                            intent.putExtra("defaultDisplayName", userName);
                                            intent.putExtra("email", user.getEmail());
                                            startActivity(intent);
                                        }else{
                                            SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = sharedPref.edit();
                                            editor.putString("uid", userId);
                                            editor.putString("userName", userModelClass.getUserName());
                                            editor.putString("email", user.getEmail());
                                            editor.apply();

                                            Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    }else{
                                        Intent intent = new Intent(SignInActivity.this, FirstTimeSetupActivity.class);
                                        intent.putExtra("uid", userId);
                                        intent.putExtra("defaultDisplayName", userName);
                                        intent.putExtra("email", user.getEmail());
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                            try{
                                Snackbar.make(getCurrentFocus(), "Authentication Failed.",
                                        Snackbar.LENGTH_SHORT).show();
                            }catch (IllegalArgumentException e){
                                Toast.makeText(SignInActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                            updateUI(null);
                        }

                        // [START_EXCLUDE]
                        //hideProgressDialog();
                        hideProgressBar();
                        // [END_EXCLUDE]
                    }
                });
    }
    // [END auth_with_google]

    // [START signin]
    private void signIn() {
        //Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        //startActivityForResult(signInIntent, RC_SIGN_IN);
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]


    private void revokeAccess() {
        // Firebase sign out
        mAuth.signOut();

        // Google revoke access
        //Auth.GoogleSignInApi.revokeAccess(mGoogleApiClient).setResultCallback(
        //        new ResultCallback<Status>() {
        //            @Override
        //            public void onResult(@NonNull Status status) {
        //                updateUI(null);
        //            }
        //        });
        // Google revoke access
        mGoogleSignInClient.revokeAccess().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        updateUI(null);
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        //hideProgressDialog();
        hideProgressBar();
        if (user != null) {
            findViewById(R.id.sign_in_button).setVisibility(View.GONE);
        } else {
        }
    }

    //@Override
    //public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    //    // An unresolvable error has occurred and Google APIs (including Sign-In) will not
    //    // be available.
    //    Log.d(TAG, "onConnectionFailed:" + connectionResult);
    //    Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    //}

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.sign_in_button) {
            signIn();
        }
    }
}
