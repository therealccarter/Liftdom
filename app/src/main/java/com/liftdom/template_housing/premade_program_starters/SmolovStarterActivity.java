package com.liftdom.template_housing.premade_program_starters;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.R;
import com.liftdom.workout_programs.Smolov;

import java.util.ArrayList;

public class SmolovStarterActivity extends AppCompatActivity {

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.activeTemplateCheckbox) CheckBox activeTemplateCheckbox;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.oneRepMaxEditText) EditText oneRepMaxEditText;
    @BindView(R.id.movementName) Button movementName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_starter);

        ButterKnife.bind(this);

        // let's test our smolov object...
        Smolov smolov = new Smolov(1, 1, 275);
        ArrayList<String> arrayList = smolov.getWorkout();

        String what = arrayList.get(0);

        //DatabaseReference activeTemplateRef = mRootRef.child("users").child(uid).child
        //        ("active_template");
        //activeTemplateRef.setValue("Smolov");

    }
}
