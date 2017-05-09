package com.liftdom.template_editor;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Brodin on 5/9/2017.
 */

public class TemplateEditorSingleton {

    String mTemplateName;
    String mUserId;
    String mUserName;
    boolean mIsPublic = false;
    String mDateCreated;
    HashMap<Integer, List<String>> mMondayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mTuesdayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mWednesdayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mThursdayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mFridayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mSaturdayMap = new HashMap<Integer, List<String>>();
    HashMap<Integer, List<String>> mSundayMap = new HashMap<Integer, List<String>>();
    boolean mIsAlgorithm = false;
    HashMap<String, List<String>> mAlgorithmInfo = new HashMap<String, List<String>>();

    //TODO: at the end, if a map is empty, but a dummy value in there so we can later update the child

}
