package com.liftdom.workout_assistor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SaveAssistorDialog extends AppCompatActivity {

    boolean isRestDay = false;

    @BindView(R.id.saveButtonSave) Button saveButton;
    @BindView(R.id.saveButtonCancel) Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_save_assistor_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        if(getIntent().getExtras().getString("isRestDay") != null){
            isRestDay = true;
        }

        //  Caused by: java.lang.NullPointerException: Attempt to invoke virtual method
        // 'java.lang.String android.os.Bundle.getString(java.lang.String)' on a null object reference
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                finish();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v){
                if(isRestDay){
                    setResult(2);
                }else{
                    setResult(1);
                }
                finish();
            }
        });

    }
}
