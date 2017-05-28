package com.liftdom.workout_assistor;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
public class ExNameWAFrag extends Fragment {


    public ExNameWAFrag() {
        // Required empty public constructor
    }

    public String exerciseName = "fail";
    List<String> infoList = new ArrayList<>();
    int repsWeightInc = 0;
    int exNameSupersetInc = 0;
    ArrayList<RepsWeightWAFrag> repsWeightFragList = new ArrayList<>();
    ArrayList<ExNameSSWAFrag> exNameSupersetFragList = new ArrayList<>();
    ArrayList<ArrayList<String>> splitInfoList = new ArrayList<>();


    @BindView(R.id.exerciseName) TextView exerciseNameView;
    @BindView(R.id.repsWeightContainer) LinearLayout repsWeightContainer;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_wa, container, false);

        ButterKnife.bind(this, view);

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        exerciseNameView.setTypeface(lobster);

        exerciseNameView.setText(infoList.get(0));


        boolean isFirstEx = true;
        boolean isFirstSetSchemes = true;
        int inc = 0;
        int supersetInc = 0;
        ArrayList<ArrayList<String>> supersetList = new ArrayList<>();

        for(String string : infoList){
            if(isExerciseName(string)){
                inc++;
                ArrayList<String> subList = new ArrayList<>();
                subList.add(string);
                splitInfoList.add(subList);
            }else{
                splitInfoList.get(inc - 1).add(string);
            }
        }

        /**
         * What we want to do:
         * Have every exercise and set scheme addable and deletable
         * Have each supersetted set alternated, and then any left over sets be added at the bottom
         * So each exercise and set scheme needs an extra options button and a delete button
         */

        /**
         *  Here we have:
         *
         *  0: Bench Press (Barbell - Flat)    0: Bench Press (Barbell - Incline)     0: Bench Press (Barbell - Decline)
         *     1: 1@1                            1: 2@2                                  1: 3@3
         *                                       2: 2@2                                  2: 3@3
         *                                                                               3: 3@3
         */

        if(splitInfoList.size() > 1){
            // has supersets
            ArrayList<ArrayList<String>> finalList = expandSplitList(splitInfoList);
            int smallestSize = getSmallestSize(finalList);
            for(int i = 0; i < smallestSize - 1; i++){
                int count1 = 0;
                for(ArrayList<String> list : finalList){
                    if(count1 == 0){
                        // add reps weight frag
                        repsWeightInc++;
                        String tag = String.valueOf(repsWeightInc);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                        repsWeightFrag.repsWeightString = list.get(i + 1);
                        repsWeightFrag.tag = tag;
                        repsWeightFragList.add(repsWeightFrag);
                        fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                        fragmentTransaction.commit();
                    }else{
                        // add an exname ss frag with name value of .get(0) and reps weight value of .get(i)
                        exNameSupersetInc++;
                        String tag = String.valueOf(exNameSupersetInc);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ExNameSSWAFrag exNameFrag = new ExNameSSWAFrag();
                        ArrayList<String> newSubList = new ArrayList<>();
                        newSubList.add(list.get(0));
                        newSubList.add(list.get(i + 1));
                        exNameFrag.infoList.addAll(newSubList);
                        exNameFrag.tag = tag;
                        exNameSupersetFragList.add(exNameFrag);
                        fragmentTransaction.add(R.id.repsWeightContainer, exNameFrag);
                        fragmentTransaction.commit();
                    }
                    count1++;
                }
            }
            // now for each list that is bigger than the smallest size, add reps weight frags or ss exname frags
            int count2 = 0;
            for(ArrayList<String> list : finalList){
                if(list.size() > smallestSize){
                    if(count2 == 0){
                        for(int i = smallestSize; i < list.size(); i++){
                            repsWeightInc++;
                            String tag = String.valueOf(repsWeightInc);
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                            repsWeightFrag.repsWeightString = list.get(i + 1);
                            repsWeightFrag.tag = tag;
                            repsWeightFragList.add(repsWeightFrag);
                            fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag);
                            fragmentTransaction.commit();
                        }
                    }else{
                        ArrayList<String> subList = new ArrayList<>();
                        subList.add(list.get(0));
                        for(int i = smallestSize; i < list.size(); i++){
                            subList.add(list.get(i));
                        }
                        exNameSupersetInc++;
                        String tag = String.valueOf(exNameSupersetInc);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ExNameSSWAFrag exNameFrag = new ExNameSSWAFrag();
                        exNameFrag.infoList.addAll(subList);
                        exNameFrag.tag = tag;
                        exNameSupersetFragList.add(exNameFrag);
                        fragmentTransaction.add(R.id.repsWeightContainer, exNameFrag);
                        fragmentTransaction.commit();
                    }
                }
                count2++;
            }
        } else{
            // no supersets
            ArrayList<ArrayList<String>> finalList = expandSplitList(splitInfoList);
            for(int i = 1; i < finalList.get(0).size(); i++){
                repsWeightInc++;
                String tag = String.valueOf(repsWeightInc);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                repsWeightFrag.repsWeightString = finalList.get(0).get(i);
                repsWeightFrag.tag = tag;
                repsWeightFragList.add(repsWeightFrag);
                fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag);
                fragmentTransaction.commit();
            }
        }

        return view;
    }

    int getSmallestSize(ArrayList<ArrayList<String>> list){
        int smallest = 0;

        int inc = 0;
        for(ArrayList<String> arrayList : list){
            inc++;
            if(inc == 1){
                smallest = arrayList.size();
            }
            int mapSize = arrayList.size();
            if(mapSize < smallest){
                smallest = mapSize;
            }
        }

        return smallest;
    }

    ArrayList<ArrayList<String>> expandSplitList(ArrayList<ArrayList<String>> splitList){
        ArrayList<ArrayList<String>> newList = new ArrayList<>();

        for(ArrayList<String> list : splitList){
            ArrayList<String> newSubList = new ArrayList<>();
            for(int i = 0; i < list.size(); i++){
                if(i == 0){
                    newSubList.add(list.get(i));
                }else{
                    newSubList.addAll(generateRepsWeightList(list.get(i)));
                }
            }
            newList.add(newSubList);
        }

        return newList;
    }

    ArrayList<String> generateRepsWeightList(String infoString){
        ArrayList<String> generatedList = new ArrayList<>();

        String[] tokens = infoString.split("x");
        int setsNumber = Integer.valueOf(tokens[0]);
        for(int i = 0; i < setsNumber; i++){
            generatedList.add(tokens[1]);
        }

        return generatedList;
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

}
