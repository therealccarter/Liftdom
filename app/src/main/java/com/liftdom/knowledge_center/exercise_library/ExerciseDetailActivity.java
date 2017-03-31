package com.liftdom.knowledge_center.exercise_library;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExerciseDetailActivity extends AppCompatActivity {

    String exName;

    @BindView(R.id.testText) TextView testText;
    @BindView(R.id.closeButton) Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);

        exName = getIntent().getExtras().getString("exName");

        ButterKnife.bind(this);

        testText.setText(exName);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
