package com.liftdom.user_profile.stat_chart_stuff;

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
