package com.liftdom.user_profile.single_user_profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ProfileActionsDialogActivity extends AppCompatActivity {

    String uidFromOutside;

    @BindView(R.id.profileActionsHolder) LinearLayout profileActionsHolder;
    @BindView(R.id.closeButton) Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actions_dialog);
        //setFinishOnTouchOutside(true);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        uidFromOutside = getIntent().getStringExtra("uidFromOutside");

        if(getIntent().getStringExtra("action") != null){
            if(getIntent().getStringExtra("action").equals("1")){
                addSendMessageFrag();
            }else{
                addSendProgramFrag();
            }
        }

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void addSendMessageFrag(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SendDirectMessageFrag sendDirectMessageFrag = new SendDirectMessageFrag();
        sendDirectMessageFrag.uidFromOutside = uidFromOutside;
        fragmentTransaction.replace(profileActionsHolder.getId(), sendDirectMessageFrag);
        fragmentTransaction.commit();
    }

    private void addSendProgramFrag(){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        SendDirectProgramFrag sendDirectProgram = new SendDirectProgramFrag();
        sendDirectProgram.uidFromOutside = uidFromOutside;
        fragmentTransaction.replace(profileActionsHolder.getId(), sendDirectProgram);
        fragmentTransaction.commit();
    }

}
