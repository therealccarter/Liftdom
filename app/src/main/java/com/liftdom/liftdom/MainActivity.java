package com.liftdom.liftdom;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.ChartsStatsToolsActivity;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.forum.ForumMainFrag;
import com.liftdom.liftdom.main_social_feed.MainFeedFrag;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchFrag;
import com.liftdom.settings.SettingsListActivity;
import com.liftdom.template_housing.TemplateMenuFrag;
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
import com.search.material.library.MaterialSearchView;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    ViewPager viewPager;

    private FirebaseAuth.AuthStateListener mAuthListener;

    String username = "failed";

    private MaterialSearchView searchView;

    // butterknife
    @BindView(R.id.title) TextView title;


    //DatabaseReference mRootRef;
    //String uid;
    DatabaseReference mRootRef;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        // set the key account values

        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");

        title.setTypeface(lobster);

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
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
                    if (getIntent().getStringExtra("username") != null) {

                    }
                    mRootRef = FirebaseDatabase.getInstance().getReference();
                    uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    final DatabaseReference userListRef = mRootRef.child("userList").child(uid);
                    userListRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if(!dataSnapshot.exists()){
                                userListRef.setValue(user.getDisplayName());
                                
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                    DatabaseReference settingsRef = mRootRef.child("users").child(uid).child("heightUnit");
                    settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.getValue() == null) {
                                DatabaseReference heightRef = mRootRef.child("users").child(uid).child("heightUnit");
                                heightRef.setValue("footInches");
                                DatabaseReference bodyWeightRef = mRootRef.child("users").child(uid).child("bodyWeightUnit");
                                bodyWeightRef.setValue("pounds");
                                DatabaseReference weightRef = mRootRef.child("users").child(uid).child("weightUnit");
                                weightRef.setValue("pounds");
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");
                    startActivity(new Intent(MainActivity.this, SignInActivity.class));
                }

            }
        };


        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        //Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
//
        //mainActivityTitle.setTypeface(lobster);


        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(this)
                .withHeaderBackground(R.drawable.header)
                .withSelectionListEnabledForSingleProfile(false)
                .withOnAccountHeaderSelectionViewClickListener(new AccountHeader.OnAccountHeaderSelectionViewClickListener() {
                    @Override
                    public boolean onClick(View view, IProfile profile) {
                        Intent intent = new Intent(MainActivity.this, CurrentUserProfile.class);
                        startActivity(intent);
                        return false;
                    }
                })
                .withOnAccountHeaderProfileImageListener(new AccountHeader.OnAccountHeaderProfileImageListener() {
                    @Override
                    public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                        Intent intent = new Intent(MainActivity.this, CurrentUserProfile.class);
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
                        new PrimaryDrawerItem().withName("Today's Workout").withIdentifier(2),
                        new PrimaryDrawerItem().withName("Workout Templating").withIdentifier(3),
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
                                intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 2) {
                                intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 2);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 3) {
                                intent = new Intent(MainActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 0);
                                startActivity(intent);
                            }
                            if (drawerItem.getIdentifier() == 4) {
                                intent = new Intent(MainActivity.this, KnowledgeCenterHolderActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 5) {
                                intent = new Intent(MainActivity.this, ChartsStatsToolsActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 6) {
                                intent = new Intent(MainActivity.this, PremiumFeaturesActivity.class);
                            }
                            if (drawerItem.getIdentifier() == 7) {
                                intent = new Intent(MainActivity.this, SettingsListActivity.class);
                            }
                            if (intent != null) {
                                MainActivity.this.startActivity(intent);
                            }
                        }
                        return true;
                    }
                })
                .build();


        if (mFirebaseUser != null) {
            DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

            //username = KeyAccountValuesActivity.getInstance().getUserName();

            header.addProfile(new ProfileDrawerItem().withIcon(ContextCompat.getDrawable(getApplicationContext(), R
                    .drawable.usertest))
                    .withName
                            (mFirebaseUser.getDisplayName()).withEmail
                            (mFirebaseUser.getEmail()), 0);
        }

        if(savedInstanceState == null){
            //FragmentManager fragmentManager = getSupportFragmentManager();
            //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
            //fragmentTransaction.commit();
        }

        if (getIntent().getExtras() != null) {

            BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);

            int id = getIntent().getExtras().getInt("fragID");

            if(id == 0){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
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

        BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);

        bottomNavigation.setOnMenuItemClickListener(new BottomNavigation.OnMenuItemSelectionListener() {
            @Override
            public void onMenuItemSelect(@IdRes int i, int i1, boolean b) {
                Log.i("info", String.valueOf(i) + ", " + String.valueOf(i1) + ", " + String.valueOf(b));

                if (i1 == 0) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 1) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 2) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 3) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    fragmentTransaction.replace(R.id.mainFragHolder, new ForumMainFrag());
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                } else if (i1 == 4) {
                    // TODO: Set to chat
                }
            }

            @Override
            public void onMenuItemReselect(@IdRes int i, int i1, boolean b) {
                Log.i("info", "menu item re-selected");
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
        if (getIntent().getExtras() != null) {
            BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);

            int id = getIntent().getExtras().getInt("fragID");

            if(id == 0){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new TemplateMenuFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else if(id == 1){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new MainFeedFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if(id == 2){
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.mainFragHolder, new WorkoutAssistorFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        } else{
            BottomNavigation bottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
            bottomNavigation.setSelectedIndex(1, false);
        }
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
    }
    // [END on_stop_remove_listener]


}

