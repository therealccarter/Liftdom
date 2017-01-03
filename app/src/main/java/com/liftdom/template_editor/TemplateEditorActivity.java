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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_editor);

        //TODO: Edit template adds setScheme from other exercise...


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

                            DayOfWeekChildFrag dayOfWeekChildFrag = new DayOfWeekChildFrag();
                            dayOfWeekChildFrag.isEdit = true;
                            dayOfWeekChildFrag.daysArray = dayStringArray.clone();
                            dayOfWeekChildFrag.selectedDaysReference = selectedTemplateDayValue;
                            dayOfWeekChildFrag.templateName = templateName;

                            fragmentTransaction.add(R.id.templateFragmentLayout, dayOfWeekChildFrag, fragString);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }



        addDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);
                DayOfWeekChildFrag frag2 = new DayOfWeekChildFrag();
                fragmentTransaction.add(R.id.templateFragmentLayout, frag2, fragString);
                fragmentTransaction.commit();

                CharSequence toastText = "(+) Day-set added";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                toast.show();
            }
        });

        removeDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                String fragString = Integer.toString(fragIdCount);
                if (fragIdCount != 0) {
                    fragmentTransaction.remove(fragmentManager.findFragmentByTag(fragString)).commit();
                    --fragIdCount;
                }

                CharSequence toastText = "(-) Day-set removed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                toast.show();
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


        //@BindView(R.id.setsWeeksEditText) EditText setsWeeksEditText;
        //@BindView(R.id.setsIncreaseEditText) EditText setsIncreasedEditText;
        //@BindView(R.id.repsWeeksEditText) EditText repsWeeksEditText;
        //@BindView(R.id.repsIncreaseEditText) EditText repsIncreasedEditText;
        //@BindView(R.id.weightsWeeksEditText) EditText weightsWeeksEditText;
        //@BindView(R.id.weightsIncreaseEditText) EditText  weightsIncreasedEditText;


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

    public void onDaySelected(String doW, View v, long id){

    }
}
