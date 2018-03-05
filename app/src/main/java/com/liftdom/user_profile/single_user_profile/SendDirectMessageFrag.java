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
public class SendDirectMessageFrag extends Fragment {


    public SendDirectMessageFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_direct_message, container, false);

        return view;
    }

}
