package com.liftdom.template_housing.public_programs;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_housing.SelectedTemplateFrag;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Created by Brodin on 6/15/2017.
 */

public class PublicTemplateViewHolder extends RecyclerView.ViewHolder{

    private final TextView mTemplateNameView;
    private final TextView mTimeStampView;
    private final TextView mDaysView;
    private final TextView mDescriptionView;
    private final TextView mCreatedByView;
    private final TextView mEditedByView;
    private final LinearLayout mTemplateHeader;
    private final LinearLayout mEditedByLinearLayout;
    private String mKey;
    private FragmentActivity mActivity;
    private boolean isMyPublicTemplate;

    public PublicTemplateViewHolder(View itemView){
        super(itemView);
        mTemplateNameView = (TextView) itemView.findViewById(R.id.templateName);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mDaysView = (TextView) itemView.findViewById(R.id.daysView);
        mDescriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        mTemplateHeader = (LinearLayout) itemView.findViewById(R.id.savedTemplateHeader);
        mCreatedByView = (TextView) itemView.findViewById(R.id.createdByView);
        mEditedByView = (TextView) itemView.findViewById(R.id.editedByView);
        mEditedByLinearLayout = (LinearLayout) itemView.findViewById(R.id.editedByLinearLayout);

        mTemplateHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                selectedTemplateFrag.templateName = mTemplateNameView.getText().toString();
                if(isMyPublicTemplate){
                    selectedTemplateFrag.isFromMyPublicList = true;
                }else{
                    selectedTemplateFrag.isFromPublicList = true;
                }
                selectedTemplateFrag.firebaseKey = mKey;

                fragmentTransaction.replace(R.id.mainFragHolder, selectedTemplateFrag);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    public boolean getIsMyPublicTemplate() {
        return isMyPublicTemplate;
    }

    public void setIsMyPublicTemplate(boolean myPublicTemplate) {
        isMyPublicTemplate = myPublicTemplate;
    }

    public void setEditedBy(String editedBy){
        if(editedBy != null){
            mEditedByLinearLayout.setVisibility(View.VISIBLE);
            mEditedByView.setText(editedBy);
        }

    }

    public void setKey(String key){
        mKey = key;
    }

    public String getKey(){
        return mKey;
    }

    public void setCreatedBy(String createdBy){
        mCreatedByView.setText(createdBy);
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
