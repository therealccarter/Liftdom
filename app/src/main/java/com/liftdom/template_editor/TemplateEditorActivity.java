package com.liftdom.template_editor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.user_profile.UserModelClass;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TemplateEditorActivity extends BaseActivity
        implements DayOfWeekChildFrag.onDaySelectedListener,
        ExerciseLevelChildFrag.setToGoldCallback,
        ExerciseLevelChildFrag.removeGoldCallback{

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private FirebaseAuth.AuthStateListener mAuthListener;

    int fragIdCount = 0;

    boolean isFirstTimeTut = false;

    boolean isTemplateEdit;
    boolean isFromPublic;

    String templateNameEdit;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // butterknife
    @BindView(R.id.addDay) Button addDay;
    @BindView(R.id.removeDay) Button removeDay;
    //@BindView(R.id.saveButton) Button onSave;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.makePublicCheckbox) CheckBox makePublicCheckbox;
    @BindView(R.id.descriptionEditText) EditText templateDescriptionEdit;
    @BindView(R.id.title) TextView title;

    ArrayList<DayOfWeekChildFrag> dayOfWeekChildFragArrayList = new ArrayList<>();

    private String handleUnitConversion(String oldValue){
        String newValue;
        if(TemplateEditorSingleton.getInstance().isTemplateImperial
                && !TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is imperial, but the user is metric
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble * 0.45359237);
            newValue = String.valueOf(valueInt);
        }else if(!TemplateEditorSingleton.getInstance().isTemplateImperial
                && TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            // the template is metric, but the user is imperial
            double valueDouble = Double.parseDouble(oldValue);
            int valueInt = (int) Math.round(valueDouble / 0.45359237);
            newValue = String.valueOf(valueInt);
        }else{
            newValue = oldValue;
        }
        return newValue;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_editor);

        final DayOfWeekChildFrag doW1 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW1);

        final DayOfWeekChildFrag doW2 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW2);

        final DayOfWeekChildFrag doW3 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW3);

        final DayOfWeekChildFrag doW4 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW4);

        final DayOfWeekChildFrag doW5 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW5);

        final DayOfWeekChildFrag doW6 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW6);

        final DayOfWeekChildFrag doW7 = new DayOfWeekChildFrag();
        dayOfWeekChildFragArrayList.add(doW7);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        ButterKnife.bind(this);

        HideKey.initialize(TemplateEditorActivity.this);

        title.setTypeface(lobster);

        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(TemplateEditorActivity.this, SignInActivity.class));
                }

            }
        };
        // [END auth_state_listener]

        final FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setUpNavDrawer(TemplateEditorActivity.this, toolbar);

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================

        if (getIntent().getExtras().getString("isEdit") != null) {
            if (getIntent().getExtras().getString("isEdit").equals("yes")){
                isTemplateEdit = true;
                if (getIntent().getExtras().getString("isFromPublic") != null) {
                    if (getIntent().getExtras().getString("isFromPublic").equals("yes")) {
                        // if template isEdit, isFromPublic
                        isFromPublic = true;

                        TemplateEditorSingleton.getInstance().isFromPublic = true;

                        templateNameEdit = getIntent().getExtras().getString("templateName");

                        // Check for active template
                        DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);

                        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                if(userModelClass.getActiveTemplate() != null){
                                    if(userModelClass.getActiveTemplate().equals(templateNameEdit)){
                                        activeTemplateCheckbox.setChecked(true);
                                    }
                                }
                                if(userModelClass.isIsImperial()){
                                    //TemplateEditorSingleton.getInstance().isImperial = true;
                                    TemplateEditorSingleton.getInstance().isCurrentUserImperial = true;
                                }else{
                                    //TemplateEditorSingleton.getInstance().isImperial = false;
                                    TemplateEditorSingleton.getInstance().isCurrentUserImperial = false;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                        DatabaseReference templateRef = mRootRef.child("publicTemplates").child("myPublic").child(uid).child(templateNameEdit);

                        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                TemplateModelClass templateClass = dataSnapshot.getValue(TemplateModelClass.class);


                                TemplateEditorSingleton.getInstance().mAlgorithmDateMap = templateClass.getAlgorithmDateMap();
                                TemplateEditorSingleton.getInstance().publicTemplateKeyId = templateClass
                                        .getPublicTemplateKeyId();

                                if(templateClass.isIsImperial()){
                                    TemplateEditorSingleton.getInstance().isTemplateImperial = true;
                                }else{
                                    TemplateEditorSingleton.getInstance().isTemplateImperial = false;
                                }

                                TemplateEditorSingleton.getInstance().mDateCreated = templateClass.getDateCreated();

                                SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                                String userName = sharedPref.getString("userName", "loading...");

                                if(!templateClass.getUserId().equals(uid)){
                                    // if editing someone else's template
                                    TemplateEditorSingleton.getInstance().mUserId = templateClass.getUserId();
                                    TemplateEditorSingleton.getInstance().mUserName = templateClass.getUserName();
                                    TemplateEditorSingleton.getInstance().mUserId2 = uid;
                                    TemplateEditorSingleton.getInstance().mUserName2 = userName;
                                }else{
                                    TemplateEditorSingleton.getInstance().mUserId = uid;
                                    TemplateEditorSingleton.getInstance().mUserName = userName;
                                }

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                if(templateClass.getMapOne() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW1.isEdit = true;
                                    doW1.isFirstTime = true;
                                    doW1.daysArray = daysToArray(templateClass.getMapOne().get("0_key").get(0));
                                    doW1.map = templateClass.getMapOne();
                                    doW1.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW1, fragString);
                                }
                                if(templateClass.getMapTwo() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW2.isEdit = true;
                                    doW2.isFirstTime = true;
                                    doW2.daysArray = daysToArray(templateClass.getMapTwo().get("0_key").get(0));
                                    doW2.map = templateClass.getMapTwo();
                                    doW2.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW2, fragString);
                                }
                                if(templateClass.getMapThree() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW3.isEdit = true;
                                    doW3.isFirstTime = true;
                                    doW3.daysArray = daysToArray(templateClass.getMapThree().get("0_key").get(0));
                                    doW3.map = templateClass.getMapThree();
                                    doW3.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW3, fragString);
                                }
                                if(templateClass.getMapFour() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW4.isEdit = true;
                                    doW4.isFirstTime = true;
                                    doW4.daysArray = daysToArray(templateClass.getMapFour().get("0_key").get(0));
                                    doW4.map = templateClass.getMapFour();
                                    doW4.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW4, fragString);
                                }
                                if(templateClass.getMapFive() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW5.isEdit = true;
                                    doW5.isFirstTime = true;
                                    doW5.daysArray = daysToArray(templateClass.getMapFive().get("0_key").get(0));
                                    doW5.map = templateClass.getMapFive();
                                    doW5.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW5, fragString);
                                }
                                if(templateClass.getMapSix() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW6.isEdit = true;
                                    doW6.isFirstTime = true;
                                    doW6.daysArray = daysToArray(templateClass.getMapSix().get("0_key").get(0));
                                    doW6.map = templateClass.getMapSix();
                                    doW6.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW6, fragString);
                                }
                                if(templateClass.getMapSeven() != null){
                                    ++fragIdCount;
                                    String fragString = Integer.toString(fragIdCount);
                                    doW7.isEdit = true;
                                    doW7.isFirstTime = true;
                                    doW7.daysArray = daysToArray(templateClass.getMapSeven().get("0_key").get(0));
                                    doW7.map = templateClass.getMapSeven();
                                    doW7.templateName = templateClass.getTemplateName();
                                    fragmentTransaction.add(R.id.templateFragmentLayout, doW7, fragString);
                                }

                                fragmentTransaction.commitAllowingStateLoss();

                                if(templateClass.getIsAlgorithm()){
                                    TemplateEditorSingleton.getInstance().mIsAlgorithm = true;
                                    if(templateClass.getIsAlgoApplyToAll()){
                                        TemplateEditorSingleton.getInstance().isAlgoApplyToAll = true;
                                        List<String> tempAlgoInfoList2 = new ArrayList<>();
                                        tempAlgoInfoList2.addAll(templateClass.getAlgorithmInfo().get("0_key"));
                                        String weightValue = handleUnitConversion(tempAlgoInfoList2.get(6));
                                        tempAlgoInfoList2.set(6, weightValue);
                                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.put("0_key", tempAlgoInfoList2);
                                    }else{
                                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo.putAll(templateClass.getAlgorithmInfo());
                                        for(Map.Entry<String, List<String>> entry : templateClass.getAlgorithmInfo().entrySet()){
                                            String key = entry.getKey();
                                            List<String> listValue = entry.getValue();
                                            String newValue = handleUnitConversion(listValue.get(6));
                                            EditTemplateAssemblerClass.getInstance().tempAlgoInfo.get(key).set(6,
                                                    newValue);
                                            //listValue.set(6, newValue);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }else{
                    // isEdit, not from public
                    templateNameEdit = getIntent().getExtras().getString("templateName");



                    // Check for active template
                    DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);

                    activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                            if(userModelClass.getActiveTemplate() != null){
                                if(userModelClass.getActiveTemplate().equals(templateNameEdit)){
                                    activeTemplateCheckbox.setChecked(true);
                                }
                            }
                            if(userModelClass.isIsImperial()){
                                TemplateEditorSingleton.getInstance().isCurrentUserImperial = true;
                            }else{
                                TemplateEditorSingleton.getInstance().isCurrentUserImperial = false;
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference templateRef = mRootRef.child("templates").child(uid).child(templateNameEdit);
                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            TemplateModelClass templateClass = dataSnapshot.getValue(TemplateModelClass.class);

                            TemplateEditorSingleton.getInstance().mAlgorithmDateMap = templateClass.getAlgorithmDateMap();

                            if(templateClass.isIsImperial()){
                                TemplateEditorSingleton.getInstance().isTemplateImperial = true;
                            }else{
                                TemplateEditorSingleton.getInstance().isTemplateImperial = false;
                            }

                            TemplateEditorSingleton.getInstance().mDateCreated = templateClass.getDateCreated();

                            SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                            String userName = sharedPref.getString("userName", "loading...");

                            if(!templateClass.getUserId().equals(uid)){
                                // if editing someone else's template
                                TemplateEditorSingleton.getInstance().mUserId = templateClass.getUserId();
                                TemplateEditorSingleton.getInstance().mUserName = templateClass.getUserName();
                                TemplateEditorSingleton.getInstance().mUserId2 = uid;
                                TemplateEditorSingleton.getInstance().mUserName2 = userName;
                            }else{
                                TemplateEditorSingleton.getInstance().mUserId = uid;
                                TemplateEditorSingleton.getInstance().mUserName = userName;
                            }

                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            if(templateClass.getMapOne() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW1.isEdit = true;
                                doW1.isFirstTime = true;
                                doW1.daysArray = daysToArray(templateClass.getMapOne().get("0_key").get(0));
                                doW1.map = templateClass.getMapOne();
                                doW1.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW1, fragString);
                            }
                            if(templateClass.getMapTwo() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW2.isEdit = true;
                                doW2.isFirstTime = true;
                                doW2.daysArray = daysToArray(templateClass.getMapTwo().get("0_key").get(0));
                                doW2.map = templateClass.getMapTwo();
                                doW2.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW2, fragString);
                            }
                            if(templateClass.getMapThree() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW3.isEdit = true;
                                doW3.isFirstTime = true;
                                doW3.daysArray = daysToArray(templateClass.getMapThree().get("0_key").get(0));
                                doW3.map = templateClass.getMapThree();
                                doW3.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW3, fragString);
                            }
                            if(templateClass.getMapFour() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW4.isEdit = true;
                                doW4.isFirstTime = true;
                                doW4.daysArray = daysToArray(templateClass.getMapFour().get("0_key").get(0));
                                doW4.map = templateClass.getMapFour();
                                doW4.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW4, fragString);
                            }
                            if(templateClass.getMapFive() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW5.isEdit = true;
                                doW5.isFirstTime = true;
                                doW5.daysArray = daysToArray(templateClass.getMapFive().get("0_key").get(0));
                                doW5.map = templateClass.getMapFive();
                                doW5.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW5, fragString);
                            }
                            if(templateClass.getMapSix() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW6.isEdit = true;
                                doW6.isFirstTime = true;
                                doW6.daysArray = daysToArray(templateClass.getMapSix().get("0_key").get(0));
                                doW6.map = templateClass.getMapSix();
                                doW6.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW6, fragString);
                            }
                            if(templateClass.getMapSeven() != null){
                                ++fragIdCount;
                                String fragString = Integer.toString(fragIdCount);
                                doW7.isEdit = true;
                                doW7.isFirstTime = true;
                                doW7.daysArray = daysToArray(templateClass.getMapSeven().get("0_key").get(0));
                                doW7.map = templateClass.getMapSeven();
                                doW7.templateName = templateClass.getTemplateName();
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW7, fragString);
                            }

                            fragmentTransaction.commitAllowingStateLoss();

                            if(templateClass.getIsAlgorithm()){
                                TemplateEditorSingleton.getInstance().mIsAlgorithm = true;
                                if(templateClass.getIsAlgoApplyToAll()){
                                    TemplateEditorSingleton.getInstance().isAlgoApplyToAll = true;
                                    List<String> tempAlgoInfoList2 = new ArrayList<>();
                                    tempAlgoInfoList2.addAll(templateClass.getAlgorithmInfo().get("0_key"));
                                    String weightValue = handleUnitConversion(tempAlgoInfoList2.get(6));
                                    tempAlgoInfoList2.set(6, weightValue);
                                    EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.put("0_key", tempAlgoInfoList2);
                                }else{
                                    EditTemplateAssemblerClass.getInstance().tempAlgoInfo.putAll(templateClass.getAlgorithmInfo());
                                    for(Map.Entry<String, List<String>> entry : templateClass.getAlgorithmInfo().entrySet()){
                                        String key = entry.getKey();
                                        List<String> listValue = entry.getValue();
                                        String newValue = handleUnitConversion(listValue.get(6));
                                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo.get(key).set(6,
                                                newValue);
                                        //listValue.set(6, newValue);
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }else if(getIntent().getExtras().getString("isEdit").equals("no")){
                //DatabaseReference firstTimeRef = mRootRef.child("firstTime").child(uid).child
                //        ("isFromScratchFirstTime");
                //if (getIntent().getExtras().getString("isEdit") != null) {
                //    if (getIntent().getExtras().getString("isEdit").equals("no")) {
                //    }
                //}
                DatabaseReference userRef = mRootRef.child("user").child(uid);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        TemplateEditorSingleton.getInstance().isCurrentUserImperial = userModelClass.isIsImperial();
                        TemplateEditorSingleton.getInstance().isTemplateImperial = userModelClass.isIsImperial();

                        Snackbar.make(getCurrentFocus(), "Click (+) Add Day Set to begin!", Snackbar
                                .LENGTH_LONG).show();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }

        // use increment to keep track of what the current doW frag is

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ++fragIdCount;

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);

                if(isFirstTimeTut){
                    doW1.firstTimeTut = true;
                }

                if(fragIdCount == 1){
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW1, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 2){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW2.daysArray = stringArray.clone();
                    doW2.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW2, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 3){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW3.daysArray = stringArray.clone();
                    doW3.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW3, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 4){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW4.daysArray = stringArray.clone();
                    doW4.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW4, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 5){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW5.daysArray = stringArray.clone();
                    doW5.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW5, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 6){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW6.daysArray = stringArray.clone();
                    doW6.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW6, fragString);
                    fragmentTransaction.commit();
                }else if(fragIdCount == 7){
                    String[] stringArray = getCurrentSelectedDays().toArray(new String[getCurrentSelectedDays().size()]);
                    doW7.daysArray = stringArray.clone();
                    doW7.isAdded = true;
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW7, fragString);
                    fragmentTransaction.commit();
                }


                CharSequence toastText = "Day-set Added";
                int duration = Toast.LENGTH_SHORT;

                try{
                    Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
                    snackbar.show();
                } catch (NullPointerException e){

                }


            }
        });

        removeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // only thing now is to remove all greyed out instances if the removed frag
                // has that selected day

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);

                if(fragIdCount != 1) {
                    CharSequence toastText = "Day-set Removed";
                    int duration = Toast.LENGTH_SHORT;
                    try{
                        Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
                        snackbar.show();
                    } catch (NullPointerException e){

                    }
                }

                if (fragIdCount != 0) {
                    if(fragIdCount == 2){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW2.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW2.dayUnselectedToFrag(days);
                            doW2.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW2.removeExercises();
                        --fragIdCount;
                    }else if(fragIdCount == 3){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW3.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW3.dayUnselectedToFrag(days);
                            doW3.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW3.removeExercises();
                        --fragIdCount;
                    }else if(fragIdCount == 4){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW4.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW4.dayUnselectedToFrag(days);
                            doW4.setToNull();

                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW4.removeExercises();
                        --fragIdCount;
                    }else if(fragIdCount == 5){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW5.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW5.dayUnselectedToFrag(days);
                            doW5.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW5.removeExercises();
                        --fragIdCount;
                    }else if(fragIdCount == 6){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW6.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW6.dayUnselectedToFrag(days);
                            doW6.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW6.removeExercises();
                        --fragIdCount;
                    }else if(fragIdCount == 7){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW7.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW7.dayUnselectedToFrag(days);
                            doW7.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                        doW7.removeExercises();
                        --fragIdCount;
                    }
                }


            }
        });

        Button onSave = (Button) findViewById(R.id.saveButton);

        onSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                boolean hasEmptyDays = false;
                boolean hasNoDays = false;

                if(doW1.getView() != null && doW1.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW2.getView() != null && doW2.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW3.getView() != null && doW3.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW4.getView() != null && doW4.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW5.getView() != null && doW5.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW6.getView() != null && doW6.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW7.getView() != null && doW7.getDoW().equals("")){
                    hasEmptyDays = true;
                }if(doW1.getView() == null
                        && doW2.getView() == null
                        && doW3.getView() == null
                        && doW4.getView() == null
                        && doW5.getView() == null
                        && doW6.getView() == null
                        && doW7.getView() == null){
                    hasNoDays = true;
                }

                if(hasNoDays){

                    AlertDialog.Builder builder = new AlertDialog.Builder(TemplateEditorActivity.this);
                    builder.setTitle("Error")
                            .setMessage("Add a day-set to begin!")
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    // create alert dialog
                    AlertDialog alertDialog = builder.create();

                    // show it
                    alertDialog.show();
                }else{
                    if(hasEmptyDays){
                        AlertDialog.Builder builder = new AlertDialog.Builder(TemplateEditorActivity.this);
                        // set title
                        builder.setTitle("Error");
                        // set dialog message
                        builder
                                .setMessage("One or more day-sets do not have selected days. Their contents will not be " +
                                        "saved!")
                                .setCancelable(false)
                                .setPositiveButton("Save anyway",new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {

                                        Intent intent = new Intent(TemplateEditorActivity.this, SaveTemplateDialog.class);

                                        boolean checkBool = activeTemplateCheckbox.isChecked();
                                        boolean isPublic = makePublicCheckbox.isChecked();
                                        String descriptionString = templateDescriptionEdit.getText().toString();
                                        TemplateEditorSingleton.getInstance().mDescription = descriptionString;

                                        if(getIntent().getExtras().getString("isEdit") != null) {
                                            if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                                                String templateName = getIntent().getExtras().getString("templateName");
                                                intent.putExtra("isEdit", "yes");
                                                intent.putExtra("templateName", templateName);
                                                intent.putExtra("isActiveTemplate", checkBool);
                                                //intent.putExtra("isAlgorithm", algBool);
                                                intent.putExtra("isPublic", isPublic);
                                                startActivityForResult(intent, 1);
                                            }else{
                                                intent.putExtra("isEdit", "no");
                                                intent.putExtra("isActiveTemplate", checkBool);
                                                //intent.putExtra("isAlgorithm", algBool);
                                                intent.putExtra("isPublic", isPublic);
                                                intent.putExtra("description", descriptionString);
                                                startActivityForResult(intent, 1);
                                            }
                                        }else{
                                            intent.putExtra("isEdit", "no");
                                            intent.putExtra("isActiveTemplate", checkBool);
                                            //intent.putExtra("isAlgorithm", algBool);
                                            intent.putExtra("isPublic", isPublic);
                                            intent.putExtra("description", descriptionString);
                                            startActivityForResult(intent, 1);
                                        }

                                        EditTemplateAssemblerClass.getInstance().isOnSaveClick = true;

                                    }
                                })
                                .setNegativeButton("Continue editing",new DialogInterface.OnClickListener() {
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
                    }else{
                        Intent intent = new Intent(TemplateEditorActivity.this, SaveTemplateDialog.class);

                        boolean checkBool = activeTemplateCheckbox.isChecked();
                        boolean isPublic = makePublicCheckbox.isChecked();
                        String descriptionString = templateDescriptionEdit.getText().toString();
                        TemplateEditorSingleton.getInstance().mDescription = descriptionString;

                        if(getIntent().getExtras().getString("isEdit") != null) {
                            if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                                String templateName = getIntent().getExtras().getString("templateName");
                                intent.putExtra("isEdit", "yes");
                                intent.putExtra("templateName", templateName);
                                intent.putExtra("isActiveTemplate", checkBool);
                                //intent.putExtra("isAlgorithm", algBool);
                                intent.putExtra("isPublic", isPublic);
                                startActivityForResult(intent, 1);
                            }else{
                                intent.putExtra("isEdit", "no");
                                intent.putExtra("isActiveTemplate", checkBool);
                                //intent.putExtra("isAlgorithm", algBool);
                                intent.putExtra("isPublic", isPublic);
                                intent.putExtra("description", descriptionString);
                                startActivityForResult(intent, 1);
                            }
                        }else{
                            intent.putExtra("isEdit", "no");
                            intent.putExtra("isActiveTemplate", checkBool);
                            //intent.putExtra("isAlgorithm", algBool);
                            intent.putExtra("isPublic", isPublic);
                            intent.putExtra("description", descriptionString);
                            startActivityForResult(intent, 1);
                        }

                        EditTemplateAssemblerClass.getInstance().isOnSaveClick = true;
                    }
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == 1){
                if(data != null){
                    // get info from the exercise frags

                    for(DayOfWeekChildFrag dayOfWeekChildFrag : dayOfWeekChildFragArrayList){
                        dayOfWeekChildFrag.setDoWInfo();
                    }

                    // set up other key things for template saved
                    Boolean activeTemplateCheckBool = activeTemplateCheckbox.isChecked();
                    String isEdit = null;
                    if(isTemplateEdit){
                        isEdit = "yes";
                    }
                    Intent intent = new Intent(TemplateEditorActivity.this, TemplateSavedActivity.class);
                    intent.putExtra("key1", data.getStringExtra("templateName"));
                    intent.putExtra("isActiveTemplate", activeTemplateCheckBool);
                    intent.putExtra("isFromEditor", true);
                    intent.putExtra("isEdit", isEdit);
                    //intent.putExtra("isAlgorithm", algBool);

                    if(!isTemplateEdit){
                        DateTime dateTime = new DateTime(DateTimeZone.UTC);
                        String dateTimeString = dateTime.toString();
                        TemplateEditorSingleton.getInstance().mDateCreated = dateTimeString;
                    }

                    TemplateEditorSingleton.getInstance().mIsPublic = isFromPublic;
                    //TemplateEditorSingleton.getInstance().mDescription = descriptionString;
                    TemplateEditorSingleton.getInstance().mTemplateName = data.getStringExtra("templateName");

                    startActivity(intent);
                }
            }
        }
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        DatabaseReference isFirstTemplate = mRootRef.child("templates").child(uid);
        isFirstTemplate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    activeTemplateCheckbox.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        final DatabaseReference firstTimeRef = mRootRef.child("firstTime").child(uid).child("isFromScratchFirstTime");
        firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TemplateEditorSingleton.getInstance().isFirstTimeTut = true;
                    FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder
                            (TemplateEditorActivity.this)
                            .title("Welcome to the Program Editor. This is where you can create " +
                                    "complex, custom workout programs.")
                            .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                            .build();
                    FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder
                            (TemplateEditorActivity.this)
                            .focusOn(addDay)
                            .title("Let's begin by adding a day set.")
                            .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER)
                            .focusShape(FocusShape.ROUNDED_RECTANGLE)
                            .fitSystemWindows(true)
                            .build();

                    new FancyShowCaseQueue()
                            .add(fancyShowCaseView1)
                            .add(fancyShowCaseView2)
                            .show();

                    isFirstTimeTut = true;

                    firstTimeRef.setValue(null);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    // [END on_stop_remove_listener]

    @Override
    public void onBackPressed(){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // set title
        builder.setTitle("Discard template?");

        // set dialog message
        builder
                .setMessage("Are you sure you want to discard changes to this template?")
                .setCancelable(false)
                .setPositiveButton("Discard",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        TemplateEditorSingleton.getInstance().clearAll();
                        EditTemplateAssemblerClass.getInstance().clearAll();

                        finish();
                    }
                })
                .setNegativeButton("Continue Editing",new DialogInterface.OnClickListener() {
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

    public void daySelectedFromFrag(String doW, String tag){
        try {
            for (int i = 0; i < fragIdCount; i++) {
                if (!dayOfWeekChildFragArrayList.get(i).getTag().equals(tag)) {
                    dayOfWeekChildFragArrayList.get(i).daySelectedToFrag(doW);
                }
            }
        } catch (IndexOutOfBoundsException e){
            System.out.print("wut");
        }
    }

    public void dayUnselectedFromFrag(String doW, String tag){
        for(int i = 0; i < fragIdCount; i++){
            if(!dayOfWeekChildFragArrayList.get(i).getTag().equals(tag)){
                dayOfWeekChildFragArrayList.get(i).dayUnselectedToFrag(doW);
            }
        }
    }

    public ArrayList<String> getSelectedDaysOtherThan(String tag){
        ArrayList<String> selectedDaysOtherThan = new ArrayList<>();

        for(int i = 0; i < fragIdCount; i++){
            if(!dayOfWeekChildFragArrayList.get(i).getTag().equals(tag)){
                ArrayList<String> getSelectedDays = dayOfWeekChildFragArrayList.get(i).getSelectedDays();
                for(String day : getSelectedDays){
                    selectedDaysOtherThan.add(day);
                }
            }
        }

        return selectedDaysOtherThan;
    }

    public void setUnChecked(String doW, String tag){

    }

    public ArrayList<String> getCurrentSelectedDays(){
        ArrayList<String> selectedDaysAL = new ArrayList<>();
        for(int i = 0; i < fragIdCount - 1; i++){
            ArrayList<String> iSelectedDaysAL = new ArrayList<>();
            iSelectedDaysAL = dayOfWeekChildFragArrayList.get(i).getSelectedDays();
            for(String day : iSelectedDaysAL){
                selectedDaysAL.add(day);
            }
        }

        return selectedDaysAL;
    }

    public void setToGold(){
        for(DayOfWeekChildFrag dayOfWeekChildFrag : dayOfWeekChildFragArrayList){
            dayOfWeekChildFrag.setToGold();
        }
    }

    public void removeGold(){
        for(DayOfWeekChildFrag dayOfWeekChildFrag : dayOfWeekChildFragArrayList){
            dayOfWeekChildFrag.removeGold();
        }
    }

    String[] daysToArray(String daysUn){
        String[] days = daysUn.split("_");
        return days;
    }

}
