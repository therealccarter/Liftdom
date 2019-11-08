package com.liftdom.workout_programs.PPL_Reddit;


import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.MainActivity;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;
import io.github.dreierf.materialintroscreen.SlideFragment;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class PPLRedditIntroFrag4 extends SlideFragment {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String userName = "";
    boolean isImperial;


    public PPLRedditIntroFrag4() {
        // Required empty public constructor
    }

    @BindView(R.id.finishButton) Button finishButton;
    @BindView(R.id.confirmationTextView) TextView resultsConfirmationView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeProgramCheckbox;
    @BindView(R.id.messageView) TextView messageView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pplreddit_intro_frag4, container, false);

        ButterKnife.bind(this, view);

        activeProgramCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    PPLRedditSingleton.getInstance().isActiveCheckbox = true;
                }else{
                    PPLRedditSingleton.getInstance().isActiveCheckbox = false;
                }
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                userName = userModelClass.getUserName();
                isImperial = userModelClass.isIsImperial();
                PPLRedditSingleton.getInstance().isImperial = isImperial;
                PPLRedditSingleton.getInstance().userName = userName;
                PPLRedditSingleton.getInstance().uid = uid;
                loadingView.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
                if(userModelClass.getActiveTemplate() == null){
                    activeProgramCheckbox.setChecked(true);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        messageView.setText(PPLRedditSingleton.getInstance().getStartDateString());

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingView.setVisibility(View.VISIBLE);
                finishButton.setVisibility(View.GONE);

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
                        modelClass.setUserId(uid);
                        modelClass.setUserName(userName);
                        modelClass.setWorkoutType("PPLReddit");
                        modelClass.setExtraInfo(extraInfoMap);
                        modelClass.setDateCreated(dateTimeString);
                        modelClass.setDateUpdated(dateTimeString);
                        modelClass.setIsImperial(isImperial);
                        modelClass.setDescription("A popular Push/Pull/Legs variant from Reddit. " +
                                "The recommended beginner program.");

                        DatabaseReference newProgramRef =
                                FirebaseDatabase.getInstance().getReference().child("templates").child(uid).child(programName);
                        if(programName != null){
                            newProgramRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(activeProgramCheckbox.isChecked()){
                                        DatabaseReference activeRef =
                                                FirebaseDatabase.getInstance().getReference().child("user").child(uid).child("activeTemplate");
                                        activeRef.setValue(programName).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Intent intent = new Intent(getContext(),
                                                        MainActivity.class);
                                                intent.putExtra("fragID", 1);
                                                startActivity(intent);
                                            }
                                        });
                                    }else{
                                        Intent intent = new Intent(getContext(),
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
        });

        return view;
    }


    @Override
    public int backgroundColor() {
        return R.color.black;
    }

    @Override
    public int buttonsColor() {
        return R.color.liftrGold1;
    }

}
