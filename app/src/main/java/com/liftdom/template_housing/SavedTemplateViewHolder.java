package com.liftdom.template_housing;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 5/16/2017.
 */

public class SavedTemplateViewHolder extends RecyclerView.ViewHolder{

    private final TextView mTemplateNameView;
    private final TextView mTimeStampView;
    private final TextView mDaysView;
    private final TextView mDescriptionView;

    public SavedTemplateViewHolder(View itemView){
        super(itemView);
        mTemplateNameView = (TextView) itemView.findViewById(R.id.templateName);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mDaysView = (TextView) itemView.findViewById(R.id.daysView);
        mDescriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
    }

    public void setTemplateNameView(String templateName){
        mTemplateNameView.setText(templateName);
    }

    public void setTimeStampView(String timeStamp){
        mTimeStampView.setText(timeStamp);
    }

    public void setDaysView(String days){
        String daysFormatted = "/";
        String delims = "[_]";
        String[] tokens = days.split(delims);
        for(String string : tokens){
            daysFormatted = daysFormatted + string + "/";
        }

        mDaysView.setText(daysFormatted);
    }

    public void setDescriptionView(String description){
        mDescriptionView.setText(description);
    }
}
