package com.liftdom.user_profile.single_user_profile;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.liftdom.knowledge_center.KnowledgeCenterHolderActivity;
import com.liftdom.liftdom.BaseActivity;
import com.liftdom.liftdom.R;

public class UserProfileFullActivity extends BaseActivity {

    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @BindView(R.id.profileLinearLayout) LinearLayout profileLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_full);

        ButterKnife.bind(this);

        // Handle Toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);

        setUpNavDrawer(UserProfileFullActivity.this, toolbar);

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
