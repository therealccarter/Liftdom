package com.liftdom.user_profile.calendar_stuff;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SimpleExNameFrag;
import com.liftdom.template_housing.ExNameSupersetFrag;
import com.liftdom.template_housing.HousingExNameFrag;
import com.liftdom.template_housing.HousingSetSchemeFrag;
import com.liftdom.template_housing.SetSchemeSupersetFrag;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FutureDateDialogSubFrag extends Fragment {


    public FutureDateDialogSubFrag() {
        // Required empty public constructor
    }

    boolean isTemplateImperial;
    boolean isCurrentUserImperial;

    public HashMap<String, List<String>> map;

    private ArrayList<SimpleExNameFrag> exNameFrags = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_future_date_dialog_sub, container, false);

        for(Map.Entry<String, List<String>> entry : map.entrySet()){
            if(!entry.getKey().equals("0_key")){
                List<String> list = entry.getValue();
                boolean isFirstEx = true;
                boolean isFirstSetSchemes = true;
                for(String string : list){
                    if(isExerciseName(string) && isFirstEx){
                        // add exname frag
                        isFirstEx = false;
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HousingExNameFrag exNameFrag = new HousingExNameFrag();
                        exNameFrag.exNameString = string;
                        exNameFrag.isSmallerText = true;
                        fragmentTransaction.add(R.id.exInfoSubHolder, exNameFrag);
                        fragmentTransaction.commit();
                    }else if(isExerciseName(string) && !isFirstEx) {
                        // add exname frag
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        ExNameSupersetFrag exNameFrag = new ExNameSupersetFrag();
                        exNameFrag.exNameString = string;
                        exNameFrag.isSmallerText = true;
                        fragmentTransaction.add(R.id.exInfoSubHolder, exNameFrag);
                        fragmentTransaction.commit();
                        isFirstSetSchemes = false;
                    }else if(!isExerciseName(string) && isFirstSetSchemes){
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        HousingSetSchemeFrag setSchemeFrag = new HousingSetSchemeFrag();
                        setSchemeFrag.isCurrentUserImperial = isCurrentUserImperial;
                        setSchemeFrag.isTemplateImperial = isTemplateImperial;
                        setSchemeFrag.setSchemeString = string;
                        setSchemeFrag.isSmallerText = true;
                        fragmentTransaction.add(R.id.exInfoSubHolder, setSchemeFrag);
                        fragmentTransaction.commit();
                    }else if(!isExerciseName(string) && !isFirstSetSchemes){
                        FragmentManager fragmentManager = getChildFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager
                                .beginTransaction();
                        SetSchemeSupersetFrag setSchemeFrag = new SetSchemeSupersetFrag();
                        setSchemeFrag.isCurrentUserImperial = isCurrentUserImperial;
                        setSchemeFrag.isTemplateImperial = isTemplateImperial;
                        setSchemeFrag.setSchemeString = string;
                        setSchemeFrag.isSmallerText = true;
                        fragmentTransaction.add(R.id.exInfoSubHolder, setSchemeFrag);
                        fragmentTransaction.commit();
                    }
                }
            }
        }

        //String delims = "[_]";
//
        //for(int i = 0; i < map.size(); i++){
        //    for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
        //        if(!mapEntry.getKey().equals("0_key")){
        //            String[] keyTokens = mapEntry.getKey().split(delims);
        //            if(keyTokens[0].equals(String.valueOf(i + 1))){
        //                SimpleExNameFrag exNameFrag = new SimpleExNameFrag();
        //                exNameFrag.exInfoList = mapEntry.getValue();
        //                exNameFrag.exerciseName = mapEntry.getValue().get(0);
        //                exNameFrags.add(exNameFrag);
        //            }
        //        }
        //    }
        //}
//
        //FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
//
        //for(SimpleExNameFrag simpleExNameFrag : exNameFrags){
        //    fragmentTransaction.add(R.id.exInfoSubHolder, simpleExNameFrag);
        //}
        //fragmentTransaction.commit();

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

}