package com.liftdom.workout_programs.FiveThreeOne_ForBeginners;


import io.github.dreierf.materialintroscreen.SlideFragment;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
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
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class W531fBIntroFrag5 extends SlideFragment {


    public W531fBIntroFrag5() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String userName = "";
    boolean isImperial;

    private HashMap<String, List<String>> mMapOne = new HashMap<>();
    private HashMap<String, List<String>> mMapTwo = new HashMap<>();
    private HashMap<String, List<String>> mMapThree = new HashMap<>();

    @BindView(R.id.finishSmolovButton) Button finishButton;
    @BindView(R.id.confirmationTextView) TextView resultsConfirmationView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;
    @BindView(R.id.activeTemplateCheckbox) CheckBox activeProgramCheckbox;
    //@BindView(R.id.messageView) TextView messageView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_w531f_bintro_frag5, container, false);

        ButterKnife.bind(this, view);

        activeProgramCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    W531fBSingleton.getInstance().isActiveCheckbox = true;
                }else{
                    W531fBSingleton.getInstance().isActiveCheckbox = false;
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
                W531fBSingleton.getInstance().isImperial = isImperial;
                W531fBSingleton.getInstance().userName = userName;
                W531fBSingleton.getInstance().uid = uid;
                if(userModelClass.getActiveTemplate() != null){
                    try{
                        String string = userModelClass.getActiveTemplate();
                        if(string == null){
                            activeProgramCheckbox.setChecked(true);
                            if(string.isEmpty()){
                                activeProgramCheckbox.setChecked(true);
                            }else{
                                activeProgramCheckbox.setChecked(false);
                            }
                        }else{
                            activeProgramCheckbox.setChecked(false);
                        }
                    }catch (NullPointerException e){
                        activeProgramCheckbox.setChecked(true);
                    }

                }else{
                    activeProgramCheckbox.setChecked(true);
                }
                loadingView.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //messageView.setText(W531fBSingleton.getInstance().getStartDateString());

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingView.setVisibility(View.VISIBLE);
                finishButton.setVisibility(View.GONE);

                //assembleHashMaps();

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
                        modelClass.setUserId(uid);
                        modelClass.setUserName(userName);
                        modelClass.setWorkoutType("W531fB");
                        modelClass.setExtraInfo(extraInfoMap);
                        modelClass.setDateCreated(dateTimeString);
                        modelClass.setDateUpdated(dateTimeString);
                        modelClass.setRestTime(W531fBSingleton.getInstance().mRestTime);
                        modelClass.setIsActiveRestTimer(W531fBSingleton.getInstance().mIsActiveRestTimer);
                        modelClass.setVibrationTime(W531fBSingleton.getInstance().mVibrationTime);
                        modelClass.setIsRestTimerAlert(W531fBSingleton.getInstance().mIsRestTimerAlert);
                        modelClass.setIsImperial(isImperial);
                        modelClass.setDescription(getResources().getString(R.string.W5314BShortDescription));

                        DatabaseReference newProgramRef = FirebaseDatabase.getInstance().getReference().child
                                ("templates").child(uid).child(programName);
                        if(programName != null){
                            newProgramRef.setValue(modelClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    DatabaseReference preMadeCountRef =
                                            FirebaseDatabase.getInstance().getReference().child("premadeCount").child("W531fB");
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

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

        return view;

    }

    private void setActiveAndIntent(String programName){
        if(activeProgramCheckbox.isChecked()){
            DatabaseReference activeRef = FirebaseDatabase.getInstance().getReference().child
                    ("user").child(uid).child("activeTemplate");
            activeRef.setValue(programName).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.putExtra("fragID", 1);
                    startActivity(intent);
                }
            });
        }else{
            Intent intent = new Intent(getContext(), MainActivity.class);
            intent.putExtra("fragID", 1);
            startActivity(intent);
        }
    }

    private void assembleHashMaps(){

        double squatTM = round((Double.parseDouble(W531fBSingleton.getInstance().squatMax) * 0.9), 2);
        double benchTM = round((Double.parseDouble(W531fBSingleton.getInstance().benchMax) * 0.9), 2);
        double deadliftTM = round((Double.parseDouble(W531fBSingleton.getInstance().deadliftMax) * 0.9), 2);


        /*
         Week 1:
         1x5 @ 65%
         1x5 @ 75%
         1x5+ @ 85%

         Week 2:
         1x3 @ 70%
         1x3 @ 80%
         1x3+ @ 90%

         Week 3:
         1x5 @ 75%
         1x3 @ 85%
         1x1+ @ 95%
         */

        //List<String> mapOneList1 = new ArrayList<>();
        //mapOneList1.add("Monday_");
        //mMapOne.put("0_key", mapOneList1);
        //List<String> mapOneList2 = new ArrayList<>();
        //mapOneList2.add("Monday_"); ()



    }

    public double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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