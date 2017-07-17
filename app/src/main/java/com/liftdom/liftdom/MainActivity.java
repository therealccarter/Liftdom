package com.liftdom.liftdom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.chat.ChatMainFrag;
import com.liftdom.liftdom.forum.ForumMainFrag;
import com.liftdom.liftdom.main_social_feed.MainFeedFrag;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchFrag;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.liftdom.misc_activities.PremiumFeaturesActivity;
import com.liftdom.misc_activities.SettingsListActivity;
import com.liftdom.template_housing.PublicTemplateChooserFrag;
import com.liftdom.template_housing.SavedTemplatesFrag;
import com.liftdom.template_housing.TemplateMenuFrag;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import com.liftdom.workout_assistor.WorkoutAssistorFrag;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.search.material.library.MaterialSearchView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements
        TemplateMenuFrag.headerChangeFromFrag,
        ChatMainFrag.headerChangeFromFrag,
        ForumMainFrag.headerChangeFromFrag,
        WorkoutAssistorFrag.headerChangeFromFrag,
        MainFeedFrag.headerChangeFromFrag,
        SavedTemplatesFrag.headerChangeFromFrag,
        PublicTemplateChooserFrag.headerChangeFromFrag{

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    ViewPager viewPager;
    private int selection = 0;

    private Bitmap profilePicBitmap;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String username = "failed";

    private MaterialSearchView searchView;
    private BottomNavigation bottomNavigation;

    // butterknife
    @BindView(R.id.title) TextView title;

    AccountHeader header;

    //DatabaseReference mRootRef;
    //String uid;
    DatabaseReference mRootRef;
    String uid;

    public void changeHeaderTitle(String title){
        TextView titleView = (TextView) findViewById(R.id.title);
        titleView.setText(title);
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        // set the key account values

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        bottomNavigation.setBackgroundColor(Color.parseColor("#000000"));
        //bottomNavigation.setDrawingCacheBackgroundColor(Color.parseColor("#000000"));


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
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // [START auth_state_listener]
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    if(savedInstanceState == null){
                        setUpNavDrawer(MainActivity.this, toolbar);
                    }
                    //SharedPreferences sharedPref = getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                    //if(sharedPref.getString("userName", "loading").equals("loading")){
                    //    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                    //}else{
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                                    MainActivitySingleton.getInstance().userModelClass = userModelClass;
                                    if(userModelClass.isIsImperial()){
                                        MainActivitySingleton.getInstance().isImperial = true;
                                    }
                                }else{
                                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    //}
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }
            }
        };


        if(savedInstanceState == null){
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
            //fragmentTransaction.commit();
        }

        if (getIntent().getExtras() != null) {

            int id = getIntent().getExtras().getInt("fragID");

            if(id == 0){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                TemplateMenuFrag templateMenuFrag = new TemplateMenuFrag();

                fragmentTransaction.replace(R.id.mainFragHolder, templateMenuFrag, "templateMenuFrag");
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                bottomNavigation.setSelectedIndex(0, false);
            }else if(id == 1){

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
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
            }
        } else{

            if(mFirebaseUser == null){
                startActivity(new Intent(MainActivity.this, SignInActivity.class));
            } else{
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
                fragmentTransaction.commit();
            }
        }



        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
                Log.i("info", String.valueOf(i) + ", " + String.valueOf(i1) + ", " + String.valueOf(b));

                if (i1 == 0) {
                    setNavDrawerSelection(3);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 1) {
                    setNavDrawerSelection(1);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 2) {
                    setNavDrawerSelection(2);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 3) {
                    setNavDrawerSelection(1);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 4) {
                    setNavDrawerSelection(1);
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new ChatMainFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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

                //    fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag());
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

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UserSearchFrag userSearchFrag = new UserSearchFrag();
                userSearchFrag.searchString = query;
                fragmentTransaction.replace(R.id.mainFragHolder, userSearchFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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

            }

            @Override
            public void onSearchViewClosed() {

            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        SearchAdapter adapter = new SearchAdapter();
        searchView.setAdapter(adapter);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    @Override
    public void onBackPressed() {
        if (searchView.isSearchOpen()) {
            searchView.closeSearch();
        } else {
            super.onBackPressed();
        }
    }

    private class SearchAdapter extends BaseAdapter implements Filterable {

        private ArrayList<String> data;

        private String[] typeAheadData;

        LayoutInflater inflater;

        public SearchAdapter() {
            inflater = LayoutInflater.from(MainActivity.this);
            data = new ArrayList<String>();
            typeAheadData = getResources().getStringArray(R.array.state_array_full);
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

    // [START on_start_add_listener]
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

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
    }
    // [END on_stop_remove_listener]


}

