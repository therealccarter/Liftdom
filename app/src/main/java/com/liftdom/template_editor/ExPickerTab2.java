package com.liftdom.template_editor;



import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExPickerTab2 extends Fragment {


    public ExPickerTab2() {
        // Required empty public constructor
    }

    int exID;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_tab2, container, false);

        //benchPressTextView.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        if(clickCounter == 0){
        //            benchPressTextView.setBackgroundColor(Color.parseColor("#388e3c"));
        //            ExercisePickerController.getInstance().exName = "Bench Press";
        //            clickCounter = 1;
        //        } else if(clickCounter == 1){
        //            benchPressTextView.setBackgroundColor(Color.parseColor("#D1B91D"));
        //            ExercisePickerController.getInstance().exName = null;
        //            clickCounter = 0;
        //        }
        //    }
        //});
//
        //curlsTextView.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        if(clickCounter == 0){
        //            curlsTextView.setBackgroundColor(Color.parseColor("#388e3c"));
        //            ExercisePickerController.getInstance().exName = "Curls";
        //            clickCounter = 1;
        //        } else if(clickCounter == 1){
        //            curlsTextView.setBackgroundColor(Color.parseColor("#D1B91D"));
        //            ExercisePickerController.getInstance().exName = null;
        //            clickCounter = 0;
        //        }
        //    }
        //});
//
        //rowsTextView.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        if(clickCounter == 0){
        //            rowsTextView.setBackgroundColor(Color.parseColor("#388e3c"));
        //            ExercisePickerController.getInstance().exName = "Rows";
        //            clickCounter = 1;
        //        } else if(clickCounter == 1){
        //            rowsTextView.setBackgroundColor(Color.parseColor("#D1B91D"));
        //            ExercisePickerController.getInstance().exName = null;
        //            clickCounter = 0;
        //        }
        //    }
        //});
//
        return view;
    }

}
