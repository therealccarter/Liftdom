package com.liftdom.liftdom.chat;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.appbar.AppBarLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupViewHolder;
import com.liftdom.liftdom.chat.ChatGroup.NewChatGroupDialog;
import com.liftdom.template_housing.TemplateMenuFrag;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.LocalDate;

import java.util.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMainFrag extends Fragment {


    public ChatMainFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mChatGroupReference = FirebaseDatabase.getInstance().getReference().child("chatGroups")
            .child(uid);
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.newChatButton) ImageButton newChatButton;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.noChatFoundView) TextView noChatsFoundView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_main, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Chat");

        AppBarLayout appBarLayout = getActivity().findViewById(R.id.appBar);
        appBarLayout.setExpanded(true, true);

        newChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewChatGroupDialog.class);
                startActivityForResult(intent, 1);

            }
        });

        setUpFirebaseAdapter();

        mChatGroupReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){

                }else{
                    loadingView.setVisibility(View.GONE);
                    noChatsFoundView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        return view;
    }

    private void setUpFirebaseAdapter(){

        Query query = mChatGroupReference.orderByChild("activeDate");

        FirebaseRecyclerOptions<ChatGroupModelClass> options = new FirebaseRecyclerOptions
                .Builder<ChatGroupModelClass>()
                .setQuery(query, ChatGroupModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatGroupModelClass, ChatGroupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatGroupViewHolder holder, int position, @NonNull ChatGroupModelClass model) {
                if(position == 0){
                    loadingView.setVisibility(View.GONE);
                    noChatsFoundView.setVisibility(View.GONE);
                }
                String chatNameUsers = "";
                if(model.getChatName() == null){
                    if(model.getMemberMap().size() > 2){
                        int inc = 0;
                        for(Map.Entry<String, String> entry : model.getMemberMap().entrySet()){
                            inc++;
                            if(!entry.getKey().equals(uid)){
                                if(inc == model.getMemberMap().size()){
                                    chatNameUsers = chatNameUsers + entry.getValue();
                                }else{
                                    chatNameUsers = chatNameUsers + entry.getValue() + ", ";
                                }
                            }
                        }
                        holder.setChatName(chatNameUsers);
                    }else{
                        for(Map.Entry<String, String> entry : model.getMemberMap().entrySet()){
                            if(!entry.getKey().equals(uid)){
                                chatNameUsers = entry.getValue();
                            }
                        }
                        holder.setChatName(chatNameUsers);
                    }
                }else{
                    holder.setChatName(model.getChatName());
                }
                holder.setPreview(model.getPreviewString());
                holder.setActiveDay(model.getActiveDate());
                holder.setChatId(model.getChatId());
                holder.setActivity(getActivity());
                holder.setMemberMap(model.getMemberMap());
                holder.setRefKey(model.getRefKey());
            }

            @Override
            public ChatGroupViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.chat_group_list_item,
                        parent, false);

                return new ChatGroupViewHolder(view);
            }
        };

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setSmoothScrollbarEnabled(true);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mFirebaseAdapter.startListening();
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private int inc = 0;
    private int nameInc = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == 1) {
                if (data.getStringArrayListExtra("userList") != null) {

                    final List<String> memberList = data.getStringArrayListExtra("userList");
                    memberList.add(uid);

                    final String uniqueID = UUID.randomUUID().toString();

                    final HashMap<String, String> userMap = new HashMap<>();

                    for(final String userId : memberList){
                        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child
                                (userId).child("userName");
                        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String userName = dataSnapshot.getValue(String.class);
                                userMap.put(userId, userName);
                                inc++;
                                if(inc == memberList.size()){
                                    if(data.getStringExtra("chatName") != null){
                                        DatabaseReference chatGroupReference = FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child("chatGroups").child(userId);

                                        Map fanoutObject = new HashMap<>();
                                        String refKey = chatGroupReference.push().getKey();

                                        String chatName = data.getStringExtra("chatName");
                                        ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(chatName, "preview text",
                                                userMap, uniqueID, refKey);
                                        String dateUTC = new DateTime(DateTimeZone.UTC).toString();
                                        chatGroupModelClass.setActiveDate(dateUTC);

                                        for(String userId : memberList){
                                            fanoutObject.put("/chatGroups/" + userId + "/" + refKey,
                                                    chatGroupModelClass);
                                        }

                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                        rootRef.updateChildren(fanoutObject);

                                        inc = 0;
                                    }else{

                                        DatabaseReference chatGroupReference = FirebaseDatabase.getInstance()
                                                .getReference()
                                                .child("chatGroups").child(userId);

                                        Map fanoutObject = new HashMap<>();
                                        String refKey = chatGroupReference.push().getKey();

                                        String chatName = null;

                                        if(memberList.size() == 1){
                                            if(memberList.get(0).equals(uid)){
                                                SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);
                                                chatName = sharedPref.getString("userName", "loading...");
                                            }
                                        }

                                        ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(chatName,
                                                "preview text",
                                                userMap, uniqueID, refKey);
                                        String dateUTC = new DateTime(DateTimeZone.UTC).toString();
                                        chatGroupModelClass.setActiveDate(dateUTC);

                                        for(String userId : memberList){
                                            fanoutObject.put("/chatGroups/" + userId + "/" + refKey,
                                                    chatGroupModelClass);
                                        }

                                        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
                                        rootRef.updateChildren(fanoutObject);

                                        inc = 0;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        }
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

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

}
