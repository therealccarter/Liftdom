package com.liftdom.user_profile.your_profile;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFrag extends Fragment {


    public ProfileHeaderFrag() {
        // Required empty public constructor
    }


    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.currentLevel) TextView currentLevel;
    @BindView(R.id.bodyWeight) TextView bodyWeight;
    @BindView(R.id.currentFocus) TextView currentFocus;
    @BindView(R.id.profileInfo) TextView infoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        userName.setText(mFirebaseUser.getDisplayName());

        DatabaseReference profileRef = mRootRef.child("users").child(uid);

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    String key = dataSnapshot1.getKey();
                    if(key.equals("bodyweight")){
                        String value = dataSnapshot1.getValue(String.class);
                        bodyWeight.setText(value);
                    }else if(key.equals("currentFocus")){
                        String value = dataSnapshot1.getValue(String.class);
                        currentFocus.setText(value);
                    }else if(key.equals("level")){
                        String value = dataSnapshot1.getValue(String.class);
                        currentLevel.setText(value);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        infoButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ProfileInfoActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

}
