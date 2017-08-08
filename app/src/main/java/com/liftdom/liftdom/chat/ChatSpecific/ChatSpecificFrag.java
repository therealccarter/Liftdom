package com.liftdom.liftdom.chat.ChatSpecific;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageModelClass;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageViewHolder;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatSpecificFrag extends Fragment {


    public ChatSpecificFrag() {
        // Required empty public constructor
    }

    public String mChatId;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public HashMap<String, String> memberMap;
    private DatabaseReference mChatGroupReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private LinearLayoutManager layoutManager;
    private boolean isFirstTime = true;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.newMessageView) EditText newMessageView;
    @BindView(R.id.sendMessageView) ImageButton sendMessageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_specific, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        layoutManager = new LinearLayoutManager(getActivity());

        BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        bottomNavigation.setVisibility(View.GONE);

        String chatId = getArguments().getString("chatId");

        mChatGroupReference = FirebaseDatabase.getInstance().getReference().child("chats")
                .child(chatId);

        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
        final String userName = sharedPref.getString("userName", "loading...");

        sendMessageButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(!newMessageView.getText().toString().equals("")){

                    DateTime dateTime = new DateTime(DateTimeZone.UTC);
                    String dateTimeString = dateTime.toString();

                    ChatMessageModelClass chatMessageModelClass = new ChatMessageModelClass(newMessageView.getText().toString(),
                            uid, userName, dateTimeString, 0, "none");

                    mChatGroupReference.push().setValue(chatMessageModelClass);

                    newMessageView.setText("");
                    //layoutManager.scrollToPosition(mFirebaseAdapter.getItemCount());
                }
            }
        });

        setUpFirebaseAdapter(mChatGroupReference);

        return view;
    }

    private void setUpFirebaseAdapter(DatabaseReference databaseReference){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatMessageModelClass, ChatMessageViewHolder>
                (ChatMessageModelClass.class, R.layout.chat_message_list_item, ChatMessageViewHolder.class, databaseReference) {
            @Override
            protected void populateViewHolder(ChatMessageViewHolder viewHolder,
                                              ChatMessageModelClass model, int position) {
                viewHolder.setMessage(model.getTextMessage());
                viewHolder.setUserName(model.getUserName());
                viewHolder.setTimeStamp(model.getTimeStamp());
                if(model.getUserId().equals(uid)){
                    viewHolder.setBackground();
                }
                //if(position != 0){
                //    mRecyclerView.scrollToPosition(mFirebaseAdapter.getItemCount());
                //
                //}
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mFirebaseAdapter);


    }

    private void updateChatGroupPreview(String chatId){
        // for each member of the current chat, update their chat group nodes


    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

}
