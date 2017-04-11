package com.liftdom.workout_programs.PPL;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.TemplateHousingActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLFinishedFrag extends Fragment {


    public PPLFinishedFrag() {
        // Required empty public constructor
    }

    List<String> list1 = new ArrayList<>();
    List<String> list2 = new ArrayList<>();
    List<String> list3 = new ArrayList<>();
    List<String> list4 = new ArrayList<>();
    List<String> list5 = new ArrayList<>();
    List<String> list6 = new ArrayList<>();
    List<String> algList = new ArrayList<>();
    List<String> algeExList = new ArrayList<>();

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

    // Butterknife binds
    @BindView(R.id.goBackHome) Button goHome;
    @BindView(R.id.goBackToTemplates) Button goToTemplates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplfinished, container, false);

        ButterKnife.bind(this, view);

        goHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        goToTemplates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), TemplateHousingActivity.class);
                startActivity(intent);
            }
        });

        PPLClass pplClass = new PPLClass(benchMax, deadliftMax, squatMax);

        ArrayList<String> PushA = pplClass.getPushA();
        ArrayList<String> PullA = pplClass.getPullA();
        ArrayList<String> LegsA = pplClass.getLegsA();
        ArrayList<String> PushB = pplClass.getPushB();
        ArrayList<String> PullB = pplClass.getPullB();
        ArrayList<String> LegsB = pplClass.getLegsB();

        for(String value : PushA){
            list1.add(value);
        }

        for(String value : PullA){
            list2.add(value);
        }

        for(String value : LegsA){
            list3.add(value);
        }

        for(String value : PushB){
            list4.add(value);
        }

        for(String value : PullB){
            list5.add(value);
        }

        for(String value : LegsB){
            list6.add(value);
        }

        algList.add("0");
        algList.add("0");
        algList.add("1");
        algList.add("1");
        algList.add("3");
        algList.add("10");
        algList.add("loop");
        //TODO: See if algorithm added weights are rounded

        algeExList.add("Bench Press (Barbell - Flat)");
        algeExList.add("Overhead Press (Dumbbell)");
        algeExList.add("Tricep Extension (Barbell - Overhead)");
        algeExList.add("Overhead Press (Barbell)");
        algeExList.add("Bench Press (Dumbbell - Incline)");
        algeExList.add("Tricep Extension (Machine)");
        algeExList.add("Deadlift (Barbell - Conventional)");
        algeExList.add("Row (Dumbbell - Bent-over)");
        algeExList.add("Curl (Dumbbell - Standing)");
        algeExList.add("Curl (Barbell - Standing)");
        algeExList.add("Deadlift (Barbell - Stiff-legged)");
        algeExList.add("Row (Barbell - Bent-over)");
        algeExList.add("Squat (Barbell - Back)");
        algeExList.add("Leg Press");
        algeExList.add("Leg Extension");
        algeExList.add("Leg Curl");
        algeExList.add("Squat (Barbell - Front)");

        DatabaseReference templatesRef = mRootRef.child("templates").child(uid);

        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                int pplInc = 1;
                int inc = 0;

                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();

                    try{
                        if(key.substring(0,12).equals("PushPullLegs")){
                            pplInc++;
                        }
                    } catch (StringIndexOutOfBoundsException e){
                        Log.i("info", "index out of bounds");
                    }

                    inc++;
                    if(inc == dataSnapshot.getChildrenCount()){
                        DatabaseReference templateRef = mRootRef.child("templates").child(uid).child("PushPullLegs" +
                                String.valueOf(pplInc));

                        templateRef.child("Monday").setValue(list1);
                        templateRef.child("Tuesday").setValue(list2);
                        templateRef.child("Wednesday").setValue(list3);
                        templateRef.child("Thursday").setValue(list4);
                        templateRef.child("Friday").setValue(list5);
                        templateRef.child("Saturday").setValue(list6);
                        templateRef.child("algorithm").setValue(algList);
                        templateRef.child("algorithmExercises").setValue(algeExList);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }


}
