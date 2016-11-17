package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
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
import com.liftdom.liftdom.R;

import java.lang.reflect.Field;
import java.util.ArrayList;

import static android.R.attr.data;

public class ExerciseLevelChildFrag extends android.app.Fragment implements SetsLevelChildFrag.setSchemesCallback{

    int fragIdCount2 = 0;



    Boolean isEdit = false;
    String spinnerValue;
    ArrayList<String> setSchemeAList = new ArrayList<String>();
    String exerciseName;
    String templateName;
    String selectedDaysReference;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.addSet) Button addSet;
    @BindView(R.id.removeSet) Button removeSet;


    public ExerciseLevelChildFrag() {
        // Required empty public constructor
    }

    // Callback
    public interface doWCallback{
        String getDoW();
    }
    private doWCallback callback;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_level_child, container, false);

        ButterKnife.bind(this, view);

        final ArrayList<String> stringSnapshotAL = new ArrayList<>();

        if(isEdit){

            exerciseButton.setText(spinnerValue);

            DatabaseReference selectedDayRef = mRootRef.child("templates").child(uid).child
                    (templateName).child(selectedDaysReference);

            selectedDayRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot daySnapshot : dataSnapshot.getChildren()){
                        String stringSnapshot = daySnapshot.getValue(String.class);

                        stringSnapshotAL.add(stringSnapshot);

                    }

                    int specificExerciseIndex = stringSnapshotAL.indexOf(exerciseName);
                    // we need to somehow get the index of the next occurring exercise name
                    int arrayListLength = stringSnapshotAL.size();
                    for(int i = specificExerciseIndex; i < arrayListLength; i++){
                        if(!isExerciseName(stringSnapshotAL.get(i))){
                            ++fragIdCount2;
                            String fragString2 = Integer.toString(fragIdCount2);
                            SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            frag1.isEdit = true;
                            frag1.setSchemeEdited = stringSnapshotAL.get(i);
                            fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        if(!isEdit){
            String fragString2 = Integer.toString(fragIdCount2);
            SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
            fragmentTransaction.commit();
        }

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExercisePickerActivity.class);
                int exID = exerciseButton.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });



        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount2;
                FragmentManager fragmentManager = getFragmentManager();
                String fragString2 = Integer.toString(fragIdCount2);
                SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();
            }
        });

        removeSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String fragString2 = Integer.toString(fragIdCount2);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if(fragIdCount2 != 0){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString2)).commit();
                    --fragIdCount2;
                }
            }
        });

        callback = (doWCallback) getParentFragment();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(requestCode == 2)
        {
            if(data.getStringExtra("MESSAGE") != null) {
                String message = data.getStringExtra("MESSAGE");
                exerciseButton.setText(message);
            }
        }
    }

    @Override
    public void onResume(){
        super.onResume();
       //if(ExercisePickerController.getInstance().exID == exerciseButton.getId()) {
       //    exerciseButton.setText(ExercisePickerController.getInstance().exName);
       //}
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

    public String getDoWValue(){
        String doWSelected = callback.getDoW();
        return doWSelected;
    }

    public String getExerciseValue(){

        String spinnerText = exerciseButton.getText().toString();

        return spinnerText;
    }




}
