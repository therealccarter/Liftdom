package com.liftdom.liftdom;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ConsentFormDialogActivity extends AppCompatActivity {

    boolean consentBool = true;

    @BindView(R.id.yesRadioButton) RadioButton yesButton;
    @BindView(R.id.noRadioButton) RadioButton noButton;
    @BindView(R.id.confirmButton) Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consent_form_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        if(savedInstanceState == null){
            yesButton.setChecked(true);
            noButton.setChecked(false);
        }

        yesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    consentBool = true;
                }
            }
        });

        noButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    consentBool = false;
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("consentBool", consentBool);
                setResult(1, intent);
                finish();
            }
        });
    }
}