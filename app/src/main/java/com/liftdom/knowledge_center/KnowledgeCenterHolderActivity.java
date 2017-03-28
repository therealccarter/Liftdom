package com.liftdom.knowledge_center;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class KnowledgeCenterHolderActivity extends AppCompatActivity {

    @BindView(R.id.knowledgeCenterHolder) LinearLayout holderView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_knowledge_center_holder);

        ButterKnife.bind(this);

        if(savedInstanceState == null){
            // get an instance of FragmentTransaction from your Activity
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //add a fragment
            KnowledgeCenterMainFrag templateMenuFrag = new KnowledgeCenterMainFrag();
            fragmentTransaction.add(R.id.knowledgeCenterHolder, templateMenuFrag);
            fragmentTransaction.commit();
        }
    }
}
