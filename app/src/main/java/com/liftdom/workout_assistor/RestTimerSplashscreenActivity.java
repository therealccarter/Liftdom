package com.liftdom.workout_assistor;

import android.content.Intent;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;

public class RestTimerSplashscreenActivity extends AppCompatActivity {

    CountDownTimer timer;

    @BindView(R.id.exName) TextView exName;
    @BindView(R.id.setInfo) TextView setInfo;
    @BindView(R.id.autoDismiss) TextView autoDismissTime;
    @BindView(R.id.dismissNowButton) Button dismissNowButton;
    @BindView(R.id.goToWorkout) Button goToWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_timer_splashscreen);

        ButterKnife.bind(this);

        autoDismissTime.setText("10");

        try{
            exName.setText(getIntent().getStringExtra("exName"));
            String setInfoString = getIntent().getStringExtra("setInfo");
            setInfo.setText(setInfoString);
        }catch (NullPointerException e){

        }

        timer = new CountDownTimer(10000, 1000) {
            @Override
            public void onTick(long l) {
                String time = String.valueOf(l / 1000);
                autoDismissTime.setText(time);
            }

            @Override
            public void onFinish() {

            }
        }.start();

        goToWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent onClickIntent = new Intent(RestTimerSplashscreenActivity.this,
                        MainActivity.class);
                onClickIntent.putExtra("fragID",  2);
                startActivity(onClickIntent);
            }
        });
    }

}
