package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;

public class ExercisePickerActivity extends AppCompatActivity {

    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Upper Body","Lower Body"};
    int Numboftabs = 2;

    @BindView(R.id.saveButton) Button saveButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Choose An Exercise");
        setContentView(R.layout.activity_exercise_picker);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        ExercisePickerController.getInstance().exID = getIntent().getExtras().getInt("exID");

        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });

        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(ExercisePickerController.getInstance().exName != null) {
                    String message = ExercisePickerController.getInstance().exName;
                    Intent intent = new Intent();
                    intent.putExtra("MESSAGE", message);
                    setResult(2, intent);
                    ExercisePickerController.getInstance().exName = null;
                }else{
                    Intent intent = new Intent();
                    setResult(1, intent);
                }
                finish();
            }
        });
    }
}
