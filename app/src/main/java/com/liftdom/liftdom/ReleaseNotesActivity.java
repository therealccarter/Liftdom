package com.liftdom.liftdom;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;

public class ReleaseNotesActivity extends AppCompatActivity {


    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.newFeaturesView) TextView newFeaturesView;
    @BindView(R.id.fixesView) TextView fixesView;
    @BindView(R.id.workingOnView) TextView workingOnView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_notes);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        newFeaturesView.setTypeface(lobster);
        fixesView.setTypeface(lobster);
        workingOnView.setTypeface(lobster);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
