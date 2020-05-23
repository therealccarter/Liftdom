package com.liftdom.workout_programs.PPL_Reddit;

import android.content.Intent;
import androidx.annotation.NonNull;
import android.os.Bundle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.template_editor.TemplateModelClass;
import io.github.dreierf.materialintroscreen.MaterialIntroActivity;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;

public class PPLRedditHolderActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new PPLRedditIntroFrag1());

        addSlide(new PPLRedditIntroFrag2());

        addSlide(new PPLRedditIntroFrag4());

        addSlide(new PPLRedditIntroFrag6());

    }

    @Override
    public void onFinish(){
        super.onFinish();

        DatabaseReference defaultRef =
                FirebaseDatabase.getInstance().getReference().child("defaultTemplates").child("FirstTimeProgram");
        defaultRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                HashMap<String, String> extraInfoMap = new HashMap<>();
                extraInfoMap.putAll(PPLRedditSingleton.getInstance().assembleExtraInfoMap());
                final String programName = PPLRedditSingleton.getInstance().programName;

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateTimeString = dateTime.toString();

                TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                modelClass.setTemplateName(programName);
                modelClass.setUserId(PPLRedditSingleton.getInstance().uid);
                modelClass.setUserName(PPLRedditSingleton.getInstance().userName);
                modelClass.setWorkoutType("PPLReddit");
                modelClass.setExtraInfo(extraInfoMap);
                modelClass.setDateCreated(dateTimeString);
                modelClass.setDateUpdated(dateTimeString);
                modelClass.setIsImperial(PPLRedditSingleton.getInstance().isImperial);
                modelClass.setDescription("A popular Push/Pull/Legs variant from Reddit. " +
                        "The recommended beginner program.");

                DatabaseReference newProgramRef =
                        FirebaseDatabase.getInstance().getReference().child("templates").child(PPLRedditSingleton.getInstance().uid).child(programName);
                if(programName != null){
                    newProgramRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(PPLRedditSingleton.getInstance().isActiveCheckbox){
                                DatabaseReference activeRef =
                                        FirebaseDatabase.getInstance().getReference().child("user").child(PPLRedditSingleton.getInstance().uid).child("activeTemplate");
                                activeRef.setValue(programName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(PPLRedditHolderActivity.this,
                                                MainActivity.class);
                                        intent.putExtra("fragID", 1);
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                Intent intent = new Intent(PPLRedditHolderActivity.this,
                                        MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}
