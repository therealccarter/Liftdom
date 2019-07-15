package com.liftdom.user_profile.your_profile;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import androidx.annotation.NonNull;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutModelClass;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.CompletedWorkoutViewHolder;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
import com.liftdom.misc_activities.SettingsListActivity;
import com.liftdom.user_profile.UserModelClass;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.wang.avi.AVLoadingIndicatorView;

public class CurrentUserProfile extends BaseActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String email = "error";
    String username = "Username";

    private String uid;
    private DatabaseReference rootRef;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter firebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView recyclerView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noPostsView) TextView noPostsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_current_user_profile);

        ButterKnife.bind(this);

        HideKey.initialize(CurrentUserProfile.this);

        /**
         * What we're going to do here.
         * Have one fragment for both your profile and other people's.
         * Have a simple check for if the desired profile is yours or not, and have a method for setting up each case.
         * We could then either use that fragment in a full size holder activity or a floating activity.
         */

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
                    email = user.getEmail();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(CurrentUserProfile.this, SignInActivity.class));
                }
            }
        };
        // [END auth_state_listener]

        FirebaseUser user = mAuth.getCurrentUser();
        String email = user.getEmail();

        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "loading...");

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setTitle(userName);

        setUpNavDrawer(CurrentUserProfile.this, toolbar);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id
                .collapsingToolbar1);
        collapsingToolbarLayout.setTitle(userName);
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));

        // BEGIN recycler view setup

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        rootRef = FirebaseDatabase.getInstance().getReference();

        final DatabaseReference socialRef = rootRef.child("feed").child(uid);
        socialRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    DatabaseReference userRef = rootRef.child("user").child(uid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                            if(userModelClass.isIsImperial()){
                                loadingView.setVisibility(View.GONE);
                                noPostsView.setVisibility(View.GONE);
                                //setUpFirebaseAdapter(socialRef, true);
                            }else{
                                loadingView.setVisibility(View.GONE);
                                noPostsView.setVisibility(View.GONE);
                                //setUpFirebaseAdapter(socialRef, false);
                            }

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }else{
                    loadingView.setVisibility(View.GONE);
                    noPostsView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference, final boolean isImperial){

        linearLayoutManager = new LinearLayoutManager(CurrentUserProfile.this);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(10);

        recyclerView.setAdapter(firebaseAdapter);
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
        if(firebaseAdapter != null){
            firebaseAdapter.stopListening();
        }
    }
    // [END on_stop_remove_listener]

}