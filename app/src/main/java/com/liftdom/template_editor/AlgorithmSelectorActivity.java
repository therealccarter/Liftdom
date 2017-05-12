package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AlgorithmSelectorActivity extends AppCompatActivity {

    ArrayList<String> algoInfoList = new ArrayList<>();
    List<String> tempAlgoInfoList = new ArrayList<>();
    boolean isLoop = false;
    String day = "null";
    String exName = "null";

    @BindView(R.id.setsWeeksEditText) EditText setsWeeksEditText;
    @BindView(R.id.setsIncreaseEditText) EditText setsIncreasedEditText;
    @BindView(R.id.repsWeeksEditText) EditText repsWeeksEditText;
    @BindView(R.id.repsIncreaseEditText) EditText repsIncreasedEditText;
    @BindView(R.id.weightsWeeksEditText) EditText weightsWeeksEditText;
    @BindView(R.id.weightsIncreaseEditText) EditText  weightsIncreasedEditText;
    @BindView(R.id.cancelButton) ImageButton cancelButton;
    @BindView(R.id.confirmButton) ImageButton confirmButton;
    @BindView(R.id.algorithmLooper) CheckBox algorithmLooper;
    @BindView(R.id.applyAlgoToExs) CheckBox applyAlgoToExs;
    @BindView(R.id.title) TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        exName = getIntent().getExtras().getString("exName");
        if(getIntent().getExtras().getString("day") != null){
            day = getIntent().getExtras().getString("day");
        }

        String concat = "Set Algorithm For: " + exName;
        titleView.setText(concat);


        if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()){
            for(Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()){
                if(entry.getKey().equals(exName)){
                    List<String> tempList = entry.getValue();

                    if(Boolean.valueOf(tempList.get(7))){
                        setsWeeksEditText.setText(tempList.get(0));
                        setsIncreasedEditText.setText(tempList.get(1));
                        repsWeeksEditText.setText(tempList.get(2));
                        repsIncreasedEditText.setText(tempList.get(3));
                        weightsWeeksEditText.setText(tempList.get(4));
                        weightsIncreasedEditText.setText(tempList.get(5));
                        boolean isLoop = Boolean.valueOf(tempList.get(6));
                        if(isLoop){
                            algorithmLooper.setChecked(true);
                        }
                        applyAlgoToExs.setChecked(true);
                    }else{
                        if(!day.equals("null")){
                            if(tempList.get(8).equals(day)){
                                setsWeeksEditText.setText(tempList.get(0));
                                setsIncreasedEditText.setText(tempList.get(1));
                                repsWeeksEditText.setText(tempList.get(2));
                                repsIncreasedEditText.setText(tempList.get(3));
                                weightsWeeksEditText.setText(tempList.get(4));
                                weightsIncreasedEditText.setText(tempList.get(5));
                                boolean isLoop = Boolean.valueOf(tempList.get(6));
                                if(isLoop){
                                    algorithmLooper.setChecked(true);
                                }
                            }
                        }
                    }
                }
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                //TODO: Just realized I'm going to have to not have exercise names as keys if there can be multiples...

                String setsWeeks = setsWeeksEditText.getText().toString();
                String repsWeeks = repsWeeksEditText.getText().toString();
                String weightWeeks = weightsWeeksEditText.getText().toString();

                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();

                String isLooper = String.valueOf(algorithmLooper.isChecked());
                String applyToAllExs = String.valueOf(applyAlgoToExs.isChecked());

                algoInfoList.clear();
                tempAlgoInfoList.clear();

                algoInfoList.add(setsWeeks);
                algoInfoList.add(setsIncrease);
                algoInfoList.add(repsWeeks);
                algoInfoList.add(repsIncrease);
                algoInfoList.add(weightWeeks);
                algoInfoList.add(weightIncrease);
                algoInfoList.add(isLooper);

                tempAlgoInfoList.add(setsWeeks);
                tempAlgoInfoList.add(setsIncrease);
                tempAlgoInfoList.add(repsWeeks);
                tempAlgoInfoList.add(repsIncrease);
                tempAlgoInfoList.add(weightWeeks);
                tempAlgoInfoList.add(weightIncrease);
                tempAlgoInfoList.add(isLooper);
                tempAlgoInfoList.add(applyToAllExs);
                tempAlgoInfoList.add(day);

                EditTemplateAssemblerClass.getInstance().tempAlgoInfo.put(exName, tempAlgoInfoList);

                Intent intent = new Intent();
                intent.putExtra("list", algoInfoList);

                setResult(4, intent);

                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });
    }
}
