package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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

    @BindView(R.id.dateTitle) TextView dateTitle;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_history_list, container, false);

        ButterKnife.bind(this, view);

        dateTitle.setText(date);

        LinearLayout itemHolder = (LinearLayout) view.findViewById(R.id.dataHolder);

        for(String item : initialDataList){
            TextView itemView = new TextView(getContext());
            itemView.setText(item);
            itemHolder.addView(itemView);
        }


        return view;
    }

}
