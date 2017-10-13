package com.liftdom.workout_programs.Smolov;


import agency.tango.materialintroscreen.SlideFragment;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.irozon.library.HideKey;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.TemplateModelClass;
import com.liftdom.user_profile.UserModelClass;
import com.wang.avi.AVLoadingIndicatorView;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class SmolovIntroFrag4 extends SlideFragment {


    public SmolovIntroFrag4() {
        // Required empty public constructor
    }

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String userName = "";
    boolean isImperial;

    @BindView(R.id.finishSmolovButton) Button finishButton;
    @BindView(R.id.confirmationTextView) TextView resultsConfirmationView;
    @BindView(R.id.loadingView) AVLoadingIndicatorView loadingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_smolov_intro_frag4, container, false);

        ButterKnife.bind(this, view);



        if(SmolovSetupSingleton.getInstance().isBeginToday){
            resultsConfirmationView.setText(
                    "Exercise: " + SmolovSetupSingleton.getInstance().exName
                            + "\n \n Current 1 rep max: " + SmolovSetupSingleton.getInstance().maxWeight
                            + "\n \n Program beginning today."
            );
        }else{
            resultsConfirmationView.setText(
                    "Exercise: " + SmolovSetupSingleton.getInstance().exName
                            + "\n \n Current 1 rep max: " + SmolovSetupSingleton.getInstance().maxWeight
                            + "\n \n Program beginning on Monday."
            );
        }

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference().child("user").child(uid);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                userName = userModelClass.getUserName();
                isImperial = userModelClass.isIsImperial();
                loadingView.setVisibility(View.GONE);
                finishButton.setVisibility(View.VISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> extraInfoMap = new HashMap<>();
                extraInfoMap.putAll(SmolovSetupSingleton.getInstance().assembleSmolovMap());

                TemplateModelClass modelClass = new TemplateModelClass(

                );

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
