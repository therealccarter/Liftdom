package com.liftdom.template_editor;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DayOfWeekChildFrag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 */
public class DayOfWeekChildFrag extends android.app.Fragment implements ExerciseLevelChildFrag.doWCallback{

    private OnFragmentInteractionListener mListener;

    int fragIdCount1 = 0;

    String isMon = null;
    String isTues = null;
    String isWed = null;
    String isThur = null;
    String isFri = null;
    String isSat = null;
    String isSun = null;

    Boolean isEdit = false;
    String[] daysArray;
    String[][] doWArray1;
    String selectedDaysReference;
    String templateName;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // Butterknife
    @BindView(R.id.M) ToggleButton monToggle;
    @BindView(R.id.Tu) ToggleButton tuesToggle;
    @BindView(R.id.W) ToggleButton wedToggle;
    @BindView(R.id.Th) ToggleButton thurToggle;
    @BindView(R.id.F) ToggleButton friToggle;
    @BindView(R.id.Sa) ToggleButton satToggle;
    @BindView(R.id.Su) ToggleButton sunToggle;
    @BindView(R.id.addExercise) Button addExercise;
    @BindView(R.id.removeExercise) Button removeExercise;


    public DayOfWeekChildFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_of_week_child, container, false);

        ButterKnife.bind(this, view);

        monToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    monToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    monToggle.setTextColor(Color.parseColor("#000000"));
                    isMon = "Monday";
                }else{
                    monToggle.setBackgroundColor(Color.parseColor("#000000"));
                    monToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isMon = null;
                }
            }
        });

        tuesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    tuesToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    tuesToggle.setTextColor(Color.parseColor("#000000"));
                    isTues = "Tuesday";
                }else{
                    tuesToggle.setBackgroundColor(Color.parseColor("#000000"));
                    tuesToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isTues = null;
                }
            }
        });

        wedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    wedToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    wedToggle.setTextColor(Color.parseColor("#000000"));
                    isWed = "Wednesday";
                }else{
                    wedToggle.setBackgroundColor(Color.parseColor("#000000"));
                    wedToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isWed = null;
                }
            }
        });

        thurToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    thurToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    thurToggle.setTextColor(Color.parseColor("#000000"));
                    isThur = "Thursday";
                }else{
                    thurToggle.setBackgroundColor(Color.parseColor("#000000"));
                    thurToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isThur = null;
                }
            }
        });

        friToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    friToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    friToggle.setTextColor(Color.parseColor("#000000"));
                    isFri = "Friday";
                }else{
                    friToggle.setBackgroundColor(Color.parseColor("#000000"));
                    friToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isFri = null;
                }
            }
        });

        satToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    satToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    satToggle.setTextColor(Color.parseColor("#000000"));
                    isSat = "Saturday";
                }else{
                    satToggle.setBackgroundColor(Color.parseColor("#000000"));
                    satToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isSat = null;
                }
            }
        });

        sunToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sunToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    sunToggle.setTextColor(Color.parseColor("#000000"));
                    isSun = "Sunday";
                }else{
                    sunToggle.setBackgroundColor(Color.parseColor("#000000"));
                    sunToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isSun = null;
                }
            }
        });

        // END BUTTON BAR TOGGLE

        addExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                ++fragIdCount1;
                String fragString1 = Integer.toString(fragIdCount1);
                ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                fragmentTransaction.commit();
            }
        });

        removeExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                String fragString1 = Integer.toString(fragIdCount1);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                if(fragIdCount1 != 0){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString1)).commit();
                    --fragIdCount1;
                }
            }
        });

        if(!isEdit){
            ++fragIdCount1;
            String fragString1 = Integer.toString(fragIdCount1);
            ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
            fragmentTransaction.commit();
        }

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();


        // BEGINS BUTTON BAR TOGGLE
        final ToggleButton monToggle = (ToggleButton) getView().findViewById(R.id.M);
        final ToggleButton tuesToggle = (ToggleButton) getView().findViewById(R.id.Tu);
        final ToggleButton wedToggle = (ToggleButton) getView().findViewById(R.id.W);
        final ToggleButton thurToggle = (ToggleButton) getView().findViewById(R.id.Th);
        final ToggleButton friToggle = (ToggleButton) getView().findViewById(R.id.F);
        final ToggleButton satToggle = (ToggleButton) getView().findViewById(R.id.Sa);
        final ToggleButton sunToggle = (ToggleButton) getView().findViewById(R.id.Su);


        if(isEdit){
            for(String day : daysArray){
                if(day.equals("Monday")){
                    monToggle.setChecked(true);
                }else if(day.equals("Tuesday")){
                    tuesToggle.setChecked(true);
                }else if(day.equals("Wednesday")){
                    wedToggle.setChecked(true);
                }else if(day.equals("Thursday")){
                    thurToggle.setChecked(true);
                }else if(day.equals("Friday")){
                    friToggle.setChecked(true);
                }else if(day.equals("Saturday")){
                    satToggle.setChecked(true);
                }else if(day.equals("Sunday")){
                    sunToggle.setChecked(true);
                }
            }



            DatabaseReference selectedDayRef = mRootRef.child("users").child(uid).child("templates").child
                    (templateName).child(selectedDaysReference);

            selectedDayRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    for(DataSnapshot daySnapshot : dataSnapshot.getChildren()){
                        String snapshotString = daySnapshot.getValue(String.class);

                        if(isExerciseName(snapshotString)){
                            ++fragIdCount1;
                            String fragString1 = Integer.toString(fragIdCount1);
                            ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

                            frag1.isEdit = true;
                            frag1.spinnerValue = snapshotString;
                            frag1.exerciseName = snapshotString;
                            frag1.selectedDaysReference = selectedDaysReference;
                            frag1.templateName = templateName;

                            fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });


        }

    }

    boolean isExerciseName(String input){
        String[] tokens = input.split("");

        boolean isExercise = true;

        char c = tokens[1].charAt(0);
        if(Character.isDigit(c)){
            isExercise = false;
        }

        return isExercise;

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public String getDoW(){
        String doWConstructor = "";

        if(isMon != null){
            doWConstructor = doWConstructor + "Monday_";
        }if(isTues != null){
            doWConstructor = doWConstructor + "Tuesday_";
        }if(isWed != null){
            doWConstructor = doWConstructor + "Wednesday_";
        }if(isThur != null){
            doWConstructor = doWConstructor + "Thursday_";
        }if(isFri != null){
            doWConstructor = doWConstructor + "Friday_";
        }if(isSat != null){
            doWConstructor = doWConstructor + "Saturday_";
        }if(isSun != null){
            doWConstructor = doWConstructor + "Sunday_";
        }

        return doWConstructor;

    }
}
