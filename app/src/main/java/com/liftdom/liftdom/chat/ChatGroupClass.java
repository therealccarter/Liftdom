package com.liftdom.liftdom.chat;

import java.util.List;

/**
 * Created by Brodin on 5/3/2017.
 */

public class ChatGroupClass {

    private String mChatName;
    private String mPreviewString;
    private String mActiveDate;
    private String mChatId;
    private List<String> mMemberList;

    public ChatGroupClass(){
        // necessary for Firebase
    }

    public ChatGroupClass(String chatName, String previewString, List<String> memberList, String chatId){
        mChatName = chatName;
        mPreviewString = previewString;
        mMemberList = memberList;
        mChatId = chatId;
    }

    public String getChatName(){
        return mChatName;
    }

    public void setChatName(String chatName){
        mChatName = chatName;
    }

    public String getPreviewString(){
        return mPreviewString;
    }

    public void setPreviewString(String previewString){
        mPreviewString = previewString;
    }

    public String getActiveDate(){
        return mActiveDate;
    }

    public void setActiveDate(String activeDate){
        mActiveDate = activeDate;
    }

    public String getChatId(){
        return mChatId;
    }

    public void setChatId(String chatId){
        mChatId = chatId;
    }

    public List<String> getMemberList(){
        return mMemberList;
    }

    public void setMemberList(List<String> memberList){
        mMemberList = memberList;
    }

}
