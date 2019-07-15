package com.liftdom.template_housing.public_programs;


import android.app.Activity;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.utils.SlidingTabLayout;

/**
 * A simple {@link Fragment} subclass.
 */
public class PublicTemplateChooserFrag extends Fragment {


    public PublicTemplateChooserFrag() {
        // Required empty public constructor
    }

    headerChangeFromFrag mCallback;

    public interface headerChangeFromFrag{
        void changeHeaderTitle(String title);
    }

    private void headerChanger(String title){
        mCallback.changeHeaderTitle(title);
    }

    PublicProgramsPagerAdapter adapter;
    CharSequence Titles[]={"All Public Programs", "My Public Programs"};
    int NumbOfTabs = 2;

    @BindView(R.id.tabs) SlidingTabLayout tabsView;
    @BindView(R.id.pager) ViewPager pager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_public_template_chooser, container, false);

        ButterKnife.bind(this, view);

        //fragmentTransaction.replace(R.id.mainFragHolder, new PublicTemplatesFrag());
        //fragmentTransaction.replace(R.id.mainFragHolder, new MyPublicTemplatesFrag());

        if(savedInstanceState == null){
            setUpSlidingLayout();
        }

        return view;
    }

    private void setUpSlidingLayout(){
        adapter = new PublicProgramsPagerAdapter(this.getChildFragmentManager(), Titles, NumbOfTabs);
        pager.setAdapter(adapter);
        tabsView.setDistributeEvenly(true);
        tabsView.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabsView.setViewPager(pager);
    }

    @Override
    public void onStart(){
        super.onStart();
        headerChanger("Public Programs");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (headerChangeFromFrag) activity;
        } catch (ClassCastException e) {
            //throw new ClassCastException(activity.toString()
            //        + " must implement OnHeadlineSelectedListener");
        }
    }

}
