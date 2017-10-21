package com.liftdom.liftdom;

import com.google.firebase.database.*;
import com.liftdom.user_profile.UserModelClass;

/**
 * Created by Brodin on 4/29/2017.
 */

public class MainActivitySingleton {
    // Singleton boilerplate
    private static MainActivitySingleton controller;

    public static MainActivitySingleton getInstance() {
        if (controller == null) {
            controller = new MainActivitySingleton();
        }
        return controller;
    }

    public boolean isWorkoutFinished = false;

    public boolean isReleaseCheck = false;

    public String currentActiveTemplate = "null";

    public UserModelClass userModelClass;

    public boolean isImperial;

    public boolean isFeedFirstTime;
    public boolean isTemplateMenuFirstTime;
    public boolean isAssistorFirstTime;

    public boolean isBannerViewInitialized;

    public void updateUserModelClass(){
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user");
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userModelClass = dataSnapshot.getValue(UserModelClass.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
