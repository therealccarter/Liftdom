package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.user_profile.CurrentUserProfile;
import com.liftdom.workout_assistor.ExerciseNameFrag;
import com.liftdom.workout_assistor.RepsWeightFrag;
import com.liftdom.workout_assistor.WorkoutAssistorActivity;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;

public class TemplateEditorActivity extends AppCompatActivity implements DayOfWeekChildFrag.onDaySelectedListener{

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;


    private FirebaseAuth.AuthStateListener mAuthListener;

    int fragIdCount = 0;


    String activeTemplateName = null;
    String selectedTemplateDayValue = null;
    String activeTemplateToday = null;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // butterknife
    @BindView(R.id.addDay) Button addDay;
    @BindView(R.id.removeDay) Button removeDay;
    @BindView(R.id.saveButton) Button onSave;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.applyAlgorithmCheckbox) CheckBox applyAlgorithmCheckbox;
    @BindView(R.id.algorithmLayout) LinearLayout algorithmLayout;
    @BindView(R.id.setsWeeksEditText) EditText setsWeeksEditText;
    @BindView(R.id.setsIncreaseEditText) EditText setsIncreasedEditText;
    @BindView(R.id.repsWeeksEditText) EditText repsWeeksEditText;
    @BindView(R.id.repsIncreaseEditText) EditText repsIncreasedEditText;
    @BindView(R.id.weightsWeeksEditText) EditText weightsWeeksEditText;
    @BindView(R.id.weightsIncreaseEditText) EditText  weightsIncreasedEditText;

    ArrayList<DayOfWeekChildFrag> dayOfWeekChildFragArrayList = new ArrayList<>();

    public void daySelectedFromFrag(String doW, String tag){
        for(int i = 0; i < fragIdCount; i++){
            if(!dayOfWeekChildFragArrayList.get(i).getTag().equals(tag)){
                dayOfWeekChildFragArrayList.get(i).daySelectedToFrag(doW);
            }
        }
    }

    public void dayUnselectedFromFrag(String doW, String tag){
        for(int i = 0; i < fragIdCount; i++){
            if(!dayOfWeekChildFragArrayList.get(i).getTag().equals(tag)){
                dayOfWeekChildFragArrayList.get(i).dayUnselectedToFrag(doW);
            }
        }
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_editor);

        //TODO: Edit template adds setScheme from other exercise...

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


        ButterKnife.bind(this);

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


        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Template Editor");

        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //Handle Profile changes
                        return false;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(TemplateEditorActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }

                    @Override
                    public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                        return false;
                    }
                })
                .build();

        // create the drawer
        Drawer drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(1),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("test1"),
                        new SecondaryDrawerItem().withName("test2")
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(TemplateEditorActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(TemplateEditorActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(TemplateEditorActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (intent != null) {
                                TemplateEditorActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        // Later
        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(this, R.drawable.usertest))
                        .withName
                                (mFirebaseUser.getDisplayName()).withEmail(email),
                0);

        /**
         * How would we implement this new idea...
         * From text message:
         *  Simply remove or add fragment depending on the action and use the incrementor,
         *  if 1, doW1, etc.
         */

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================

        if (getIntent().getExtras().getString("isEdit") != null) {
            if (getIntent().getExtras().getString("isEdit").equals("yes")) {

                EditTemplateAssemblerClass.getInstance().clearAllLists();

                final String templateName = getIntent().getExtras().getString("templateName");

                /**
                 * Alright, this is what we have to do:
                 * 1. Get a path to the selected template
                 * 2. Get a path to each DayGroup
                 * 3. For each DayGroup, create a DoWLevel frag, passing in its selected days and each exercise/value
                 */

                // 1. Get a path to the selected template
                // "realll"
                final DatabaseReference selectedTemplateDataRef = mRootRef.child("templates").child(uid).child(templateName);

                DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String activeTemplateName = dataSnapshot.getValue(String.class);
                        if(activeTemplateName.equals(templateName)){
                            activeTemplateCheckbox.setChecked(true);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                // find the matching day
                selectedTemplateDataRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot templateSnapshot : dataSnapshot.getChildren()) {
                            // Here we're looking at each DoW entry ("Monday_Thursday", "Tuesday_Saturday", etc)
                            // Thursday_
                            selectedTemplateDayValue = templateSnapshot.getKey();

                            String[] dayStringArray = selectedTemplateDayValue.split("_");

                            ++fragIdCount;
                            FragmentManager fragmentManager = getFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            String fragString = Integer.toString(fragIdCount);

                            if(fragIdCount == 1){
                                doW1.isEdit = true;
                                doW1.daysArray = dayStringArray.clone();
                                doW1.selectedDaysReference = selectedTemplateDayValue;
                                doW1.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW1, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 2){

                                doW2.isEdit = true;
                                doW2.daysArray = dayStringArray.clone();
                                doW2.selectedDaysReference = selectedTemplateDayValue;
                                doW2.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW2, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 3){

                                doW3.isEdit = true;
                                doW3.daysArray = dayStringArray.clone();
                                doW3.selectedDaysReference = selectedTemplateDayValue;
                                doW3.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW3, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 4){

                                doW4.isEdit = true;
                                doW4.daysArray = dayStringArray.clone();
                                doW4.selectedDaysReference = selectedTemplateDayValue;
                                doW4.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW4, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 5){

                                doW5.isEdit = true;
                                doW5.daysArray = dayStringArray.clone();
                                doW5.selectedDaysReference = selectedTemplateDayValue;
                                doW5.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW5, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 6){

                                doW6.isEdit = true;
                                doW6.daysArray = dayStringArray.clone();
                                doW6.selectedDaysReference = selectedTemplateDayValue;
                                doW6.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW6, fragString);
                                fragmentTransaction.commitAllowingStateLoss();
                            }else if(fragIdCount == 7){

                                doW7.isEdit = true;
                                doW7.daysArray = dayStringArray.clone();
                                doW7.selectedDaysReference = selectedTemplateDayValue;
                                doW7.templateName = templateName;
                                fragmentTransaction.add(R.id.templateFragmentLayout, doW7, fragString);
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




        // use increment to keep track of what the current doW frag is

        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);

                /**
                 * Only problem we're having now is that when a new day-set is added,
                 *  the new day-set is unaware of current/existing selected days
                 *
                 *  So what we need to do is call a check out to see what days are selected
                 *  in existing day-set fragments.
                 */

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


                CharSequence toastText = "(+) Day-set added";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                toast.show();

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




                if (fragIdCount != 0) {
                    //if(fragIdCount == 1){
                    //    fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                    //
                    if(fragIdCount == 2){
                        ArrayList<String> removeList = new ArrayList<>();
                        removeList = doW2.getSelectedDays();
                        for(String days : removeList){
                            dayUnselectedFromFrag(days, fragString);
                            doW2.dayUnselectedToFrag(days);
                            doW2.setToNull();
                        }
                        fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
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
                        --fragIdCount;
                    }
                }

                if(fragIdCount != 1) {
                    CharSequence toastText = "(-) Day-set removed";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                    toast.show();
                }
            }
        });

        applyAlgorithmCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    algorithmLayout.setVisibility(View.VISIBLE);
                    applyAlgorithmCheckbox.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }else{
                    algorithmLayout.setVisibility(View.GONE);
                    applyAlgorithmCheckbox.setBackgroundColor(Color.parseColor("#00000000"));
                }
            }
        });

        onSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SaveTemplateDialog.class);

                boolean checkBool = activeTemplateCheckbox.isChecked();
                boolean algBool = applyAlgorithmCheckbox.isChecked();

                ArrayList<ArrayList> algorithmMasterList = new ArrayList<>();

                if(algBool){
                    ArrayList<Integer> setsAL = new ArrayList<>();
                    setsAL.add(Integer.parseInt(setsWeeksEditText.getText().toString()));
                    setsAL.add(Integer.parseInt(setsIncreasedEditText.getText().toString()));

                    ArrayList<Integer> repsAL = new ArrayList<>();
                    repsAL.add(Integer.parseInt(repsWeeksEditText.getText().toString()));
                    repsAL.add(Integer.parseInt(repsIncreasedEditText.getText().toString()));

                    ArrayList<Integer> weightsAL = new ArrayList<>();
                    weightsAL.add(Integer.parseInt(weightsWeeksEditText.getText().toString()));
                    weightsAL.add(Integer.parseInt(weightsIncreasedEditText.getText().toString()));

                    algorithmMasterList.add(setsAL);
                    algorithmMasterList.add(repsAL);
                    algorithmMasterList.add(weightsAL);
                }

                if(getIntent().getExtras().getString("isEdit") != null) {
                    if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                        String templateName = getIntent().getExtras().getString("templateName");
                        intent.putExtra("isEdit", "yes");
                        intent.putExtra("templateName", templateName);
                        intent.putExtra("isActiveTemplate", checkBool);
                        intent.putExtra("isAlgorithm", algBool);
                        EditTemplateAssemblerClass.getInstance().algorithmMasterList = algorithmMasterList;
                        startActivity(intent);
                    }
                }else{
                    intent.putExtra("isEdit", "no");
                    intent.putExtra("isActiveTemplate", checkBool);
                    intent.putExtra("isAlgorithm", algBool);
                    EditTemplateAssemblerClass.getInstance().algorithmMasterList = algorithmMasterList;
                    startActivity(intent);
                }

                EditTemplateAssemblerClass.getInstance().isOnSaveClick = true;
            }
        });

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
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

    //@Override
    //public void onPause(){
    //    super.onPause();
    //
    //}

}
