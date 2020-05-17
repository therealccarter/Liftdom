package com.liftdom.workout_programs;

import android.content.Intent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.FiveThreeOne_ForBeginners.W531fBHolderActivity;
import com.liftdom.workout_programs.PPL_Reddit.PPLRedditHolderActivity;
import com.liftdom.workout_programs.Smolov.SmolovHolderActivity;

/**
 * Created by Brodin on 5/17/2020.
 */
public class PremadeProgramViewHolder extends RecyclerView.ViewHolder{

    private final TextView templateNameView;
    private final TextView descriptionView;
    private final LinearLayout parentLL;
    private final TextView experienceLevelView;
    private final TextView workoutTypeView;
    private String mWorkoutCode;
    private FragmentActivity mActivity;

    public PremadeProgramViewHolder(View itemView){
        super(itemView);

        templateNameView = itemView.findViewById(R.id.templateName);
        descriptionView = itemView.findViewById(R.id.descriptionView);
        parentLL = itemView.findViewById(R.id.parentLL);
        experienceLevelView = itemView.findViewById(R.id.experienceLevelView);
        workoutTypeView = itemView.findViewById(R.id.workoutTypeView);

        parentLL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(getWorkoutCode() != null){
                    String workoutCode = getWorkoutCode();

                    if(workoutCode.equals("Smolov")){
                        Intent intent;
                        intent = new Intent(mActivity, SmolovHolderActivity.class);
                        mActivity.startActivity(intent);
                    }else if(workoutCode.equals("W531fB")){
                        Intent intent;
                        intent = new Intent(mActivity, W531fBHolderActivity.class);
                        mActivity.startActivity(intent);
                    }else if(workoutCode.equals("PPLReddit")){
                        Intent intent;
                        intent = new Intent(mActivity, PPLRedditHolderActivity.class);
                        mActivity.startActivity(intent);
                    }
                }
            }
        });
    }

    public void setActivity(FragmentActivity fragmentActivity){
        mActivity = fragmentActivity;
    }

    public void setTemplateName(String templateName){
        templateNameView.setText(templateName);
    }

    public void setDescription(String description){
        descriptionView.setText(description);
    }

    public void setExperienceLevel(String experienceLevel){
        experienceLevelView.setText(experienceLevel);
    }

    public void setWorkoutType(String workoutType){
        workoutTypeView.setText(workoutType);
    }

    public String getWorkoutCode() {
        return mWorkoutCode;
    }

    public void setWorkoutCode(String mWorkoutCode) {
        this.mWorkoutCode = mWorkoutCode;
    }
}
