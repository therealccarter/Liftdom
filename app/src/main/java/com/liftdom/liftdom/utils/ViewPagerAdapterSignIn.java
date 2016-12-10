package com.liftdom.liftdom.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.liftdom.liftdom.SignInTab1;
import com.liftdom.liftdom.SignInTab2;
import com.liftdom.template_editor.ExPickerTab1;
import com.liftdom.template_editor.ExPickerTab2;

/**
 * Created by Chris on 12/10/2016.
 */

public class ViewPagerAdapterSignIn extends FragmentStatePagerAdapter {

    CharSequence Titles[]={"Sign-In","Create Account"};
    int NumbOfTabs = 2; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created


    // Build a Constructor and assign the passed Values to appropriate values in the class
    public ViewPagerAdapterSignIn(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            SignInTab1 tab1 = new SignInTab1();
            return tab1;
        }
        else
        {
            SignInTab2 tab2 = new SignInTab2();
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
