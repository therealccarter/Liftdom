package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutHistoryListFrag extends Fragment {


    public WorkoutHistoryListFrag() {
        // Required empty public constructor
    }

    String date = "fail";
    ArrayList<String> initialDataList = new ArrayList<>();
    DatabaseReference daySpecificRef;

    @BindView(R.id.dateTitle) TextView dateTitle;
    @BindView(R.id.dataHolder) LinearLayout dataHolder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_history_list, container, false);

        ButterKnife.bind(this, view);

        dateTitle.setText(date);


        //TODO: Add date converter

        daySpecificRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot dateSnapshot : dataSnapshot.getChildren()) {

                    String itemString = dateSnapshot.getValue(String.class);

                    String itemKey = dateSnapshot.getKey();

                    if (isExerciseName(itemString) && !itemKey.equals("private_journal") && !itemKey.equals
                            ("restDay")) {
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HistoryExerciseNameFrag exerciseNameFrag = new HistoryExerciseNameFrag();
                        fragmentTransaction.add(R.id.dataHolder,
                                exerciseNameFrag);
                        fragmentTransaction.commit();
                        exerciseNameFrag.exerciseName = itemString;
                    } else if(!isExerciseName(itemString)){
                        String stringSansSpaces = itemString.replaceAll("\\s+", "");

                        String delims = "[x,@]";

                        String[] tokens = stringSansSpaces.split(delims);

                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HistoryRepsWeightFrag repsWeightFrag = new HistoryRepsWeightFrag();
                        fragmentTransaction.add(R.id.dataHolder,
                                repsWeightFrag);
                        fragmentTransaction.commit();
                        repsWeightFrag.reps = tokens[0];
                        repsWeightFrag.weight = tokens[1];
                    } else if(itemKey.equals("private_journal") && !itemString.equals("")){
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HistoryPrivateJournalFrag historyPrivateJournalFrag = new HistoryPrivateJournalFrag();
                        historyPrivateJournalFrag.journalString = itemString;
                        fragmentTransaction.add(R.id.dataHolder,
                                historyPrivateJournalFrag);
                        fragmentTransaction.commit();
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

}
