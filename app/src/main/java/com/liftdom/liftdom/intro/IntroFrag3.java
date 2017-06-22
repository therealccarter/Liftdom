package com.liftdom.liftdom.intro;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag3 extends SlideFragment {


    public IntroFrag3() {
        // Required empty public constructor
    }

    private RadioButton imperialRadioButton;
    private RadioButton metricRadioButton;
    private LinearLayout imperialHeightLL;
    private EditText feetEditText;
    private EditText inchesEditText;
    private LinearLayout metricHeightLL;
    private EditText cmHeightEditText;
    private EditText weightEditText;
    private TextView poundsTextView;
    private TextView kgTextView;
    private EditText ageEditText;
    private RadioButton maleRadioButton;
    private RadioButton femaleRadioButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag3, container, false);

        imperialRadioButton = (RadioButton) view.findViewById(R.id.imperialRadioButton);
        metricRadioButton = (RadioButton) view.findViewById(R.id.metricRadioButton);
        imperialHeightLL = (LinearLayout) view.findViewById(R.id.imperialHeight);
        feetEditText = (EditText) view.findViewById(R.id.feetEditText);
        inchesEditText = (EditText) view.findViewById(R.id.inchesEditText);
        metricHeightLL = (LinearLayout) view.findViewById(R.id.metricHeight);
        cmHeightEditText = (EditText) view.findViewById(R.id.cmHeightEditText);
        weightEditText = (EditText) view.findViewById(R.id.weightEditText);
        poundsTextView = (TextView) view.findViewById(R.id.poundsTextView);
        kgTextView = (TextView) view.findViewById(R.id.kgsTextView);
        ageEditText = (EditText) view.findViewById(R.id.ageEditText);
        maleRadioButton = (RadioButton) view.findViewById(R.id.maleRadioButton);
        femaleRadioButton = (RadioButton) view.findViewById(R.id.femaleRadioButton);

        if(IntroSingleton.getInstance().isImperial){
            imperialRadioButton.setChecked(true);
            metricRadioButton.setChecked(false);
        }else{
            imperialRadioButton.setChecked(false);
            metricRadioButton.setChecked(true);
        }

        if(IntroSingleton.getInstance().isMale){
            maleRadioButton.setChecked(true);
            femaleRadioButton.setChecked(false);
        }else{
            maleRadioButton.setChecked(false);
            femaleRadioButton.setChecked(true);
        }

        imperialRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    IntroSingleton.getInstance().isImperial = true;

                    metricHeightLL.setVisibility(View.GONE);
                    kgTextView.setVisibility(View.GONE);
                    imperialHeightLL.setVisibility(View.VISIBLE);
                    poundsTextView.setVisibility(View.VISIBLE);
                }else{
                    IntroSingleton.getInstance().isImperial = false;

                    metricHeightLL.setVisibility(View.VISIBLE);
                    kgTextView.setVisibility(View.VISIBLE);
                    imperialHeightLL.setVisibility(View.GONE);
                    poundsTextView.setVisibility(View.GONE);
                }
            }
        });

        maleRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    IntroSingleton.getInstance().isMale = true;
                }else{
                    IntroSingleton.getInstance().isMale = false;
                }
            }
        });




        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmgreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

    @Override
    public boolean canMoveFurther() {
        boolean validName = false;

        if(IntroSingleton.getInstance().isImperial){
            // check imperial values
            if(!feetEditText.getText().toString().equals("")){
                if(!inchesEditText.getText().toString().equals("")){
                    if(!weightEditText.getText().toString().equals("")){
                        IntroSingleton.getInstance().feet = feetEditText.getText().toString();
                        IntroSingleton.getInstance().inches = inchesEditText.getText().toString();
                        IntroSingleton.getInstance().weightImperial = weightEditText.getText().toString();
                        validName = true;
                    }
                }
            }
        }else{
            // check metric values
            if(!cmHeightEditText.getText().toString().equals("")){
                if(!weightEditText.getText().toString().equals("")){
                    IntroSingleton.getInstance().cm = cmHeightEditText.getText().toString();
                    IntroSingleton.getInstance().weightMetric = weightEditText.getText().toString();
                    validName = true;
                }
            }
        }

        if(validName){
            IntroSingleton.getInstance().age = ageEditText.getText().toString();
        }

        return validName;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidProfileFields);
    }
}

