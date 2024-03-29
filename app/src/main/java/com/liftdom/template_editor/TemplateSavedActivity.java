package com.liftdom.template_editor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.LinearLayout;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
import com.liftdom.misc_activities.SettingsListActivity;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


import java.util.HashMap;
import java.util.List;


public class TemplateSavedActivity extends BaseActivity {

    private static final String TAG = "EmailPassword";
    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    // declare_auth
    private FirebaseUser mFirebaseUser;

    // Butterknife binds
    @BindView(R.id.goBackHome) Button goHome;
    @BindView(R.id.goBackToTemplates) Button goToTemplates;
    @BindView(R.id.templateSavedHolder) LinearLayout templateHolder;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    String templateName;
    Boolean checkBool;
    boolean isEdit = false;
    //Boolean algBool;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    Bundle mSaved;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_saved);

        ButterKnife.bind(this);

        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mSaved = savedInstanceState;

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
        }

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
                    startActivity(new Intent(TemplateSavedActivity.this, SignInActivity.class));
                }

            }
        };
        // [END auth_state_listener]


        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Saved Programs");

        setUpNavDrawer(TemplateSavedActivity.this, toolbar);

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        goToTemplates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("fragID", 1);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart(){
        super.onStart();

        if(mSaved == null && getIntent().getExtras().getBoolean("isFromEditor")) {

            if(getIntent().getExtras().getString("isEdit") != null){
                if(getIntent().getExtras().getString("isEdit").equals("yes")){
                    isEdit = true;
                }
            }

            Intent intent = getIntent();
            templateName = intent.getStringExtra("key1");
            checkBool = getIntent().getExtras().getBoolean("isActiveTemplate");

            if(checkBool){
                final DatabaseReference activeTemplateRef = mRootRef.child("user").child(uid);
                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        userModelClass.setActiveTemplate(templateName);
                        activeTemplateRef.setValue(userModelClass);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            final DatabaseReference runningAssistorRef = mRootRef.child("runningAssistor").child(uid);
            runningAssistorRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        runningAssistorRef.setValue(null);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            DatabaseReference selectedTemplateDataRef;

            if(TemplateEditorSingleton.getInstance().isFromPublic){
                selectedTemplateDataRef = mRootRef.child("publicTemplates").child("myPublic").child(uid).child
                        (templateName);
            }else{
                selectedTemplateDataRef = mRootRef.child("templates").child(uid).child(templateName);
            }

            String xTemplateName = TemplateEditorSingleton.getInstance().mTemplateName;
            String xUserId = TemplateEditorSingleton.getInstance().mUserId;
            String xUserName= TemplateEditorSingleton.getInstance().mUserName;
            String xUserId2 = TemplateEditorSingleton.getInstance().mUserId2;
            String xUserName2 = TemplateEditorSingleton.getInstance().mUserName2;
            final boolean xIsPublic = TemplateEditorSingleton.getInstance().mIsPublic;
            String xDateCreated = TemplateEditorSingleton.getInstance().mDateCreated;
            String xDescription = TemplateEditorSingleton.getInstance().mDescription;
            String xRestTimer = TemplateEditorSingleton.getInstance().mRestTime;
            boolean xIsActiveRestTimer = TemplateEditorSingleton.getInstance().mIsActiveRestTimer;
            String xVibrationTimer = TemplateEditorSingleton.getInstance().mVibrationTime;
            boolean xIsShowAlert = TemplateEditorSingleton.getInstance().mIsRestTimerAlert;
            HashMap<String, List<String>> xMapOne = new HashMap<>();
            xMapOne.putAll(TemplateEditorSingleton.getInstance().mapOne);
            HashMap<String, List<String>> xMapTwo = new HashMap<>();
            xMapTwo.putAll(TemplateEditorSingleton.getInstance().mapTwo);
            HashMap<String, List<String>> xMapThree = new HashMap<>();
            xMapThree.putAll(TemplateEditorSingleton.getInstance().mapThree);
            HashMap<String, List<String>> xMapFour = new HashMap<>();
            xMapFour.putAll(TemplateEditorSingleton.getInstance().mapFour);
            HashMap<String, List<String>> xMapFive = new HashMap<>();
            xMapFive.putAll(TemplateEditorSingleton.getInstance().mapFive);
            HashMap<String, List<String>> xMapSix = new HashMap<>();
            xMapSix.putAll(TemplateEditorSingleton.getInstance().mapSix);
            HashMap<String, List<String>> xMapSeven = new HashMap<>();
            xMapSeven.putAll(TemplateEditorSingleton.getInstance().mapSeven);
            boolean xIsAlgorithm = TemplateEditorSingleton.getInstance().mIsAlgorithm;
            boolean xIsAlgoApplyToAll = TemplateEditorSingleton.getInstance().isAlgoApplyToAll;
            HashMap<String, List<String>> xAlgorithmInfo;
            if(xIsAlgoApplyToAll){
                xAlgorithmInfo = EditTemplateAssemblerClass.getInstance().tempAlgoInfo2;
            }else{
                xAlgorithmInfo = TemplateEditorSingleton.getInstance().mAlgorithmInfo;
            }
            HashMap<String, List<String>> xAlgorithmDateMap = TemplateEditorSingleton.getInstance().mAlgorithmDateMap;

            String xDays = getDays(xMapOne, xMapTwo, xMapThree, xMapFour, xMapFive, xMapSix, xMapSeven);

            String dateUpdated = null;
            if(isEdit){
                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                dateUpdated = dateTime.toString();
                Log.i("isEdit", "yes");
            }else{
                dateUpdated = xDateCreated;
                Log.i("isEdit", "no");
            }

            String workoutType = "placeholder";

            boolean unitsIsImperial;

            if(isEdit){
                unitsIsImperial = TemplateEditorSingleton.getInstance().isTemplateImperial;
            }else{
                unitsIsImperial = TemplateEditorSingleton.getInstance().isCurrentUserImperial;
            }

            final TemplateModelClass modelClass = new TemplateModelClass(xTemplateName, xDays, xUserId, xUserName,
                                                xUserId2, xUserName2, xIsPublic,
                                                xDateCreated, dateUpdated, workoutType, xDescription, xMapOne, xMapTwo,
                                                xMapThree, xMapFour, xMapFive, xMapSix,
                                                xMapSeven, xIsAlgorithm, xIsAlgoApplyToAll, xAlgorithmInfo,
                    xAlgorithmDateMap, unitsIsImperial, null, xRestTimer, xIsActiveRestTimer,
                    xVibrationTimer, xIsShowAlert);

            if(TemplateEditorSingleton.getInstance().isFromPublic){
                modelClass.setPublicTemplateKeyId(TemplateEditorSingleton.getInstance().publicTemplateKeyId);
            }

            selectedTemplateDataRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    if(TemplateEditorSingleton.getInstance().isFromPublic){
                        String key = modelClass.getPublicTemplateKeyId();
                        DatabaseReference publicRef = mRootRef.child("publicTemplates").child("public").child(key);
                        publicRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                DatabaseReference runningRef =
                                        mRootRef.child("templatesRunning").child(uid);
                                runningRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        loadingView.setVisibility(View.GONE);
                                        templateHolder.setVisibility(View.VISIBLE);
                                        TemplateEditorSingleton.getInstance().clearAll();
                                        EditTemplateAssemblerClass.getInstance().clearAll();
                                    }
                                });

                            }
                        });
                    }else{
                        DatabaseReference runningRef =
                                mRootRef.child("templatesRunning").child(uid);
                        runningRef.setValue(null).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                loadingView.setVisibility(View.GONE);
                                templateHolder.setVisibility(View.VISIBLE);
                                TemplateEditorSingleton.getInstance().clearAll();
                                EditTemplateAssemblerClass.getInstance().clearAll();
                            }
                        });
                    }
                }
            });
        }

        // END UPLOAD OF TEMPLATE

        getIntent().removeExtra("isFromEditor");

    }

    String[] stringSplitter(String unSplitted){
        String delims = "[,]";
        String[] splitted = unSplitted.split(delims);
        return splitted;
    }

    String getDays(HashMap<String, List<String>> mMapOne,
            HashMap<String, List<String>> mMapTwo,
            HashMap<String, List<String>> mMapThree,
            HashMap<String, List<String>> mMapFour,
            HashMap<String, List<String>> mMapFive,
            HashMap<String, List<String>> mMapSix,
            HashMap<String, List<String>> mMapSeven){
        String days = "";
        if(mMapOne != null && !mMapOne.isEmpty()){
            String daysOne = mMapOne.get("0_key").get(0);
            days = days + daysOne;
        }
        if(mMapTwo != null && !mMapTwo.isEmpty()){
            if(mMapTwo.get("0_key") != null){
                String daysOne = mMapTwo.get("0_key").get(0);
                days = days + daysOne;
            }
        }
        if(mMapThree != null && !mMapThree.isEmpty()){
            if(mMapThree.get("0_key") != null){
                String daysOne = mMapThree.get("0_key").get(0);
                days = days + daysOne;
            }
        }
        if(mMapFour != null && !mMapFour.isEmpty()){
            if(mMapFour.get("0_key") != null){
                String daysOne = mMapFour.get("0_key").get(0);
                days = days + daysOne;
            }
        }
        if(mMapFive != null && !mMapFive.isEmpty()){
            if(mMapFive.get("0_key") != null){
                String daysOne = mMapFive.get("0_key").get(0);
                days = days + daysOne;
            }
        }
        if(mMapSix != null && !mMapSix.isEmpty()){
            if(mMapSix.get("0_key") != null){
                String daysOne = mMapSix.get("0_key").get(0);
                days = days + daysOne;
            }
        }
        if(mMapSeven != null && !mMapSeven.isEmpty()){
            if(mMapSeven.get("0_key") != null){
                String daysOne = mMapSeven.get("0_key").get(0);
                days = days + daysOne;
            }
        }

        return days;
    }


}
