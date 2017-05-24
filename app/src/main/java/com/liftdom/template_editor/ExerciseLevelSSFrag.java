package com.liftdom.template_editor;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class ExerciseLevelSSFrag extends android.app.Fragment {

    public ExerciseLevelSSFrag() {
        // Required empty public constructor
    }

    String fragTag;
    boolean isEdit;
    ArrayList<String> isEditSetSchemeList = new ArrayList<>();
    int initialSchemeCount = 0;
    ArrayList<SetsLevelSSFrag> setSchemeList = new ArrayList<>();

    public interface removeFragCallback2{
        void removeFrag2(String fragTag);
    }

    public interface getParentEx{
        String getParentEx1();
    }

    public interface getParentDoW{
        String getDoW1();
    }

    private removeFragCallback2 removeFragCallback;
    private getParentEx getParentExCallback;
    private getParentDoW getParentDoWCallback;

    // Butterknife
    @BindView(R.id.movementName) Button exerciseButton;
    @BindView(R.id.destroyFrag) ImageButton destroyFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_super_set_ex, container, false);

        ButterKnife.bind(this, view);

        removeFragCallback = (removeFragCallback2) getParentFragment();

        getParentExCallback = (getParentEx) getParentFragment();
        getParentDoWCallback = (getParentDoW) getParentFragment();


        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFragCallback.removeFrag2(fragTag);
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

        if(isEdit){
            int inc = 0;
            for(String string : isEditSetSchemeList){
                if(inc == 0){
                    exerciseButton.setText(string);
                }else{
                    String fragString = "sss" + Integer.toString(inc);
                    FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                    SetsLevelSSFrag superSetSetFrag = new SetsLevelSSFrag();
                    superSetSetFrag.isEdit = true;
                    superSetSetFrag.isEditSetScheme = string;
                    fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
                    fragmentTransaction.commitAllowingStateLoss();
                    setSchemeList.add(superSetSetFrag);
                }
                inc++;
            }
        }else{
            for(int i = 0; i < initialSchemeCount; i++){
                String fragString = "sss" + Integer.toString(i + 1);
                FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
                SetsLevelSSFrag superSetSetFrag = new SetsLevelSSFrag();
                fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
                fragmentTransaction.commitAllowingStateLoss();
                setSchemeList.add(superSetSetFrag);
            }
        }

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == 2) {
                if (data.getStringExtra("MESSAGE") != null) {
                    String message = data.getStringExtra("MESSAGE");
                    String newMessage = newLineExname(message);
                    exerciseButton.setText(newMessage);
                }
            }
        }
    }

    public List<String> getSupersetInfoList(){
        List<String> infoList = new ArrayList<>();
        if(!getExerciseValueFormatted().equals("Click to select exercise")){
            infoList.add(getExerciseValueFormatted());

            for(SetsLevelSSFrag setsLevelSSFrag : setSchemeList){
                infoList.add(setsLevelSSFrag.getInfoList());
            }
        }

        return infoList;
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

    public void addSetScheme(String tag){
        String fragString = "sss" + tag;
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        SetsLevelSSFrag superSetSetFrag = new SetsLevelSSFrag();
        fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
        fragmentTransaction.commitAllowingStateLoss();
        setSchemeList.add(superSetSetFrag);
    }

    public void removeSetScheme(String tag){
        String fragString = "sss" + tag;
        int tagInt = Integer.parseInt(tag);
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString));
        fragmentTransaction.commitAllowingStateLoss();
        setSchemeList.remove(tagInt - 1);
    }

    public List<String> getExInfo(){
        List<String> exInfoList = new ArrayList<>();
        exInfoList.add(getExerciseValueFormatted());
        for(SetsLevelSSFrag setsLevelFrag : setSchemeList){
            String setsInfo = setsLevelFrag.getInfoList();
            exInfoList.add(setsInfo);
        }
        return exInfoList;
    }

    public String getExerciseValueFormatted(){
        String spinnerText = exerciseButton.getText().toString();

        return spinnerText.replaceAll("\n", "");
    }

}