package com.liftdom.workout_assistor;

import android.content.Intent;
import android.os.CountDownTimer;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.View;
import android.view.WindowManager;
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
    Vibrator vibrator;

    @BindView(R.id.exName) TextView exName;
    @BindView(R.id.setInfo) TextView setInfo;
    @BindView(R.id.autoDismiss) TextView autoDismissTime;
    @BindView(R.id.dismissNowButton) Button dismissNowButton;
    @BindView(R.id.goToWorkout) Button goToWorkout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest_timer_splashscreen);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        ButterKnife.bind(this);

        autoDismissTime.setText("10");

        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        customVibratePatternRepeat();

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
                autoDismissTime.setText("0");
                if(timer != null){
                    timer.cancel();
                }
                if(vibrator != null){
                    vibrator.cancel();
                }
                finish();
            }
        }.start();

        goToWorkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(vibrator != null){
                    vibrator.cancel();
                }
                Intent onClickIntent = new Intent(RestTimerSplashscreenActivity.this,
                        MainActivity.class);
                onClickIntent.putExtra("fragID",  2);
                startActivity(onClickIntent);
            }
        });

        dismissNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null){
                    timer.cancel();
                }
                if(vibrator != null){
                    vibrator.cancel();
                }
                finish();
            }
        });
    }

    private void vibrateFor(long millis) {
        vibrator.vibrate(millis);
    }

    private void customVibratePatternRepeat() {

        // 0 : Start without a delay
        // 400 : Vibrate for 400 milliseconds
        // 200 : Pause for 200 milliseconds
        // 400 : Vibrate for 400 milliseconds
        long[] mVibratePattern = new long[]{0, 300, 300, 300};

        // -1 : Do not repeat this pattern
        // pass 0 if you want to repeat this pattern from 0th index
        vibrator.vibrate(mVibratePattern, 0);

    }

    @Override
    public void onDestroy(){
        if(vibrator != null){
            vibrator.cancel();
        }
        super.onDestroy();
    }

}
