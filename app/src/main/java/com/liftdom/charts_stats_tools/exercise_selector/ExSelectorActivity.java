package com.liftdom.charts_stats_tools.exercise_selector;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
//import com.google.android.material.appbar.AppBarLayout;
import androidx.appcompat.widget.AppCompatTextView;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
//import android.support.v7.widget.*;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import android.widget.Toolbar;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchFrag;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.template_editor.ExercisePickerController;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
//import com.search.material.library.MaterialSearchView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ExSelectorActivity extends AppCompatActivity {

    ViewPager pager;
    ExPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Upper Body", "Lower Body", "Other", "Custom"};
    int Numboftabs = 4;
    //CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    //int Numboftabs = 3;
    private MaterialSearchView searchView;
    private ArrayList<String> typeAheadData;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean isFromExChart;
    String fragTag;

    @BindView(R.id.confirmButton) Button confirmButton;
    //@BindView(R.id.search_view) MaterialSearchView searchView;

    //public ExSelectorActivity(Boolean isExclusive){
//
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_ex_selector);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        // Handle Toolbar
        final androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        searchView = (MaterialSearchView) findViewById(R.id.search_view);

        setUpTypeAheadData();

        if(getIntent().getExtras() != null){
            ExercisePickerController.getInstance().exID = getIntent().getExtras().getInt("exID");
            ExercisePickerController.getInstance().fragTag = getIntent().getExtras().getString(
                    "fragTag");
            if(getIntent().getStringExtra("exclusive") != null){
                adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, true, true);
                confirmButton.setVisibility(View.GONE);
            }else{
                // is from template editor
                // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
                adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, true, false);
                confirmButton.setVisibility(View.GONE);
            }
        }else{
            isFromExChart = true;
            // is from ex-chart frag
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, false, true);
        }

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in
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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                if(!query.equals("")){

                    // need to make this search the exercises and show it

                    //FragmentManager fragmentManager = getSupportFragmentManager();
                    //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    //UserSearchFrag userSearchFrag = new UserSearchFrag();
                    //userSearchFrag.searchString = query;
                    //fragmentTransaction.replace(R.id.mainFragHolder, userSearchFrag);
                    //fragmentTransaction.addToBackStack(null);
                    //fragmentTransaction.commit();

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
                //AppBarLayout appBarLayout = findViewById(R.id.appBar);
                //appBarLayout.setExpanded(false, true);
            }

            @Override
            public void onSearchViewClosed() {
                //AppBarLayout appBarLayout = findViewById(R.id.appBar);
                //appBarLayout.setExpanded(true, false);
            }
        });

        searchView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("infoClick", "hello");
                try{
                    searchView.closeSearch();
                    if(isFromExChart){
                        ExSelectorSingleton.getInstance().upperBodyItems.add(((AppCompatTextView) view).getText().toString());
                        setResult(1);
                        finish();
                    }else{
                        Intent intent = new Intent();
                        intent.putExtra("MESSAGE", ((AppCompatTextView) view).getText().toString());
                        intent.putExtra("fragTag", ExercisePickerController.getInstance().fragTag);
                        setResult(2, intent);
                        finish();
                    }
                }catch (NullPointerException e){

                }
            }
        });

        SearchAdapter adapter = new SearchAdapter();
        searchView.setAdapter(adapter);

    }

    private void setUpTypeAheadData(){
        typeAheadData = new ArrayList<>();
        DatabaseReference customExRef = FirebaseDatabase.getInstance().getReference().child("customExercises")
                .child(uid);

        customExRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int i = 0;
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        CustomExModelClass customExModelClass = dataSnapshot1.getValue(CustomExModelClass.class);
                        typeAheadData.add(customExModelClass.getExerciseName());
                        i++;
                        if(i == dataSnapshot.getChildrenCount()){
                            String[] upper = getResources().getStringArray(R.array.upperBodyList);
                            String[] lower = getResources().getStringArray(R.array.lowerBodyList);
                            String[] other = getResources().getStringArray(R.array.otherBodyList);

                            List<String> upperList = Arrays.asList(upper);
                            List<String> lowerList = Arrays.asList(lower);
                            List<String> otherList = Arrays.asList(other);

                            typeAheadData.addAll(upperList);
                            typeAheadData.addAll(lowerList);
                            typeAheadData.addAll(otherList);
                        }
                    }
                }else{
                    String[] upper = getResources().getStringArray(R.array.upperBodyList);
                    String[] lower = getResources().getStringArray(R.array.lowerBodyList);
                    String[] other = getResources().getStringArray(R.array.otherBodyList);

                    List<String> upperList = Arrays.asList(upper);
                    List<String> lowerList = Arrays.asList(lower);
                    List<String> otherList = Arrays.asList(other);

                    typeAheadData.addAll(upperList);
                    typeAheadData.addAll(lowerList);
                    typeAheadData.addAll(otherList);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        item.setTitle(R.string.searchExercises);
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

        //private String[] typeAheadData;

        LayoutInflater inflater;

        public SearchAdapter() {
            inflater = LayoutInflater.from(ExSelectorActivity.this);
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
                            if (hasSearchWord(constraint.toString().toLowerCase(), str)) {
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
            SearchAdapter.MyViewHolder mViewHolder;

            if (convertView == null) {
                convertView = inflater.inflate(android.R.layout.simple_list_item_1, parent, false);
                mViewHolder = new SearchAdapter.MyViewHolder(convertView);
                convertView.setTag(mViewHolder);
            } else {
                mViewHolder = (SearchAdapter.MyViewHolder) convertView.getTag();
            }

            String currentListData = (String) getItem(position);

            mViewHolder.textView.setText(currentListData);

            return convertView;
        }


        private class MyViewHolder {
            TextView textView;

            public MyViewHolder(View convertView) {
                textView = (TextView) convertView.findViewById(android.R.id.text1);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setBackgroundColor(Color.parseColor("#000000"));
            }
        }
    }

    private boolean hasSearchWord(String input, String str){
        boolean matches = false;

        String delims = "[ ,(,),-]";
        String[] tokens = str.split(delims);
        List<String> tokensList = Arrays.asList(tokens);

        for(String string : tokensList){
            if(string.toLowerCase().startsWith(input)){
                matches = true;
            }
        }

        return matches;
    }
}
