package com.liftdom.charts_stats_tools.tools;

import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.helper_classes.DigitsInputFilter;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.LocalDate;

public class MaxesEditExistingDialog extends AppCompatActivity {

    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private String exName;
    private String date;
    private String oldMax;
    private boolean isImperial;
    private boolean isImperialPOV;

    @BindView(R.id.exName) TextView exNameTextView;
    @BindView(R.id.date) TextView dateTextView;
    @BindView(R.id.oldMax) TextView oldMaxTextView;
    @BindView(R.id.maxEditText) EditText maxEditText;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.units) TextView unitsTextView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_maxes_edit_existing_dialog);

        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){
            finish();
        }else{
            exName = getIntent().getStringExtra("exercise");
            exNameTextView.setTypeface(null, Typeface.BOLD);
            hideLoadingView();
            exNameTextView.setText(exName);
            date = getIntent().getStringExtra("date");
            oldMax = getIntent().getStringExtra("oldMax");
            isImperial = getIntent().getBooleanExtra("isImperial", true);
            isImperialPOV = getIntent().getBooleanExtra("isImperialPOV", true);
            if(isImperialPOV){
                unitsTextView.setText("lbs");
                maxEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                maxEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
            }else{
                unitsTextView.setText("kgs");
                maxEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                maxEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
            }
        }

        String date2 = "(" + date + ")";
        dateTextView.setText(date2);
        setMaxViews();

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

    @Override
    public void onResume(){
        super.onResume();
        if(exName == null || date == null || uid == null){
            finish();
        }
    }

    private void setMaxViews(){

        if(isImperial == isImperialPOV){
            if(isImperial){
                String val = oldMax + " lbs";
                String valSans = oldMax;
                oldMaxTextView.setText(val);
                maxEditText.setHint(valSans);
            }else{
                String val = oldMax + " kgs";
                String valSans = oldMax;
                oldMaxTextView.setText(val);
                maxEditText.setHint(valSans);
            }
        }else{
            if(isImperial && !isImperialPOV){
                // convert from imperial to metric
                String val = imperialToMetric() + " kgs";
                oldMaxTextView.setText(val);
                maxEditText.setHint(imperialToMetric());
            }else if(!isImperial && isImperialPOV){
                // convert from metric to imperial
                String val = metricToImperial() + " lbs";
                oldMaxTextView.setText(val);
                maxEditText.setHint(metricToImperial());
            }
        }

    }

    private String metricToImperial(){

        double lbsDouble = Double.parseDouble(oldMax) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        String newString = String.valueOf(lbsInt);

        return newString;
    }

    private String imperialToMetric(){

        double kgDouble = Double.parseDouble(oldMax) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        String newString = String.valueOf(kgInt);

        return newString;
    }
}
