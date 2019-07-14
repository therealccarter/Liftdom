package com.liftdom.workout_programs.Smolov;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovInfoFrag extends Fragment {


    public SmolovInfoFrag() {
        // Required empty public constructor
    }

    public HashMap<String, String> infoMap = new HashMap<>();

    @BindView(R.id.exerciseNameView) TextView exNameView;
    @BindView(R.id.maxView) TextView maxWeightView;
    @BindView(R.id.beginDateView) TextView beginDateView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_info, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                String max;
                if(userModelClass.isIsImperial()){
                    max = infoMap.get("maxWeight") + " lbs";
                }else{
                    max = infoMap.get("maxWeight") + " kgs";
                }

                exNameView.setText(infoMap.get("exName"));
                maxWeightView.setText(max);
                beginDateView.setText(infoMap.get("beginDate"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

}
