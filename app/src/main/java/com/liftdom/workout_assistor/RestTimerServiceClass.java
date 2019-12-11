package com.liftdom.workout_assistor;

import android.app.*;
import android.content.*;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import androidx.core.app.NotificationCompat;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 12/6/2019.
 */
public class RestTimerServiceClass extends Service {

    public static final String TOGGLEPAUSE_ACTION = "com.liftdom.workout_assistor.pause";


    public static final String CHANNEL_ID = "assistor_channel_02";
    String title = "Rest Timer";
    Long mTime;
    boolean isPaused = false;
    String exNameCursor = "";
    String setInfoCursor = "";

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //final String command = intent.getStringExtra(CMDNAME);

            Log.i("serviceInfo", "mIntentReceiver");

            handleCommandIntent(intent);
        }
    };

    private void handleCommandIntent(Intent intent){
        final String action = intent.getAction();

        if(action != null){
            if(action.equals(TOGGLEPAUSE_ACTION)){
                processTogglePauseAction();
            }
        }
    }

    @Override
    public void onCreate(){
        final IntentFilter filter = new IntentFilter();
        filter.addAction(TOGGLEPAUSE_ACTION);
        registerReceiver(mIntentReceiver, filter);
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

    private final PendingIntent retrieveAction(final String action){
        Log.i("serviceInfo", "retrieveMapAction");
        final ComponentName serviceName = new ComponentName(this, RestTimerServiceClass.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);

        return PendingIntent.getService(this, 0, intent, 0);
    }

    private void processTogglePauseAction(){
        if(isPaused){
            isPaused = false;
            if(timer != null){
                timer.cancel();
            }
            final long time = mTime;
            timer = new CountDownTimer(stringTimeToMillis(millisToString(time)), 1000){
                public void onTick(long millisUntilFinished){
                    startForeground(102, buildNotification(millisToString(millisUntilFinished),
                            R.drawable.pause));
                    mTime = millisUntilFinished;
                }

                public void onFinish(){
                    startForeground(102, buildNotification("0:00", R.drawable.pause));
                    Intent finishedIntent = new Intent(RestTimerServiceClass.this,
                            RestTimerSplashscreenActivity.class);
                    finishedIntent.putExtra("exName", exNameCursor);
                    finishedIntent.putExtra("setInfo", setInfoCursor);
                    finishedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(finishedIntent);
                }
            }.start();
        }else{
            isPaused = true;
            if(timer != null){
                timer.cancel();
            }
            startForeground(102, buildNotification(millisToString(mTime), R.drawable.play));
        }
    }

    private Notification buildNotification(String time, int pauseOrPlay){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name2);
            String description = getString(R.string.channel_description2);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.putExtra("fragID",  2);

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(
                this,
                0,
                onClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        //int pauseOrPlay;
//
        //if(isPaused){
        //    pauseOrPlay = R.drawable.ic_play_white_36dp;
        //}else{
        //    pauseOrPlay = R.drawable.pause;
        //}

        NotificationCompat.Builder builder = timerBuilder(onClickPendingIntent, time, pauseOrPlay);

        //Notification n = builder.build();

        return builder.build();
    }

    private NotificationCompat.Builder timerBuilder(PendingIntent onClickPendingIntent,
                                                    String time, int pauseOrPlay){
        NotificationCompat.Builder builder = new NotificationCompat.
                Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0))
                .setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(time)
                .setContentIntent(onClickPendingIntent)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(pauseOrPlay,
                        "Pause",
                        retrieveAction(TOGGLEPAUSE_ACTION));

        return builder;

    }

    CountDownTimer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        if(intent != null){
            if(intent.getAction() == null){
                String time;
                if(intent.getStringExtra("time") == null){
                    time = "1:30";
                    mTime = stringTimeToMillis(time);
                }else{
                    time = intent.getStringExtra("time");
                    mTime = stringTimeToMillis(time);
                }
                if(timer != null){
                    timer.cancel();
                }
                timer = new CountDownTimer(stringTimeToMillis(time), 1000){
                    public void onTick(long millisUntilFinished){
                        startForeground(102, buildNotification(millisToString(millisUntilFinished),
                                R.drawable.pause));
                        mTime = millisUntilFinished;
                    }

                    public void onFinish(){
                        startForeground(102, buildNotification("0:00", R.drawable.pause));
                        Intent finishedIntent = new Intent(RestTimerServiceClass.this,
                                RestTimerSplashscreenActivity.class);
                        finishedIntent.putExtra("exName", exNameCursor);
                        finishedIntent.putExtra("setInfo", setInfoCursor);
                        finishedIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(finishedIntent);
                    }
                }.start();
                try{
                    exNameCursor = intent.getStringExtra("exName");
                    setInfoCursor = intent.getStringExtra("setInfo");
                }catch (NullPointerException e){

                }
            }else{
                handleCommandIntent(intent);
            }

            //if(intent.getAction().equals(TOGGLEPAUSE_ACTION)){
            //    if(isPaused){
            //        startForeground(102, buildNotification(millisToString(mTime)));
            //    }else{
            //        if(timer != null){
            //            timer.cancel();
            //        }
            //        final long time = mTime;
            //        timer = new CountDownTimer(stringTimeToMillis(millisToString(time)), 1000){
            //            public void onTick(long millisUntilFinished){
            //                startForeground(102,
            //                        buildNotification(millisToString(millisUntilFinished)));
            //                mTime = millisUntilFinished;
            //            }
//
            //            public void onFinish(){
            //                startForeground(102, buildNotification("0:00"));
            //            }
            //        }.start();
            //    }
            //}else{
            //    String time;
            //    if(intent.getStringExtra("time") == null){
            //        time = "1:30";
            //        mTime = stringTimeToMillis(time);
            //    }else{
            //        time = intent.getStringExtra("time");
            //        mTime = stringTimeToMillis(time);
            //    }
            //    if(timer != null){
            //        timer.cancel();
            //    }
            //    timer = new CountDownTimer(stringTimeToMillis(time), 1000){
            //        public void onTick(long millisUntilFinished){
            //            startForeground(102,
            //                    buildNotification(millisToString(millisUntilFinished)));
            //            mTime = millisUntilFinished;
            //        }
//
            //        public void onFinish(){
            //            startForeground(102, buildNotification("0:00"));
            //        }
            //    }.start();
            //}
        }

        return START_NOT_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent){
        // used only in bound services
        return null;
    }
}
