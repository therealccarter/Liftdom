package com.liftdom.liftdom;

import com.google.firebase.auth.FirebaseAuth;
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


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String username = "error";

    public void setUsername(){
        DatabaseReference usernameRef = mRootRef.child("users").child(uid);
        usernameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    if(dataSnapshot1.getKey().equals("username")){
                        username = dataSnapshot1.getValue(String.class);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String getUserName(){

        return username;
    }
}
