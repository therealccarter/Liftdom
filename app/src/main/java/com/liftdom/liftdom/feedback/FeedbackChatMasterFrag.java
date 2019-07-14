package com.liftdom.liftdom.feedback;


import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupModelClass;
import com.liftdom.liftdom.chat.ChatGroup.ChatGroupViewHolder;

import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class FeedbackChatMasterFrag extends Fragment {


    public FeedbackChatMasterFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mChatGroupReference = FirebaseDatabase.getInstance().getReference().child
            ("feedbackChatMaster");
    private FirebaseRecyclerAdapter mFirebaseAdapter;

    @BindView(R.id.recycler_view) RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_feedback_chat_master, container, false);

        ButterKnife.bind(this, view);

        setUpFirebaseAdapter();

        return view;
    }

    private void setUpFirebaseAdapter(){

        FirebaseRecyclerOptions<ChatGroupModelClass> options = new FirebaseRecyclerOptions
                .Builder<ChatGroupModelClass>()
                .setQuery(mChatGroupReference, ChatGroupModelClass.class)
                .build();

        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatGroupModelClass, ChatGroupViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ChatGroupViewHolder holder, int position, @NonNull ChatGroupModelClass model) {
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
                holder.setFromFeedbackMaster(true);
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

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mFirebaseAdapter.startListening();
        mRecyclerView.setAdapter(mFirebaseAdapter);
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
