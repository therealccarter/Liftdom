package com.liftdom.workout_programs.Smolov;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovIntroFrag extends Fragment {


    public SmolovIntroFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.nextButton) Button nextButton;
    @BindView(R.id.smolovDescription) TextView smolovDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_intro, container, false);

        ButterKnife.bind(this, view);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SmolovSetupFrag smolovSetupFrag = new SmolovSetupFrag();
                fragmentTransaction.replace(R.id.smolovFragHolder, smolovSetupFrag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction.addToBackStack(null);
            }
        });

        return view;
    }

}
