package com.liftdom.template_editor;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
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

public class ExerciseLevelChildFrag extends android.app.Fragment
        implements SetsLevelChildFrag.setSchemesCallback,
                SetsLevelChildFrag.removeFragCallback,
                SuperSetExFrag.removeFragCallback2,
                SuperSetExFrag.getParentEx,
                SuperSetExFrag.getParentDoW{

    int fragIdCount2 = 0;
    int supersetFragCount = 0;

    Boolean isEdit = false;
    Boolean toastInvalidator = true;
    String exerciseName;
    String templateName;
    String selectedDaysReference;

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

    private doWCallback callback;
    private removeFragCallback removeFragCallback;
    private removeFragCallback2 removeFragCallback2;

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

        if(isEdit){

            exerciseButton.setText(fromEditList.get(0));

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
                        SuperSetExFrag superSetExFrag = new SuperSetExFrag();
                        superSetExFrag.fragTag = fragString;
                        superSetExFrag.isEdit = true;
                        superSetExFrag.isEditSetSchemeList = arrayList;
                        fragmentTransaction.add(R.id.superSetHolder, superSetExFrag, fragString);
                        fragmentTransaction.commitAllowingStateLoss();
                        hasSupersets = true;
                        superSetFragList.add(superSetExFrag);
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
                    for(SuperSetExFrag exFrag : superSetFragList){
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

    ArrayList<SuperSetExFrag> superSetFragList = new ArrayList<>();

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
                    if (message.equals("Pullups") || message.equals("Crunches")
                            || message.equals("Sit-ups")
                            || message.equals("Leg Raises")) {
                        if (!setsLevelChildFragAL.isEmpty()) {
                            setsLevelChildFragAL.get(0).weightEditText.setText("B.W.");
                            setsLevelChildFragAL.get(0).pounds.setVisibility(View.GONE);
                            setsLevelChildFragAL.get(0).weightEditText.setEnabled(false);
                        }
                    }
                }
            } else if(requestCode == 3){
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
                        SuperSetExFrag superSetExFrag = new SuperSetExFrag();
                        superSetExFrag.fragTag = fragString;
                        superSetExFrag.initialSchemeCount = fragIdCount2 + 1;
                        fragmentTransaction.add(R.id.superSetHolder, superSetExFrag, fragString);
                        fragmentTransaction.commitAllowingStateLoss();
                        hasSupersets = true;
                        superSetFragList.add(superSetExFrag);
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

            } else if(requestCode == 6){
                if(data.getStringExtra("exercise") != null){
                    String exerciseName = data.getStringExtra("exercise");

                }
            }
        }
    }

    @Override
    public void onPause(){
        super.onPause();

        //TODO: Get the all-ex algorithm both uploading and callbacking to every ex. Then we begin reading in of algo
        //TODO:  info

        if(EditTemplateAssemblerClass.getInstance().isOnSaveClick){
            if(!algorithmList.isEmpty()){
                algorithmList.add(getDoWValue());
                TemplateEditorSingleton.getInstance().mIsAlgorithm = true;
                TemplateEditorSingleton.getInstance().setAlgorithmList(getExerciseValueFormatted(), algorithmList);
                if(hasSupersets){
                    for(SuperSetExFrag exFrag : superSetFragList){
                        String exName = exFrag.getExerciseValueFormatted();
                        if(!exName.equals("Click to select exercise")){
                            List<String> stringList = new ArrayList<>();
                            stringList.addAll(algorithmList);
                            TemplateEditorSingleton.getInstance().setAlgorithmList(exFrag.getExerciseValueFormatted(), stringList);
                        }
                    }
                }
            }
            if(hasSupersets){
                List<String> supersetInfoList = new ArrayList<>();
                for(SuperSetExFrag exFrag : superSetFragList){
                    supersetInfoList.addAll(exFrag.getExInfo());
                }
                String exName = getExerciseValueFormatted();
                String days = getDoWValue();
                String cat = days + "+" + exName;

                TemplateEditorSingleton.getInstance().supersetInfoList.put(cat, supersetInfoList);
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
                for(SuperSetExFrag exFrag : superSetFragList){
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
