package com.liftdom.charts_stats_tools.exercise_selector;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Brodin on 5/9/2020.
 */
public class ExPagerAdapterCustomList extends FragmentStatePagerAdapter {

    private CharSequence Titles[]={"Upper Body", "Lower Body", "Other", "Custom"};
    //CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    private int NumbOfTabs = 4; // Store the number of tabs, this will also be passed when the
    private boolean mNoCheckbox = false;
    private boolean mIsExclusive = false;
    private String listType;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ExPagerAdapterCustomList(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ExPagerAdapterCustomList(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb,
                           boolean noCheckbox, boolean isExclusive) {
        super(fm);
        mNoCheckbox = noCheckbox;
        mIsExclusive = isExclusive;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    public String getListType() {
        return listType;
    }

    public void setListType(String listType) {
        this.listType = listType;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        CustomOpenExListFrag tab1 = new CustomOpenExListFrag();
        if(mNoCheckbox){
            tab1.noCheckbox = true;
        }
        if(mIsExclusive){
            tab1.isExclusive = true;
        }
        tab1.setListType(getListType());
        return tab1;
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }
}
