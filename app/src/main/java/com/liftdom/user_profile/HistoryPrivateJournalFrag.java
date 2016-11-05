package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryPrivateJournalFrag extends Fragment {


    public HistoryPrivateJournalFrag() {
        // Required empty public constructor
    }

    String journalString;

    @BindView(R.id.privateJournalView) TextView privateJournalView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_private_journal, container, false);

        ButterKnife.bind(this, view);

        privateJournalView.setText(journalString);

        return view;
    }

}
