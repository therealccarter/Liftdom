package com.liftdom.user_profile.calendar_stuff;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;
import com.liftdom.workout_assistor.ExerciseNameFrag;
import com.liftdom.workout_assistor.WorkoutProgressModelClass;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class SelectedPastDateDialog extends AppCompatActivity {

    String formattedDate = "null";

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public Boolean isOtherUser = false;
    public String xUid = "null";

    @BindView(R.id.selectedDateView) TextView selectedDateView;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.privateJournalTextView) TextView privateJournal;
    @BindView(R.id.privateJournalTitle) TextView privateJournalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_past_date_dialog);

        ButterKnife.bind(this);

        if(getIntent().getBooleanExtra("isOtherUser", false)){
            uid = getIntent().getExtras().getString("xUid");
        }else{
            isOtherUser = true;
        }

        formattedDate = getIntent().getExtras().getString("date");
        DateTimeFormatter fmt = DateTimeFormat.forPattern("MM-dd-yyyy");

        DateTime dateTime = new DateTime(formattedDate);

        String formatted = fmt.print(dateTime);

        selectedDateView.setText(formatted);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        selectedDateView.setTypeface(lobster);

        DatabaseReference historyRef = mRootRef.child("workoutHistory").child(uid).child(formattedDate);
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                WorkoutHistoryModelClass modelClass = dataSnapshot.getValue(WorkoutHistoryModelClass.class);
                try{
                    if(modelClass.getUserId() == null){
                        finish();
                    }else{
                        generateLayout(modelClass);
                    }
                }catch (NullPointerException e){
                    finish();
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

    private void generateLayout(WorkoutHistoryModelClass modelClass){
        if(uid != null){
            if(uid.equals(modelClass.getUserId())){
                if(!modelClass.getPrivateJournal().equals("")){
                    privateJournalTitle.setVisibility(View.VISIBLE);
                    privateJournal.setText(modelClass.getPrivateJournal());
                }
            }
        }
        DatabaseReference imperialRef = mRootRef.child("user").child(uid).child("isImperial");
        imperialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    Boolean isUserImperial = dataSnapshot.getValue(Boolean.class);
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    PastDateDialogSubFrag pastDateDialogSubFrag = new PastDateDialogSubFrag();
                    pastDateDialogSubFrag.workoutHistoryModelClass = modelClass;
                    pastDateDialogSubFrag.isImperialPOV = isUserImperial;
                    fragmentTransaction.replace(R.id.eachExerciseFragHolder, pastDateDialogSubFrag);
                    fragmentTransaction.commit();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
