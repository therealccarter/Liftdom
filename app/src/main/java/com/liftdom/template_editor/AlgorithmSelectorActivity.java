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
    boolean wasApplyToAll;
    boolean wasCleared = false;

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
    @BindView(R.id.unitsView) TextView units;
    @BindView(R.id.percentWeightsWeeksEditText) EditText percentWeeksEditText;
    @BindView(R.id.percentWeightsIncreaseEditText) EditText percentIncreaseEditText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        wasCleared = false;

        if(TemplateEditorSingleton.getInstance().isCurrentUserImperial){
            units.setText("lbs");
        }else{
            units.setText("kgs");
        }

        exName = getIntent().getExtras().getString("exName");
        if(getIntent().getExtras().getString("day") != null){
            day = getIntent().getExtras().getString("day");
            String cat = exName + "\n" + dayFormatter(day);
            exNameDowView.setText(cat);
        }else{
            exNameDowView.setText(exName);
        }

        if(TemplateEditorSingleton.getInstance().isAlgoApplyToAll){
            wasApplyToAll = true;
            if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.isEmpty()){

                List<String> tempList = EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.get("0_key");

                setsWeeksEditText.setText(tempList.get(1));
                setsIncreasedEditText.setText(tempList.get(2));
                repsWeeksEditText.setText(tempList.get(3));
                repsIncreasedEditText.setText(tempList.get(4));
                weightsWeeksEditText.setText(tempList.get(5));
                weightsIncreasedEditText.setText(tempList.get(6));
                percentWeeksEditText.setText(tempList.get(7));
                percentIncreaseEditText.setText(tempList.get(8));
                boolean isLoop = Boolean.valueOf(tempList.get(9));
                if(isLoop){
                    algorithmLooper.setChecked(true);
                }
                applyAlgoToAllExs.setChecked(true);
            }
        }else{
            if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()){
                for(Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()){
                    List<String> tempList = entry.getValue();
                    if(tempList.get(0).equals(exName)){
                        if(tempList.size() < 14){
                            if(Boolean.valueOf(tempList.get(10))){ // apply to all ex instances
                                setsWeeksEditText.setText(tempList.get(1));
                                setsIncreasedEditText.setText(tempList.get(2));
                                repsWeeksEditText.setText(tempList.get(3));
                                repsIncreasedEditText.setText(tempList.get(4));
                                weightsWeeksEditText.setText(tempList.get(5));
                                weightsIncreasedEditText.setText(tempList.get(6));
                                percentWeeksEditText.setText(tempList.get(7));
                                percentIncreaseEditText.setText(tempList.get(8));
                                boolean isLoop = Boolean.valueOf(tempList.get(9));
                                if(isLoop){
                                    algorithmLooper.setChecked(true);
                                }
                                applyAlgoToExs.setChecked(true);
                            }else{
                                if(!day.equals("null")){
                                    if(containsDay(tempList.get(12), day)){
                                        setsWeeksEditText.setText(tempList.get(1));
                                        setsIncreasedEditText.setText(tempList.get(2));
                                        repsWeeksEditText.setText(tempList.get(3));
                                        repsIncreasedEditText.setText(tempList.get(4));
                                        weightsWeeksEditText.setText(tempList.get(5));
                                        weightsIncreasedEditText.setText(tempList.get(6));
                                        percentWeeksEditText.setText(tempList.get(7));
                                        percentIncreaseEditText.setText(tempList.get(8));
                                        boolean isLoop = Boolean.valueOf(tempList.get(9));
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
        }

        applyAlgoToAllExs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    TemplateEditorSingleton.getInstance().isAlgoApplyToAll = true;
                }else{
                    TemplateEditorSingleton.getInstance().isAlgoApplyToAll = false;
                }
            }
        });

        clearButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                wasCleared = true;

                String tag = "null";
                setsWeeksEditText.setText("");
                setsIncreasedEditText.setText("");
                repsWeeksEditText.setText("");
                repsIncreasedEditText.setText("");
                weightsWeeksEditText.setText("");
                weightsIncreasedEditText.setText("");
                percentWeeksEditText.setText("");
                percentIncreaseEditText.setText("");
                algoInfoList.clear();
                tempAlgoInfoList.clear();
                tempAlgoInfoList2.clear();
                algorithmLooper.setChecked(false);
                applyAlgoToExs.setChecked(false);
                applyAlgoToAllExs.setChecked(false);

                if(TemplateEditorSingleton.getInstance().isAlgoApplyToAll){
                    EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.clear();
                }else{
                    if(!EditTemplateAssemblerClass.getInstance().tempAlgoInfo.isEmpty()) {
                        for (Map.Entry<String, List<String>> entry : EditTemplateAssemblerClass.getInstance().tempAlgoInfo.entrySet()) {
                            List<String> tempList = entry.getValue();
                            if (tempList.get(0).equals(exName) && tempList.get(10).equals(day)) {
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
                String percentWeeks = percentWeeksEditText.getText().toString();

                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();
                String percentIncrease = percentIncreaseEditText.getText().toString();

                String isLooper = String.valueOf(algorithmLooper.isChecked());
                String applyToAllExInstance = String.valueOf(applyAlgoToExs.isChecked());
                String applyToAllExs = String.valueOf(applyAlgoToAllExs.isChecked());

                algoInfoList.clear();
                tempAlgoInfoList.clear();
                tempAlgoInfoList2.clear();

                if(setsIncrease.isEmpty() &&
                        repsIncrease.isEmpty() &&
                        weightIncrease.isEmpty() &&
                        percentIncrease.isEmpty()){
                    isEmpty = true;
                    Intent intent = new Intent();
                    intent.putExtra("isEmpty", isEmpty);
                    if(wasCleared){
                        intent.putExtra("wasCleared", "yes");
                    }
                    setResult(5, intent);
                    finish();
                }else{
                    if(TemplateEditorSingleton.getInstance().isAlgoApplyToAll){

                        // this is what is going down to the exlevelfrag
                        algoInfoList.add("all");                    //0
                        algoInfoList.add(setsWeeks);                //1
                        algoInfoList.add(setsIncrease);             //2
                        algoInfoList.add(repsWeeks);                //3
                        algoInfoList.add(repsIncrease);             //4
                        algoInfoList.add(weightWeeks);              //5
                        algoInfoList.add(weightIncrease);           //6
                        algoInfoList.add(percentWeeks);             //7
                        algoInfoList.add(percentIncrease);          //8
                        algoInfoList.add(isLooper);                 //9
                        algoInfoList.add(applyToAllExInstance);     //10
                        algoInfoList.add(applyToAllExs);            //11

                        // this is what's going to the singleton
                        tempAlgoInfoList2.add("all");               //0
                        tempAlgoInfoList2.add(setsWeeks);           //1
                        tempAlgoInfoList2.add(setsIncrease);        //2
                        tempAlgoInfoList2.add(repsWeeks);           //3
                        tempAlgoInfoList2.add(repsIncrease);        //4
                        tempAlgoInfoList2.add(weightWeeks);         //5
                        tempAlgoInfoList2.add(weightIncrease);      //6
                        tempAlgoInfoList2.add(percentWeeks);        //7
                        tempAlgoInfoList2.add(percentIncrease);     //8
                        tempAlgoInfoList2.add(isLooper);            //9
                        tempAlgoInfoList.add(applyToAllExInstance); //10
                        tempAlgoInfoList.add(applyToAllExs);        //11
                        tempAlgoInfoList.add(day);                  //12

                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.clear();
                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo2.put("0_key", tempAlgoInfoList2);

                        Intent intent = new Intent();
                        intent.putExtra("list", algoInfoList);
                        intent.putExtra("applyToAll", "yes");
                        if(wasCleared){
                            intent.putExtra("wasCleared", "yes");
                        }


                        setResult(4, intent);

                        finish();
                    }else{

                        // this is what is going down to the exlevelfrag
                        algoInfoList.add(exName);                   //0
                        algoInfoList.add(setsWeeks);                //1
                        algoInfoList.add(setsIncrease);             //2
                        algoInfoList.add(repsWeeks);                //3
                        algoInfoList.add(repsIncrease);             //4
                        algoInfoList.add(weightWeeks);              //5
                        algoInfoList.add(weightIncrease);           //6
                        algoInfoList.add(percentWeeks);             //7
                        algoInfoList.add(percentIncrease);          //8
                        algoInfoList.add(isLooper);                 //9
                        algoInfoList.add(applyToAllExInstance);     //10
                        algoInfoList.add(applyToAllExs);            //11

                        // this is what's going to the singleton
                        tempAlgoInfoList.add(exName);               //0
                        tempAlgoInfoList.add(setsWeeks);            //1
                        tempAlgoInfoList.add(setsIncrease);         //2
                        tempAlgoInfoList.add(repsWeeks);            //3
                        tempAlgoInfoList.add(repsIncrease);         //4
                        tempAlgoInfoList.add(weightWeeks);          //5
                        tempAlgoInfoList.add(weightIncrease);       //6
                        tempAlgoInfoList.add(percentWeeks);         //7
                        tempAlgoInfoList.add(percentIncrease);      //8
                        tempAlgoInfoList.add(isLooper);             //9
                        tempAlgoInfoList.add(applyToAllExInstance); //10
                        tempAlgoInfoList.add(applyToAllExs);        //11
                        tempAlgoInfoList.add(day);                  //12

                        EditTemplateAssemblerClass.getInstance().tempAlgoInfo.put(mapSize() + "_key",
                                tempAlgoInfoList);

                        Intent intent = new Intent();
                        intent.putExtra("list", algoInfoList);
                        if(wasCleared){
                            intent.putExtra("wasCleared", "yes");
                        }
                        if(wasApplyToAll){
                            intent.putExtra("wasApplyToAll", "yes");
                        }

                        setResult(4, intent);

                        finish();
                    }
                }


            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isEmpty = false;
                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();
                String percentIncrease = percentIncreaseEditText.getText().toString();

                if(setsIncrease.isEmpty() &&
                   repsIncrease.isEmpty() &&
                   weightIncrease.isEmpty() &&
                   percentIncrease.isEmpty()){
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

    private String mapSize(){
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

    private boolean containsDay(String algoDays, String newDays){
        boolean contains = false;

        String delims = "[_]";
        String[] tokens1 = algoDays.split(delims);
        String[] tokens2 = newDays.split(delims);

        for(int i = 0; i < tokens1.length; i++){
            String day1 = tokens1[i];
            for(int j = 0; j < tokens2.length; j++){
                if(day1.equals(tokens2[j])){
                    contains = true;
                }
            }
        }

        return contains;
    }
}
