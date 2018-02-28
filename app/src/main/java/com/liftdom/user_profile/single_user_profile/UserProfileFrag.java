package com.liftdom.user_profile.single_user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.ProfileOptionsTabFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserProfileFrag extends Fragment {


    public UserProfileFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;

    @BindView(R.id.headerHolder) LinearLayout headerHolder;
    @BindView(R.id.headerHolder2) LinearLayout headerHolder2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_profile, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null){
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();

            ProfileHeaderFrag profileHeaderFrag = new ProfileHeaderFrag();
            profileHeaderFrag.uidFromOutside = uidFromOutside;
            fragmentTransaction.replace(headerHolder.getId(), profileHeaderFrag);

            ProfileOptionsTabFrag profileOptionsTabFrag = new ProfileOptionsTabFrag();
            profileOptionsTabFrag.xUid = uidFromOutside;
            fragmentTransaction.replace(headerHolder2.getId(), profileOptionsTabFrag);

            fragmentTransaction.commitAllowingStateLoss();
        }



        return view;
    }

}
