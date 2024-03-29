package com.liftdom.workout_assistor;


import android.app.ActivityManager;
import android.content.*;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.InputFilter;
import android.view.*;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import android.util.Log;
import android.widget.*;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.workout_programs.FiveThreeOne_ForBeginners.Wendler_531_For_Beginners;
import com.liftdom.workout_programs.PPL_Reddit.PPLRedditClass;
import com.liftdom.workout_programs.Smolov.Smolov;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorHolderFrag extends android.app.Fragment
                implements ExNameWAFrag.removeFragCallback,
                ExNameWAFrag.startFirstTimeShowcase,
                ExNameWAFrag.updateWorkoutStateCallback,
                ExNameWAFrag.updateWorkoutStateForResultCallback,
                ExNameWAFrag.updateWorkoutStateFastCallback,
                ExNameWAFrag.updateExNameCallback,
                ExNameWAFrag.sendAssistanceExerciseCallback,
                ExNameWAFrag.updateChildFragWeightsCallback{


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TemplateModelClass mTemplateClass;
    ArrayList<ExNameWAFrag> exNameFragList = new ArrayList<>();
    int exNameInc = 0;
    boolean savedState = false;
    WorkoutProgressModelClass workoutProgressModelClass;
    String premadeWeekDayString;
    boolean isTemplateImperial;
    boolean isRunningImperial;
    ArrayList<String> fragTagList = new ArrayList<>();
    boolean isRevisedWorkout;
    boolean isFreestyleWorkout;
    String refKey;
    boolean isFromRestDay;
    boolean isInForeground;
    //boolean isLastDay;
    boolean isListening;
    HashMap<String, String> preMadeInfo = new HashMap<>();
    String templateName;

    boolean mIsKeyboardVisible = false;
    View rootView;
    Rect measureRect;
    ViewTreeObserver.OnGlobalLayoutListener keyboardListener;

    DatabaseReference mRunningAssistorRef = mRootRef.child("runningAssistor").child(uid);
    //.child("assistorModel");
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    public interface scrollToBottomInterface{
        void scrollToBottom();
    }

    public interface killAssistorListener{
        void killAssistor();
    }

    private killAssistorListener killAssistorListener;

    private scrollToBottomInterface scrollToBottomCallback;

    @BindView(R.id.addExerciseButton) Button addExButton;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.saveHolder) CardView saveHolder;
    @BindView(R.id.saveProgressButton) Button saveProgressButton;
    @BindView(R.id.privateJournal) EditText privateJournalView;
    @BindView(R.id.publicComment) EditText publicCommentView;
    @BindView(R.id.saveImage) ImageButton saveImage;
    @BindView(R.id.extraInfoTextView) TextView extraInfoTextView;
    @BindView(R.id.endView) TextView endView;
    @BindView(R.id.activateStatusBarWA) Button activateStatusBarService;
    @BindView(R.id.deactivateStatusBarWA) Button deactivateStatusBarService;
    @BindView(R.id.deactivateStatusBarImageView) ImageView deactiveStatusBarImage;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.exInfoHolder2) LinearLayout exInfoHolder2;
    @BindView(R.id.deactivateLL) LinearLayout deactivateLL;
    @BindView(R.id.serviceCardView) CardView serviceCardView;
    @BindView(R.id.resetWorkoutButton) Button resetWorkoutProgressButton;
    @BindView(R.id.cancelRevision) Button cancelRevisionButton;
    @BindView(R.id.cancelRevisionHolder) CardView cancelRevisionHolder;
    @BindView(R.id.extraText) TextView extraText;
    @BindView(R.id.restTimerLL) LinearLayout restTimerLL;
    @BindView(R.id.restTimerSwitch) Switch restTimerSwitch;
    @BindView(R.id.restTimerInfoLL) LinearLayout restTimerInfoLL;
    @BindView(R.id.minutes) EditText minutesEditText;
    @BindView(R.id.seconds) EditText secondsEditText;
    @BindView(R.id.confirmRestTimerButton) Button confirmRestTimer;
    @BindView(R.id.secondsVibrate) EditText secondsVibrateEditText;
    @BindView(R.id.showRestTimerAlertRadioButton) RadioButton showRestTimerAlertRB;
    @BindView(R.id.justVibrateRadioButton) RadioButton justVibrateRB;


    boolean isFirstTimeFirstTime = true;
    boolean isTutorialFirstTime = false;

    boolean isUserImperial = true;

    boolean restTimerBool = false;

    boolean restTimerNoUpdate = false;
    boolean justVibrateNoUpdate = false;

    private ValueEventListener runningAssistorListener;

    //DatabaseReference runningAssistorRef;

    public void firstTimeShowcase(CheckBox checkBox){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    boolean isGreen = false;

    CountDownTimer timer;

    private void flashUpdateButton(){
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(3000, 200) {
            @Override
            public void onTick(long l) {
                if(isGreen){
                    confirmRestTimer.setBackgroundColor(Color.parseColor("#27632a"));
                    isGreen = false;
                }else{
                    confirmRestTimer.setBackgroundColor(Color.parseColor("#388e3c"));
                    isGreen = true;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);

        ButterKnife.bind(this, view);

        rootView = (LinearLayout) view.findViewById(R.id.assistorHolder);

        HideKey.initialize(getActivity());

        isInForeground = true;

        Log.i("assistorInfo", "onCreateView called (assistor holder)");

        Log.i("assistorInfo", "onCreateView");

        isListening = true;

        //checkIfUserIsImperial();

        /*
         * This is being called on the split screen shit.
         */
        //checkForOldData();

        //killAssistorListener = (killAssistorListener) getParentFragment();

        if(isRevisedWorkout || isFreestyleWorkout){
            cancelRevisionHolder.setVisibility(View.VISIBLE);
        }

        if(isFreestyleWorkout){
            resetWorkoutProgressButton.setVisibility(View.GONE);
            setPlaceHolderTemplate();
        }

        // ========================= ONLY LISTENERS BEYOND THIS POINT ===============================

        cancelRevisionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRevision();
            }
        });

        cancelRevisionHolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelRevision();
            }
        });

        secondsEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        minutesEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 15)});
        secondsVibrateEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});

        restTimerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restTimerNoUpdate = false;
                if(restTimerBool){
                    restTimerSwitch.setChecked(false);
                }else{
                    restTimerSwitch.setChecked(true);
                }
            }
        });

        restTimerSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    restTimerBool = true;
                    restTimerInfoLL.setVisibility(View.VISIBLE);
                    if(!restTimerNoUpdate){
                        updateWorkoutState();
                    }else{
                        restTimerNoUpdate = false;
                    }
                }else{
                    restTimerBool = false;
                    restTimerInfoLL.setVisibility(View.GONE);
                    if(!restTimerNoUpdate){
                        updateWorkoutState();
                    }else{
                        restTimerNoUpdate = false;
                    }
                }
            }
        });

        showRestTimerAlertRB.setChecked(true);

        showRestTimerAlertRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                /*
                 * Problem now is that it's "true" twice. Takes one manual turn to turn it false.
                 * Should be "true" until it's populated and then turn false automatically.
                 */
                if(!justVibrateNoUpdate){
                    //DatabaseReference activeRef =
                    //        FirebaseDatabase.getInstance().getReference().child
                    //        ("runningAssistor").
                    //                child(uid).child("assistorModel").child("isRestTimerAlert");
                    //activeRef.setValue(b);
                    if(b){
                        flashUpdateButton();
                    }
                }else{
                    justVibrateNoUpdate = false;
                }
            }
        });

        justVibrateRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(!justVibrateNoUpdate){
                    //DatabaseReference activeRef =
                    //        FirebaseDatabase.getInstance().getReference().child
                    //        ("runningAssistor").
                    //                child(uid).child("assistorModel").child("isRestTimerAlert");
                    //activeRef.setValue(b);
                    if(b){
                        flashUpdateButton();
                    }
                }else{
                    justVibrateNoUpdate = false;
                }
            }
        });

        confirmRestTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(timer != null){
                    timer.cancel();
                }
                updateWorkoutStateSnackbarAndInitialize(false);
            }
        });

        resetWorkoutProgressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // set title
                builder.setTitle("Reset Workout Progress?");

                // set dialog message
                builder
                        .setMessage("Caution!\nThis will reset your workout progress to its original state.")
                        .setCancelable(false)
                        .setPositiveButton("Reset",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                cleanUpState();
                                noProgressInflateViews();
                                //runningAssistorRef.setValue(null);
                                //noProgressInflateViews();
                                //runningAssistorRef.removeEventListener(runningAssistorListener);
                                //runningAssistorRef.setValue(null);
                                //Intent intent = new Intent(getActivity(), MainActivity.class);
                                //intent.putExtra("fragID",  2);
                                //startActivity(intent);
                            }
                        })
                        .setNegativeButton("Don't reset",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        });

        addExButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = 101;
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);

                // IMPORTANT SHIT BELOW. DO NOT DELETE.
                //exNameInc++;
                //String tag = String.valueOf(exNameInc) + "ex";
                //android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                //ExNameWAFrag exNameFrag = new ExNameWAFrag();
                //exNameFrag.isTemplateImperial = isTemplateImperial;
                //exNameFrag.fragTag = tag;
                //if (!getActivity().isFinishing()) {
                //    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                //    fragmentTransaction.commitAllowingStateLoss();
                //    getChildFragmentManager().executePendingTransactions();
                //    exNameFragList.add(exNameFrag);
                //    fragTagList.add(tag);
                //}
            }
        });

        saveImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(exNameFragList.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // set title
                    builder.setTitle("Error");

                    // set dialog message
                    builder.setMessage("At least one exercise must be present to complete workout")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.dismiss();

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = builder.create();

                    // show it
                    alertDialog.show();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                    //builder.setTitle("Finish?");
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_custom_1, null);
                    builder.setView(dialogView);
                    builder.setCancelable(false);

                    // create alert dialog
                    final AlertDialog alertDialog = builder.create();

                    TextView titleView = (TextView) dialogView.findViewById(R.id.titleView);
                    titleView.setText(R.string.finishWorkoutQuestion);

                    Button positiveButton = (Button) dialogView.findViewById(R.id.positiveView);
                    positiveButton.setText(R.string.finish);

                    Button negativeButton = (Button) dialogView.findViewById(R.id.negativeView);
                    negativeButton.setText(R.string.cancel);

                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String day = LocalDate.now().toString("dd");

                            double dayDouble = Double.parseDouble(day);

                            // possibly have rando number generate
                            alertDialog.dismiss();
                            finishWorkoutFromAd();

                            //if(dayDouble % (double) 3 == 0.01 && !isTutorialFirstTime){
                            //    alertDialog.dismiss();
                            //    finishWorkoutFromAd();
//
                            //    Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                            //    Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                            //        @Override
                            //        public void onInterstitialLoaded(boolean b) {
                            //            Log.i("appodeal", "loaded");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialFailedToLoad() {
                            //            Log.i("appodeal", "failed");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialShown() {
                            //            Log.i("appodeal", "shown");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialClicked() {
                            //            Log.i("appodeal", "clicked");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialClosed() {
                            //            Log.i("appodeal", "closed");
                            //        }
                            //    });
                            //}else{
                            //    alertDialog.dismiss();
                            //    finishWorkout();
                            //}



                        }
                    });

                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    // show it
                    alertDialog.show();

                    //Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                    //intent.putExtra("isRestDay", "no");
                    //startActivityForResult(intent, 1);
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(exNameFragList.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                    // set title
                    builder.setTitle("Error");

                    // set dialog message
                    builder.setMessage("At least one exercise must be present to complete workout")
                            .setCancelable(false)
                            .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,int id) {

                                    dialog.dismiss();

                                }
                            });

                    // create alert dialog
                    AlertDialog alertDialog = builder.create();

                    // show it
                    alertDialog.show();
                }else{

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


                    //builder.setTitle("Finish?");
                    LayoutInflater inflater = getActivity().getLayoutInflater();
                    View dialogView = inflater.inflate(R.layout.dialog_custom_1, null);
                    builder.setView(dialogView);
                    builder.setCancelable(false);

                    // create alert dialog
                    final AlertDialog alertDialog = builder.create();

                    TextView titleView = (TextView) dialogView.findViewById(R.id.titleView);
                    titleView.setText(R.string.finishWorkoutQuestion);

                    Button positiveButton = (Button) dialogView.findViewById(R.id.positiveView);
                    positiveButton.setText(R.string.finish);

                    Button negativeButton = (Button) dialogView.findViewById(R.id.negativeView);
                    negativeButton.setText(R.string.cancel);

                    /*
                     * Couple problems I'm seeing here.
                     * Why did another algoInfoDateMap get created?
                     * Why didn't the shit increase weight? because I set both dates to 2-07
                     */

                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String day = LocalDate.now().toString("dd");

                            double dayDouble = Double.parseDouble(day);

                            // possibly have rando number generate
                            alertDialog.dismiss();
                            finishWorkoutFromAd();

                            //if(dayDouble % (double) 3 == 0.01 && !isTutorialFirstTime){
                            //    alertDialog.dismiss();
                            //    finishWorkoutFromAd();
//
                            //    Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                            //    Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                            //        @Override
                            //        public void onInterstitialLoaded(boolean b) {
                            //            Log.i("appodeal", "loaded");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialFailedToLoad() {
                            //            Log.i("appodeal", "failed");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialShown() {
                            //            Log.i("appodeal", "shown");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialClicked() {
                            //            Log.i("appodeal", "clicked");
                            //        }
//
                            //        @Override
                            //        public void onInterstitialClosed() {
                            //            Log.i("appodeal", "closed");
                            //        }
                            //    });
                            //}else{
                            //    alertDialog.dismiss();
                            //    finishWorkout();
                            //}

                        }
                    });

                    negativeButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alertDialog.dismiss();
                        }
                    });

                    // show it
                    alertDialog.show();

                    //Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                    //intent.putExtra("isRestDay", "no");
                    //startActivityForResult(intent, 1);
                }
            }
        });

        saveProgressButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                updateWorkoutState();
            }
        });

        if(savedInstanceState == null){
            //activateStatusBarService.setVisibility(View.GONE);
            //deactivateLL.setVisibility(View.VISIBLE);
//
            //Intent startIntent = new Intent(getActivity(), AssistorServiceClass.class);
            //startIntent.putExtra("uid", uid);
            //startIntent.putExtra("userImperial", String.valueOf(isUserImperial));
            //getActivity().startService(startIntent);
        }

        activateStatusBarService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStatusBarService.setVisibility(View.GONE);
                deactivateLL.setVisibility(View.VISIBLE);

                Intent startIntent = new Intent(getActivity(), AssistorServiceClass.class);
                startIntent.putExtra("uid", uid);
                startIntent.putExtra("userImperial", String.valueOf(isUserImperial));
                getActivity().startService(startIntent);
            }
        });

        deactivateStatusBarService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStatusBarService.setVisibility(View.VISIBLE);
                deactivateLL.setVisibility(View.GONE);

                Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);

                getActivity().stopService(stopIntent);
            }
        });

        deactiveStatusBarImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStatusBarService.setVisibility(View.VISIBLE);
                deactivateLL.setVisibility(View.GONE);

                Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);

                getActivity().stopService(stopIntent);
            }
        });

        deactivateLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activateStatusBarService.setVisibility(View.VISIBLE);
                deactivateLL.setVisibility(View.GONE);

                Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);

                getActivity().stopService(stopIntent);
            }
        });

        return view;
    }

    ViewTreeObserver.OnGlobalLayoutListener layoutListener;

    private void setUpLayoutListener(){

        if(rootView != null){

            measureRect = new Rect();

            keyboardListener = new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    //you should cache this, onGlobalLayout can get called often
                    rootView.getWindowVisibleDisplayFrame(measureRect);
                    // measureRect.bottom is the position above soft keypad
                    int keypadHeight = rootView.getRootView().getHeight() - measureRect.bottom;

                    if (keypadHeight > 0) {
                        // keyboard is opened
                        mIsKeyboardVisible = true;
                    } else {
                        //store keyboard state to use in onBackPress if you need to
                        if(mIsKeyboardVisible){
                            mIsKeyboardVisible = false;
                            if(isInForeground){
                                updateWorkoutState();
                            }

                        }

                    }
                }
            };

            rootView.getViewTreeObserver().addOnGlobalLayoutListener(keyboardListener);

        }

    }


    // index will always be the last item checked, or the first item.

    private void cancelRevision(){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // set title
        builder.setTitle("Cancel Revision");

        // set dialog message
        builder
                .setMessage("Would you like to cancel this workout revision?")
                .setCancelable(false)
                .setPositiveButton("Cancel revision",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        activateStatusBarService.setVisibility(View.VISIBLE);
                        deactivateLL.setVisibility(View.GONE);

                        Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);

                        getActivity().stopService(stopIntent);
                        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference()
                                .child("runningAssistor").child(uid).child("assistorModel");
                        if(isFromRestDay){
                            runningRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("fragID",  2);
                                    startActivity(intent);
                                }
                            });
                        }else{
                            runningRef.child("isRevise").setValue(false).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(getActivity(), MainActivity.class);
                                    intent.putExtra("fragID",  2);
                                    startActivity(intent);
                                }
                            });
                        }
                    }
                })
                .setNegativeButton("Keep editing",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        dialog.cancel();
                    }
                });

        // create alert dialog
        AlertDialog alertDialog = builder.create();

        // show it
        alertDialog.show();
    }

    public void updateExName(String frag, String exName){
        getChildFragmentManager().executePendingTransactions();
        if(frag != null){
            for(ExNameWAFrag exNameWAFrag : exNameFragList){
                if(exNameWAFrag.fragTag.equals(frag)){
                    exNameWAFrag.setExName(exName);
                    updateWorkoutState();
                }
            }
        }
    }

    public void updateChildFragWeights(String frag, String exName, String weight){
        getChildFragmentManager().executePendingTransactions();
        if(frag != null){
            for(ExNameWAFrag exNameWAFrag : exNameFragList){
                if(exNameWAFrag.fragTag.equals(frag)){
                    if(exNameWAFrag.getExerciseName().equals(exName)){
                        exNameWAFrag.updateChildWeights(weight, false);
                        updateWorkoutState();
                    }
                }
            }
        }
    }

    public void updateChildFragWeights(String frag, String exName, String weight, String reps){
        getChildFragmentManager().executePendingTransactions();
        if(frag != null){
            for(ExNameWAFrag exNameWAFrag : exNameFragList){
                if(exNameWAFrag.fragTag.equals(frag)){
                    if(exNameWAFrag.getExerciseName().equals(exName)){
                        if(!weight.isEmpty()){
                            exNameWAFrag.updateChildWeights(weight, false);
                        }
                        if(!reps.isEmpty()){
                            exNameWAFrag.updateChildReps(reps);
                        }
                        updateWorkoutState();
                    }
                }
            }
        }
    }

    public void updateWorkoutStateNoProgress(){
        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);
        //dateTimeString = "2019-12-05";
        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();
        boolean completedBool = false; // obviously this will be set to true in assistor saved
        String mediaResource = "";

        boolean isW531fB = false;
        List<String> W531fBAssistanceList = new ArrayList<>();

        if(preMadeInfo != null){
            if(!preMadeInfo.isEmpty()){
                if(preMadeInfo.get("type") != null){
                    if(preMadeInfo.get("type").equals("W531fB")){
                        isW531fB = true;
                        W531fBAssistanceList = getAssistanceExList();
                    }
                }
            }
        }

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            if(isW531fB){
                if(W531fBAssistanceList.contains(exNameFrag.getExerciseName())){
                    setAssistanceExercise(exNameFrag.getExerciseName(), exNameFrag.getHighestChildWeight());
                }
            }
        }

        WorkoutProgressModelClass progressModelClass;

        if(isRevisedWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, isRevisedWorkout, isFromRestDay);
        }else if(isFreestyleWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, true, isFromRestDay);
        }else{
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    null, isRevisedWorkout, isFromRestDay);
        }

        if(!preMadeInfo.isEmpty()){
            progressModelClass.setPreMadeInfo(preMadeInfo);
        }

        progressModelClass.setTemplateName(templateName);

        if(secondsEditText.getText().toString().isEmpty() || secondsEditText.getText().toString().equals("0")){
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":00");
        }else{
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
        }
        progressModelClass.setIsActiveRestTimer(restTimerBool);

        if(secondsVibrateEditText.getText().toString().isEmpty()){
            progressModelClass.setVibrationTime("0");
        }else{
            progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
        }

        progressModelClass.setIsRunningImperial(isUserImperial);

        progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        workoutProgressModelClass = progressModelClass;
        cleanUpState();
        runningAssistorRef.setValue(progressModelClass);
        initializeViews();
    }

    public void updateWorkoutStateSnackbarAndInitialize(boolean initialize){
        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);
        //dateTimeString = "2019-12-05";
        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();
        boolean completedBool = false; // obviously this will be set to true in assistor saved
        String mediaResource = "";

        /*
            Where we at:
            Gotta duplicate/do something similar to this in the final finishWorkout method.
         */

        boolean isW531fB = false;
        List<String> W531fBAssistanceList = new ArrayList<>();

        if(preMadeInfo != null){
            if(!preMadeInfo.isEmpty()){
                if(preMadeInfo.get("type") != null){
                    if(preMadeInfo.get("type").equals("W531fB")){
                        isW531fB = true;
                        W531fBAssistanceList = getAssistanceExList();
                    }
                }
            }
        }

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            if(isW531fB){
                if(W531fBAssistanceList.contains(exNameFrag.getExerciseName())){
                    setAssistanceExercise(exNameFrag.getExerciseName(), exNameFrag.getHighestChildWeight());
                }
            }
        }

        WorkoutProgressModelClass progressModelClass;

        if(isRevisedWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, isRevisedWorkout, isFromRestDay);
        }else if(isFreestyleWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, true, isFromRestDay);
        }else{
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    null, isRevisedWorkout, isFromRestDay);
        }

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        if(!preMadeInfo.isEmpty()){
            progressModelClass.setPreMadeInfo(preMadeInfo);
        }

        progressModelClass.setTemplateName(templateName);

        if(secondsEditText.getText().toString().isEmpty() || secondsEditText.getText().toString().equals("0")){
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":00");
        }else{
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
        }
        progressModelClass.setIsActiveRestTimer(restTimerBool);

        if(secondsVibrateEditText.getText().toString().isEmpty()){
            progressModelClass.setVibrationTime("0");
        }else{
            progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
        }

        progressModelClass.setIsRunningImperial(isUserImperial);

        progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());
        workoutProgressModelClass = progressModelClass;

        runningAssistorRef.setValue(progressModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(initialize){
                    initializeViews();
                }else{
                    Snackbar.make(getView(), "Timer updated", Snackbar.LENGTH_SHORT).show();
                }

            }
        });

        //for(ExNameWAFrag exNameWAFrag : exNameFragList){
        //    removeFrag(exNameWAFrag.fragTag);
        //}

        /*
         * OK, so what we need to do is just convert the original template class to a running model, then inflate that.
         * Inflating the original, then deleting it, then inflating the running model is just too much overhead.
         */
    }

    public void updateWorkoutState(){
        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);
        //dateTimeString = "2019-12-05";
        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();
        boolean completedBool = false; // obviously this will be set to true in assistor saved
        String mediaResource = "";

        boolean isW531fB = false;
        List<String> W531fBAssistanceList = new ArrayList<>();

        if(preMadeInfo != null){
            if(!preMadeInfo.isEmpty()){
                if(preMadeInfo.get("type") != null){
                    if(preMadeInfo.get("type").equals("W531fB")){
                        isW531fB = true;
                        W531fBAssistanceList = getAssistanceExList();
                    }
                }
            }
        }

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            if(isW531fB){
                if(W531fBAssistanceList.contains(exNameFrag.getExerciseName())){
                    setAssistanceExercise(exNameFrag.getExerciseName(), exNameFrag.getHighestChildWeight());
                }
            }
        }

        WorkoutProgressModelClass progressModelClass;

        if(isRevisedWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, isRevisedWorkout, isFromRestDay);
        }else if(isFreestyleWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, true, isFromRestDay);
        }else{
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    null, isRevisedWorkout, isFromRestDay);
        }

        if(!preMadeInfo.isEmpty()){
            progressModelClass.setPreMadeInfo(preMadeInfo);
        }

        progressModelClass.setTemplateName(templateName);

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        if(secondsEditText.getText().toString().isEmpty() || secondsEditText.getText().toString().equals("0")){
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":00");
        }else{
            progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
        }
        progressModelClass.setIsActiveRestTimer(restTimerBool);

        if(secondsVibrateEditText.getText().toString().isEmpty()){
            progressModelClass.setVibrationTime("0");
        }else{
            progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
        }

        progressModelClass.setIsRunningImperial(isUserImperial);

        progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());

        workoutProgressModelClass = progressModelClass;

        runningAssistorRef.setValue(progressModelClass);

        //for(ExNameWAFrag exNameWAFrag : exNameFragList){
        //    removeFrag(exNameWAFrag.fragTag);
        //}

        /*
         * OK, so what we need to do is just convert the original template class to a running model, then inflate that.
         * Inflating the original, then deleting it, then inflating the running model is just too much overhead.
         */
    }

    final Handler handler = new Handler();

    public void updateWorkoutStateWithDelay(){

        /*
         * Current issue: when checking something off and then minimizing the app, duplicates (9) were added
         * to the db. first we'll try it without the delay, then possibly an (!getActivity.isFinishing) conditional
         */

        Log.i("fuckYou", "updateWorkoutStateWithDelay method call");
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.i("fuckYou", "updateWorkoutStateWithDelay method inner call");
                if(isInForeground){
                    DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                            ("assistorModel");
                    HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
                    int inc = 0;
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now();
                    String dateTimeString = fmt.print(localDate);
                    String privateJournal = privateJournalView.getText().toString();
                    String publicComment = publicCommentView.getText().toString();
                    boolean completedBool = false; // obviously this will be set to true in assistor saved
                    String mediaResource = "";

                    boolean isW531fB = false;
                    List<String> W531fBAssistanceList = new ArrayList<>();

                    if(preMadeInfo != null){
                        if(!preMadeInfo.isEmpty()){
                            if(preMadeInfo.get("type") != null){
                                if(preMadeInfo.get("type").equals("W531fB")){
                                    isW531fB = true;
                                    W531fBAssistanceList = getAssistanceExList();
                                }
                            }
                        }
                    }

                    // might need to make this not clickable without inflated views so it isn't set to null
                    for(ExNameWAFrag exNameFrag : exNameFragList){
                        inc++;
                        //for(int i = 1; i <= exNameFragList.size(); i++){}
                        runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
                        if(isW531fB){
                            if(W531fBAssistanceList.contains(exNameFrag.getExerciseName())){
                                setAssistanceExercise(exNameFrag.getExerciseName(), exNameFrag.getHighestChildWeight());
                            }
                        }
                    }

                    WorkoutProgressModelClass progressModelClass;

                    if(isRevisedWorkout){
                        progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                                completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                                refKey, isRevisedWorkout, isFromRestDay);
                    }else if(isFreestyleWorkout){
                        progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                                completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                                refKey, true, isFromRestDay);
                    }else{
                        progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                                completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                                null, isRevisedWorkout, isFromRestDay);
                    }

                    if(!preMadeInfo.isEmpty()){
                        progressModelClass.setPreMadeInfo(preMadeInfo);
                    }

                    progressModelClass.setTemplateName(templateName);

                    if(secondsEditText.getText().toString().isEmpty() || secondsEditText.getText().toString().equals("0")){
                        progressModelClass.setRestTime(minutesEditText.getText().toString() + ":00");
                    }else{
                        progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
                    }
                    progressModelClass.setIsActiveRestTimer(restTimerBool);

                    if(secondsVibrateEditText.getText().toString().isEmpty()){
                        progressModelClass.setVibrationTime("0");
                    }else{
                        progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
                    }

                    progressModelClass.setIsRunningImperial(isUserImperial);

                    progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());
                    workoutProgressModelClass = progressModelClass;

                    runningAssistorRef.setValue(progressModelClass);
                }
            }
        }, 1500);

    }


    public void updateWorkoutStateForResult(final String exNameFragTag, final String repsWeightFragTag){
        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
        LocalDate localDate = LocalDate.now();
        String dateTimeString = fmt.print(localDate);
        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();
        boolean completedBool = false; // obviously this will be set to true in assistor saved
        String mediaResource = "";

        boolean isW531fB = false;
        List<String> W531fBAssistanceList = new ArrayList<>();

        if(preMadeInfo != null){
            if(!preMadeInfo.isEmpty()){
                if(preMadeInfo.get("type") != null){
                    if(preMadeInfo.get("type").equals("W531fB")){
                        isW531fB = true;
                        W531fBAssistanceList = getAssistanceExList();
                    }
                }
            }
        }

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            if(isW531fB){
                if(W531fBAssistanceList.contains(exNameFrag.getExerciseName())){
                    setAssistanceExercise(exNameFrag.getExerciseName(), exNameFrag.getHighestChildWeight());
                }
            }
        }

        WorkoutProgressModelClass progressModelClass;

        if(isRevisedWorkout){
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    refKey, isRevisedWorkout, isFromRestDay);
        }else{
            progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                    completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial,
                    null, isRevisedWorkout, isFromRestDay);
        }

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        if(!preMadeInfo.isEmpty()){
            progressModelClass.setPreMadeInfo(preMadeInfo);
        }

        progressModelClass.setTemplateName(templateName);

        progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
        progressModelClass.setIsActiveRestTimer(restTimerBool);

        if(secondsVibrateEditText.getText().toString().isEmpty()){
            progressModelClass.setVibrationTime("0");
        }else{
            progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
        }

        progressModelClass.setIsRunningImperial(isUserImperial);

        progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());
        workoutProgressModelClass = progressModelClass;

        runningAssistorRef.setValue(progressModelClass).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                for(ExNameWAFrag exNameWAFrag : exNameFragList){
                    if(exNameWAFrag.fragTag.equals(exNameFragTag)){
                        exNameWAFrag.setCheckedSuccess(repsWeightFragTag);
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

        //for(ExNameWAFrag exNameWAFrag : exNameFragList){
        //    removeFrag(exNameWAFrag.fragTag);
        //}

        /*
         * OK, so what we need to do is just convert the original template class to a running model, then inflate that.
         * Inflating the original, then deleting it, then inflating the running model is just too much overhead.
         */


    }

    @Override
    public void onResume(){
        Log.i("lifecycleAssistor", "AssistorHolderFrag onResume called");
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(serviceReceiver,
                new IntentFilter("serviceMessage"));
        if(mTemplateClass == null){
            if(isRevisedWorkout || isFreestyleWorkout){
                //initializeViews();
                checkForOldData();
                super.onResume();
            }else{
                Log.i("assistorInfo", "templateClass is null (onResume)");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("fragID",  2);
                startActivity(intent);
                super.onResume();
            }
        }else{
            Log.i("assistorInfo", "AssistorHolderFrag (onResume)");
            checkIfUserIsImperial();
            checkForOldData();
            super.onResume();
        }
        //if(runningAssistorListener == null){
        //    checkForOldData();
        //}else{
        //    //runningAssistorRef.addValueEventListener(runningAssistorListener);
        //}
    }

    private BroadcastReceiver serviceReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("message");
            if(message != null){
                if(message.equals("message")){
                    initializeViews();
                }
            }
        }
    };

    private void setPlaceHolderTemplate(){
        DatabaseReference placeholderRef = mRootRef.child("defaultTemplates").child("FirstTimeProgram");
        placeholderRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TemplateModelClass placeholder = dataSnapshot.getValue(TemplateModelClass.class);
                mTemplateClass = placeholder;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkForOldData(){
        if(mTemplateClass != null){
            Log.i("assistorInfo", "templateClass is not null");
            //cleanUpState();
            isTemplateImperial = mTemplateClass.isIsImperial();
            initializeViews();
        }else{
            if(isRevisedWorkout){
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference()
                        .child("user").child(uid).child("activeTemplate");
                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            String templateName = dataSnapshot.getValue(String.class);
                            DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference()
                                    .child("templates").child(uid).child(templateName);

                            templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mTemplateClass = dataSnapshot.getValue(TemplateModelClass.class);
                                    initializeViews();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }else{
                            // is freestyle revised
                            DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                            LocalDate localDate = LocalDate.now();
                            final String dateTimeString = fmt.print(localDate);
                            DatabaseReference runningRef =
                                    mRootRef.child("runningAssistor").child(uid).child("assistorModel");
                            runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    WorkoutProgressModelClass progressModelClass = dataSnapshot.getValue
                                            (WorkoutProgressModelClass.class);

                                    if(progressModelClass.getDate().equals(dateTimeString)){
                                        isFreestyleWorkout = true;
                                        if(mTemplateClass == null){
                                            setPlaceHolderTemplate();
                                        }
                                        initializeViews();
                                    }else{
                                        Log.i("assistorInfo", "templateClass is null");
                                        Intent intent = new Intent(getActivity(), MainActivity.class);
                                        intent.putExtra("fragID",  0);
                                        startActivity(intent);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }else{
                /*
                 * Where we are now: we have the freestyle set up in running assistor. may need
                 * to make it no have is from rest day/revise, not sure.
                 * But right now the issue is that there is no associated template with it.
                 * Look at initializeViews and mTemplateClass and how we can make it see this as
                 * a freestyle.
                 */
                DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.now();
                final String dateTimeString = fmt.print(localDate);
                DatabaseReference runningRef =
                        mRootRef.child("runningAssistor").child(uid).child("assistorModel");
                runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        WorkoutProgressModelClass progressModelClass = dataSnapshot.getValue
                                (WorkoutProgressModelClass.class);
                        if(isFreestyleWorkout){
                            isFreestyleWorkout = true;
                            initializeViews();
                        }else{
                            Log.i("assistorInfo", "templateClass is null");
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            intent.putExtra("fragID",  0);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                //Log.i("assistorInfo", "templateClass is null");
                //Intent intent = new Intent(getActivity(), MainActivity.class);
                //intent.putExtra("fragID",  0);
                //startActivity(intent);
                //cleanUpState();
                //DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid).child("activeTemplate");
                //activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                //    @Override
                //    public void onDataChange(DataSnapshot dataSnapshot) {
                //        String templateName = dataSnapshot.getValue(String.class);
                //        if(templateName != null){
                //            DatabaseReference activeTemplateClassRef = mRootRef.child("templates").child(uid).child
                //                    (templateName);
                //            activeTemplateClassRef.addListenerForSingleValueEvent(new ValueEventListener() {
                //                @Override
                //                public void onDataChange(DataSnapshot dataSnapshot) {
                //                    mTemplateClass = dataSnapshot.getValue(TemplateModelClass.class);
                //                    isTemplateImperial = mTemplateClass.isIsImperial();
                //                    initializeViews();
                //                }
//
                //                @Override
                //                public void onCancelled(DatabaseError databaseError) {
//
                //                }
                //            });
                //        }else{
//
                //        }
//
                //    }
//
                //    @Override
                //    public void onCancelled(DatabaseError databaseError) {
//
                //    }
                //});
            }
        }
    }

    /*
     * Edge case and set/ex deletion time.
     */

    @Override
    public void onStart(){
        super.onStart();
        isInForeground = true;
        setButtonsForService();
        setUpLayoutListener();
    }

    private void setButtonsForService(){
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context
                .ACTIVITY_SERVICE);
        if(manager.getRunningServices(Integer.MAX_VALUE) != null){
            for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
                if(AssistorServiceClass.class.getName().equals(serviceInfo.service.getClassName())){
                    activateStatusBarService.setVisibility(View.GONE);
                    deactivateLL.setVisibility(View.VISIBLE);
                }
            }
        }else{
            activateStatusBarService.setVisibility(View.VISIBLE);
            deactivateLL.setVisibility(View.GONE);
        }
    }

    private boolean isServiceUp(){
        boolean isUp = false;

        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context
                .ACTIVITY_SERVICE);
        if(manager.getRunningServices(Integer.MAX_VALUE) != null){
            for(ActivityManager.RunningServiceInfo serviceInfo : manager.getRunningServices(Integer.MAX_VALUE)){
                if(AssistorServiceClass.class.getName().equals(serviceInfo.service.getClassName())){
                    isUp = true;
                }
            }
        }

        return isUp;
    }

    private void wasInOnStartNowDeleted(){
        final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference()
                .child("firstTime").child(uid).child("isAssistorFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    isTutorialFirstTime = true;

                    FancyShowCaseView fancyShowCaseView = new FancyShowCaseView.Builder(getActivity())
                            .title("This is the Workout Assistor!" +
                                    "\n It's where you will complete workouts and rest days." +
                                    "\n You can use the notification bar functionality to complete sets without " +
                                    "having to unlock your phone or be in the app itself!")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build();

                    FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
                            .title("Check off all of the sets that you complete. \n \n" +
                                    "You can also freestyle with it and add/remove exercises and sets.\n \n" +
                                    "Remember, you want to most accurately document your workout!")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build();

                    FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
                            .title("The Private Journal is where you can take workout notes. It'll only be viewable " +
                                    "by you. \n \n " +
                                    "The Public Description is what your followers will see as the " +
                                    "description for this workout.\n \n" +
                                    "Good luck and have fun!")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build();

                    new FancyShowCaseQueue()
                            .add(fancyShowCaseView)
                            .add(fancyShowCaseView1)
                            .add(fancyShowCaseView2)
                            .show();

                    firstTimeRef.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onPause(){
        Log.i("lifecycleAssistor", "AssistorHolderFrag onPause called");
        //runningAssistorRef.removeEventListener(runningAssistorListener);

        super.onPause();
    }

    @Override
    public void onStop(){
        Log.i("lifecycleAssistor", "AssistorHolderFrag onStop called");
        //if(runningAssistorRef != null){
        //    runningAssistorRef.removeEventListener(runningAssistorListener);
        //}
        isInForeground = false;
        if(rootView != null && keyboardListener != null){
            rootView.getViewTreeObserver().removeOnGlobalLayoutListener(keyboardListener);
        }

        super.onStop();
    }
//
    @Override
    public void onDestroy(){
        //if(runningAssistorRef != null){
        //    runningAssistorRef.removeEventListener(runningAssistorListener);
        //}
        isInForeground = false;
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(serviceReceiver);
        super.onDestroy();
    }

    private void checkIfUserIsImperial(){
        DatabaseReference userImperialRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                .child("isImperial");
        userImperialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                isUserImperial = dataSnapshot.getValue(Boolean.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void processPreMadeInfo(){
        if(preMadeInfo.get("type").equals("Smolov")){
            if(preMadeInfo.get("oneRepMaxDay").equals("true")){
                extraInfoTextView.setText(R.string.oneRepMaxDay);
                extraInfoTextView.setVisibility(View.VISIBLE);
            }
        }
    }


    private void initializeViews(){
        /*
         * So what I'm thinking right now is to automatically update to the running assistor, and have both the WA
         * and the Service setting/getting continuously from that node. We shall see.
         * just trying some things out. thinking about ways of implementing this WA/Service symbiosis.
         * I'm excited though! Could make it so we don't have to deal with the save button AND get a notification bar working.
         */

        /*
         * This was folly. It's laggy and doesn't allow for offline use. Also there is a problem
         * with calling update when no views are there/views are not properly inflated.
         *
         * What we're going to do:
         *
         */

        DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        runningAssistorRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //if(isListening){
                    if(dataSnapshot.exists()){
                        Log.i("assistorInfo", "runningAssistor triggered/exists");
                        DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                        LocalDate localDate = LocalDate.now();
                        String dateTimeString = fmt.print(localDate);
                        //dateTimeString = "2019-12-05";
                        workoutProgressModelClass = dataSnapshot.getValue(WorkoutProgressModelClass.class);
                        if(dateTimeString.equals(workoutProgressModelClass.getDate())){
                            if(!workoutProgressModelClass.isCompletedBool()){
                                Log.i("assistorInfo", "runningAssistor confirmed");
                                cleanUpState();
                                isTemplateImperial = workoutProgressModelClass.isIsTemplateImperial();
                                isRunningImperial = workoutProgressModelClass.isIsRunningImperial();
                                isFromRestDay = workoutProgressModelClass.isIsFromRestDay();
                                if(isFromRestDay){
                                    cancelRevisionHolder.setVisibility(View.VISIBLE);
                                }
                                if(workoutProgressModelClass.getPreMadeInfo() != null){
                                    if(!workoutProgressModelClass.getPreMadeInfo().isEmpty()){
                                        preMadeInfo = workoutProgressModelClass.getPreMadeInfo();
                                    }
                                }
                                if(workoutProgressModelClass.getExInfoHashMap() == null){
                                    cleanUpState();
                                    noProgressInflateViews();
                                }else{
                                    savedProgressInflateViews(workoutProgressModelClass.getExInfoHashMap(), workoutProgressModelClass.getPrivateJournal(),
                                            workoutProgressModelClass.getPublicComment(), workoutProgressModelClass.isIsTemplateImperial(),
                                            workoutProgressModelClass.isIsRunningImperial());
                                }
                            }else{
                                if(exNameFragList.isEmpty()){
                                    cleanUpState();
                                    noProgressInflateViews();
                                }else{
                                    noProgressInflateViews();
                                }
                            }
                        }else{
                            if(exNameFragList.isEmpty()){
                                cleanUpState();
                                noProgressInflateViews();
                            }else{
                                noProgressInflateViews();
                            }
                        }
                    }else{
                        if(exNameFragList.isEmpty()){
                            cleanUpState();
                            noProgressInflateViews();
                        }else{
                            noProgressInflateViews();
                        }
                    }
                //}

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void cleanUpState(){
        Log.i("assistorInfo", "cleanUpState called (assistor holder)");
        try{
            for(ExNameWAFrag frag : exNameFragList){
                frag.cleanUpSubFrags();
            }
            exNameFragList.clear();
            //getChildFragmentManager().executePendingTransactions();
            for(String tag : fragTagList){
                if(getChildFragmentManager().findFragmentByTag(tag) != null){
                    android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                }
            }
            exNameInc = 0;
            fragTagList.clear();
        }catch (IllegalStateException e){
            //Intent intent = new Intent(getActivity(), MainActivity.class);
            //intent.putExtra("fragID",  2);
            //startActivity(intent);
        }
    }

    private void setUpFirebaseAdapter(){
        //Log.i("assistorInfo", "setUpFirebaseAdapter called");
        //mFirebaseAdapter = new FirebaseRecyclerAdapter<WorkoutProgressModelClass, WorkoutProgressViewHolder>
        //        (WorkoutProgressModelClass.class, R.layout.workout_progress_parent_item,
        //                WorkoutProgressViewHolder.class, mRunningAssistorRef) {
        //    @Override
        //    protected void populateViewHolder(WorkoutProgressViewHolder viewHolder, WorkoutProgressModelClass model,
        //    int position) {
        //        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        //        viewHolder.setFragment(AssistorHolderFrag.this);
        //        viewHolder.setFragmentTransaction(fragmentTransaction);
        //        viewHolder.setWorkoutProgressModel(model);
        //        Log.i("assistorInfo", "setUpFirebaseAdapter populateViewHolder");
        //    }
        //};
//
        //mRecyclerView.setHasFixedSize(false);
        //mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private void finishWorkout(){
        updateWorkoutState();
        List<String> exInfo = new ArrayList<>();
        for(ExNameWAFrag exNameFrag : exNameFragList){
            exInfo.addAll(exNameFrag.getExInfo());
        }
        AssistorSingleton.getInstance().endList.clear();
        AssistorSingleton.getInstance().endList.addAll(exInfo);

        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        for(ExNameWAFrag exNameFrag : exNameFragList){
            if(!hasOnlyExNames(exNameFrag.getInfoForMap())){
                inc++;
                runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            }
        }

        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();

        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AssistorSavedFrag assistorSavedFrag = new AssistorSavedFrag();
        if(isTutorialFirstTime){
            assistorSavedFrag.isFirstTimeFirstTime = true;
        }
        if(premadeWeekDayString != null){
            assistorSavedFrag.premadeWeekDayString = premadeWeekDayString;
        }
        assistorSavedFrag.templateClass = mTemplateClass;
        assistorSavedFrag.completedMap = runningMap;
        assistorSavedFrag.privateJournal = privateJournal;
        assistorSavedFrag.publicDescription = publicComment;
        assistorSavedFrag.isFromRestDay = isFromRestDay;
        //assistorSavedFrag.isLastDay = isLastDay;
        assistorSavedFrag.preMadeInfo = preMadeInfo;
        if(isRevisedWorkout){
            assistorSavedFrag.isRevisedWorkout = true;
            assistorSavedFrag.redoRefKey = refKey;
        }
        //if(runningAssistorRef != null){
        //    runningAssistorRef.removeEventListener(runningAssistorListener);
        //}
        fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
        fragmentTransaction.commit();
    }

    private void finishWorkoutFromAd(){
        updateWorkoutState();
        List<String> exInfo = new ArrayList<>();
        for(ExNameWAFrag exNameFrag : exNameFragList){
            exInfo.addAll(exNameFrag.getExInfo());
        }
        AssistorSingleton.getInstance().endList.clear();
        AssistorSingleton.getInstance().endList.addAll(exInfo);

        HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
        int inc = 0;
        for(ExNameWAFrag exNameFrag : exNameFragList){
            if(!hasOnlyExNames(exNameFrag.getInfoForMap())){
                inc++;
                runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
            }
        }

        String privateJournal = privateJournalView.getText().toString();
        String publicComment = publicCommentView.getText().toString();

        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        AssistorSavedFrag assistorSavedFrag = new AssistorSavedFrag();
        if(isTutorialFirstTime){
            assistorSavedFrag.isFirstTimeFirstTime = true;
        }
        if(premadeWeekDayString != null){
            assistorSavedFrag.premadeWeekDayString = premadeWeekDayString;
        }
        assistorSavedFrag.templateClass = mTemplateClass;
        assistorSavedFrag.completedMap = runningMap;
        assistorSavedFrag.privateJournal = privateJournal;
        assistorSavedFrag.publicDescription = publicComment;
        assistorSavedFrag.isFromRestDay = isFromRestDay;
        assistorSavedFrag.preMadeInfo = preMadeInfo;
        if(isRevisedWorkout){
            assistorSavedFrag.isRevisedWorkout = true;
            assistorSavedFrag.redoRefKey = refKey;
        }
        if(isFreestyleWorkout){
            assistorSavedFrag.isFreestyle = true;
        }
        //if(runningAssistorRef != null){
        //    runningAssistorRef.removeEventListener(runningAssistorListener);
        //}
        isListening = false;
        assistorSavedFrag.isFromAd = true;
        fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
        fragmentTransaction.commit();
    }

    List<String> getAssistanceExList(){
        List<String> W531fBAssistanceList = new ArrayList<>();

        if(preMadeInfo != null) {
            if (!preMadeInfo.isEmpty()) {
                if (preMadeInfo.get("type") != null) {
                    if (preMadeInfo.get("type").equals("W531fB")) {
                        List<String> dummyList = new ArrayList<>();
                        dummyList.add("TMIncreaseWeek");
                        dummyList.add("SpecialWeek");
                        dummyList.add("Squat (Barbell - Back)");
                        dummyList.add("Bench Press (Barbell - Flat)");
                        dummyList.add("Overhead Press (Barbell)");
                        dummyList.add("Deadlift (Barbell - Conventional)");
                        dummyList.add("whichDay");
                        for(Map.Entry<String, String> entry : preMadeInfo.entrySet()){
                            if(!dummyList.contains(entry.getKey())){
                                W531fBAssistanceList.add(entry.getKey());
                            }
                        }
                    }
                }
            }
        }

        return W531fBAssistanceList;
    }

    private void savedProgressInflateViews(HashMap<String, HashMap<String, List<String>>> runningMap, String
            privateJournal, String publicComment, boolean isTemplateImperial1,
                                           boolean isRunningImperial1){

        Log.i("assistorInfo", "savedProgressInflateViews");

        if(workoutProgressModelClass.getRestTime() != null){
            String delims = "[:]";
            String[] tokens = workoutProgressModelClass.getRestTime().split(delims);
            minutesEditText.setText(tokens[0]);
            secondsEditText.setText(tokens[1]);
        }

        if(workoutProgressModelClass.getTemplateName() != null){
            templateName = workoutProgressModelClass.getTemplateName();
        }

        justVibrateNoUpdate = true;
        if(workoutProgressModelClass.getVibrationTime() != null){
            secondsVibrateEditText.setText(workoutProgressModelClass.getVibrationTime());
            if(workoutProgressModelClass.isIsRestTimerAlert()){
                //justVibrateRB.setChecked(false);
                showRestTimerAlertRB.setChecked(true);
            }else{
                justVibrateRB.setChecked(true);
                //showRestTimerAlertRB.setChecked(false);
            }
            justVibrateNoUpdate = false;
        }

        boolean isW531fB = false;
        List<String> W531fBAssistanceList = new ArrayList<>();

        boolean isPPLReddit = false;

        if(preMadeInfo != null){
            if(!preMadeInfo.isEmpty()){
                if(preMadeInfo.get("type") != null){
                    if(preMadeInfo.get("type").equals("W531fB")){
                        isW531fB = true;
                        W531fBAssistanceList = getAssistanceExList();
                        if(preMadeInfo.get("TMIncreaseWeek") != null){
                            if(preMadeInfo.get("TMIncreaseWeek").equals("true")){
                                extraInfoTextView.setText(R.string.W5314BIncreaseWeekAlert);
                                extraInfoTextView.setVisibility(View.VISIBLE);
                            }
                        }
                        if(preMadeInfo.get("SpecialWeek") != null){
                            if(preMadeInfo.get("SpecialWeek").equals("true")){
                                extraInfoTextView.setText(R.string.W5314BSpecialWeekInstruction);
                                extraInfoTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }else if(preMadeInfo.get("type").equals("PPLReddit")){
                        isPPLReddit = true;
                        if(preMadeInfo.get("firstWeek") != null){
                            if(preMadeInfo.get("firstWeek").equals("true")){
                                extraInfoTextView.setText(R.string.PPLRedditFirstWeekInstruction);
                                extraInfoTextView.setVisibility(View.VISIBLE);
                            }
                        }
                    }else if(preMadeInfo.get("type").equals("Smolov")){
                        if(preMadeInfo.get("oneRepMaxDay") != null){
                            if(preMadeInfo.get("oneRepMaxDay").equals("true")){
                                extraInfoTextView.setText(R.string.oneRepMaxDay);
                                extraInfoTextView.setVisibility(View.VISIBLE);
                            }
                        }
                        if(preMadeInfo.get("isLastDay") != null){
                            if(preMadeInfo.get("isLastDay").equals("true")){
                                endView.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }
        }

        restTimerNoUpdate = true;
        restTimerSwitch.setChecked(workoutProgressModelClass.isIsActiveRestTimer());
        if(!restTimerSwitch.isChecked()){
            restTimerBool = false;
            restTimerInfoLL.setVisibility(View.GONE);
        }else{
            restTimerBool = true;
            restTimerInfoLL.setVisibility(View.VISIBLE);
        }

        if(runningMap != null){
            for(int i = 0; i < runningMap.size(); i++){
                if(i == 0){
                    loadingView.setVisibility(View.GONE);
                    restTimerLL.setVisibility(View.VISIBLE);
                    serviceCardView.setVisibility(View.VISIBLE);
                }
                for(Map.Entry<String, HashMap<String, List<String>>> entry : runningMap.entrySet()) {
                    if(isOfIndex(i, entry.getKey())){
                        exNameInc++;
                        String tag = String.valueOf(exNameInc) + "ex";
                        HashMap<String, List<String>> exerciseMap = entry.getValue();
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        //exNameFrag.isTemplateImperial = isTemplateImperial1;
                        if(isW531fB){
                            for(Map.Entry<String, List<String>> entry1 : exerciseMap.entrySet()){
                                if(W531fBAssistanceList.contains(entry1.getValue().get(0))){
                                    exNameFrag.isAssistanceW531fB = true;
                                }
                            }
                        }
                        if(isPPLReddit){
                            exNameFrag.isFirstTimePPLR = isSetZeroWeightMap(exerciseMap);
                        }
                        exNameFrag.isPPLReddit = isPPLReddit;
                        exNameFrag.isTemplateImperial = isRunningImperial1;
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.isEditInfoList = exerciseMap;
                        exNameFrag.fragTag = tag;
                        exNameFrag.isEdit = true;
                        exNameFrag.templateName = templateName;
                        exNameFrag.topLevelKey = entry.getKey();
                        if(getActivity() != null){
                            if (!getActivity().isFinishing()) {
                                fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                getChildFragmentManager().executePendingTransactions();
                                exNameFragList.add(exNameFrag);
                                fragTagList.add(tag);
                            }
                        }
                    }
                }
            }

            privateJournalView.setText(privateJournal);
            publicCommentView.setText(publicComment);
        }else{
            loadingView.setVisibility(View.GONE);
            //HashMap<String, HashMap<String, List<String>>> runningMap2 = new HashMap<>();
//
            //HashMap<String, List<String>> subMap = new HashMap<>();
//
            //List<String> subList = new ArrayList<>();
//
            //subList.add("CLICK TO CHOOSE EXERCISE");
            //subList.add("1@1_unchecked");
//
            //subMap.put("0_key", subList);
//
            //runningMap2.put("1_key", subMap);
//
            //DatabaseReference mapRef = FirebaseDatabase.getInstance().getReference()
            //        .child("runningAssistor").child(uid).child("assistorModel").child("exInfoHashMap");
            //mapRef.setValue(runningMap2);
        }
    }

    private void processPreMadeProgram(){
        if(mTemplateClass.getWorkoutType().equals("Smolov")){
            inflateSmolov();
        }else if(mTemplateClass.getWorkoutType().equals("W531fB")){
            inflateW531fB();
        }else if(mTemplateClass.getWorkoutType().equals("PPLReddit")){
            inflatePPLReddit();
        }
    }

    private void inflatePPLReddit(){
        PPLRedditClass pplReddit = new PPLRedditClass(mTemplateClass.getExtraInfo());
        HashMap<String, List<String>> map = pplReddit.generateWorkoutMap();

        if(pplReddit.isFirstWeek()){
            extraInfoTextView.setText(R.string.PPLRedditFirstWeekInstruction);
            extraInfoTextView.setVisibility(View.VISIBLE);
            preMadeInfo.put("firstWeek", "true");
        }else{
            preMadeInfo.put("firstWeek", "false");
        }

        preMadeInfo.put("type", "PPLReddit");
        preMadeInfo.put("version", pplReddit.getVersion());

        preMadeInfo.putAll(pplReddit.getTodayExercises());

        /**
         * Let's actually think about what we need to do.
         *
         * We need the exname frags to know that they are PPLR.
         *  - So that they can pulse the extra options button.
         *
         * How can we handle knowing which exercise pool it is when some have the same exercises
         * in it?
         *
         * The real redpill is that we might not be able to have exercises change on the fly.
         *
         * We still need to know in AS what exercises we're working with that day, and they'll be
         * just like the are in template. Key is shortened exName, value is real exName.
         *
         * Easily could do this by generating a map of those names in generateWorkout.
         *
         * If it's pplr show little notification on exname click that says you can change ex in
         * editor.
         *
         * What do we do once in AS though?
         *
         * https://www.youtube.com/watch?v=thj44G9-8SQ
         * https://www.youtube.com/watch?v=b3oTBUkLDXo
         * https://www.youtube.com/watch?v=xGCUnOi2Dv0
         *
         */

        for(int i = 0; i < map.size(); i++) {
            if (i == 0) {
                loadingView.setVisibility(View.GONE);
                restTimerLL.setVisibility(View.VISIBLE);
            }
            for (Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (!entry.getKey().equals("0_key")) {
                    if (isOfIndex(i, entry.getKey())) {
                        exNameInc++;
                        String tag = String.valueOf(exNameInc) + "ex";
                        List<String> stringList = entry.getValue();
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        exNameFrag.infoList = stringList;
                        if(isSetZeroWeight(stringList)){
                            exNameFrag.isFirstTimePPLR = true;
                        }
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.fragTag = tag;
                        exNameFrag.templateName = templateName;
                        exNameFrag.isPPLReddit = true;
                        exNameFrag.topLevelKey = entry.getKey();
                        if (getActivity() != null) {
                            if (!getActivity().isFinishing()) {
                                fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                getChildFragmentManager().executePendingTransactions();
                                exNameFragList.add(exNameFrag);
                                fragTagList.add(tag);
                            }
                        }

                    }
                }
            }
            if(i == (map.size() - 1)){
                updateWorkoutStateNoProgress();
                serviceCardView.setVisibility(View.VISIBLE);
            }
        }

    }

    private boolean isSetZeroWeight(List<String> stringList){
        boolean isZeroWeight;

        String delims = "[@]";
        String[] tokens = stringList.get(1).split(delims);

        if(tokens[tokens.length - 1].equals("0")){
            isZeroWeight = true;
        }else{
            isZeroWeight = false;
        }

        return isZeroWeight;
    }

    private boolean isSetZeroWeightMap(HashMap<String, List<String>> exerciseMap){
        boolean isZeroWeight = true;

        for(Map.Entry<String, List<String>> entry : exerciseMap.entrySet()){
            int i = 0;
            for(String value : entry.getValue()){
                if(i != 0){
                    String delims = "[@]";
                    String[] tokens = value.split(delims);
                    String delims2 = "[_]";
                    String[] tokens2 = tokens[tokens.length - 1].split(delims2);
                    if(!tokens2[0].equals("0")){
                        isZeroWeight = false;
                    }
                }
                i++;
            }
        }

        return isZeroWeight;
    }

    private void inflateW531fB(){
        Wendler_531_For_Beginners W531fB = new Wendler_531_For_Beginners(
                mTemplateClass.getExtraInfo());
        HashMap<String, List<String>> map = W531fB.generateWorkoutMap();

        //HashMap<String, List<String>> map = W531fB.generateSpecificWorkoutMap(10, 1);

        if(W531fB.isTMIncreaseWeek()){
            extraInfoTextView.setText(R.string.W5314BIncreaseWeekAlert);
            extraInfoTextView.setVisibility(View.VISIBLE);
            preMadeInfo.clear();
            preMadeInfo.put("TMIncreaseWeek", "true");
            preMadeInfo.put("SpecialWeek", "false");
            preMadeInfo.put("whichDay", W531fB.getWhichDay());
            /*
             * If it's TM increase day, we need to send that to AS in preMadeInfo
             * Send type W531fB
             * Send if it's special day.
             *  If it's special day, in AS we need to read through for the set which has a weight of
             *  100% of TM or higher. Then see what that set's reps are and manipulate TM from there.
             *  So we need to also send the current TM of the current Exercise/s.
             */
        }else{
            preMadeInfo.clear();
            preMadeInfo.put("TMIncreaseWeek", "false");
        }

        boolean isSpecialWeek = false;

        if(W531fB.isSpecialWeek()){
            extraInfoTextView.setText(R.string.W5314BSpecialWeekInstruction);
            extraInfoTextView.setVisibility(View.VISIBLE);
            // in AS we should tell them how their TM changed.
            // so either way, this or TMIncrease, we'll be sending in the current TM.
            preMadeInfo.clear();
            preMadeInfo.put("SpecialWeek", "true");
            preMadeInfo.put("TMIncreaseWeek", "false");
            preMadeInfo.put("whichDay", W531fB.getWhichDay());
            isSpecialWeek = true;
            //HashMap<String, String> exercisesAndTMs = W531fB.exercisesAndTMs;
            //for(Map.Entry<String, String> entry : exercisesAndTMs.entrySet()){
            //    preMadeInfo.put(entry.getKey(), entry.getValue());
            //}
        }else{
            preMadeInfo.put("SpecialWeek", "false");
        }

        preMadeInfo.put("type", "W531fB");

        for(int i = 0; i < map.size(); i++){
            if(i == 0){
                loadingView.setVisibility(View.GONE);
                restTimerLL.setVisibility(View.VISIBLE);
            }
            for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                if(!entry.getKey().equals("0_key")){
                    if(isOfIndex(i, entry.getKey())){
                        exNameInc++;
                        String tag = String.valueOf(exNameInc) + "ex";
                        List<String> stringList = entry.getValue();
                        if(isSpecialWeek){
                            setSpecialWeekTMs(stringList, isUserImperial, isTemplateImperial);
                        }
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        exNameFrag.infoList = stringList;
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.fragTag = tag;
                        exNameFrag.templateName = templateName;
                        if(getActivity() != null){
                            if (!getActivity().isFinishing()) {
                                fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                getChildFragmentManager().executePendingTransactions();
                                exNameFragList.add(exNameFrag);
                                fragTagList.add(tag);
                            }
                        }

                    }
                }
            }
            if(i == (map.size() - 1)){

                /**
                 * What we need to do:
                 * add the set scheme info to each exNameWAFrag
                 * Now that I think about it, it doesn't need to be "remembered" if we put it in
                 * first thing, because ofc the set schemes are stored in the db.
                 */

                for(int j = 0; j < 3; j++){
                    exNameInc++;
                    String tag = String.valueOf(exNameInc) + "ex";
                    android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager()
                            .beginTransaction();
                    ExNameWAFrag exNameFrag = new ExNameWAFrag();
                    exNameFrag.isTemplateImperial = isTemplateImperial;
                    exNameFrag.isUserImperial = isUserImperial;
                    exNameFrag.isAssistanceW531fB = true;
                    exNameFrag.templateName = templateName;
                    //exNameFrag.exerciseName = data.getStringExtra("MESSAGE");
                    ArrayList<String> info = new ArrayList<>();
                    if(j == 0){
                        String lastUsed = W531fB.getLastUsedAssistance(1);
                        if(!lastUsed.equals("null")){
                            info.add(lastUsed);
                            if(isBodyweight(lastUsed)){
                                info.add(W531fB.getPushSetScheme() + "@B.W.");
                            }else{
                                try{
                                    info.add(W531fB.getPushSetScheme() + "@" + mTemplateClass.getExtraInfo().get(lastUsed));
                                }catch (NullPointerException e){
                                    info.add(W531fB.getPushSetScheme() + "@1");
                                }
                            }
                        }else{
                            info.add("Choose PUSH assistance exercise");
                            info.add(W531fB.getPushSetScheme() + "@1");
                        }
                        //info.add(mTemplateClass.getExtraInfo().get("pushSetScheme") + "@1");
                    }else if(j == 1){
                        String lastUsed = W531fB.getLastUsedAssistance(2);
                        if(!lastUsed.equals("null")){
                            info.add(lastUsed);
                            if(isBodyweight(lastUsed)){
                                info.add(W531fB.getPullSetScheme() + "@B.W.");
                            }else{
                                try{
                                    info.add(W531fB.getPullSetScheme() + "@" + mTemplateClass.getExtraInfo().get(lastUsed));
                                }catch (NullPointerException e){
                                    info.add(W531fB.getPullSetScheme() + "@1");
                                }
                            }
                        }else{
                            info.add("Choose PULL assistance exercise");
                            info.add(W531fB.getPullSetScheme() + "@1");
                        }
                        //info.add(mTemplateClass.getExtraInfo().get("pullSetScheme") + "@1");
                    }else if(j == 2){
                        String lastUsed = W531fB.getLastUsedAssistance(3);
                        if(!lastUsed.equals("null")){
                            info.add(lastUsed);
                            if(isBodyweight(lastUsed)){
                                info.add(W531fB.getLegCoreSetScheme() + "@B.W.");
                            }else{
                                try{
                                    info.add(W531fB.getLegCoreSetScheme() + "@" + mTemplateClass.getExtraInfo().get(lastUsed));
                                }catch (NullPointerException e){
                                    info.add(W531fB.getLegCoreSetScheme() + "@1");
                                }
                            }
                        }else{
                            info.add("Choose SINGLE LEG/CORE assistance exercise");
                            info.add(W531fB.getLegCoreSetScheme() + "@1");
                        }
                        //info.add(mTemplateClass.getExtraInfo().get("legCoreSetScheme") + "@1");
                    }
                    exNameFrag.infoList = info;
                    exNameFrag.fragTag = tag;
                    if(getActivity() != null) {
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                            fragmentTransaction.commitAllowingStateLoss();
                            getChildFragmentManager().executePendingTransactions();
                            exNameFragList.add(exNameFrag);
                            fragTagList.add(tag);
                        }
                    }
                }


                updateWorkoutStateNoProgress();
                serviceCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    public void setAssistanceExercise(String exName, int type){
        preMadeInfo.put(exName, "0");
        if(type == 1){
            preMadeInfo.put("lastUsedPush", exName);
        }else if(type == 2){
            preMadeInfo.put("lastUsedPull", exName);
        }else if(type == 3){
            preMadeInfo.put("lastUsedLegCore", exName);
        }
    }

    public void setAssistanceExercise(String exName, String value){
        /**
         * You've already checked. So just check if there's an @ in it and if it's bench/ohp.
         * If tokens.length is 1, it's a main lift, if 2 it's not. then give it the funky name.
         */
        if(preMadeInfo.get("type").equals("PPLReddit")){
            String delims = "[@]";
            String[] tokens = value.split(delims);
            if(tokens.length == 1){
                preMadeInfo.put(exName, value);
            }else{
                if(exName.equals("Bench Press (Barbell - Flat)")){
                    preMadeInfo.put(exName, value);
                }else if(exName.equals("Overhead Press (Barbell)")){
                    preMadeInfo.put(exName, value);
                }else{
                    preMadeInfo.put(exName, value);
                }
            }
        }
        preMadeInfo.put(exName, value);
    }

    private void setSpecialWeekTMs(List<String> infoList, boolean isUserImperial,
                                   boolean isTemplateImperial){
        List<String> exList = new ArrayList<>();
        exList.add("Squat (Barbell - Back)");
        exList.add("Bench Press (Barbell - Flat)");
        exList.add("Overhead Press (Barbell)");
        exList.add("Deadlift (Barbell - Conventional)");

        if(exList.contains(infoList.get(0))){
            double weight;

            String delims = "[_]";
            String[] tokens = infoList.get(4).split(delims);

            String unConverted = tokens[tokens.length - 1];

            //weight = Double.parseDouble(tokens[tokens.length - 1]);
            //double roundedWeight = Double.parseDouble(roundNumberToNearest5(weight));

            /**
             * What we need to do:
             * We need to replicate the normal process to get the right value, but we also need
             * to get the opposite as well. So, one in lbs and one in kgs.
             */

            String convertedLbs;
            String convertedKgs;

            if(isUserImperial && !isTemplateImperial){
                // user is lbs, template is kgs
                convertedLbs = metricToImperial(unConverted);
                convertedLbs = roundNumberToNearest5(convertedLbs);
                convertedKgs = imperialToMetric(unConverted);
                convertedKgs = roundNumberToNearest5(convertedKgs);

            }else if(!isUserImperial && isTemplateImperial){
                // user is kgs, template is lbs
                convertedLbs = metricToImperial(unConverted);
                convertedLbs = roundNumberToNearest5(convertedLbs);
                convertedKgs = imperialToMetric(unConverted);
                convertedKgs = roundNumberToNearest5(convertedKgs);
            }else{
                convertedLbs = unConverted;
                if(isUserImperial){
                    double doubleVersion = Double.parseDouble
                            (roundNumberToNearest5(unConverted));
                    int intVersion = (int) doubleVersion;
                    convertedLbs = String.valueOf(intVersion);
                    convertedKgs = imperialToMetric(unConverted);
                    convertedKgs = roundNumberToNearest5(convertedKgs);
                }else{
                    double doubleVersion = Double.parseDouble
                            (roundNumberToNearest5(unConverted));
                    int intVersion = (int) doubleVersion;
                    convertedKgs = String.valueOf(intVersion);
                    convertedLbs = metricToImperial(unConverted);
                    convertedLbs = roundNumberToNearest5(convertedLbs);
                }
            }

            String first = infoList.get(0) + "Lbs";
            String second = infoList.get(0) + "Kgs";

            setAssistanceExercise(first, convertedLbs);
            setAssistanceExercise(second, convertedKgs);

        }
    }

    public boolean isPercentageShort(String string){
        boolean isPercentage;

        char c = string.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            isPercentage = true;
        }else{
            isPercentage = false;
        }

        return isPercentage;
    }

    private String metricToImperial(String input){

        String weight;
        String newString;

        char c = input.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            String delims = "[_]";
            String[] tokens = input.split(delims);
            weight = tokens[tokens.length - 1];
            double lbsDouble = Double.parseDouble(weight) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            newString =
                    tokens[0] + "_" + tokens[1] + "_" + tokens[2] + "_" + String.valueOf(lbsInt);
        }else{
            weight = input;
            double lbsDouble = Double.parseDouble(weight) * 2.2046;
            int lbsInt = (int) Math.round(lbsDouble);
            newString = String.valueOf(lbsInt);
        }


        return newString;
    }

    private String imperialToMetric(String input){

        String weight;
        String newString;

        char c = input.charAt(0);
        String cString = String.valueOf(c);
        if(cString.equals("p")){
            String delims = "[_]";
            String[] tokens = input.split(delims);
            weight = tokens[tokens.length - 1];
            double kgDouble = Double.parseDouble(weight) / 2.2046;
            int kgInt = (int) Math.round(kgDouble);
            newString =
                    tokens[0] + "_" + tokens[1] + "_" + tokens[2] + "_" + String.valueOf(kgInt);
        }else{
            weight = input;
            double kgDouble = Double.parseDouble(weight) / 2.2046;
            int kgInt = (int) Math.round(kgDouble);
            newString = String.valueOf(kgInt);
        }

        return newString;
    }

    private String roundNumberToNearest5(String weightString){
        String rounded;

        double weight;
        int weight2;

        weight = Double.parseDouble(weightString);

        weight2 = (int) (5 * (Math.round(weight / 5)));

        rounded = String.valueOf(weight2);

        return rounded;
    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }

    private void inflateSmolov(){
        Smolov smolov = new Smolov(mTemplateClass.getExtraInfo().get("exName"),
                mTemplateClass.getExtraInfo().get("maxWeight"));
        if(mTemplateClass.getExtraInfo().get("isTakeOff10") != null){
            smolov.setTakeOff10(Boolean.parseBoolean(mTemplateClass.getExtraInfo().get("isTakeOff10")));
        }

        boolean round;

        if(mTemplateClass.getExtraInfo().get("roundToNearest5") != null){
            round = Boolean.parseBoolean(mTemplateClass.getExtraInfo().get("roundToNearest5"));
        }else{
            round = false;
        }

        HashMap<String, List<String>> smolovMap = smolov.generateSmolovWorkoutMap
                (mTemplateClass.getExtraInfo().get("beginDate"), round);

        //HashMap<String, List<String>> smolovMap = smolov.generateSpecific(6, 4);

        if(smolov.getIsOneRepMaxDay()){
            extraInfoTextView.setText(R.string.oneRepMaxDay);
            extraInfoTextView.setVisibility(View.VISIBLE);
            preMadeInfo.clear();
            preMadeInfo.put("type", "Smolov");
            preMadeInfo.put("oneRepMaxDay", "true");
            preMadeInfo.put("isTakeOff10", String.valueOf(smolov.isTakeOff10()));
            if(smolov.isLastDay()){
                preMadeInfo.put("isLastDay", "true");
                endView.setVisibility(View.VISIBLE);
            }else{
                preMadeInfo.put("isLastDay", "false");
            }
        }

        premadeWeekDayString = smolov.getWeekDayString();

        for(int i = 0; i < smolovMap.size(); i++){
            if(i == 0){
                loadingView.setVisibility(View.GONE);
                restTimerLL.setVisibility(View.VISIBLE);
            }
            for(Map.Entry<String, List<String>> entry : smolovMap.entrySet()) {
                if(!entry.getKey().equals("0_key")){
                    if(isOfIndex(i, entry.getKey())){
                        exNameInc++;
                        String tag = String.valueOf(exNameInc) + "ex";
                        List<String> stringList = entry.getValue();
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        exNameFrag.infoList = stringList;
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.fragTag = tag;
                        exNameFrag.templateName = templateName;
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                            fragmentTransaction.commitAllowingStateLoss();
                            getChildFragmentManager().executePendingTransactions();
                            exNameFragList.add(exNameFrag);
                            fragTagList.add(tag);
                        }
                    }
                }
            }
            if(i == (smolovMap.size() - 1)){
                updateWorkoutStateNoProgress();
                serviceCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void noProgressInflateViews(){

        /*
         * Current problem: after the listener has been set, saving a template causes the updated info to be added
         * onto the existing running assistor node. It should either totally delete it or delete the current stuff and
         * add the new info in place of the old info.
         */



        DatabaseReference activeTemplateRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                .child("activeTemplate");
        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    String templateNameString = dataSnapshot.getValue(String.class);

                    DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child("templates").child(uid)
                            .child(templateNameString);

                    templateName = templateNameString;

                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                mTemplateClass = dataSnapshot.getValue(TemplateModelClass.class);
                                isTemplateImperial = mTemplateClass.isIsImperial();

                                if(mTemplateClass.getRestTime() != null){
                                    String delims = "[:]";
                                    String[] tokens = mTemplateClass.getRestTime().split(delims);
                                    minutesEditText.setText(tokens[0]);
                                    secondsEditText.setText(tokens[1]);
                                }

                                justVibrateNoUpdate = true;
                                if(mTemplateClass.getVibrationTime() != null){
                                    secondsVibrateEditText.setText(mTemplateClass.getVibrationTime());
                                    if(mTemplateClass.isIsRestTimerAlert()){
                                        //justVibrateRB.setChecked(false);
                                        showRestTimerAlertRB.setChecked(true);
                                    }else{
                                        justVibrateRB.setChecked(true);
                                        //showRestTimerAlertRB.setChecked(false);
                                    }
                                    justVibrateNoUpdate = false;
                                }

                                restTimerNoUpdate = true;
                                restTimerSwitch.setChecked(mTemplateClass.isIsActiveRestTimer());

                                // without having saved any progress
                                DateTime dateTime = new DateTime();
                                int currentWeekday = dateTime.getDayOfWeek();
                                if(mTemplateClass.getWorkoutType().equals("placeholder")){
                                    if(mTemplateClass.getMapForDay(intToWeekday(currentWeekday)) != null){
                                        if(!mTemplateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()){
                                            HashMap<String, List<String>> map = mTemplateClass.getMapForDay(intToWeekday(currentWeekday));
                                            for(int i = 0; i < map.size(); i++){
                                                if(i == 0){
                                                    loadingView.setVisibility(View.GONE);
                                                    restTimerLL.setVisibility(View.VISIBLE);
                                                    serviceCardView.setVisibility(View.VISIBLE);
                                                }
                                                for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                                                    if(!entry.getKey().equals("0_key")){
                                                        if(isOfIndex(i, entry.getKey())){
                                                            exNameInc++;
                                                            String tag = String.valueOf(exNameInc) + "ex";
                                                            List<String> stringList = entry.getValue();
                                                            android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                                                            ExNameWAFrag exNameFrag = new ExNameWAFrag();
                                                            exNameFrag.isTemplateImperial = isTemplateImperial;
                                                            exNameFrag.infoList = stringList;
                                                            exNameFrag.isUserImperial = isUserImperial;
                                                            exNameFrag.fragTag = tag;
                                                            exNameFrag.templateName = templateName;
                                                            if(getActivity() != null){
                                                                if(!getActivity().isFinishing()) {
                                                                    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                                                                    fragmentTransaction.commitAllowingStateLoss();
                                                                    getChildFragmentManager().executePendingTransactions();
                                                                    exNameFragList.add(exNameFrag);
                                                                    fragTagList.add(tag);
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                                if(i == (map.size() - 1)){
                                                    updateWorkoutStateNoProgress();
                                                    serviceCardView.setVisibility(View.VISIBLE);
                                                }
                                            }
                                        }
                                    }
                                }else{
                                    processPreMadeProgram();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private boolean hasOnlyExNames(HashMap<String, List<String>> map){
        boolean onlyExNames = true;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            List<String> list = entry.getValue();
            for(String string : list){
                //if(string.equals("Overhead Press (Dumbbell)")){
                //    Log.i("i", "i");
                //}
                if(!isExerciseName(string)){
                    onlyExNames = false;
                    //String delims = "[_]";
                    //String[] tokens = string.split(delims);
                    //if(tokens[1].equals("checked")){
                    //    onlyExNames = false;
                    //}
                }
            }
        }
        return onlyExNames;
    }

    private boolean isOfIndex(int index, String key){
        String delims = "[_]";
        String tokens[] = key.split(delims);
        int index2 = Integer.valueOf(tokens[0]);
        if(index + 1 == index2){
            return true;
        }else{
            return false;
        }
    }

    private HashMap<String, List<String>> reorderMap(HashMap<String, List<String>> map){
        HashMap<String, List<String>> orderedMap = new HashMap<>();

        String dummyString = "dummy";
        List<String> dummyList = new ArrayList<>();
        dummyList.add(dummyString);

        int mapSize = map.size();
        for(int i = 0; i < mapSize; i++){
            orderedMap.put(dummyString, dummyList);
        }

        for(Map.Entry<String, List<String>> subMap : map.entrySet()){
            String delims = "[_]";
            //int index = Integer.valueOf(subMap.getValue().get(0));
            String tokens[] = subMap.getValue().get(0).split(delims);
            int index = Integer.valueOf(tokens[0]);

        }

        return orderedMap;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            // workout done
            HashMap<String, HashMap<String, List<String>>> runningMap = new HashMap<>();
            int inc = 0;
            for(ExNameWAFrag exNameFrag : exNameFragList){
                if(!hasOnlyExNames(exNameFrag.getInfoForMap())){
                    inc++;
                    runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
                }
            }

            String privateJournal = privateJournalView.getText().toString();
            String publicComment = publicCommentView.getText().toString();

            android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            AssistorSavedFrag assistorSavedFrag = new AssistorSavedFrag();
            if(isTutorialFirstTime){
                assistorSavedFrag.isFirstTimeFirstTime = true;
            }
            assistorSavedFrag.templateClass = mTemplateClass;
            assistorSavedFrag.completedMap = runningMap;
            assistorSavedFrag.privateJournal = privateJournal;
            assistorSavedFrag.publicDescription = publicComment;
            assistorSavedFrag.isFromRestDay = isFromRestDay;
            fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
            fragmentTransaction.commit();
        }else if(requestCode == 2){
            // new exercise
            if(data != null){
                if(data.getStringExtra("MESSAGE") != null){
                    //exerciseNameView.setText(data.getStringExtra("MESSAGE"));

                    Log.i("addExInfo", "onActivityResult called");



                    /*
                     * So we're going to take the ex name from this intent
                     * and then start a new activity for result (requestCode == 3).
                     * The new activity would have template editor style functions for adding set schemes
                     * and supersets. We'll have to do all that later though, it's a large undertaking.
                     *
                     * What if we write directly to fb?
                     */

                    updateWorkoutStateSnackbarAndInitialize(true);

                    //workoutProgressModelClass.addExercise(data.getStringExtra("MESSAGE"));
                    //DatabaseReference runningAssistorRef =
                    //        mRootRef.child("runningAssistor").child(uid).child
                    //        ("assistorModel");
                    //runningAssistorRef.setValue(workoutProgressModelClass)
                                    // .addOnCompleteListener(new OnCompleteListener<Void>() {
                    //    @Override
                    //    public void onComplete(@NonNull Task<Void> task) {
                    //        initializeViews();
//
                    //        //if(isServiceUp()){
                    //        //    setButtonsForService();
                    //        //}else{
                    //        //    Intent startIntent = new Intent(getActivity(),
                    //        //            AssistorServiceClass.class);
                    //        //    startIntent.putExtra("uid", uid);
                    //        //    startIntent.putExtra("userImperial",
                    //        //            String.valueOf(isUserImperial));
                    //        //    getActivity().startService(startIntent);
                    //        //    setButtonsForService();
                    //        //}
                    //    }
                    //});

                    //exNameInc++;
                    //String tag = String.valueOf(exNameInc) + "ex";
                    //android.app.FragmentTransaction fragmentTransaction =
                    //        getChildFragmentManager().beginTransaction();
                    //ExNameWAFrag exNameFrag = new ExNameWAFrag();
                    //exNameFrag.isTemplateImperial = isTemplateImperial;
                    ////exNameFrag.infoList = stringList;
                    //exNameFrag.isUserImperial = isUserImperial;
                    //exNameFrag.fragTag = tag;
                    //if(getActivity() != null){
                    //    if(!getActivity().isFinishing()) {
                    //        fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                    //        fragmentTransaction.commitAllowingStateLoss();
                    //        getChildFragmentManager().executePendingTransactions();
                    //        exNameFragList.add(exNameFrag);
                    //        fragTagList.add(tag);
                    //    }
                    //}

                    exNameInc++;
                    String tag = String.valueOf(exNameInc) + "ex";
                    android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager()
                     .beginTransaction();
                    ExNameWAFrag exNameFrag = new ExNameWAFrag();
                    exNameFrag.isTemplateImperial = isTemplateImperial;
                    exNameFrag.isUserImperial = isUserImperial;
                    exNameFrag.templateName = templateName;
                    //exNameFrag.exerciseName = data.getStringExtra("MESSAGE");
                    ArrayList<String> info = new ArrayList<>();
                    info.add(data.getStringExtra("MESSAGE"));
                    info.add("0x0@0");
                    exNameFrag.infoList = info;
                    exNameFrag.fragTag = tag;
                    if(getActivity() != null) {
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                            fragmentTransaction.commitAllowingStateLoss();
                            getChildFragmentManager().executePendingTransactions();
                            exNameFragList.add(exNameFrag);
                            fragTagList.add(tag);
                        }
                    }

                    updateWorkoutStateSnackbarAndInitialize(true);
                }
            }
        }
    }

    public void removeFrag(String tag){
        getChildFragmentManager().executePendingTransactions();
        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(exNameInc != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                int inc = 0;
                int index = 0;
                boolean hasIndex = false;
                for(ExNameWAFrag exNameWAFrag : exNameFragList){
                    if(exNameWAFrag.fragTag.equals(tag)){
                        index = inc;
                        hasIndex = true;
                    }
                    inc++;
                }
                if(hasIndex){
                    exNameFragList.remove(index);
                    int tagListIndex = fragTagList.indexOf(tag);
                    fragTagList.remove(tagListIndex);
                }
                --exNameInc;
                if(exNameInc == 0){
                    activateStatusBarService.setVisibility(View.GONE);
                    deactivateLL.setVisibility(View.GONE);

                    Intent stopIntent = new Intent(getActivity(), AssistorServiceClass.class);
                    getActivity().stopService(stopIntent);

                }
                updateWorkoutState();
            }
        }
    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
            if(input.length() > 5){
                String string = input.substring(0, 4);
                //String string2 = input.substring(0, 2);
                if(string.equals("T.F.")){
                    isExercise = false;
                }
            }
        }

        return isExercise;
    }

    String intToWeekday(int inc){
        String weekday = "";

        if(inc == 1){
            weekday = "Monday";
        }else if(inc == 2){
            weekday = "Tuesday";
        }else if(inc == 3){
            weekday = "Wednesday";
        }else if(inc == 4){
            weekday = "Thursday";
        }else if(inc == 5){
            weekday = "Friday";
        }else if(inc == 6){
            weekday = "Saturday";
        }else if(inc == 7){
            weekday = "Sunday";
        }

        return weekday;
    }

    boolean containsToday(String dayUnformatted, int inc){
        boolean contains = false;
        String[] tokens = dayUnformatted.split("_");

        try{
            for(String string : tokens){
                if(string.equals(intToWeekday(inc))){
                    contains = true;
                }
            }
        } catch (IndexOutOfBoundsException e){

        }

        return contains;
    }



}
