package com.liftdom.user_profile;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.liftdom.charts_stats_tools.ex_history_chart.StatChartsFrag;

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {


    CharSequence Titles[]={"Progression Charts", "Body Level"};
    int NumbOfTabs = 2; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ProfilePagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            StatChartsFrag tab1 = new StatChartsFrag();
            return tab1;
        }
        else{
            BodyLevelTab tab2 = new BodyLevelTab();
            return tab2;
        }
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
