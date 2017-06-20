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

        addSlide(new IntroFrag1());

        addSlide(new IntroFrag2());

        addSlide(new SlideFragmentBuilder()
                .backgroundColor(R.color.white)
                .buttonsColor(R.color.black)
                .title("That's it")
                .description("Would you join us?")
                .build());
    }

    @Override
    public void onFinish(){
        super.onFinish();
        Toast.makeText(this, "On Finish!", Toast.LENGTH_SHORT).show();
    }
}
