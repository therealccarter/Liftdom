package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileOptionsTabFrag extends Fragment {


    public ProfileOptionsTabFrag() {
        // Required empty public constructor
    }

    public String uid;
    public String xUid;
    public boolean isOtherUser;

    @BindView(R.id.followersCountTextView) TextView followerCountView;
    @BindView(R.id.followingCountTextView) TextView followingCountView;
    @BindView(R.id.messageLinearLayout) LinearLayout messageLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_options_tab, container, false);

        ButterKnife.bind(this, view);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if(!isOtherUser){
            // is you
            xUid = uid;
        }else{
            // uncomment this to get message view up
            //messageLayout.setVisibility(View.VISIBLE);
        }

        //TODO: view followers and following in separate fragment. really need to get that back arrow going..

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        DatabaseReference followerRef = FirebaseDatabase.getInstance().getReference().child("followers").
                child(xUid).child("followerMap");
        followerRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int size = (int) dataSnapshot.getChildrenCount();
                    followerCountView.setText(String.valueOf(size));
                }else{
                    followerCountView.setText("0");
                }


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        DatabaseReference followingRef = FirebaseDatabase.getInstance().getReference().child("following").
                child(xUid).child("followingMap");
        followingRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    int size = (int) dataSnapshot.getChildrenCount();
                    followingCountView.setText(String.valueOf(size));
                }else{
                    followingCountView.setText("0");
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

}