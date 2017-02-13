package com.liftdom.workout_programs.Smolov;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SmolovSaveDialog extends AppCompatActivity {

    @BindView(R.id.saveButtonCancel) Button cancel;
    @BindView(R.id.saveButtonSave) Button save;
    @BindView(R.id.templateName) EditText templateName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_smolov_save_dialog);

        ButterKnife.bind(this);
    }
}
