package com.liftdom.workout_programs.Smolov;


import agency.tango.materialintroscreen.SlideFragment;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.transition.Slide;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.charts_stats_tools.exercise_selector.ExSelectorActivity;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovIntroFrag2 extends SlideFragment {


    public SmolovIntroFrag2() {
        // Required empty public constructor
    }

    @BindView(R.id.exerciseButton) Button exerciseButton;
    @BindView(R.id.todayRadioButton) RadioButton todayRadioButton;
    @BindView(R.id.mondayRadioButton) RadioButton mondayRadioButton;
    @BindView(R.id.maxWeightEditText) EditText maxWeightEditText;
    @BindView(R.id.unitsView) TextView unitsView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_intro_frag2, container, false);

        ButterKnife.bind(this, view);

        HideKey.initialize(getActivity(), view);

        exerciseButton.setText("Squat (Barbell - Back)");

        mondayRadioButton.setChecked(true);

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExSelectorActivity.class);
                int exID = exerciseButton.getId();
                intent.putExtra("exID", exID);
                startActivityForResult(intent, 2);
            }
        });

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(FirebaseAuth
                .getInstance().getCurrentUser().getUid());
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                if(!userModelClass.isIsImperial()){
                    unitsView.setText("kgs");
                }else{
                    unitsView.setText("lbs");
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null) {
            if (requestCode == 2) {
                if (data.getStringExtra("MESSAGE") != null) {
                    String message = data.getStringExtra("MESSAGE");
                    //String newMessage = newLineExname(message);
                    if (isBodyweight(message)) {
                        Snackbar.make(getView(), "Bodyweight exercise not allowed for Smolov", Snackbar.LENGTH_SHORT).show();
                    } else {
                        exerciseButton.setText(message);
                    }
                }
            }
        }
    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }

    @Override
    public boolean canMoveFurther(){
        boolean valuesEntered = false;

        if(!maxWeightEditText.getText().toString().equals("") && !maxWeightEditText.getText().toString().equals(" ")){
            SmolovSetupSingleton.getInstance().isBeginToday = todayRadioButton.isChecked();
            SmolovSetupSingleton.getInstance().exName = exerciseButton.getText().toString();
            SmolovSetupSingleton.getInstance().maxWeight = maxWeightEditText.getText().toString();
            valuesEntered = true;
        }

        return valuesEntered;
    }

    @Override
    public String cantMoveFurtherErrorMessage() {
        return getString(R.string.invalidSmolovFields);
    }

    @Override
    public int backgroundColor() {
        return R.color.confirmgreen;
    }

    @Override
    public int buttonsColor() {
        return R.color.black;
    }

}
