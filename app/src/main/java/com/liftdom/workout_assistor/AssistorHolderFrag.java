package com.liftdom.workout_assistor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorHolderFrag extends android.app.Fragment
                implements ExNameWAFrag.removeFragCallback{


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    TemplateModelClass templateClass;
    ArrayList<ExNameWAFrag> exNameFragList = new ArrayList<>();
    int exNameInc = 0;

    @BindView(R.id.addExerciseButton) Button addExButton;
    @BindView(R.id.saveButton) Button saveButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);

        ButterKnife.bind(this, view);

        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();

        addExButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exNameInc++;
                String tag = String.valueOf(exNameInc) + "ex";
                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                ExNameWAFrag exNameFrag = new ExNameWAFrag();
                exNameFrag.fragTag = tag;
                if (!getActivity().isFinishing()) {
                    fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    getChildFragmentManager().executePendingTransactions();
                    exNameFragList.add(exNameFrag);
                }
            }
        });

        if(templateClass.getMapForDay(intToWeekday(currentWeekday)) != null){
            if(!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()){

                HashMap<String, List<String>> map = templateClass.getMapForDay(intToWeekday(currentWeekday));
                for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                    if(!entry.getKey().equals("0_key")){
                        exNameInc++;
                        String tag = String.valueOf(exNameInc) + "ex";
                        List<String> stringList = entry.getValue();
                        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.infoList = stringList;
                        exNameFrag.fragTag = tag;
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag, tag);
                            fragmentTransaction.commitAllowingStateLoss();
                            getChildFragmentManager().executePendingTransactions();
                            exNameFragList.add(exNameFrag);
                        }
                    }
                }
            }
        }



        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> exInfo = new ArrayList<>();
                for(ExNameWAFrag exNameFrag : exNameFragList){
                    exInfo.addAll(exNameFrag.getExInfo());
                }
                AssistorSingleton.getInstance().endList.clear();
                AssistorSingleton.getInstance().endList.addAll(exInfo);
                Intent intent = new Intent(getActivity(), SaveAssistorDialog.class);
                startActivityForResult(intent, 1);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if(resultCode == 1){

                //android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
                //android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //AssistorSavedFrag assistorSavedFrag = new AssistorSavedFrag();
                //fragmentManager.r
            }
        }
    }

    public void removeFrag(String tag){
        getChildFragmentManager().executePendingTransactions();
        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(exNameInc != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                int inc = 0;
                int index = 0;
                boolean hasIndex = false;
                for(ExNameWAFrag exNameWAFrag : exNameFragList){
                    if(exNameWAFrag.fragTag.equals(tag)){
                        index = inc;
                        hasIndex = true;
                    }
                    inc++;
                }
                if(hasIndex){
                    exNameFragList.remove(index);
                }
                --exNameInc;
            }
        }
    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;
    }

    String intToWeekday(int inc){
        String weekday = "";

        if(inc == 1){
            weekday = "Monday";
        }else if(inc == 2){
            weekday = "Tuesday";
        }else if(inc == 3){
            weekday = "Wednesday";
        }else if(inc == 4){
            weekday = "Thursday";
        }else if(inc == 5){
            weekday = "Friday";
        }else if(inc == 6){
            weekday = "Saturday";
        }else if(inc == 7){
            weekday = "Sunday";
        }

        return weekday;
    }

    boolean containsToday(String dayUnformatted, int inc){
        boolean contains = false;
        String[] tokens = dayUnformatted.split("_");

        try{
            for(String string : tokens){
                if(string.equals(intToWeekday(inc))){
                    contains = true;
                }
            }
        } catch (IndexOutOfBoundsException e){

        }

        return contains;
    }
}
