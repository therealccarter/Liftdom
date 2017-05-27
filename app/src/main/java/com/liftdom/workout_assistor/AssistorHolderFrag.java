package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.template_housing.HousingExNameFrag;
import org.joda.time.DateTime;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorHolderFrag extends Fragment {


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    TemplateModelClass templateClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);

        DateTime dateTime = new DateTime();
        int currentWeekday = dateTime.getDayOfWeek();

        if(templateClass.getMapForDay(intToWeekday(currentWeekday)) != null){
            if(!templateClass.getMapForDay(intToWeekday(currentWeekday)).isEmpty()){

                HashMap<String, List<String>> map = templateClass.getMapForDay(intToWeekday(currentWeekday));
                for(Map.Entry<String, List<String>> entry : map.entrySet()) {
                    if(!entry.getKey().equals("0_key")){
                        List<String> stringList = entry.getValue();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ExNameWAFrag exNameFrag = new ExNameWAFrag();
                        exNameFrag.infoList = stringList;
                        if (!getActivity().isFinishing()) {
                            fragmentTransaction.add(R.id.exInfoHolder2, exNameFrag);
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }
                }
            }
        }

        return view;
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
