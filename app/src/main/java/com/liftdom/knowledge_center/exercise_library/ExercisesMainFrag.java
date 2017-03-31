package com.liftdom.knowledge_center.exercise_library;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.liftdom.utils.exercise_selector.ExPagerAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExercisesMainFrag extends Fragment {


    public ExercisesMainFrag() {
        // Required empty public constructor
    }

    headerChangeToFrag mCallback;

    public interface headerChangeToFrag{
        public void changeHeader(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeader(title);
    }

    ViewPager pager;
    ExLibPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    int Numboftabs = 3;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercises_main, container, false);
        headerChanger("Exercise Library");

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ExLibPagerAdapter(fragmentManager, Titles,
                Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) view.findViewById(R.id.pager1);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) view.findViewById(R.id.tabs1);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeToFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
