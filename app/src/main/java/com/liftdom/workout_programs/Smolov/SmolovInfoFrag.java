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
    public boolean isMaxImperial;


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
                //if(userModelClass.isIsImperial()){
                //    max = infoMap.get("maxWeight") + " lbs";
                //}else{
                //    max = infoMap.get("maxWeight") + " kgs";
                //}

                exNameView.setText(infoMap.get("exName"));
                //maxWeightView.setText(max);
                beginDateView.setText(infoMap.get("beginDate"));

                convertUnitsAndPost(userModelClass.isIsImperial());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return view;
    }

    private void convertUnitsAndPost(boolean currentPOV){
        /**
         * We need the units of the max, and the units we currently are.
         */

        String maxValue = infoMap.get("maxWeight");
        // maxImperial = what the max is
        // currentPOV = what the user currently is (this will be what the template is saved as)

        if(isMaxImperial == currentPOV){
            if(currentPOV){
                //imperial
                String val = maxValue + " lbs";
                maxWeightView.setText(val);
            }else{
                //kgs
                String val = maxValue + " kgs";
                maxWeightView.setText(val);
            }

        }else{
            if(isMaxImperial && !currentPOV){
                // convert from imperial to metric
                // max is imperial, POV is kg
                String val = imperialToMetric(maxValue) + " kgs";
                maxWeightView.setText(val);
            }else if(!isMaxImperial && currentPOV){
                // convert from metric to imperial
                // max is kg, POV is imperial
                String val = metricToImperial(maxValue) + " lbs";
                maxWeightView.setText(val);
            }
        }

    }

    private String metricToImperial(String input){

        double lbsDouble = Double.parseDouble(input) * 2.2046;
        int lbsInt = (int) Math.round(lbsDouble);
        String newString = String.valueOf(lbsInt);

        return newString;
    }

    private String imperialToMetric(String input){

        double kgDouble = Double.parseDouble(input) / 2.2046;
        int kgInt = (int) Math.round(kgDouble);
        String newString = String.valueOf(kgInt);

        return newString;
    }

}
