package com.liftdom.workout_assistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 12/4/2017.
 */

public class AssistorServiceClass extends Service {

    // CONSTANTS
    public static final String PREVIOUS_ACTION = "com.liftdom.workout_assistor.previous";
    public static final String NEXT_ACTION = "com.liftdom.workout_assistor.next";
    public static final String CHECK_ACTION = "com.liftdom.workout_assistor.check";
    public static final String UNCHECK_ACTION = "com.liftdom.workout_assistor.uncheck";

    String myString;
    RemoteViews notificationView;
    String uid;

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //final String command = intent.getStringExtra(CMDNAME);

            handleCommandIntent(intent);
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();

        Log.i("serviceInfo", "Service Started (onCreate)");

        final IntentFilter filter = new IntentFilter();
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        filter.addAction(CHECK_ACTION);
        filter.addAction(UNCHECK_ACTION);
        registerReceiver(mIntentReceiver, filter);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.i("serviceInfo", "Service Started (onStartCommand)");

        if(intent != null){
            if(intent.getStringExtra("uid") != null){
                uid = intent.getStringExtra("uid");
                Log.i("serviceInfo", "uid set");
            }

            handleCommandIntent(intent);

        }

        return START_STICKY; // check for null intent
    }

    private void handleCommandIntent(Intent intent){
        final String action = intent.getAction();

        if(action != null){
            if(action.equals(NEXT_ACTION)){

            }else if(action.equals(PREVIOUS_ACTION)){

            }else if(action.equals(CHECK_ACTION)){

            }else if(action.equals(UNCHECK_ACTION)){

            }
        }
    }

    private void updateNotification(){

    }

    //private Notification buildNotification(){

    //}

    private final PendingIntent retrieveMapAction(final String action){
        final ComponentName serviceName = new ComponentName(this, MainActivity.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);

        return PendingIntent.getService(this, 0, intent, 0);
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