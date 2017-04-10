package com.liftdom.workout_programs.PPL;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLFinishedFrag extends Fragment {


    public PPLFinishedFrag() {
        // Required empty public constructor
    }

    String benchMax;
    String deadliftMax;
    String squatMax;

    /**
     * PushA.add("Monday");
     PullA.add("Tuesday");
     LegsA.add("Wednesday");
     PushB.add("Thursday");
     PullB.add("Friday");
     LegsB.add("Saturday");
     */

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplfinished, container, false);

        PPLClass pplClass = new PPLClass(benchMax, deadliftMax, squatMax);

        ArrayList<String> PushA = pplClass.getPushA();
        ArrayList<String> PullA = pplClass.getPullA();
        ArrayList<String> LegsA = pplClass.getLegsA();
        ArrayList<String> PushB = pplClass.getPushB();
        ArrayList<String> PullB = pplClass.getPullB();
        ArrayList<String> LegsB = pplClass.getLegsB();

        List<String> list1 = new ArrayList<>();
        for(String value : PushA){
            list1.add(value);
        }

        List<String> list2 = new ArrayList<>();
        for(String value : PullA){
            list2.add(value);
        }

        List<String> list3 = new ArrayList<>();
        for(String value : LegsA){
            list3.add(value);
        }

        List<String> list4 = new ArrayList<>();
        for(String value : PushB){
            list4.add(value);
        }

        List<String> list5 = new ArrayList<>();
        for(String value : PullB){
            list5.add(value);
        }

        List<String> list6 = new ArrayList<>();
        for(String value : LegsB){
            list6.add(value);
        }

        DatabaseReference templateRef = mRootRef.child("templates").child(uid).child("PushPullLegs");
        templateRef.child("Monday").setValue(list1);
        templateRef.child("Tuesday").setValue(list2);
        templateRef.child("Wednesday").setValue(list3);
        templateRef.child("Thursday").setValue(list4);
        templateRef.child("Friday").setValue(list5);
        templateRef.child("Saturday").setValue(list6);

        return view;
    }

}
