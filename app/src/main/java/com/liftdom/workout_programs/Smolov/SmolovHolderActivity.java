package com.liftdom.workout_programs.Smolov;

import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
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
                modelClass.setRestTime(SmolovSetupSingleton.getInstance().mRestTime);
                modelClass.setIsActiveRestTimer(SmolovSetupSingleton.getInstance().mIsActiveRestTimer);
                modelClass.setVibrationTime(SmolovSetupSingleton.getInstance().mVibrationTime);
                modelClass.setIsRestTimerAlert(SmolovSetupSingleton.getInstance().mIsRestTimerAlert);
                modelClass.setIsImperial(SmolovSetupSingleton.getInstance().isImperial);
                modelClass.setDescription(getResources().getString(R.string.smolovShortDescription));

                DatabaseReference smolovRef = FirebaseDatabase.getInstance().getReference().child("templates")
                        .child(uid).child(programName);
                smolovRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DatabaseReference preMadeCountRef =
                                FirebaseDatabase.getInstance().getReference().child("premadeCount").child("Smolov");
                        preMadeCountRef.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.exists()){
                                    Integer countInt = dataSnapshot.getValue(Integer.class);
                                    countInt++;
                                    preMadeCountRef.setValue(countInt).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            setActiveAndIntent(programName);
                                        }
                                    });
                                }else{
                                    preMadeCountRef.setValue(1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            setActiveAndIntent(programName);
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setActiveAndIntent(String programName){
        if(SmolovSetupSingleton.getInstance().isActiveTemplate){
            DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child
                    ("user").child(SmolovSetupSingleton.getInstance().uid).child("activeTemplate");
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
}
