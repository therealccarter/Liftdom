package com.liftdom.liftdom.main_social_feed.feed_slider;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedHolderFrag extends Fragment {


    public FeedHolderFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        public void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    bottomNavChanger navChangerCallback;

    public interface bottomNavChanger{
        void setBottomNavIndex(int navIndex);
    }

    private void navChanger(int navIndex){
        navChangerCallback.setBottomNavIndex(navIndex);
    }

    FeedHolderPagerAdapter adapter;
    CharSequence Titles[]={"Home Feed", "Global Feed"};
    int NumbOfTabs = 2;

    @BindView(R.id.tabs) SlidingTabLayout tabsView;
    @BindView(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feed_holder, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Home");
        navChanger(0);

        setUpSlidingLayout();

        return view;
    }

    private void setUpSlidingLayout(){
        adapter = new FeedHolderPagerAdapter(this.getChildFragmentManager(), Titles, NumbOfTabs);

        pager.setAdapter(adapter);
        tabsView.setDistributeEvenly(true);
        tabsView.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabsView.setViewPager(pager);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
            navChangerCallback = (bottomNavChanger) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
