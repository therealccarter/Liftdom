package com.liftdom.liftdom.main_social_feed.user_search;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.single_user_profile.UserProfileFullActivity;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserSearchResultFrag extends Fragment {


    public UserSearchResultFrag() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public String userName;
    public String xUid;

    @BindView(R.id.userName) TextView userNameTextView;
    @BindView(R.id.userLevel) TextView userLevelTextView;
    @BindView(R.id.profilePic) ImageView profilePicImageView;
    @BindView(R.id.userInfoHolder) LinearLayout userInfoHolder;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_search_result, container, false);

        ButterKnife.bind(this, view);

        userNameTextView.setText(userName);

        StorageReference profilePicRef = FirebaseStorage.getInstance().getReference().child("images/user/" +
                xUid + "/profilePic.png");

        profilePicRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.i("glide", "success");
                Glide.with(getContext()).load(uri).crossFade().into(profilePicImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.i("glide", "failure");
                profilePicImageView.setImageResource(R.drawable.usertest);
            }
        });

        DatabaseReference powerLevelRef = FirebaseDatabase.getInstance().getReference().child("user").child(xUid)
                .child("powerLevel");
        powerLevelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String powerLevel = dataSnapshot.getValue(String.class);
                if(powerLevel != null){
                    userLevelTextView.setText(powerLevel);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), UserProfileFullActivity.class);
                if(uid.equals(xUid)){
                    startActivity(intent);
                } else {
                    intent.putExtra("xUid", xUid);
                    startActivity(intent);
                    //FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
                    //OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    //otherUserProfileFrag.userName = userName;
                    //otherUserProfileFrag.xUid = xUid;
//
                    //fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    //fragmentTransaction.addToBackStack(null);
                    //fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

}
