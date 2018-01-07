package com.liftdom.workout_assistor;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    List<String> tagListWA = new ArrayList<>();
    List<String> tagListSSEx = new ArrayList<>();
    int repsWeightInc = 0;
    int exNameSupersetInc = 0;
    boolean isEdit = false;
    HashMap<String, List<String>> isEditInfoList = new HashMap<>();
    String fragTag;
    ArrayList<RepsWeightWAFrag> repsWeightFragList1 = new ArrayList<>();
    ArrayList<RepsWeightWAFrag> repsWeightFragList2 = new ArrayList<>();
    ArrayList<ExNameSSWAFrag> exNameSupersetFragList = new ArrayList<>();
    ArrayList<ArrayList<String>> splitInfoList = new ArrayList<>();
    public boolean isTemplateImperial;

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private removeFragCallback removeFrag;

    public interface startFirstTimeShowcase{
        void firstTimeShowcase(CheckBox checkBox);
    }

    /**
     * We need to clear the children here too
     * Gotta account for SuperSet frags in this level and the top level
     */

    private startFirstTimeShowcase firstTimeShowcaseCallback;

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
        firstTimeShowcaseCallback = (startFirstTimeShowcase) getParentFragment();

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
                repsWeightFrag.isTemplateImperial = isTemplateImperial;
                repsWeightFrag.repsWeightString = " @ ";
                repsWeightFrag.fragTag1 = tag;
                fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                fragmentTransaction.commitAllowingStateLoss();
                fragmentManager.executePendingTransactions();
                repsWeightFragList2.add(repsWeightFrag);
                tagListWA.add(tag);
            }
        });

        if(isEdit){
            ArrayList<List<String>> finalList = new ArrayList<>();
            for(Map.Entry<String, List<String>> entry : isEditInfoList.entrySet()){
                finalList.add(entry.getValue());
            }
            int biggestSize = getBiggestSizeList(finalList);
            inflateFragsFromEdit(finalList, biggestSize);
        }else{
            inflateFrags();
        }

        //TODO: READ THIS YO: Possibly just make the keys be ordered? _key style.


        return view;
    }

    boolean isFirstTimeFirstTime = true;

    @Override
    public void onStart(){
        super.onStart();

        //final DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime").child
        //        (FirebaseAuth.getInstance().getCurrentUser().getxUid()).child("isAssistorFirstTime");
        //firstTimeRef.addListenerForSingleValueEvent(new ValueEventListener() {
        //    @Override
        //    public void onDataChange(DataSnapshot dataSnapshot) {
        //        if(dataSnapshot.exists()){
//
        //            FancyShowCaseView fancyShowCaseView1 = new FancyShowCaseView.Builder(getActivity())
        //                    .title("Welcome to the Workout Assistor. \n \n" +
        //                            "This is where you'll come every day to complete your workouts and check off " +
        //                    "rest" +
        //                            " days." +
        //                            "\n \n You can customize everything here to most accurately reflect and record " +
        //                    "the workout " +
        //                            "you did that day.")
        //                    .titleStyle(R.style.showCaseViewStyle1, Gravity.CENTER)
        //                    .build();
//
        //            FancyShowCaseView fancyShowCaseView2 = new FancyShowCaseView.Builder(getActivity())
        //                    .focusOn(getView())
        //                    .title("This view shows every set for each exercise. \n \n You can add or delete sets " +
        //                            "here, as " +
        //                            "well as change the reps to 'to failure' or weight to 'bodyweight' via the 3
                                    // dot" +
        //                    " " +
        //                            "menu on the left." +
        //                            "\n \n Go ahead and check off the first set now, and we'll move on.")
        //                    .titleStyle(R.style.showCaseViewStyle2, Gravity.CENTER | Gravity.BOTTOM)
        //                    .focusShape(FocusShape.ROUNDED_RECTANGLE)
        //                    .build();
//
        //            new FancyShowCaseQueue()
        //                    .add(fancyShowCaseView1)
        //                    .add(fancyShowCaseView2)
        //                    .show();
//
        //            CheckBox checkBox = (CheckBox) repsWeightFragList2.get(0).getView().findViewById(R.id.checkBox);
        //            firstTimeShowcaseCallback.firstTimeShowcase(checkBox);
//
        //            //firstTimeRef.setValue(null);
        //        }
        //    }
//
        //    @Override
        //    public void onCancelled(DatabaseError databaseError) {
//
        //    }
        //});
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

    private void inflateFrags(){
        if(infoList.isEmpty()){
            repsWeightInc++;
            String tag = String.valueOf(repsWeightInc) + "rwSS_2";
            android.app.FragmentManager fragmentManager = getChildFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
            repsWeightFrag.isTemplateImperial = isTemplateImperial;
            repsWeightFrag.repsWeightString = " @ ";
            repsWeightFrag.fragTag1 = tag;
            fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
            fragmentTransaction.commitAllowingStateLoss();
            fragmentManager.executePendingTransactions();
            repsWeightFragList2.add(repsWeightFrag);
            tagListWA.add(tag);
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
                                repsWeightFrag.isTemplateImperial = isTemplateImperial;
                                repsWeightFrag.repsWeightString = arrayList.get(i + 1);
                                repsWeightFrag.fragTag1 = tag;
                                fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                                fragmentTransaction.commitAllowingStateLoss();
                                fragmentManager.executePendingTransactions();
                                repsWeightFragList1.add(repsWeightFrag);
                                tagListSSEx.add(tag);
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
                                exNameFrag.isTemplateImperial = isTemplateImperial;
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
                                tagListSSEx.add(tag);
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
                    repsWeightFrag.isTemplateImperial = isTemplateImperial;
                    repsWeightFrag.repsWeightString = finalList.get(0).get(i);
                    repsWeightFrag.fragTag1 = tag;
                    fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    repsWeightFragList2.add(repsWeightFrag);
                    fragmentManager.executePendingTransactions();
                    tagListWA.add(tag);
                }
            }
        }
    }

    private void inflateFragsFromEdit(ArrayList<List<String>> finalList, int biggestSize){

        exerciseNameView.setText(finalList.get(0).get(0));

        for(int i = 0; i < biggestSize; i++){
            int count = 0;
            for(List<String> arrayList : finalList){
                if(count == 0){
                    // parent ex
                    try{
                        repsWeightInc++;
                        String tag = String.valueOf(repsWeightInc) + "rwSS";
                        android.app.FragmentManager fragmentManager = getChildFragmentManager();
                        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
                        repsWeightFrag.isTemplateImperial = isTemplateImperial;
                        repsWeightFrag.repsWeightString = arrayList.get(i + 1);
                        repsWeightFrag.fragTag1 = tag;
                        repsWeightFrag.isEdit = true;
                        fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                        fragmentTransaction.commitAllowingStateLoss();
                        fragmentManager.executePendingTransactions();
                        repsWeightFragList1.add(repsWeightFrag);
                        tagListWA.add(tag);
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
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        ArrayList<String> newSubList = new ArrayList<>();
                        newSubList.add(arrayList.get(0));
                        newSubList.add(arrayList.get(i + 1));
                        exNameFrag.infoList.addAll(newSubList);
                        exNameFrag.fragTag2 = tag;
                        exNameFrag.isEdit = true;
                        if(count + 1 == finalList.size()){
                            exNameFrag.inflateBottomView = true;
                        }
                        fragmentTransaction.add(R.id.repsWeightContainer, exNameFrag, tag);
                        fragmentTransaction.commitAllowingStateLoss();
                        fragmentManager.executePendingTransactions();
                        exNameSupersetFragList.add(exNameFrag);
                        tagListSSEx.add(tag);
                    } catch(IndexOutOfBoundsException e){

                    }
                }
                count++;
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

    public String getExerciseValueFormatted(){
        return exerciseNameView.getText().toString();
    }

    public void cleanUpSubFrags(){
        for(String tag : tagListWA){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                //getChildFragmentManager().executePendingTransactions();
                android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
            }
        }
        for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
            // cleanup their's
        }
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
                    /**
                     * Let's say we've got this list: [1rwSS, 2rwSS, 3rwSS, 4rwSS] size = 4;
                     * If we delete, say, 3rwSS, we're removing the frag at index (2) (size = 3 now)
                     * but then if we next remove 4rwSS, we're removing at index (3), which is now out of bounds.
                     */

                    //repsWeightFragList1.remove(inc - 1);

                    Integer index = null;
                    int newInc = 0;
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                        if(repsWeightWAFrag.getTag().equals(tag)){
                            index = newInc;
                        }
                        newInc++;
                    }

                    if(index != null){
                        int newIndex = index;
                        repsWeightFragList1.remove(newIndex);
                    }
                    --repsWeightInc;
                }
            }
        }else{
            if(repsWeightInc != 0){
                //String fragString = Integer.toString(repsWeightInc);
                if(getChildFragmentManager().findFragmentByTag(tag) != null){
                    fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                    Integer index = null;
                    int newInc = 0;
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                        if(repsWeightWAFrag.getTag().equals(tag)){
                            index = newInc;
                        }
                        newInc++;
                    }

                    if(index != null){
                        int newIndex = index;
                        repsWeightFragList2.remove(newIndex);
                    }
                    --repsWeightInc;
                }
            }
        }
    }

    // superset removal
    public void removeFrag2(String tag){
        getChildFragmentManager().executePendingTransactions();
        android.app.FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        String[] tokens = tag.split("e");
        int inc = Integer.valueOf(tokens[0]);
        if(exNameSupersetInc != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                Integer index = null;
                int newInc = 0;
                for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
                    if(exNameSSWAFrag.getTag().equals(tag)){
                        index = newInc;
                    }
                    newInc++;
                }

                if(index != null){
                    int newIndex = index;
                    exNameSupersetFragList.remove(newIndex);
                }
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

    int getBiggestSizeList(ArrayList<List<String>> list){
        int biggest = 0;

        for(List<String> arrayList : list){
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
