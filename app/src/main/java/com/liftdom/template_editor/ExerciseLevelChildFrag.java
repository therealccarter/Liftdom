package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

public class ExerciseLevelChildFrag extends android.app.Fragment implements SetsLevelChildFrag.setSchemesCallback,
                SetsLevelChildFrag.removeFragCallback{

    int fragIdCount2 = 0;

    Boolean isEdit = false;
    Boolean toastInvalidator = true;
    String exerciseName;
    String templateName;
    String selectedDaysReference;

    String fragTag;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<String> algoExAL = new ArrayList<>();
    List<String> algorithmList = new ArrayList<>();

    public ExerciseLevelChildFrag() {
        // Required empty public constructor
    }

    // Callback
    public interface doWCallback{
        String getDoW();
    }

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private doWCallback callback;
    private removeFragCallback removeFragCallback;

    ArrayList<SetsLevelChildFrag> setsLevelChildFragAL = new ArrayList<>();

    ArrayList<String> stringSnapshotAL = new ArrayList<>();

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.addSet) Button addSet;
    @BindView(R.id.destroyFrag) ImageButton destroyFrag;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

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
                            //algoCheckBox.setChecked(true);
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
                frag1.fragTag = fragString2;
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();

                CharSequence toastText = "Set-scheme Added";
                int duration = Toast.LENGTH_SHORT;

                try{
                    Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                    snackbar.show();
                } catch (NullPointerException e){

                }
            }
        });

        removeFragCallback = (removeFragCallback) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFragCallback.removeFrag(fragTag);
                Log.i("tag", "fragbutton pushed");
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

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlgorithmOrSuperSetDialog.class);
                String exName = exerciseButton.getText().toString();
                intent.putExtra("exName", exName);
                startActivityForResult(intent, 3);
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
                    String newMessage = newLineExname(message);
                    exerciseButton.setText(newMessage);
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
            } else if(requestCode == 3){
                if(data.getStringExtra("choice") != null){
                    String message = data.getStringExtra("choice");
                    if(message.equals("algo")){
                        Intent intent = new Intent(getActivity(), AlgorithmSelectorActivity.class);
                        String exName = exerciseButton.getText().toString();
                        intent.putExtra("exName", exName);
                        if(getDoWValue() != null){
                            intent.putExtra("day", getDoWValue());
                        }
                        startActivityForResult(intent, 4);
                    }else if(message.equals("superset")){
                        Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                        String exName = exerciseButton.getText().toString();
                        intent.putExtra("exName", exName);
                        startActivityForResult(intent, 5);
                    }
                }
            } else if(requestCode == 4){
                if(data.getStringArrayListExtra("list") != null){
                    ArrayList<String> arrayList = data.getStringArrayListExtra("list");
                    if(!algorithmList.isEmpty()){
                        algorithmList.clear();
                    }
                    algorithmList = ((List<String>) arrayList);
                }

            } else if(requestCode == 5){

            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        if(EditTemplateAssemblerClass.getInstance().isOnSaveClick){
            if(!algorithmList.isEmpty()){
                algorithmList.add(getDoWValue());
                TemplateEditorSingleton.getInstance().mIsAlgorithm = true;
                TemplateEditorSingleton.getInstance().mAlgorithmInfo.put(getExerciseValueFormatted(), algorithmList);
            }
        }
    }

    public String newLineExname(String exerciseName){
        String newExNameString = "null";
        if(exerciseName.length() > 21){
            String exerciseName1 = exerciseName.substring(0, Math.min(exerciseName.length(), 21));
            String exerciseName2 = exerciseName.substring(20, exerciseName.length());
            newExNameString = exerciseName1 + "\n" + exerciseName2;
        } else{
            newExNameString = exerciseName;
        }
        return newExNameString;
    }

    public void removeFrag(String tag){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(fragIdCount2 != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                --fragIdCount2;
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

    private String getExerciseValueFormatted(){
        String spinnerText = exerciseButton.getText().toString();

        return spinnerText.replaceAll("\n", "");
    }





}
