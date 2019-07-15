package com.liftdom.user_profile.single_user_profile;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.user_profile.ProfileOptionsTabFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFrag extends Fragment {


    public UserProfileFrag() {
        // Required empty public constructor
    }

    ProfilePagerAdapter adapter;
    CharSequence Titles[]={"Workouts", "Calendar"};
    int NumbOfTabs = 2;

    String uidFromOutside;

    @BindView(R.id.headerHolder) LinearLayout headerHolder;
    @BindView(R.id.headerHolder2) LinearLayout headerHolder2;
    @BindView(R.id.tabs) SlidingTabLayout tabsView;
    @BindView(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, view);

        // Handle Toolbar
        //Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
//
        //setUpNavDrawer(UserProfileFullActivity.this, toolbar);

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            ProfileHeaderFrag profileHeaderFrag = new ProfileHeaderFrag();
            profileHeaderFrag.uidFromOutside = uidFromOutside;
            fragmentTransaction.replace(headerHolder.getId(), profileHeaderFrag);

            ProfileOptionsTabFrag profileOptionsTabFrag = new ProfileOptionsTabFrag();
            profileOptionsTabFrag.xUid = uidFromOutside;
            fragmentTransaction.replace(headerHolder2.getId(), profileOptionsTabFrag);

            fragmentTransaction.commitAllowingStateLoss();

            setUpSlidingLayout();
        }

        return view;
    }

    private void setUpSlidingLayout(){
        boolean isOtherUser = false;
        if(!FirebaseAuth.getInstance().getCurrentUser().getUid().equals(uidFromOutside)){
            isOtherUser = true;
        }

        adapter = new ProfilePagerAdapter(this.getChildFragmentManager(), Titles, NumbOfTabs,
                uidFromOutside, isOtherUser);

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

}
