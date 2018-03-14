package com.liftdom.user_profile.single_user_profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.liftdom.liftdom.R;

public class UserProfileDialogActivity extends AppCompatActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.profileLinearLayout) LinearLayout profileLinearLayout;
    @BindView(R.id.closeButton) TextView closeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_dialog);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        ButterKnife.bind(this);

        closeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if(getIntent().getStringExtra("xUid") != null){
            if(getIntent().getStringExtra("xUid").equals(uid)){
                // current user
                addProfileFrag(uid);
            }else{
                // other user
                addProfileFrag(getIntent().getStringExtra("xUid"));
            }
        }else{
            // current user
            addProfileFrag(uid);
        }
    }

    private void addProfileFrag(String userId){
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        UserProfileFrag userProfileFrag = new UserProfileFrag();
        userProfileFrag.uidFromOutside = userId;
        fragmentTransaction.replace(profileLinearLayout.getId(), userProfileFrag);
        fragmentTransaction.commit();
    }
}
