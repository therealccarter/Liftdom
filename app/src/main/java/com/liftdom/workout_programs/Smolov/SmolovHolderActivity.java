package com.liftdom.workout_programs.Smolov;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class SmolovHolderActivity extends MaterialIntroActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addSlide(new SmolovIntroFrag1());

        addSlide(new SmolovIntroFrag2());

        addSlide(new SmolovIntroFrag3());

        addSlide(new SmolovIntroFrag4());

    }
}
