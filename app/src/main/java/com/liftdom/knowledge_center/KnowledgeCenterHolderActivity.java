package com.liftdom.knowledge_center;

import android.content.Intent;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.liftdom.charts.ChartsAndStatsActivity;
import com.liftdom.knowledge_center.exercise_library.ExercisesMainFrag;
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

public class KnowledgeCenterHolderActivity extends AppCompatActivity
        implements ArticlesMainFrag.headerChangeToFrag,
        KnowledgeCenterMainFrag.headerChangeToFrag,
        ExercisesMainFrag.headerChangeToFrag,
        HallOfFameMainFrag.headerChangeToFrag
{

    @BindView(R.id.knowledgeCenterHolder) LinearLayout holderView;
    @BindView(R.id.toolbar1) Toolbar toolbar;
    @BindView(R.id.collapsingToolbar1) CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.toolbarImage1) ImageView toolbarImage;

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_center_holder);

        ButterKnife.bind(this);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if(savedInstanceState == null){
            // get an instance of FragmentTransaction from your Activity
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //add a fragment
            KnowledgeCenterMainFrag knowledgeCenterMainFrag = new KnowledgeCenterMainFrag();
            fragmentTransaction.add(R.id.knowledgeCenterHolder, knowledgeCenterMainFrag);
            fragmentTransaction.commit();
        }

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        //Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
//
        //mainActivityTitle.setTypeface(lobster);


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
                        Intent intent = new Intent(KnowledgeCenterHolderActivity.this, CurrentUserProfile.class);
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
                        new PrimaryDrawerItem().withName("Charts & Stats").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(5),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("Tools").withIdentifier(6),
                        new SecondaryDrawerItem().withName("Settings").withIdentifier(7)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(KnowledgeCenterHolderActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(KnowledgeCenterHolderActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(KnowledgeCenterHolderActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(KnowledgeCenterHolderActivity.this, ChartsAndStatsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(KnowledgeCenterHolderActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (intent != null) {
                                KnowledgeCenterHolderActivity.this.startActivity(intent);
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
    }

    public void changeHeader(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsingToolbar1);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarImage1);

        collapsingToolbarLayout.setTitle(title);

    }

}