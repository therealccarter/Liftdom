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

        addSlide(new IntroFrag2());

        addSlide(new IntroFrag3());

        addSlide(new IntroFrag4());

    }

    //@Override
    //public boolean dispatchTouchEvent(MotionEvent ev) {
    //    View view = getCurrentFocus();
    //    if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE)
    //            && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
    //        int scrcoords[] = new int[2];
    //        view.getLocationOnScreen(scrcoords);
    //        float x = ev.getRawX() + view.getLeft() - scrcoords[0];
    //        float y = ev.getRawY() + view.getTop() - scrcoords[1];
    //        if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
    //            ((InputMethodManager)this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
    //        (this.getWindow().getDecorView().getApplicationWindowToken()), 0);
    //    }
    //    return super.dispatchTouchEvent(ev);
    //}

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

        final UserModelClass userModelClass = new UserModelClass(userName, userId, email, age, isImperial,
                feetInchesHeight, cmHeight, pounds,
                kgs, maxList, sex, repLevel, powerLevel, currentStreak,
                currentFocus, activeTemplate);

        FirstTimeModelClass firstTimeModelClass = new FirstTimeModelClass(true, true, true, true, true, true);

        DatabaseReference userListRef = FirebaseDatabase.getInstance().getReference().child("userList").child
                (userId);
        userListRef.setValue(userName);
        DatabaseReference userNameRef = FirebaseDatabase.getInstance().getReference().child("userNames").child
                (userName);
        userNameRef.setValue("true");
        DatabaseReference firstTimeRef = FirebaseDatabase.getInstance().getReference().child("firstTime").child
                (userId);
        firstTimeRef.setValue(firstTimeModelClass);

        DatabaseReference firstTimeTemplateRef = FirebaseDatabase.getInstance().getReference().child
                ("defaultTemplates").child("FirstTimeProgram");

        firstTimeTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                TemplateModelClass firstTimeModel = dataSnapshot.getValue(TemplateModelClass.class);
                firstTimeModel.setUserId(userId);
                firstTimeModel.setUserName(userName);
                firstTimeModel.setDateCreated(new DateTime(DateTimeZone.UTC).toString());
                firstTimeModel.setDateUpdated(new DateTime(DateTimeZone.UTC).toString());
                DatabaseReference templateRef = FirebaseDatabase.getInstance().getReference().child
                        ("templates").child(userId).child(firstTimeModel.getTemplateName());
                templateRef.setValue(firstTimeModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
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
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        //DatabaseReference userNode = FirebaseDatabase.getInstance().getReference().child("user").child(userId);
//
        //userNode.setValue(userModelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
        //    @Override
        //    public void onComplete(@NonNull Task<Void> task) {
//
        //        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        //        SharedPreferences.Editor editor = sharedPref.edit();
        //        editor.putString("uid", userModelClass.getUserId());
        //        editor.putString("userName", userModelClass.getUserName());
        //        editor.commit();
//
        //        Intent intent = new Intent(FirstTimeSetupActivity.this, MainActivity.class);
        //        startActivity(intent);
        //    }
        //});
    }

}
