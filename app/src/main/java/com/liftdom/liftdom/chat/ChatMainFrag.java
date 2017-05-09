package com.liftdom.liftdom.chat;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupViewHolder;
import com.liftdom.liftdom.chat.ChatGroup.NewChatGroupDialog;
import com.liftdom.template_housing.TemplateMenuFrag;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chat_main, container, false);

        ButterKnife.bind(this, view);

        headerChanger("Chat");

        newChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), NewChatGroupDialog.class);
                startActivityForResult(intent, 1);

            }
        });

        setUpFirebaseAdapter();

        return view;
    }

    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatGroupModelClass, ChatGroupViewHolder>
                (ChatGroupModelClass.class, R.layout.chat_group_list_item, ChatGroupViewHolder.class, mChatGroupReference) {
            @Override
            protected void populateViewHolder(ChatGroupViewHolder viewHolder,
                                              ChatGroupModelClass model, int position) {

                viewHolder.setChatName(model.getChatName());
                viewHolder.setPreview(model.getPreviewString());
                viewHolder.setActiveDay(model.getActiveDate());
                viewHolder.setChatId(model.getChatId());
                viewHolder.setActivity(getActivity());
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    private int inc = 0;

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null){
            if (requestCode == 1) {
                if (data.getStringArrayListExtra("userList") != null) {

                    final List<String> memberList = data.getStringArrayListExtra("userList");
                    memberList.add(uid);

                    final String uniqueID = UUID.randomUUID().toString();

                    if(data.getStringExtra("chatName") != null){
                        String chatName = data.getStringExtra("chatName");
                        ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(chatName, "preview text", memberList, uniqueID);
                        LocalDate localDate = LocalDate.now();
                        chatGroupModelClass.setActiveDate(localDate.toString());

                        for(String userId : memberList){
                            DatabaseReference nChatGroupReference = FirebaseDatabase.getInstance().getReference()
                                    .child("chatGroups").child(userId);
                            nChatGroupReference.push().setValue(chatGroupModelClass);
                        }
                    }else{

                        inc = 0;

                        final List<String> newList = new ArrayList<>();

                        for(String user : memberList){

                            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child
                                    ("userList").child(user);
                            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    String value = dataSnapshot.getValue(String.class);

                                    newList.add(value);
                                    inc++;
                                    if(inc == memberList.size()){

                                        String cat = "";
                                        for(String user : newList){
                                            cat = cat + user + ", ";
                                        }

                                        ChatGroupModelClass chatGroupModelClass = new ChatGroupModelClass(cat, "preview " +
                                                "text",
                                                memberList, uniqueID);
                                        LocalDate localDate = LocalDate.now();
                                        chatGroupModelClass.setActiveDate(localDate.toString());

                                        for(String userId : memberList){
                                            DatabaseReference nChatGroupReference = FirebaseDatabase.getInstance().getReference()
                                                    .child("chatGroups").child(userId);
                                            nChatGroupReference.push().setValue(chatGroupModelClass);
                                        }
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }


                    }

                    // For each person in the member list, push them a new chatGroupClass with a mChatId. That
                    //  mChatId will be what allows them in.
                }
            }
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
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
