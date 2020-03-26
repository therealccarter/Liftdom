package com.liftdom.charts_stats_tools.tools;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.ExerciseMaxesModelClass;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Brodin on 3/26/2020.
 */
public class MaxesAdapter extends RecyclerView.Adapter<ExerciseMaxesViewHolder> {

    ArrayList<ExerciseMaxesModelClass> modelClassHashMap;
    boolean mIsImperial;
    FragmentActivity fragmentActivity;

    public MaxesAdapter (ArrayList<ExerciseMaxesModelClass> modelClasses,
                         boolean isImperial){
        modelClassHashMap = modelClasses;
        mIsImperial = isImperial;

    }

    @Override
    public ExerciseMaxesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exercise_maxes_list_item, parent, false);

        return new ExerciseMaxesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ExerciseMaxesViewHolder holder, int position){
        holder.setDate(modelClassHashMap.get(position).getDate());
        holder.setExerciseName(modelClassHashMap.get(position).getExerciseName());
        holder.setIsImperial(modelClassHashMap.get(position).isIsImperial());
        holder.setIsImperialPOV(mIsImperial);
        holder.setMaxValue(modelClassHashMap.get(position).getMaxValue());
        holder.setFragmentActivity(fragmentActivity);
    }

    public FragmentActivity getFragmentActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }

    @Override
    public int getItemCount(){
        return modelClassHashMap.size();
    }
}
