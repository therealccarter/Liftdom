package com.liftdom.template_housing.premade_program_starters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;

public class SmolovStarterActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_starter);
    }
}
