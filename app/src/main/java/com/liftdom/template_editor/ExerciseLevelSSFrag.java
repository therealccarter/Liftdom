package com.liftdom.template_editor;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

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

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), PercentageOptionsDialog.class);
                intent.putExtra("isImperial", String.valueOf(TemplateEditorSingleton.getInstance()
                        .isCurrentUserImperial));
                intent.putExtra("isFrom", "exLevel");
                startActivityForResult(intent, 3);
            }
        });

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
                    if(isBodyweight(message)) {
                        if (!setSchemeList.isEmpty()) {
                            for(int i = 0; i < setSchemeList.size(); i++){
                                InputFilter[] filterArray = new InputFilter[1];
                                filterArray[0] = new InputFilter.LengthFilter(4);
                                setSchemeList.get(i).weightEditText.setFilters(filterArray);
                                setSchemeList.get(i).weightEditText.setText("B.W.");
                                setSchemeList.get(i).units.setVisibility(View.GONE);
                                setSchemeList.get(i).weightEditText.setEnabled(false);
                            }
                        }
                    }else{
                        if (!setSchemeList.isEmpty()) {
                            for(int i = 0; i < setSchemeList.size(); i++){
                                InputFilter[] filterArray = new InputFilter[1];
                                filterArray[0] = new InputFilter.LengthFilter(3);
                                setSchemeList.get(i).weightEditText.setFilters(filterArray);
                                if(setSchemeList.get(i).weightEditText.getText().toString().equals("B.W.")){
                                    setSchemeList.get(i).weightEditText.setText("");
                                }
                                setSchemeList.get(i).units.setVisibility(View.VISIBLE);
                                setSchemeList.get(i).weightEditText.setEnabled(true);
                            }
                        }
                    }
                }
            }else if(requestCode == 3 && resultCode == 8){
                if(!setSchemeList.isEmpty()){
                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                        setsLevelChildFrag.setWeightToPercentAndSetWeightText(data.getStringExtra("weightResult"));
                    }
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
