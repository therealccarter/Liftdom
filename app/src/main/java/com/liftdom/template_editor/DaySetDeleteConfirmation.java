package com.liftdom.template_editor;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class DaySetDeleteConfirmation extends AppCompatActivity {

    @BindView(R.id.yesButton) Button yesButton;
    @BindView(R.id.noButton) Button noButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_set_delete_confirmation);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        yesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("remove", true);
                setResult(2, intent);
                finish();
            }
        });

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("remove", false);
                setResult(2, intent);
                finish();
            }
        });
    }
}
