package com.liftdom.workout_programs.PPL_Reddit;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import io.github.dreierf.materialintroscreen.SlideFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag3 extends SlideFragment {

    public PPLRedditIntroFrag3() {
        // Required empty public constructor
    }

    @BindView(R.id.chooseAccessories) TextView chooseAccessories;

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

    @BindView(R.id.dipsWRB) RadioButton dipsWRB;
    @BindView(R.id.dipsRB) RadioButton dipsRB;

    @BindView(R.id.pulldownsRB) RadioButton pulldownsRB;
    @BindView(R.id.pullupsRB) RadioButton pullupsRB;
    @BindView(R.id.chinupsRB) RadioButton chinupsRB;
    @BindView(R.id.pullupsWRB) RadioButton pullupsWRB;
    @BindView(R.id.chinupsWRB) RadioButton chinupsWRB;

    @BindView(R.id.seatedCableRowsRB) RadioButton seatedCableRowsRB;
    @BindView(R.id.chestSupportedRowsRB) RadioButton chestSupportedRowsRB;
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

    @BindView(R.id.abWheelRB1) RadioButton abWheelRB1;
    @BindView(R.id.hangingLegRaisesRB1) RadioButton hangingLegRaisesRB1;

    @BindView(R.id.legPressRB) RadioButton legPressRB;
    @BindView(R.id.frontSquatRB) RadioButton frontSquatRB;

    @BindView(R.id.legCurlsRB) RadioButton legCurlsRB;
    @BindView(R.id.ghrRB) RadioButton ghrRB;
    @BindView(R.id.ghrWRB) RadioButton ghrWRB;

    @BindView(R.id.barbellCalfRaisesRB) RadioButton barbellCalfRaisesRB;
    @BindView(R.id.dumbbellCalfRaisesRB) RadioButton dumbbellCalfRaisesRB;

    @BindView(R.id.abWheelRB2) RadioButton abWheelRB2;
    @BindView(R.id.hangingLegRaisesRB2) RadioButton hangingLegRaisesRB2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag3, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        chooseAccessories.setTypeface(lobster);

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
        hangingLegRaisesRB1.setChecked(true);
        hangingLegRaisesRB2.setChecked(true);

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

    boolean areAllChecked;

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

        areAllChecked = true;

        if(inclineDbRB.isChecked()){
            PPLRedditSingleton.getInstance().inclineDB = inclineDbRB.getText().toString();
        }else if(landminePressRB.isChecked()){
            PPLRedditSingleton.getInstance().inclineDB = landminePressRB.getText().toString();
        }else{
            areAllChecked = false;
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
        }else{
            areAllChecked = false;

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
        }else{
            areAllChecked = false;

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
        }else if(pullupsWRB.isChecked()){
            PPLRedditSingleton.getInstance().pulldowns =
                    pullupsWRB.getText().toString();
        }else if(chinupsWRB.isChecked()){
            PPLRedditSingleton.getInstance().pulldowns =
                    chinupsWRB.getText().toString();
        }else{
            areAllChecked = false;

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
        }else if(chestSupportedRowsRB.isChecked()){
            PPLRedditSingleton.getInstance().seatedCableRows =
                    chestSupportedRowsRB.getText().toString();
        }else{
            areAllChecked = false;

        }
        if(facePullsRB.isChecked()){
            PPLRedditSingleton.getInstance().facePulls = facePullsRB.getText().toString();
        }else if(rearDeltFlyesRB.isChecked()){
            PPLRedditSingleton.getInstance().facePulls = rearDeltFlyesRB.getText().toString();
        }else{
            areAllChecked = false;

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
        }else{
            areAllChecked = false;

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
        }else{
            areAllChecked = false;

        }
        if(legPressRB.isChecked()){
            PPLRedditSingleton.getInstance().legPress = legPressRB.getText().toString();
        }else if(frontSquatRB.isChecked()){
            PPLRedditSingleton.getInstance().legPress = frontSquatRB.getText().toString();
        }else{
            areAllChecked = false;

        }
        if(legCurlsRB.isChecked()){
            PPLRedditSingleton.getInstance().legCurls = legCurlsRB.getText().toString();
        }else if(ghrRB.isChecked()){
            PPLRedditSingleton.getInstance().legCurls = ghrRB.getText().toString();
        }else if(ghrWRB.isChecked()){
            PPLRedditSingleton.getInstance().legCurls = ghrWRB.getText().toString();
        }else{
            areAllChecked = false;

        }
        if(barbellCalfRaisesRB.isChecked()){
            PPLRedditSingleton.getInstance().barbellCalfRaises = barbellCalfRaisesRB.getText().toString();
        }else if(dumbbellCalfRaisesRB.isChecked()){
            PPLRedditSingleton.getInstance().barbellCalfRaises = dumbbellCalfRaisesRB.getText().toString();
        }else{
            areAllChecked = false;

        }

        if(dipsWRB.isChecked()){
            PPLRedditSingleton.getInstance().dips = dipsWRB.getText().toString();
        }else if(dipsRB.isChecked()){
            PPLRedditSingleton.getInstance().dips = dipsRB.getText().toString();
        }else{
            PPLRedditSingleton.getInstance().dips = "false";
        }

        if(abWheelRB1.isChecked()){
            PPLRedditSingleton.getInstance().abs1 = abWheelRB1.getText().toString();
        }else if(hangingLegRaisesRB1.isChecked()){
            PPLRedditSingleton.getInstance().abs1 = hangingLegRaisesRB1.getText().toString();
        }else{
            areAllChecked = false;
        }
        if(abWheelRB2.isChecked()){
            PPLRedditSingleton.getInstance().abs2 = abWheelRB2.getText().toString();
        }else if(hangingLegRaisesRB2.isChecked()){
            PPLRedditSingleton.getInstance().abs2 = hangingLegRaisesRB2.getText().toString();
        }else{
            areAllChecked = false;
        }

    }

    /**
     * Could possibly make strength version "built in" by having those accessory options already
     * here? Then just have a radio button in frag2 which puts in a boolean that determines the
     * rep ranges. Same for Endurance version.
     *
     */

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        generateSingletonValues();
        if(areAllChecked){
            valuesEntered = true;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.makeSureAllChecked);
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
