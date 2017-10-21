package com.liftdom.workout_assistor;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.workout_programs.Smolov.Smolov;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutAssistorFrag extends Fragment{


    public WorkoutAssistorFrag() {
        // Required empty public constructor
    }

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    Boolean noActiveTemplateBool = false;

    int assistorInfoInc = 0;
    ArrayList<ArrayList<String>> assistorInfoLists = new ArrayList<ArrayList<String>>();
    ArrayList<String> assistorInfoRunningList = new ArrayList<>();

    ArrayList<ExerciseNameFrag> exerciseNameFragList = new ArrayList<>();
    ArrayList<RepsWeightFrag> repsWeightFragList = new ArrayList<>();
    ArrayList<RunningAssistorClass> runningAssistorList = new ArrayList<>();

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();



    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    //@BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.currentTemplateView) TextView currentTemplateView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.exInfoHolder) LinearLayout exInfoHolderLL;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_assistor, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Today's Workout");

        if(!MainActivitySingleton.getInstance().isBannerViewInitialized){
            String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
            Appodeal.initialize(getActivity(), appKey, Appodeal.BANNER);
            Appodeal.show(getActivity(), Appodeal.BANNER_VIEW);
            MainActivitySingleton.getInstance().isBannerViewInitialized = true;
        }

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");
        currentTemplateView.setTypeface(lobster);

        if(savedInstanceState == null){

            String localDateNow = new LocalDate(LocalDate.now()).toString();
            DatabaseReference workoutHistoryRef = mRootRef.child("workoutHistory").child(uid).child(localDateNow);
            workoutHistoryRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){

                        setTemplateName();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        WorkoutFinishedFrag workoutFinishedFrag = new WorkoutFinishedFrag();
                        if(loadingView.getVisibility() == View.VISIBLE){
                            loadingView.setVisibility(View.GONE);
                        }

                        if (!getActivity().isFinishing()) {
                            if(exInfoHolderLL != null){
                                fragmentTransaction.replace(R.id.exInfoHolder,
                                        workoutFinishedFrag);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                        }
                    }else{
                        initiliazeFrags();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }


        return view;
    }

    private void setTemplateName(){
        DatabaseReference userRef = mRootRef.child("user").child(uid).child("activeTemplate");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String activeTemplateString = dataSnapshot.getValue(String.class);

                if(activeTemplateString != null) {
                    currentTemplateView.setText(activeTemplateString);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initiliazeFrags(){
        DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);
        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                loadingView.setVisibility(View.GONE);

                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                String activeTemplateString = userModelClass.getActiveTemplate();

                if(activeTemplateString != null){

                    currentTemplateView.setText(activeTemplateString);

                    DatabaseReference templateRef = mRootRef.child("templates").child(uid).child
                            (activeTemplateString);
                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){

                                TemplateModelClass templateModelClass = dataSnapshot.getValue(TemplateModelClass.class);

                                if(templateModelClass.getWorkoutType().equals("Smolov")){

                                    Smolov smolov = new Smolov(templateModelClass.getExtraInfo().get("exName"),
                                            templateModelClass.getExtraInfo().get("maxWeight"));
                                    HashMap<String, List<String>> smolovMap = smolov.generateSmolovWorkoutMap
                                            (templateModelClass.getExtraInfo().get("beginDate"));

                                    if(smolovMap.get("1_key").get(1).equals("rest")){
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        RestDayFrag restDayFrag = new RestDayFrag();
                                        if (!getActivity().isFinishing()) {
                                            try {
                                                LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                                        .exInfoHolder);
                                                fragmentTransaction.replace(exInfoHolder.getId(), restDayFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                            }catch (NullPointerException e){

                                            }
                                        }
                                    }else{
                                        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                                        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        AssistorHolderFrag assistorHolderFrag = new AssistorHolderFrag();
                                        assistorHolderFrag.mTemplateClass = templateModelClass;
                                        if (!getActivity().isFinishing()) {
                                            try {
                                                LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                                        .exInfoHolder);
                                                fragmentTransaction.replace(exInfoHolder.getId(), assistorHolderFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                            }catch (NullPointerException e){

                                            }
                                        }
                                    }

                                }else{
                                    DateTime dateTime = new DateTime();
                                    int currentWeekday = dateTime.getDayOfWeek();

                                    if(containsToday(templateModelClass.getDays(), currentWeekday)){
                                        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                                        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        AssistorHolderFrag assistorHolderFrag = new AssistorHolderFrag();
                                        assistorHolderFrag.mTemplateClass = templateModelClass;
                                        if (!getActivity().isFinishing()) {
                                            try {
                                                LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                                        .exInfoHolder);
                                                fragmentTransaction.replace(exInfoHolder.getId(), assistorHolderFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                            }catch (NullPointerException e){

                                            }
                                        }
                                    }else{
                                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                        RestDayFrag restDayFrag = new RestDayFrag();
                                        if (!getActivity().isFinishing()) {
                                            try {
                                                LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                                        .exInfoHolder);
                                                fragmentTransaction.replace(exInfoHolder.getId(), restDayFrag);
                                                fragmentTransaction.commitAllowingStateLoss();
                                            }catch (NullPointerException e){

                                            }
                                        }
                                    }
                                }

                            }else{
                                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                NoActiveTemplateFrag noActiveTemplateFrag = new NoActiveTemplateFrag();
                                if (!getActivity().isFinishing()) {
                                    try {
                                        LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                                .exInfoHolder);
                                        fragmentTransaction.replace(exInfoHolder.getId(), noActiveTemplateFrag);
                                        fragmentTransaction.commitAllowingStateLoss();
                                    }catch (NullPointerException e){

                                    }
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
                        try {
                            LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                    .exInfoHolder);
                            fragmentTransaction.replace(exInfoHolder.getId(), noActiveTemplateFrag);
                            fragmentTransaction.commitAllowingStateLoss();
                        }catch (NullPointerException e){

                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();


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
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    

}
