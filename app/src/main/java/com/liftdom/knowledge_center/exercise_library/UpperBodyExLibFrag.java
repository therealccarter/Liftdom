package com.liftdom.knowledge_center.exercise_library;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.exercise_selector.ExSelectorStickyAdapter;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperBodyExLibFrag extends Fragment {


    public UpperBodyExLibFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList4) StickyListHeadersListView stickyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upper_body_ex_lib, container, false);

        ButterKnife.bind(this, view);

        ExLibraryStickyAdapter adapter = new ExLibraryStickyAdapter(getContext(), "upper");

        stickyList.setAdapter(adapter);

        return view;
    }

}
