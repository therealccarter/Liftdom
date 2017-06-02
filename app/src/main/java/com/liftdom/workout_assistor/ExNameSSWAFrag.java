package com.liftdom.workout_assistor;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
            implements RepsWeightSSWAFrag.removeFragCallback{


    public ExNameSSWAFrag() {
        // Required empty public constructor
    }

    String exName = "null";
    String fragTag2;
    ArrayList<String> infoList = new ArrayList<>();
    ArrayList<RepsWeightSSWAFrag> repsWeightFragList = new ArrayList<>();
    int fragCount = 0;
    boolean inflateBottomView;

    public interface removeFragCallback1{
        void removeFrag2(String fragTag);
    }

    private removeFragCallback1 removeFrag1;

    @BindView(R.id.exerciseName) TextView exNameView;
    @BindView(R.id.repsWeightContainerSS) LinearLayout repsWeightContainer;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_sswa, container, false);

        ButterKnife.bind(this, view);

        exNameView.setText(infoList.get(0));

        removeFrag1 = (removeFragCallback1) getParentFragment();

        for(int i = 1; i < infoList.size(); i++){
            fragCount++;
            String countString = String.valueOf(fragCount) + "ss";
            android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            RepsWeightSSWAFrag repsWeightFrag = new RepsWeightSSWAFrag();
            repsWeightFrag.repsWeightString = infoList.get(i);
            repsWeightFrag.tag = countString;
            if(inflateBottomView && i + 1 == infoList.size()){
                repsWeightFrag.inflateBottomView = true;
            }
            repsWeightFragList.add(repsWeightFrag);
            fragmentTransaction.add(R.id.repsWeightContainerSS, repsWeightFrag, countString);
            fragmentTransaction.commitAllowingStateLoss();
        }

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

                // set title
                builder.setTitle("Delete exercise?");

                // set dialog message
                builder
                        .setMessage("Are you sure you want to delete this exercise?")
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

        return view;
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
        }
    }

}
