package com.liftdom.user_profile.calendar_stuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.WorkoutInfoRecyclerAdapter;
import com.liftdom.liftdom.utils.SimpleExNameFrag;
import com.liftdom.liftdom.utils.WorkoutHistoryModelClass;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastDateDialogSubFrag extends Fragment {


    public PastDateDialogSubFrag() {
        // Required empty public constructor
    }

    public WorkoutHistoryModelClass workoutHistoryModelClass;

    @BindView(R.id.infoRecyclerView) RecyclerView infoRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_date_dialog_sub, container, false);

        ButterKnife.bind(this, view);

        HashMap<String, List<String>> map = new HashMap<>();

        if(workoutHistoryModelClass.getWorkoutInfoMap() == null){
            TextView restDayView = (TextView) view.findViewById(R.id.restDayView);
            restDayView.setVisibility(View.VISIBLE);
        }else{
            map.putAll(workoutHistoryModelClass.getWorkoutInfoMap());

            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, getContext());
            adapter.setIsOriginallyImperial(workoutHistoryModelClass.isIsImperial());
            //adapter.setInfoList(workoutInfoMap);
            infoRecyclerView.setAdapter(adapter);
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        }


        return view;
    }

}
