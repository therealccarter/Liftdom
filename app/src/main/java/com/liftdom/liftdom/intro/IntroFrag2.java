package com.liftdom.liftdom.intro;


import android.content.Intent;
import android.net.Uri;
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
    private TextView appodealLink;
    private boolean isConsent = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag2, container, false);

        yesButton = (RadioButton) view.findViewById(R.id.yesRadioButton);
        noButton = (RadioButton) view.findViewById(R.id.noRadioButton);
        appodealLink = (TextView) view.findViewById(R.id.appodealLink);

        if(savedInstanceState == null){
            isConsent = true;
            yesButton.setChecked(true);
            noButton.setChecked(false);
        }

        appodealLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.appodeal.com/home/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        yesButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //IntroSingleton.getInstance().isGDPR = true;
                    isConsent = true;
                }
            }
        });

        noButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    //IntroSingleton.getInstance().isGDPR = false;
                    isConsent = false;
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
        return isConsent;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.consentOrElse);
    }
}
