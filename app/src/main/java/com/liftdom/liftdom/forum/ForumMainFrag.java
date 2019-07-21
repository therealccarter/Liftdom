package com.liftdom.liftdom.forum;


import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.utils.RandomUsersBannerFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class ForumMainFrag extends Fragment {


    public ForumMainFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
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

    @BindView(R.id.comingSoonView)
    TextView comingSoonView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_forum_main_frag2, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        comingSoonView.setTypeface(lobster);

        headerChanger("Forum");
        navChanger(3);

        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getChildFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RandomUsersBannerFrag bannerFrag = new RandomUsersBannerFrag();
            bannerFrag.isShowAllTheTime = true;
            fragmentTransaction.add(R.id.randomUsersBannerLL, bannerFrag, "randomUsersBanner");
            fragmentTransaction.commit();
        }

        return view;
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
