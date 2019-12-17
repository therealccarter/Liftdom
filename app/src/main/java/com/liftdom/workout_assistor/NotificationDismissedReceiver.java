package com.liftdom.workout_assistor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Brodin on 12/13/2019.
 */
public class NotificationDismissedReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getExtras().getInt("notificationId");
        /* Your code to handle the event here */
        if(notificationId == 101){
            Intent stopIntent = new Intent(context, AssistorServiceClass.class);
            context.stopService(stopIntent);
        }
    }
}