package com.liftdom.liftdom.chat.ChatGroup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatSpecific.ChatSpecificFrag;

/**
 * Created by Brodin on 5/3/2017.
 */

public class ChatGroupViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    View mView;
    Context mContext;
    private final TextView mChatNameView;
    private final TextView mPreviewView;
    private final TextView mActiveDayView;
    private String mChatId;
    FragmentActivity mActivity;

    public ChatGroupViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mChatNameView = (TextView) itemView.findViewById(R.id.chatNameView);
        mPreviewView = (TextView) itemView.findViewById(R.id.previewTextView);
        mActiveDayView = (TextView) itemView.findViewById(R.id.activeDayView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChatSpecificFrag chatSpecificFrag = new ChatSpecificFrag();
        Bundle chatIdBundle = new Bundle();
        chatIdBundle.putString("chatId", mChatId);
        chatSpecificFrag.setArguments(chatIdBundle);

        fragmentTransaction.replace(R.id.mainFragHolder, chatSpecificFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
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

    public void setChatId(String chatId){
        mChatId = chatId;
    }

    public void setActivity(FragmentActivity activity){
        mActivity = activity;
    }

}
