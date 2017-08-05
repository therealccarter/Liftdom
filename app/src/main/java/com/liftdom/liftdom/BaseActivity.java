package com.liftdom.liftdom;// Created: 9/10/2016


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.auth.api.signin.SignInAccount;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
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
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class BaseActivity extends AppCompatActivity {

    private BottomNavigation mBottomNavigation;
    private ViewPager mViewPager;
    public AccountHeader header;
    public Drawer drawer;
    public int selectionInt = 0;

    public void setUpNavDrawer(final Context context, Toolbar toolbar){

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header_pattern)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        Intent intent = new Intent(context, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }
                })
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(context, CurrentUserProfile.class);
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
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(1),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Workout Programming").withIdentifier(3),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Charts/Stats/Tools").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Premium Features").withIdentifier(6),
                        new PrimaryDrawerItem().withName("App Settings").withIdentifier(7)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 2);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 0);
                                startActivity(intent);
                                drawer.setSelection(position);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(context, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(context, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(context, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(context, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                context.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();


        String uid2 = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(FirebaseAuth.getInstance().getCurrentUser().getUid() == null){
            //Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            //startActivity(intent);
        }

        final StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                uid2 + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");

                Glide.with(getApplicationContext()).load(uri).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);

                        ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                                //.withIcon(R.drawable.usertest)
                                .withName(sharedPref.getString("userName", "loading..."))
                                .withEmail(sharedPref.getString("email", "loading..."));

                        profileDrawerItem.withIcon(resource);

                        if(header.getProfiles().size() == 0){
                            header.addProfile(profileDrawerItem, 0);
                        }
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);

                ProfileDrawerItem profileDrawerItem = new ProfileDrawerItem()
                        //.withIcon(R.drawable.usertest)
                        .withName(sharedPref.getString("userName", "loading..."))
                        .withEmail(sharedPref.getString("email", "loading..."));

                profileDrawerItem.withIcon(R.drawable.usertest);

                header.addProfile(profileDrawerItem, 0);
            }
        });
    }

    public Drawer getDrawer(){
        return drawer;
    }

    public void setNavDrawerSelection(int i){
        if(drawer != null){
            drawer.setSelection(i, false);
        }
    }

    public BottomNavigation getBottomNavigation() {
        if (mBottomNavigation == null) {
            mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        }
        return mBottomNavigation;
    }

    public ViewPager getViewPager() {
        if(mViewPager == null){
            mViewPager = (ViewPager) findViewById(R.id.ViewPager01);
        }
        return mViewPager;
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}