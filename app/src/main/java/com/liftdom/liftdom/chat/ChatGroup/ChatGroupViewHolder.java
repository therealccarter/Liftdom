package com.liftdom.liftdom.chat.ChatGroup;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatSpecific.ChatSpecificFrag;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormatter;

import java.util.HashMap;

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
    private HashMap<String, String> memberMap;
    private String mRefKey;
    FragmentActivity mActivity;

    public ChatGroupViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        mContext = itemView.getContext();
        mChatNameView = (TextView) itemView.findViewById(R.id.chatNameView);
        mPreviewView = (TextView) itemView.findViewById(R.id.previewTextView);
        mActiveDayView = (TextView) itemView.findViewById(R.id.activeDayView);
        itemView.setOnClickListener(this);
        //TODO: if user hasn't read message, set to black. Otherwise set to grey
    }

    @Override
    public void onClick(View view){
        FragmentManager fragmentManager = mActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        ChatSpecificFrag chatSpecificFrag = new ChatSpecificFrag();
        chatSpecificFrag.memberMap = getMemberMap();
        chatSpecificFrag.refKey = getRefKey();
        Bundle chatIdBundle = new Bundle();
        chatIdBundle.putString("chatId", mChatId);
        chatSpecificFrag.setArguments(chatIdBundle);

        fragmentTransaction.replace(R.id.mainFragHolder, chatSpecificFrag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public HashMap<String, String> getMemberMap(){
        return memberMap;
    }

    public void setMemberMap(HashMap<String, String> memberMap1){
        memberMap = memberMap1;
    }

    public void setChatName(String chatName){
        if(chatName.length() > 30){
            chatName = chatName.substring(0, Math.min(chatName.length(), 30));
            chatName = chatName + "...";
        }
        mChatNameView.setText(chatName);
    }

    public void setRefKey(String refKey){
        mRefKey = refKey;
    }

    public String getRefKey(){
        return mRefKey;
    }

    public void setPreview(String previewText){
        mPreviewView.setText(previewText);
        //DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(mChatId);
        //chatRef.addListenerForSingleValueEvent();
    }

    public void setActiveDay(String activeDay){
        //LocalDate localDate = new LocalDate(activeDay, DateTimeZone.UTC);
        DateTime dateTime = new DateTime(DateTime.parse(activeDay), DateTimeZone.getDefault());
        String localDateString = dateTime.toString("EEEE, MMM d");
        mActiveDayView.setText(localDateString);
    }

    private String getFormattedDate(DateTime dateTime){
        String formatted = "";



        return formatted;
    }

    public void setChatId(String chatId){
        mChatId = chatId;
    }

    public void setActivity(FragmentActivity activity){
        mActivity = activity;
    }

}
