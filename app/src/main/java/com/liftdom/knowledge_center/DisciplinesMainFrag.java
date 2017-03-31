package com.liftdom.knowledge_center;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class DisciplinesMainFrag extends Fragment {


    public DisciplinesMainFrag() {
        // Required empty public constructor
    }

    headerChangeToFrag mCallback;

    public interface headerChangeToFrag{
        public void changeHeader(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeader(title);
    }

    @BindView(R.id.bodybuildingButton) Button bodybuildingButton;
    @BindView(R.id.powerliftingButton) Button powerliftingButton;
    @BindView(R.id.strongmanButton) Button strongmanButton;
    @BindView(R.id.powerbuildingButton) Button powerbuildingButton;
    @BindView(R.id.fitnessButton) Button fitnessButton;
    @BindView(R.id.crossfitButton) Button crossfitButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_disciplines_main, container, false);

        headerChanger("Disciplines");

        ButterKnife.bind(this, view);


        bodybuildingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "Bodybuilding";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        powerliftingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "Powerlifting";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        strongmanButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "Strongman";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        powerbuildingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "Powerbuilding";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "General Fitness";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        crossfitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // get an instance of FragmentTransaction from your Activity
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //add a fragment
                DisciplinesDetailFrag disciplineFrag = new DisciplinesDetailFrag();
                disciplineFrag.disciplineString = "Crossfit";
                fragmentTransaction.replace(R.id.knowledgeCenterHolder, disciplineFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeToFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
}
