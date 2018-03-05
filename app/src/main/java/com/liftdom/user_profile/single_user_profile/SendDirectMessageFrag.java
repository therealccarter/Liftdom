package com.liftdom.user_profile.single_user_profile;


import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class SendDirectMessageFrag extends Fragment {


    public SendDirectMessageFrag() {
        // Required empty public constructor
    }

    String uidFromOutside;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    boolean hasOneOnOneChatAlready = false;
    String chatId;

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
                String text = "Send message to " + userModelClass.getUserName();
                userNameView.setText(text);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference chatGroupRef = FirebaseDatabase.getInstance().getReference().child("chatGroups").child(uid);
        chatGroupRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                int i = 0;
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    i++;
                    ChatGroupModelClass chatGroupModelClass = dataSnapshot1.getValue(ChatGroupModelClass.class);
                    if(chatGroupModelClass.getMemberMap().size() == 2){
                        for(Map.Entry<String, String> entry : chatGroupModelClass.getMemberMap().entrySet()){
                            if(entry.getKey().equals(uidFromOutside)){
                                hasOneOnOneChatAlready = true;
                                chatId = chatGroupModelClass.getChatId();
                            }
                        }
                    }
                    if(i == dataSnapshot.getChildrenCount()){
                        loadingView.setVisibility(View.GONE);
                        messageHolder.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendMessageToExistingChat(String message){
        // logic here should be the same as in chat specific
    }

    private void createNewChatGroupAndSendMessage(String message){

    }

}
