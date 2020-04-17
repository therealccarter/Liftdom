package com.liftdom.template_editor;


import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
public class ExerciseLevelSSFrag extends android.app.Fragment
        implements SetsLevelSSFrag.updateCallback,
                    SetsLevelSSFrag.withinCallback{

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

    public interface updateCallback{
        void updateNode();
    }

    public interface withinCallback{
        void fromWithin();
    }

    private withinCallback fromWithinCallback;
    updateCallback mUpdate;
    private removeFragCallback2 removeFragCallback;
    private getParentEx getParentExCallback;
    private getParentDoW getParentDoWCallback;

    public void updateNode(){
        mUpdate.updateNode();
    }

    public void fromWithin(){
        fromWithinCallback.fromWithin();
    }

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
        mUpdate = (updateCallback) getParentFragment();
        fromWithinCallback = (withinCallback) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFragCallback.removeFrag2(fragTag);
            }
        });

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fromWithinCallback.fromWithin();
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
                if(i == initialSchemeCount - 1){
                    superSetSetFrag.isLastOne = true;
                }
                fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
                fragmentTransaction.commitAllowingStateLoss();
                setSchemeList.add(superSetSetFrag);
            }
        }

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromWithinCallback.fromWithin();
                //Intent intent = new Intent(getActivity(), PercentageOptionsDialog.class);
                //intent.putExtra("isImperial", String.valueOf(TemplateEditorSingleton.getInstance()
                //        .isCurrentUserImperial));
                //intent.putExtra("isFrom", "exLevel");
                //startActivityForResult(intent, 3);
                Intent intent = new Intent(v.getContext(), ExSSLevelOptionsDialog.class);
                String exName = getExerciseValueFormatted();
                intent.putExtra("exName", exName);
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
                                //InputFilter[] filterArray = new InputFilter[1];
                                //filterArray[0] = new InputFilter.LengthFilter(4);
                                //setSchemeList.get(i).weightEditText.setFilters(filterArray);
                                //setSchemeList.get(i).weightEditText.setText("B.W.");
                                //setSchemeList.get(i).units.setVisibility(View.GONE);
                                //setSchemeList.get(i).weightEditText.setEnabled(false);
                                setSchemeList.get(i).setWeightToBW();
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
                                    setSchemeList.get(i).setToDefaultWeightBoolean();
                                }
                                setSchemeList.get(i).units.setVisibility(View.VISIBLE);
                                setSchemeList.get(i).weightEditText.setEnabled(true);
                            }
                        }
                    }
                    mUpdate.updateNode();
                }
            }else if(requestCode == 3){
                if(resultCode == 1){
                    fromWithinCallback.fromWithin();
                    Intent intent = new Intent(getActivity(), PercentageOptionsDialog.class);
                    intent.putExtra("isImperial", String.valueOf(TemplateEditorSingleton.getInstance()
                            .isCurrentUserImperial));
                    intent.putExtra("isFrom", "exLevel");
                    startActivityForResult(intent, 3);
                }else if(resultCode == 2){
                    fromWithinCallback.fromWithin();
                    Intent intent = new Intent(getActivity(), ExtraOptionsDialog.class);

                    String isPercentage = "false";
                    String isAmrap = "false";
                    String isToFailure = "false";
                    String isBW = "false";
                    if(!setSchemeList.isEmpty()){
                        isPercentage = String.valueOf(setSchemeList.get(0).isPercentage);
                        isAmrap = String.valueOf(setSchemeList.get(0).isAmrap);
                        isToFailure = String.valueOf(setSchemeList.get(0).isToFailure);
                        isBW = String.valueOf(setSchemeList.get(0).isBW);
                    }

                    intent.putExtra("isPercentageString", isPercentage);
                    intent.putExtra("isAmrap", isAmrap);
                    intent.putExtra("isToFailure", isToFailure);
                    intent.putExtra("isBW", isBW);

                    intent.putExtra("isOverall", "true");
                    startActivityForResult(intent, 7);
                }else if(resultCode == 8){
                    if(!setSchemeList.isEmpty()){ // if result is 8?
                        for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                            setsLevelChildFrag.setWeightToPercentAndSetWeightText(data.getStringExtra("weightResult"));
                        }
                        mUpdate.updateNode();
                    }
                }
            }else if(requestCode == 7){
                if(resultCode == 3){
                    if(data != null){
                        if(data.getStringExtra("MESSAGE1") != null) {
                            String message = data.getStringExtra("MESSAGE1");
                            if(message.equals("bodyweight")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.setWeightToBW();
                                            setsLevelChildFrag.changeRepsIMEtoFinish();
                                            setsLevelChildFrag.setToBWBoolean();
                                        }
                                    }
                                }
                            }else if(message.equals("defaultWeight")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.setWeightToDefault();
                                            setsLevelChildFrag.setToDefaultWeightBoolean();
                                        }
                                    }
                                }
                            }else if(message.equals("percentage")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.weightLL.setVisibility(View.GONE);
                                            setsLevelChildFrag.percentageLL.setVisibility(View.VISIBLE);
                                            setsLevelChildFrag.setToPercentageBoolean();
                                        }
                                    }
                                }
                            }
                        }
                        if(data.getStringExtra("MESSAGE2") != null) {
                            String message = data.getStringExtra("MESSAGE2");
                            if(message.equals("to failure")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.setRepsToFailure();
                                            setsLevelChildFrag.setToFailureBoolean();
                                        }
                                    }
                                }
                            }else if(message.equals("defaultReps")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.setRepsToDefault();
                                            setsLevelChildFrag.setToDefaultRepsBoolean();
                                        }
                                    }
                                }
                            }else if(message.equals("amrap")){
                                if(!setSchemeList.isEmpty()){
                                    for(SetsLevelSSFrag setsLevelChildFrag : setSchemeList){
                                        if(setsLevelChildFrag.amrap != null){
                                            setsLevelChildFrag.amrap.setVisibility(View.VISIBLE);
                                            setsLevelChildFrag.setToAmrapBoolean();
                                        }
                                    }
                                }
                            }
                        }

                        mUpdate.updateNode();
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
                //if(!setsLevelSSFrag.getInfoList().equals("0x0@0")){
                    infoList.add(setsLevelSSFrag.getInfoList());
                //}
            }
        }

        return infoList;
    }

    public List<String> getSupersetInfoListRunning(){
        List<String> infoList = new ArrayList<>();

        infoList.add(getExerciseValueFormatted());

        for(SetsLevelSSFrag setsLevelSSFrag : setSchemeList){
            //if(!setsLevelSSFrag.getInfoList().equals("0x0@0")){
            infoList.add(setsLevelSSFrag.getInfoList());
            //}
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
        if(!setSchemeList.isEmpty()){
            boolean isBW = false;
            boolean isToFailure = false;
            boolean isAmrap = false;
            boolean isPercentage = false;
            String percentageWeight = "";
            for(int i = 0; i < setSchemeList.size(); i++){
                if(setSchemeList.get(i).amrap != null){
                    isBW = setSchemeList.get(i).isBW;
                    isToFailure =
                            setSchemeList.get(i).isToFailure;
                    isAmrap =
                            setSchemeList.get(i).isAmrap;
                    isPercentage =
                            setSchemeList.get(i).isPercentage;
                    if(isPercentage){
                        percentageWeight =
                                setSchemeList.get(i).percentageWeightButton.getText().toString();
                    }
                }
            }
            superSetSetFrag.isBW = isBW;
            superSetSetFrag.isToFailure = isToFailure;
            superSetSetFrag.isAmrap = isAmrap;
            superSetSetFrag.isPercentage = isPercentage;
            if(isPercentage){
                superSetSetFrag.percentageWeight = percentageWeight;
            }
            superSetSetFrag.isLastOne = true;
        }
        fragmentTransaction.add(R.id.superSetSchemeHolder, superSetSetFrag, fragString);
        fragmentTransaction.commitAllowingStateLoss();
        setSchemeList.add(superSetSetFrag);
    }

    public void removeSetScheme(String tag){
        try{
            String fragString = "sss" + tag;
            int tagInt = Integer.parseInt(tag);
            FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
            fragmentTransaction.remove(getChildFragmentManager().findFragmentByTag(fragString));
            fragmentTransaction.commitAllowingStateLoss();
            setSchemeList.remove(tagInt - 1);
        }catch (IndexOutOfBoundsException e){

        }
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
