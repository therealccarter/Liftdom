package com.liftdom.template_housing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.PremadeProgramModelClass;
import com.liftdom.workout_programs.PremadeProgramViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Brodin on 5/19/2020.
 */

public class PremadeProgramRecyclerAdapter extends RecyclerView.Adapter<PremadeProgramViewHolder> {

    private List<PremadeProgramModelClass> premadeList = new ArrayList<>();
    private FragmentActivity mActivity;

    public PremadeProgramRecyclerAdapter(List<PremadeProgramModelClass> list,
                                         FragmentActivity activity){
        premadeList = list;
        mActivity = activity;
    }

    @Override
    public PremadeProgramViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view =
                (LayoutInflater.from(parent.getContext())).inflate(R.layout.premade_program_list_item, parent, false);
        PremadeProgramViewHolder holder = new PremadeProgramViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(PremadeProgramViewHolder viewHolder, int position){
        viewHolder.setActivity(mActivity);
        viewHolder.setDescription(premadeList.get(position).getProgramDescription());
        viewHolder.setExperienceLevel(premadeList.get(position).getExperienceLevel());
        viewHolder.setTemplateName(premadeList.get(position).getProgramName());
        viewHolder.setWorkoutType(premadeList.get(position).getWorkoutType());
        viewHolder.setWorkoutCode(premadeList.get(position).getWorkoutCode());
    }

    @Override
    public int getItemCount(){
        return premadeList.size();
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView){
        super.onAttachedToRecyclerView(recyclerView);
    }
}
