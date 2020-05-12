package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;

import android.content.Intent;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.charts_stats_tools.DigitsInputFilter;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;
import com.liftdom.user_profile.UserModelClass;

public class W531fBWeightDialog extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String templateName = "";
    String exName = "";
    String fragTag;

    @BindView(R.id.titleView) TextView titleView;
    @BindView(R.id.weightEditText) EditText weightEditText;
    @BindView(R.id.unitsView) TextView unitView;
    @BindView(R.id.manualCancelButton) Button manualCancelButton;
    @BindView(R.id.confirmButton) Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_w531f_b_weight_dialog);

        ButterKnife.bind(this);

        if(getIntent().getStringExtra("fragTag") != null){
            fragTag = getIntent().getStringExtra("fragTag");
        }
        if(getIntent().getStringExtra("exName") != null){
            exName = getIntent().getStringExtra("exName");

            String title = getResources().getString(R.string.chooseAssistanceWeight) + exName;

            titleView.setText(title);
        }
        if(getIntent().getStringExtra("templateName") != null){
            templateName = getIntent().getStringExtra("templateName");

            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);

                    boolean isImperial = userModelClass.isIsImperial();

                    if(isImperial){
                        unitView.setText("lbs");
                        weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                        weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
                    }else{
                        unitView.setText("kgs");
                        weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                        weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
                    }

                    DatabaseReference exNameTemplateRef =
                            FirebaseDatabase.getInstance().getReference().child("templates").child(uid)
                                    .child(templateName).child("extraInfo").child(exName);

                    exNameTemplateRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.exists()){
                                try{
                                    String weight = dataSnapshot.getValue(String.class);
                                    weightEditText.setText(weight);
                                }catch (NullPointerException e){

                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

        manualCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fragTag == null){
                    finish();
                }else{
                    Intent intent = new Intent();
                    intent.putExtra("weight", weightEditText.getText().toString());
                    intent.putExtra("fragTag", fragTag);
                    intent.putExtra("exName", exName);
                    setResult(7, intent);
                    finish();
                }
            }
        });
    }
}
