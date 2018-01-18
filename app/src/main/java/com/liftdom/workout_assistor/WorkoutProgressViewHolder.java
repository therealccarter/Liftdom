package com.liftdom.workout_assistor;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import com.liftdom.liftdom.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Brodin on 1/16/2018.
 */

public class WorkoutProgressViewHolder extends RecyclerView.ViewHolder {

    private final LinearLayout mParentLL;
    private WorkoutProgressModelClass mWorkoutProgressModel;
    private Fragment mFragmentParent;
    int exNameInc = 0;
    FragmentTransaction mFragmentTransaction;

    public WorkoutProgressViewHolder(View itemView){
        super(itemView);
        mParentLL = (LinearLayout) itemView.findViewById(R.id.assistorInfoLL);
    }

    public void setFragmentTransaction(FragmentTransaction fragmentTransaction){
        mFragmentTransaction = fragmentTransaction;
    }

    public void setWorkoutProgressModel(WorkoutProgressModelClass workoutProgressModel){
        mWorkoutProgressModel = workoutProgressModel;
        setUpViews();
    }

    public void setFragment(Fragment fragment){
        mFragmentParent = fragment;
    }

    private void setUpViews(){
        Log.i("assistorInfo", "setUpViews in recycler view");
        HashMap<String, HashMap<String, List<String>>> runningMap = mWorkoutProgressModel.getExInfoHashMap();
        boolean isTemplateImperial1 = mWorkoutProgressModel.isIsTemplateImperial();

        for(int i = 0; i < runningMap.size(); i++){
            //if(i == 0){
            //    loadingView.setVisibility(View.GONE);
            //    serviceCardView.setVisibility(View.VISIBLE);
            //}
            for(Map.Entry<String, HashMap<String, List<String>>> entry : runningMap.entrySet()) {
                if(isOfIndex(i, entry.getKey())){
                    exNameInc++;
                    String tag = String.valueOf(exNameInc) + "ex";
                    HashMap<String, List<String>> exerciseMap = entry.getValue();
                    android.app.FragmentTransaction fragmentTransaction = mFragmentParent.getChildFragmentManager()
                            .beginTransaction();
                    ExNameWAFrag exNameFrag = new ExNameWAFrag();
                    exNameFrag.isTemplateImperial = isTemplateImperial1;
                    exNameFrag.isEditInfoList = exerciseMap;
                    exNameFrag.fragTag = tag;
                    exNameFrag.isEdit = true;
                    //if (!getActivity().isFinishing()) {
                        fragmentTransaction.add(mParentLL.getId(), exNameFrag, tag);
                        fragmentTransaction.commitAllowingStateLoss();
                        //mFragmentParent.getChildFragmentManager().executePendingTransactions();
                        //exNameFragList.add(exNameFrag);
                    //}
                }
            }
        }
    }

    private boolean isOfIndex(int index, String key){
        String delims = "[_]";
        String tokens[] = key.split(delims);
        int index2 = Integer.valueOf(tokens[0]);
        if(index + 1 == index2){
            return true;
        }else{
            return false;
        }
    }
}
