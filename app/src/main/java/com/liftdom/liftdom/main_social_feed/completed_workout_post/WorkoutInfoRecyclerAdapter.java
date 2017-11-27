package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.liftdom.liftdom.R;

import java.util.*;

/**
 * Created by Brodin on 6/29/2017.
 */

public class WorkoutInfoRecyclerAdapter extends RecyclerView.Adapter<WorkoutInfoViewHolder>{

    private List<String> mWorkoutInfoList = new ArrayList<>();
    private HashMap<String, List<String>> mWorkoutInfoMap;
    private Context mContext;
    private boolean isOriginallyImperial;
    private boolean isImperialPOV;

    public WorkoutInfoRecyclerAdapter(HashMap<String, List<String>> map, Context context){
        this.mWorkoutInfoMap = map;
        this.mContext = context;

        if(map != null){
            setInfoList(map);
        }
    }

    private void setInfoList(HashMap<String, List<String>> map){
        for(int i = 0; i < map.size(); i++){
            for(Map.Entry<String, List<String>> mapEntry : map.entrySet()){
                String[] keyTokens = mapEntry.getKey().split("_");
                if(Integer.parseInt(keyTokens[0]) == i){
                    List<String> list = new ArrayList<>();
                    list.addAll(mapEntry.getValue());
                    boolean isFirstEx = true;
                    boolean isFirstRepsWeight = true;
                    for(String string : list){
                        if(isExerciseName(string) && isFirstEx){
                            mWorkoutInfoList.add(string);
                            isFirstEx = false;
                        }else if(isExerciseName(string) && !isFirstEx){
                            mWorkoutInfoList.add(string + "_ss");
                            isFirstRepsWeight = false;
                        }else if(!isExerciseName(string) && isFirstRepsWeight){
                            mWorkoutInfoList.add(string);
                        }else if(!isExerciseName(string) && !isFirstRepsWeight){
                            mWorkoutInfoList.add(string);
                        }
                    }
                }
            }
        }
    }

    public boolean getIsImperialPOV() {
        return isImperialPOV;
    }

    public void setImperialPOV(boolean imperialPOV) {
        isImperialPOV = imperialPOV;
    }

    public boolean getIsOriginallyImperial() {
        return isOriginallyImperial;
    }

    public void setIsOriginallyImperial(boolean originallyImperial) {
        isOriginallyImperial = originallyImperial;
    }

    @Override
    public WorkoutInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.workout_info_list_item, parent, false);
        WorkoutInfoViewHolder holder = new WorkoutInfoViewHolder(view);
        holder.setIsOriginallyImperial(getIsOriginallyImperial());
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkoutInfoViewHolder viewHolder, int position){
        viewHolder.setInfoString(mWorkoutInfoList.get(position));
        viewHolder.setImperialPOV(getIsImperialPOV());
        viewHolder.setUpView();
    }

    @Override
    public int getItemCount(){
        return mWorkoutInfoList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }

    boolean isExerciseName(String input) {

        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
            if(input.length() > 5){
                String string = input.substring(0, 4);
                if(string.equals("T.F.")){
                    isExercise = false;
                }
            }
        }

        return isExercise;
    }

}
