package com.liftdom.user_profile.your_profile;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivitySingleton;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

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
    @BindView(R.id.profileInfo) ImageView infoButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_info, container, false);

        ButterKnife.bind(this, view);

        mAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mAuth.getCurrentUser();

        SharedPreferences sharedPref = getActivity().getSharedPreferences("prefs", Activity.MODE_PRIVATE);

        userName.setText(sharedPref.getString("userName", "loading..."));

        DatabaseReference profileRef = mRootRef.child("user").child(uid);

        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                if(MainActivitySingleton.getInstance().isImperial){
                    bodyWeight.setText(userModelClass.getPounds());
                }else{
                    bodyWeight.setText(userModelClass.getKgs());
                }
                currentLevel.setText(userModelClass.getRepLevel());
                currentFocus.setText(userModelClass.getCurrentFocus());
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
