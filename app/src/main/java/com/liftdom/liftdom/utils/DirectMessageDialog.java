package com.liftdom.liftdom.utils;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.liftdom.liftdom.R;

public class DirectMessageDialog extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direct_message_dialog);

        //TODO: if they already have a single conversation with the user, send it there. otherwise start anew
    }
}
