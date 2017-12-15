package com.liftdom.workout_assistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 12/4/2017.
 */

public class AssistorServiceClass extends Service {

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();

        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        notificationIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //notificationIntent.setAction(Constants.ACTION.MAIN_ACTION);
        //notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        RemoteViews notificationView = new RemoteViews(this.getPackageName(), R.layout.assistor_notification_layout);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentTitle("Today\'s Workout")
                .setTicker("Test ticker")
                .setContentText("Test contentText")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContent(notificationView)
                .setOngoing(true)
                .build();

        startForeground(101, notification);

        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //stopForeground(true); may do an if/else in onStartCommand?
    }

    @Override
    public IBinder onBind(Intent intent){
        // used only in bound services
        return null;
    }


}