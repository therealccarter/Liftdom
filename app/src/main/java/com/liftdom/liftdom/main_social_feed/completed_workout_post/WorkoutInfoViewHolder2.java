package com.liftdom.liftdom.main_social_feed.completed_workout_post;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 11/30/2017.
 */

public class WorkoutInfoViewHolder2 extends RecyclerView.ViewHolder{

    private final LinearLayout mParentLinearLayout;

    public WorkoutInfoViewHolder2(View itemView){
        super(itemView);
        mParentLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearLayout1);
    }
}
