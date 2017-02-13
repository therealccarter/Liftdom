package com.liftdom.workout_programs.Smolov;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateHousingActivity;
import com.liftdom.user_profile.CurrentUserProfile;
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
import org.joda.time.LocalDate;

public class SmolovSavedActivity extends AppCompatActivity {


    @BindView(R.id.goBackHome) Button goHome;
    @BindView(R.id.goBackToTemplates) Button goToTemplates;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String OneRepMax;
    String ExName;

    void setOneRepMax(String oneRepMax){
        OneRepMax = oneRepMax;
    }

    void setExName(String exName){
        ExName = exName;
    }

    Boolean isActive;


    //public SmolovSavedActivity(String oneRepMax, String exName){
    //    setOneRepMax(oneRepMax);
    //    setExName(exName);
    //}

    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_saved);

        ButterKnife.bind(this);

        // [START AUTH AND NAV-DRAWER BOILERPLATE] =================================================================

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Saved Templates");

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
                        Intent intent = new Intent(SmolovSavedActivity.this, CurrentUserProfile.class);
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
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Tools").withIdentifier(4),
                        new SecondaryDrawerItem().withName("Exercise Academy (Info)").withIdentifier(5)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(SmolovSavedActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(SmolovSavedActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(SmolovSavedActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (intent != null) {
                                SmolovSavedActivity.this.startActivity(intent);
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

        setExName(getIntent().getStringExtra("exName"));
        setOneRepMax(getIntent().getStringExtra("1rm"));
        isActive = getIntent().getBooleanExtra("isActive", false);



        final DatabaseReference templateRef = mRootRef.child("templates").child(uid);

        final DatabaseReference smolovRef = mRootRef.child("templates").child(uid).child("Smolov");

        smolovRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                String templateName = dataSnapshot1.getKey();

                                if(templateName.length() >= 6){
                                    String firstSix = templateName.substring(0,6);
                                    if(firstSix.equals("Smolov")){
                                        if(templateName.length() > 6){
                                            String numbers = templateName.substring(6, templateName.length());
                                            int number = Integer.parseInt(numbers);
                                            number += 1;
                                            String newNumber = Integer.toString(number);
                                            final String newName = "Smolov" + newNumber;

                                            final DatabaseReference smolovRef1 = mRootRef.child("templates").child(uid).child
                                                    (newName);
                                            smolovRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    smolovRef1.child("1rm").setValue(OneRepMax);
                                                    smolovRef1.child("exName").setValue(ExName);
                                                    smolovRef1.child("timeStamp").setValue(LocalDate.now().toString());
                                                    smolovRef1.child("id").setValue("Smolov");

                                                    if(isActive){
                                                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                                                ("active_template");
                                                        activeTemplateRef.setValue(newName);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }else{

                                            final DatabaseReference smolovRef2 = mRootRef.child("templates").child(uid).child
                                                    ("Smolov2");
                                            smolovRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    smolovRef2.child("1rm").setValue(OneRepMax);
                                                    smolovRef2.child("exName").setValue(ExName);
                                                    smolovRef2.child("timeStamp").setValue(LocalDate.now().toString());
                                                    smolovRef2.child("id").setValue("Smolov");

                                                    if(isActive){
                                                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                                                ("active_template");
                                                        activeTemplateRef.setValue("Smolov2");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    smolovRef.child("1rm").setValue(OneRepMax);
                    smolovRef.child("exName").setValue(ExName);
                    smolovRef.child("timeStamp").setValue(LocalDate.now().toString());
                    smolovRef.child("id").setValue("Smolov");

                    if(isActive){
                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                ("active_template");
                        activeTemplateRef.setValue("Smolov");
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                Intent intent = new Intent(v.getContext(), TemplateHousingActivity.class);
                startActivity(intent);
            }
        });

    }
}
