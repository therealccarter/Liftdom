package com.liftdom.knowledge_center.exercise_library;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExerciseDetailActivity extends AppCompatActivity {

    String exName;

    @BindView(R.id.exTitle) TextView exTitle;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.exSummary) TextView exSummary;
    @BindView(R.id.exHowTo) TextView exHowTo;
    @BindView(R.id.variantsAndDetailsHolder) LinearLayout variantsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exercise_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        exName = getIntent().getExtras().getString("exName");

        ButterKnife.bind(this);

        exTitle.setText(exName);





        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
