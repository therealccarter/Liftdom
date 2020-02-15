package com.liftdom.liftdom;// Created: 9/10/2016


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.annotation.VisibleForTesting;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.feedback.FeedbackActivity;
import com.liftdom.liftdom.notifications_bell.NotificationsActivity;
import com.liftdom.misc_activities.SettingsListActivity;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.single_user_profile.UserProfileFullActivity;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class BaseActivity extends AppCompatActivity {


    private ViewPager mViewPager;
    //public AccountHeader header;
    public Drawer drawer;
    public int selectionInt = 0;

    public void setUpNavDrawer(final Context context, Toolbar toolbar){

        if(drawer == null){


        // create the drawer
        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHeader(R.layout.custom_account_header)
                //.withAccountHeader(header)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName("Home").withIdentifier(1),
                        //new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Workout Programming").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(3),
                        new PrimaryDrawerItem().withName("Forum").withIdentifier(4),
                        new PrimaryDrawerItem().withName("Chat").withIdentifier(5),
                        new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Knowledge Center").withIdentifier(6),
                        new PrimaryDrawerItem().withName("Charts/Stats/Tools").withIdentifier(7),
                        //new DividerDrawerItem(),
                        new PrimaryDrawerItem().withName("Feedback/Bug Reports").withIdentifier(8),
                        new PrimaryDrawerItem().withName("App Settings").withIdentifier(9)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // Handle clicks

                        if (drawerItem != null) {
                            Intent intent = null;
                            if (drawerItem.getIdentifier() == 1) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID",  0);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 2);
                                startActivity(intent);
                                //drawer.setSelection(position);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 3);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(context, MainActivity.class);
                                intent.putExtra("fragID", 4);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(context, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(context, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 8) {
                                intent = new Intent(context, FeedbackActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 9) {
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


        final String uid2 = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid2);

        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                    MainActivitySingleton.getInstance().userModelClass = userModelClass;
                    if(userModelClass.isIsImperial()){
                        MainActivitySingleton.getInstance().isImperial = true;
                    }

                    StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                            uid2 + "/profilePic.png");

                    profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            ImageView profilePicView = (ImageView) drawer.getHeader().findViewById(R.id.profilePicImageView);
                            Glide.with(getApplicationContext()).load(uri).crossFade().into(profilePicView);
                            profilePicView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), UserProfileFullActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            ImageView profilePicView = (ImageView) drawer.getHeader().findViewById(R.id.profilePicImageView);
                            profilePicView.setImageResource(R.drawable.usertest);
                            profilePicView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), UserProfileFullActivity.class);
                                    startActivity(intent);
                                }
                            });
                        }
                    });

                    userRef.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                            TextView userNameView = (TextView) drawer.getHeader().findViewById(R.id.usernameTextView);
                            TextView powerLevelView = (TextView) drawer.getHeader().findViewById(R.id.powerLevelTextView);
                            TextView streakTextView = (TextView) drawer.getHeader().findViewById(R.id.currentStreakTextView);
                            TextView notificationCountView = (TextView) drawer.getHeader().findViewById(R.id
                                    .notificationsTextView);
                            LinearLayout notificationLL = (LinearLayout) drawer.getHeader().findViewById(R.id
                                    .notificationsLL);

                            if(userModelClass.getNotificationCount() != null
                                    && !userModelClass.getNotificationCount().isEmpty()
                                    && !userModelClass.getNotificationCount().equals("")){
                                if(!userModelClass.getNotificationCount().equals("0")){
                                    notificationCountView.setText(userModelClass.getNotificationCount());
                                }else{
                                    notificationCountView.setText("");
                                }
                            }

                            notificationLL.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), NotificationsActivity.class);
                                    startActivity(intent);
                                }
                            });

                            userNameView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getApplicationContext(), UserProfileFullActivity.class);
                                    startActivity(intent);
                                }
                            });

                            userNameView.setText(userModelClass.getUserName());
                            powerLevelView.setText(userModelClass.getPowerLevel());
                            streakTextView.setText(userModelClass.getCurrentStreak());


                            SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPref.edit();
                            editor.putString("userName", userModelClass.getUserName());
                            editor.commit();


                            String userName = sharedPref.getString("userName", "loading...");

                            Log.i("prefs", userName);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                }else{
                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

            Log.i("drawer", "drawer is null (setting up) (BaseActivity)");
        }else{
            Log.i("drawer", "drawer is not null (BaseActivity)");
        }

    }

    public void setUpBottomNav(){

    }

    public void setBackArrow(){
        try{
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }catch (NullPointerException e){

        }
    }

    public void setHamburger(){
        try{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            drawer.getActionBarDrawerToggle().setDrawerIndicatorEnabled(true);
        }catch (NullPointerException e){

        }
    }

    public String getUsernameBase(){
        TextView userNameView = (TextView) drawer.getHeader().findViewById(R.id.usernameTextView);
        return userNameView.getText().toString();
    }

    public boolean isDrawerNull(){
        if(getDrawer() == null){
            return true;
        }else{
            return false;
        }
    }

    public Drawer getDrawer(){
        return drawer;
    }

    public void setNavDrawerSelection(int i){
        if(drawer != null){
            drawer.setSelection(i, false);
        }
    }

    public ViewPager getViewPager() {
        if(mViewPager == null){
            //mViewPager = (ViewPager) findViewById(R.id.ViewPager01);
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
    public void onBackPressed(){
        if(drawer != null){
            if(drawer.isDrawerOpen()){
                drawer.closeDrawer();
            }else{
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}