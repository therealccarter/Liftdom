package com.liftdom.workout_programs.Smolov;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;

public class SmolovHolderActivity extends MaterialIntroActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SmolovIntroFrag1());

        addSlide(new SmolovIntroFrag2());

        addSlide(new SmolovIntroFrag3());

        addSlide(new SmolovIntroFrag4());

    }

    @Override
    public void onFinish() {
        super.onFinish();
        DatabaseReference defaultRef = FirebaseDatabase.getInstance().getReference().child
                ("defaultTemplates").child("FirstTimeProgram");
        defaultRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, String> extraInfoMap = new HashMap<>();
                extraInfoMap.putAll(SmolovSetupSingleton.getInstance().assembleSmolovMap());
                final String programName = SmolovSetupSingleton.getInstance().programName;
                final String uid = SmolovSetupSingleton.getInstance().uid;

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateTimeString = dateTime.toString();

                TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                modelClass.setTemplateName(programName);
                modelClass.setUserId(uid);
                modelClass.setUserName(SmolovSetupSingleton.getInstance().userName);
                modelClass.setWorkoutType("Smolov");
                modelClass.setExtraInfo(extraInfoMap);
                modelClass.setDateCreated(dateTimeString);
                modelClass.setDateUpdated(dateTimeString);
                modelClass.setIsImperial(SmolovSetupSingleton.getInstance().isImperial);

                DatabaseReference smolovRef = FirebaseDatabase.getInstance().getReference().child("templates")
                        .child(uid).child(programName);
                smolovRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(SmolovSetupSingleton.getInstance().isActiveTemplate){
                            DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child
                                    ("user").child(uid).child("activeTemplate");
                            activeRef.setValue(programName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    Intent intent = new Intent(SmolovHolderActivity.this, MainActivity.class);
                                    intent.putExtra("fragID", 1);
                                    startActivity(intent);
                                }
                            });
                        }else{
                            Intent intent = new Intent(SmolovHolderActivity.this, MainActivity.class);
                            intent.putExtra("fragID", 1);
                            startActivity(intent);
                        }
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
