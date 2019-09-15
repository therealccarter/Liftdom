package com.liftdom.workout_assistor;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExDeleteConfirmation extends AppCompatActivity {

    @BindView(R.id.yesButton) Button yesButton;
    @BindView(R.id.noButton) Button noButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_delete_confirmation);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("remove", true);
                setResult(4, intent);
                finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("remove", false);
                setResult(4, intent);
                finish();
            }
        });


    }
}
