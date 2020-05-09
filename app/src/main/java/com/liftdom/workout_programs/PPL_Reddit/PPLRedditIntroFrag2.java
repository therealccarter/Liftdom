package com.liftdom.workout_programs.PPL_Reddit;


import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
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
public class PPLRedditIntroFrag2 extends SlideFragment {


    public PPLRedditIntroFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.pplpplrRadioButton) RadioButton pplpplrRadioButton;
    @BindView(R.id.pplrpplRadioButton) RadioButton pplrpplRadioButton;

    @BindView(R.id.inclineDbRB) RadioButton inclineDbRB;
    @BindView(R.id.landminePressRB) RadioButton landminePressRB;

    @BindView(R.id.tricepsPushdownsRB) RadioButton tricepsPushdownsRB;
    @BindView(R.id.skullcrushersRB1) RadioButton skullcrushersRB1;
    @BindView(R.id.jmPressRB1) RadioButton jmPressRB1;
    @BindView(R.id.dbTricepsExtensionsRB1) RadioButton dbTricepsExtensionsRB1;

    @BindView(R.id.overheadTricepsExtensionsRB) RadioButton overheadTricepsExtensionsRB;
    @BindView(R.id.skullcrushersRB2) RadioButton skullcrushersRB2;
    @BindView(R.id.jmPressRB2) RadioButton jmPressRB2;
    @BindView(R.id.dbTricepsExtensionsRB2) RadioButton dbTricepsExtensionsRB2;

    @BindView(R.id.pulldownsRB) RadioButton pulldownsRB;
    @BindView(R.id.pullupsRB) RadioButton pullupsRB;
    @BindView(R.id.chinupsRB) RadioButton chinupsRB;

    @BindView(R.id.seatedCableRowsRB) RadioButton seatedCableRowsRB;
    @BindView(R.id.dbRowsRB) RadioButton dbRowsRB;
    @BindView(R.id.tBarRowsRB) RadioButton tBarRowsRB;

    @BindView(R.id.facePullsRB) RadioButton facePullsRB;
    @BindView(R.id.rearDeltFlyesRB) RadioButton rearDeltFlyesRB;

    @BindView(R.id.dumbbellCurlsRB) RadioButton dumbbellCurlsRB;
    @BindView(R.id.barbellCurlsRB1) RadioButton barbellCurlsRB1;
    @BindView(R.id.machineCurlsRB1) RadioButton machineCurlsRB1;

    @BindView(R.id.hammerCurlsRB) RadioButton hammerCurlsRB;
    @BindView(R.id.barbellCurlsRB2) RadioButton barbellCurlsRB2;
    @BindView(R.id.machineCurlsRB2) RadioButton machineCurlsRB2;

    @BindView(R.id.legPressRB) RadioButton legPressRB;
    @BindView(R.id.frontSquatRB) RadioButton frontSquatRB;

    @BindView(R.id.legCurlsRB) RadioButton legCurlsRB;
    @BindView(R.id.ghrRB) RadioButton ghrRB;

    @BindView(R.id.barbellCalfRaisesRB) RadioButton barbellCalfRaisesRB;
    @BindView(R.id.dumbbellCalfRaisesRB) RadioButton dumbbellCalfRaisesRB;

    @BindView(R.id.programNameEditText) EditText programNameEditText;
    @BindView(R.id.programNameTakenView) TextView programNameTakenView;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    private long delay = 50;
    private long lastTextEdit = 0;
    Handler handler = new Handler();
    List<String> templateNameList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        getTemplateNames();

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

        // We'll collect the data into the singleton on program name evaluation.

        pplpplrRadioButton.setChecked(true);
        inclineDbRB.setChecked(true);
        tricepsPushdownsRB.setChecked(true);
        overheadTricepsExtensionsRB.setChecked(true);
        pulldownsRB.setChecked(true);
        seatedCableRowsRB.setChecked(true);
        facePullsRB.setChecked(true);
        dumbbellCurlsRB.setChecked(true);
        hammerCurlsRB.setChecked(true);
        legPressRB.setChecked(true);
        legCurlsRB.setChecked(true);
        barbellCalfRaisesRB.setChecked(true);

        skullcrushersRB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    skullcrushersRB2.setVisibility(View.GONE);
                }else{
                    skullcrushersRB2.setVisibility(View.VISIBLE);
                }
            }
        });

        skullcrushersRB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    skullcrushersRB1.setVisibility(View.GONE);
                }else{
                    skullcrushersRB1.setVisibility(View.VISIBLE);
                }
            }
        });

        jmPressRB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    jmPressRB2.setVisibility(View.GONE);
                }else{
                    jmPressRB2.setVisibility(View.VISIBLE);
                }
            }
        });

        jmPressRB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    jmPressRB1.setVisibility(View.GONE);
                }else{
                    jmPressRB1.setVisibility(View.VISIBLE);
                }
            }
        });

        dbTricepsExtensionsRB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    dbTricepsExtensionsRB2.setVisibility(View.GONE);
                }else{
                    dbTricepsExtensionsRB2.setVisibility(View.VISIBLE);
                }
            }
        });

        dbTricepsExtensionsRB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    dbTricepsExtensionsRB1.setVisibility(View.GONE);
                }else{
                    dbTricepsExtensionsRB1.setVisibility(View.VISIBLE);
                }
            }
        });

        barbellCurlsRB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    barbellCurlsRB2.setVisibility(View.GONE);
                }else{
                    barbellCurlsRB2.setVisibility(View.VISIBLE);
                }
            }
        });

        barbellCurlsRB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    barbellCurlsRB1.setVisibility(View.GONE);
                }else{
                    barbellCurlsRB1.setVisibility(View.VISIBLE);
                }
            }
        });

        machineCurlsRB1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    machineCurlsRB2.setVisibility(View.GONE);
                }else{
                    machineCurlsRB2.setVisibility(View.VISIBLE);
                }
            }
        });

        machineCurlsRB2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    machineCurlsRB1.setVisibility(View.GONE);
                }else{
                    machineCurlsRB1.setVisibility(View.VISIBLE);
                }
            }
        });


        return view;
    }

    private void generateSingletonValues(){
        /*
         * How are we going to do this?
         *
         * Each day, 6 different ways, we'll be using the exercises
         * available for that day. We need to use the PPL program page as a reference and
         * assemble the chosen accessories into nodes for each of the 6 days and somehow order
         * them for easy retrieval in the WA.
         *
         * Should we also add some sort of function in for a
         * deload week? That's definitely something to consider after we get the program running.
         *
         * Other issue is how we set up the numbers. One way is to do it on the first day of each
         * exercise, and use the recommended method of doing sets of 5 until it starts slowing
         * down. Nah we're doing that.
         */

        PPLRedditSingleton.getInstance().pplpplrBool = pplpplrRadioButton.isChecked();

        if(inclineDbRB.isChecked()){
            PPLRedditSingleton.getInstance().inclineDB = inclineDbRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().inclineDB = landminePressRB.getText().toString();
        }

        if(tricepsPushdownsRB.isChecked()){
            PPLRedditSingleton.getInstance().tricepsPushdowns =
                    tricepsPushdownsRB.getText().toString();
        }else if(skullcrushersRB1.isChecked()){
            PPLRedditSingleton.getInstance().tricepsPushdowns =
                    skullcrushersRB1.getText().toString();
        }else if(jmPressRB1.isChecked()){
            PPLRedditSingleton.getInstance().tricepsPushdowns =
                    jmPressRB1.getText().toString();
        }else if(dbTricepsExtensionsRB1.isChecked()){
            PPLRedditSingleton.getInstance().tricepsPushdowns =
                    dbTricepsExtensionsRB1.getText().toString();
        }

        if(overheadTricepsExtensionsRB.isChecked()){
            PPLRedditSingleton.getInstance().overheadTricepsExtensions =
                    tricepsPushdownsRB.getText().toString();
        }else if(skullcrushersRB2.isChecked()){
            PPLRedditSingleton.getInstance().overheadTricepsExtensions =
                    skullcrushersRB2.getText().toString();
        }else if(jmPressRB2.isChecked()){
            PPLRedditSingleton.getInstance().overheadTricepsExtensions =
                    jmPressRB2.getText().toString();
        }else if(dbTricepsExtensionsRB2.isChecked()){
            PPLRedditSingleton.getInstance().overheadTricepsExtensions =
                    dbTricepsExtensionsRB2.getText().toString();
        }

        if(pulldownsRB.isChecked()){
            PPLRedditSingleton.getInstance().pulldowns =
                    pulldownsRB.getText().toString();
        }else if(pullupsRB.isChecked()){
            PPLRedditSingleton.getInstance().pulldowns =
                    pullupsRB.getText().toString();
        }else if(chinupsRB.isChecked()){
            PPLRedditSingleton.getInstance().pulldowns =
                    chinupsRB.getText().toString();
        }

        if(seatedCableRowsRB.isChecked()){
            PPLRedditSingleton.getInstance().seatedCableRows =
                    seatedCableRowsRB.getText().toString();
        }else if(dbRowsRB.isChecked()){
            PPLRedditSingleton.getInstance().seatedCableRows =
                    dbRowsRB.getText().toString();
        }else if(tBarRowsRB.isChecked()){
            PPLRedditSingleton.getInstance().seatedCableRows =
                    tBarRowsRB.getText().toString();
        }

        if(facePullsRB.isChecked()){
            PPLRedditSingleton.getInstance().facePulls = facePullsRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().facePulls = rearDeltFlyesRB.getText().toString();
        }

        if(dumbbellCurlsRB.isChecked()){
            PPLRedditSingleton.getInstance().dumbbellCurls =
                    dumbbellCurlsRB.getText().toString();
        }else if(barbellCurlsRB1.isChecked()){
            PPLRedditSingleton.getInstance().dumbbellCurls =
                    barbellCurlsRB1.getText().toString();
        }else if(machineCurlsRB1.isChecked()){
            PPLRedditSingleton.getInstance().dumbbellCurls =
                    machineCurlsRB1.getText().toString();
        }

        if(hammerCurlsRB.isChecked()){
            PPLRedditSingleton.getInstance().hammerCurls =
                    hammerCurlsRB.getText().toString();
        }else if(barbellCurlsRB2.isChecked()){
            PPLRedditSingleton.getInstance().hammerCurls =
                    barbellCurlsRB2.getText().toString();
        }else if(machineCurlsRB2.isChecked()){
            PPLRedditSingleton.getInstance().hammerCurls =
                    machineCurlsRB2.getText().toString();
        }

        if(legPressRB.isChecked()){
            PPLRedditSingleton.getInstance().legPress = legPressRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().legPress = frontSquatRB.getText().toString();
        }

        if(legCurlsRB.isChecked()){
            PPLRedditSingleton.getInstance().legCurls = legCurlsRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().legCurls = ghrRB.getText().toString();
        }

        if(barbellCalfRaisesRB.isChecked()){
            PPLRedditSingleton.getInstance().barbellCalfRaises = barbellCalfRaisesRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().barbellCalfRaises = dumbbellCalfRaisesRB.getText().toString();
        }

        PPLRedditSingleton.getInstance().programName = programNameEditText.getText().toString();

    }

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!programNameEditText.getText().toString().isEmpty()){
            PPLRedditSingleton.getInstance().programName = programNameEditText.getText().toString();
            generateSingletonValues();
            valuesEntered = true;
        }

        return valuesEntered;
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
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.enterProgramName);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmGreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
