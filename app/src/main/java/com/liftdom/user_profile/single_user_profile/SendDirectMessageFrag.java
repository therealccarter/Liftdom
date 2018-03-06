package com.liftdom.user_profile.single_user_profile;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDirectMessageFrag extends Fragment {


    public SendDirectMessageFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;
    String userNameFromOutside;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean hasOneOnOneChatAlready = false;
    String chatId;
    String chatGroupRefKey;

    @BindView(R.id.userNameView) TextView userNameView;
    @BindView(R.id.messageHolder) LinearLayout messageHolder;
    @BindView(R.id.messageEditText) EditText messageEditText;
    @BindView(R.id.sendMessageView) ImageButton sendMessageButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.messageSentView) TextView messageSentView;
    @BindView(R.id.messageFailedView) TextView messageFailedView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_send_direct_message, container, false);

        ButterKnife.bind(this, view);

        /**
         * So, first we need to check to see if there's already a chat with just you and the person you're messaging.
         * If there is, we will update that chat.
         * If not, we need to create a new chat group for both users and a new chat with the message typed here.
         * Got to make sure it's not empty.
         * We'll hide the edit text/initial views, show a loading view, and when the onComplete triggers,
         * we'll finish the dialog activity.
         */

        if(savedInstanceState == null){
            initialLogic();
        }

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                messageHolder.setVisibility(View.GONE);
                String message = messageEditText.getText().toString();
                if(!message.isEmpty()){
                    if(hasOneOnOneChatAlready){
                        // add new message to chatId
                        sendMessageToExistingChat(message);
                    }else{
                        // create new chat group and add the message to the subsequent chat.
                        createNewChatGroupAndSendMessage(message);
                    }
                }
            }
        });

        return view;
    }

    private void initialLogic(){
        DatabaseReference outsideUserNameRef = FirebaseDatabase.getInstance().getReference().child("user").child
                (uidFromOutside);
        outsideUserNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                String text = "To " + userModelClass.getUserName() + ":";
                userNameView.setText(text);
                userNameFromOutside = userModelClass.getUserName();

                DatabaseReference chatGroupRef = FirebaseDatabase.getInstance().getReference().child("chatGroups").child(uid);
                chatGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.exists()){
                            int i = 0;
                            for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                i++;
                                ChatGroupModelClass chatGroupModelClass = dataSnapshot1.getValue(ChatGroupModelClass.class);
                                if(chatGroupModelClass.getMemberMap().size() == 2){
                                    for(Map.Entry<String, String> entry : chatGroupModelClass.getMemberMap().entrySet()){
                                        if(entry.getKey().equals(uidFromOutside)){
                                            hasOneOnOneChatAlready = true;
                                            chatId = chatGroupModelClass.getChatId();
                                            chatGroupRefKey = chatGroupModelClass.getRefKey();
                                        }
                                    }
                                }
                                if(i == dataSnapshot.getChildrenCount()){
                                    loadingView.setVisibility(View.GONE);
                                    messageHolder.setVisibility(View.VISIBLE);
                                }
                            }
                        }else{
                            loadingView.setVisibility(View.GONE);
                            messageHolder.setVisibility(View.VISIBLE);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    private void sendMessageToExistingChat(String message){
        // logic here should be the same as in chat specific
        DatabaseReference chatRef = FirebaseDatabase.getInstance().getReference().child("chats").child(chatId);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName = sharedPref.getString("userName", "loading...");

        DateTime dateTime = new DateTime(DateTimeZone.UTC);
        String dateTimeString = dateTime.toString();

        ChatMessageModelClass chatMessageModelClass = new ChatMessageModelClass(message,
                uid, userName, dateTimeString, 0, "none");

        chatRef.push().setValue(chatMessageModelClass);

        updateChatGroups(message, dateTimeString, userName);
    }

    private void createNewChatGroupAndSendMessage(String message){
        DatabaseReference chatGroupReference = FirebaseDatabase.getInstance()
                .getReference()
                .child("chatGroups").child(uid);

        Map fanoutObject = new HashMap<>();
        String refKey = chatGroupReference.push().getKey();

        chatGroupRefKey = refKey;

        String chatName = null;

        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        String userName1 = sharedPref.getString("userName", "loading...");

        chatName = userName1 + ", " + userNameFromOutside;

        final String uniqueID = UUID.randomUUID().toString();
        chatId = uniqueID;

        final HashMap<String, String> userMap = new HashMap<>();

        userMap.put(uid, userName1);
        userMap.put(uidFromOutside, userNameFromOutside);

        ArrayList<String> userList = new ArrayList<>();
        userList.add(uid);
        userList.add(uidFromOutside);

        ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(chatName,
                "preview text", userMap, uniqueID, refKey);
        String dateUTC = new DateTime(DateTimeZone.UTC).toString();
        chatGroupModelClass.setActiveDate(dateUTC);

        for(String userId : userList){
            fanoutObject.put("/chatGroups/" + userId + "/" + refKey,
                    chatGroupModelClass);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.updateChildren(fanoutObject);

        sendMessageToExistingChat(message);
    }

    private void updateChatGroups(String message, String dateTime, String userName){
        // for each member of the current chat, update their chat group nodes
        Map fanoutObject = new HashMap<>();

        ArrayList<String> userList = new ArrayList<>();
        userList.add(uid);
        userList.add(uidFromOutside);

        for(String user : userList){
            fanoutObject.put("/chatGroups/" + user + "/" + chatGroupRefKey + "/previewString", userName + ": "
                    + getTruncatedString(message));
            fanoutObject.put("/chatGroups/" + user + "/" + chatGroupRefKey + "/activeDate", dateTime);
        }

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.updateChildren(fanoutObject).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                loadingView.setVisibility(View.GONE);
                messageHolder.setVisibility(View.GONE);
                messageSentView.setVisibility(View.VISIBLE);
                userNameView.setVisibility(View.GONE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                loadingView.setVisibility(View.GONE);
                messageHolder.setVisibility(View.GONE);
                messageFailedView.setVisibility(View.VISIBLE);
            }
        });
    }

    private String getTruncatedString(String unCut){
        String cut;
        if(unCut.length() > 15){
            cut = unCut.substring(0, Math.min(unCut.length(), 15)) + "...";
        }else{
            cut = unCut;
        }

        return cut;
    }

}
