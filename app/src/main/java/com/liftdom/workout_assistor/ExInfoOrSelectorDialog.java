package com.liftdom.workout_assistor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExInfoOrSelectorDialog extends AppCompatActivity {

    @BindView(R.id.selectExButton) Button selectExButton;
    @BindView(R.id.exInfoButton) Button exInfoButton;
    @BindView(R.id.exerciseName) TextView exNameView;

    String exerciseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_info_or_selector_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        exerciseName = getIntent().getExtras().getString("exName");

        if(!exerciseName.equals("Click to select exercise")){
            exNameView.setText(exerciseName);
        }

        selectExButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("choice", "selectEx");
                setResult(1, intent);
                finish();
            }
        });

        exInfoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("choice", "infoEx");
                setResult(1, intent);
                finish();
            }
        });
    }
}
