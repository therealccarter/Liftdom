package com.liftdom.workout_assistor;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssistorHolderFrag extends Fragment {


    public AssistorHolderFrag() {
        // Required empty public constructor
    }

    TemplateModelClass modelClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assistor_holder, container, false);



        return view;
    }

}
