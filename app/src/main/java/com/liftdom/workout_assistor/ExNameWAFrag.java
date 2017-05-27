package com.liftdom.workout_assistor;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    int fragIdCount2 = 0;
    int supersetFragCount = 0;
    ArrayList<RepsWeightFrag> repsWeightList = new ArrayList<>();
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

        exerciseNameView.setText(exerciseName);

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

        if(splitInfoList.size() > 1){
            // has supersets
            ArrayList<ArrayList<String>> finalList = expandSplitList(splitInfoList);
            int smallestSize = getSmallestSize(finalList);
            for(int i = 0; i < smallestSize; i++){
                for(ArrayList<String> list : finalList){

                }
            }
        } else{
            // no supersets
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
            newSubList.clear();
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
