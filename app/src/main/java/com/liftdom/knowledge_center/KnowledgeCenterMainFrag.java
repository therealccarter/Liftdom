package com.liftdom.knowledge_center;


import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class KnowledgeCenterMainFrag extends Fragment {


    public KnowledgeCenterMainFrag() {
        // Required empty public constructor
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
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.knowledgeCenterHolder, new ExercisesMainFrag());
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
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

}
