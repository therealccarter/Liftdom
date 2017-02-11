package com.liftdom.template_housing.premade_program_starters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import org.joda.time.LocalDate;

public class SmolovSavedActivity extends AppCompatActivity {


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    String OneRepMax;
    String ExName;

    void setOneRepMax(String oneRepMax){
        OneRepMax = oneRepMax;
    }

    void setExName(String exName){
        ExName = exName;
    }

    Boolean isActive;

    //public SmolovSavedActivity(String oneRepMax, String exName){
    //    setOneRepMax(oneRepMax);
    //    setExName(exName);
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_saved);

        setExName(getIntent().getStringExtra("exName"));
        setOneRepMax(getIntent().getStringExtra("1rm"));
        isActive = getIntent().getBooleanExtra("isActive", false);

        if(isActive){
            DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
                    ("active_template");
            activeTemplateRef.setValue("Smolov");
        }

        final DatabaseReference smolovRef = mRootRef.child("templates").child(uid).child("Smolov");

        smolovRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                smolovRef.child("1rm").setValue(OneRepMax);
                smolovRef.child("exName").setValue(ExName);
                smolovRef.child("timeStamp").setValue(LocalDate.now().toString());

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
