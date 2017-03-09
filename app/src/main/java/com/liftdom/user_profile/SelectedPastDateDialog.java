package com.liftdom.user_profile;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.ExerciseNameFrag;

public class SelectedPastDateDialog extends AppCompatActivity {

    String formattedDate = "null";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    @BindView(R.id.selectedDateView) TextView selectedDateView;
    @BindView(R.id.closeButton) Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_past_date_dialog);

        ButterKnife.bind(this);

        formattedDate = getIntent().getExtras().getString("date");

        selectedDateView.setText(formattedDate);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        selectedDateView.setTypeface(lobster);

        DatabaseReference dateRef = mRootRef.child("workout_history").child(uid).child(formattedDate);

        dateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String snapshotString = dataSnapshot1.getValue(String.class);
                    String keyString = dataSnapshot1.getKey();

                    if (isExerciseName(snapshotString) && !keyString.equals("private_journal")) {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                exerciseNameFrag);
                        if (!isFinishing()) {
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                        exerciseNameFrag.exerciseName = snapshotString;
                    } else if(isExerciseName(snapshotString) && keyString.equals("private_journal")
                        && !snapshotString.equals("")){
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        PastJournalFrag pastJournalFrag = new PastJournalFrag();
                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                pastJournalFrag);
                        if (!isFinishing()) {
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                        pastJournalFrag.journalString = snapshotString;
                    } else {
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                        fragmentTransaction.add(R.id.eachExerciseFragHolder,
                                repsWeightFrag);
                        if (!isFinishing()) {
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                        repsWeightFrag.repsWeight = snapshotString;

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }
}
