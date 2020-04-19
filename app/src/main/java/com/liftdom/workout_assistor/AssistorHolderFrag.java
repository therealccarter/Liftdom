package com.liftdom.workout_assistor;


import android.app.ActivityManager;
import android.content.*;
import android.graphics.Color;
import android.graphics.Rect;
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
import com.liftdom.workout_programs.FiveThreeOne.Wendler_531_For_Beginners;
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
                ExNameWAFrag.updateExNameCallback{


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
    String smolovWeekDayString;
    boolean isTemplateImperial;
    ArrayList<String> fragTagList = new ArrayList<>();
    boolean isRevisedWorkout;
    boolean isFreestyleWorkout;
    String refKey;
    boolean isFromRestDay;
    boolean isInForeground;
    boolean isLastDay;
    boolean isListening;

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
    @BindView(R.id.oneRepMaxDayView) TextView maxDayView;
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

        /**
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
                /**
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

                    /**
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

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
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

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
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

        /**
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

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
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

        progressModelClass.setIsRestTimerAlert(showRestTimerAlertRB.isChecked());

        workoutProgressModelClass = progressModelClass;

        runningAssistorRef.setValue(progressModelClass);

        //for(ExNameWAFrag exNameWAFrag : exNameFragList){
        //    removeFrag(exNameWAFrag.fragTag);
        //}

        /**
         * OK, so what we need to do is just convert the original template class to a running model, then inflate that.
         * Inflating the original, then deleting it, then inflating the running model is just too much overhead.
         */
    }

    final Handler handler = new Handler();

    public void updateWorkoutStateWithDelay(){

        /**
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

                    // might need to make this not clickable without inflated views so it isn't set to null
                    for(ExNameWAFrag exNameFrag : exNameFragList){
                        inc++;
                        //for(int i = 1; i <= exNameFragList.size(); i++){}
                        runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
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

        // might need to make this not clickable without inflated views so it isn't set to null
        for(ExNameWAFrag exNameFrag : exNameFragList){
            inc++;
            //for(int i = 1; i <= exNameFragList.size(); i++){}
            runningMap.put(String.valueOf(inc) + "_key", exNameFrag.getInfoForMap());
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

        progressModelClass.setRestTime(minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString());
        progressModelClass.setIsActiveRestTimer(restTimerBool);

        if(secondsVibrateEditText.getText().toString().isEmpty()){
            progressModelClass.setVibrationTime("0");
        }else{
            progressModelClass.setVibrationTime(secondsVibrateEditText.getText().toString());
        }

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

        /**
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
                /**
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

    /**
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



    private void initializeViews(){
        /**
         * So what I'm thinking right now is to automatically update to the running assistor, and have both the WA
         * and the Service setting/getting continuously from that node. We shall see.
         * just trying some things out. thinking about ways of implementing this WA/Service symbiosis.
         * I'm excited though! Could make it so we don't have to deal with the save button AND get a notification bar working.
         */

        /**
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
                                isFromRestDay = workoutProgressModelClass.isIsFromRestDay();
                                if(isFromRestDay){
                                    cancelRevisionHolder.setVisibility(View.VISIBLE);
                                }
                                if(workoutProgressModelClass.getExInfoHashMap() == null){
                                    cleanUpState();
                                    noProgressInflateViews();
                                }else{
                                    savedProgressInflateViews(workoutProgressModelClass.getExInfoHashMap(), workoutProgressModelClass.getPrivateJournal(),
                                            workoutProgressModelClass.getPublicComment(), workoutProgressModelClass.isIsTemplateImperial());
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
        if(smolovWeekDayString != null){
            assistorSavedFrag.smolovWeekDayString = smolovWeekDayString;
        }
        assistorSavedFrag.templateClass = mTemplateClass;
        assistorSavedFrag.completedMap = runningMap;
        assistorSavedFrag.privateJournal = privateJournal;
        assistorSavedFrag.publicDescription = publicComment;
        assistorSavedFrag.isFromRestDay = isFromRestDay;
        assistorSavedFrag.isLastDay = isLastDay;
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
        if(smolovWeekDayString != null){
            assistorSavedFrag.smolovWeekDayString = smolovWeekDayString;
        }
        assistorSavedFrag.templateClass = mTemplateClass;
        assistorSavedFrag.completedMap = runningMap;
        assistorSavedFrag.privateJournal = privateJournal;
        assistorSavedFrag.publicDescription = publicComment;
        assistorSavedFrag.isFromRestDay = isFromRestDay;
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

    private void savedProgressInflateViews(HashMap<String, HashMap<String, List<String>>> runningMap, String
            privateJournal, String publicComment, boolean isTemplateImperial1){

        Log.i("assistorInfo", "savedProgressInflateViews");

        if(workoutProgressModelClass.getRestTime() != null){
            String delims = "[:]";
            String[] tokens = workoutProgressModelClass.getRestTime().split(delims);
            minutesEditText.setText(tokens[0]);
            secondsEditText.setText(tokens[1]);
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
                        exNameFrag.isTemplateImperial = isTemplateImperial1;
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.isEditInfoList = exerciseMap;
                        exNameFrag.fragTag = tag;
                        exNameFrag.isEdit = true;
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
        }
    }

    private void inflateW531fB(){
        Wendler_531_For_Beginners W531fBClass = new Wendler_531_For_Beginners(mTemplateClass.getExtraInfo());
        HashMap<String, List<String>> map = W531fBClass.generateWorkoutMap();

        //smolovWeekDayString = smolov.getWeekDayString();

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
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        exNameFrag.infoList = stringList;
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.fragTag = tag;
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
                updateWorkoutStateNoProgress();
                serviceCardView.setVisibility(View.VISIBLE);
            }
        }
    }

    private void inflateSmolov(){
        Smolov smolov = new Smolov(mTemplateClass.getExtraInfo().get("exName"),
                mTemplateClass.getExtraInfo().get("maxWeight"));
        HashMap<String, List<String>> smolovMap = smolov.generateSmolovWorkoutMap
                (mTemplateClass.getExtraInfo().get("beginDate"));
        if(smolov.getIsOneRepMaxDay()){
            maxDayView.setVisibility(View.VISIBLE);
            endView.setVisibility(View.VISIBLE);
            isLastDay = true;
        }

        smolovWeekDayString = smolov.getWeekDayString();

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

        /**
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



                    /**
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
