package com.liftdom.liftdom.main_social_feed;


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
public class PostSetSchemeFrag extends Fragment {


    public PostSetSchemeFrag() {
        // Required empty public constructor
    }

    String setSchemeString = "error";
    boolean differentType = false;

    @BindView(R.id.setSchemeView) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_post_set_scheme, container, false);

        ButterKnife.bind(this, view);

        setSchemesView.setText(setSchemeString);

        return view;
    }

}
