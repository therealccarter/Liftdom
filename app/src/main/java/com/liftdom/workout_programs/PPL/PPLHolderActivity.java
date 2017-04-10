package com.liftdom.workout_programs.PPL;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class PPLHolderActivity extends AppCompatActivity {

    @BindView(R.id.pplFragHolder) LinearLayout pplFragHolder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pplholder);

        ButterKnife.bind(this);

        if(savedInstanceState == null){
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            PPLIntroFrag pplIntroFrag = new PPLIntroFrag();
            fragmentTransaction.add(R.id.pplFragHolder, pplIntroFrag);
            fragmentTransaction.commitAllowingStateLoss();
        }
    }
}
