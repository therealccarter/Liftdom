package com.liftdom.template_housing;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Brodin on 3/4/2018.
 */

public class SavedProgramsPagerAdapter extends FragmentStatePagerAdapter {

    CharSequence Titles[]={"Saved Programs", "Program Inbox"};
    int NumbOfTabs = 2;

    public SavedProgramsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb){
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            SavedTemplatesFrag tab1 = new SavedTemplatesFrag();

            return tab1;
        }else{
            ProgramInboxFrag tab2 = new ProgramInboxFrag();

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
