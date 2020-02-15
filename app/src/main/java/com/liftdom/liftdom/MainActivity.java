package com.liftdom.liftdom;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.appodeal.ads.Appodeal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.chat.ChatMainFrag;
import com.liftdom.liftdom.forum.ForumMainFrag3;
import com.liftdom.liftdom.main_social_feed.feed_slider.FeedHolderFrag;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchFrag;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.template_housing.*;
import com.liftdom.template_housing.public_programs.PublicTemplateChooserFrag;
import com.liftdom.workout_assistor.AssistorHolderFrag;
import com.liftdom.workout_assistor.WorkoutAssistorFrag;
import com.search.material.library.MaterialSearchView;
//import io.paperdb.Paper;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        TemplateMenuFrag.headerChangeFromFrag,
        ChatMainFrag.headerChangeFromFrag,
        ForumMainFrag3.headerChangeFromFrag,
        WorkoutAssistorFrag.headerChangeFromFrag,
        FeedHolderFrag.headerChangeFromFrag,
        SavedProgramsHolderFrag.headerChangeFromFrag,
        PublicTemplateChooserFrag.headerChangeFromFrag,
        PremadeTemplatesFrag.headerChangeFromFrag,
        FeedHolderFrag.bottomNavChanger,
        ForumMainFrag3.bottomNavChanger,
        TemplateMenuFrag.bottomNavChanger,
        SavedProgramsHolderFrag.bottomNavChanger,
        SelectedTemplateFrag.bottomNavChanger,
        WorkoutAssistorFrag.bottomNavChanger,
        AssistorHolderFrag.scrollToBottomInterface
        {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private MaterialSearchView searchView;
    private BottomNavigation bottomNavigation;
    private ArrayList<String> typeAheadData;
    private ScrollView scrollView;

    Toolbar toolbar;


    DatabaseReference mRootRef;
    String uid;

    // butterknife
    @BindView(R.id.title) TextView title;
    @BindView(R.id.appBar) AppBarLayout appBarLayout;
    @BindView(R.id.newWorkoutButton) Button newWorkoutButton;

    public void changeHeaderTitle(String title){
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
        if(title.equals("Workout Programming")){
            titleView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
        }
    }

    public String getUsername(){
        return getUsernameBase();
    }

    public void scrollToBottom(){
        scrollView.scrollTo(0, scrollView.getBottom());
    }

    private void paperTest(){
        DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child(
                "templates").child(uid).child("pro");
        templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                TemplateModelClass pro = dataSnapshot.getValue(TemplateModelClass.class);
                //Paper.book().write("pro", pro);
                //TemplateModelClass proOffline = Paper.book().read("pro");
                //Log.i("asd", proOffline.getDateCreated());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        // set the key account values

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //Paper.init(getApplicationContext());

        //appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
        //    @Override
        //    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        //        if(verticalOffset == 0){
        //            Log.i("appBar", "0");
        //            Appodeal.show(MainActivity.this, Appodeal.BANNER_VIEW);
        //        }else{
        //            Log.i("appBar", "not 0");
        //            Appodeal.hide(MainActivity.this, Appodeal.BANNER_VIEW);
        //        }
        //    }
        //});

        //Appodeal.show(this, Appodeal.BANNER_VIEW);

        bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        bottomNavigation.setBackgroundColor(Color.parseColor("#000000"));

        //bottomNavigation.setDrawingCacheBackgroundColor(Color.parseColor("#000000"));

        //Appodeal.show(this, Appodeal.BANNER_TOP);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        title.setTypeface(lobster);

        // [START AUTH AND NAV-DRAWER BOILERPLATE]

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
        }

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull final FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    keepSynced();
                    //paperTest();
                    setUpTypeAheadData();
                    //FirstTimeModelClass firstTimeModelClass = new FirstTimeModelClass(true, true, true, true, true,
                    //        true);
                    //DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime")
                    //        .child(uid);
                    //firstTimeRef.setValue(firstTimeModelClass);
                    if(savedInstanceState == null){
                        if(isDrawerNull()){
                            Log.i("drawer", "drawer is null (MainActivity - onCreate)");
                            setUpNavDrawer(MainActivity.this, toolbar);
                        }
                        if(getIntent().getExtras() == null){
                            setNavDrawerSelection(1);
                        }else{
                            int id = getIntent().getExtras().getInt("fragID");
                            if(id == 0){
                                setNavDrawerSelection(1);
                            }else if(id == 1){
                                setNavDrawerSelection(2);
                            }else if(id == 2){
                                setNavDrawerSelection(3);
                            }else if(id == 3){
                                setNavDrawerSelection(4);
                            }else if(id == 4){
                                setNavDrawerSelection(5);
                            }
                        }

                    }
                    setUpAppodealAndGDPR(uid);
                    checkForBadges();
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            }
        };

        newWorkoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bottomNavigation.setSelectedIndex(2, false);
            }
        });

        if(savedInstanceState == null){
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
            //fragmentTransaction.commit();
        }



        if (getIntent().getExtras() != null) {
            if(getIntent().getStringExtra("fragIDAndExtras") != null){
                String delims = "[_]";
                String mExtras = getIntent().getStringExtra("fragIDAndExtras");
                String[] tokens = mExtras.split(delims);

                int id = Integer.parseInt(tokens[0]);

                if(id == 1){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TemplateMenuFrag templateMenuFrag = new TemplateMenuFrag();
                    templateMenuFrag.extras = mExtras;
                    fragmentTransaction.replace(R.id.mainFragHolder, templateMenuFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(1, false);
                }

            }else{
                int id = getIntent().getExtras().getInt("fragID");

                if(id == 0){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new FeedHolderFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(0, false);

                }else if(id == 1){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    TemplateMenuFrag templateMenuFrag = new TemplateMenuFrag();

                    fragmentTransaction.replace(R.id.mainFragHolder, templateMenuFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(1, false);
                } else if(id == 2){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(2, false);
                } else if(id == 3){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag3());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(3, false);
                } else if(id == 4){

                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new ChatMainFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    bottomNavigation.setSelectedIndex(4, false);
                }
            }
        } else{
            if(mFirebaseUser == null){
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            }else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragHolder, new FeedHolderFrag());
                fragmentTransaction.commit();
                bottomNavigation.setSelectedIndex(0, false);
            }
        }

        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
                Log.i("infoBottom", String.valueOf(i) + ", " + String.valueOf(i1) + ", " + String.valueOf(b));

                try{
                    if (i1 == 0) {
                        setNavDrawerSelection(1);
                        //showSearchButton();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.mainFragHolder, new FeedHolderFrag());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                    } else if (i1 == 1) {
                        setNavDrawerSelection(2);
                        //hideSearchButton();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                    } else if (i1 == 2) {
                        setNavDrawerSelection(3);
                        //hideSearchButton();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                        fragmentTransaction.addToBackStack(null); // some kind of crash (illegal state exception)
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                    } else if (i1 == 3) {
                        setNavDrawerSelection(4);
                        //hideSearchButton();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag3());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                    } else if (i1 == 4) {
                        setNavDrawerSelection(5);
                        //hideSearchButton();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                        fragmentTransaction.replace(R.id.mainFragHolder, new ChatMainFrag());
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                        invalidateOptionsMenu();
                    }
                }catch (IllegalStateException e){

                }
            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                //TODO: if not on first fragment, come back to original frag
                Log.i("info", "menu item re-selected");

                //if (i1 == 0) {
                //    FragmentManager fragmentManager = getSupportFragmentManager();
                //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //    fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                //    fragmentTransaction.addToBackStack(null);
                //    fragmentTransaction.commit();
                //} else if (i1 == 1) {
                //    FragmentManager fragmentManager = getSupportFragmentManager();
                //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //    fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
                //    fragmentTransaction.addToBackStack(null);
                //    fragmentTransaction.commit();
                //} else if (i1 == 2) {
                //    FragmentManager fragmentManager = getSupportFragmentManager();
                //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //    fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                //    fragmentTransaction.addToBackStack(null);
                //    fragmentTransaction.commit();
                //} else if (i1 == 3) {
                //    FragmentManager fragmentManager = getSupportFragmentManager();
                //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //    fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag3());
                //    fragmentTransaction.addToBackStack(null);
                //    fragmentTransaction.commit();
                //} else if (i1 == 4) {
                //    FragmentManager fragmentManager = getSupportFragmentManager();
                //    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //    fragmentTransaction.replace(R.id.mainFragHolder, new ChatMainFrag());
                //    fragmentTransaction.addToBackStack(null);
                //    fragmentTransaction.commit();
                //}
            }
        });

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!query.equals("")){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    UserSearchFrag userSearchFrag = new UserSearchFrag();
                    userSearchFrag.searchString = query;
                    fragmentTransaction.replace(R.id.mainFragHolder, userSearchFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    return false;
                }

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                AppBarLayout appBarLayout = findViewById(R.id.appBar);
                appBarLayout.setExpanded(false, true);
            }

            @Override
            public void onSearchViewClosed() {
                AppBarLayout appBarLayout = findViewById(R.id.appBar);
                appBarLayout.setExpanded(true, false);
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("infoClick", "hello");
                try{
                    searchView.closeSearch();
                    String userName = ((AppCompatTextView) view).getText().toString();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    UserSearchFrag userSearchFrag = new UserSearchFrag();
                    userSearchFrag.searchString = userName;
                    fragmentTransaction.replace(R.id.mainFragHolder, userSearchFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }catch (NullPointerException e){

                }
            }
        });

        SearchAdapter adapter = new SearchAdapter();
        searchView.setAdapter(adapter);

    }

    private void keepSynced(){

        DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child(
                "templates").child(uid);
        DatabaseReference userRef =
                FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference().child(
                "runningAssistor").child(uid).child("assistorModel");
        DatabaseReference workoutHistoryRef =
                FirebaseDatabase.getInstance().getReference().child("workoutHistory").child(uid);
        DatabaseReference defaultRef = FirebaseDatabase.getInstance().getReference().child(
                "defaultTemplates");
        templateRef.keepSynced(true);
        userRef.keepSynced(true);
        runningRef.keepSynced(true);
        workoutHistoryRef.keepSynced(true);
        defaultRef.keepSynced(true);

    }

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        Log.i("drawer", "MainActivity onStart called");

        if(isDrawerNull() && (FirebaseAuth.getInstance().getCurrentUser() != null)){
            Log.i("drawer", "drawer is null (MainActivity - onStart)");
            setUpNavDrawer(MainActivity.this, toolbar);
        }

        //if(MainActivitySingleton.getInstance().isFeedFirstTime){

        //}

        //TemplateMenuFrag templateMenuFrag = (TemplateMenuFrag) getSupportFragmentManager().findFragmentByTag
        //        ("templateMenuFrag");
        //if(templateMenuFrag != null){
        //    if(templateMenuFrag.isVisible()){
        //        setNavDrawerSelection(3);
        //    }
        //}

    }
    // [END on_start_add_listener]

    // [START on_stop_remove_listener]
    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
        try{
            //Intent stopIntent = new Intent(MainActivity.this,
            //        AssistorServiceClass.class);
            //Intent stopIntent2 = new Intent(MainActivity.this,
            //        RestTimerServiceClass.class);
            //MainActivity.this.stopService(stopIntent);
            //MainActivity.this.stopService(stopIntent2);
        }catch (NullPointerException e){

        }
    }
    // [END on_stop_remove_listener]

    private void setUpAppodealAndGDPR(String uidl){
        final SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        if(sharedPref.contains("consent")){
            boolean isGDPR = sharedPref.getBoolean("consent", true);
            String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
            Appodeal.disableLocationPermissionCheck();
            Appodeal.setBannerViewId(R.id.appodealBannerView);
            Appodeal.initialize(MainActivity.this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER, isGDPR);
            Appodeal.show(MainActivity.this, Appodeal.BANNER_VIEW);
            Log.i("consent", "Consent bool = " + isGDPR + ", inflating ads");
        }else{
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uidl).child
                    ("isGDPR");
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        boolean isGDPR = dataSnapshot.getValue(Boolean.class);
                        String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
                        Appodeal.disableLocationPermissionCheck();
                        Appodeal.setBannerViewId(R.id.appodealBannerView);
                        Appodeal.initialize(MainActivity.this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER, isGDPR);
                        Appodeal.show(MainActivity.this, Appodeal.BANNER_VIEW);
                        sharedPref.edit().putBoolean("consent", isGDPR).apply();
                        Log.i("consent", "Consent bool = " + isGDPR + ", inflating ads");
                    }else{
                        showConsentForm();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

    }


    private void showConsentForm(){
        Intent intent = new Intent(MainActivity.this, ConsentFormDialogActivity.class);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if(resultCode == 1){
                boolean isGDPR = data.getBooleanExtra("consentBool", true);
                SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                sharedPref.edit().putBoolean("consent", isGDPR).apply();
                String appKey = "e05b98bf43240a8687216b4e3106a598ced75a344b6c75f2";
                Appodeal.disableLocationPermissionCheck();
                Appodeal.setBannerViewId(R.id.appodealBannerView);
                Appodeal.initialize(MainActivity.this, appKey, Appodeal.INTERSTITIAL | Appodeal.BANNER, isGDPR);
                Appodeal.show(MainActivity.this, Appodeal.BANNER_VIEW);
                Log.i("consent", "Consent bool = " + isGDPR + ", inflating ads");
                DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid)
                        .child("isGDPR");
                userRef.setValue(isGDPR);
            }
        }
    }

    private void checkForBadges(){
        // first we'll check for uncompleted workout
        final String today = LocalDate.now().toString();
        DatabaseReference historyRef = FirebaseDatabase.getInstance().getReference().
                child("workoutHistory").child(uid).child(today);
        historyRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(!dataSnapshot.exists()){
                    bottomNavigation.getBadgeProvider().show(R.id.bbn_item3);
                    String newWorkoutString = "New Workout (" + today + ")! >>";
                    newWorkoutButton.setText(newWorkoutString);
                    newWorkoutButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void hideBadge(int i){
        if(i == 3){
            if(bottomNavigation.getBadgeProvider().hasBadge(R.id.bbn_item3)){
                bottomNavigation.getBadgeProvider().remove(R.id.bbn_item3);
            }
        }
    }

    public void hideToolbarBadge(){
        newWorkoutButton.setVisibility(View.GONE);
    }

    private void setUpTypeAheadData(){
        // set typeAheadData
        typeAheadData = new ArrayList<>();

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").child(uid);
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int index = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        UserNameIdModelClass userNameIdModelClass = dataSnapshot1.getValue(UserNameIdModelClass.class);
                        if(userNameIdModelClass.getUserName() != null){
                            String userName = userNameIdModelClass.getUserName();
                            typeAheadData.add(userName);
                        }
                        index++;
                        if(index == dataSnapshot.getChildrenCount()){
                            DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference()
                                    .child("followers").child(uid);
                            followerRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()) {
                                        for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                            UserNameIdModelClass userNameIdModelClass = dataSnapshot1.getValue(UserNameIdModelClass.class);
                                            if (userNameIdModelClass.getUserName() != null) {
                                                String userName = userNameIdModelClass.getUserName();
                                                typeAheadData.add(userName);
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
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    public void hideSearchButton(){
        //LinearLayout searchViewLL = (LinearLayout) findViewById(R.id.searchViewLL);
        //searchViewLL.setVisibility(View.GONE);
        //searchView.setBackgroundColor(Color.parseColor("#000000"));

    }

    public void showSearchButton(){
        //LinearLayout searchViewLL = (LinearLayout) findViewById(R.id.searchViewLL);
        //searchViewLL.setVisibility(View.VISIBLE);
        //searchView.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(bottomNavigation != null){
            if(bottomNavigation.getSelectedIndex() == 0){
                getMenuInflater().inflate(R.menu.menu_main, menu);

                MenuItem item = menu.findItem(R.id.action_search);
                searchView.setMenuItem(item);

                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }

    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    public void setBottomNavIndex(int navIndex){
        //BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        bottomNavigation.setSelectedIndex(navIndex, false);
    }

    private class SearchAdapter extends BaseAdapter implements Filterable {

        private ArrayList<String> data;

        //private String[] typeAheadData;

        LayoutInflater inflater;

        public SearchAdapter() {
            inflater = LayoutInflater.from(MainActivity.this);
            data = new ArrayList<String>();
            //typeAheadData = getResources().getStringArray(R.array.state_array_full);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (!TextUtils.isEmpty(constraint)) {
                        // Retrieve the autocomplete results.
                        List<String> searchData = new ArrayList<>();

                        for (String str : typeAheadData) {
                            if (str.toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                                searchData.add(str);
                            }
                        }

                        // Assign the data to the FilterResults
                        filterResults.values = searchData;
                        filterResults.count = searchData.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results.values != null) {
                        data = (ArrayList<String>) results.values;
                        notifyDataSetChanged();
                    }
                }
            };
            return filter;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                mViewHolder = new MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (MyViewHolder) convertView.getTag();
            }

            String currentListData = (String) getItem(position);

            mViewHolder.textView.setText(currentListData);

            return convertView;
        }


        private class MyViewHolder {
            TextView textView;

            public MyViewHolder(View convertView) {
                textView = (TextView) convertView.findViewById(android.R.id.text1);
                textView.setTextColor(Color.parseColor("#D1B91D"));
                textView.setBackgroundColor(Color.parseColor("#000000"));
            }
        }



    }

    @Override
    public void onResume(){
        super.onResume();
        //if (getIntent().getExtras() != null) {
        //BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        //bottomNavigation.setBackgroundColor(Color.parseColor("#000000"));
//
        //    int id = getIntent().getExtras().getInt("fragID");
//
        //    if(id == 0){
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
        //        fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
        //        fragmentTransaction.addToBackStack(null);
        //        fragmentTransaction.commit();
        //    }else if(id == 1){
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
        //        fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
        //        fragmentTransaction.addToBackStack(null);
        //        fragmentTransaction.commit();
        //    } else if(id == 2){
        //        FragmentManager fragmentManager = getSupportFragmentManager();
        //        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
        //        fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
        //        fragmentTransaction.addToBackStack(null);
        //        fragmentTransaction.commit();
        //    }
        //} else{
        //    BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        //    bottomNavigation.setSelectedIndex(1, false);
        //}
    }




}

