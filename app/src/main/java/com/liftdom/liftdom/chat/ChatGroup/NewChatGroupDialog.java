package com.liftdom.liftdom.chat.ChatGroup;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.TemporaryUserChatListItemFrag;
import com.liftdom.liftdom.utils.FollowingModelClass;
import com.liftdom.liftdom.utils.UserNameIdModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewChatGroupDialog extends AppCompatActivity {

    ArrayList<String> userList = new ArrayList<>();
    private FirebaseRecyclerAdapter mFirebaseAdapter;
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private DatabaseReference mFollowerRef = FirebaseDatabase.getInstance().getReference().child("following").child
            (uid).child("followingMap");

    @BindView(R.id.chatNameCheckBox) CheckBox chatNameCheckBox;
    @BindView(R.id.chatNameTextView) TextView chatNameTextView;
    @BindView(R.id.chatNameEditText) EditText chatNameEditText;
    //@BindView(R.id.recycler_view) RecyclerView mRecyclerView;
    @BindView(R.id.confirmButton) ImageButton confirmButton;
    @BindView(R.id.cancelButton) ImageButton cancelButton;
    @BindView(R.id.userListLL) LinearLayout userListLL;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.followToChatView) TextView followToChatView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_chat_group_dialog);

        ButterKnife.bind(this);

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").child
                (uid);
        followingRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    FollowingModelClass followingModelClass = dataSnapshot.getValue(FollowingModelClass.class);
                    HashMap<String, String> followingMap = followingModelClass.getFollowingMap();
                    loadingView.setVisibility(View.GONE);
                    for(Map.Entry<String, String> mapEntry : followingMap.entrySet()){
                        TemporaryUserChatListItemFrag userChatListItemFrag = new TemporaryUserChatListItemFrag();
                        userChatListItemFrag.userId = mapEntry.getKey();
                        userChatListItemFrag.userName = mapEntry.getValue();
                        userChatListItemFrag.newChatGroupDialog = NewChatGroupDialog.this;
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.add(R.id.userListLL, userChatListItemFrag);
                        fragmentTransaction.commit();
                    }
                }else{
                    loadingView.setVisibility(View.GONE);
                    followToChatView.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        chatNameCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    chatNameTextView.setTextColor(Color.parseColor("#000000"));
                    chatNameEditText.setEnabled(true);
                }else{
                    chatNameTextView.setTextColor(Color.parseColor("#cccccc"));
                    chatNameEditText.setEnabled(false);
                }
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("userList", userList);
                if(chatNameCheckBox.isChecked()){
                    if(!chatNameEditText.getText().toString().equals("")){
                        intent.putExtra("chatName", chatNameEditText.getText().toString());
                    }
                }
                setResult(1, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //setUpFirebaseAdapter(this);
    }

    //private void setUpFirebaseAdapter(final NewChatGroupDialog activity){
    //    mFirebaseAdapter = new FirebaseRecyclerAdapter<UserNameIdModelClass, NewChatViewHolder>
    //            (UserNameIdModelClass.class, R.layout.user_checkbox_list_item, NewChatViewHolder.class,
    // mFollowerRef) {
    //        @Override
    //        protected void populateViewHolder(NewChatViewHolder viewHolder, UserNameIdModelClass model, int
    // position) {
    //            if(model.getUserId().equals(uid)){
    //                viewHolder.setUserName(model.getUserName());
    //                viewHolder.setUserId(model.getUserId());
    //                viewHolder.setActivity(activity);
    //                viewHolder.nukeViews();
    //            }else{
    //                viewHolder.setUserName(model.getUserName());
    //                viewHolder.setUserId(model.getUserId());
    //                viewHolder.setActivity(activity);
    //            }
    //        }
    //    };
//
    //    mRecyclerView.setHasFixedSize(false);
    //    mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    //    mRecyclerView.setAdapter(mFirebaseAdapter);
    //}

    public void addToUserList(String user){
        userList.add(user);
    }

    public void removeUser(String user){
        if(userList.contains(user)){
            int index = userList.indexOf(user);
            userList.remove(index);
        }
    }
}
