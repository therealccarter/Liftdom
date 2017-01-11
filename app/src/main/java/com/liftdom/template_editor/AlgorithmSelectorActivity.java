package com.liftdom.template_editor;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;

public class AlgorithmSelectorActivity extends AppCompatActivity {


    @BindView(R.id.algorithmLayout) LinearLayout algorithmLayout;
    @BindView(R.id.setsWeeksEditText) EditText setsWeeksEditText;
    @BindView(R.id.setsIncreaseEditText) EditText setsIncreasedEditText;
    @BindView(R.id.repsWeeksEditText) EditText repsWeeksEditText;
    @BindView(R.id.repsIncreaseEditText) EditText repsIncreasedEditText;
    @BindView(R.id.weightsWeeksEditText) EditText weightsWeeksEditText;
    @BindView(R.id.weightsIncreaseEditText) EditText  weightsIncreasedEditText;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.saveButton) Button saveButton;
    @BindView(R.id.algorithmLooper) CheckBox algorithmLooper;
    @BindView(R.id.applyAlgo) CheckBox applyAlgo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        setTitle("Algorithm:");
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        if(EditTemplateAssemblerClass.getInstance().isApplyAlgo){
            setsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            setsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
            repsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            repsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
            weightsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            weightsIncreasedEditText.setTextColor(Color.parseColor("#000000"));

            setsWeeksEditText.setEnabled(true);
            setsIncreasedEditText.setEnabled(true);
            repsWeeksEditText.setEnabled(true);
            repsIncreasedEditText.setEnabled(true);
            weightsWeeksEditText.setEnabled(true);
            weightsIncreasedEditText.setEnabled(true);
        }else{

            setsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
            setsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
            repsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
            repsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
            weightsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
            weightsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));


            setsWeeksEditText.setEnabled(false);
            setsIncreasedEditText.setEnabled(false);
            repsWeeksEditText.setEnabled(false);
            repsIncreasedEditText.setEnabled(false);
            weightsWeeksEditText.setEnabled(false);
            weightsIncreasedEditText.setEnabled(false);
        }

        if(EditTemplateAssemblerClass.getInstance().isAlgoLooper){
            algorithmLooper.setChecked(true);
        }else{
            algorithmLooper.setChecked(false);
        }

        applyAlgo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EditTemplateAssemblerClass.getInstance().isApplyAlgo = true;


                    setsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    setsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
                    repsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    repsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
                    weightsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    weightsIncreasedEditText.setTextColor(Color.parseColor("#000000"));

                    setsWeeksEditText.setEnabled(true);
                    setsIncreasedEditText.setEnabled(true);
                    repsWeeksEditText.setEnabled(true);
                    repsIncreasedEditText.setEnabled(true);
                    weightsWeeksEditText.setEnabled(true);
                    weightsIncreasedEditText.setEnabled(true);
                }else{
                    EditTemplateAssemblerClass.getInstance().isApplyAlgo = false;



                    setsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    setsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    repsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    repsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    weightsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    weightsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));


                    setsWeeksEditText.setEnabled(false);
                    setsIncreasedEditText.setEnabled(false);
                    repsWeeksEditText.setEnabled(false);
                    repsIncreasedEditText.setEnabled(false);
                    weightsWeeksEditText.setEnabled(false);
                    weightsIncreasedEditText.setEnabled(false);

                }
            }
        });

        algorithmLooper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EditTemplateAssemblerClass.getInstance().isAlgoLooper = true;
                }else{
                    EditTemplateAssemblerClass.getInstance().isAlgoLooper = false;
                }
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(applyAlgo.isChecked()){
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[0] = setsWeeksEditText.getText()
                            .toString();
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[1] = setsIncreasedEditText.getText()
                            .toString();
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[2] = repsWeeksEditText.getText()
                            .toString();
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[3] = repsIncreasedEditText.getText()
                            .toString();
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[4] = weightsWeeksEditText.getText()
                            .toString();
                    EditTemplateAssemblerClass.getInstance().algorithmDataList[5] = weightsIncreasedEditText.getText()
                            .toString();
                    if(algorithmLooper.isChecked()){
                        EditTemplateAssemblerClass.getInstance().algorithmDataList[6] = "loop";
                    }else{
                        EditTemplateAssemblerClass.getInstance().algorithmDataList[6] = "no loop";
                    }
                }

                Intent intent = new Intent();
                setResult(4, intent);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                setResult(1, intent);
                finish();
            }
        });

    }

    @Override
    public void onPause(){
        super.onPause();
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
