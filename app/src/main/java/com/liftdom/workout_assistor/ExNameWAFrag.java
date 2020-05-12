package com.liftdom.workout_assistor;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.inputmethod.InputMethodManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.FiveThreeOne_ForBeginners.W531fBWeightDialog;
import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;
import me.toptas.fancyshowcase.FocusShape;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExNameWAFrag extends android.app.Fragment
        implements RepsWeightWAFrag.removeFragCallback,
        ExNameSSWAFrag.removeFragCallback1,
        RepsWeightWAFrag.updateStateCallback,
        ExNameSSWAFrag.updateStateCallback,
        RepsWeightWAFrag.updateStateForResultCallback,
        RepsWeightWAFrag.updateWorkoutStateFastCallback,
        ExNameSSWAFrag.updateWorkoutStateFastCallback{


    public ExNameWAFrag() {
        // Required empty public constructor
    }

    //public String exerciseName = "fail";
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
    public boolean isUserImperial;
    boolean isComingFromReps;
    boolean isComingFromReps2;
    public String templateName;

    //W531fB
    public boolean isAssistanceW531fB;
    boolean isPushW531fB;
    boolean isPullW531fB;
    boolean isLegCoreW531fB;

    // need to set up update progress callback

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    public interface updateWorkoutStateCallback{
        void updateWorkoutStateWithDelay();
        //void updateWorkoutState();
    }

    public interface updateWorkoutStateFastCallback{
        void updateWorkoutState();
        //void updateWorkoutState();
    }

    public interface updateExNameCallback{
        void updateExName(String frag, String exName);
    }

    public interface updateChildFragWeightsCallback{
        void updateChildFragWeights(String frag, String exName, String weight);
    }

    public interface updateWorkoutStateForResultCallback{
        void updateWorkoutStateForResult(String tag1, String tag2);
    }

    public interface sendAssistanceExerciseCallback{
        void setAssistanceExercise(String exName);
    }

    private sendAssistanceExerciseCallback sendAssistance;

    private updateWorkoutStateCallback updateWorkoutState;
    private updateWorkoutStateFastCallback updateWorkoutStateFast;
    private updateExNameCallback updateExName;
    private updateChildFragWeightsCallback updateChildWeights;

    private updateWorkoutStateForResultCallback updateWorkoutStateForResult;

    private removeFragCallback removeFrag;

    public interface startFirstTimeShowcase{
        void firstTimeShowcase(CheckBox checkBox);
    }

    /*
     * We need to clear the children here too
     * Gotta account for SuperSet frags in this level and the top level
     */

    private startFirstTimeShowcase firstTimeShowcaseCallback;

    public void updateWorkoutState(){
        // ISSUE RIGHT NOW IS THAT IF I CHECK ONE OFF AFTER CHECKING ALL, THEY ALL CHECK OFF
        checkIfAllAreChecked();
        updateWorkoutState.updateWorkoutStateWithDelay();
    }

    public void updateWorkoutStateFast(){
        updateWorkoutStateFast.updateWorkoutState();
    }

    public void updateWorkoutStateForResult(String tag2){
        updateWorkoutStateForResult.updateWorkoutStateForResult(fragTag, tag2);
    }

    @BindView(R.id.exerciseName) TextView exerciseNameView;
    @BindView(R.id.repsWeightContainer) LinearLayout repsWeightContainer;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.addRepsWeightButton) Button addSchemeButton;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.checkOffAll) CheckBox checkOffAll;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ex_name_wa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();
        firstTimeShowcaseCallback = (startFirstTimeShowcase) getParentFragment();
        updateWorkoutState = (updateWorkoutStateCallback) getParentFragment();
        updateWorkoutStateForResult = (updateWorkoutStateForResultCallback) getParentFragment();
        updateWorkoutStateFast = (updateWorkoutStateFastCallback) getParentFragment();
        updateExName = (updateExNameCallback) getParentFragment();
        sendAssistance = (sendAssistanceExerciseCallback) getParentFragment();
        updateChildWeights = (updateChildFragWeightsCallback) getParentFragment();

        Typeface lobster = Typeface.createFromAsset(getActivity().getAssets(), "fonts/Lobster-Regular.ttf");

        exerciseNameView.setTypeface(lobster);

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //removeFrag.removeFrag(fragTag);
                Intent intent = new Intent(v.getContext(), ExDeleteConfirmation.class);
                startActivityForResult(intent, 1);
            }
        });

        checkOffAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkOffAll.isChecked()){
                    if(exNameSupersetFragList != null){
                        if(!exNameSupersetFragList.isEmpty()){
                            checkOffSS();
                            updateWorkoutState.updateWorkoutStateWithDelay();
                        }else{
                            checkOffNonSS();
                            updateWorkoutState.updateWorkoutStateWithDelay();
                        }
                    }else{
                        checkOffNonSS();
                        updateWorkoutState.updateWorkoutStateWithDelay();
                    }
                }else{
                    if(exNameSupersetFragList != null){
                        if(!exNameSupersetFragList.isEmpty()){
                            unCheckOffSS();
                            updateWorkoutState.updateWorkoutStateWithDelay();
                        }else{
                            unCheckOffNonSS();
                            updateWorkoutState.updateWorkoutStateWithDelay();
                        }
                    }else{
                        unCheckOffNonSS();
                        updateWorkoutState.updateWorkoutStateWithDelay();
                    }
                }
            }
        });


        exerciseNameView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //Intent intent = new Intent(v.getContext(), ExInfoOrSelectorDialog.class);
                //String exName = getExerciseValueFormatted();
                //intent.putExtra("exName", exName);
                //startActivityForResult(intent, 1);
                if(getActivity() != null){
                    if(getActivity().getCurrentFocus() != null){
                        try {
                            InputMethodManager inputMethodManager =
                                    (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            inputMethodManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(), 0);
                        }catch (NullPointerException e){

                        }
                    }
                }

                if(isAssistanceW531fB){
                    handleExNameIntentW531fB();
                }else{
                    Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                    int exID = exerciseNameView.getId();
                    intent.putExtra("fragTag", fragTag);
                    intent.putExtra("exID", exID);
                    startActivityForResult(intent, 2);
                }

                /**
                 *
                 * What we're working on now:
                 * We need it to bring up the right exercises.
                 *
                 * First case is none have been selected, so it has the pertinent info already
                 * "stored" in the ex name placeholder text. And we'll bring up a version of
                 * ExSelectorActivity with a custom list (which we'll have to make).
                 *
                 * However, how will we know which type of list to bring up once we've selected
                 * an exercise? Maybe "IF isAssistanceW531fB is true, check current ex name
                 * against a list of the available exercises and then we'll know which one it is?"
                 *
                 */

            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = exerciseNameView.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 3);
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
                repsWeightFrag.isUserImperial = isUserImperial;
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
            for(int i = 0; i < isEditInfoList.size(); i++){
                //final int iVal = i;
                for(Map.Entry<String, List<String>> entry : isEditInfoList.entrySet()){
                    String delims = "[_]";
                    String[] tokens = entry.getKey().split(delims);
                    if(Integer.parseInt(tokens[0]) == i){
                        finalList.add(entry.getValue());
                    }
                }
            }
            int biggestSize = getBiggestSizeList(finalList);
            inflateFragsFromEdit(finalList, biggestSize);
            //checkIfAllAreChecked();
            /*
             * ISSUE RIGHT NOW IS THAT THE FRAGS ARE NOT YET ALIVE WHEN THIS GETS RUN.
             */
        }else{
            inflateFrags();
            //checkIfAllAreChecked();
        }

        return view;
    }

    private void handleExNameIntentW531fB(){
        String exName = exerciseNameView.getText().toString();

        if(exName.equals("Choose PUSH assistance exercise")){
            isPushW531fB = true;
            Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
            int exID = exerciseNameView.getId();
            intent.putExtra("fragTag", fragTag);
            intent.putExtra("exID", exID);
            intent.putExtra("customList", "true");
            intent.putExtra("W531fB", "true");
            intent.putExtra("push", "true");
            startActivityForResult(intent, 2);
        }else if(exName.equals("Choose PULL assistance exercise")){
            isPullW531fB = true;
            Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
            int exID = exerciseNameView.getId();
            intent.putExtra("fragTag", fragTag);
            intent.putExtra("exID", exID);
            intent.putExtra("customList", "true");
            intent.putExtra("W531fB", "true");
            intent.putExtra("pull", "true");
            startActivityForResult(intent, 2);
        }else if(exName.equals("Choose SINGLE LEG/CORE assistance exercise")){
            isLegCoreW531fB = true;
            Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
            int exID = exerciseNameView.getId();
            intent.putExtra("fragTag", fragTag);
            intent.putExtra("exID", exID);
            intent.putExtra("customList", "true");
            intent.putExtra("W531fB", "true");
            intent.putExtra("legCore", "true");
            startActivityForResult(intent, 2);
        }else{
            // compare current ex name to list
            String[] pushArray = getActivity().getResources().getStringArray(R.array.W531fBPush);
            ArrayList<String> pushList = new ArrayList<>(Arrays.asList(pushArray));
            String[] pullArray = getActivity().getResources().getStringArray(R.array.W531fBPull);
            ArrayList<String> pullList = new ArrayList<>(Arrays.asList(pullArray));
            String[] legCoreArray = getActivity().getResources().getStringArray(R.array.W531fBLegCore);
            ArrayList<String> legCoreList = new ArrayList<>(Arrays.asList(legCoreArray));

            if(pushList.contains(exName)){
                isAssistanceW531fB = true;
                isPushW531fB = true;
                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = exerciseNameView.getId();
                intent.putExtra("fragTag", fragTag);
                intent.putExtra("exID", exID);
                intent.putExtra("customList", "true");
                intent.putExtra("W531fB", "true");
                intent.putExtra("push", "true");
                startActivityForResult(intent, 2);
            }else if(pullList.contains(exName)){
                isAssistanceW531fB = true;
                isPullW531fB = true;
                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = exerciseNameView.getId();
                intent.putExtra("fragTag", fragTag);
                intent.putExtra("exID", exID);
                intent.putExtra("customList", "true");
                intent.putExtra("W531fB", "true");
                intent.putExtra("pull", "true");
                startActivityForResult(intent, 2);
            }else if(legCoreList.contains(exName)){
                isAssistanceW531fB = true;
                isLegCoreW531fB = true;
                Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                int exID = exerciseNameView.getId();
                intent.putExtra("fragTag", fragTag);
                intent.putExtra("exID", exID);
                intent.putExtra("customList", "true");
                intent.putExtra("W531fB", "true");
                intent.putExtra("legCore", "true");
                startActivityForResult(intent, 2);
            }
        }
    }

    private void checkExNameForW531fB(String exName){
        if(exName.equals("Choose PUSH assistance exercise")){
            isAssistanceW531fB = true;
            isPushW531fB = true;
        }else if(exName.equals("Choose PULL assistance exercise")){
            isAssistanceW531fB = true;
            isPullW531fB = true;
        }else if(exName.equals("Choose SINGLE LEG/CORE assistance exercise")){
            isAssistanceW531fB = true;
            isLegCoreW531fB = true;
        }else{
            if(isAssistanceW531fB){
                String[] pushArray = getActivity().getResources().getStringArray(R.array.W531fBPush);
                ArrayList<String> pushList = new ArrayList<>(Arrays.asList(pushArray));
                String[] pullArray = getActivity().getResources().getStringArray(R.array.W531fBPull);
                ArrayList<String> pullList = new ArrayList<>(Arrays.asList(pullArray));
                String[] legCoreArray = getActivity().getResources().getStringArray(R.array.W531fBLegCore);
                ArrayList<String> legCoreList = new ArrayList<>(Arrays.asList(legCoreArray));

                if(pushList.contains(exName)){
                    //isAssistanceW531fB = true;
                    isPushW531fB = true;
                }else if(pullList.contains(exName)){
                    //isAssistanceW531fB = true;
                    isPullW531fB = true;
                }else if(legCoreList.contains(exName)){
                    //isAssistanceW531fB = true;
                    isLegCoreW531fB = true;
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 5){
            if(resultCode == 7){
                try{
                    String fragTag1 = data.getStringExtra("fragTag");
                    if(fragTag1.equals(fragTag)){
                        String weight = data.getStringExtra("weight");
                        updateChildWeights(weight, false);
                        updateWorkoutStateFast();
                    }else{
                        updateChildWeights.updateChildFragWeights(fragTag1, data.getStringExtra(
                                "exName"), data.getStringExtra("weight"));
                    }
                }catch (NullPointerException e){

                }
            }
        }else{
            if(resultCode == 1){
                if(data != null){
                    if(data.getStringExtra("choice") != null){
                        if(data.getStringExtra("choice").equals("selectEx")){
                            Intent intent = new Intent(getActivity(), ExSelectorActivity.class);
                            int exID = exerciseNameView.getId();
                            intent.putExtra("fragTag", fragTag);
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
                        String fragTag1 = data.getStringExtra("fragTag");
                        if(fragTag1.equals(fragTag)){
                            updateChildExNames(data.getStringExtra("MESSAGE"));
                            if(isAssistanceW531fB){
                                sendAssistance.setAssistanceExercise(data.getStringExtra("MESSAGE"));
                            }
                            exerciseNameView.setText(data.getStringExtra("MESSAGE"));
                            updateWorkoutStateFast();
                        }else{
                            updateExName.updateExName(fragTag1, data.getStringExtra("MESSAGE"));
                        }
                        //updateWorkoutStateFast();
                    }
                }
            }else if(resultCode == 3){
                if(data != null){
                    if(data.getStringArrayExtra("MESSAGE") != null){
                        /*
                         * What we'd have to do here is show a pop up for SxR@W and then add it to the key location that
                         * will be set from parent frag.
                         */
                    }
                }
            }else if(resultCode == 4){
                if(data != null){
                    if(data.getBooleanExtra("remove", false)){
                        removeFrag.removeFrag(fragTag);
                    }
                }
            }else if(resultCode == 5){
                // W531fB Ex Name Handling
                String fragTag1 = data.getStringExtra("fragTag");
                if(fragTag1.equals(fragTag)){
                    updateChildExNames(data.getStringExtra("MESSAGE"));
                    if(isAssistanceW531fB){
                        String exName = data.getStringExtra("MESSAGE");
                        if(isBodyweight(exName)){
                            // If ex is bodyweight we don't show dialog and just update child frags
                            sendAssistance.setAssistanceExercise(data.getStringExtra("MESSAGE"));
                            exerciseNameView.setText(data.getStringExtra("MESSAGE"));
                            updateChildWeights("", true);
                            updateWorkoutStateFast();
                        }else{
                            // If ex is not bodyweight show dialog
                            // We need to pass ex name into dialog
                            // Would be nice to pass set schemes in too...would have to gather them from child frags.
                            sendAssistance.setAssistanceExercise(data.getStringExtra("MESSAGE"));
                            exerciseNameView.setText(data.getStringExtra("MESSAGE"));
                            updateWorkoutStateFast();
                            Intent intent = new Intent(getActivity(), W531fBWeightDialog.class);
                            intent.putExtra("exName", exName);
                            intent.putExtra("templateName", templateName);
                            intent.putExtra("fragTag", fragTag);
                            startActivityForResult(intent, 5);
                        }
                    }
                }else{
                    updateExName.updateExName(fragTag1, data.getStringExtra("MESSAGE"));
                    updateWorkoutStateFast();
                }
            }
        }
    }

    public void updateChildWeights(String weight, boolean isBW){
        if(!repsWeightFragList1.isEmpty()){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                repsWeightWAFrag.updateWeights(weight, isBW);
            }
        }
        if(!repsWeightFragList2.isEmpty()){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                repsWeightWAFrag.updateWeights(weight, isBW);
            }
        }
    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }

    private void updateChildExNames(String exerciseName){
        if(!repsWeightFragList1.isEmpty()){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                /*
                 * We need reps frags to switch to bw if the ex is bw.
                 */
                repsWeightWAFrag.updateExName(exerciseName);
            }
        }
        if(!repsWeightFragList2.isEmpty()){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                /*
                 * We need reps frags to switch to bw if the ex is bw.
                 */
                repsWeightWAFrag.updateExName(exerciseName);
            }
        }
    }

    public void setExName(String exName){
        updateChildExNames(exName);
        if(isAssistanceW531fB){
            sendAssistance.setAssistanceExercise(exName);
        }
        exerciseNameView.setText(exName);
    }

    private void inflateFrags(){
        if(infoList.isEmpty()){
            repsWeightInc++;
            String tag = String.valueOf(repsWeightInc) + "rwSS_2";
            android.app.FragmentManager fragmentManager = getChildFragmentManager();
            android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            RepsWeightWAFrag repsWeightFrag = new RepsWeightWAFrag();
            repsWeightFrag.isUserImperial = isUserImperial;
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
            checkExNameForW531fB(infoList.get(0));

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
                                repsWeightFrag.isUserImperial = isUserImperial;
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
                                // we should be good here because this should only be triggered
                                // the first time and if you later add empty supersets it'll go
                                // elsewhere
                                if(!arrayList.get(i + 1).equals("0@0")){
                                    exNameSupersetInc++;
                                    String tag = String.valueOf(exNameSupersetInc) + "exSS";
                                    android.app.FragmentManager fragmentManager = getChildFragmentManager();
                                    android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    ExNameSSWAFrag exNameFrag = new ExNameSSWAFrag();
                                    exNameFrag.isUserImperial = isUserImperial;
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
                                }

                            } catch(IndexOutOfBoundsException e){

                            }
                        }
                        count++;
                        if(count == biggestSize){
                            //updateChildExNames(infoList.get(0));
                        }
                    }
                    checkIfAllAreCheckedWithDelay();
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
                    repsWeightFrag.isUserImperial = isUserImperial;
                    repsWeightFrag.exName = finalList.get(0).get(0);
                    repsWeightFrag.fragTag1 = tag;
                    fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                    fragmentTransaction.commitAllowingStateLoss();
                    repsWeightFragList2.add(repsWeightFrag);
                    fragmentManager.executePendingTransactions();
                    tagListWA.add(tag);
                }
                checkIfAllAreCheckedWithDelay();
            }
        }
    }

    private void inflateFragsFromEdit(ArrayList<List<String>> finalList, int biggestSize){

        exerciseNameView.setText(finalList.get(0).get(0));
        checkExNameForW531fB(finalList.get(0).get(0));

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
                        repsWeightFrag.isUserImperial = isUserImperial;
                        repsWeightFrag.isTemplateImperial = isTemplateImperial;
                        repsWeightFrag.repsWeightString = arrayList.get(i + 1);
                        repsWeightFrag.fragTag1 = tag;
                        repsWeightFrag.isEdit = true;
                        repsWeightFrag.exName = arrayList.get(0);
                        fragmentTransaction.add(R.id.repsWeightContainer, repsWeightFrag, tag);
                        fragmentTransaction.commitAllowingStateLoss();
                        fragmentManager.executePendingTransactions();
                        //repsWeightFrag.updateExName(arrayList.get(0));
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
                        exNameFrag.isUserImperial = isUserImperial;
                        exNameFrag.isTemplateImperial = isTemplateImperial;
                        ArrayList<String> newSubList = new ArrayList<>();
                        newSubList.add(arrayList.get(0));
                        newSubList.add(arrayList.get(i + 1));
                        exNameFrag.infoList.addAll(newSubList);
                        exNameFrag.fragTag2 = tag;
                        exNameFrag.isEdit = true;
                        exNameFrag.exName = arrayList.get(0);
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
                if(count == biggestSize){
                    //updateChildExNames(finalList.get(0).get(0));
                }
            }
            checkIfAllAreCheckedWithDelay();
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

    public String getExerciseName(){
        return exerciseNameView.getText().toString();
    }

    public String getHighestChildWeight(){
        String highestWeight = "0";

        List<String> parentExInfoList = new ArrayList<>();

        if(repsWeightFragList1.size() != 0){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                parentExInfoList.add(repsWeightWAFrag.getWeight());
            }
        }

        if(repsWeightFragList2.size() != 0){
            for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                parentExInfoList.add(repsWeightWAFrag.getWeight());
            }
        }


        if(!parentExInfoList.isEmpty()){
            for(String string : parentExInfoList){
                double firstWeight = Double.parseDouble(highestWeight);
                double secondWeight = Double.parseDouble(string);
                if(secondWeight > firstWeight){
                    highestWeight = String.valueOf(secondWeight);
                }
            }
        }

        return highestWeight;
    }

    public void setCheckedSuccess(String repsWeightTag){
        for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
            if(repsWeightWAFrag.fragTag1.equals(repsWeightTag)){
                repsWeightWAFrag.setCheckedView();
            }
        }
    }

    public void checkForSS(){
        boolean areAllChecked = true;
        try{
            if(!repsWeightFragList1.isEmpty()){
                for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                    if(!repsWeightWAFrag.isChecked()){
                        areAllChecked = false;
                    }
                }

                //updateWorkoutState.updateWorkoutStateWithDelay();
            }else{
                //checkOffAll.setChecked(false);
            }
        }catch (NullPointerException e){
            //areAllChecked = false;
        }
        try{
            if(!repsWeightFragList2.isEmpty()){
                for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                    if(!repsWeightWAFrag.isChecked()){
                        areAllChecked = false;
                    }
                }
                //updateWorkoutState.updateWorkoutStateWithDelay();
            }
        }catch (NullPointerException e){
            //checkOffAll.setChecked(false);
        }
        for(ExNameSSWAFrag exNameFrag : exNameSupersetFragList){
            if(!exNameFrag.isChecked()){
                areAllChecked = false;
                /*
                 * This is getting null/false the first time it's checked. Maybe do a boolean
                 * instead? idk.
                 */
            }
        }
        if(areAllChecked){
            checkOffAll.setChecked(true);
        }else{
            isComingFromReps = true;
            checkOffAll.setChecked(false);
        }
    }

    public void checkForNonSS(){
        boolean areAllChecked = true;
        try{
            if(!repsWeightFragList1.isEmpty()){
                for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                    if(!repsWeightWAFrag.isChecked()){
                        areAllChecked = false;
                    }
                }

                //updateWorkoutState.updateWorkoutStateWithDelay();
            }else{
                //checkOffAll.setChecked(false);
            }
        }catch (NullPointerException e){
            //checkOffAll.setChecked(false);
        }
        try{
            if(!repsWeightFragList2.isEmpty()){
                for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                    if(!repsWeightWAFrag.isChecked()){
                        areAllChecked = false;
                    }
                }
                //updateWorkoutState.updateWorkoutStateWithDelay();
            }
        }catch (NullPointerException e){
            //checkOffAll.setChecked(false);
        }
        if(areAllChecked){
            checkOffAll.setChecked(true);
        }else{
            isComingFromReps = true;
            checkOffAll.setChecked(false);
        }
    }

    public void checkIfAllAreCheckedWithDelay(){
        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(exNameSupersetFragList != null){
                    if(!exNameSupersetFragList.isEmpty()){
                        checkForSS();
                        //updateWorkoutState.updateWorkoutState();
                    }else{
                        checkForNonSS();
                    }
                }else{
                    checkForNonSS();
                }
            }
        }, 1000);


    }

    public void checkIfAllAreChecked(){

        // This is where the issue is. Clicking once checks them all off, but the main checkmark
        // stays unchecked, until you click it again, at which point the sets stay checked, but
        // the main checkmark finally becomes checked.

        // so what's happening is we're triggering a check every time we

        if(exNameSupersetFragList != null){
            if(!exNameSupersetFragList.isEmpty()){
                checkForSS();
                //updateWorkoutState.updateWorkoutState();
            }else{
                checkForNonSS();
            }
        }else{
            checkForNonSS();
        }
    }

    public void checkOffSS(){
        checkOffNonSS();
        if(exNameSupersetFragList != null){
            if(!exNameSupersetFragList.isEmpty()){
                try{
                    for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
                        exNameSSWAFrag.checkAllRepsWeight();
                    }
                }catch (NullPointerException e){
                    ////checkOffAll.setChecked(false);
                }
            }else{
                ////checkOffAll.setChecked(false);
            }
        }else{
            ////checkOffAll.setChecked(false);
        }
    }

    public void checkOffNonSS(){
        //checkOffAll.setChecked(true);
        if(repsWeightFragList1 != null){
            if(!repsWeightFragList1.isEmpty()){
                try{
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                        repsWeightWAFrag.setCheckedView();
                    }
                    //updateWorkoutState.updateWorkoutStateWithDelay();
                }catch (NullPointerException e){
                    //checkOffAll.setChecked(false);
                }
            }else{
                //checkOffAll.setChecked(false);
            }
        }else{
            //checkOffAll.setChecked(false);
        }
        if(repsWeightFragList2 != null){
            if(!repsWeightFragList2.isEmpty()){
                try{
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                        repsWeightWAFrag.setCheckedView();
                    }
                    //updateWorkoutState.updateWorkoutStateWithDelay();
                }catch (NullPointerException e){
                    //checkOffAll.setChecked(false);
                }
            }else{
                //checkOffAll.setChecked(false);
            }
        }else{
            //checkOffAll.setChecked(false);
        }
    }

    public void unCheckOffSS(){
        unCheckOffNonSS();
        if(exNameSupersetFragList != null){
            if(!exNameSupersetFragList.isEmpty()){//
                try{
                    for(ExNameSSWAFrag exNameSSWAFrag : exNameSupersetFragList){
                        exNameSSWAFrag.unCheckAllRepsWeight();
                    }
                }catch (NullPointerException e){
                    //checkOffAll.setChecked(false);
                }
            }else{
                //checkOffAll.setChecked(false);
            }
        }else{
            //checkOffAll.setChecked(false);
        }
    }

    public void unCheckOffNonSS(){
        if(repsWeightFragList1 != null){
            if(!repsWeightFragList1.isEmpty()){
                try{
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList1){
                        repsWeightWAFrag.setUnCheckedView();
                    }
                    //updateWorkoutState.updateWorkoutStateWithDelay();
                }catch (NullPointerException e){
                    //checkOffAll.setChecked(false);
                }
            }else{
                //checkOffAll.setChecked(false);
            }
        }else{
            //checkOffAll.setChecked(false);
        }
        if(repsWeightFragList2 != null){
            if(!repsWeightFragList2.isEmpty()){
                try{
                    for(RepsWeightWAFrag repsWeightWAFrag : repsWeightFragList2){
                        repsWeightWAFrag.setUnCheckedView();
                    }
                    //updateWorkoutState.updateWorkoutStateWithDelay();
                }catch (NullPointerException e){
                    //checkOffAll.setChecked(false);
                }
            }else{
                //checkOffAll.setChecked(false);
            }
        }else{
            //checkOffAll.setChecked(false);
        }
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
                    /*
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
                    updateWorkoutState.updateWorkoutStateWithDelay();
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
                    updateWorkoutState.updateWorkoutStateWithDelay();
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
                updateWorkoutState.updateWorkoutStateWithDelay();
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
        if(setsNumber == 0){
            setsNumber = 1;
        }
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
