package com.liftdom.workout_assistor;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.workout_programs.Smolov.Smolov;
import com.wang.avi.AVLoadingIndicatorView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutAssistorFrag extends Fragment {


    public WorkoutAssistorFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String email = "error";

    String activeTemplateName = null;
    String activeTemplateDayValue = null;
    String activeTemplateToday = null;
    String exerciseString = "fail";

    Boolean isSavedInstanceBool;
    Boolean isRunningAssistor = false;
    Boolean isRunningDate = false;

    Boolean noActiveTemplateBool = false;

    int ArrayListIterator = 0;

    //boolean firstEx = true;
    //boolean setSchemesFinished = true;

    int assistorInfoInc = 0;
    ArrayList<ArrayList<String>> assistorInfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<String> assistorInfoRunningList = new ArrayList<>();

    ArrayList<ExerciseNameFrag> exerciseNameFragList = new ArrayList<>();
    ArrayList<RepsWeightFrag> repsWeightFragList = new ArrayList<>();
    ArrayList<RunningAssistorClass> runningAssistorList = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.currentTemplateView) TextView currentTemplateView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_assistor, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Workout Assistor");

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        currentTemplateView.setTypeface(lobster);

        //if(savedInstanceState == null){
        //    isSavedInstanceBool = true;
        //}else{
        //    isSavedInstanceBool = false;
        //}

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaveAssistorDialog.class);

                startActivity(intent);
            }
        });
        
        return view;
    }


    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        if (MainActivitySingleton.getInstance().isWorkoutFinished) {

            // faux cache mechanism
            currentTemplateView.setText(MainActivitySingleton.getInstance().currentActiveTemplate);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager
                    .beginTransaction();
            WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();
            if(loadingView.getVisibility() == View.VISIBLE){
                loadingView.setVisibility(View.GONE);
            }

            if (!getActivity().isFinishing()) {
                fragmentTransaction.replace(R.id.eachExerciseFragHolder,
                        workoutFinishedFrag);
                fragmentTransaction.commitAllowingStateLoss();
            }

        } else {

            DatabaseReference activeTemplateRef = mRootRef.child("users").child("active_template");
            activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String activeTemplateString = dataSnapshot.getValue(String.class);
                        DatabaseReference templateRef = mRootRef.child("templates").child(uid).child
                                (activeTemplateString);
                        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){

                                    TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);

                                    DateTime dateTime = new DateTime();
                                    int currentWeekday = dateTime.getDayOfWeek();

                                    if(containsToday(templateModelClass.getDays(), currentWeekday)){
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        AssistorHolderFrag assistorHolderFrag = new AssistorHolderFrag();
                                        assistorHolderFrag.templateClass = templateModelClass;
                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.replace(R.id.exInfoHolder, assistorHolderFrag);
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
                                    } else{
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        RestDayFrag restDayFrag = new RestDayFrag();
                                        if (!getActivity().isFinishing()) {
                                            fragmentTransaction.replace(R.id.exInfoHolder, restDayFrag);
                                            fragmentTransaction.commitAllowingStateLoss();
                                        }
                                    }

                                } else{
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    NoActiveTemplateFrag noActiveTemplateFrag = new NoActiveTemplateFrag();
                                    if (!getActivity().isFinishing()) {
                                        fragmentTransaction.replace(R.id.exInfoHolder, noActiveTemplateFrag);
                                        fragmentTransaction.commitAllowingStateLoss();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }else{
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        NoActiveTemplateFrag noActiveTemplateFrag = new NoActiveTemplateFrag();
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.replace(R.id.exInfoHolder, noActiveTemplateFrag);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }
    // [END on_start_add_listener]

    @Override
    public void onPause() {
        super.onPause();
        //EditText privateJournalView = (EditText) getActivity().findViewById(R.id.privateJournal);
        //
        //String privateJournal = privateJournalView.getText().toString();
        //
        //WorkoutAssistorAssemblerClass.getInstance().privateJournal = privateJournal;
        //
        ////TODO: Look at ramifications of below line
        //isSavedInstanceBool = false;

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

    ArrayList<String> dayFormat(String dayString){
        ArrayList<String> daysFormmatted = new ArrayList<>();
        if(dayString != null) {
            if(!dayString.equals("algorithm") || !dayString.equals("algorithmExercises")){
                //SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
                //Date d = new Date();
                //String currentDoW = sdf.format(d);

                String delims = "[_]";
                String[] tokens = dayString.split(delims);

                for (String day : tokens) {
                    daysFormmatted.add(day);
                }
            }
        }
        return daysFormmatted;

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

    private void normalAssistorParser(DatabaseReference specificDate){

    }

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }

        //TODO: we could have loading view pop up and go away on running assistor complete upload. also should have a
        //TODO:  button to save progress

        if(!noActiveTemplateBool){

            final DatabaseReference runningBoolRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                    ("isRunningBoolDate");

            runningBoolRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);


                    if (value == null) {
                        runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                    } else {
                        String delims = "[_]";

                        String[] tokens1 = value.split(delims);

                        LocalDate localDate = LocalDate.now();
                        LocalDate convertedDate = new LocalDate(tokens1[1]);

                        // (isEditing, isDate)
                        // (false, true) = workout has been finished - do nothing
                        // (true, true) = workout is being edited - do nothing
                        // (false, false) = workout has not been finished - set to (true, true)
                        // (true, false) = yesterday's workout not finished - set to (true, true)
                        if (!Boolean.valueOf(tokens1[0]) && !localDate.equals(convertedDate)) {
                            runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                        } else if (Boolean.valueOf(tokens1[0]) && !localDate.equals(convertedDate)) {
                            runningBoolRef.setValue("true" + "_" + LocalDate.now().toString());
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid).child("isRunning").child
                    ("isRunningInfo");

            runningAssistorRef.setValue(null);

            for (ExerciseNameFrag exNameFrag : exerciseNameFragList) {
                String exName = exNameFrag.exerciseName;
                DatabaseReference exNameRef = runningAssistorRef.child(exName);
                int inc = 0;
                ArrayList<String> exerciseStringList = new ArrayList<>();

                for (RepsWeightFrag repsWeightFrag : repsWeightFragList) {
                    if (repsWeightFrag.getParentExercise().equals(exName)) {
                        String infoString = repsWeightFrag.fullString + "_" + repsWeightFrag.getCheckedStatus();
                        exerciseStringList.add(infoString);
                    }

                    inc++;

                    if (inc == repsWeightFragList.size()) {
                        List<String> list = exerciseStringList;
                        exNameRef.setValue(list);
                    }


                }
            }
        }
    }
    // [END on_stop_remove_listener]

    //TODO: Will have to decide whether to keep this method or do it via the type tag...
    boolean smolovChecker(String templateName){
        boolean isSmolov = false;

        try{
            if(templateName.substring(0,6).equals("Smolov")){
                isSmolov = true;
            }
        } catch (StringIndexOutOfBoundsException e){
            isSmolov = false;
        }


        return isSmolov;
    }

    ArrayList<Integer> getWeekAndDaySmolov(LocalDate oldDate){
        ArrayList<Integer> arrayList = new ArrayList<>();

        LocalDate newDate = LocalDate.now();

        int daysBetween = Days.daysBetween(oldDate, newDate).getDays();

        int weeks = (int) Math.round(daysBetween / 7);
        arrayList.add(weeks);
        int days = daysBetween % 7;
        arrayList.add(days);


        return arrayList;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    

}
