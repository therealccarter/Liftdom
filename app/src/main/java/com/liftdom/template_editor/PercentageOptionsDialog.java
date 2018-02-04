package com.liftdom.template_editor;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;

public class PercentageOptionsDialog extends AppCompatActivity {

    @BindView(R.id.unitsView) TextView unitsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_percentage_chooser_dialog);
        this.setFinishOnTouchOutside(true);

        HideKey.initialize(this);

    }

}
