package com.liftdom.liftdom.utils.exercise_selector;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Brodin on 3/24/2017.
 */

public class ExPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    int NumbOfTabs = 3; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created
    boolean mNoCheckbox = false;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ExPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ExPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, boolean noCheckbox) {
        super(fm);
        mNoCheckbox = noCheckbox;
        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            UpperBodyExListFrag tab1 = new UpperBodyExListFrag();
            if(mNoCheckbox){
                tab1.noCheckbox = true;
            }
            return tab1;
        }
        else if(position == 1){
            LowerBodyExListFrag tab2 = new LowerBodyExListFrag();
            if(mNoCheckbox){
                tab2.noCheckbox = true;
            }
            return tab2;
        }else {
            OtherExListFrag tab3 = new OtherExListFrag();
            if(mNoCheckbox){
                tab3.noCheckbox = true;
            }
            return tab3;
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
