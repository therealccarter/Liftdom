package com.liftdom.liftdom.main_social_feed.feed_slider;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.liftdom.liftdom.main_social_feed.GlobalFeedFrag;
import com.liftdom.liftdom.main_social_feed.MainFeedFrag;

/**
 * Created by Brodin on 4/19/2018.
 */
public class FeedHolderPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]={"Home Feed", "Global Feed"};
    int NumbOfTabs = 2;

    public FeedHolderPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb){
        super(fm);
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){

            MainFeedFrag tab1 = new MainFeedFrag();

            return tab1;
        }else{

            GlobalFeedFrag tab2 = new GlobalFeedFrag();

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
