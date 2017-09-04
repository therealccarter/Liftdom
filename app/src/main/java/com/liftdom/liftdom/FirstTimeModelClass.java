package com.liftdom.liftdom;

/**
 * Created by Brodin on 9/1/2017.
 */

public class FirstTimeModelClass {

    private boolean mIsFeedFirstTime;
    private boolean mIsTemplateMenuFirstTime;
    private boolean mIsAssistorFirstTime;
    private boolean mIsFromScratchFirstTime;
    private boolean mIsSelectedProgFirstTime;
    private boolean mIsSavedProgFirstTime;

    public FirstTimeModelClass(){
        // firebase
    }

    public FirstTimeModelClass(boolean isFeedFirstTime, boolean isTemplateMenuFirstTime, boolean isAssistorFirstTime,
                                boolean isFromScratchFirstTime, boolean isSelectedProgFirstTime, boolean isSavedProgFirstTime){
        mIsFeedFirstTime = isFeedFirstTime;
        mIsTemplateMenuFirstTime = isTemplateMenuFirstTime;
        mIsAssistorFirstTime = isAssistorFirstTime;
        mIsFromScratchFirstTime = isFromScratchFirstTime;
        mIsSelectedProgFirstTime = isSelectedProgFirstTime;
        mIsSavedProgFirstTime = isSavedProgFirstTime;
    }

    public boolean isIsFromScratchFirstTime() {
        return mIsFromScratchFirstTime;
    }

    public boolean isIsSelectedProgFirstTime() {
        return mIsSelectedProgFirstTime;
    }

    public boolean isIsSavedProgFirstTime() {
        return mIsSavedProgFirstTime;
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
