package com.liftdom.user_profile.calendar_stuff;

import android.graphics.Typeface;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.liftdom.main_social_feed.completed_workout_post.WorkoutInfoRecyclerAdapter;
import com.liftdom.workout_assistor.ExerciseNameFrag;
import com.liftdom.workout_programs.FiveThreeOne_ForBeginners.Wendler_531_For_Beginners;
import com.liftdom.workout_programs.Smolov.Smolov;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SelectedFutureDateDialog extends AppCompatActivity {

    String formattedDate = "null";
    int collectionNumber = 0;
    public Boolean isOtherUser = false;
    public String xUid = "null";
    boolean isTemplateImperial;
    boolean isCurrentUserImperial;

    boolean isSmolov;

    @BindView(R.id.selectedDateView) TextView selectedDateView;
    @BindView(R.id.closeButton) Button closeButton;
    @BindView(R.id.infoRecyclerView) RecyclerView infoRecyclerView;

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

        isCurrentUserImperial = getIntent().getExtras().getBoolean("isCurrentUserImperial");
        isTemplateImperial = getIntent().getExtras().getBoolean("isTemplateImperial");

        formattedDate = getIntent().getExtras().getString("date");
        collectionNumber = getIntent().getExtras().getInt("collectionNumber");
        if(getIntent().getStringExtra("type") != null){
            String type = getIntent().getStringExtra("type");
            if(type.equals("Smolov")){
                String maxWeight = getIntent().getStringExtra("maxWeight");
                String exName = getIntent().getStringExtra("exName");
                String beginDate = getIntent().getStringExtra("beginDate");
                String round = getIntent().getStringExtra("isRound");
                generateLayoutSmolov(maxWeight, exName, beginDate, round);
            }else if(type.equals("W531fB")){
                HashMap<String, String> map =
                        (HashMap<String, String>) getIntent().getSerializableExtra("W531fBMap");
                generateLayoutW531fB(map);
            }
        }else{
            generateLayout();
        }

        selectedDateView.setText(formattedDate);
        Typeface lobster = Typeface.createFromAsset(getAssets(), "fonts/Lobster-Regular.ttf");
        selectedDateView.setTypeface(lobster);

        closeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void generateLayoutSmolov(String maxWeight, String exName, String beginDate,
                                      String round){
        Smolov smolov = new Smolov(exName, maxWeight);
        HashMap<String, List<String>> workoutMap = new HashMap<>();
        workoutMap.putAll(smolov.getMapForSpecificDay(beginDate, formattedDate,
                Boolean.parseBoolean(round)));

        setUpAdapter(workoutMap);
    }

    private void generateLayoutW531fB(HashMap<String, String> infoMap){
        Wendler_531_For_Beginners w531fB = new Wendler_531_For_Beginners(infoMap);
        HashMap<String, List<String>> workoutMap = new HashMap<>();
        workoutMap.putAll(w531fB.generateSpecificWithDates(formattedDate, isTemplateImperial, isCurrentUserImperial));

        setUpAdapter(workoutMap);
    }

    private void setUpAdapter(HashMap<String, List<String>> workoutMap){
        WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(workoutMap, this);
        adapter.setIsOriginallyImperial(isTemplateImperial);
        //adapter.setInfoList(workoutInfoMap);
        adapter.setImperialPOV(isCurrentUserImperial);
        infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
        infoRecyclerView.setHasFixedSize(false);
        infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
    }

    private void generateLayout(){
        if(collectionNumber == 1){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("0_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 2){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("1_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 3){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("2_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 4){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("3_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 5){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("4_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 6){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("5_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }else if(collectionNumber == 7){
            HashMap<String, List<String>> map = new HashMap<>();
            map.putAll(FutureDateHelperClass.getInstance().DataCollectionMap.get("6_key"));
            WorkoutInfoRecyclerAdapter adapter = new WorkoutInfoRecyclerAdapter(map, this);
            adapter.setIsOriginallyImperial(isTemplateImperial);
            //adapter.setInfoList(workoutInfoMap);
            adapter.setImperialPOV(isCurrentUserImperial);
            infoRecyclerView.setAdapter(adapter); // isImperialPOV = false
            infoRecyclerView.setHasFixedSize(false);
            infoRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        }
    }
}
