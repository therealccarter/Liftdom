package com.liftdom.liftdom.intro;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag2 extends SlideFragment {


    public IntroFrag2() {
        // Required empty public constructor
    }

    private EditText displayNameEditText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag2, container, false);

        displayNameEditText = (EditText) view.findViewById(R.id.displayNameEditText);

        return view;
    }


    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

    @Override
    public boolean canMoveFurther() {
        boolean validName = false;
        String displayName = displayNameEditText.getText().toString();

        if(displayName != null){
            if(!displayName.equals("")){
                if(displayName.length() > 2 && displayName.length() < 13){
                    validName = true;
                    IntroSingleton.getInstance().displayName = displayName;
                }
            }
        }
        return validName;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidDisplayName);
    }
}
