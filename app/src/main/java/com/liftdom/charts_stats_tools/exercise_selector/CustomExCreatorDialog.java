package com.liftdom.charts_stats_tools.exercise_selector;

import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class CustomExCreatorDialog extends AppCompatActivity {

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.customExEditText) EditText customExEditText;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.cancelButton) Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ex_creator_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!customExEditText.getText().toString().equals("")){
                    loadingView.setVisibility(View.VISIBLE);
                    customExEditText.setVisibility(View.GONE);

                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    DatabaseReference customExRef = FirebaseDatabase.getInstance().getReference().child
                            ("customExercises").child(uid);
                    String refKey = customExRef.push().getKey();
                    String dateTimeString = new DateTime(DateTime.now(), DateTimeZone.UTC).toString();
                    String newExName = customExEditText.getText().toString();

                    CustomExModelClass customExModelClass = new CustomExModelClass(newExName, null,
                            dateTimeString, refKey);

                    customExRef.child(refKey).setValue(customExModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            setResult(1);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setResult(2);
                            finish();
                        }
                    });
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
