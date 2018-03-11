package com.liftdom.knowledge_center;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.user_profile.single_user_profile.ProfilePagerAdapter;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
    @BindView(R.id.comingSoonView) TextView comingSoonView;
    //@BindView(R.id.tabs) SlidingTabLayout tabsView;
    //@BindView(R.id.pager) ViewPager pager;
    @BindView(R.id.nestedScrollView) NestedScrollView nestedScrollView;

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

        nestedScrollView.setFillViewport(true);

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

        comingSoonView.setTypeface(lobster);

        setUpNavDrawer(KnowledgeCenterHolderActivity.this, toolbar);
        setNavDrawerSelection(4);

        Appodeal.show(this, Appodeal.BANNER_BOTTOM);

        //setUpSlidingLayout();

    }

    public void changeHeader(String title){
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsingToolbar1);
        ImageView imageView = (ImageView) findViewById(R.id.toolbarImage1);

        collapsingToolbarLayout.setTitle(title);

    }
    private void BLAM(){
        DatabaseReference feedRef = FirebaseDatabase.getInstance().getReference().child("feed");

        feedRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    i++;
                    final String userId = dataSnapshot1.getKey();
                    int j = 0;
                    Map<String, CompletedWorkoutModelClass> map = new LinkedHashMap<>();
                    for(DataSnapshot dataSnapshot2 : dataSnapshot1.getChildren()){
                        j++;
                        String key = dataSnapshot2.getKey();
                        CompletedWorkoutModelClass value = dataSnapshot2.getValue(CompletedWorkoutModelClass.class);
                        if(userId.equals(value.getUserId())){
                            map.put(key, value);
                        }
                        if(j == dataSnapshot1.getChildrenCount()){
                            DatabaseReference selfFeedRef = FirebaseDatabase.getInstance().getReference().child
                                    ("selfFeed").child(userId);
                            selfFeedRef.setValue(map);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}