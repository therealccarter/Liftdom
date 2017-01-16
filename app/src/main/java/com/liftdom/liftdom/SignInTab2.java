package com.liftdom.liftdom;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

/**
 * A simple {@link Fragment} subclass.
 */
public class SignInTab2 extends Fragment {


    public SignInTab2() {
        // Required empty public constructor
    }


    @BindView(R.id.field_email) EditText emailField;
    @BindView(R.id.field_password) EditText passwordField;
    @BindView(R.id.email_create_account_button) Button createAccountButton;
    @BindView(R.id.go_to_main) Button goToMainButton;
    @BindView(R.id.field_username) EditText usernameField;
    @BindView(R.id.signUpTitle) TextView signUpTitle;


    // [START declare_auth]
    private FirebaseAuth mAuth;
    // [END declare_auth]

    // [START declare_auth_listener]
    private FirebaseAuth.AuthStateListener mAuthListener;
    // [END declare_auth_listener]

    private static final String TAG = "EmailPassword";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_in_tab2, container, false);

        ButterKnife.bind(this, view);


        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        signUpTitle.setTypeface(lobster);


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
        createAccountButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        getContext());

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
                                String email = emailField.getText().toString();
                                String password = passwordField.getText().toString();
                                String username = usernameField.getText().toString();

                                createAccount(email, username, password);

                                Intent intent = new Intent(getContext(), MainActivity.class);
                                startActivity(intent);

                                getActivity().finish();
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
        //goToMainButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        Intent intent = new Intent(getContext(), MainActivity.class);
//
        //        intent.putExtra("username", usernameField.getText().toString());
//
        //        startActivity(intent);
        //    }
        //});




        return view;
    }

    private void createAccount(final String email, final String username, final String password) {
        Log.d(TAG, "createAccount:" + email);
        // [START create_user_with_email]
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new
                OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                //TODO: invalid password
                final FirebaseUser user = task.getResult().getUser();

                UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                        .setDisplayName(username)
                        .build();

                        mAuth.getCurrentUser().updateProfile(changeRequest).addOnCompleteListener(getActivity(), new OnCompleteListener<Void>() {
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

        // [END create_user_with_email]
        //goToMainButton.setVisibility(View.VISIBLE);
    }

    // [START on_start_add_listener]
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
