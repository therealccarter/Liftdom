package com.liftdom.liftdom.chat;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatMainFrag extends Fragment {


    public ChatMainFrag() {
        // Required empty public constructor
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

        newChatButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                List<String> memberList = new ArrayList<String>();

                memberList.add("Chris Carter");
                memberList.add("David D");
                memberList.add("Chris Bourque");

                ChatGroupClass chatGroupClass = new ChatGroupClass("Real Ones", "get rekt fam", memberList, "1");
                LocalDate localDate = LocalDate.now();
                chatGroupClass.setActiveDate(localDate.toString());
                mChatGroupReference.push().setValue(chatGroupClass);
            }
        });

        setUpFirebaseAdapter();

        return view;
    }


    private void setUpFirebaseAdapter(){
        mFirebaseAdapter = new FirebaseRecyclerAdapter<ChatGroupClass, ChatGroupViewHolder>
                (ChatGroupClass.class, R.layout.chat_group_list_item, ChatGroupViewHolder.class, mChatGroupReference) {
            @Override
            protected void populateViewHolder(ChatGroupViewHolder viewHolder,
                                              ChatGroupClass model, int position) {
                    viewHolder.setChatName(model.getChatName());
                    viewHolder.setPreview(model.getPreviewString());
                    viewHolder.setActiveDay(model.getActiveDate());
            }
        };

        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setAdapter(mFirebaseAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        mFirebaseAdapter.cleanup();
    }

}
