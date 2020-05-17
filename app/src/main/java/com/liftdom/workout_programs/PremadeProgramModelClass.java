package com.liftdom.workout_programs;

/**
 * Created by Brodin on 5/17/2020.
 */
public class PremadeProgramModelClass {

    private String mWorkoutType;
    private String mProgramName;
    private String mExperienceLevel;
    private String mProgramDescription;
    private String mWorkoutCode;

    public PremadeProgramModelClass(){
        // necessary for Firebase
    }

    public PremadeProgramModelClass(String workoutType, String programName,
                                    String experienceLevel, String programDescription,
                                    String workoutCode){

        mWorkoutType = workoutType;
        mProgramName = programName;
        mExperienceLevel = experienceLevel;
        mProgramDescription = programDescription;
        mWorkoutCode = workoutCode;
    }

    public String getWorkoutType() {
        return mWorkoutType;
    }

    public void setWorkoutType(String mWorkoutType) {
        this.mWorkoutType = mWorkoutType;
    }

    public String getProgramName() {
        return mProgramName;
    }

    public void setProgramName(String mProgramName) {
        this.mProgramName = mProgramName;
    }

    public String getExperienceLevel() {
        return mExperienceLevel;
    }

    public void setExperienceLevel(String mExperienceLevel) {
        this.mExperienceLevel = mExperienceLevel;
    }

    public String getProgramDescription() {
        return mProgramDescription;
    }

    public void setProgramDescription(String mProgramDescription) {
        this.mProgramDescription = mProgramDescription;
    }

    public String getWorkoutCode() {
        return mWorkoutCode;
    }

    public void setWorkoutCode(String mWorkoutCode) {
        this.mWorkoutCode = mWorkoutCode;
    }
}
