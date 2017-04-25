package com.liftdom.workout_programs.Smolov;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import org.joda.time.LocalDate;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovFinishedFrag extends Fragment {


    public SmolovFinishedFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String OneRepMax;
    String ExName;
    Boolean isActive = false;

    @BindView(R.id.goHome) Button goHome;
    @BindView(R.id.goBackToTemplates) Button goToTemplates;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_finished, container, false);

        ButterKnife.bind(this, view);

        final DatabaseReference templateRef = mRootRef.child("templates").child(uid);

        final DatabaseReference smolovRef = mRootRef.child("templates").child(uid).child("Smolov");

        smolovRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    templateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                String templateName = dataSnapshot1.getKey();

                                if(templateName.length() >= 6){
                                    String firstSix = templateName.substring(0,6);
                                    if(firstSix.equals("Smolov")){
                                        if(templateName.length() > 6){
                                            String numbers = templateName.substring(6, templateName.length());
                                            int number = Integer.parseInt(numbers);
                                            number += 1;
                                            String newNumber = Integer.toString(number);
                                            final String newName = "Smolov" + newNumber;

                                            final DatabaseReference smolovRef1 = mRootRef.child("templates").child(uid).child
                                                    (newName);
                                            smolovRef1.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    smolovRef1.child("1rm").setValue(OneRepMax);
                                                    smolovRef1.child("exName").setValue(ExName);
                                                    smolovRef1.child("timeStamp").setValue(LocalDate.now().toString());
                                                    smolovRef1.child("id").setValue("Smolov");

                                                    if(isActive){
                                                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                                                ("active_template");
                                                        activeTemplateRef.setValue(newName);
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }else{

                                            final DatabaseReference smolovRef2 = mRootRef.child("templates").child(uid).child
                                                    ("Smolov2");
                                            smolovRef2.addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                    smolovRef2.child("1rm").setValue(OneRepMax);
                                                    smolovRef2.child("exName").setValue(ExName);
                                                    smolovRef2.child("timeStamp").setValue(LocalDate.now().toString());
                                                    smolovRef2.child("id").setValue("Smolov");

                                                    if(isActive){
                                                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                                                ("active_template");
                                                        activeTemplateRef.setValue("Smolov2");
                                                    }
                                                }

                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                        }

                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }else{
                    smolovRef.child("1rm").setValue(OneRepMax);
                    smolovRef.child("exName").setValue(ExName);
                    smolovRef.child("timeStamp").setValue(LocalDate.now().toString());
                    smolovRef.child("id").setValue("Smolov");

                    if(isActive){
                        DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                                ("active_template");
                        activeTemplateRef.setValue("Smolov");
                    }

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                intent.putExtra("fragID", 0);
                startActivity(intent);
            }
        });

        return view;
    }

}
