package com.liftdom.workout_assistor;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.SignInActivity;
import com.liftdom.template_editor.TemplateSavedActivity;
import com.liftdom.template_housing.TemplateHousingActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AssistorSavedActivity extends AppCompatActivity {

    @BindView(R.id.goBackHome) Button goHome;


    private static final String FIREBASE_URL = "https://liftdom-27d9d.firebaseio.com/";

    private static final String TAG = "EmailPassword";
    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    // declare_auth
    private FirebaseUser mFirebaseUser;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistor_saved);

        ButterKnife.bind(this);


        // [START AUTH AND NAV-DRAWER BOILERPLATE]

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
                    startActivity(new Intent(AssistorSavedActivity.this, SignInActivity.class));
                }

            }
        };
        // [END auth_state_listener]


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
                                intent = new Intent(AssistorSavedActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(AssistorSavedActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(AssistorSavedActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (intent != null) {
                                AssistorSavedActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();

        // Later
        header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(this, R.drawable.usertest))
                        .withName
                                ("Username").withEmail(email),
                0);

        // [END AUTH AND NAV-DRAWER BOILERPLATE] =================================================================


        DateFormat dateFormat = new SimpleDateFormat("yyyy:MM:dd");
        dateFormat.setTimeZone(TimeZone.getTimeZone("gmt"));
        String gmtTime = dateFormat.format(new Date());

        DatabaseReference workoutHistoryRef = mRootRef.child("users").child(uid).child("workout_history");

        DatabaseReference specificDate = workoutHistoryRef.child(gmtTime);

        DatabaseReference journalRef = specificDate.child("private_journal");

        String privateJournalString = WorkoutAssistorAssemblerClass.getInstance().privateJournal;

        ArrayList<String> assistorArrayList = WorkoutAssistorAssemblerClass.getInstance().DoWAL1;

        List<String> list = new ArrayList<>();

        if(savedInstanceState == null) {
            for (String item : assistorArrayList) {
                list.add(item);
            }

            specificDate.setValue(list);

            if (privateJournalString != null) {
                journalRef.setValue(privateJournalString);
            }
        }


        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });


    }
}
