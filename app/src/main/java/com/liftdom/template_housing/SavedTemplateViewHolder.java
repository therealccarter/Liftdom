package com.liftdom.template_housing;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.liftdom.liftdom.R;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Brodin on 5/16/2017.
 */

public class SavedTemplateViewHolder extends RecyclerView.ViewHolder{

    private final TextView mTemplateNameView;
    private final TextView mTimeStampView;
    private final TextView mDaysView;
    private final TextView mDescriptionView;
    private final LinearLayout mTemplateHeader;
    private FragmentActivity mActivity;

    public SavedTemplateViewHolder(View itemView){
        super(itemView);
        mTemplateNameView = (TextView) itemView.findViewById(R.id.templateName);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mDaysView = (TextView) itemView.findViewById(R.id.daysView);
        mDescriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        mTemplateHeader = (LinearLayout) itemView.findViewById(R.id.savedTemplateHeader);

        mTemplateHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                selectedTemplateFrag.templateName = mTemplateNameView.getText().toString();

                fragmentTransaction.replace(R.id.mainFragHolder, selectedTemplateFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public void setActivity(FragmentActivity fragmentActivity){
        mActivity = fragmentActivity;
    }


    public void setTemplateNameView(String templateName){
        mTemplateNameView.setText(templateName);
    }

    public void setTimeStampView(String timeStamp){
        DateTime dateTimeOriginal = DateTime.parse(timeStamp);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");
        mTimeStampView.setText(formattedLocalDate);
    }

    public void setDaysView(String days){
        String daysFormatted = "";
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
