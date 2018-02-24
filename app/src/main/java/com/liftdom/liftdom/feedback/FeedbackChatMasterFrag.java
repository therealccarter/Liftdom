package com.liftdom.liftdom.feedback;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatGroupModelClass, ChatGroupViewHolder>
                (ChatGroupModelClass.class, R.layout.chat_group_list_item, ChatGroupViewHolder.class, mChatGroupReference) {
            @Override
            protected void populateViewHolder(ChatGroupViewHolder viewHolder,
                                              ChatGroupModelClass model, int position) {
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
                        viewHolder.setChatName(chatNameUsers);
                    }else{
                        for(Map.Entry<String, String> entry : model.getMemberMap().entrySet()){
                            if(!entry.getKey().equals(uid)){
                                chatNameUsers = entry.getValue();
                            }
                        }
                        viewHolder.setChatName(chatNameUsers);
                    }
                }else{
                    viewHolder.setChatName(model.getChatName());
                }
                viewHolder.setFromFeedbackMaster(true);
                viewHolder.setPreview(model.getPreviewString());
                viewHolder.setActiveDay(model.getActiveDate());
                viewHolder.setChatId(model.getChatId());
                viewHolder.setActivity(getActivity());
                viewHolder.setMemberMap(model.getMemberMap());
                viewHolder.setRefKey(model.getRefKey());
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }
}
