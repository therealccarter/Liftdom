package com.liftdom.liftdom.main_social_feed;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.liftdom.user_profile.other_profile.OtherUserProfileFrag;
import com.liftdom.user_profile.your_profile.CurrentUserProfile;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class CompletedWorkoutPostFrag extends Fragment {


    public CompletedWorkoutPostFrag() {
        // Required empty public constructor
    }

    public String userId;
    public String userName;
    public String publicComment;
    public List workoutInfoList;
    public String dateAndTime;
    public HashMap<String, Boolean> repsMap;
    private String userLevel;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.userName) TextView userNameView;
    //@BindView(R.id.workoutContents) TextView workoutContentsView;
    @BindView(R.id.userLevel) TextView userLevelView;
    @BindView(R.id.publicComment) TextView publicCommentView;
    @BindView(R.id.timeStampView) TextView timeStampView;
    @BindView(R.id.postInfoHolder) LinearLayout postInfoHolder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_completed_workout_post, container, false);

        ButterKnife.bind(this, view);

        //String workoutInfoTestString = "";

        for(int i = 0; i < workoutInfoList.size(); i++){
            //workoutInfoTestString = workoutInfoTestString + workoutInfoList.get(i) + "\n";
            String infoString = (String) workoutInfoList.get(i);

            if(isExerciseName(infoString)){
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                PostExNameFrag exNameFrag = new PostExNameFrag();
                exNameFrag.exNameString = infoString;
                fragmentTransaction.add(R.id.exContentsHolder, exNameFrag);
                fragmentTransaction.commit();
            }else{
                FragmentManager fragmentManager = getChildFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager
                        .beginTransaction();
                PostSetSchemeFrag setSchemesFrag = new PostSetSchemeFrag();
                setSchemesFrag.setSchemeString = infoString;
                fragmentTransaction.add(R.id.exContentsHolder, setSchemesFrag);
                fragmentTransaction.commit();
            }
        }

        DatabaseReference userLevelRef = mRootRef.child("users").child(userId).child("userLevel");
        userLevelRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userLevel = dataSnapshot.getValue(String.class);
                String cat = "Level " + userLevel;
                userLevelView.setText(cat);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        userNameView.setText(userName);

        //workoutContentsView.setText(workoutInfoTestString);

        DateTime dateTimeOriginal = DateTime.parse(dateAndTime);
        DateTime localDate = dateTimeOriginal.withZone(DateTimeZone.getDefault());
        String formattedLocalDate = localDate.toString("MM/dd/yyyy");

        timeStampView.setText(formattedLocalDate);

        publicCommentView.setText(publicComment);

        postInfoHolder.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(uid.equals(userId)){
                    Intent intent = new Intent(getContext(), CurrentUserProfile.class);
                    startActivity(intent);
                } else {
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                    OtherUserProfileFrag otherUserProfileFrag = new OtherUserProfileFrag();
                    otherUserProfileFrag.userName = userName;
                    otherUserProfileFrag.xUid = userId;

                    fragmentTransaction.replace(R.id.mainFragHolder, otherUserProfileFrag);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        return view;
    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }

}