package com.liftdom.charts_stats_tools.tools;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class MaxesEditExistingDialog extends AppCompatActivity {

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maxes_edit_existing_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){
            finish();
        }else{
            exName = getIntent().getStringExtra("exercise");
            date = getIntent().getStringExtra("date");
            oldMax = getIntent().getStringExtra("oldMax");
            isImperial = getIntent().getBooleanExtra("isImperial", true);
            isImperialPOV = getIntent().getBooleanExtra("isImperialPOV", true);
        }

        exNameTextView.setText(exName);
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

            }
        });

        /**
         * Add the number checker? Maybe not, since 999 is a possible weight.
         * On confirm we'll add the new number to the exName node along with the imperialPOV
         * It looks like shit OMEGALUL. Check the layout.
         */

    }

    private void setMaxViews(){

        if(isImperial == isImperialPOV){
            if(isImperial){
                String val = oldMax + " lbs";
                oldMaxTextView.setText(val);
                maxEditText.setHint(val);
            }else{
                String val = oldMax + " kgs";
                oldMaxTextView.setText(val);
                maxEditText.setHint(val);
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
