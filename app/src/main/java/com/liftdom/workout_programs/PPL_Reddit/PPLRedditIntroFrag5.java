package com.liftdom.workout_programs.PPL_Reddit;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.*;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag5 extends SlideFragment {

    public PPLRedditIntroFrag5() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private long delay = 50;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    List<String> templateNameList = new ArrayList<>();

    @BindView(R.id.programNameEditText) EditText programNameEditText;
    @BindView(R.id.programNameTakenView) TextView programNameTakenView;
    @BindView(R.id.restTimerLL) LinearLayout restTimerLL;
    @BindView(R.id.restTimerSwitch) Switch restTimerSwitch;
    @BindView(R.id.restTimerInfoLL) LinearLayout restTimerInfoLL;
    @BindView(R.id.minutes) EditText minutesEditText;
    @BindView(R.id.seconds) EditText secondsEditText;
    @BindView(R.id.secondsVibrate) EditText secondsVibrateEditText;
    @BindView(R.id.showRestTimerAlertRadioButton) RadioButton showRestTimerAlertRB;
    @BindView(R.id.justVibrateRadioButton) RadioButton justVibrateRB;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag5, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        getTemplateNames();

        showRestTimerAlertRB.setChecked(true);
        restTimerSwitch.setChecked(true);

        if(savedInstanceState == null){
            programNameEditText.setText("PPLReddit");
        }

        programNameEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                handler.removeCallbacks(inputFinishChecker);
                programNameTakenView.setVisibility(View.GONE);
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() > 0){
                    lastTextEdit = System.currentTimeMillis();
                    handler.postDelayed(inputFinishChecker, delay);
                }
            }
        });

        return view;
    }

    private Runnable inputFinishChecker = new Runnable() {
        @Override
        public void run() {
            if(System.currentTimeMillis() > (lastTextEdit + delay - 500)){
                String editTextString = programNameEditText.getText().toString();
                if(templateNameList.contains(editTextString)){
                    programNameTakenView.setVisibility(View.VISIBLE);
                }
            }
        }
    };

    private void getTemplateNames(){
        DatabaseReference templatesRef = mRootRef.child("templates").child(uid);
        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        templateNameList.add(dataSnapshot1.getKey());
                        if(dataSnapshot1.getKey().equals("PPLReddit")){
                            programNameTakenView.setVisibility(View.VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean canMoveFurther() {
        boolean valuesEntered = false;

        if(!programNameEditText.getText().toString().isEmpty()){
            PPLRedditSingleton.getInstance().programName = programNameEditText.getText().toString();
            PPLRedditSingleton.getInstance().mRestTime =
                    minutesEditText.getText().toString() + ":" + secondsEditText.getText().toString();
            PPLRedditSingleton.getInstance().mIsActiveRestTimer = restTimerSwitch.isChecked();
            PPLRedditSingleton.getInstance().mVibrationTime = secondsVibrateEditText.getText().toString();
            PPLRedditSingleton.getInstance().mIsRestTimerAlert = showRestTimerAlertRB.isChecked();
            valuesEntered = true;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidSmolovFields);
    }


    @Override
    public int backgroundColor() {
        return R.color.grey;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }
}
