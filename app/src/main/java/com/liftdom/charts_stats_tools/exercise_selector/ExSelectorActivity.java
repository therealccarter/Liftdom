package com.liftdom.charts_stats_tools.exercise_selector;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatTextView;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.user_search.UserSearchFrag;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.template_editor.ExercisePickerController;
import com.search.material.library.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class ExSelectorActivity extends AppCompatActivity {

    ViewPager pager;
    ExPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Upper Body", "Lower Body", "Other", "Custom"};
    int Numboftabs = 4;
    //CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    //int Numboftabs = 3;
    //private MaterialSearchView searchView;
    private ArrayList<String> typeAheadData;

    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.search_view) MaterialSearchView searchView;

    //public ExSelectorActivity(Boolean isExclusive){
//
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_ex_selector);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        setUpTypeAheadData();

        if(getIntent().getExtras() != null){
            ExercisePickerController.getInstance().exID = getIntent().getExtras().getInt("exID");
            // is from template editor
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, true, false);
            confirmButton.setVisibility(View.GONE);
        }else{
            // is from ex-chart frag
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, false, true);
        }

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
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

    private void setUpTypeAheadData(){
        typeAheadData = new ArrayList<>();
        typeAheadData.add("yo");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
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
                textView.setTextColor(Color.parseColor("#D1B91D"));
            }
        }



    }
}
