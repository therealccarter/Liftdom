package com.liftdom.liftdom.intro;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import agency.tango.materialintroscreen.SlideFragment;
import agency.tango.materialintroscreen.SlideFragmentBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import com.liftdom.liftdom.R;

public class FirstTimeSetupActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_first_time_setup);

        String userId = getIntent().getExtras().getString("uid");
        String defaultDisplayName = null;
        if(getIntent().getExtras().getString("defaultDisplayName") != null){
            defaultDisplayName = getIntent().getExtras().getString("defaultDisplayName");
        }

        IntroSingleton.getInstance().userId = userId;
        IntroSingleton.getInstance().defaultDisplayName = defaultDisplayName;

        addSlide(new IntroFrag1());

        addSlide(new IntroFrag2());

        addSlide(new IntroFrag3());

        addSlide(new IntroFrag4());
    }

    @Override
    public void onFinish(){
        super.onFinish();
        Toast.makeText(this, "On Finish!", Toast.LENGTH_SHORT).show();
    }

}
