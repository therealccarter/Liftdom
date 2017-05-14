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
    List<String> tempAlgoInfoList2 = new ArrayList<>();
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
    @BindView(R.id.applyAlgoToAllExs) CheckBox applyAlgoToAllExs;
    @BindView(R.id.title) TextView titleView;
    @BindView(R.id.exNameAndDowView) TextView exNameDowView;
    @BindView(R.id.clearButton) Button clearButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        exName = getIntent().getExtras().getString("exName");
        if(getIntent().getExtras().getString("day") != null){
            day = getIntent().getExtras().getString("day");
            String cat = exName + "\n" + dayFormatter(day);
            exNameDowView.setText(cat);
        }else{
            exNameDowView.setText(exName);
        }

        if(EditTemplateAssemblerClass.getInstance().isApplyToAll){
            if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.isEmpty()){

                List<String> tempList = EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.get("0");

                setsWeeksEditText.setText(tempList.get(1));
                setsIncreasedEditText.setText(tempList.get(2));
                repsWeeksEditText.setText(tempList.get(3));
                repsIncreasedEditText.setText(tempList.get(4));
                weightsWeeksEditText.setText(tempList.get(5));
                weightsIncreasedEditText.setText(tempList.get(6));
                boolean isLoop = Boolean.valueOf(tempList.get(7));
                if(isLoop){
                    algorithmLooper.setChecked(true);
                }
            }
        }else{
            if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()){
                for(Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()){
                    List<String> tempList = entry.getValue();
                    if(tempList.get(0).equals(exName)){

                        if(Boolean.valueOf(tempList.get(9))){
                            setsWeeksEditText.setText(tempList.get(1));
                            setsIncreasedEditText.setText(tempList.get(2));
                            repsWeeksEditText.setText(tempList.get(3));
                            repsIncreasedEditText.setText(tempList.get(4));
                            weightsWeeksEditText.setText(tempList.get(5));
                            weightsIncreasedEditText.setText(tempList.get(6));
                            boolean isLoop = Boolean.valueOf(tempList.get(7));
                            if(isLoop){
                                algorithmLooper.setChecked(true);
                            }
                            applyAlgoToExs.setChecked(true);
                        }else{
                            if(!day.equals("null")){
                                if(tempList.get(8).equals(day)){
                                    setsWeeksEditText.setText(tempList.get(1));
                                    setsIncreasedEditText.setText(tempList.get(2));
                                    repsWeeksEditText.setText(tempList.get(3));
                                    repsIncreasedEditText.setText(tempList.get(4));
                                    weightsWeeksEditText.setText(tempList.get(5));
                                    weightsIncreasedEditText.setText(tempList.get(6));
                                    boolean isLoop = Boolean.valueOf(tempList.get(7));
                                    if(isLoop){
                                        algorithmLooper.setChecked(true);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        applyAlgoToAllExs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EditTemplateAssemblerClass.getInstance().isApplyToAll = true;
                }else{
                    EditTemplateAssemblerClass.getInstance().isApplyToAll = false;
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String tag = "null";
                setsWeeksEditText.setText("");
                setsIncreasedEditText.setText("");
                repsWeeksEditText.setText("");
                repsIncreasedEditText.setText("");
                weightsWeeksEditText.setText("");
                weightsIncreasedEditText.setText("");
                algoInfoList.clear();
                tempAlgoInfoList.clear();
                tempAlgoInfoList2.clear();
                algorithmLooper.setChecked(false);
                applyAlgoToExs.setChecked(false);
                applyAlgoToAllExs.setChecked(false);

                if(EditTemplateAssemblerClass.getInstance().isApplyToAll){
                    EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.clear();
                }else{
                    if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()) {
                        for (Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()) {
                            List<String> tempList = entry.getValue();
                            if (tempList.get(0).equals(exName) && tempList.get(8).equals(day)) {
                                tag = entry.getKey();
                            }
                        }
                    }

                    if(!tag.equals("null")){
                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo.remove(tag);
                    }
                }


            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isEmpty = false;

                String setsWeeks = setsWeeksEditText.getText().toString();
                String repsWeeks = repsWeeksEditText.getText().toString();
                String weightWeeks = weightsWeeksEditText.getText().toString();

                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();

                String isLooper = String.valueOf(algorithmLooper.isChecked());
                String applyToAllExInstance = String.valueOf(applyAlgoToExs.isChecked());
                String applyToAllExs = String.valueOf(applyAlgoToAllExs.isChecked());

                algoInfoList.clear();
                tempAlgoInfoList.clear();
                tempAlgoInfoList2.clear();

                if(setsIncrease.equals("") &&
                        repsIncrease.equals("") &&
                        weightIncrease.equals("")){
                    isEmpty = true;
                    Intent intent = new Intent();
                    intent.putExtra("isEmpty", isEmpty);
                    setResult(5, intent);
                    finish();
                }else{
                    if(EditTemplateAssemblerClass.getInstance().isApplyToAll){

                        algoInfoList.add(exName);                   //0
                        algoInfoList.add(setsWeeks);                //1
                        algoInfoList.add(setsIncrease);             //2
                        algoInfoList.add(repsWeeks);                //3
                        algoInfoList.add(repsIncrease);             //4
                        algoInfoList.add(weightWeeks);              //5
                        algoInfoList.add(weightIncrease);           //6
                        algoInfoList.add(isLooper);                 //7

                        tempAlgoInfoList2.add(exName);               //0
                        tempAlgoInfoList2.add(setsWeeks);            //1
                        tempAlgoInfoList2.add(setsIncrease);         //2
                        tempAlgoInfoList2.add(repsWeeks);            //3
                        tempAlgoInfoList2.add(repsIncrease);         //4
                        tempAlgoInfoList2.add(weightWeeks);          //5
                        tempAlgoInfoList2.add(weightIncrease);       //6
                        tempAlgoInfoList2.add(isLooper);             //7
                        tempAlgoInfoList2.add(day);                  //8
                        tempAlgoInfoList2.add(applyToAllExInstance); //9
                        tempAlgoInfoList2.add(applyToAllExs);        //10

                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.clear();
                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.put("0", tempAlgoInfoList2);

                        Intent intent = new Intent();
                        intent.putExtra("list", algoInfoList);

                        setResult(4, intent);

                        finish();
                    } else{

                        algoInfoList.add(exName);                   //0
                        algoInfoList.add(setsWeeks);                //1
                        algoInfoList.add(setsIncrease);             //2
                        algoInfoList.add(repsWeeks);                //3
                        algoInfoList.add(repsIncrease);             //4
                        algoInfoList.add(weightWeeks);              //5
                        algoInfoList.add(weightIncrease);           //6
                        algoInfoList.add(isLooper);                 //7

                        tempAlgoInfoList.add(exName);               //0
                        tempAlgoInfoList.add(setsWeeks);            //1
                        tempAlgoInfoList.add(setsIncrease);         //2
                        tempAlgoInfoList.add(repsWeeks);            //3
                        tempAlgoInfoList.add(repsIncrease);         //4
                        tempAlgoInfoList.add(weightWeeks);          //5
                        tempAlgoInfoList.add(weightIncrease);       //6
                        tempAlgoInfoList.add(isLooper);             //7
                        tempAlgoInfoList.add(day);                  //8
                        tempAlgoInfoList.add(applyToAllExInstance); //9
                        tempAlgoInfoList.add(applyToAllExs);        //10


                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo.put(stringSize(), tempAlgoInfoList);

                        Intent intent = new Intent();
                        intent.putExtra("list", algoInfoList);

                        setResult(4, intent);

                        finish();
                    }
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isEmpty = false;

                String setsWeeks = setsWeeksEditText.getText().toString();
                String repsWeeks = repsWeeksEditText.getText().toString();
                String weightWeeks = weightsWeeksEditText.getText().toString();

                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();

                String isLooper = String.valueOf(algorithmLooper.isChecked());
                String applyToAllExInstance = String.valueOf(applyAlgoToExs.isChecked());
                String applyToAllExs = String.valueOf(applyAlgoToAllExs.isChecked());

                if(setsIncrease.isEmpty() &&
                   repsIncrease.isEmpty() &&
                   weightIncrease.isEmpty()){
                    isEmpty = true;
                    Intent intent = new Intent();
                    intent.putExtra("isEmpty", isEmpty);
                    setResult(5, intent);
                    finish();
                }else{
                    finish();
                }


            }
        });
    }

    private String stringSize(){
        int intSize = EditTemplateAssemblerClass.getInstance().tempAlgoInfo.size();
        intSize++;

        String stringVersion = String.valueOf(intSize);
        return stringVersion;
    }

    private String dayFormatter(String dayUn){
        String formatted = "/";
        String delims = "[_]";
        String[] tokens = dayUn.split(delims);
        for(String string : tokens){
            formatted = formatted + string + "/";
        }
        return formatted;
    }
}
