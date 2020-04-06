package com.liftdom.template_editor;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExSSLevelOptionsDialog extends AppCompatActivity {

    @BindView(R.id.exerciseName) TextView exNameView;
    @BindView(R.id.setSchemeSettings) Button setSchemesPercentageSettingsButton;
    @BindView(R.id.extraOptionsButton) Button extraOptionsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_s_s_level_options_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        String exName = getIntent().getExtras().getString("exName");

        if(exName.equals("Click to select exercise")){
            setSchemesPercentageSettingsButton.setVisibility(View.GONE);
            extraOptionsButton.setVisibility(View.GONE);
            String dummy = "First choose an exercise";
            exNameView.setText(dummy);
        }else{
            exNameView.setText(exName);
        }

        setSchemesPercentageSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(2, intent);
                finish();
            }
        });
    }
}
