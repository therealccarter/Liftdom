package com.liftdom.liftdom;

import android.content.Context;
import androidx.multidex.MultiDex;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Brodin on 1/31/2020.
 */
public class Application extends android.app.Application {

    @Override
    public void onCreate(){
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
}
