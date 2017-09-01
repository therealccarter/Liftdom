package com.liftdom.liftdom;

/**
 * Created by Brodin on 9/1/2017.
 */

public class FirstTimeModelClass {

    private boolean mIsFeedFirstTime;
    private boolean mIsTemplateMenuFirstTime;
    private boolean mIsAssistorFirstTime;

    public FirstTimeModelClass(){
        // firebase
    }

    public FirstTimeModelClass(boolean isFeedFirstTime, boolean isTemplateMenuFirstTime, boolean isAssistorFirstTime){
        mIsFeedFirstTime = isFeedFirstTime;
        mIsTemplateMenuFirstTime = isTemplateMenuFirstTime;
        mIsAssistorFirstTime = isAssistorFirstTime;
    }

    public boolean isIsFeedFirstTime() {
        return mIsFeedFirstTime;
    }

    public void setIsFeedFirstTime(boolean mIsFeedFirstTime) {
        this.mIsFeedFirstTime = mIsFeedFirstTime;
    }

    public boolean isIsTemplateMenuFirstTime() {
        return mIsTemplateMenuFirstTime;
    }

    public void setIsTemplateMenuFirstTime(boolean mIsTemplateMenuFirstTime) {
        this.mIsTemplateMenuFirstTime = mIsTemplateMenuFirstTime;
    }

    public boolean isIsAssistorFirstTime() {
        return mIsAssistorFirstTime;
    }

    public void setIsAssistorFirstTime(boolean mIsAssistorFirstTime) {
        this.mIsAssistorFirstTime = mIsAssistorFirstTime;
    }
}
