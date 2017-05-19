package com.liftdom.template_editor;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.LogRecord;


public class DayOfWeekChildFrag extends android.app.Fragment implements ExerciseLevelChildFrag.doWCallback,
                ExerciseLevelChildFrag.removeFragCallback{


    //private OnFragmentInteractionListener mListener;

    boolean isFirstTime = false;

    int fragIdCount1 = 0;

    String isMon = null;
    String isTues = null;
    String isWed = null;
    String isThur = null;
    String isFri = null;
    String isSat = null;
    String isSun = null;

    Boolean isEdit = false;
    Boolean toastInvalidator = true;
    Boolean isAdded = false;
    Boolean isRemoved = false;
    String[] daysArray;
    String[][] doWArray1;
    String selectedDaysReference;
    HashMap<String, List<String>> map;
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



    public DayOfWeekChildFrag() {
        // Required empty public constructor
    }

    onDaySelectedListener mCallback;

    public interface onDaySelectedListener{
        public void daySelectedFromFrag(String doW, String tag);
        public void dayUnselectedFromFrag(String doW, String tag);
        public ArrayList<String> getSelectedDaysOtherThan(String tag);
    }


    public void daySelectedToFrag(String doW){
        setGreyChecked(doW);
    }

    public void dayUnselectedToFrag(String doW){
        setGreyUnChecked(doW);
    }


    public ArrayList<String> getSelectedDays(){
        ArrayList<String> selectedDays = new ArrayList<>();

        if(monToggle != null && tuesToggle != null && wedToggle != null && thurToggle != null
                && friToggle != null && satToggle != null && sunToggle != null){
            if(monToggle.isChecked()){
                selectedDays.add("Monday");
            }
            if(tuesToggle.isChecked()){
                selectedDays.add("Tuesday");
            }
            if(wedToggle.isChecked()){
                selectedDays.add("Wednesday");
            }
            if(thurToggle.isChecked()){
                selectedDays.add("Thursday");
            }
            if(friToggle.isChecked()){
                selectedDays.add("Friday");
            }
            if(satToggle.isChecked()){
                selectedDays.add("Saturday");
            }
            if(sunToggle.isChecked()){
                selectedDays.add("Sunday");
            }
        }

        return selectedDays;
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
                    mCallback.daySelectedFromFrag("Monday", getTag());
                }else{
                    monToggle.setBackgroundColor(Color.parseColor("#000000"));
                    monToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isMon = null;
                    mCallback.dayUnselectedFromFrag("Monday", getTag());
                }
            }
        });

        tuesToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    tuesToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    tuesToggle.setTextColor(Color.parseColor("#000000"));
                    isTues = "Tuesday";
                    mCallback.daySelectedFromFrag("Tuesday", getTag());
                }else{
                    tuesToggle.setBackgroundColor(Color.parseColor("#000000"));
                    tuesToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isTues = null;
                    mCallback.dayUnselectedFromFrag("Tuesday", getTag());
                }
            }
        });

        wedToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    wedToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    wedToggle.setTextColor(Color.parseColor("#000000"));
                    isWed = "Wednesday";
                    mCallback.daySelectedFromFrag("Wednesday", getTag());
                }else{
                    wedToggle.setBackgroundColor(Color.parseColor("#000000"));
                    wedToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isWed = null;
                    mCallback.dayUnselectedFromFrag("Wednesday", getTag());
                }
            }
        });

        thurToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    thurToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    thurToggle.setTextColor(Color.parseColor("#000000"));
                    isThur = "Thursday";
                    mCallback.daySelectedFromFrag("Thursday", getTag());
                }else{
                    thurToggle.setBackgroundColor(Color.parseColor("#000000"));
                    thurToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isThur = null;
                    mCallback.dayUnselectedFromFrag("Thursday", getTag());
                }
            }
        });

        friToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    friToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    friToggle.setTextColor(Color.parseColor("#000000"));
                    isFri = "Friday";
                    mCallback.daySelectedFromFrag("Friday", getTag());
                }else{
                    friToggle.setBackgroundColor(Color.parseColor("#000000"));
                    friToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isFri = null;
                    mCallback.dayUnselectedFromFrag("Friday", getTag());
                }
            }
        });

        satToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    satToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    satToggle.setTextColor(Color.parseColor("#000000"));
                    isSat = "Saturday";
                    mCallback.daySelectedFromFrag("Saturday", getTag());
                }else{
                    satToggle.setBackgroundColor(Color.parseColor("#000000"));
                    satToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isSat = null;
                    mCallback.dayUnselectedFromFrag("Saturday", getTag());
                }
            }
        });

        sunToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked){
                    sunToggle.setBackgroundColor(Color.parseColor("#BDA71A"));
                    sunToggle.setTextColor(Color.parseColor("#000000"));
                    isSun = "Sunday";
                    mCallback.daySelectedFromFrag("Sunday", getTag());
                }else{
                    sunToggle.setBackgroundColor(Color.parseColor("#000000"));
                    sunToggle.setTextColor(Color.parseColor("#FFFFFF"));
                    isSun = null;
                    mCallback.dayUnselectedFromFrag("Sunday", getTag());
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
                //Bundle fragTagBundle = new Bundle();
                //fragTagBundle.putString("fragTag", fragString1);
                frag1.fragTag = fragString1;
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                fragmentTransaction.commit();

                CharSequence toastText = "Exercise Added";
                int duration = Toast.LENGTH_SHORT;

                try{
                    Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                    snackbar.show();
                } catch (NullPointerException e){

                }
            }
        });


        if(isEdit && isFirstTime) {
            for (String day : daysArray) {
                if (day.equals("Monday")) {
                    monToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Monday", getTag());
                } else if (day.equals("Tuesday")) {
                    tuesToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Tuesday", getTag());
                } else if (day.equals("Wednesday")) {
                    wedToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Wednesday", getTag());
                } else if (day.equals("Thursday")) {
                    thurToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Thursday", getTag());
                } else if (day.equals("Friday")) {
                    friToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Friday", getTag());
                } else if (day.equals("Saturday")) {
                    satToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Saturday", getTag());
                } else if (day.equals("Sunday")) {
                    sunToggle.setChecked(true);
                    mCallback.daySelectedFromFrag("Sunday", getTag());
                }
            }
        }

        if(!isEdit){
            ++fragIdCount1;
            String fragString1 = Integer.toString(fragIdCount1);
            ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
            frag1.fragTag = fragString1;
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

        if(isAdded){
            for(String day : daysArray){
                if(day.equals("Monday")){
                    setGreyChecked("Monday");
                }else if(day.equals("Tuesday")){
                    setGreyChecked("Tuesday");
                }else if(day.equals("Wednesday")){
                    setGreyChecked("Wednesday");
                }else if(day.equals("Thursday")){
                    setGreyChecked("Thursday");
                }else if(day.equals("Friday")){
                    setGreyChecked("Friday");
                }else if(day.equals("Saturday")){
                    setGreyChecked("Saturday");
                }else if(day.equals("Sunday")){
                    setGreyChecked("Sunday");
                }
            }
        }


        if(isEdit && isFirstTime){

            if(isFirstTime){
                ArrayList<String> selectedDaysOtherThan = mCallback.getSelectedDaysOtherThan(getTag());
                for(String days : selectedDaysOtherThan){
                    setGreyChecked(days);
                }
            }

            for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                if (!entry.getKey().equals("0_key")) {
                    List<String> list = entry.getValue();
                    String fragString1 = Integer.toString(fragIdCount1);
                    ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

                    frag1.isEdit = true;
                    frag1.fragTag = fragString1;
                    frag1.fromEditList = list;
                    frag1.templateName = templateName;

                    fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                    if(!getActivity().isFinishing()){
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    ++fragIdCount1;
                }
            }
        }

    }

    public void removeFrag(String tag){

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(fragIdCount1 != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                --fragIdCount1;
            }
        }

        CharSequence toastText = "Exercise Removed";
        int duration = Toast.LENGTH_SHORT;

        try{
            Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
            snackbar.show();
        } catch (NullPointerException e){

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

    private void setGreyChecked(String doW){
        if(doW.equals("Monday")){
            if(monToggle != null){
                monToggle.setEnabled(false);
                monToggle.setBackgroundColor(Color.parseColor("#707070"));
                monToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Tuesday")){
            if(tuesToggle != null){
                tuesToggle.setEnabled(false);
                tuesToggle.setBackgroundColor(Color.parseColor("#707070"));
                tuesToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Wednesday")){
            if(wedToggle != null){
                wedToggle.setEnabled(false);
                wedToggle.setBackgroundColor(Color.parseColor("#707070"));
                wedToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Thursday")){
            if(thurToggle != null){
                thurToggle.setEnabled(false);
                thurToggle.setBackgroundColor(Color.parseColor("#707070"));
                thurToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Friday")){
            if(friToggle != null){
                friToggle.setEnabled(false);
                friToggle.setBackgroundColor(Color.parseColor("#707070"));
                friToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Saturday")){
            if(satToggle != null){
                satToggle.setEnabled(false);
                satToggle.setBackgroundColor(Color.parseColor("#707070"));
                satToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }else if(doW.equals("Sunday")){
            if(sunToggle != null){
                sunToggle.setEnabled(false);
                sunToggle.setBackgroundColor(Color.parseColor("#707070"));
                sunToggle.setTextColor(Color.parseColor("#E8E8E8"));
            }
        }
    }

    private void setGreyUnChecked(String doW){
        if(doW.equals("Monday")){
            monToggle.setEnabled(true);
            monToggle.setBackgroundColor(Color.parseColor("#000000"));
            monToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Tuesday")){
            tuesToggle.setEnabled(true);
            tuesToggle.setBackgroundColor(Color.parseColor("#000000"));
            tuesToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Wednesday")){
            wedToggle.setEnabled(true);
            wedToggle.setBackgroundColor(Color.parseColor("#000000"));
            wedToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Thursday")){
            thurToggle.setEnabled(true);
            thurToggle.setBackgroundColor(Color.parseColor("#000000"));
            thurToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Friday")){
            friToggle.setEnabled(true);
            friToggle.setBackgroundColor(Color.parseColor("#000000"));
            friToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Saturday")){
            satToggle.setEnabled(true);
            satToggle.setBackgroundColor(Color.parseColor("#000000"));
            satToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }else if(doW.equals("Sunday")){
            sunToggle.setEnabled(true);
            sunToggle.setBackgroundColor(Color.parseColor("#000000"));
            sunToggle.setTextColor(Color.parseColor("#FFFFFF"));
        }
    }

    public void setToNull(){
        monToggle.setChecked(false);
        isMon = null;

        isTues = null;
        tuesToggle.setChecked(false);

        isWed = null;
        wedToggle.setChecked(false);

        isThur = null;
        thurToggle.setChecked(false);

        isFri = null;
        friToggle.setChecked(false);

        isSat = null;
        satToggle.setChecked(false);

        isSun = null;
        sunToggle.setChecked(false);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (onDaySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        isFirstTime = false;
    }
}
