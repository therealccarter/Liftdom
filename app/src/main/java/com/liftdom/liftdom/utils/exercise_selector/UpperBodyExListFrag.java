package com.liftdom.liftdom.utils.exercise_selector;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class UpperBodyExListFrag extends Fragment{


    public UpperBodyExListFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList1) StickyListHeadersListView stickyList;

    boolean noCheckbox = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_upper_body_ex_list, container, false);

        ButterKnife.bind(this, view);

        ExSelectorStickyAdapter adapter = new ExSelectorStickyAdapter(getContext(), getActivity(), "upper", noCheckbox);

        stickyList.setAdapter(adapter);

        return view;
    }


}