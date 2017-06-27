package com.liftdom.liftdom.main_social_feed.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostSetSchemeSSFrag extends Fragment {


    public PostSetSchemeSSFrag() {
        // Required empty public constructor
    }


    String setSchemeString = "error";
    boolean differentType = false;

    @BindView(R.id.repsView)
    TextView repsView;
    @BindView(R.id.weightView) TextView weightView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_set_scheme_s, container, false);

        ButterKnife.bind(this, view);

        String delims = "[@]";
        String[] tokens = setSchemeString.split(delims);

        if(tokens.length != 1){
            repsView.setText(tokens[0]);
            weightView.setText(tokens[1]);
        }

        return view;
    }

}
