package com.liftdom.liftdom.chat;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.AppCompatCheckBox;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.CompoundButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.chat.ChatGroup.NewChatGroupDialog;

/**
 * A simple {@link Fragment} subclass.
 */
public class TemporaryUserChatListItemFrag extends Fragment {


    public TemporaryUserChatListItemFrag() {
        // Required empty public constructor
    }

    //public String userName;
    public String userId;
    public NewChatGroupDialog newChatGroupDialog;

    @BindView(R.id.userNameView) TextView userNameView;
    @BindView(R.id.userNameCheckBox) AppCompatCheckBox checkBox;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_temporary_user_chat_list_item, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("userList").child(userId);
        userNameRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.getValue(String.class);
                userNameView.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    newChatGroupDialog.addToUserList(userId);
                }else{
                    newChatGroupDialog.removeUser(userId);
                }
            }
        });

        return view;
    }

}
