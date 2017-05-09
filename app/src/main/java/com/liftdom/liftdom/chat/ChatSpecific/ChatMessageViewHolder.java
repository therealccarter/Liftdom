package com.liftdom.liftdom.chat.ChatSpecific;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 5/4/2017.
 */

public class ChatMessageViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context mContext;
    private final TextView mTextMessageView;
    private final TextView mUserNameView;
    private final TextView mTimeStampView;
    private final Button mRepCountView;
    private final LinearLayout mMessageLayout;

    public ChatMessageViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mTextMessageView = (TextView) itemView.findViewById(R.id.textMessageView);
        mUserNameView = (TextView) itemView.findViewById(R.id.userNameView);
        mTimeStampView = (TextView) itemView.findViewById(R.id.timeStampView);
        mRepCountView = (Button) itemView.findViewById(R.id.repCountView);
        mMessageLayout = (LinearLayout) itemView.findViewById(R.id.chatMessageLinearLayout);
    }

    public void setMessage(String message){
        mTextMessageView.setText(message);
    }

    public void setUserName(String userName){
        mUserNameView.setText(userName);
    }

    public void setRepCount(int repCount){
        mRepCountView.setText(String.valueOf(repCount));
    }

    public void setTimeStamp(String timeStamp){
        mTimeStampView.setText(timeStamp);
    }

    public void setBackground(){
        mMessageLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        mUserNameView.setTextColor(Color.parseColor("#000000"));

    }
}
