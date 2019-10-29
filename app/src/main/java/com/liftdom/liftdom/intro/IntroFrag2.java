package com.liftdom.liftdom.intro;


import io.github.dreierf.materialintroscreen.SlideFragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag2 extends SlideFragment {


    public IntroFrag2() {
        // Required empty public constructor
    }

    private RadioButton yesButton;
    private RadioButton noButton;
    private boolean isAvailable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag2, container, false);

        yesButton = (RadioButton) view.findViewById(R.id.yesRadioButton);
        noButton = (RadioButton) view.findViewById(R.id.noRadioButton);

        if(savedInstanceState == null){
            yesButton.setChecked(true);
            noButton.setChecked(false);
        }

        yesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    IntroSingleton.getInstance().isGDPR = true;
                }
            }
        });

        noButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    IntroSingleton.getInstance().isGDPR = false;
                }
            }
        });


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
        return true;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        if(!isAvailable){
            return getString(R.string.invalidDisplayNameTaken);
        }else{
            return getString(R.string.invalidDisplayName);
        }
    }
}
