package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class WorkoutFinishedFrag extends Fragment {


    public WorkoutFinishedFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.reviseWorkout) Button reviseWorkout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_workout_finished, container, false);

        ButterKnife.bind(this, view);

        reviseWorkout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                DatabaseReference runningRef = FirebaseDatabase.getInstance().getReference()
                        .child("runningAssistor").child(uid).child("assistorModel").child("isRevise");

                runningRef.setValue(true).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        AssistorHolderFrag assistorHolderFrag = new AssistorHolderFrag();
                        if (!getActivity().isFinishing()) {
                            try {
                                LinearLayout exInfoHolder = (LinearLayout) getView().findViewById(R.id
                                        .exInfoHolder);
                                fragmentTransaction.replace(exInfoHolder.getId(), assistorHolderFrag);
                                fragmentTransaction.commitAllowingStateLoss();
                            }catch (NullPointerException e){

                            }
                        }
                    }
                });
            }
        });

        return view;
    }

}
