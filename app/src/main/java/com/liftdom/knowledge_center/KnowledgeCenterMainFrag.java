package com.liftdom.knowledge_center;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.knowledge_center.exercise_library.ExerciseLibraryActivity;
import com.liftdom.knowledge_center.hall_of_fame.HallOfFameMainFrag;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeCenterMainFrag extends Fragment {


    public KnowledgeCenterMainFrag() {
        // Required empty public constructor
    }

    headerChangeToFrag mCallback;

    public interface headerChangeToFrag{
        public void changeHeader(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeader(title);
    }

    @BindView(R.id.articlesButton) Button articlesButton;
    @BindView(R.id.exercisesButton) Button exercisesButton;
    @BindView(R.id.disciplinesButton) Button disciplinesButton;
    @BindView(R.id.hallOfFameButton) Button hallOfFameButton;
    @BindView(R.id.knowledgeOfTheDayHolder) LinearLayout kOTDHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_knowledge_center_main, container, false);

        headerChanger("Knowledge Center");

        ButterKnife.bind(this, view);

        articlesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.knowledgeCenterHolder, new ArticlesMainFrag());

                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        exercisesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                //fragmentTransaction.replace(R.id.knowledgeCenterHolder, new ExercisesMainFrag());
                //fragmentTransaction.addToBackStack(null);
                //fragmentTransaction.commit();

                Intent intent = new Intent(getContext(), ExerciseLibraryActivity.class);
                startActivity(intent);
            }
        });

        disciplinesButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.knowledgeCenterHolder, new DisciplinesMainFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        hallOfFameButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.knowledgeCenterHolder, new HallOfFameMainFrag());
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
