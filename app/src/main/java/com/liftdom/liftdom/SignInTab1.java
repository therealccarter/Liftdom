package com.liftdom.liftdom;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInTab1 extends Fragment {


    public SignInTab1() {
        // Required empty public constructor
    }


    private static final String TAG = "EmailPassword";

    private TextView mStatusTextView;
    private TextView mDetailTextView;
    private EditText mEmailField;
    private EditText mPasswordField;

    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    //@BindView(R.id.status) TextView statusTextView;
    //@BindView(R.id.detail) TextView detailTextView;
    @BindView(R.id.field_email) EditText emailField;
    @BindView(R.id.field_password) EditText passwordField;
    @BindView(R.id.email_sign_in_button) Button emailSignInButton;
    @BindView(R.id.sign_out_button) Button signOutButton;
    @BindView(R.id.go_to_main) Button goToMainButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_tab1, container, false);

        ButterKnife.bind(this, view);
        // Views

        // [START initialize_auth]
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

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

        goToMainButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        emailSignInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String email = emailField.getText().toString();
                String password = passwordField.getText().toString();

                //Log.d(TAG, "signIn:" + email);
                //if (!validateForm()) {
                //    return;
                //}

                //showProgressDialog();

                // [START sign_in_with_email]
                mAuth.signInWithEmailAndPassword(email, password);
                //        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                //            @Override
                //            public void onComplete(@NonNull Task<AuthResult> task) {
                //                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());
//
                //                // If sign in fails, display a message to the user. If sign in succeeds
                //                // the auth state listener will be notified and logic to handle the
                //                // signed in user can be handled in the listener.
                //                if (!task.isSuccessful()) {
                //                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                //                    Toast.makeText(view.getContext(), R.string.auth_failed,Toast.LENGTH_SHORT).show();
                //                }
//
                //                // [START_EXCLUDE]
                //                if (!task.isSuccessful()) {
                //                    mStatusTextView.setText(R.string.auth_failed);
                //                }
                //                //hideProgressDialog();
                //                // [END_EXCLUDE]
                //            }
                //        });
                //// [END sign_in_with_email]
                goToMainButton.setVisibility(View.VISIBLE);
            }
        });



        return view;

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

    //private void createAccount(String email, String password) {
    //    Log.d(TAG, "createAccount:" + email);
    //    if (!validateForm()) {
    //        return;
    //    }
//
    //    showProgressDialog();
//
    //    // [START create_user_with_email]
    //    mAuth.createUserWithEmailAndPassword(email, password)
    //            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
    //                @Override
    //                public void onComplete(@NonNull Task<AuthResult> task) {
    //                    Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());
//
    //                    // If sign in fails, display a message to the user. If sign in succeeds
    //                    // the auth state listener will be notified and logic to handle the
    //                    // signed in user can be handled in the listener.
    //                    if (!task.isSuccessful()) {
    //                        Toast.makeText(SignInActivity.this, R.string.auth_failed,
    //                                Toast.LENGTH_SHORT).show();
    //                    }
//
    //                    // [START_EXCLUDE]
    //                    hideProgressDialog();
    //                    // [END_EXCLUDE]
    //                }
    //            });
    //    // [END create_user_with_email]
    //}

    //private void signIn(String email, String password) {
    //    Log.d(TAG, "signIn:" + email);
    //    if (!validateForm()) {
    //        return;
    //    }
    //}

        //showProgressDialog();


    //private void signOut() {
    //    mAuth.signOut();
    //    //updateUI(null);
    //}
//
    //private boolean validateForm() {
    //    boolean valid = true;
//
    //    String email = mEmailField.getText().toString();
    //    if (TextUtils.isEmpty(email)) {
    //        mEmailField.setError("Required.");
    //        valid = false;
    //    } else {
    //        mEmailField.setError(null);
    //    }
//
    //    String password = mPasswordField.getText().toString();
    //    if (TextUtils.isEmpty(password)) {
    //        mPasswordField.setError("Required.");
    //        valid = false;
    //    } else {
    //        mPasswordField.setError(null);
    //    }
//
    //    return valid;
    //}

    //private void updateUI(FirebaseUser user) {
    //    hideProgressDialog();
    //    if (user != null) {
    //        mStatusTextView.setText(getString(R.string.emailpassword_status_fmt, user.getEmail()));
    //        mDetailTextView.setText(getString(R.string.firebase_status_fmt, user.getUid()));
//
    //        findViewById(R.id.email_password_buttons).setVisibility(View.GONE);
    //        findViewById(R.id.email_password_fields).setVisibility(View.GONE);
    //        //findViewById(R.id.sign_out_button).setVisibility(View.VISIBLE);
    //        findViewById(R.id.go_to_main).setVisibility(View.VISIBLE);
//
    //    } else {
    //        mStatusTextView.setText(R.string.signed_out);
    //        mDetailTextView.setText(null);
//
    //        findViewById(R.id.email_password_buttons).setVisibility(View.VISIBLE);
    //        findViewById(R.id.email_password_fields).setVisibility(View.VISIBLE);
    //        findViewById(R.id.sign_out_button).setVisibility(View.GONE);
    //        findViewById(R.id.go_to_main).setVisibility(View.GONE);
//
    //    }
    //}

    //@Override
    //public void onClick(View v) {
    //    int i = v.getId();
    //    if (i == R.id.email_create_account_button) {
    //        createAccount(mEmailField.getText().toString(), mPasswordField.getText().toString());
    //    } else if (i == R.id.email_sign_in_button) {
    //        signIn(mEmailField.getText().toString(), mPasswordField.getText().toString());
    //    } else if (i == R.id.sign_out_button) {
    //        signOut();
    //    }
    //}

}
