package com.liftdom.liftdom.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liftdom.liftdom.R;

/**
 * Created by Brodin on 5/3/2017.
 */

public class ChatGroupViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context mContext;
    private final TextView mChatNameView;
    private final TextView mPreviewView;
    private final TextView mActiveDayView;

    public ChatGroupViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mChatNameView = (TextView) itemView.findViewById(R.id.chatNameView);
        mPreviewView = (TextView) itemView.findViewById(R.id.previewTextView);
        mActiveDayView = (TextView) itemView.findViewById(R.id.activeDayView);
        //itemView.setOnClickListener(this);
    }

    public void setChatName(String chatName){
        mChatNameView.setText(chatName);
    }

    public void setPreview(String previewText){
        mPreviewView.setText(previewText);
    }

    public void setActiveDay(String activeDay){
        mActiveDayView.setText(activeDay);
    }

}
