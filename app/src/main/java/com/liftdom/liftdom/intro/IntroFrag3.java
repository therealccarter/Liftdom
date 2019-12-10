package com.liftdom.liftdom.intro;


import io.github.dreierf.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.wang.avi.AVLoadingIndicatorView;

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
    private EditText displayNameEditText;
    private TextView usernameTaken;
    private TextView usernameAvailable;
    private AVLoadingIndicatorView loadingView;
    private long delay = 1000;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    private boolean isAvailable;
    private String currentText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag3, container, false);

        HideKey.initialize(getActivity(), view);

        displayNameEditText = (EditText) view.findViewById(R.id.displayNameEditText);
        usernameAvailable = (TextView) view.findViewById(R.id.usernameAvailableView);
        usernameTaken = (TextView) view.findViewById(R.id.usernameTakenView);
        loadingView = (AVLoadingIndicatorView) view.findViewById(R.id.loadingView);
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

        displayNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                isAvailable = false;
                handler.removeCallbacks(inputFinishChecker);
                loadingView.setVisibility(View.VISIBLE);
                usernameTaken.setVisibility(View.GONE);
                usernameAvailable.setVisibility(View.GONE);
                if(s.length() == 0){
                    loadingView.setVisibility(View.GONE);
                    usernameTaken.setVisibility(View.GONE);
                    usernameAvailable.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    lastTextEdit = System.currentTimeMillis();
                    handler.postDelayed(inputFinishChecker, delay);
                }
            }
        });

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

                    feetEditText.setText("");
                    inchesEditText.setText("");
                    cmHeightEditText.setText("");
                    weightEditText.setText("");
                }else{
                    IntroSingleton.getInstance().isImperial = false;

                    metricHeightLL.setVisibility(View.VISIBLE);
                    kgTextView.setVisibility(View.VISIBLE);
                    imperialHeightLL.setVisibility(View.GONE);
                    poundsTextView.setVisibility(View.GONE);

                    feetEditText.setText("");
                    inchesEditText.setText("");
                    cmHeightEditText.setText("");
                    weightEditText.setText("");
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

    private Runnable inputFinishChecker = new Runnable(){
        public void run(){
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("userNames")
                        .child(displayNameEditText.getText().toString());
                userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            loadingView.setVisibility(View.GONE);
                            usernameTaken.setVisibility(View.VISIBLE);
                            usernameAvailable.setVisibility(View.GONE);
                            isAvailable = false;
                        }else{
                            loadingView.setVisibility(View.GONE);
                            usernameTaken.setVisibility(View.GONE);
                            usernameAvailable.setVisibility(View.VISIBLE);
                            isAvailable = true;
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
    };

    @Override
    public int backgroundColor() {
        return R.color.confirmGreen;
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
                if(displayName.length() > 2 && isAvailable){
                    IntroSingleton.getInstance().displayName = displayName;
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

