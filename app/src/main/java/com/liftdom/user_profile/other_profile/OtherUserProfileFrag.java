package com.liftdom.user_profile.other_profile;


import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.calendar_stuff.HistoryCalendarTab;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherUserProfileFrag extends Fragment {


    public OtherUserProfileFrag() {
        // Required empty public constructor
    }

    public String userName;
    public String xUid;

    @BindView(R.id.profileHeaderHolder) LinearLayout profileHeaderHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_user_profile, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            OtherUserProfileHeaderFrag otherUserProfileHeaderFrag = new OtherUserProfileHeaderFrag();
            otherUserProfileHeaderFrag.userName = userName;
            otherUserProfileHeaderFrag.xUid = xUid;

            fragmentTransaction.add(R.id.profileHeaderHolder, otherUserProfileHeaderFrag);

            HistoryCalendarTab historyCalendarTab = new HistoryCalendarTab();
            historyCalendarTab.isOtherUser = true;
            historyCalendarTab.xUid = xUid;

            fragmentTransaction.add(R.id.fragHolder2, historyCalendarTab);

            fragmentTransaction.commit();
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) view.findViewById(R.id
                .collapsingToolbar1);
        collapsingToolbarLayout.setTitle(userName);
        collapsingToolbarLayout.setExpandedTitleTextColor(ColorStateList.valueOf(Color.TRANSPARENT));


        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);


        return view;
    }

    @Override
    public void onStop(){
        super.onStop();

        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setVisibility(View.VISIBLE);

    }


}
