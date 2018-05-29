package com.liftdom.workout_programs.FiveThreeOne;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.template_editor.TemplateModelClass;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;

/**
 * Created by Brodin on 5/18/2018.
 */
public class W531fB_HolderActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addSlide(new W531fBIntroFrag1());

        addSlide(new W531fBIntroFrag2());

        addSlide(new W531fBIntroFrag3());
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
                extraInfoMap.putAll(W531fBSingleton.getInstance().assembleExtraInfoMap());
                final String programName = W531fBSingleton.getInstance().programName;

                DateTime dateTime = new DateTime(DateTimeZone.UTC);
                String dateTimeString = dateTime.toString();

                TemplateModelClass modelClass = dataSnapshot.getValue(TemplateModelClass.class);
                modelClass.setTemplateName(programName);
                modelClass.setUserId(W531fBSingleton.getInstance().uid);
                modelClass.setUserName(W531fBSingleton.getInstance().userName);
                modelClass.setWorkoutType("W531fB");
                modelClass.setExtraInfo(extraInfoMap);
                modelClass.setDateCreated(dateTimeString);
                modelClass.setDateUpdated(dateTimeString);
                modelClass.setIsImperial(W531fBSingleton.getInstance().isImperial);
                modelClass.setDescription("Wendler\'s 5/3/1 For Beginners is an intermediate strength " +
                        "program that can provide amazing and consistent strength gains when done correctly.");

                DatabaseReference newProgramRef = FirebaseDatabase.getInstance().getReference().child
                        ("templates").child(W531fBSingleton.getInstance().uid).child(programName);
                if(programName != null){
                    newProgramRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(W531fBSingleton.getInstance().isActiveCheckbox){
                                DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child
                                        ("user").child(W531fBSingleton.getInstance().uid).child("activeTemplate");
                                activeRef.setValue(programName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Intent intent = new Intent(W531fB_HolderActivity.this, MainActivity.class);
                                        intent.putExtra("fragID", 1);
                                        startActivity(intent);
                                    }
                                });
                            }else{
                                Intent intent = new Intent(W531fB_HolderActivity.this, MainActivity.class);
                                intent.putExtra("fragID", 1);
                                startActivity(intent);
                            }
                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
