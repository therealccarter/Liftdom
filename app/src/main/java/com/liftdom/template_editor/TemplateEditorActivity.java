package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.settings.SettingsListActivity;
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

import java.util.ArrayList;
import java.util.List;

public class TemplateEditorActivity extends AppCompatActivity
        implements DayOfWeekChildFrag.onDaySelectedListener,
        ExerciseLevelChildFrag.setToGoldCallback{

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    private FirebaseAuth.AuthStateListener mAuthListener;

    int fragIdCount = 0;

    String templateNameEdit;



    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // butterknife
    @BindView(R.id.addDay) Button addDay;
    @BindView(R.id.removeDay) Button removeDay;
    @BindView(R.id.saveButton) Button onSave;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.makePublicCheckbox) CheckBox makePublicCheckbox;
    @BindView(R.id.descriptionEditText) EditText templateDescriptionEdit;
    @BindView(R.id.title) TextView title;
    //@BindView(R.id.algoButton) Button algoButton;

    ArrayList<DayOfWeekChildFrag> dayOfWeekChildFragArrayList = new ArrayList<>();

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

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        Intent intent = new Intent(TemplateEditorActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
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
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Charts/Stats/Tools").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Premium Features").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(7)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(TemplateEditorActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(TemplateEditorActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 2);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(TemplateEditorActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 0);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(TemplateEditorActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(TemplateEditorActivity.this, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(TemplateEditorActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(TemplateEditorActivity.this, SettingsListActivity.class);
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
            if (getIntent().getExtras().getString("isEdit").equals("yes")){
                templateNameEdit = getIntent().getExtras().getString("templateName");

                // Check for active template
                DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child("active_template");

                activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String activeTemplateName = dataSnapshot.getValue(String.class);
                        if(activeTemplateName.equals(templateNameEdit)){
                            activeTemplateCheckbox.setChecked(true);
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

                        if(templateClass.getIsAlgorithm()){
                            if(templateClass.getIsAlgoApplyToAll()){
                                TemplateEditorSingleton.getInstance().isAlgoApplyToAll = true;
                                List<String> tempAlgoInfoList2 = new ArrayList<>();
                                tempAlgoInfoList2.addAll(templateClass.getAlgorithmInfo().get("0_key"));
                                EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.put("0_key", tempAlgoInfoList2);
                            }else{
                                EditTemplateAssemblerClass.getInstance().tempAlgoInfo.putAll(templateClass.getAlgorithmInfo());
                            }
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


            }
        });

        onSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {

                boolean hasEmptyDays = false;

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
                }

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

                                    Intent intent = new Intent(v.getContext(), SaveTemplateDialog.class);

                                    boolean checkBool = activeTemplateCheckbox.isChecked();
                                    //boolean algBool = EditTemplateAssemblerClass.getInstance().isApplyAlgo;
                                    boolean isPublic = makePublicCheckbox.isChecked();
                                    String descriptionString = templateDescriptionEdit.getText().toString();
                                    TemplateEditorSingleton.getInstance().mDescription = descriptionString;

                                    //ArrayList<ArrayList> algorithmMasterList = new ArrayList<>();


                                    if(getIntent().getExtras().getString("isEdit") != null) {
                                        if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                                            String templateName = getIntent().getExtras().getString("templateName");
                                            intent.putExtra("isEdit", "yes");
                                            intent.putExtra("templateName", templateName);
                                            intent.putExtra("isActiveTemplate", checkBool);
                                            //intent.putExtra("isAlgorithm", algBool);
                                            intent.putExtra("isPublic", isPublic);
                                            startActivity(intent);
                                        }
                                    }else{
                                        intent.putExtra("isEdit", "no");
                                        intent.putExtra("isActiveTemplate", checkBool);
                                        //intent.putExtra("isAlgorithm", algBool);
                                        intent.putExtra("isPublic", isPublic);
                                        intent.putExtra("description", descriptionString);
                                        startActivity(intent);
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
                    Intent intent = new Intent(v.getContext(), SaveTemplateDialog.class);

                    boolean checkBool = activeTemplateCheckbox.isChecked();
                    //boolean algBool = EditTemplateAssemblerClass.getInstance().isApplyAlgo;
                    boolean isPublic = makePublicCheckbox.isChecked();
                    String descriptionString = templateDescriptionEdit.getText().toString();
                    TemplateEditorSingleton.getInstance().mDescription = descriptionString;

                    //ArrayList<ArrayList> algorithmMasterList = new ArrayList<>();


                    if(getIntent().getExtras().getString("isEdit") != null) {
                        if(getIntent().getExtras().getString("isEdit").equals("yes")) {
                            String templateName = getIntent().getExtras().getString("templateName");
                            intent.putExtra("isEdit", "yes");
                            intent.putExtra("templateName", templateName);
                            intent.putExtra("isActiveTemplate", checkBool);
                            //intent.putExtra("isAlgorithm", algBool);
                            intent.putExtra("isPublic", isPublic);
                            startActivity(intent);
                        }
                    }else{
                        intent.putExtra("isEdit", "no");
                        intent.putExtra("isActiveTemplate", checkBool);
                        //intent.putExtra("isAlgorithm", algBool);
                        intent.putExtra("isPublic", isPublic);
                        intent.putExtra("description", descriptionString);
                        startActivity(intent);
                    }

                    EditTemplateAssemblerClass.getInstance().isOnSaveClick = true;
                }
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == 4)
        {
        }
    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        DatabaseReference isFirstTemplate = mRootRef.child("templates");
        isFirstTemplate.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Boolean isFirst = false;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String userId = dataSnapshot1.getKey();
                    if(userId.equals(uid)){
                        isFirst = true;
                    }
                }
                if(!isFirst){
                    activeTemplateCheckbox.setChecked(true);
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
                        //Intent intent = new Intent(TemplateEditorActivity.this, TemplateHousingActivity.class);
                        //startActivity(intent);

                        //TemplateEditorActivity.super.onBackPressed();
                        finish();
                    }
                })
                .setNegativeButton("Continue",new DialogInterface.OnClickListener() {
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

    String[] daysToArray(String daysUn){
        String[] days = daysUn.split("_");
        return days;
    }

}
