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
        DatabaseReference usernameRef = mRootRef.child("users").child(uid).child("username");

        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot usernameSnap : dataSnapshot.getChildren()){
                    String key = usernameSnap.getKey();
                    if(key.equals("username")) {
                        username = usernameSnap.getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return username;
    }

    public String getEmail(){
        return email;
    }
}
