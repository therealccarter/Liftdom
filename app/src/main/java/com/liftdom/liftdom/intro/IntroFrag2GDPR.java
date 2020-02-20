package com.liftdom.liftdom.intro;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag2GDPR extends SlideFragment {


    public IntroFrag2GDPR() {
        // Required empty public constructor
    }

    private Button yesButton;
    private Button noButton;
    private RadioButton noRB;
    private RadioButton yesRB;
    private TextView appodealLink;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag2_gdpr, container, false);

        yesButton = (Button) view.findViewById(R.id.yesButton);
        noButton = (Button) view.findViewById(R.id.noButton);
        noRB = (RadioButton) view.findViewById(R.id.noRadioButton);
        yesRB = (RadioButton) view.findViewById(R.id.yesRadioButton);
        appodealLink = (TextView) view.findViewById(R.id.appodealLink);

        appodealLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.appodeal.com/home/privacy-policy/"));
                startActivity(browserIntent);
            }
        });

        if(savedInstanceState == null){
            yesRB.setChecked(false);
            noRB.setChecked(false);
        }

        yesRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    IntroSingleton.getInstance().isGDPR = true;
                    //isConsent = true;
                }
            }
        });

        noRB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    IntroSingleton.getInstance().isGDPR = false;
                    //isConsent = false;
                }
            }
        });

        return view;
    }

    private boolean isOneButtonSelected(){
        boolean selected = false;

        if(yesRB.isChecked()){
            selected = true;
            IntroSingleton.getInstance().isGDPR = true;
        }else if(noRB.isChecked()){
            selected = true;
            IntroSingleton.getInstance().isGDPR = false;
        }

        return selected;
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
        return isOneButtonSelected();
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.gdprOrElse);
    }

}
