package com.liftdom.knowledge_center.hall_of_fame;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

/**
 * Created by Brodin on 3/31/2017.
 */

public class HoFPagerAdapter extends FragmentStatePagerAdapter{

    CharSequence Titles[]={"Bodybuilding", "Powerlifting", "Strongman", "Other"};
    int NumbOfTabs = 4; // Store the number of tabs, this will also be passed when the ViewPagerAdapter is created

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public HoFPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {
        if(position == 0) // if the position is 0 we are returning the First tab
        {
            BodybuildingHoFFrag tab1 = new BodybuildingHoFFrag();
            return tab1;
        }
        else if(position == 1){
            PowerliftingHoFFrag tab2 = new PowerliftingHoFFrag();
            return tab2;
        }else if(position == 2){
            StrongmanHoFFrag tab3 = new StrongmanHoFFrag();
            return tab3;
        } else{
            GeneralFitnessHoFFRag tab4 = new GeneralFitnessHoFFRag();
            return tab4;
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
