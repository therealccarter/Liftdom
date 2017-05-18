package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class AlgorithmOrSuperSetDialog extends AppCompatActivity {

    @BindView(R.id.exerciseName) TextView exNameView;
    @BindView(R.id.addAlgorithmButton) Button addAlgoButton;
    @BindView(R.id.addSupersetButton) Button addSupersetButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_or_super_set_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        String exName = getIntent().getExtras().getString("exName");

        if(exName.equals("Click to select exercise")){
            addAlgoButton.setVisibility(View.GONE);
            addSupersetButton.setVisibility(View.GONE);
            String dummy = "First choose an exercise";
            exNameView.setText(dummy);
        }else{
            exNameView.setText(exName);
        }

        addAlgoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("choice", "algo");
                setResult(3, intent);
                finish();
            }
        });

        addSupersetButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("choice", "superset");
                setResult(6, intent);
                finish();
            }
        });
    }
}