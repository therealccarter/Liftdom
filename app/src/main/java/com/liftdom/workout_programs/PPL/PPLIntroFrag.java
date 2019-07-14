package com.liftdom.workout_programs.PPL;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
public class PPLIntroFrag extends Fragment {


    public PPLIntroFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.nextButton) Button nextButton;
    @BindView(R.id.pplDescription) TextView pplDescription;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplintro, container, false);

        ButterKnife.bind(this, view);

        nextButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PPLSetupFrag pplSetupFrag = new PPLSetupFrag();
                fragmentTransaction.replace(R.id.pplFragHolder, pplSetupFrag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction.addToBackStack(null);
            }
        });

        return view;
    }

}
