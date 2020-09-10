package com.liftdom.workout_assistor;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ImageView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExNameSSWAFrag extends android.app.Fragment
            implements RepsWeightSSWAFrag.removeFragCallback,
            RepsWeightSSWAFrag.updateStateCallback,
            RepsWeightSSWAFrag.updateWorkoutStateFastCallback{


    public ExNameSSWAFrag() {
        // Required empty public constructor
    }

    String exName = "null";
    String fragTag2;
    ArrayList<String> infoList = new ArrayList<>();
    ArrayList<RepsWeightSSWAFrag> repsWeightFragList = new ArrayList<>();
    int fragCount = 0;
    boolean inflateBottomView;
    public boolean isTemplateImperial;
    public boolean isUserImperial;
    boolean isEdit = false;

    //PPLReddit
    public boolean isPPLReddit;
    public boolean isFirstTimePPLR;
    boolean isDark;


    public interface removeFragCallback1{
        void removeFrag2(String fragTag);
    }

    private removeFragCallback1 removeFrag1;

    public interface updateStateCallback{
        void updateWorkoutState();
    }

    public interface updateWorkoutStateFastCallback{
        void updateWorkoutStateFast();
        //void updateWorkoutState();
    }

    private updateStateCallback updateWorkoutState;
    private updateWorkoutStateFastCallback updateWorkoutStateFast;

    public void updateWorkoutState(){
        updateWorkoutState.updateWorkoutState();
    }

    public void updateWorkoutStateFast(){
        updateWorkoutStateFast.updateWorkoutStateFast();
    }

    @BindView(R.id.exerciseName) TextView exNameView;
    @BindView(R.id.repsWeightContainerSS) LinearLayout repsWeightContainer;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_sswa, container, false);

        ButterKnife.bind(this, view);

        if(infoList.size() != 0){
            updateChildExNames(infoList.get(0));
            exNameView.setText(infoList.get(0));

            for(int i = 1; i < infoList.size(); i++){
                fragCount++;
                String countString = String.valueOf(fragCount) + "ss";
                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                RepsWeightSSWAFrag repsWeightFrag = new RepsWeightSSWAFrag();
                repsWeightFrag.isTemplateImperial = isTemplateImperial;
                repsWeightFrag.isUserImperial = isUserImperial;
                repsWeightFrag.repsWeightString = infoList.get(i);
                repsWeightFrag.tag = countString;
                if(isEdit){
                    repsWeightFrag.isEdit = true;
                }
                if(inflateBottomView && i + 1 == infoList.size()){
                    repsWeightFrag.inflateBottomView = true;
                }
                repsWeightFragList.add(repsWeightFrag);
                fragmentTransaction.add(R.id.repsWeightContainerSS, repsWeightFrag, countString);
                fragmentTransaction.commitAllowingStateLoss();

            }
        }

        removeFrag1 = (removeFragCallback1) getParentFragment();
        updateWorkoutState = (updateStateCallback) getParentFragment();
        updateWorkoutStateFast = (updateWorkoutStateFastCallback) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // set title
                builder.setTitle("Delete exercise?");

                // set dialog message
                builder
                        .setMessage("Are you sure you want to delete this?")
                        .setCancelable(false)
                        .setPositiveButton("Delete",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                removeFrag1.removeFrag2(fragTag2);

                            }
                        })
                        .setNegativeButton("Cancel deletion",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // if this button is clicked, just close
                                // the dialog box and do nothing
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = builder.create();

                // show it
                alertDialog.show();
            }
        });

        /**
         * we need the three dots
         * we need them to flash (so we need to know we're pplr)
         * we might not want the ability to delete sets bc we can't add them back for supersets.
         *
         * how do we get flashing shit to work?
         *
         * For the flashing, we need to know that we're pplr.
         *  - we need to somehow get the information that the parent exname knows into here
         *
         * We also need to know what kind of lift it is and whether it's a main lift.
         *
         * For the actual changing weight/reps we're going to need to callback to the parent
         *  exname frag and then do it for all exnameSS frags (with the right ex name?)
         */

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(getActivity(), ExNameWAOptionDialog.class);
                //String exID = fragTag;
                //intent.putExtra("exID", exID);
                //intent.putExtra("exercise", getExerciseName());
                //intent.putExtra("isImperialPOV", isUserImperial);
                //intent.putExtra("isPPLR", String.valueOf(isPPLReddit));
                //intent.putExtra("isMainLift", String.valueOf(isMainLiftPPLR()));
                //startActivityForResult(intent, 3);
            }
        });

        if(isFirstTimePPLR){
            flashExtraOptions();
            makeDark();
        }

        return view;
    }

    boolean isGrey = false;
    CountDownTimer timer;

    private void flashExtraOptions(){
        if(timer != null){
            timer.cancel();
        }
        if(isFirstTimePPLR){
            timer = new CountDownTimer(15000, 250) {
                @Override
                public void onTick(long l) {
                    if(isGrey){
                        extraOptionsButton.setBackgroundColor(Color.parseColor("#a79413"));
                        isGrey = false;
                    }else{
                        extraOptionsButton.setBackgroundColor(Color.parseColor("#454545"));
                        isGrey = true;
                    }
                }

                @Override
                public void onFinish() {
                    extraOptionsButton.setBackgroundColor(Color.parseColor("#454545"));
                }
            }.start();
        }
    }

    /**
     * So each one of these will have the flashing buttons, but just click in/fill one of them and
     * it'll call back and fill all of them.
     */

    void makeDark(){
        isDark = true;
        if(!repsWeightFragList.isEmpty()){
            for(RepsWeightSSWAFrag repsWeightWAFrag : repsWeightFragList){
                //repsWeightWAFrag.makeDark();
                repsWeightWAFrag.isDark = true;
            }
        }
    }

    void makeLight(){
        isDark = false;
        isFirstTimePPLR = false;
        if(timer != null){
            timer.cancel();
        }
        if(!repsWeightFragList.isEmpty()){
            for(RepsWeightSSWAFrag repsWeightWAFrag : repsWeightFragList){
                repsWeightWAFrag.makeLight();
            }
        }
    }

    private void updateChildExNames(String exerciseName){
        if(!repsWeightFragList.isEmpty()){
            for(RepsWeightSSWAFrag repsWeightWAFrag : repsWeightFragList){
                /*
                 * We need reps frags to switch to bw if the ex is bw.
                 */
                repsWeightWAFrag.updateExName(exerciseName);
            }
        }
    }

    public boolean isChecked(){
        boolean checked = true;
        if(repsWeightFragList != null){
            if(!repsWeightFragList.isEmpty()){
                for(RepsWeightSSWAFrag repsWeightSSWAFrag : repsWeightFragList){
                    if(!repsWeightSSWAFrag.isChecked()){
                        checked = false;
                    }
                }
                //updateWorkoutState.updateWorkoutState();
            }else{
                checked = false;
            }
        }else{
            checked = false;
        }
        return checked;
    }

    public void checkAllRepsWeight(){
        if(repsWeightFragList != null){
            if(!repsWeightFragList.isEmpty()){
                for(RepsWeightSSWAFrag repsWeightSSWAFrag : repsWeightFragList){
                    repsWeightSSWAFrag.setCheckedView();
                }
                //updateWorkoutState.updateWorkoutState();
            }
        }
    }

    public void unCheckAllRepsWeight(){
        if(repsWeightFragList != null){
            if(!repsWeightFragList.isEmpty()){
                for(RepsWeightSSWAFrag repsWeightSSWAFrag : repsWeightFragList){
                    repsWeightSSWAFrag.setUnCheckedView();
                }
                //updateWorkoutState.updateWorkoutState();
            }
        }
    }


    public List<String> getInfoForMap(){
        List<String> exInfo = new ArrayList<>();

        exInfo.add(getExName());

        for(RepsWeightSSWAFrag repsWeightSSWAFrag : repsWeightFragList){
            exInfo.add(repsWeightSSWAFrag.getInfo());
        }

        return exInfo;
    }

    public List<String> getExInfo(){
        List<String> exInfo = new ArrayList<>();

        for(RepsWeightSSWAFrag repsWeightSSWAFrag : repsWeightFragList){
            exInfo.add(repsWeightSSWAFrag.getInfo());
        }

        return exInfo;
    }

    private String getExName(){
        return exNameView.getText().toString();
    }

    boolean isExerciseName(String input) {
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;
    }

    public void removeFrag(String tag){
        android.app.FragmentManager fragmentManager = getChildFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String[] tokens = tag.split("s");
        int inc = Integer.valueOf(tokens[0]);
        if(fragCount != 0){
            //String fragString = Integer.toString(repsWeightInc);
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                repsWeightFragList.remove(inc - 1);
                --fragCount;
            }
            removeFrag1.removeFrag2(fragTag2);
        }
    }

}
