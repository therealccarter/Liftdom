package com.liftdom.settings;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liftdom.liftdom.LoginActivity;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

public class SettingsListActivity extends AppCompatActivity {

    private static final String TAG = "EmailPassword";

    // declare_auth
    private FirebaseUser mFirebaseUser;
    private FirebaseAuth mAuth;

    private FirebaseAuth.AuthStateListener mAuthListener;

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.sign_out_button) Button signOutButton;
    @BindView(R.id.settingsTitle) TextView settingsTitle;
    @BindView(R.id.lbsWeight) RadioButton poundsWeight;
    @BindView(R.id.kgsWeight) RadioButton kiloWeight;
    @BindView(R.id.lbsBodyWeight) RadioButton poundsBodyWeight;
    @BindView(R.id.kgsBodyWeight) RadioButton kiloBodyWeight;
    @BindView(R.id.footInchHeight) RadioButton footInchesHeight;
    @BindView(R.id.centimetersHeight) RadioButton centiHeight;
    @BindView(R.id.checkBoxRound) CheckBox checkBoxRound;
    @BindView(R.id.saveButton) Button saveButton;

    ArrayList<String> settingsArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_list);

        ButterKnife.bind(this);

        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        settingsTitle.setTypeface(lobster);

        signOutButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAuth.signOut();
                Intent intent = new Intent(v.getContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(poundsWeight.isChecked()){
                    firebaseSetter("weightUnit", "pounds");
                }else if(kiloWeight.isChecked()){
                    firebaseSetter("weightUnit", "kilos");
                }
                if(poundsBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "pounds");
                }else if(kiloBodyWeight.isChecked()){
                    firebaseSetter("bodyWeightUnit", "kilos");
                }
                if(footInchesHeight.isChecked()){
                    firebaseSetter("heightUnit", "footInches");
                }else if(centiHeight.isChecked()){
                    firebaseSetter("heightUnit", "centimeters");
                }
                if(checkBoxRound.isChecked()){
                    firebaseSetter("roundWeight", "yes");
                }else{
                    firebaseSetter("roundWeight", "no");
                }
            }
        });
    }

    void firebaseSetter(String key, String value){
        DatabaseReference dataRef = mRootRef.child("users").child(uid).child(key);
        dataRef.setValue(value);
    }
}
