package com.liftdom.settings;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.charts.ChartsAndStatsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.LoginActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.PremiumFeaturesActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.tools.ToolsMainActivity;
import com.liftdom.user_profile.CurrentUserProfile;
import com.liftdom.workout_assistor.WorkoutAssistorActivity;
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

public class SettingsListActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.sign_out_button) Button signOutButton;
    @BindView(R.id.settingsTitle) TextView settingsTitle;
    @BindView(R.id.lbsWeight) RadioButton poundsWeight;
    @BindView(R.id.kgsWeight) RadioButton kiloWeight;
    @BindView(R.id.lbsBodyWeight) RadioButton poundsBodyWeight;
    @BindView(R.id.kgsBodyWeight) RadioButton kiloBodyWeight;
    @BindView(R.id.footInchHeight) RadioButton footInchesHeight;
    @BindView(R.id.centimetersHeight) RadioButton centiHeight;
    @BindView(R.id.checkBoxRound) CheckBox checkBoxRound;
    @BindView(R.id.saveButton) Button saveButton;

    ArrayList<String> settingsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        settingsTitle.setTypeface(lobster);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
//
        //mainActivityTitle.setTypeface(lobster);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        return false;
                    }
                }).withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(SettingsListActivity.this, CurrentUserProfile.class);
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
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Charts & Stats").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Premium Features").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Tools").withIdentifier(7),
                        new PrimaryDrawerItem().withName("Settings").withIdentifier(8)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(SettingsListActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(SettingsListActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(SettingsListActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(SettingsListActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(SettingsListActivity.this, ChartsAndStatsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(SettingsListActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(SettingsListActivity.this, ToolsMainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 8) {
                                intent = new Intent(SettingsListActivity.this, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                SettingsListActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(getApplicationContext(), R
                        .drawable.usertest))
                        .withName
                                (mFirebaseUser.getDisplayName()).withEmail
                                (mFirebaseUser.getEmail()),
                0);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(poundsWeight.isChecked()){
                    firebaseSetter("weightUnit", "pounds");
                }else if(kiloWeight.isChecked()){
                    firebaseSetter("weightUnit", "kilos");
                }
                if(poundsBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "pounds");
                }else if(kiloBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "kilos");
                }
                if(footInchesHeight.isChecked()){
                    firebaseSetter("heightUnit", "footInches");
                }else if(centiHeight.isChecked()){
                    firebaseSetter("heightUnit", "centimeters");
                }
                if(checkBoxRound.isChecked()){
                    firebaseSetter("roundWeight", "yes");
                }else{
                    firebaseSetter("roundWeight", "no");
                }
            }
        });
    }

    void firebaseSetter(String key, String value){
        DatabaseReference dataRef = mRootRef.child("users").child(uid).child(key);
        dataRef.setValue(value);
    }
}
