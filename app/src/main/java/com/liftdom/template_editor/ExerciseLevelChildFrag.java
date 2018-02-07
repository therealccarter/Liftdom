package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ExerciseLevelChildFrag extends android.app.Fragment
        implements SetsLevelChildFrag.setSchemesCallback,
                SetsLevelChildFrag.removeFragCallback,
                ExerciseLevelSSFrag.removeFragCallback2,
                ExerciseLevelSSFrag.getParentEx,
                ExerciseLevelSSFrag.getParentDoW{

    int fragIdCount2 = 0;
    int supersetFragCount = 0;

    Boolean isEdit = false;
    Boolean toastInvalidator = true;
    String exerciseName;
    String templateName;
    String selectedDaysReference;
    String editInitialDays;

    String fragTag;

    boolean hasSupersets = false;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    ArrayList<String> algoExAL = new ArrayList<>();
    List<String> algorithmList = new ArrayList<>();
    List<String> fromEditList;

    public ExerciseLevelChildFrag() {
        // Required empty public constructor
    }

    // Callback
    public interface doWCallback{
        String getDoW();
    }

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    public interface removeFragCallback2{
        void removeFrag2(String fragTag);
    }

    public interface setToGoldCallback{
        void setToGold();
    }

    public interface removeGoldCallback{
        void removeGold();
    }
    private doWCallback callback;
    private removeFragCallback removeFragCallback;
    private removeFragCallback2 removeFragCallback2;
    private setToGoldCallback setToGoldCallback;
    private removeGoldCallback removeGoldCallback;

    ArrayList<SetsLevelChildFrag> setsLevelChildFragAL = new ArrayList<>();

    ArrayList<String> stringSnapshotAL = new ArrayList<>();

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.addSet) Button addSet;
    @BindView(R.id.destroyFrag) ImageButton destroyFrag;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_exercise_level_child, container, false);

        ButterKnife.bind(this, view);

        if(TemplateEditorSingleton.getInstance().isAlgoApplyToAll){
            setToGoldFromDoW();
        }

        if(isEdit){
            for(Map.Entry<String, List<String>> map : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()){
                if(map.getValue().get(0).equals(fromEditList.get(0))){
                    if(map.getValue().get(10).equals(editInitialDays)){
                        algorithmList.clear();
                        //map.getValue().remove(10);
                        algorithmList.addAll(map.getValue());
                    }
                }
            }

            exerciseButton.setText(newLineExname(fromEditList.get(0)));
            if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()) {
                for (Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()) {
                    List<String> tempList = entry.getValue();
                    if (tempList.get(0).equals(getExerciseValueFormatted())) {
                        setToGoldFromDoW();
                    }
                }
            }
            // let's split this bitch up

            boolean isFirstEx = true;
            boolean isFirstSetSchemes = true;
            int inc = 0;
            int supersetInc = 0;
            ArrayList<ArrayList<String>> supersetList = new ArrayList<>();
            for(String string : fromEditList){
                inc++;
                if(inc != 1){
                    if(isExerciseName(string)){
                        // superset ex
                        isFirstSetSchemes = false;
                        ArrayList<String> list = new ArrayList<>();
                        list.add(string);
                        supersetList.add(list);
                        supersetInc++;
                    }else if(!isExerciseName(string) && isFirstSetSchemes){
                        // first set schemes
                        ++fragIdCount2;
                        String fragString2 = Integer.toString(fragIdCount2);
                        SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        setsLevelChildFragAL.add(frag1);
                        frag1.fragTag = fragString2;
                        frag1.isEdit = true;
                        frag1.setSchemeEdited = string;
                        fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                        if (getActivity() != null) {
                            fragmentTransaction.commitAllowingStateLoss();
                        }
                    }else if(!isExerciseName(string) && !isFirstSetSchemes){
                        // superset set schemes
                        supersetList.get(supersetInc - 1).add(string);
                    }
                }
                if(inc == fromEditList.size()){
                    for(ArrayList<String> arrayList : supersetList){
                        supersetFragCount++;
                        String fragString = "ss" + Integer.toString(supersetFragCount);
                        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                        ExerciseLevelSSFrag exerciseLevelSSFrag = new ExerciseLevelSSFrag();
                        exerciseLevelSSFrag.fragTag = fragString;
                        exerciseLevelSSFrag.isEdit = true;
                        exerciseLevelSSFrag.isEditSetSchemeList = arrayList;
                        fragmentTransaction.add(R.id.superSetHolder, exerciseLevelSSFrag, fragString);
                        fragmentTransaction.commitAllowingStateLoss();
                        hasSupersets = true;
                        superSetFragList.add(exerciseLevelSSFrag);
                    }
                }
            }
        }

        if(!isEdit){
            String fragString2 = Integer.toString(fragIdCount2);
            SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
            setsLevelChildFragAL.add(frag1);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
            fragmentTransaction.commit();
        }

        addSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ++fragIdCount2;
                FragmentManager fragmentManager = getFragmentManager();
                String fragString2 = Integer.toString(fragIdCount2);
                SetsLevelChildFrag frag1 = new SetsLevelChildFrag();
                setsLevelChildFragAL.add(frag1);
                frag1.fragTag = fragString2;
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                fragmentTransaction.add(R.id.LinearLayoutChild1, frag1, fragString2);
                fragmentTransaction.commit();
                fragmentManager.executePendingTransactions();

                if(!superSetFragList.isEmpty()){
                    for(ExerciseLevelSSFrag exFrag : superSetFragList){
                        exFrag.addSetScheme(fragString2);
                    }
                }


                CharSequence toastText = "Set-scheme Added";
                int duration = Toast.LENGTH_SHORT;

                try{
                    Snackbar snackbar = Snackbar.make(getView(), toastText, duration);
                    snackbar.show();
                } catch (NullPointerException e){

                }
            }
        });

        removeFragCallback = (removeFragCallback) getParentFragment();
        setToGoldCallback = (setToGoldCallback) getActivity();
        removeGoldCallback = (removeGoldCallback) getActivity();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFragCallback.removeFrag(fragTag);
                Log.i("tag", "fragbutton pushed");
            }
        });

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                int exID = exerciseButton.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), AlgorithmOrSuperSetDialog.class);
                String exName = getExerciseValueFormatted();
                intent.putExtra("exName", exName);
                startActivityForResult(intent, 3);
            }
        });


        callback = (doWCallback) getParentFragment();

        return view;
    }

    ArrayList<ExerciseLevelSSFrag> superSetFragList = new ArrayList<>();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 2
        if(resultCode == 5){
            extraOptionsButton.setImageResource(R.drawable.three_dot_menu);
        }
        if(data != null){
            if (requestCode == 2) {
                if (data.getStringExtra("MESSAGE") != null) {
                    String message = data.getStringExtra("MESSAGE");
                    String newMessage = newLineExname(message);
                    exerciseButton.setText(newMessage);
                    if(isBodyweight(message)) {
                        if (!setsLevelChildFragAL.isEmpty()) {
                            for(int i = 0; i < setsLevelChildFragAL.size(); i++){
                                InputFilter[] filterArray = new InputFilter[1];
                                filterArray[0] = new InputFilter.LengthFilter(4);
                                setsLevelChildFragAL.get(i).weightEditText.setFilters(filterArray);
                                setsLevelChildFragAL.get(i).weightEditText.setText("B.W.");
                                setsLevelChildFragAL.get(i).units.setVisibility(View.GONE);
                                setsLevelChildFragAL.get(i).weightEditText.setEnabled(false);
                            }
                        }
                    }else{
                        if (!setsLevelChildFragAL.isEmpty()) {
                            for(int i = 0; i < setsLevelChildFragAL.size(); i++){
                                InputFilter[] filterArray = new InputFilter[1];
                                filterArray[0] = new InputFilter.LengthFilter(3);
                                setsLevelChildFragAL.get(i).weightEditText.setFilters(filterArray);
                                //setsLevelChildFragAL.get(i).weightEditText.setText("");
                                if(setsLevelChildFragAL.get(i).weightEditText.getText().toString().equals("B.W.")){
                                    setsLevelChildFragAL.get(i).weightEditText.setText("");
                                }
                                setsLevelChildFragAL.get(i).units.setVisibility(View.VISIBLE);
                                setsLevelChildFragAL.get(i).weightEditText.setEnabled(true);
                            }
                        }
                    }
                }
            } else if(requestCode == 3){
                if(resultCode == 7){
                    // handle set scheme choice
                    Intent intent = new Intent(getActivity(), PercentageOptionsDialog.class);
                    intent.putExtra("isImperial", String.valueOf(TemplateEditorSingleton.getInstance()
                            .isCurrentUserImperial));
                    intent.putExtra("isFrom", "exLevel");
                    startActivityForResult(intent, 3);
                }else if(resultCode == 8){
                    if(!setsLevelChildFragAL.isEmpty()){
                        for(SetsLevelChildFrag setsLevelChildFrag : setsLevelChildFragAL){
                            setsLevelChildFrag.setWeightToPercentAndSetWeightText(data.getStringExtra("weightResult"));
                        }
                    }
                }else{
                    if(data.getStringExtra("choice") != null){
                        String message = data.getStringExtra("choice");
                        if(message.equals("algo")){
                            Intent intent = new Intent(getActivity(), AlgorithmSelectorActivity.class);
                            String exName = getExerciseValueFormatted();
                            intent.putExtra("exName", exName);
                            if(getDoWValue() != null){
                                intent.putExtra("day", getDoWValue());
                            }
                            startActivityForResult(intent, 4);
                        }else if(message.equals("superset")){
                            supersetFragCount++;
                            String fragString = "ss" + Integer.toString(supersetFragCount);
                            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                            ExerciseLevelSSFrag exerciseLevelSSFrag = new ExerciseLevelSSFrag();
                            exerciseLevelSSFrag.fragTag = fragString;
                            exerciseLevelSSFrag.initialSchemeCount = setsLevelChildFragAL.size();
                            fragmentTransaction.add(R.id.superSetHolder, exerciseLevelSSFrag, fragString);
                            fragmentTransaction.commitAllowingStateLoss();
                            hasSupersets = true;
                            superSetFragList.add(exerciseLevelSSFrag);
                        }
                    }
                }
            } else if(requestCode == 4){
                if(data.getStringArrayListExtra("list") != null){
                    ArrayList<String> arrayList = data.getStringArrayListExtra("list");
                    if(!algorithmList.isEmpty()){
                        algorithmList.clear();
                    }
                    algorithmList = ((List<String>) arrayList);

                    try{
                        Snackbar snackbar = Snackbar.make(getView(), "Algorithm added", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    } catch (NullPointerException e){

                    }
                    extraOptionsButton.setImageResource(R.drawable.three_dot_menu_gold);
                }
                if(data.getStringExtra("applyToAll") != null){
                    if(data.getStringExtra("applyToAll").equals("yes")){
                        setToGoldCallback.setToGold();
                    }
                }
                if(data.getStringExtra("wasApplyToAll") != null){
                    if(data.getStringExtra("wasApplyToAll").equals("yes")){
                        removeGoldCallback.removeGold();
                        setToGoldFromDoW();
                    }
                }

            } else if(requestCode == 6){
                if(data.getStringExtra("exercise") != null){
                    String exerciseName = data.getStringExtra("exercise");

                }
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

    public void setToGoldFromDoW(){
        extraOptionsButton.setImageResource(R.drawable.three_dot_menu_gold);
    }

    public void removeGoldFromDoW(){
        extraOptionsButton.setImageResource(R.drawable.three_dot_menu);
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onPause(){
        super.onPause();

        if(EditTemplateAssemblerClass.getInstance().isOnSaveClick){

            List<String> infoList = new ArrayList<>();
            if(!getExerciseValueFormatted().equals("Click to select exercise")){
                infoList.add(getExerciseValueFormatted());

                for(SetsLevelChildFrag setsLevelChildFrag : setsLevelChildFragAL){
                    if(!setsLevelChildFrag.getSetSchemeString().equals("")){
                        String delims = "[x, @]";
                        String[] schemeTokens = setsLevelChildFrag.getSetSchemeString().split(delims);
                        if(schemeTokens.length == 3){
                            infoList.add(setsLevelChildFrag.getSetSchemeString());
                        }
                    }
                }

                for(ExerciseLevelSSFrag exerciseLevelSSFrag : superSetFragList){
                    if(!exerciseLevelSSFrag.getSupersetInfoList().isEmpty()){
                        infoList.addAll(exerciseLevelSSFrag.getSupersetInfoList());
                    }
                }

                TemplateEditorSingleton.getInstance().setValues2(getDoW1(), infoList);
            }

            if(!algorithmList.isEmpty()){
                if(algorithmList.size() > 10){
                    algorithmList.set(10, getDoWValue());
                }else{
                    algorithmList.add(getDoWValue());
                }
                TemplateEditorSingleton.getInstance().mIsAlgorithm = true;
                TemplateEditorSingleton.getInstance().setAlgorithmList(getExerciseValueFormatted(), algorithmList);
                if(hasSupersets){
                    for(ExerciseLevelSSFrag exFrag : superSetFragList){
                        String exName = exFrag.getExerciseValueFormatted();
                        if(!exName.equals("Click to select exercise")){
                            List<String> stringList = new ArrayList<>();
                            stringList.addAll(algorithmList);
                            stringList.add("ss");
                            TemplateEditorSingleton.getInstance().setAlgorithmList(exFrag.getExerciseValueFormatted(), stringList);
                        }
                    }
                }
            }
        }
    }

    public String getParentEx1(){
        return exerciseButton.getText().toString();
    }

    public String newLineExname(String exerciseName){
        String newExNameString = "null";
        if(exerciseName.length() > 21){
            String exerciseName1 = exerciseName.substring(0, Math.min(exerciseName.length(), 21));
            String exerciseName2 = exerciseName.substring(21, exerciseName.length());
            newExNameString = exerciseName1 + "\n" + exerciseName2;
        } else{
            newExNameString = exerciseName;
        }
        return newExNameString;
    }

    public void removeFrag(String tag){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(fragIdCount2 != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                String fragString2 = Integer.toString(fragIdCount2);
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                for(ExerciseLevelSSFrag exFrag : superSetFragList){
                    exFrag.removeSetScheme(fragString2);
                }
                --fragIdCount2;
            }
        }
    }

    public void removeFrag2(String tag){
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        if(supersetFragCount != 0){
            if(getChildFragmentManager().findFragmentByTag(tag) != null){
                fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(tag)).commit();
                superSetFragList.remove(supersetFragCount - 1);
                --supersetFragCount;
                if(supersetFragCount == 0){
                    hasSupersets = false;
                }
            }
        }
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

    public String getDoWValue(){
        String doWSelected = callback.getDoW();
        return doWSelected;
    }

    public String getDoW1(){
        return getDoWValue();
    }

    public String getExerciseValueFormatted(){
        String spinnerText = exerciseButton.getText().toString();

        return spinnerText.replaceAll("\n", "");
    }

    private String stringSize(){
        int intSize = TemplateEditorSingleton.getInstance().mAlgorithmInfo.size();
        intSize++;

        String stringVersion = String.valueOf(intSize);
        return stringVersion;
    }




}
