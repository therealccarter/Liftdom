package com.liftdom.template_editor;

import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
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

        return view;
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
            doWConstructor = doWConstructor + "Monday//";
        }if(isTues != null){
            doWConstructor = doWConstructor + "Tuesday//";
        }if(isWed != null){
            doWConstructor = doWConstructor + "Wednesday//";
        }if(isThur != null){
            doWConstructor = doWConstructor + "Thursday//";
        }if(isFri != null){
            doWConstructor = doWConstructor + "Friday//";
        }if(isSat != null){
            doWConstructor = doWConstructor + "Saturday//";
        }if(isSun != null){
            doWConstructor = doWConstructor + "Sunday//";
        }

        return doWConstructor;

    }
}
