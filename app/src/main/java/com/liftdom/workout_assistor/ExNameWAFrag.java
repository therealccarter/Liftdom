package com.liftdom.workout_assistor;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExNameWAFrag extends android.app.Fragment
        implements RepsWeightWAFrag.removeFragCallback,
                    ExNameSSWAFrag.removeFragCallback1{


    public ExNameWAFrag() {
        // Required empty public constructor
    }

    public String exerciseName = "fail";
    List<String> infoList = new ArrayList<>();
    int repsWeightInc = 0;
    int exNameSupersetInc = 0;
    boolean isEdit = false;
    List<String> isEditInfoList = new ArrayList<>();
    String fragTag;
    ArrayList<RepsWeightWAFrag> repsWeightFragList1 = new ArrayList<>();
    ArrayList<RepsWeightWAFrag> repsWeightFragList2 = new ArrayList<>();
    ArrayList<ExNameSSWAFrag> exNameSupersetFragList = new ArrayList<>();
    ArrayList<ArrayList<String>> splitInfoList = new ArrayList<>();

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private removeFragCallback removeFrag;

    @BindView(R.id.exerciseName) TextView exerciseNameView;
    @BindView(R.id.repsWeightContainer) LinearLayout repsWeightContainer;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.addRepsWeightButton) Button addSchemeButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_wa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        exerciseNameView.setTypeface(lobster);

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFrag.removeFrag(fragTag);
            }
        });

        exerciseNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExInfoOrSelectorDialog.class);
                String exName = getExerciseValueFormatted();
                intent.putExtra("exName", exName);
                startActivityForResult(intent, 1);
            }
        });

        addSchemeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                repsWeightInc++;
                String tag = String.valueOf(repsWeightInc) + "rwSS_2";
                android.app.FragmentManager fragmentManager = getChildFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                repsWeightFrag.repsWeightString = " @ ";
                repsWeightFrag.fragTag1 = tag;
                fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                repsWeightFragList2.add(repsWeightFrag);
            }
        });

        if(isEdit){
            boolean isFirstEx = true;
            boolean isFirstRepsWeight = true;
            int inc = 0;
            ArrayList<List<String>> superSetList = new ArrayList<>();
            for(String value : isEditInfoList){
                if(isExerciseName(value) && isFirstEx){
                    exerciseNameView.setText(value);
                    isFirstEx = false;
                }else if(!isExerciseName(value) && isFirstRepsWeight){
                    repsWeightInc++;
                    String tag = String.valueOf(repsWeightInc) + "rwSS";
                    android.app.FragmentManager fragmentManager = getChildFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                    repsWeightFrag.repsWeightString = value;
                    repsWeightFrag.fragTag1 = tag;
                    repsWeightFrag.isEdit = true;
                    fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    fragmentManager.executePendingTransactions();
                    repsWeightFragList1.add(repsWeightFrag);
                }else if(isExerciseName(value) && !isFirstEx){
                    List<String> list = new ArrayList<>();
                    list.add(value);
                    inc++;
                    isFirstRepsWeight = false;
                }else if(!isExerciseName(value) && !isFirstRepsWeight){
                    superSetList.get(inc - 1).add(value);
                }
            }
            for(List<String> list : superSetList){
                exNameSupersetInc++;
                String tag = String.valueOf(exNameSupersetInc) + "exSS";
                android.app.FragmentManager fragmentManager = getChildFragmentManager();
                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ExNameSSWAFrag exNameFrag = new ExNameSSWAFrag();
                ArrayList<String> newSubList = new ArrayList<>();
                newSubList.addAll(list);
                exNameFrag.infoList.addAll(newSubList);
                exNameFrag.fragTag2 = tag;
                fragmentTransaction.add(R.id.repsWeightContainer, exNameFrag, tag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                exNameSupersetFragList.add(exNameFrag);
            }
        }else{
            inflateFrags();
        }


        return view;
    }

    private void inflateFrags(){
        if(infoList.isEmpty()){
            repsWeightInc++;
            String tag = String.valueOf(repsWeightInc) + "rwSS_2";
            android.app.FragmentManager fragmentManager = getChildFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
            repsWeightFrag.repsWeightString = " @ ";
            repsWeightFrag.fragTag1 = tag;
            fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            repsWeightFragList2.add(repsWeightFrag);
        }else {
            exerciseNameView.setText(infoList.get(0));

            int inc = 0;

            for (String string : infoList) {
                if (isExerciseName(string)) {
                    inc++;
                    ArrayList<String> subList = new ArrayList<>();
                    subList.add(string);
                    splitInfoList.add(subList);
                } else {
                    splitInfoList.get(inc - 1).add(string);
                }
            }

            if (splitInfoList.size() > 1) {
                ArrayList<ArrayList<String>> finalList = expandSplitList(splitInfoList);
                int biggestSize = getBiggestSize(finalList);
                for(int i = 0; i < biggestSize; i++){
                    int count = 0;
                    for(ArrayList<String> arrayList : finalList){
                        if(count == 0){
                            // parent ex
                            try{
                                repsWeightInc++;
                                String tag = String.valueOf(repsWeightInc) + "rwSS";
                                android.app.FragmentManager fragmentManager = getChildFragmentManager();
                                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                                repsWeightFrag.repsWeightString = arrayList.get(i + 1);
                                repsWeightFrag.fragTag1 = tag;
                                fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                                repsWeightFragList1.add(repsWeightFrag);
                            } catch(IndexOutOfBoundsException e){

                            }
                        }else{
                            // superset ex
                            try{
                                exNameSupersetInc++;
                                String tag = String.valueOf(exNameSupersetInc) + "exSS";
                                android.app.FragmentManager fragmentManager = getChildFragmentManager();
                                android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                ExNameSSWAFrag exNameFrag = new ExNameSSWAFrag();
                                ArrayList<String> newSubList = new ArrayList<>();
                                newSubList.add(arrayList.get(0));
                                newSubList.add(arrayList.get(i + 1));
                                exNameFrag.infoList.addAll(newSubList);
                                exNameFrag.fragTag2 = tag;
                                if(count + 1 == finalList.size()){
                                    exNameFrag.inflateBottomView = true;
                                }
                                fragmentTransaction.add(R.id.repsWeightContainer, exNameFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                                exNameSupersetFragList.add(exNameFrag);
                            } catch(IndexOutOfBoundsException e){

                            }
                        }
                        count++;
                    }
                }
            }else{
                // no supersets
                ArrayList<ArrayList<String>> finalList = expandSplitList(splitInfoList);
                for(int i = 1; i < finalList.get(0).size(); i++){
                    repsWeightInc++;
                    String tag = String.valueOf(repsWeightInc) + "rwSS_2";
                    android.app.FragmentManager fragmentManager = getChildFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                    repsWeightFrag.repsWeightString = finalList.get(0).get(i);
                    repsWeightFrag.fragTag1 = tag;
                    fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    repsWeightFragList2.add(repsWeightFrag);
                    fragmentManager.executePendingTransactions();
                }
            }
        }
    }

    public List<String> getExInfo(){
        List<String> exInfoList = new ArrayList<>();

        for(RepsWeightWAFrag repsWeightFrag : repsWeightFragList1){
            exInfoList.add(repsWeightFrag.getInfo());
        }

        for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
            exInfoList.addAll(exNameSSWAFrag.getExInfo());
        }

        for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
            exInfoList.add(repsWeightWAFrag.getInfo());
        }

        return exInfoList;
    }

    public HashMap<String, List<String>> getInfoForMap(){
        List<String> exInfoList = new ArrayList<>();
        List<String> parentExInfoList = new ArrayList<>();

        parentExInfoList.add(getExerciseValueFormatted());

        if(getExerciseValueFormatted().equals("Chest Fly (Machine)")){
            Log.i("info", "info");
        }

        ArrayList<List<String>> collectedExInfoByExList = new ArrayList<>();


        if(repsWeightFragList1.size() != 0){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                parentExInfoList.add(repsWeightWAFrag.getInfo());
            }
        }

        if(repsWeightFragList2.size() != 0){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                parentExInfoList.add(repsWeightWAFrag.getInfo());
            }
        }

        collectedExInfoByExList.add(parentExInfoList);

        for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
            if(listHasEx(collectedExInfoByExList, exNameSSWAFrag.getInfoForMap().get(0))){
                int index = getListHasExInt(collectedExInfoByExList, exNameSSWAFrag.getInfoForMap().get(0));
                collectedExInfoByExList.get(index).add(exNameSSWAFrag.getInfoForMap().get(1));
            }else{
                collectedExInfoByExList.add(exNameSSWAFrag.getInfoForMap());
            }
        }

        HashMap<String, List<String>> exMap = new HashMap<>();

        int inc = 0;
        for(List<String> list : collectedExInfoByExList){
            List<String> newList = new ArrayList<>();
            newList.addAll(list);
            exMap.put(String.valueOf(inc) + "_key", newList);
            inc++;
        }

        return exMap;
    }

    private boolean listHasEx(ArrayList<List<String>> parentList, String exName){
        boolean bool = false;

        int inc = 0;
        for(List<String> list : parentList){
            if(inc != 0){
                if(list.get(0).equals(exName)){
                    bool = true;
                }
            }
            inc++;
        }

        return bool;
    }

    private int getListHasExInt(ArrayList<List<String>> parentList, String exName){
        int index = 0;

        int inc = 0;
        for(List<String> list : parentList){
            if(inc != 0){
                if(list.get(0).equals(exName)){
                  index = inc;
                }
            }
            inc++;
        }

        return index;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == 1){
            if(data != null){
                if(data.getStringExtra("choice") != null){
                    if(data.getStringExtra("choice").equals("selectEx")){
                        Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                        int exID = exerciseNameView.getId();
                        intent.putExtra("exID", exID);
                        startActivityForResult(intent, 2);
                    }else if(data.getStringExtra("choice").equals("infoEx")){
                        //TODO: get exercise info popup
                    }
                }
            }
        }else if(resultCode == 2){
            if(data != null){
                if(data.getStringExtra("MESSAGE") != null){
                    exerciseNameView.setText(data.getStringExtra("MESSAGE"));
                }
            }
        }
    }

    public String getExerciseValueFormatted(){
        return exerciseNameView.getText().toString();
    }

    public void removeFrag(String tag){
        android.app.FragmentManager fragmentManager = getChildFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        String delims = "[r, _]";
        String[] tokens = tag.split(delims);
        int inc = Integer.valueOf(tokens[0]);
        if(tokens.length < 3){
            if(repsWeightInc != 0){
                //String fragString = Integer.toString(repsWeightInc);
                if(getChildFragmentManager().findFragmentByTag(tag) != null){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                    repsWeightFragList1.remove(inc - 1);
                    --repsWeightInc;
                }
            }
        }else{
            if(repsWeightInc != 0){
                //String fragString = Integer.toString(repsWeightInc);
                if(getChildFragmentManager().findFragmentByTag(tag) != null){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                    repsWeightFragList2.remove(inc - 1);
                    --repsWeightInc;
                }
            }
        }
    }

    public void removeFrag2(String tag){
        getChildFragmentManager().executePendingTransactions();
        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        String[] tokens = tag.split("e");
        int inc = Integer.valueOf(tokens[0]);
        if(exNameSupersetInc != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                exNameSupersetFragList.remove(inc - 1);
                --exNameSupersetInc;
            }
        }
    }

    int getBiggestSize(ArrayList<ArrayList<String>> list){
        int biggest = 0;

        for(ArrayList<String> arrayList : list){
            if(arrayList.size() > biggest){
                biggest = arrayList.size();
            }
        }

        return biggest;
    }

    int getBiggestForMap(int p, int s){
        if(p > s){
            return p;
        }else{
            return s;
        }
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
