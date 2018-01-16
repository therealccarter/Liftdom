package com.liftdom.workout_assistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.graphics.drawable.Icon;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.Toast;
import com.google.firebase.database.*;
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
    public static final String TOGGLECHECK_ACTION = "com.liftdom.workout_assistor.togglecheck";
    public static final String CHANNEL_ID = "assistor_channel_01";

    WorkoutProgressModelClass workoutProgressModelClass;

    MediaSessionCompat mediaSession;

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
        filter.addAction(TOGGLECHECK_ACTION);
        registerReceiver(mIntentReceiver, filter);

        mediaSession = new MediaSessionCompat(this, "debug tag for media session");

        mediaSession.setActive(true);

        mediaSession.setCallback(new MediaSessionCompat.Callback() {
        });


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

        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child("runningAssistor").child(uid).child
                ("assistorModel");
        runningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                workoutProgressModelClass = dataSnapshot.getValue(WorkoutProgressModelClass.class);
                startForeground(101, buildNotification());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



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

    private Notification buildNotification(){
        Log.i("serviceInfo", "Building notification...");

        int checkOrUncheckedId = getCheckedForCurrentPosition();

        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.putExtra("fragID",  2);

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(
                this,
                0,
                onClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            Log.i("serviceInfo", "greater than M");
            //Notification.Action action1 = new Notification.Action.Builder(
            //        Icon.createWithResource(this, R.drawable.ic_skip_previous_white_36dp),
            //        "",
            //        retrieveMapAction(PREVIOUS_ACTION))
            //        .build();
            //Notification.Action action2 = new Notification.Action.Builder(
            //        Icon.createWithResource(this, checkOrUncheckedId),
            //        "", retrieveMapAction(TOGGLECHECK_ACTION))
            //        .build();
            //Notification.Action action3 = new Notification.Action.Builder(
            //        Icon.createWithResource(this, R.drawable.ic_skip_next_white_36dp),
            //        "",
            //        retrieveMapAction(NEXT_ACTION))
            //        .build();
//
            //Notification.Builder builder = new Notification.Builder(this)
            //        .setSmallIcon(R.mipmap.ic_launcher)
            //        .setContentTitle("Bench Press (Barbell - Flat)")
            //        .setContentText("1 rep @ 135lbs")
            //        .addAction(action1)
            //        .addAction(action2)
            //        .addAction(action3);

            android.support.v4.app.NotificationCompat.Builder builder = new android.support.v4.app.NotificationCompat
                    .Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.just_knight_white_small)
                    .setContentIntent(onClickPendingIntent)
                    .setContentTitle("Bench Press")
                    .setContentText("3 reps @ 135lbs")
                    .setWhen(System.currentTimeMillis())
                    .setPriority(NotificationCompat.PRIORITY_MAX)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)
                    .addAction(R.drawable.ic_previous,
                            "Previous",
                            retrieveMapAction(PREVIOUS_ACTION))
                    .addAction(R.drawable.ic_crop_square_white_small, "Check",
                            retrieveMapAction(TOGGLECHECK_ACTION))
                    .addAction(R.drawable.ic_next,
                            "Next",
                            retrieveMapAction(NEXT_ACTION));


            android.support.v4.media.app.NotificationCompat.MediaStyle style = new android.support.v4.media.app
                    .NotificationCompat.MediaStyle()
                    .setShowActionsInCompactView(0, 1, 2);
            builder.setStyle(style);

            Notification n = builder.build();

            return n;
        }else{
            // we'll deal with this later, but before next release.
            Log.i("serviceInfo", "less than M");
            NotificationCompat.Action action1 = new NotificationCompat.Action.Builder(
                    R.drawable.ic_skip_previous_white_36dp, "",
                    retrieveMapAction(PREVIOUS_ACTION))
                    .build();
            NotificationCompat.Action action2 = new NotificationCompat.Action.Builder(
                    checkOrUncheckedId, "", retrieveMapAction(TOGGLECHECK_ACTION))
                    .build();
            NotificationCompat.Action action3 = new NotificationCompat.Action.Builder(
                    R.drawable.ic_skip_next_white_36dp, "", retrieveMapAction(NEXT_ACTION))
                    .build();

            NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setContentTitle("Bench Press (Barbell - Flat)")
                    .setContentText("1 rep @ 135lbs")
                    .addAction(action1)
                    .addAction(action2)
                    .addAction(action3);

            Notification n = builder.build();

            return n;
        }
    }

    private int getCheckedForCurrentPosition(){
        return R.drawable.ic_check_box_white_24dp;
    }

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