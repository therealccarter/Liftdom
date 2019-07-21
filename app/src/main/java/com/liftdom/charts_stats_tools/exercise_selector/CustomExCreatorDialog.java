package com.liftdom.charts_stats_tools.exercise_selector;

import android.os.Handler;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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

    private long delay = 1000;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    private boolean isValid = false;

    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.customExEditText) EditText customExEditText;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.invalidExNameView) TextView invalidExNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ex_creator_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        customExEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isValid = false;
                handler.removeCallbacks(inputFinishChecker);
                invalidExNameView.setVisibility(View.GONE);
                if(s.length() == 0){
                    invalidExNameView.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    lastTextEdit = System.currentTimeMillis();
                    handler.postDelayed(inputFinishChecker, delay);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!customExEditText.getText().toString().equals("")){
                    if(isValid){
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
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private Runnable inputFinishChecker = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                if(isFirstCharNum(customExEditText.getText().toString())){
                    isValid = false;
                    invalidExNameView.setVisibility(View.VISIBLE);
                    confirmButton.setClickable(false);
                }else{
                    isValid = true;
                    invalidExNameView.setVisibility(View.GONE);
                    confirmButton.setClickable(true);
                }
            }
        }
    };

    private boolean isFirstCharNum(String input){
        boolean isChar = false;

        char c = input.charAt(0);
        if (Character.isDigit(c)) {
            isChar = true;
        }

        return isChar;
    }
}
