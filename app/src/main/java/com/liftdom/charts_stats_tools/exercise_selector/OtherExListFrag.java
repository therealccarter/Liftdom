package com.liftdom.charts_stats_tools.exercise_selector;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

/**
 * A simple {@link Fragment} subclass.
 */
public class OtherExListFrag extends Fragment {


    public OtherExListFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList3) StickyListHeadersListView stickyList;

    boolean noCheckbox = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_full_body_ex_list, container, false);

        ButterKnife.bind(this, view);

        ExSelectorStickyAdapter adapter = new ExSelectorStickyAdapter(getContext(), getActivity(), "other", noCheckbox);

        stickyList.setAdapter(adapter);


        return view;
    }


}
