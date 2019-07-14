package com.liftdom.liftdom.intro;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.MessageButtonBehaviour;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.liftdom.liftdom.FirstTimeModelClass;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

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

        addSlide(new IntroFrag3());

        addSlide(new IntroFrag2());

        addSlide(new IntroFrag4());

    }


    @Override
    public void onFinish(){
        super.onFinish();
        final String userName = IntroSingleton.getInstance().displayName;
        final String userId = IntroSingleton.getInstance().userId;
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

        String currentStreak = "0";

        String activeTemplate = null;

        String email = IntroSingleton.getInstance().email;

        boolean isGDPR = IntroSingleton.getInstance().isGDPR;

        final UserModelClass userModelClass = new UserModelClass(userName, userId, email, age, isImperial,
                feetInchesHeight, cmHeight, pounds,
                kgs, maxList, sex, repLevel, powerLevel, currentStreak,
                currentFocus, activeTemplate, "0", isGDPR);

        FirstTimeModelClass firstTimeModelClass = new FirstTimeModelClass(true, true, true, true, true, true, true);

        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList").child
                (userId);
        userListRef.setValue(userName);
        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("userNames").child
                (userName);
        userNameRef.setValue("true");
        DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime").child
                (userId);
        //firstTimeRef.setValue(firstTimeModelClass);

        DatabaseReference firstTimeTemplateRef = FirebaseDatabase.getInstance().getReference().child
                ("defaultTemplates").child("FirstTimeProgram");

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
