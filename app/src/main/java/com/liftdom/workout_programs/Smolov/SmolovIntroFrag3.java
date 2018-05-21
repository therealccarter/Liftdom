package com.liftdom.workout_programs.Smolov;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovIntroFrag3 extends SlideFragment {


    public SmolovIntroFrag3() {
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_intro_frag3, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        getTemplateNames();

        if(savedInstanceState == null){
            programNameEditText.setText("Smolov");
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
                        if(dataSnapshot1.getKey().equals("Smolov")){
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

        if(!programNameEditText.getText().toString().equals("") && !programNameEditText.getText().toString().equals(" ")){
            SmolovSetupSingleton.getInstance().programName = programNameEditText.getText().toString();
            validName = true;
        }

        return validName;
    }

    @Override
    public String cantMoveFurtherErrorMessage(){
        return getString(R.string.invalidProgramName);
    }

}
