package com.liftdom.workout_programs.FiveThreeOne;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBIntroFrag2 extends SlideFragment {


    public W531fBIntroFrag2() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private long delay = 50;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    List<String> templateNameList = new ArrayList<>();

    @BindView(R.id.squatEditText) EditText squatEditText;
    @BindView(R.id.unitsView1) TextView unitsView1;
    @BindView(R.id.benchPressEditText) EditText benchPressEditText;
    @BindView(R.id.unitsView2) TextView unitsView2;
    @BindView(R.id.deadliftEditText) EditText deadliftEditText;
    @BindView(R.id.unitsView3) TextView unitsView3;
    @BindView(R.id.deadliftEditText) EditText ohpEditText;
    @BindView(R.id.unitsView3) TextView unitsView4;
    @BindView(R.id.programNameEditText) EditText programNameEditText;
    @BindView(R.id.programNameTakenView) TextView programNameTakenView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_w531f_bintro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        getTemplateNames();

        if(savedInstanceState == null){
            programNameEditText.setText("Wendler_5-3-1");
        }

        /**
         * I'd feel best about this program if it is in normal formatting.
         * So the user can edit to their heart's content.
         */

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                if(userModelClass.isIsImperial()){
                    unitsView1.setText("lbs");
                    unitsView2.setText("lbs");
                    unitsView3.setText("lbs");
                    unitsView4.setText("lbs");
                }else{
                    unitsView1.setText("kgs");
                    unitsView2.setText("kgs");
                    unitsView3.setText("kgs");
                    unitsView4.setText("kgs");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

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

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!squatEditText.getText().toString().isEmpty()
                && !benchPressEditText.getText().toString().isEmpty()
                && !deadliftEditText.getText().toString().isEmpty()
                && !ohpEditText.getText().toString().isEmpty()
                && !programNameEditText.getText().toString().isEmpty()){
            W531fBSingleton.getInstance().squatMax = squatEditText.getText().toString();
            W531fBSingleton.getInstance().benchMax = benchPressEditText.getText().toString();
            W531fBSingleton.getInstance().deadliftMax = deadliftEditText.getText().toString();
            W531fBSingleton.getInstance().ohpMax = ohpEditText.getText().toString();
            valuesEntered = true;
        }

        return valuesEntered;
    }

    private void getTemplateNames(){
        DatabaseReference templatesRef = mRootRef.child("templates").child(uid);
        templatesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        templateNameList.add(dataSnapshot1.getKey());
                        if(dataSnapshot1.getKey().equals("Wendler_5-3-1")){
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
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidSmolovFields);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmgreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
