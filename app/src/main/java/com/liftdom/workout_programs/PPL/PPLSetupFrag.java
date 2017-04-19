package com.liftdom.workout_programs.PPL;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLSetupFrag extends Fragment {


    public PPLSetupFrag() {
        // Required empty public constructor
    }

    String benchMax;
    String deadliftMax;
    String squatMax;

    @BindView(R.id.benchPress1rm) EditText benchPress1rm;
    @BindView(R.id.deadlift1rm) EditText deadlift1rm;
    @BindView(R.id.squat1rm) EditText squat1rm;
    @BindView(R.id.finishButton) Button finishButton;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplsetup, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference maxesRef = mRootRef.child("users").child(uid).child("maxes");

        maxesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    String value = dataSnapshot1.getValue(String.class);

                    if(key.equals("benchMax")){
                        benchPress1rm.setText(value);
                        benchMax = value;
                    }else if(key.equals("deadliftMax")){
                        deadlift1rm.setText(value);
                        deadliftMax = value;
                    }else if(key.equals("squatMax")){
                        squat1rm.setText(value);
                        squatMax = value;
                    }

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                PPLFinishedFrag pplFinishedFrag = new PPLFinishedFrag();
                pplFinishedFrag.benchMax = benchMax;
                pplFinishedFrag.squatMax = squatMax;
                pplFinishedFrag.deadliftMax = deadliftMax;
                if(activeTemplateCheckbox.isChecked()){
                    pplFinishedFrag.isActive = true;
                }
                fragmentTransaction.replace(R.id.pplFragHolder, pplFinishedFrag);
                fragmentTransaction.commitAllowingStateLoss();
            }
        });

        return view;
    }

}
