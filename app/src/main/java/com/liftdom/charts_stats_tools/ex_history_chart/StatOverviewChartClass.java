package com.liftdom.charts_stats_tools.ex_history_chart;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * Created by Brodin on 3/13/2017.
 */

public class StatOverviewChartClass {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private ArrayList<ValueAndDateObject> OverviewStatValues = new ArrayList<>();

    public ArrayList<ValueAndDateObject> getOverviewStatValues(){


        return OverviewStatValues;
    }
}
