package com.liftdom.misc_activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;

public class DeleteAccountDialog extends AppCompatActivity {

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.deleteButton) Button deleteButton;
    @BindView(R.id.progressBar) ProgressBar progressBar;
    @BindView(R.id.deleteAccountText) TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_account_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText(R.string.stayForDelete);
                progressBar.setVisibility(View.VISIBLE);
                // need to delete user node and then throughout app add contingencies for a dead xUid.
                DatabaseReference userListRef =
                        FirebaseDatabase.getInstance().getReference().child("userList").child(uid);
                userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String username = dataSnapshot.getValue(String.class);
                        DatabaseReference userNameRef =
                                FirebaseDatabase.getInstance().getReference().child("userNames").child(username);
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child(
                                "user").child(uid);
                        DatabaseReference templateRef =
                                FirebaseDatabase.getInstance().getReference().child("templates").child(uid);
                        DatabaseReference runningRef =
                                FirebaseDatabase.getInstance().getReference().child(
                                        "runningAssistor").child(uid);
                        DatabaseReference workoutHistory =
                                FirebaseDatabase.getInstance().getReference().child(
                                        "workoutHistory").child(uid);
                        DatabaseReference completedExRef =
                                FirebaseDatabase.getInstance().getReference().child(
                                        "completedExercises").child(uid);
                        DatabaseReference maxesRef =
                                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid);
                        userNameRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                userListRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        templateRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                runningRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        workoutHistory.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                            @Override
                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                completedExRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                        maxesRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                            @Override
                                                                            public void onComplete(@NonNull Task<Void> task) {
                                                                                userRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                                    @Override
                                                                                    public void onComplete(@NonNull Task<Void> task) {
                                                                                        progressBar.setVisibility(View.GONE);
                                                                                        signOut();
                                                                                    }
                                                                                });
                                                                            }
                                                                        });
                                                                    }
                                                                });
                                                            }
                                                        });
                                                    }
                                                });
                                            }
                                        });
                                    }
                                });
                            }
                        });

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    private void signOut(){
        // Firebase sign out
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new
        // ResultCallback<Status>() {
        //    @Override
        //    public void onResult(@NonNull Status status) {
        //progressBar.setVisibility(View.GONE);
        //signOut();
        //        mAuth.signOut();
        //    }
        //});
        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        mAuth = FirebaseAuth.getInstance();

        mAuth.signOut();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(this,
                new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        startActivity(new Intent(getApplicationContext(),
                                SignInActivity.class));
                    }
                });
    }
}
