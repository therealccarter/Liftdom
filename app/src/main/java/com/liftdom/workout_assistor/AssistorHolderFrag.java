package com.liftdom.workout_assistor;


import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityManagerCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.appodeal.ads.InterstitialCallbacks;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
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
                ExNameWAFrag.updateWorkoutStateCallback{


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    TemplateModelClass mTemplateClass;
    ArrayList<ExNameWAFrag> exNameFragList = new ArrayList<>();
    int exNameInc = 0;
    boolean savedState = false;
    WorkoutProgressModelClass modelClass;
    String smolovWeekDayString;
    boolean isTemplateImperial;
    ArrayList<String> fragTagList = new ArrayList<>();

    DatabaseReference mRunningAssistorRef = mRootRef.child("runningAssistor").child(uid);
    //.child("assistorModel");
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    public interface scrollToBottomInterface{
        void scrollToBottom();
    }

    private scrollToBottomInterface scrollToBottomCallback;

    @BindView(R.id.addExerciseButton) Button addExButton;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.saveHolder) CardView saveHolder;
    @BindView(R.id.saveProgressButton) Button saveProgressButton;
    @BindView(R.id.privateJournal) EditText privateJournalView;
    @BindView(R.id.publicComment) EditText publicCommentView;
    @BindView(R.id.saveImage) ImageButton saveImage;
    @BindView(R.id.oneRepMaxDayView) TextView maxDayView;
    @BindView(R.id.activateStatusBarWA) Button activateStatusBarService;
    @BindView(R.id.deactivateStatusBarWA) Button deactivateStatusBarService;
    @BindView(R.id.deactivateStatusBarImageView) ImageView deactiveStatusBarImage;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.exInfoHolder2) LinearLayout exInfoHolder2;
    @BindView(R.id.deactivateLL) LinearLayout deactivateLL;
    @BindView(R.id.serviceCardView) CardView serviceCardView;
    @BindView(R.id.resetWorkoutButton) Button resetWorkoutProgressButton;

    boolean isFirstTimeFirstTime = true;
    boolean isTutorialFirstTime = false;

    private ValueEventListener runningAssistorListener;

    DatabaseReference runningAssistorRef;

    public void firstTimeShowcase(CheckBox checkBox){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            }
        });
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity());

        Log.i("assistorInfo", "onCreateView called (assistor holder)");

        Log.i("assistorInfo", "onCreateView");

        checkForOldData();

        // ========================= ONLY LISTENERS BEYOND THIS POINT ===============================

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
                                runningAssistorRef.setValue(null);
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
                exNameInc++;
                String tag = String.valueOf(exNameInc) + "ex";
                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                ExNameWAFrag exNameFrag = new ExNameWAFrag();
                exNameFrag.isTemplateImperial = isTemplateImperial;
                exNameFrag.fragTag = tag;
                if (!getActivity().isFinishing()) {
                    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    getChildFragmentManager().executePendingTransactions();
                    exNameFragList.add(exNameFrag);
                    fragTagList.add(tag);
                }
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

                            if(dayDouble % (double) 2 == 0.0 || isTutorialFirstTime){
                                alertDialog.dismiss();
                                finishWorkout();
                            }else{
                                alertDialog.dismiss();
                                finishWorkoutFromAd();

                                Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                                Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                                    @Override
                                    public void onInterstitialLoaded(boolean b) {
                                        Log.i("appodeal", "loaded");
                                    }

                                    @Override
                                    public void onInterstitialFailedToLoad() {
                                        Log.i("appodeal", "failed");
                                    }

                                    @Override
                                    public void onInterstitialShown() {
                                        Log.i("appodeal", "shown");
                                    }

                                    @Override
                                    public void onInterstitialClicked() {
                                        Log.i("appodeal", "clicked");
                                    }

                                    @Override
                                    public void onInterstitialClosed() {
                                        Log.i("appodeal", "closed");
                                    }
                                });
                            }



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

                    positiveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            String day = LocalDate.now().toString("dd");

                            double dayDouble = Double.parseDouble(day);

                            // possibly have rando number generate

                            if(dayDouble % (double) 2 == 0.0 || isTutorialFirstTime){
                                alertDialog.dismiss();
                                finishWorkout();
                            }else{

                                alertDialog.dismiss();
                                finishWorkoutFromAd();

                                Appodeal.show(getActivity(), Appodeal.INTERSTITIAL);
                                Appodeal.setInterstitialCallbacks(new InterstitialCallbacks() {
                                    @Override
                                    public void onInterstitialLoaded(boolean b) {
                                        Log.i("appodeal", "loaded");
                                    }

                                    @Override
                                    public void onInterstitialFailedToLoad() {
                                        Log.i("appodeal", "failed");
                                    }

                                    @Override
                                    public void onInterstitialShown() {
                                        Log.i("appodeal", "shown");
                                    }

                                    @Override
                                    public void onInterstitialClicked() {
                                        Log.i("appodeal", "clicked");
                                    }

                                    @Override
                                    public void onInterstitialClosed() {
                                        Log.i("appodeal", "closed");
                                    }
                                });
                            }

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

        activateStatusBarService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activateStatusBarService.setVisibility(View.GONE);
                deactivateLL.setVisibility(View.VISIBLE);

                Intent startIntent = new Intent(getActivity(), AssistorServiceClass.class);
                startIntent.putExtra("uid", uid);
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

    // index will always be the last item checked, or the first item.

    public void updateWorkoutStateNoProgress(){
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

        WorkoutProgressModelClass progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial);

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        cleanUpState();
        runningAssistorRef.setValue(progressModelClass);
    }

    public void updateWorkoutState(){
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

        WorkoutProgressModelClass progressModelClass = new WorkoutProgressModelClass(dateTimeString,
                completedBool, runningMap, privateJournal, publicComment, mediaResource, isTemplateImperial);

        //progressModelClass.setIsTemplateImperial(isTemplateImperial);

        runningAssistorRef.setValue(progressModelClass);

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
        if(mTemplateClass == null){
            Log.i("assistorInfo", "templateClass is null (onResume)");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("fragID",  0);
            startActivity(intent);
            super.onResume();
        }else{
            Log.i("assistorInfo", "AssistorHolderFrag (onResume)");
            super.onResume();
        }
        if(runningAssistorListener == null){
            checkForOldData();
        }else{
            runningAssistorRef.addValueEventListener(runningAssistorListener);
        }
    }

    /**
     * Edge case and set/ex deletion time.
     */

    @Override
    public void onStart(){
        super.onStart();

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

    private void checkForOldData(){
        if(mTemplateClass != null){
            Log.i("assistorInfo", "templateClass is not null");
            //cleanUpState();
            isTemplateImperial = mTemplateClass.isIsImperial();
            initializeViews();
        }else{
            Log.i("assistorInfo", "templateClass is null");
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.putExtra("fragID",  0);
            startActivity(intent);
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

    private void initializeViews(){
        /**
         * So what I'm thinking right now is to automatically update to the running assistor, and have both the WA
         * and the Service setting/getting continuously from that node. We shall see.
         * just trying some things out. thinking about ways of implementing this WA/Service symbiosis.
         * I'm excited though! Could make it so we don't have to deal with the save button AND get a notification bar working.
         */

        runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child
                ("assistorModel");
        runningAssistorListener = runningAssistorRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Log.i("assistorInfo", "runningAssistor triggered/exists");
                    DateTimeFormatter fmt = DateTimeFormat.forPattern("yyyy-MM-dd");
                    LocalDate localDate = LocalDate.now();
                    String dateTimeString = fmt.print(localDate);
                    modelClass = dataSnapshot.getValue(WorkoutProgressModelClass.class);
                    if(dateTimeString.equals(modelClass.getDate())){
                        if(!modelClass.isCompletedBool()){
                            Log.i("assistorInfo", "runningAssistor confirmed");
                            cleanUpState();
                            //Toast.makeText(getActivity(), "running assistor set", Toast.LENGTH_SHORT);
                            savedProgressInflateViews(modelClass.getExInfoHashMap(), modelClass.getPrivateJournal(),
                                    modelClass.getPublicComment(), modelClass.isIsTemplateImperial());
                            //noProgressInflateViews();
                            //setUpFirebaseAdapter();
                        }else{
                            noProgressInflateViews();
                        }
                    }else{
                        noProgressInflateViews();
                    }
                }else{
                    noProgressInflateViews();
                }
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

    @Override
    public void onStop(){
        Log.i("onStop", "AssistorHolderFrag onStop called");
        runningAssistorRef.removeEventListener(runningAssistorListener);
        super.onStop();
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
        fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
        fragmentTransaction.commit();
    }

    private void finishWorkoutFromAd(){
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
        assistorSavedFrag.isFromAd = true;
        fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
        fragmentTransaction.commit();
    }

    private void savedProgressInflateViews(HashMap<String, HashMap<String, List<String>>> runningMap, String
            privateJournal, String publicComment, boolean isTemplateImperial1){

        Log.i("assistorInfo", "savedProgressInflateViews");

        for(int i = 0; i < runningMap.size(); i++){
            if(i == 0){
                loadingView.setVisibility(View.GONE);
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

    }

    private void noProgressInflateViews(){

        /**
         * So what we're currently at: some of this stuff isn't being retained on GC so we need to do a lot of fun
         * testing!
         */

        // without having saved any progress
        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();
        if(mTemplateClass.getWorkoutType().equals("Smolov")){
            Smolov smolov = new Smolov(mTemplateClass.getExtraInfo().get("exName"),
                    mTemplateClass.getExtraInfo().get("maxWeight"));
            HashMap<String, List<String>> smolovMap = smolov.generateSmolovWorkoutMap
                    (mTemplateClass.getExtraInfo().get("beginDate"));
            if(smolov.getIsOneRepMaxDay()){
                maxDayView.setVisibility(View.VISIBLE);
            }

            smolovWeekDayString = smolov.getWeekDayString();

            for(int i = 0; i < smolovMap.size(); i++){
                if(i == 0){
                    loadingView.setVisibility(View.GONE);
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


        }else{
            if(mTemplateClass.getMapForDay(intToWeekday(currentWeekday)) != null){
                if(!mTemplateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()){
                    HashMap<String, List<String>> map = mTemplateClass.getMapForDay(intToWeekday(currentWeekday));
                    for(int i = 0; i < map.size(); i++){
                        if(i == 0){
                            loadingView.setVisibility(View.GONE);
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
                        if(i == (map.size() - 1)){
                            updateWorkoutStateNoProgress();
                            serviceCardView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }
        }
    }

    private boolean hasOnlyExNames(HashMap<String, List<String>> map){
        boolean onlyExNames = true;

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            List<String> list = entry.getValue();
            for(String string : list){
                if(!isExerciseName(string)){
                    String delims = "[_]";
                    String[] tokens = string.split(delims);
                    if(tokens[1].equals("checked")){
                        onlyExNames = false;
                    }
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
            fragmentTransaction.replace(R.id.exInfoHolder, assistorSavedFrag);
            fragmentTransaction.commit();
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
