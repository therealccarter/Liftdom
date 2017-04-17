package com.liftdom.workout_programs.Smolov;


import android.content.Intent;
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
import com.liftdom.charts.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovSetupFrag extends Fragment {


    public SmolovSetupFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.finishButton) Button finishButton;
    @BindView(R.id.oneRepMaxEditText) EditText oneRepMaxEditText;
    @BindView(R.id.movementName) Button movementName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_setup, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState == null){
            DatabaseReference squatMaxRef = mRootRef.child("users").child(uid).child("maxes").child("squatMax");

            squatMaxRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists()){
                        String value = dataSnapshot.getValue(String.class);
                        oneRepMaxEditText.setText(value);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        finishButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                SmolovFinishedFrag smolovFinishedFrag = new SmolovFinishedFrag();
                smolovFinishedFrag.OneRepMax = oneRepMaxEditText.getText()
                        .toString();
                smolovFinishedFrag.ExName = movementName.getText().toString();
                if(activeTemplateCheckbox.isChecked()){
                    smolovFinishedFrag.isActive = true;
                }
                fragmentTransaction.replace(R.id.smolovFragHolder, smolovFinishedFrag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentTransaction.addToBackStack(null);
            }
        });

        movementName.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                int exID = movementName.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(data != null){
            if (requestCode == 2) {
                if (data.getStringExtra("MESSAGE") != null) {
                    String message = data.getStringExtra("MESSAGE");
                    movementName.setText(message);
                }
            }
        }
    }

}
