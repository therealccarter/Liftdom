package com.liftdom.liftdom.feedback;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.fragment.app.Fragment;
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
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageModelClass;
import com.liftdom.liftdom.chat.ChatSpecific.ChatMessageViewHolder;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackChatFrag extends Fragment {


    public FeedbackChatFrag() {
        // Required empty public constructor
    }

    String chatId;
    public String refKey;
    private List<String> userList;
    public boolean isFromMaster;

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String otherUid;
    public HashMap<String, String> memberMap;
    private DatabaseReference mChatGroupReference;
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;
    private LinearLayoutManager linearLayoutManager;
    private boolean isFirstTime = true;

    String userName;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.newMessageView) EditText newMessageView;
    @BindView(R.id.sendMessageView) ImageButton sendMessageButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback_chat, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        //userList = new ArrayList<>();
        //
        //for(Map.Entry<String, String> mapEntry : memberMap.entrySet()){
        //    userList.add(mapEntry.getKey());
        //}

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        //BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        //bottomNavigation.setVisibility(View.GONE);

        if(!isFromMaster){
            otherUid = uid;
        }

        mChatGroupReference = FirebaseDatabase.getInstance().getReference().child("feedbackChat").child(otherUid);

        mChatGroupReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    final String key = dataSnapshot1.getKey();
                    setUpFirebaseAdapter(mChatGroupReference.child(key));
                    chatId = key;

                    SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                    userName = sharedPref.getString("userName", "loading...");

                    sendMessageButton.setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            if(!newMessageView.getText().toString().equals("")){

                                String message = newMessageView.getText().toString();

                                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                                String dateTimeString = dateTime.toString();

                                ChatMessageModelClass chatMessageModelClass = new ChatMessageModelClass(message,
                                        uid, userName, dateTimeString, 0, "none");

                                mChatGroupReference.child(key).push().setValue(chatMessageModelClass);

                                updateChatGroups(message, dateTimeString);

                                newMessageView.setText("");
                                //linearLayoutManager.scrollToPosition(mFirebaseAdapter.getItemCount());
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }


    private void setUpFirebaseAdapter(DatabaseReference databaseReference){

        linearLayoutManager = new LinearLayoutManager(getActivity());
        //linearLayoutManager.setStackFromEnd(true);
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setItemViewCacheSize(15);

        FirebaseRecyclerOptions<ChatMessageModelClass> options = new FirebaseRecyclerOptions
                .Builder<ChatMessageModelClass>()
                .setQuery(databaseReference, ChatMessageModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatMessageModelClass, ChatMessageViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatMessageViewHolder holder, int position, @NonNull ChatMessageModelClass model) {
                holder.setMessage(model.getTextMessage());
                holder.setUserName(model.getUserName());
                holder.setTimeStamp(model.getTimeStamp());
                holder.setUserId(model.getUserId());
                if(model.getUserId().equals(uid)){
                    holder.setBackground();
                }
            }

            @Override
            public ChatMessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.chat_message_list_item,
                        parent, false);

                return new ChatMessageViewHolder(view);
            }
        };

        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                int messageCount = mFirebaseAdapter.getItemCount();
                int lastVisiblePosition = linearLayoutManager.findLastCompletelyVisibleItemPosition();

                if(lastVisiblePosition == -1 ||
                        (positionStart >= (messageCount - 1))){
                    mRecyclerView.scrollToPosition(positionStart);
                }
            }
        });

        mFirebaseAdapter.startListening();
        mRecyclerView.setAdapter(mFirebaseAdapter);


    }

    private void updateChatGroups(String message, String dateTime){
        // for each member of the current chat, update their chat group nodes
        Map fanoutObject = new HashMap<>();

        fanoutObject.put("/feedbackChatMaster/" + chatId + "/previewString", userName + ": "
                + getTruncatedString(message));
        fanoutObject.put("/feedbackChatMaster/" + chatId + "/activeDate", dateTime);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.updateChildren(fanoutObject);
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

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        //BottomNavigation bottomNavigation = (BottomNavigation) getActivity().findViewById(R.id.BottomNavigation);
        //bottomNavigation.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart(){
        super.onStart();
        if(mFirebaseAdapter != null && mFirebaseAdapter.getItemCount() == 0){
            mFirebaseAdapter.startListening();
        }
    }

    @Override
    public void onStop(){
        super.onStop();
        if(mFirebaseAdapter != null){
            mFirebaseAdapter.stopListening();
        }
    }

}
