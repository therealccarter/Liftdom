package com.liftdom.user_profile.calendar_stuff;

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
import java.util.HashMap;
import java.util.List;

public class SelectedFutureDateDialog extends AppCompatActivity {

    String formattedDate = "null";
    int collectionNumber = 0;
    public Boolean isOtherUser = false;
    public String xUid = "null";

    @BindView(R.id.selectedDateView) TextView selectedDateView;
    @BindView(R.id.closeButton) Button closeButton;

    //TODO: Add buttons to go forward or backwards in time. Get current position in collection and add or subtract

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_selected_future_date_dialog);

        ButterKnife.bind(this);

        if(isOtherUser){
           // uid = xUid;
        }

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

        generateLayout();
    }

    private void generateLayout(){
        if(collectionNumber == 1){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("0_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 2){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("1_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 3){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("2_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 4){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("3_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 5){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("4_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 6){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("5_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }else if(collectionNumber == 7){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("6_key"));
            FutureDateDialogSubFrag subFrag = new FutureDateDialogSubFrag();
            subFrag.map = map;
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.eachExerciseFragHolder, subFrag);
            fragmentTransaction.commit();
        }
    }
}
