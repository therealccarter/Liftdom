package com.liftdom.liftdom.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleExNameFrag extends Fragment {


    public SimpleExNameFrag() {
        // Required empty public constructor
    }

    public List<String> exInfoList;

    @BindView(R.id.exNameView) Button exNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_ex_name, container, false);

        ButterKnife.bind(this, view);

        ArrayList<List<String>> infoLists = new ArrayList<>();
        int inc = 0;
        int exInc = 0;
        boolean isFirstSetSchemes = true;
        for(String string : exInfoList){
            inc++;
            if(inc != 1){
                if(isExerciseName(string)){
                    exInc++;
                    List<String> list = new ArrayList<>();
                    list.add(string);
                    infoLists.add(list);
                    isFirstSetSchemes = false;
                }else{
                    if(isFirstSetSchemes){
                        infoLists.get(0).add(string);
                    }else{
                        infoLists.get(exInc).add(string);
                    }
                }
            }else {
                List<String> list = new ArrayList<>();
                list.add(string);
                infoLists.add(list);
            }
        }

        int inc2 = 0;
        for(List<String> list : infoLists){
            inc2++;
            if(inc2 == 1){
                exNameView.setText(list.get(0));
                for(String string : list){
                    if(!isExerciseName(string)){
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        SimpleSetSchemeFrag setSchemeFrag = new SimpleSetSchemeFrag();
                        setSchemeFrag.setSchemeString = string;
                        fragmentTransaction.add(R.id.setSchemeHolder, setSchemeFrag);
                        fragmentTransaction.commit();
                    }
                }
            }else{
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                SimpleExNameSSFrag exNameSSFrag = new SimpleExNameSSFrag();
                exNameSSFrag.exInfoList = list;
                fragmentTransaction.add(R.id.superSetHolder, exNameSSFrag);
                fragmentTransaction.commit();
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

}
