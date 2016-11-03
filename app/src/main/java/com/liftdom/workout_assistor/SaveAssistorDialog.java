package com.liftdom.workout_assistor;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SaveAssistorDialog extends Activity {

    @BindView(R.id.saveButtonSave) Button saveButton;
    @BindView(R.id.saveButtonCancel) Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_assistor_dialog);

        ButterKnife.bind(this);

        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                Intent intent = new Intent(v.getContext(), AssistorSavedActivity.class);
                startActivity(intent);

            }
        });

    }
}
