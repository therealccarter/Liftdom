package com.liftdom.knowledge_center.hall_of_fame;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
public class StrongmanHoFFrag extends Fragment {


    public StrongmanHoFFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.stickyList) StickyListHeadersListView stickyList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_strongman_ho_f, container, false);

        ButterKnife.bind(this, view);

        HoFStickyAdapter adapter = new HoFStickyAdapter(getContext(), "Powerlifting");

        stickyList.setAdapter(adapter);

        return view;
    }

}
