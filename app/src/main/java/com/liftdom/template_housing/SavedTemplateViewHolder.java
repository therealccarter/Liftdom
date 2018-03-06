package com.liftdom.template_housing;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.single_user_profile.SendDirectProgramFrag;
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
    private final LinearLayout parentLL;
    private FragmentActivity mActivity;
    private boolean isFromSendProgram;
    private String uidFromOutside;

    public SavedTemplateViewHolder(View itemView){
        super(itemView);
        mTemplateNameView = (TextView) itemView.findViewById(R.id.templateName);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mDaysView = (TextView) itemView.findViewById(R.id.daysView);
        mDescriptionView = (TextView) itemView.findViewById(R.id.descriptionView);
        mTemplateHeader = (LinearLayout) itemView.findViewById(R.id.savedTemplateHeader);
        parentLL = (LinearLayout) itemView.findViewById(R.id.parentLL);

        parentLL.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if(isFromSendProgram){
                    SendDirectProgramFrag sendDirectProgramFrag = new SendDirectProgramFrag();
                    sendDirectProgramFrag.uidFromOutside = uidFromOutside;
                    sendDirectProgramFrag.templateName = mTemplateNameView.getText().toString();

                    fragmentTransaction.replace(R.id.profileActionsHolder, sendDirectProgramFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                    selectedTemplateFrag.templateName = mTemplateNameView.getText().toString();

                    fragmentTransaction.replace(R.id.mainFragHolder, selectedTemplateFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }

            }
        });

        mTemplateHeader.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                if(isFromSendProgram){
                    SendDirectProgramFrag sendDirectProgramFrag = new SendDirectProgramFrag();
                    sendDirectProgramFrag.uidFromOutside = uidFromOutside;
                    sendDirectProgramFrag.templateName = mTemplateNameView.getText().toString();

                    fragmentTransaction.replace(R.id.profileActionsHolder, sendDirectProgramFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }else{
                    SelectedTemplateFrag selectedTemplateFrag = new SelectedTemplateFrag();
                    selectedTemplateFrag.templateName = mTemplateNameView.getText().toString();

                    fragmentTransaction.replace(R.id.mainFragHolder, selectedTemplateFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        DatabaseReference activeTemplateRef = FirebaseDatabase.getInstance().getReference().child("user").child
                (FirebaseAuth.getInstance().getCurrentUser().getUid());
        activeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                String activeTemplateString = userModelClass.getActiveTemplate();
                if(mTemplateNameView.getText().toString().equals(activeTemplateString)){
                    mTemplateNameView.setTextColor(Color.parseColor("#D1B91D"));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setActivity(FragmentActivity fragmentActivity){
        mActivity = fragmentActivity;
    }

    public boolean isFromSendProgram() {
        return isFromSendProgram;
    }

    public void setFromSendProgram(boolean fromSendProgram) {
        isFromSendProgram = fromSendProgram;
    }

    public String getUidFromOutside() {
        return uidFromOutside;
    }

    public void setUidFromOutside(String uidFromOutside) {
        this.uidFromOutside = uidFromOutside;
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
