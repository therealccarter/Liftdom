package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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

    ArrayList<String> templateNamesList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_templates, container, false);

        DatabaseReference mDatabase  = FirebaseDatabase.getInstance().getReference();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        final DatabaseReference mTemplateRef = mDatabase.child("users").child(uid).child("templates");

        //TODO loading symbol until it loads...

        mTemplateRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for(DataSnapshot templateSnapshot : dataSnapshot.getChildren()){

                            //MasterListTemplateClass templateClass = templateSnapshot.getValue(MasterListTemplateClass.class);

                            String templateClassName = templateSnapshot.getKey();

                            //templateNamesList.add(templateClassName);

                            TemplateListItem templateListItem = new TemplateListItem();

                            templateListItem.templateName = templateClassName;

                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                            fragmentTransaction.add(R.id.myTemplatesList, templateListItem);
                            fragmentTransaction.commit();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w(TAG, "getUser:onCancelled", databaseError.toException());
                        // ...
                    }
                });


        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        // We need to get the name of each template in a user's collection


    }


}
