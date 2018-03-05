package com.liftdom.user_profile.single_user_profile;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import butterknife.BindView;
import com.liftdom.liftdom.R;

public class ProfileActionsDialogActivity extends AppCompatActivity {

    @BindView(R.id.profileActionsHolder) LinearLayout profileActionsHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_actions_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        if(getIntent().getStringExtra("action") != null){
            if(getIntent().getStringExtra("action").equals("1")){
                addSendMessageFrag();
            }else{
                addSendProgramFrag();
            }
        }
    }

    private void addSendMessageFrag(){

    }

    private void addSendProgramFrag(){

    }

}
