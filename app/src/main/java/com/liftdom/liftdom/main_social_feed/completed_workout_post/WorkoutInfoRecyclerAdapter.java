package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.liftdom.liftdom.R;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brodin on 6/29/2017.
 */

public class WorkoutInfoRecyclerAdapter extends RecyclerView.Adapter<WorkoutInfoViewHolder>{

    List<String> mWorkoutInfoList = Collections.emptyList();
    Context mContext;

    public WorkoutInfoRecyclerAdapter(List<String> list, Context context){
        this.mWorkoutInfoList = list;
        this.mContext = context;
    }

    @Override
    public WorkoutInfoViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = (LayoutInflater.from(parent.getContext())).inflate(R.layout.workout_info_list_item, parent, false);
        WorkoutInfoViewHolder holder = new WorkoutInfoViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(WorkoutInfoViewHolder viewHolder, int position){

    }

    @Override
    public int getItemCount(){
        return mWorkoutInfoList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
