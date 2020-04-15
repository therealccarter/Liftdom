package com.liftdom.template_editor;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.InputFilter;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
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
import com.liftdom.workout_assistor.AssistorServiceClass;
import com.wang.avi.AVLoadingIndicatorView;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateEditorActivity extends BaseActivity
        implements DayOfWeekChildFrag.onDaySelectedListener,
        ExerciseLevelChildFrag.setToGoldCallback,
        ExerciseLevelChildFrag.removeGoldCallback,
        DayOfWeekChildFrag.updateCallback{

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    int fragIdCount = 0;
    boolean isFirstTimeTut = false;
    boolean isTemplateEdit;
    boolean isFromPublic;
    boolean restTimerBool = false;
    String templateNameEdit;

    ArrayList<DayOfWeekChildFrag> dayOfWeekChildFragArrayList = new ArrayList<>();

    DayOfWeekChildFrag doW1 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW2 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW3 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW4 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW5 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW6 = new DayOfWeekChildFrag();
    DayOfWeekChildFrag doW7 = new DayOfWeekChildFrag();

    // butterknife
    @BindView(R.id.addDay) Button addDay;
    @BindView(R.id.removeDay) Button removeDay;
    //@BindView(R.id.saveButton) Button onSave;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.makePublicCheckbox) CheckBox makePublicCheckbox;
    @BindView(R.id.descriptionEditText) EditText templateDescriptionEdit;
    @BindView(R.id.title) TextView title;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.restTimerLL) LinearLayout restTimerLL;
    @BindView(R.id.restTimerSwitch) Switch restTimerSwitch;
    @BindView(R.id.restTimerInfoLL) LinearLayout restTimerInfoLL;
    @BindView(R.id.minutes) EditText minutesEditText;
    @BindView(R.id.seconds) EditText secondsEditText;
    @BindView(R.id.secondsVibrate) EditText secondsVibrateEditText;
    @BindView(R.id.showRestTimerAlertRadioButton) RadioButton showRestTimerAlertRB;
    @BindView(R.id.justVibrateRadioButton) RadioButton justVibrateRB;
    @BindView(R.id.updateButton) Button updateButton;
    @BindView(R.id.templateNameView) TextView templateNameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_editor);

        dayOfWeekChildFragArrayList.add(doW1);
        doW1.doWTag = 1;

        dayOfWeekChildFragArrayList.add(doW2);
        doW2.doWTag = 2;

        dayOfWeekChildFragArrayList.add(doW3);
        doW3.doWTag = 3;

        dayOfWeekChildFragArrayList.add(doW4);
        doW4.doWTag = 4;

        dayOfWeekChildFragArrayList.add(doW5);
        doW5.doWTag = 5;

        dayOfWeekChildFragArrayList.add(doW6);
        doW6.doWTag = 6;

        dayOfWeekChildFragArrayList.add(doW7);
        doW7.doWTag = 7;

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        ButterKnife.bind(this);

        HideKey.initialize(TemplateEditorActivity.this);

        title.setTypeface(lobster);

        //if(savedInstanceState == null){
            showRestTimerAlertRB.setChecked(true);
        //}

        secondsEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        minutesEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 15)});
        secondsVibrateEditText.setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});

        restTimerLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                }else{
                    restTimerBool = false;
                    restTimerInfoLL.setVisibility(View.GONE);
                }
            }
        });

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

        // use increment to keep track of what the current doW frag is

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTemplateNode();
            }
        });

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addDaySet();
            }
        });

        removeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragIdCount != 0 && fragIdCount != 1){
                    Intent intent = new Intent(v.getContext(), DaySetDeleteConfirmation.class);
                    startActivityForResult(intent, 2);
                }else if(fragIdCount == 0){
                    try{
                        Snackbar.make(getCurrentFocus(), "No day sets have been added",
                                Snackbar.LENGTH_SHORT).show();
                    }catch (NullPointerException e){

                    }
                }else{
                    try{
                        Snackbar.make(getCurrentFocus(), "One day set is the minimum amount",
                                Snackbar.LENGTH_SHORT).show();
                    }catch (NullPointerException e){

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
                                        //boolean isPublic = makePublicCheckbox.isChecked();
                                        boolean isPublic =
                                                TemplateEditorSingleton.getInstance().isFromPublic;
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
                                                if(TemplateEditorSingleton.getInstance().isEdit){
                                                    intent.putExtra("isEdit", "yes");
                                                    intent.putExtra("templateName", templateNameEdit);
                                                }else{
                                                    intent.putExtra("isEdit", "no");
                                                }
                                                intent.putExtra("isActiveTemplate", checkBool);
                                                //intent.putExtra("isAlgorithm", algBool);
                                                intent.putExtra("isPublic", isPublic);
                                                intent.putExtra("description", descriptionString);
                                                startActivityForResult(intent, 1);
                                            }
                                        }else{
                                            if(TemplateEditorSingleton.getInstance().isEdit){
                                                intent.putExtra("isEdit", "yes");
                                                intent.putExtra("templateName", templateNameEdit);
                                            }else{
                                                intent.putExtra("isEdit", "no");
                                            }
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
                                if(TemplateEditorSingleton.getInstance().isEdit){
                                    intent.putExtra("isEdit", "yes");
                                    intent.putExtra("templateName", templateNameEdit);
                                }else{
                                    intent.putExtra("isEdit", "no");
                                }
                                intent.putExtra("isActiveTemplate", checkBool);
                                //intent.putExtra("isAlgorithm", algBool);
                                intent.putExtra("isPublic", isPublic);
                                intent.putExtra("description", descriptionString);
                                startActivityForResult(intent, 1);
                            }
                        }else{
                            if(TemplateEditorSingleton.getInstance().isEdit){
                                intent.putExtra("isEdit", "yes");
                                intent.putExtra("templateName", templateNameEdit);
                            }else{
                                intent.putExtra("isEdit", "no");
                            }
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

        if(dayOfWeekChildFragArrayList.get(0).buttonbar == null){
            flashAddDay();
        }

        checkForEditState();

    }


    @Override
    public void onResume(){
        super.onResume();

        /**
         * so we don't want to clear before updating...
         */

        if(hasHitOnPause){
            hideKeyboard();
            hasHitOnPause = false;
            //cleanUpState();
            checkRunningNode();
        }

    }

    private void hideKeyboard(){
        try{
            InputMethodManager imm =
                    (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }catch (NullPointerException e){

        }
    }

    boolean hasHitOnPause = false;

    @Override
    public void onPause(){
        super.onPause();

        hasHitOnPause = true;

    }

    private void checkRunningNode(){

        DatabaseReference runningRef =
                mRootRef.child("templatesRunning").child(uid);

        runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                    if(modelClass.getMapOne() != null){
                        inflateFromTemplateModelClass(modelClass);
                    }else{
                        checkForEditState();
                    }
                }else{
                    // do nothing?
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
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

        if(isTemplateEdit){

        }else{
            final DatabaseReference firstTimeRef = mRootRef.child("firstTime").child(uid).child("isFromScratchFirstTime");
            firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        TemplateEditorSingleton.getInstance().isFirstTimeTut = true;
                        FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder
                                (TemplateEditorActivity.this)
                                .title("Welcome to the Program Editor. This is where you can create " +
                                        "complex custom workout programs.")
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

    }

    /**
     * Where we at: we solved the isPublic = false problem.
     * However, we need to make sure that the userIds (1 and 2) (usernames too)
     * are going to be set correctly. We want users to be able to "fork" workouts.
     *
     * If they simply save a workout it retains the user ids, and we also must probably set isPublic to
     * false, because it's just going to their normal templates node.
     * However, it will retain uid1 as a different uid. So that's how we'll tell.
     *
     * Then if they edit it AND publish it themselves, they become the author?
     * Either way, it'll have a lot to do with isPublic vs isFromPublic.
     *
     * So there's two cases:
     * Someone editing a public template they've saved.
     *  - Same as normal isEdit, but retain different uids.
     *
     * Someone editing a public template of their own in myPublic.
     *  - If it's their own, it wouldn't have different uids, but we can still do the check.
     *  - Definitely retain isPublic and the key. Then on save it should do it's thing
     *
     */

    private void inflateFromTemplateModelClass(TemplateModelClass templateClass){
        if(templateClass != null && templateClass.getUserId() != null){

            cleanUpState();

            if(!isTemplateEdit){
                if(templateClass.isIsEdit()){
                    TemplateEditorSingleton.getInstance().isEdit = true;
                    isTemplateEdit = true;
                    //getIntent().putExtra("isEdit", "yes");
                }else{
                    TemplateEditorSingleton.getInstance().isEdit = false;
                    isTemplateEdit = false;
                    //getIntent().putExtra("isEdit", "no");
                }
            }

            if(templateClass.getTemplateName() != null){
                TemplateEditorSingleton.getInstance().mTemplateName =
                        templateClass.getTemplateName();
                templateNameEdit = templateClass.getTemplateName();
                templateNameView.setText(templateClass.getTemplateName());
                templateNameView.setVisibility(View.VISIBLE);
            }

            if(templateClass.isIsPublic()){
                TemplateEditorSingleton.getInstance().isFromPublic = true;
                TemplateEditorSingleton.getInstance().mIsPublic = true;
                makePublicCheckbox.setChecked(true);
                isFromPublic = true;
                TemplateEditorSingleton.getInstance().publicTemplateKeyId = templateClass
                        .getPublicTemplateKeyId();
                //getIntent().putExtra("isFromPublic", "yes");
            }else{
                TemplateEditorSingleton.getInstance().isFromPublic = false;
                TemplateEditorSingleton.getInstance().mIsPublic = false;
                //getIntent().putExtra("isFromPublic", "no");
                makePublicCheckbox.setChecked(false);
                isFromPublic = false;
            }

            TemplateEditorSingleton.getInstance().mAlgorithmDateMap = templateClass.getAlgorithmDateMap();

            if(templateClass.isIsImperial()){
                TemplateEditorSingleton.getInstance().isTemplateImperial = true;
            }else{
                TemplateEditorSingleton.getInstance().isTemplateImperial = false;
            }

            if(templateClass.getRestTime() != null){
                String delims = "[:]";
                String[] tokens = templateClass.getRestTime().split(delims);
                minutesEditText.setText(tokens[0]);
                secondsEditText.setText(tokens[1]);
            }

            if(templateClass.getVibrationTime() != null){
                secondsVibrateEditText.setText(templateClass.getVibrationTime());
                if(templateClass.isIsRestTimerAlert()){
                    justVibrateRB.setChecked(false);
                    showRestTimerAlertRB.setChecked(true);
                }else{
                    justVibrateRB.setChecked(true);
                    showRestTimerAlertRB.setChecked(false);
                }
            }

            restTimerSwitch.setChecked(templateClass.isIsActiveRestTimer());

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

            int count = templateClass.getMapCount();

            if(templateClass.getMapOne() != null){
                ++fragIdCount;
                String fragString = Integer.toString(fragIdCount);
                if(count == 1){
                    doW1.isBiggest = true;
                }
                doW1.isEdit = true;
                doW1.isFirstTime = true;
                doW1.daysArray = daysToArray(templateClass.getMapOne().get("0_key").get(0));
                doW1.map = templateClass.getMapOne();
                doW1.templateName = templateClass.getTemplateName();
                if(fragmentManager.findFragmentByTag(fragString) != null){
                    fragmentTransaction.replace(R.id.templateFragmentLayout, doW1, fragString);
                }else{
                    fragmentTransaction.add(R.id.templateFragmentLayout, doW1, fragString);
                }
                loadingView.setVisibility(View.GONE);
            }
            if(templateClass.getMapTwo() != null){
                ++fragIdCount;
                String fragString = Integer.toString(fragIdCount);
                if(count == 2){
                    doW2.isBiggest = true;
                }
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
                if(count == 3){
                    doW3.isBiggest = true;
                }
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
                if(count == 4){
                    doW4.isBiggest = true;
                }
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
                if(count == 5){
                    doW5.isBiggest = true;
                }
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
                if(count == 6){
                    doW6.isBiggest = true;
                }
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
                if(count == 7){
                    doW7.isBiggest = true;
                }
                doW7.isEdit = true;
                doW7.isFirstTime = true;
                doW7.daysArray = daysToArray(templateClass.getMapSeven().get("0_key").get(0));
                doW7.map = templateClass.getMapSeven();
                doW7.templateName = templateClass.getTemplateName();
                fragmentTransaction.add(R.id.templateFragmentLayout, doW7, fragString);
            }

            if(templateClass.getMapOne() == null && templateClass.getMapTwo() == null &&
                    templateClass.getMapThree() == null && templateClass.getMapFour() == null &&
                    templateClass.getMapFive() == null && templateClass.getMapSix() == null &&
                    templateClass.getMapSeven() == null){
                loadingView.setVisibility(View.GONE);
                addDaySet();
            }

            fragmentTransaction.commit();

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
            if(timer2 != null){
                timer2.cancel();
            }
            timer2 = new CountDownTimer(2000, 2000) {
                @Override
                public void onTick(long millisUntilFinished) {

                }

                @Override
                public void onFinish() {
                    //updateTemplateNode();
                }
            }.start();
        }
    }

    CountDownTimer timer2;


    //First: updating right after inflating doesn't work, it doesn't catch the DoW maps
    //Second: having checkForEditState in onResume means that every time it comes back in view,
    //          it's still got the intents so it erases any progress and re-inflates from default.

    /**
     * We should put checkForEditState in onCreate. Then again in onResume. However, we'll put a
     * boolean in onPause so we aren't onResuming from the start, only if onPause gets triggered
     * first. In onResume it shouldn't be full checkForEditState, it'll just be checking the
     * running ref. To protect against a malformed/too early update, if there are no maps, we
     * will do full checkForEditState.
     */

    private void checkForEditState(){
        if (getIntent().getExtras().getString("isEdit") != null) {
            if (getIntent().getExtras().getString("isEdit").equals("yes")){
                isTemplateEdit = true;
                TemplateEditorSingleton.getInstance().isEdit = true;
                if (getIntent().getExtras().getString("isFromPublic") != null) {
                    if (getIntent().getExtras().getString("isFromPublic").equals("yes")) {
                        // if template isEdit, isFromPublic
                        isFromPublic = true;
                        TemplateEditorSingleton.getInstance().isFromPublic = true;
                        TemplateEditorSingleton.getInstance().mIsPublic = true;

                        templateNameEdit = getIntent().getExtras().getString("templateName");
                        TemplateEditorSingleton.getInstance().mTemplateName = getIntent().getExtras().getString("templateName");
                        templateNameView.setText(templateNameEdit);
                        templateNameView.setVisibility(View.VISIBLE);

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

                                inflateFromTemplateModelClass(templateClass);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                }else{

                    // isEdit, not from public, coming from saved workouts.
                    templateNameEdit = getIntent().getExtras().getString("templateName");
                    TemplateEditorSingleton.getInstance().mTemplateName = getIntent().getExtras().getString("templateName");
                    templateNameView.setText(templateNameEdit);
                    templateNameView.setVisibility(View.VISIBLE);

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
                            inflateFromTemplateModelClass(templateClass);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
            }else if(getIntent().getExtras().getString("isEdit").equals("no")){
                // totally from scratch
                restTimerSwitch.setChecked(true);

                DatabaseReference userRef = mRootRef.child("user").child(uid);

                userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                        TemplateEditorSingleton.getInstance().isCurrentUserImperial = userModelClass.isIsImperial();
                        TemplateEditorSingleton.getInstance().isTemplateImperial = userModelClass.isIsImperial();
                        if(userModelClass.getActiveTemplate() == null){
                            activeTemplateCheckbox.setChecked(true);
                        }

                        try{
                            if(getCurrentFocus() != null){
                                Snackbar.make(getCurrentFocus(), "Click (+) Add Day Set to begin!", Snackbar
                                        .LENGTH_LONG).show();
                            }
                        }catch (IllegalStateException e){

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference runningRef =
                        mRootRef.child("templatesRunning").child(uid).child("userId");

                runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            // we ask first
                            //TODO This detects true even well after the node has been deleted
                            //checkRunningNode();
                            inflateFromRunning();
                        }else{
                            // do nothing?
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                loadingView.setVisibility(View.GONE);
            }
        }
    }


    private void cleanUpState(){
        if(!dayOfWeekChildFragArrayList.isEmpty()){
            removeAllDaySets();
        }
    }

    public void updateTemplateNode(){
        TemplateEditorSingleton.getInstance().clearWorkoutInfo();

        for(DayOfWeekChildFrag dayOfWeekChildFrag : dayOfWeekChildFragArrayList){
            dayOfWeekChildFrag.setDoWInfoRunning();
        }

        String dateUpdated = null;

        DateTime dateTime = new DateTime(DateTimeZone.UTC);
        dateUpdated = dateTime.toString();

        DatabaseReference selectedTemplateDataRef;
        selectedTemplateDataRef = mRootRef.child("templatesRunning").child(uid);

        //selectedTemplateDataRef.setValue(null);

        String descriptionString = templateDescriptionEdit.getText().toString();
        TemplateEditorSingleton.getInstance().mDescription = descriptionString;
        TemplateEditorSingleton.getInstance().mIsActiveRestTimer = restTimerBool;
        TemplateEditorSingleton.getInstance().mRestTime = minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString();
        TemplateEditorSingleton.getInstance().mVibrationTime =
                secondsVibrateEditText.getText().toString();
        TemplateEditorSingleton.getInstance().mIsRestTimerAlert =
                showRestTimerAlertRB.isChecked();


        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "loading...");
        if(TemplateEditorSingleton.getInstance().isFromPublic){
            // if editing someone else's template
            TemplateEditorSingleton.getInstance().mUserId2 = uid;
            TemplateEditorSingleton.getInstance().mUserName2 = userName;
        }else{
            TemplateEditorSingleton.getInstance().mUserId = uid;
            TemplateEditorSingleton.getInstance().mUserName = userName;
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

        String workoutType = "placeholder";

        boolean unitsIsImperial;

        unitsIsImperial = TemplateEditorSingleton.getInstance().isCurrentUserImperial;

        final TemplateModelClass modelClass = new TemplateModelClass(xTemplateName, xDays, xUserId, xUserName,
                xUserId2, xUserName2, xIsPublic,
                xDateCreated, dateUpdated, workoutType, xDescription, xMapOne, xMapTwo,
                xMapThree, xMapFour, xMapFive, xMapSix,
                xMapSeven, xIsAlgorithm, xIsAlgoApplyToAll, xAlgorithmInfo,
                xAlgorithmDateMap, unitsIsImperial, null, xRestTimer, xIsActiveRestTimer,
                xVibrationTimer, xIsShowAlert);

        modelClass.setIsEdit(TemplateEditorSingleton.getInstance().isEdit);

        if(TemplateEditorSingleton.getInstance().isFromPublic){
            modelClass.setPublicTemplateKeyId(TemplateEditorSingleton.getInstance().publicTemplateKeyId);
        }

        selectedTemplateDataRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                boolean myAss = TemplateEditorSingleton.getInstance().isCurrentUserImperial;
            }
        });
    }

    // END UPLOAD OF TEMPLATE

    private void addDaySet(){
        if(timer != null){
            timer.cancel();
            addDay.setBackgroundColor(Color.parseColor("#000000"));
        }

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
        int duration = Snackbar.LENGTH_SHORT;

        try{
            Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
            snackbar.show();
        } catch (NullPointerException e){

        }

        //updateTemplateNode();
    }

    private void removeAllDaySets(){
        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //String fragString = Integer.toString(fragIdCount);

        if(fragIdCount != 0){
            for(int i = fragIdCount; i > 0 ; i--){
                String fragString = Integer.toString(i);
                handleDoWRemoval(i, fragString);
                fragmentTransaction.remove(dayOfWeekChildFragArrayList.get(i - 1));
            }
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.popBackStackImmediate();
        }

        fragIdCount = 0;
    }

    private void handleDoWRemoval(int index, String fragString){
        ArrayList<String> removeList = new ArrayList<>();
        if(index == 1){
            removeList = doW1.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW1.dayUnselectedToFrag(days);
                doW1.setToNull();
            }
            doW1.removeExercises();
        }else if(index == 2){
            removeList = doW2.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW2.dayUnselectedToFrag(days);
                doW2.setToNull();
            }
            doW2.removeExercises();
        }else if(index == 3){
            removeList = doW3.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW3.dayUnselectedToFrag(days);
                doW3.setToNull();
            }
            doW3.removeExercises();
        }else if(index == 4){
            removeList = doW4.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW4.dayUnselectedToFrag(days);
                doW4.setToNull();
            }
            doW4.removeExercises();
        }else if(index == 5){
            removeList = doW5.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW5.dayUnselectedToFrag(days);
                doW5.setToNull();
            }
            doW5.removeExercises();
        }else if(index == 6){
            removeList = doW6.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW6.dayUnselectedToFrag(days);
                doW6.setToNull();
            }
            doW6.removeExercises();
        }else if(index == 7){
            removeList = doW7.getSelectedDays();
            for(String days : removeList){
                dayUnselectedFromFrag(days, fragString);
                doW7.dayUnselectedToFrag(days);
                doW7.setToNull();
            }
            doW7.removeExercises();
        }
    }

    private void removeDaySet(){
        // only thing now is to remove all greyed out instances if the removed frag
        // has that selected day

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String fragString = Integer.toString(fragIdCount);

        if(fragIdCount != 1) {
            CharSequence toastText = "Day-set Removed";
            int duration = Snackbar.LENGTH_SHORT;
            try{
                Snackbar snackbar = Snackbar.make(getCurrentFocus(), toastText, duration);
                snackbar.show();
            } catch (NullPointerException e){

            }
            //updateTemplateNode();
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

    private void inflateFromRunning(){
        DatabaseReference runningRef = mRootRef.child("templatesRunning").child(uid);
        runningRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                inflateFromTemplateModelClass(modelClass);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                    TemplateEditorSingleton.getInstance().mIsActiveRestTimer = restTimerBool;
                    TemplateEditorSingleton.getInstance().mRestTime = minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString();
                    TemplateEditorSingleton.getInstance().mVibrationTime =
                            secondsVibrateEditText.getText().toString();
                    TemplateEditorSingleton.getInstance().mIsRestTimerAlert =
                            showRestTimerAlertRB.isChecked();


                    if(!isTemplateEdit){
                        DateTime dateTime = new DateTime(DateTimeZone.UTC);
                        String dateTimeString = dateTime.toString();
                        TemplateEditorSingleton.getInstance().mDateCreated = dateTimeString;
                    }

                    TemplateEditorSingleton.getInstance().mIsPublic = isFromPublic;
                    //TemplateEditorSingleton.getInstance().mDescription = descriptionString;
                    TemplateEditorSingleton.getInstance().mTemplateName = data.getStringExtra("templateName");

                    Intent stopIntent = new Intent(TemplateEditorActivity.this,
                            AssistorServiceClass.class);
                    TemplateEditorActivity.this.stopService(stopIntent);

                    startActivity(intent);
                }
            }else if(resultCode == 2){
                TemplateEditorSingleton.getInstance().ass = "ass";
            }
        }else if(requestCode == 2){
            if(resultCode == 2){
                if(data != null){
                    if(data.getBooleanExtra("remove", false)){
                        removeDaySet();
                    }
                }
            }
        }else if(requestCode == 3){
            if(resultCode == 1){
                // inflate from running
                inflateFromRunning();
            }else if(resultCode == 2){
                // normal inflate
            }
        }
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


    boolean isBlack = false;

    CountDownTimer timer;

    private void flashAddDay(){
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(2000, 150) {
            @Override
            public void onTick(long l) {
                if(isBlack){
                    addDay.setBackgroundColor(Color.parseColor("#000000"));
                    isBlack = false;
                }else{
                    addDay.setBackgroundColor(Color.parseColor("#303030"));
                    isBlack = true;
                }
            }

            @Override
            public void onFinish() {
                addDay.setBackgroundColor(Color.parseColor("#000000"));
            }
        }.start();
    }

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
