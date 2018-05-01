package com.liftdom.template_housing.public_programs;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Brodin on 4/30/2018.
 */
public class PublicProgramsPagerAdapter extends FragmentStatePagerAdapter{

    CharSequence Titles[]={"All Public Programs", "My Public Programs"};
    int NumbOfTabs = 2;

    public PublicProgramsPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb){
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
    }

    @Override
    public Fragment getItem(int position){
        if(position == 0){
            PublicTemplatesFrag tab1 = new PublicTemplatesFrag();

            return tab1;
        }else{
            MyPublicTemplatesFrag tab2 = new MyPublicTemplatesFrag();

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
