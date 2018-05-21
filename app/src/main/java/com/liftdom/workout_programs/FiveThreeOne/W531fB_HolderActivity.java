package com.liftdom.workout_programs.FiveThreeOne;

import agency.tango.materialintroscreen.MaterialIntroActivity;
import android.os.Bundle;

/**
 * Created by Brodin on 5/18/2018.
 */
public class W531fB_HolderActivity extends MaterialIntroActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        addSlide(new W531fBIntroFrag1());

        addSlide(new W531fBIntroFrag2());

        addSlide(new W531fBIntroFrag3());
    }
}
