package com.liftdom.user_profile;

import android.graphics.Typeface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.workout_assistor.ExerciseNameFrag;

import java.util.ArrayList;

public class SelectedFutureDateDialog extends AppCompatActivity {

    String formattedDate = "null";
    int collectionNumber = 0;

    @BindView(R.id.selectedDateView) TextView selectedDateView;
    @BindView(R.id.closeButton) Button closeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_future_date_dialog);

        ButterKnife.bind(this);

        formattedDate = getIntent().getExtras().getString("date");
        collectionNumber = getIntent().getExtras().getInt("collectionNumber");

        selectedDateView.setText(formattedDate);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        selectedDateView.setTypeface(lobster);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });


        if(collectionNumber == 1){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(0);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 2){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(2);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 3){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(3);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 4){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(4);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 5){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(5);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 6){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(6);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }else if(collectionNumber == 7){
            ArrayList<String> dataCollection = FutureDateHelperClass.getInstance().DataCollection.get(7);

            for(String data : dataCollection){
                if(isExerciseName(data)){
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    ExerciseNameFrag exerciseNameFrag = new ExerciseNameFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            exerciseNameFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    exerciseNameFrag.exerciseName = data;
                }else {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager
                            .beginTransaction();
                    PastDateRepsWeightFrag repsWeightFrag = new PastDateRepsWeightFrag();
                    fragmentTransaction.add(R.id.eachExerciseFragHolder,
                            repsWeightFrag);
                    if (!isFinishing()) {
                        fragmentTransaction.commitAllowingStateLoss();
                    }
                    repsWeightFrag.repsWeight = data;
                }
            }
        }
    }

    boolean isExerciseName(String input){
        boolean isExercise = true;

        if(input.length() != 0) {
            char c = input.charAt(0);
            if (Character.isDigit(c)) {
                isExercise = false;
            }
        }

        return isExercise;

    }
}
