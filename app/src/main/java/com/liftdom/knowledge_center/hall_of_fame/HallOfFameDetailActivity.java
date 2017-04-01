package com.liftdom.knowledge_center.hall_of_fame;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class HallOfFameDetailActivity extends AppCompatActivity {

    @BindView(R.id.name) TextView hoFName;
    @BindView(R.id.hoFText) TextView hofText;
    @BindView(R.id.mainHoFImage) ImageView hofImage;
    @BindView(R.id.closeButton) Button closeButton;

    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame_detail);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        name = getIntent().getExtras().getString("person");

        ButterKnife.bind(this);

        hoFName.setText(name);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

    }
}
