package com.liftdom.liftdom;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;

/**
 * Created by Chris on 11/26/2016.
 */

public class KeyAccountValuesActivity {

    // Singleton boilerplate
    private static KeyAccountValuesActivity controller;
    public static KeyAccountValuesActivity getInstance() {
        if (controller == null) {
            controller = new KeyAccountValuesActivity();
        }
        return controller;
    }

    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;



    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();


    String username = "error";
    String email = "error";

    public void setUsername(String username){
        DatabaseReference usernameRef = mRootRef.child("users").child(uid).child("username");
        usernameRef.setValue(username);
    }

    public void setEmail(String emailPassedIn){
        email = emailPassedIn;
    }

    public String getUserName(){
        return username;
    }

    public String getEmail(){
        return email;
    }
}
