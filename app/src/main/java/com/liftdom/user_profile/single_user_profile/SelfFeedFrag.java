package com.liftdom.user_profile.single_user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SelfFeedFrag extends Fragment {


    public SelfFeedFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_self_feed, container, false);

        /**
         * What are the consequences of a self-feed? How would that work from the perspective of other users?
         *
         * So, if we are in the main feed and we comment: it needs to update to all main feeds, and the one self feed.
         *  If we rep the post, it needs to also go to all main feeds and the self feed but also add the uid to the
         *  self feed's post. That is so we know whether to show the shit or not.
         *
         * If we are in the self feed and we comment, we'll have to do a very
         */

        /**
         * OK, there's a huge bug in the WA. I need to fix that ASAP.
         *
         */

        return view;
    }

}
