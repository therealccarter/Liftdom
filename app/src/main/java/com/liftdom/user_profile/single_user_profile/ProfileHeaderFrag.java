package com.liftdom.user_profile.single_user_profile;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.liftdom.user_profile.your_profile.ProfileInfoActivity;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileHeaderFrag extends Fragment {


    public ProfileHeaderFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uidPov = FirebaseAuth.getInstance().getCurrentUser().getUid();
    public String uidFromOutside;

    @BindView(R.id.userName) TextView userName;
    @BindView(R.id.currentLevel) TextView currentLevel;
    @BindView(R.id.bodyWeight) TextView bodyWeight;
    @BindView(R.id.currentStreak) TextView currentStreak;
    @BindView(R.id.profileInfo) ImageView infoButton;
    @BindView(R.id.profilePicView) ImageView profilePicView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_header, container, false);

        ButterKnife.bind(this, view);

        if(uidPov.equals(uidFromOutside)){
            infoButton.setVisibility(View.VISIBLE);
        }else{
            infoButton.setVisibility(View.GONE);
        }

        DatabaseReference profileRef = mRootRef.child("user").child(uidFromOutside);
        profileRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                userName.setText(userModelClass.getUserName());
                currentLevel.setText(userModelClass.getPowerLevel());
                if(userModelClass.isIsImperial()){
                    String bw = userModelClass.getPounds() + " lbs";
                    bodyWeight.setText(bw);
                }else{
                    String bw = userModelClass.getKgs() + " kgs";
                    bodyWeight.setText(bw);
                }
                currentStreak.setText(userModelClass.getCurrentStreak());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                uidFromOutside + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(getActivity()).load(uri).into(profilePicView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                profilePicView.setImageResource(R.drawable.usertest);
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
