package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExtraOptionsActivity extends AppCompatActivity {

    @BindView(R.id.cancelButton1) Button cancelButton;
    @BindView(R.id.defaultRepsOptionButton) Button defaultRepsOptionButton;
    @BindView(R.id.toFailureOptionButton) Button toFailureOptionButton;
    @BindView(R.id.defaultWeightOptionButton) Button defaultWeightOptionButton;
    @BindView(R.id.bodyWeightOptionButton) Button bodyWeightOptionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Options:");
        setContentView(R.layout.activity_extra_options);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        defaultRepsOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "defaultReps";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        toFailureOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "to failure";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        defaultWeightOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "defaultWeight";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        bodyWeightOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "bodyweight";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        finish();
    }
}
