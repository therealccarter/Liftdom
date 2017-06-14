package com.liftdom.liftdom.utils;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SimpleExNameSSFrag extends Fragment {


    public SimpleExNameSSFrag() {
        // Required empty public constructor
    }

    List<String> exInfoList;
    public boolean isPastDate;

    @BindView(R.id.exNameView) Button exNameView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_simple_ex_name_s, container, false);

        ButterKnife.bind(this, view);

        for(String string : exInfoList){
            if(isExerciseName(string)){
                exNameView.setText(string);
            }else{
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                SimpleSetSchemeSSFrag setSchemeSSFrag = new SimpleSetSchemeSSFrag();
                setSchemeSSFrag.setSchemeString = string;
                setSchemeSSFrag.isPastDate = true;
                fragmentTransaction.add(R.id.setSchemeHolder, setSchemeSSFrag);
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
