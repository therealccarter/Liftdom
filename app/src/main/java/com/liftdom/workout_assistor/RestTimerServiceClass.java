package com.liftdom.workout_assistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 12/6/2019.
 */
public class RestTimerServiceClass extends Service {

    public static final String CHANNEL_ID = "assistor_channel_02";
    String title = "Rest Timer";
    //String time = "1:30";

    @Override
    public void onCreate(){

    }

    private String millisToString(long millis){
        //int minutes = (int) millis / 60;
        //minutes = minutes / 1000;
        long minute = (millis / (1000 * 60)) % 60;
        long seconds = (millis / 1000) % 60;

        String secondsString;

        if(seconds < 10){
            secondsString = "0" + String.valueOf(seconds);
        }else{
            secondsString = String.valueOf(seconds);
        }

        return String.valueOf(minute) + ":" + secondsString;
    }

    private long stringTimeToMillis(String time){
        String delims = "[:]";
        String[] tokens = time.split(delims);

        long seconds = 0;
        seconds = seconds + (Long.parseLong(tokens[0]) * 60);
        seconds = seconds + Long.parseLong(tokens[1]);

        return seconds * 1000;
    }

    private Notification buildNotification(String time){
        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.putExtra("fragID",  2);

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(
                this,
                0,
                onClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        NotificationCompat.Builder builder = timerBuilder(onClickPendingIntent, time);

        //Notification n = builder.build();

        return builder.build();
    }

    private NotificationCompat.Builder timerBuilder(PendingIntent onClickPendingIntent,
                                                    String time){
        NotificationCompat.Builder builder = new NotificationCompat.
                Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setContentTitle(title)
                .setContentText(time)
                .setContentIntent(onClickPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        return builder;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        if(intent != null){
            String time;
            if(intent.getStringExtra("time") == null){
                time = "1:30";
            }else{
                time = intent.getStringExtra("time");
            }
            new CountDownTimer(stringTimeToMillis(time), 1000){
                public void onTick(long millisUntilFinished){
                    startForeground(102, buildNotification(millisToString(millisUntilFinished)));
                }

                public void onFinish(){
                    startForeground(102, buildNotification("0:00"));
                }
            }.start();
        }

        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        // used only in bound services
        return null;
    }
}
