package com.liftdom.liftdom.chat;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Brodin on 5/3/2017.
 */

public class ChatGroupViewHolder extends RecyclerView.ViewHolder {

    View mView;
    Context mContext;

    public ChatGroupViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        //itemView.setOnClickListener(this);
    }

    public void bindChatGroup(ChatGroupClass chatGroupClass){
        // bind views

    }


}
