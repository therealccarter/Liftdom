package com.liftdom.knowledge_center.hall_of_fame;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.liftdom.charts_stats_tools.history_chart.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.PremiumFeaturesActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.settings.SettingsListActivity;
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
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

public class HallOfFameActivity extends AppCompatActivity {

    ViewPager pager;
    HoFPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Bodybuilding", "Powerlifting", "Strongman", "Other"};
    int Numboftabs = 4;

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hall_of_fame);

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
                        Intent intent = new Intent(HallOfFameActivity.this, CurrentUserProfile.class);
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
                                intent = new Intent(HallOfFameActivity.this, MainActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(HallOfFameActivity.this, TemplateHousingActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(HallOfFameActivity.this, WorkoutAssistorActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(HallOfFameActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(HallOfFameActivity.this, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(HallOfFameActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(HallOfFameActivity.this, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                HallOfFameActivity.this.startActivity(intent);
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

        FragmentManager fragmentManager = getSupportFragmentManager();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new HoFPagerAdapter(fragmentManager, Titles, Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager2);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs2);
        tabs.setDistributeEvenly(false); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in
        // Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);
    }
}
