package com.liftdom.user_profile.single_user_profile;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;
import com.liftdom.user_profile.calendar_stuff.HistoryCalendarTab;

/**
 * Created by Brodin on 2/28/2018.
 */

public class ProfilePagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]={"Workouts", "Calendar"};
    int NumbOfTabs = 2;
    String mUid;
    boolean isOtherUser;

    public ProfilePagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, String uid, boolean
            otherUser){
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mUid = uid;
        this.isOtherUser = otherUser;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            SelfFeedFrag tab1 = new SelfFeedFrag();

            tab1.uidFromOutside = mUid;

            return tab1;
        }else{
            HistoryCalendarTab tab2 = new HistoryCalendarTab();

            tab2.xUid = mUid;
            tab2.isOtherUser = isOtherUser;

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
