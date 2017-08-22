package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.CompletedExercisesModelClass;

import java.util.Collections;
import java.util.List;

/**
 * Created by Brodin on 6/28/2017.
 */

public class CompleteWorkoutRecyclerAdapter extends RecyclerView.Adapter<CompletedWorkoutViewHolder>{

    List<CompletedWorkoutModelClass> mCompletedWorkoutList = Collections.emptyList();
    Context mContext;
    DatabaseReference mRootRef;
    FragmentActivity mActivity;
    boolean isOriginallyImperial;

    public CompleteWorkoutRecyclerAdapter(List<CompletedWorkoutModelClass> list, Context context, FragmentActivity activity){
        this.mCompletedWorkoutList = list;
        this.mContext = context;
        this.mRootRef = FirebaseDatabase.getInstance().getReference();
        this.mActivity = activity;
    }

    @Override
    public CompletedWorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.completed_workout_list_item, parent,
                false);
        CompletedWorkoutViewHolder holder = new CompletedWorkoutViewHolder(view);
        return holder;
    }

    public boolean getIsOriginallyImperial() {
        return isOriginallyImperial;
    }

    public void setOriginallyImperial(boolean originallyImperial) {
        isOriginallyImperial = originallyImperial;
    }

    @Override
    public void onBindViewHolder(CompletedWorkoutViewHolder viewHolder, int position){
        viewHolder.setUserName(mCompletedWorkoutList.get(position).getUserName());
        viewHolder.setUserLevel(mCompletedWorkoutList.get(position).getUserId(), mRootRef);
        viewHolder.setActivity(mActivity);
        viewHolder.setUserId(mCompletedWorkoutList.get(position).getUserId());
        viewHolder.setUpProfilePics(mCompletedWorkoutList.get(position).getUserId());
        viewHolder.setPublicDescription(mCompletedWorkoutList.get(position).getPublicDescription());
        viewHolder.setTimeStamp(mCompletedWorkoutList.get(position).getDateTime());
        viewHolder.setPostInfo(mCompletedWorkoutList.get(position).getWorkoutInfoMap(), mActivity, mContext,
                mCompletedWorkoutList.get(position).isIsImperial());
        viewHolder.setActivity(mActivity);
        viewHolder.setRefKey(mCompletedWorkoutList.get(position).getRef());
        viewHolder.setCommentRecycler(mCompletedWorkoutList.get(position).getRef());
    }

    @Override
    public int getItemCount(){
        return mCompletedWorkoutList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
