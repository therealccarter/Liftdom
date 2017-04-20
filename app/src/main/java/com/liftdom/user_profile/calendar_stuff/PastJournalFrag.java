package com.liftdom.user_profile.calendar_stuff;


import android.graphics.Typeface;
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
public class PastJournalFrag extends Fragment {

    public String journalString;


    public PastJournalFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.journalTextView) TextView journalView;
    @BindView(R.id.title) TextView title;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_journal, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        title.setTypeface(lobster);

        journalView.setText(journalString);


        return view;
    }

}
