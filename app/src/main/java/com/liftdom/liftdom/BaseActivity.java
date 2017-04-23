package com.liftdom.liftdom;// Created: 9/10/2016


import android.app.ProgressDialog;
import android.support.annotation.VisibleForTesting;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import it.sephiroth.android.library.bottomnavigation.BottomNavigation;

public class BaseActivity extends AppCompatActivity {

    private BottomNavigation mBottomNavigation;
    private ViewPager mViewPager;


    public BottomNavigation getBottomNavigation() {
        if (mBottomNavigation == null) {
            mBottomNavigation = (BottomNavigation) findViewById(R.id.BottomNavigation);
        }
        return mBottomNavigation;
    }

    public ViewPager getViewPager() {
        if(mViewPager == null){
            mViewPager = (ViewPager) findViewById(R.id.ViewPager01);
        }
        return mViewPager;
    }

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

}