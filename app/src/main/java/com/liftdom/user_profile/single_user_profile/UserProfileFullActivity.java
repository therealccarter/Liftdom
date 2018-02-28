package com.liftdom.user_profile.single_user_profile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class UserProfileFullActivity extends AppCompatActivity {


    @BindView(R.id.profileLinearLayout) LinearLayout profileLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile_full);

        ButterKnife.bind(this);



    }
}
