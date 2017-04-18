package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

public class ExerciseLevelChildFrag extends android.app.Fragment implements SetsLevelChildFrag.setSchemesCallback{

    int fragIdCount2 = 0;

    Boolean isEdit = false;
    Boolean toastInvalidator = true;
    String exerciseName;
    String templateName;
    String selectedDaysReference;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.addSet) Button addSet;
    @BindView(R.id.removeSet) Button removeSet;
    @BindView(R.id.algorithmCheckBox) CheckBox algoCheckBox;

    ArrayList<String> algoExAL = new ArrayList<>();


    public ExerciseLevelChildFrag() {
        // Required empty public constructor
    }

    // Callback
    public interface doWCallback{
        String getDoW();
    }
    private doWCallback callback;

    ArrayList<SetsLevelChildFrag> setsLevelChildFragAL = new ArrayList<>();

    ArrayList<String> stringSnapshotAL = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_level_child, container, false);

        ButterKnife.bind(this, view);

        if(isEdit){
            DatabaseReference algoExercises = mRootRef.child("templates").child(uid).child(templateName).child
                    ("algorithmExercises");

            algoExercises.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String value = dataSnapshot1.getValue(String.class);
                        if(value.equals(exerciseName)){
                            algoCheckBox.setChecked(true);
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


            exerciseButton.setText(exerciseName);


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

                    Boolean isFirstEx = true;

                    for(int i = specificExerciseIndex; i < arrayListLength; i++){
                        if(isFirstEx) {
                            if (!isExerciseName(stringSnapshotAL.get(i))) {
                                ++fragIdCount2;
                                String fragString2 = Integer.toString(fragIdCount2);
                                SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                                frag1.isEdit = true;
                                frag1.setSchemeEdited = stringSnapshotAL.get(i);
                                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                                if (getActivity() != null) {
                                    fragmentTransaction.commitAllowingStateLoss();
                                }
                            } else if(!stringSnapshotAL.get(i).equals(exerciseName)){
                                isFirstEx = false;
                            }
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
            setsLevelChildFragAL.add(frag1);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
            fragmentTransaction.commit();
        }

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount2;
                FragmentManager fragmentManager = getFragmentManager();
                String fragString2 = Integer.toString(fragIdCount2);
                SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                setsLevelChildFragAL.add(frag1);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();

                CharSequence toastText = "(+) Set-scheme added";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), toastText, duration);
                toast.show();
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

                CharSequence toastText = "(-) Set-scheme removed";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(getActivity(), toastText, duration);
                toast.show();
            }
        });

        algoCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CharSequence toastText = "(+) Added to Algorithm List";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), toastText, duration);
                    toast.show();
                } else {
                    CharSequence toastText = "(-) Removed from Algorithm List";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getActivity(), toastText, duration);
                    toast.show();
                }
            }
        });

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                int exID = exerciseButton.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });

        callback = (doWCallback) getParentFragment();

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
                    exerciseButton.setText(message);
                    if (message.equals("Pullups") || message.equals("Crunches")
                            || message.equals("Sit-ups")
                            || message.equals("Leg Raises")) {
                        if (!setsLevelChildFragAL.isEmpty()) {
                            setsLevelChildFragAL.get(0).weightEditText.setText("B.W.");
                            setsLevelChildFragAL.get(0).pounds.setVisibility(View.GONE);
                            setsLevelChildFragAL.get(0).weightEditText.setEnabled(false);
                        }
                    }
                }
            }
        }
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

    public Boolean getCheckBoxValue(){

        Boolean isChecked = algoCheckBox.isChecked();

        return isChecked;
    }




}
