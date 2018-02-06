package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;

public class PercentageOptionsDialog extends AppCompatActivity {

    boolean isImperial = false;
    boolean isFromEx = false;

    @BindView(R.id.unitsView) TextView unitsView;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.arbitraryPercentEditText) EditText arbitraryEditText;
    @BindView(R.id.setsLevelReminderView) TextView setsLevelReminder;
    @BindView(R.id.exLevelReminderView) TextView exLevelReminder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage_chooser_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        HideKey.initialize(this);

        if(getIntent().getStringExtra("isFrom") != null){
            if(getIntent().getStringExtra("isFrom").equals("setsLevel")){
                Log.i("percentageInfo", "percentage options is setsLevel");
                isFromEx = false;
                setsLevelReminder.setVisibility(View.VISIBLE);
                exLevelReminder.setVisibility(View.GONE);
            }else if(getIntent().getStringExtra("isFrom").equals("exLevel")){
                Log.i("percentageInfo", "percentage options is exLevel");
                isFromEx = true;
                exLevelReminder.setVisibility(View.VISIBLE);
                setsLevelReminder.setVisibility(View.GONE);
            }
        }

        if(getIntent().getStringExtra("isImperial") != null){
            isImperial = Boolean.parseBoolean(getIntent().getStringExtra("isImperial"));
            if(isImperial){
                Log.i("percentageInfo", "percentage options is imperial");
                unitsView.setText(" lbs");
            }else{
                Log.i("percentageInfo", "percentage options is metric");
                unitsView.setText(" kgs");
            }
        }

        if(getIntent().getStringExtra("currentPercentOf") != null){
            String currentWeight = getIntent().getStringExtra("currentPercentOf");
            arbitraryEditText.setText(currentWeight);
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("weightResult", arbitraryEditText.getText().toString());
                if(isFromEx){
                    setResult(8, intent);
                }else{
                    setResult(4, intent);
                }
                finish();
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

}
