package com.liftdom.knowledge_center;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
import com.liftdom.liftdom.R;
import com.liftdom.misc_activities.SettingsListActivity;
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

public class KnowledgeCenterHolderActivity extends BaseActivity
        implements ArticlesMainFrag.headerChangeToFrag,
        KnowledgeCenterMainFrag.headerChangeToFrag,
        DisciplinesMainFrag.headerChangeToFrag,
        DisciplinesDetailFrag.headerChangeToFrag
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

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        collapsingToolbar.setCollapsedTitleTextColor(Color.parseColor("#D1B91D"));
        collapsingToolbar.setCollapsedTitleTypeface(lobster);

        setUpNavDrawer(KnowledgeCenterHolderActivity.this, toolbar);
        setNavDrawerSelection(4);

    }

    public void changeHeader(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsingToolbar1);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarImage1);

        collapsingToolbarLayout.setTitle(title);

    }

}