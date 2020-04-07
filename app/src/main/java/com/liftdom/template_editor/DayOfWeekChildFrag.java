package com.liftdom.template_editor;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import com.google.android.material.snackbar.Snackbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DayOfWeekChildFrag extends android.app.Fragment
        implements ExerciseLevelChildFrag.doWCallback,
                ExerciseLevelChildFrag.removeFragCallback{


    //private OnFragmentInteractionListener mListener;

    boolean isFirstTime = false;

    boolean firstTimeTut = false;

    int fragIdCount1 = 0;

    String isMon = null;
    String isTues = null;
    String isWed = null;
    String isThur = null;
    String isFri = null;
    String isSat = null;
    String isSun = null;

    Boolean isEdit = false;
    Boolean isAdded = false;
    String[] daysArray;
    HashMap<String, List<String>> map;
    String templateName;

    ArrayList<ExerciseLevelChildFrag> exLevelFragList = new ArrayList<>();

    onDaySelectedListener mCallback;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public DayOfWeekChildFrag() {
        // Required empty public constructor
    }

    public interface onDaySelectedListener{
        void daySelectedFromFrag(String doW, String tag);
        void dayUnselectedFromFrag(String doW, String tag);
        ArrayList<String> getSelectedDaysOtherThan(String tag);
    }

    public void setDoWInfo(){
        for(ExerciseLevelChildFrag exerciseLevelChildFrag : exLevelFragList){
            exerciseLevelChildFrag.setExerciseInfo();
        }
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

    boolean hideShowBool = true;

    // Butterknife
    @BindView(R.id.M) ToggleButton monToggle;
    @BindView(R.id.Tu) ToggleButton tuesToggle;
    @BindView(R.id.W) ToggleButton wedToggle;
    @BindView(R.id.Th) ToggleButton thurToggle;
    @BindView(R.id.F) ToggleButton friToggle;
    @BindView(R.id.Sa) ToggleButton satToggle;
    @BindView(R.id.Su) ToggleButton sunToggle;
    @BindView(R.id.addExercise) Button addExercise;
    @BindView(R.id.buttonBar) LinearLayout buttonbar;
    @BindView(R.id.hideShow) LinearLayout hideShow;
    @BindView(R.id.daySetLL) LinearLayout daySetLL;
    @BindView(R.id.hideShowText) TextView hideShowText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_day_of_week_child, container, false);

        ButterKnife.bind(this, view);

        hideShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(hideShowBool){
                    hideShowText.setText("(+) Show Day Set");
                    daySetLL.setVisibility(View.GONE);
                    hideShowBool = false;
                }else{
                    hideShowText.setText("(-) Hide Day Set");
                    daySetLL.setVisibility(View.VISIBLE);
                    hideShowBool = true;
                }
            }
        });

        monToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                if(timer != null){
                    timer.cancel();
                }
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
                frag1.fragTag = fragString1;
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                exLevelFragList.add(frag1);
                fragmentTransaction.commit();

                CharSequence toastText = "Exercise Added";
                int duration = Snackbar.LENGTH_SHORT;

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
            exLevelFragList.add(frag1);
            fragmentTransaction.commit();
        }


        if(getSelectedDays().size() == 0){
            try{
                Snackbar.make(getActivity().getCurrentFocus(), "Click on the days you want from the button bar.",
                        Snackbar.LENGTH_SHORT).show();
            }catch (NullPointerException e){

            }
            flashAddDay();
        }

        return view;
    }

    boolean isBlack = false;

    CountDownTimer timer;

    private void flashAddDay(){
        if(timer != null){
            timer.cancel();
        }
        timer = new CountDownTimer(2000, 150) {
            @Override
            public void onTick(long l) {
                if(isBlack){
                    buttonbar.setBackgroundColor(Color.parseColor("#000000"));
                    isBlack = false;
                }else{
                    buttonbar.setBackgroundColor(Color.parseColor("#303030"));
                    isBlack = true;
                }
            }

            @Override
            public void onFinish() {
                buttonbar.setBackgroundColor(Color.parseColor("#000000"));
            }
        }.start();
    }

    @Override
    public void onResume(){
        super.onResume();

        if(TemplateEditorSingleton.getInstance().isFirstTimeTut){
            Button exButton = (Button) exLevelFragList.get(0).getView().findViewById(R.id.movementName);
            ImageView exExtraOptions = (ImageView) exLevelFragList.get(0).getView().findViewById(R.id
                    .extraOptionsButton);
            LinearLayout setsLevelLL = (LinearLayout) exLevelFragList.get(0).setsLevelChildFragAL.get(0).getView()
                    .findViewById(R.id.setsLevelParentLL);

            FancyShowCaseView fancyShowCaseView = new FancyShowCaseView.Builder(getActivity())
                    .title("The days of week toggle bar is where you select the days you'd like to do the exercises " +
                            "you want. (Any combination of M/Tu/W/Th/F/Sa/Su)" +
                            " \n \n You can add up to 7 days (each day of the week), " +
                            "and that schedule will repeat for as long as you want.")
                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER)
                    .build();
            FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
                    .focusOn(exButton)
                    .title("You can click here to select the exercise that you want.")
                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER)
                    .focusShape(FocusShape.ROUNDED_RECTANGLE)
                    .fitSystemWindows(true)
                    .build();
            FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
                    .focusOn(exExtraOptions)
                    .title("This is where you can either add a superset exercise or set a custom algorithm (auto " +
                            "increasing reps/sets/weight).")
                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER)
                    .fitSystemWindows(true)
                    .build();
            FancyShowCaseView fancyShowCaseView3 = new FancyShowCaseView.Builder(getActivity())
                    .focusOn(setsLevelLL)
                    .title("The format for set schemes is \n Sets x Reps @ Weight. \n \n The left option menu here is" +
                            " for changing reps to and from 'to failure'/numerical formats, and the weight to and from " +
                            "body-weight/numerical formats.")
                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER | Gravity.BOTTOM)
                    .focusShape(FocusShape.ROUNDED_RECTANGLE)
                    .fitSystemWindows(true)
                    .build();
            FancyShowCaseView fancyShowCaseView4 = new FancyShowCaseView.Builder(getActivity())
                    .title("Good luck!")
                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
                    .build();

            new FancyShowCaseQueue()
                    .add(fancyShowCaseView)
                    .add(fancyShowCaseView1)
                    .add(fancyShowCaseView2)
                    .add(fancyShowCaseView3)
                    .add(fancyShowCaseView4)
                    .show();

            DatabaseReference firstTimeRef = mRootRef.child("firstTime").child(uid).child("isFromScratchFirstTime");
            firstTimeRef.setValue(null);

            firstTimeTut = false;
            TemplateEditorSingleton.getInstance().isFirstTimeTut = false;
        }
    }


    @Override
    public void onStart() {
        super.onStart();

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

            for(int i = 0; i < map.size(); i++){
                for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                    if (!entry.getKey().equals("0_key")) {
                        String[] tokens = entry.getKey().split("_");
                        int keyInt = Integer.parseInt(tokens[0]);
                        if(keyInt == i){
                            List<String> list = entry.getValue();
                            String fragString1 = Integer.toString(fragIdCount1);
                            ExerciseLevelChildFrag frag1 = new ExerciseLevelChildFrag();
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();

                            frag1.isEdit = true;
                            frag1.fragTag = fragString1;
                            frag1.fromEditList = list;
                            frag1.templateName = templateName;
                            frag1.editInitialDays = getDowForEdit(daysArray);

                            fragmentTransaction.add(R.id.exerciseFragmentLayout, frag1, fragString1);
                            if(!getActivity().isFinishing()){
                                exLevelFragList.add(frag1);
                                fragmentTransaction.commitAllowingStateLoss();
                            }
                            ++fragIdCount1;
                        }
                    }
                }
            }
        }

    }

    public void setToGold(){
        for(ExerciseLevelChildFrag childFrag : exLevelFragList){
            childFrag.setToGoldFromDoW();
        }
    }

    public void removeGold(){
        for(ExerciseLevelChildFrag childFrag : exLevelFragList){
            childFrag.removeGoldFromDoW();
        }
    }

    public void removeFrag(String tag){

        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(fragIdCount1 != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                ExerciseLevelChildFrag exerciseLevelChildFrag1  = new ExerciseLevelChildFrag();
                for(ExerciseLevelChildFrag exerciseLevelChildFrag : exLevelFragList){
                    if(exerciseLevelChildFrag.fragTag.equals(tag)){
                        exerciseLevelChildFrag1 = exerciseLevelChildFrag;
                    }
                }
                exLevelFragList.remove(exerciseLevelChildFrag1);
                --fragIdCount1;
            }
        }

        CharSequence toastText = "Exercise Removed";
        int duration = Snackbar.LENGTH_SHORT;

        try{
            Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
            snackbar.show();
        } catch (NullPointerException e){

        }
    }

    public void removeExercises(){
        ArrayList<String> tagList = new ArrayList<>();
        for(ExerciseLevelChildFrag exerciseLevelChildFrag : exLevelFragList){
            tagList.add(exerciseLevelChildFrag.fragTag);
        }
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        for(String tag : tagList){
            fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag));
            ExerciseLevelChildFrag exerciseLevelChildFrag1  = new ExerciseLevelChildFrag();
            for(ExerciseLevelChildFrag exerciseLevelChildFrag : exLevelFragList){
                if(exerciseLevelChildFrag.fragTag.equals(tag)){
                    exerciseLevelChildFrag1 = exerciseLevelChildFrag;
                }
            }
            exLevelFragList.remove(exerciseLevelChildFrag1);
            --fragIdCount1;
        }
        fragmentTransaction.commit();
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

    private String getDowForEdit(String[] daysArray){
        String doWConstructor = "";

        for(String day : daysArray){
            doWConstructor = doWConstructor + day + "_";
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
