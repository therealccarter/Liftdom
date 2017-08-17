package com.liftdom.user_profile.calendar_stuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import com.liftdom.liftdom.R;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_date_dialog_sub, container, false);

        HashMap<String, List<String>> map = new HashMap<>();

        if(workoutHistoryModelClass.getWorkoutInfoMap() == null){
            TextView restDayView = (TextView) view.findViewById(R.id.restDayView);
            restDayView.setVisibility(View.VISIBLE);
        }
        map.putAll(workoutHistoryModelClass.getWorkoutInfoMap());

        String delims = "[_]";

        for(int i = 0; i < map.size(); i++){
            for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
                String[] keyTokens = mapEntry.getKey().split(delims);
                if(keyTokens[0].equals(String.valueOf(i + 1))){
                    FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                    SimpleExNameFrag exNameFrag = new SimpleExNameFrag();
                    exNameFrag.isPastDate = true;
                    exNameFrag.exInfoList = mapEntry.getValue();
                    fragmentTransaction.add(R.id.exInfoSubHolder, exNameFrag);
                    fragmentTransaction.commit();
                }
            }
        }

        return view;
    }

}
