package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.MasterListTemplateClass;

import java.util.ArrayList;

import static com.google.android.gms.internal.zzs.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyTemplatesFrag extends Fragment {

    // declare_auth
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;


    public MyTemplatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_templates, container, false);

        //TODO: Retrieve user's list of templates

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // We need to get the name of each template in a user's collection

        DatabaseReference mDatabase  = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        DatabaseReference mTemplateRef = mDatabase.child("users").child(uid).child("templates");

        mTemplateRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot templateSnapshot : dataSnapshot.getChildren()){

                           MasterListTemplateClass templateClass = templateSnapshot.getValue(MasterListTemplateClass.class);

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });
    }

}
