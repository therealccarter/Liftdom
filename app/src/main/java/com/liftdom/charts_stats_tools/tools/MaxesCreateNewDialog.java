package com.liftdom.charts_stats_tools.tools;

import android.graphics.Typeface;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.LocalDate;

public class MaxesCreateNewDialog extends AppCompatActivity {

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private String exName;
    private boolean isImperialPOV;

    @BindView(R.id.exName) TextView exNameTextView;
    @BindView(R.id.maxEditText) EditText maxEditText;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.units) TextView unitsTextView;
    @BindView(R.id.maxExistsTextView) TextView maxExistsTextView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maxes_create_new_dialog);

        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){
            finish();
        }else{
            exName = getIntent().getStringExtra("exercise");
            exNameTextView.setTypeface(null, Typeface.BOLD);
            hideLoadingView();
            exNameTextView.setText(exName);
            checkForExisting();
            isImperialPOV = getIntent().getBooleanExtra("isImperialPOV", true);
            if(isImperialPOV){
                unitsTextView.setText("lbs");
            }else{
                unitsTextView.setText("kgs");
            }
        }

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!maxEditText.getText().toString().isEmpty()){
                    showLoadingView();
                    DatabaseReference maxRef =
                            FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(exName);
                    ExerciseMaxesModelClass modelClass = new ExerciseMaxesModelClass(exName,
                            maxEditText.getText().toString(), isImperialPOV, LocalDate.now().toString());
                    maxRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            setResult(1);
                            finish();
                        }
                    });

                }
            }
        });
    }

    private void showLoadingView(){
        loadingView.setVisibility(View.VISIBLE);
        maxEditText.setVisibility(View.GONE);
        unitsTextView.setVisibility(View.GONE);
    }

    private void hideLoadingView(){
        loadingView.setVisibility(View.GONE);
        maxEditText.setVisibility(View.VISIBLE);
        unitsTextView.setVisibility(View.VISIBLE);
    }

    private void checkForExisting(){
        DatabaseReference maxRef =
                FirebaseDatabase.getInstance().getReference().child("maxes").child(uid).child(exName);
        maxRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    maxExistsTextView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        if(exName == null || uid == null){
            finish();
        }
    }


}
