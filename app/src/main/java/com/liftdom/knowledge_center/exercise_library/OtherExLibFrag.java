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
public class OtherExLibFrag extends Fragment {


    public OtherExLibFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList6) StickyListHeadersListView stickyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_other_ex_lib, container, false);

        ButterKnife.bind(this, view);

        ExLibraryStickyAdapter adapter = new ExLibraryStickyAdapter(getContext(), "other");

        stickyList.setAdapter(adapter);

        return view;
    }

}
