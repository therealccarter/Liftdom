package com.liftdom.liftdom.chat.ChatGroup;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 5/5/2017.
 */

public class NewChatViewHolder extends RecyclerView.ViewHolder {

    private final TextView mUserName;
    private final CheckBox mCheckBox;
    private String mUserId;
    private NewChatGroupDialog newChatGroupDialog;

    public NewChatViewHolder(View itemView){
        super(itemView);
        mUserName = (TextView) itemView.findViewById(R.id.userNameView);
        mCheckBox = (CheckBox) itemView.findViewById(R.id.userNameCheckBox);

        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    newChatGroupDialog.addToUserList(mUserId);
                }else{
                    newChatGroupDialog.removeUser(mUserId);
                }
            }
        });
    }

    public void setUserName(String userName){
        mUserName.setText(userName);
    }

    public void setActivity(NewChatGroupDialog activity){
        newChatGroupDialog = activity;
    }

    public void setUserId(String userId){
        mUserId = userId;
    }

    public void nukeViews(){
        mUserName.setVisibility(View.GONE);
        mCheckBox.setVisibility(View.GONE);
    }

}
