package com.liftdom.liftdom.intro;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

import java.util.HashMap;

public class FirstTimeSetupActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String userId = getIntent().getExtras().getString("uid");
        String email = getIntent().getExtras().getString("email");
        String defaultDisplayName = null;
        if(getIntent().getExtras().getString("defaultDisplayName") != null){
            defaultDisplayName = getIntent().getExtras().getString("defaultDisplayName");
        }

        IntroSingleton.getInstance().userId = userId;
        IntroSingleton.getInstance().email = email;
        IntroSingleton.getInstance().defaultDisplayName = defaultDisplayName;

        addSlide(new IntroFrag1());

        addSlide(new IntroFrag2());

        addSlide(new IntroFrag3());

        addSlide(new IntroFrag4());


    }

    @Override
    public void onFinish(){
        super.onFinish();
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

                SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString("uid", userModelClass.getUserId());
                editor.putString("userName", userModelClass.getUserName());
                editor.commit();

                Intent intent = new Intent(FirstTimeSetupActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

}
