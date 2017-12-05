package com.liftdom.workout_assistor;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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