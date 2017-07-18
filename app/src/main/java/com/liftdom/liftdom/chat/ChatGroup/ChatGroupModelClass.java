package com.liftdom.liftdom.chat.ChatGroup;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/3/2017.
 */

public class ChatGroupModelClass {

    private String mChatName;
    private String mPreviewString;
    private String mActiveDate;
    private String mChatId;
    private List<String> mMemberList;
    private HashMap<String, String> mMemberMap;
    private String mChatGroupId;

    public ChatGroupModelClass(){
        // necessary for Firebase
    }

    public ChatGroupModelClass(String chatName, String previewString, HashMap<String, String> memberMap, String
            chatId){
        mChatName = chatName;
        mPreviewString = previewString;
        mMemberMap = memberMap;
        mChatId = chatId;

    }

    public HashMap<String, String> getMemberMap() {
        return mMemberMap;
    }

    public void setMemberMap(HashMap<String, String> mMemberMap) {
        this.mMemberMap = mMemberMap;
    }

    public String getChatGroupId() {
        return mChatGroupId;
    }

    public void setChatGroupId(String mChatGroupId) {
        this.mChatGroupId = mChatGroupId;
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
