package com.liftdom.liftdom.intro;


import agency.tango.materialintroscreen.SlideFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.LinearLayout;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class IntroFrag4 extends SlideFragment {


    public IntroFrag4() {
        // Required empty public constructor
    }

    private AVLoadingIndicatorView loadingView;
    private LinearLayout linearLayout;
    private Button finishSetupButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_intro_frag4, container, false);

        loadingView = (AVLoadingIndicatorView) view.findViewById(R.id.loadingView1);
        linearLayout = (LinearLayout) view.findViewById(R.id.linearLayout);
        finishSetupButton = (Button) view.findViewById(R.id.finishButton);

        finishSetupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadingView.setVisibility(View.VISIBLE);
                finishSetupButton.setVisibility(View.GONE);

                String userName = IntroSingleton.getInstance().displayName;
                String userId = IntroSingleton.getInstance().userId;
                String age = IntroSingleton.getInstance().age;
                boolean isImperial = IntroSingleton.getInstance().isImperial;

                String feetInchesHeight = IntroSingleton.getInstance().feet + "_" + IntroSingleton.getInstance().inches;
                String cmHeight = IntroSingleton.getInstance().cm;
                String pounds = IntroSingleton.getInstance().weightImperial;

                String kgs = IntroSingleton.getInstance().weightMetric;

                HashMap<String, String> maxList = new HashMap<>();

                String sex;
                if(IntroSingleton.getInstance().isMale){
                    sex = "male";
                }else{
                    sex = "female";
                }

                String repLevel = "0";
                String powerLevel = "0";
                String currentFocus = "General Fitness";

                String email = IntroSingleton.getInstance().email;

                final UserModelClass userModelClass = new UserModelClass(userName, userId, email, age, isImperial,
                        feetInchesHeight, cmHeight, pounds,
                        kgs, maxList, sex, repLevel, powerLevel,
                        currentFocus);

                DatabaseReference userNode = FirebaseDatabase.getInstance().getReference().child("user").child(userId);

                userNode.setValue(userModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("uid", userModelClass.getUserId());
                        editor.putString("userName", userModelClass.getUserName());
                        editor.commit();

                        Intent intent = new Intent(getContext(), MainActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

        return view;
    }

    @Override
    public int backgroundColor() {
        return R.color.black;
    }

    @Override
    public int buttonsColor() {
        return R.color.liftrGold1;
    }

}
