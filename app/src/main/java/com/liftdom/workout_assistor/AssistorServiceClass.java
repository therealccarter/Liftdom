package com.liftdom.workout_assistor;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.*;
import android.os.Build;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;
import android.widget.RemoteViews;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

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
    public static final String FIRSTSET_ACTION = "com.liftdom.workout_assistor.reset";
    public static final String LASTSET_ACTION = "com.liftdom.workout_assistor.lastsetaction";
    public static final String CHANNEL_ID = "assistor_channel_01";
    private NotificationManagerCompat mNotificationManager;

    WorkoutProgressModelClass workoutProgressModelClass;

    MediaSessionCompat mediaSession;

    String myString;
    RemoteViews notificationView;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean isUserImperial = true;


    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //final String command = intent.getStringExtra(CMDNAME);

            Log.i("serviceInfo", "mIntentReceiver");

            handleCommandIntent(intent);
        }
    };

    @Override
    public void onCreate(){
        super.onCreate();

        Log.i("serviceInfo", "Service Started (onCreate)");

        mNotificationManager = NotificationManagerCompat.from(this);

        final IntentFilter filter = new IntentFilter();
        filter.addAction(NEXT_ACTION);
        filter.addAction(PREVIOUS_ACTION);
        filter.addAction(CHECK_ACTION);
        filter.addAction(UNCHECK_ACTION);
        filter.addAction(TOGGLECHECK_ACTION);
        filter.addAction(FIRSTSET_ACTION);
        filter.addAction(LASTSET_ACTION);
        registerReceiver(mIntentReceiver, filter);
        //has leaked IntentReceiver com.liftdom.workout_assistor.AssistorServiceClass$1@2ff3b64 that was originally registered here.
        // Are you missing a call to unregisterReceiver()?

        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child("runningAssistor").child(uid).child
                ("assistorModel");
        runningRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot == null){
                    stopSelf(101);
                }else{
                    Log.i("serviceInfo", "FIREBASE workoutProgressModelClass updated");
                    workoutProgressModelClass = dataSnapshot.getValue(WorkoutProgressModelClass.class);
                    if(workoutProgressModelClass != null){
                        if(workoutProgressModelClass.getExInfoHashMap() == null){
                            stopSelf(101);
                        }else{
                            checkDate();
                            startForeground(101, buildNotification());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference unitRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                .child("isImperial");
        unitRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot != null){
                    isUserImperial = dataSnapshot.getValue(Boolean.class);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //mediaSession = new MediaSessionCompat(this, "debug tag for media session");

        //mediaSession.setActive(true);

        //mediaSession.setCallback(new MediaSessionCompat.Callback() {
        //});


    }

    private void checkDate(){
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);

        if(!dateTimeString.equals(workoutProgressModelClass.getDate())){
            stopSelf(101);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Log.i("serviceInfo", "Service Started (onStartCommand)");

        //checkDate();

        if(intent != null){
            //if(intent.getStringExtra("uid") != null){
            //    uid = intent.getStringExtra("uid");
            //    Log.i("serviceInfo", "uid set");
            //}
            if(intent.getStringExtra("userImperial") != null){
                isUserImperial = Boolean.valueOf(intent.getStringExtra("userImperial"));
            }
            Log.i("serviceInfo", "onStartCommand/intent != null");
            handleCommandIntent(intent);
        }

        return START_STICKY; // check for null intent
    }

    private void handleCommandIntent(Intent intent){
        final String action = intent.getAction();

        Log.i("serviceInfo", "handleCommentIntent");

        if(action != null){
            if(action.equals(NEXT_ACTION)){
                Log.i("serviceInfo", "NEXT_ACTION");
                processNextAction();
            }else if(action.equals(PREVIOUS_ACTION)){
                Log.i("serviceInfo", "PREVIOUS_ACTION");
                processPreviousAction();
            }else if(action.equals(TOGGLECHECK_ACTION)){
                Log.i("serviceInfo", "TOGGLECHECK_ACTION");
                processToggleCheckAction();
            }else if(action.equals(FIRSTSET_ACTION)){
                Log.i("serviceInfo", "FIRSTSET_ACTION");
                processFirstSetAction();
            }else if(action.equals(LASTSET_ACTION)){
                Log.i("serviceInfo", "LASTSET_ACTION");
                processLastSetAction();
            }
        }
    }

    private void processLastSetAction(){
        workoutProgressModelClass.setViewCursorToLast();
        updateFirebaseProgressModel();
        startForeground(101, buildNotification());
    }

    private void processFirstSetAction(){
        workoutProgressModelClass.setViewCursor("1_0_1");
        updateFirebaseProgressModel();
        startForeground(101, buildNotification());
    }

    private void processNextAction(){
        workoutProgressModelClass.next();
        updateFirebaseProgressModel();
        startForeground(101, buildNotification());
    }

    private void processPreviousAction(){
        workoutProgressModelClass.previous();
        updateFirebaseProgressModel();
        startForeground(101, buildNotification());
    }

    private void processToggleCheckAction(){
        workoutProgressModelClass.toggleCheck();
        updateFirebaseProgressModel();
        //mNotificationManager.notify(101, buildNotification());
        startForeground(101, buildNotification());
    }

    private void updateFirebaseProgressModel(){
        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child("runningAssistor").child(uid).child
                ("assistorModel");
        runningRef.setValue(workoutProgressModelClass);
    }

    private String metricToImperial(String input){

        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        String newString = String.valueOf(lbsInt);

        return newString;
    }

    private String imperialToMetric(String input){

        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        String newString = String.valueOf(kgInt);

        return newString;
    }

    private String formatSetScheme(String unformatted){
        String formatted; // tokens: 4, a@214, unchecked. tokens2: 4
        // tokens: 4@B.W., unchecked. tokens2:4, B.W.

        String delimsCheck = "[_,@]";
        String[] tokensCheck = unformatted.split(delimsCheck);

        if(tokensCheck[1].equals("a")){
            String delims = "[_]";
            String[] tokens = unformatted.split(delims);

            String delims2 = "[@]";
            String[] tokens2 = tokens[0].split(delims2);

            String unit;
            if(isUserImperial){
                unit = "lbs";
            }else{
                unit = "kgs";
            }

            formatted = tokensCheck[0] + "+ reps @ " + checkForUnits(tokensCheck[2]) + " " + unit;
        }else{
            String delims = "[_]";
            String[] tokens = unformatted.split(delims);

            String delims2 = "[@]";
            String[] tokens2 = tokens[0].split(delims2);

            String unit;
            if(isUserImperial){
                unit = "lbs";
            }else{
                unit = "kgs";
            }

            formatted = tokens2[0] + " reps @ " + checkForUnits(tokens2[1]) + " " + unit;
            // array index out of bounds exception
        }

        return formatted;
    }

    private String checkForUnits(String input){
        String converted;

        if(!input.equals("B.W.")){
            if(isUserImperial && !workoutProgressModelClass.isIsTemplateImperial()){
                // user is lbs, template is kgs
                converted = metricToImperial(input);
            }else if(!isUserImperial && workoutProgressModelClass.isIsTemplateImperial()){
                // user is kgs, template is lbs
                converted = imperialToMetric(input);
            }else{
                converted = input;
            }
        }else{
            converted = input;
        }

        return converted;
    }

    private Notification buildNotification(){
        Log.i("serviceInfo", "Building notification...");

        int checkOrUncheckedId = getCheckedForCurrentPosition(); // not using this

        Intent onClickIntent = new Intent(this, MainActivity.class);
        onClickIntent.putExtra("fragID",  2);

        PendingIntent onClickPendingIntent = PendingIntent.getActivity(
                this,
                0,
                onClickIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        final String exerciseName;
        final String setSchemeUnformatted;
        final String setSchemeFormatted;
        final int checkedOrUncheckedResource;

        if(workoutProgressModelClass.getViewCursor() == null){
            // if first time
            workoutProgressModelClass.setViewCursor("1_0_1");
        }

        if(workoutProgressModelClass.getViewCursor().equals("1_0_1")){

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                // First set builder

                exerciseName = workoutProgressModelClass.exNameForCursor();
                //exerciseName = "Bench Press - \n (Barbell - Decline)";
                setSchemeUnformatted = workoutProgressModelClass.setForCursor();
                setSchemeFormatted = formatSetScheme(setSchemeUnformatted) + " " + workoutProgressModelClass.setIndex();

                if(isChecked(setSchemeUnformatted)){
                    checkedOrUncheckedResource = R.drawable.ic_crop_checked_white_small;
                    Log.i("serviceInfo", "isChecked");
                }else{
                    checkedOrUncheckedResource = R.drawable.ic_crop_square_white_small;
                    Log.i("serviceInfo", "not isChecked");
                }

                NotificationCompat.Builder builder
                        = firstSetBuilder(onClickPendingIntent, exerciseName, setSchemeFormatted,
                        checkedOrUncheckedResource);

                Notification n = builder.build();

                return n;

            }else{

                // First set builder (compat)

                exerciseName = workoutProgressModelClass.exNameForCursor();
                //exerciseName = "Bench Press - \n (Barbell - Decline)";
                setSchemeUnformatted = workoutProgressModelClass.setForCursor();
                setSchemeFormatted = formatSetScheme(setSchemeUnformatted) + " " + workoutProgressModelClass.setIndex();

                if(isChecked(setSchemeUnformatted)){
                    checkedOrUncheckedResource = R.drawable.ic_crop_checked_white_small;
                    Log.i("serviceInfo", "isChecked");
                }else{
                    checkedOrUncheckedResource = R.drawable.ic_crop_square_white_small;
                    Log.i("serviceInfo", "not isChecked");
                }

                NotificationCompat.Builder builder
                        = firstSetBuilderCompat(onClickPendingIntent, exerciseName, setSchemeFormatted,
                        checkedOrUncheckedResource);

                Notification n = builder.build();

                return n;

            }
        }else{
            if(workoutProgressModelClass.getViewCursor().equals("workoutDone")){

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    // Workout done builder

                    Log.i("serviceInfo", "greater than M");

                    NotificationCompat.Builder builder
                            = workoutDoneBuilder(onClickPendingIntent);

                    Notification n = builder.build();

                    return n;
                }else{

                    // Workout done builder (compat)

                    NotificationCompat.Builder builder
                            = workoutDoneBuilderCompat(onClickPendingIntent);

                    Notification n = builder.build();

                    return n;
                }
            }else{

                exerciseName = workoutProgressModelClass.exNameForCursor();
                setSchemeUnformatted = workoutProgressModelClass.setForCursor();
                setSchemeFormatted = formatSetScheme(setSchemeUnformatted) + " " + workoutProgressModelClass.setIndex();

                if(isChecked(setSchemeUnformatted)){
                    checkedOrUncheckedResource = R.drawable.ic_crop_checked_white_small;
                    Log.i("serviceInfo", "isChecked");
                }else{
                    checkedOrUncheckedResource = R.drawable.ic_crop_square_white_small;
                    Log.i("serviceInfo", "not isChecked");
                }

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){

                    // Normal set builder

                    Log.i("serviceInfo", "greater than M");

                    NotificationCompat.Builder builder =
                            normalSetBuilder(
                                    onClickPendingIntent, exerciseName,
                                    setSchemeFormatted, checkedOrUncheckedResource
                            );

                    Notification n = builder.build();

                    return n;
                }else{

                    // Normal set builder (compat)

                    NotificationCompat.Builder builder =
                            normalSetBuilderCompat(
                                    onClickPendingIntent, exerciseName,
                                    setSchemeFormatted, checkedOrUncheckedResource
                            );

                    Notification n = builder.build();

                    return n;
                }
            }
        }
    }

    private NotificationCompat.Builder firstSetBuilder(PendingIntent onClickPendingIntent, String exerciseName,
                                                       String setSchemeFormatted, int checkedOrUncheckedResource){
        Log.i("lastSetAction", "firstSetBuilder called");
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle(exerciseName)
                .setContentText(setSchemeFormatted)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(LASTSET_ACTION))
                .addAction(checkedOrUncheckedResource, "Check",
                        retrieveMapAction(TOGGLECHECK_ACTION))
                .addAction(R.drawable.ic_next,
                        "Next",
                        retrieveMapAction(NEXT_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private NotificationCompat.Builder normalSetBuilder(PendingIntent onClickPendingIntent, String exerciseName,
                                                        String setSchemeFormatted, int checkedOrUncheckedResource){
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle(exerciseName)
                .setContentText(setSchemeFormatted)
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(PREVIOUS_ACTION))
                .addAction(checkedOrUncheckedResource, "Check",
                        retrieveMapAction(TOGGLECHECK_ACTION))
                .addAction(R.drawable.ic_next,
                        "Next",
                        retrieveMapAction(NEXT_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private NotificationCompat.Builder workoutDoneBuilder(PendingIntent onClickPendingIntent){

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle("Workout Done!")
                .setContentText("Click this notification to finalize.")
                .setWhen(System.currentTimeMillis())
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setPublicVersion()
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(PREVIOUS_ACTION))
                .addAction(R.drawable.ic_next,
                        "First set",
                        retrieveMapAction(FIRSTSET_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private NotificationCompat.Builder firstSetBuilderCompat(PendingIntent onClickPendingIntent, String exerciseName,
                                                             String setSchemeFormatted, int checkedOrUncheckedResource){
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle(exerciseName)
                .setContentText(setSchemeFormatted)
                //.setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(LASTSET_ACTION))
                .addAction(checkedOrUncheckedResource, "Check",
                        retrieveMapAction(TOGGLECHECK_ACTION))
                .addAction(R.drawable.ic_next,
                        "Next",
                        retrieveMapAction(NEXT_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private NotificationCompat.Builder normalSetBuilderCompat(PendingIntent onClickPendingIntent, String exerciseName,
                                                        String setSchemeFormatted, int checkedOrUncheckedResource){
        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1, 2))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle(exerciseName)
                .setContentText(setSchemeFormatted)
                //.setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(PREVIOUS_ACTION))
                .addAction(checkedOrUncheckedResource, "Check",
                        retrieveMapAction(TOGGLECHECK_ACTION))
                .addAction(R.drawable.ic_next,
                        "Next",
                        retrieveMapAction(NEXT_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private NotificationCompat.Builder workoutDoneBuilderCompat(PendingIntent onClickPendingIntent){

        NotificationCompat.Builder builder = new NotificationCompat
                .Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.just_knight_white_small)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle()
                        .setShowActionsInCompactView(0, 1))
                .setContentIntent(onClickPendingIntent)
                .setContentTitle("Workout Done!")
                .setContentText("Click this notification to finalize.")
                //.setWhen(System.currentTimeMillis())
                .setShowWhen(false)
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                //.setPublicVersion()
                .addAction(R.drawable.ic_previous,
                        "Previous",
                        retrieveMapAction(PREVIOUS_ACTION))
                .addAction(R.drawable.ic_next,
                        "First set",
                        retrieveMapAction(FIRSTSET_ACTION))
                .setAutoCancel(false);

        return builder;
    }

    private boolean isChecked(String string){
        boolean checked = false;

        String delims = "[_]";
        String[] tokens = string.split(delims);

        if(tokens[1].equals("checked")){
            checked = true;
        }

        return checked;
    }

    private int getCheckedForCurrentPosition(){
        return R.drawable.ic_check_box_white_24dp;
    }

    private final PendingIntent retrieveMapAction(final String action){
        Log.i("serviceInfo", "retrieveMapAction");
        final ComponentName serviceName = new ComponentName(this, AssistorServiceClass.class);
        Intent intent = new Intent(action);
        intent.setComponent(serviceName);

        return PendingIntent.getService(this, 0, intent, 0);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        //stopForeground(true); may do an if/else in onStartCommand?
        unregisterReceiver(mIntentReceiver);
    }

    @Override
    public IBinder onBind(Intent intent){
        // used only in bound services
        return null;
    }



}