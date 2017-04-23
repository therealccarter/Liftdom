package com.liftdom.charts_stats_tools.exercise_selector;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;
import com.liftdom.template_editor.ExercisePickerController;

public class ExSelectorActivity extends AppCompatActivity {

    ViewPager pager;
    ExPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Upper Body", "Lower Body", "Other"};
    int Numboftabs = 3;

    @BindView(R.id.confirmButton) Button confirmButton;

    //public ExSelectorActivity(Boolean isExclusive){
//
    //}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_ex_selector);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        if(getIntent().getExtras() != null){
            ExercisePickerController.getInstance().exID = getIntent().getExtras().getInt("exID");
            // is from template editor
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, true, false);
            confirmButton.setVisibility(View.GONE);
        }else{
            // is from ex-chart frag
            // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
            adapter =  new ExPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, false, true);
        }

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

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                setResult(1);
                finish();
            }
        });

    }
}
