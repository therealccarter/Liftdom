package com.liftdom.workout_programs.Smolov;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SmolovHolderActivity extends AppCompatActivity {

    @BindView(R.id.smolovFragHolder) LinearLayout smolovFragHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smolov_holder);

        ButterKnife.bind(this);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            SmolovIntroFrag smolovIntroFrag = new SmolovIntroFrag();
            fragmentTransaction.add(R.id.smolovFragHolder, smolovIntroFrag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
