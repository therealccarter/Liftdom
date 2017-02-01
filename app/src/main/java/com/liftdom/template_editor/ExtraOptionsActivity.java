package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExtraOptionsActivity extends AppCompatActivity {

    @BindView(R.id.confirmButton) Button confirm;
    //@BindView(R.id.defaultRepsOptionButton) LinearLayout defaultRepsOptionButton;
    //@BindView(R.id.toFailureOptionButton) LinearLayout toFailureOptionButton;
    //@BindView(R.id.defaultWeightOptionButton) LinearLayout defaultWeightOptionButton;
    //@BindView(R.id.bodyWeightOptionButton) LinearLayout bodyWeightOptionButton;
    @BindView(R.id.weightRadioGroup) RadioGroup weightRadioGroup;
    @BindView(R.id.repsRadioGroup) RadioGroup repsRadioGroup;
    @BindView(R.id.numericalWeightRadioButton) RadioButton numericalWeightRadioButton;
    @BindView(R.id.bodyWeightRadioButton) RadioButton bodyWeightRadioButton;
    @BindView(R.id.numericalRepsRadioButton) RadioButton numericalRepsRadioButton;
    @BindView(R.id.toFailureRadioButton) RadioButton toFailureRadioButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle("Options:");
        setContentView(R.layout.activity_extra_options);
        //this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        /**
         * So what are we trying to do here?...
         * if weight is a number, show bodyweight option and hide default weight
         * if reps is a number, show to failure option and hide default reps
         * else if weight is text, show default weight option
         * else if reps is text, show default reps option
         */



        String repsText = getIntent().getExtras().getString("repsText");
        String weightText = getIntent().getExtras().getString("weightText");

        if(!isNumber(weightText)){ // if weight is text
            bodyWeightRadioButton.setChecked(true);
        }else{
            numericalWeightRadioButton.setChecked(true);
        }

        if(!isNumber(repsText)){ // if reps is text
            toFailureRadioButton.setChecked(true);
        }else{
            numericalRepsRadioButton.setChecked(true);
        }

        //repsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //        if(checkedId == numericalRepsRadioButton.getId()){
        //            isNumericalReps = true;
        //        }else if(checkedId == toFailureRadioButton.getId()){
        //            isNumericalReps = false;
        //        }
        //    }
        //});
//
        //weightRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
        //    @Override
        //    public void onCheckedChanged(RadioGroup group, int checkedId) {
        //        if(checkedId == numericalWeightRadioButton.getId()){
        //            isNumericalWeight = true;
        //        }else if(checkedId == bodyWeightRadioButton.getId()){
        //            isNumericalWeight = false;
        //        }
        //    }
        //});
//
        //defaultRepsOptionButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        String message = "defaultReps";
        //        Intent intent = new Intent();
        //        intent.putExtra("MESSAGE", message);
        //        setResult(3, intent);
        //        finish();
        //    }
        //});
//
        //toFailureOptionButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        String message = "to failure";
        //        Intent intent = new Intent();
        //        intent.putExtra("MESSAGE", message);
        //        setResult(3, intent);
        //        finish();
        //    }
        //});
//
        //defaultWeightOptionButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        String message = "defaultWeight";
        //        Intent intent = new Intent();
        //        intent.putExtra("MESSAGE", message);
        //        setResult(3, intent);
        //        finish();
        //    }
        //});
//
        //bodyWeightOptionButton.setOnClickListener(new View.OnClickListener() {
        //    public void onClick(View v) {
        //        String message = "bodyweight";
        //        Intent intent = new Intent();
        //        intent.putExtra("MESSAGE", message);
        //        setResult(3, intent);
        //        finish();
        //    }
        //});

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();

                if(weightRadioGroup.getCheckedRadioButtonId() == numericalWeightRadioButton.getId()){
                    String message = "defaultWeight";
                    intent.putExtra("MESSAGE1", message);
                }else{
                    String message = "bodyweight";
                    intent.putExtra("MESSAGE1", message);
                }

                if(repsRadioGroup.getCheckedRadioButtonId() == numericalRepsRadioButton.getId()){
                    String message = "defaultReps";
                    intent.putExtra("MESSAGE2", message);
                }else{
                    String message = "to failure";
                    intent.putExtra("MESSAGE2", message);
                }

                setResult(3, intent);
                finish();

            }
        });

    }

    boolean isNumber(String input){
        boolean isNumber = false;

        String inputWithout = input.replaceAll("\\s+","");

        if(inputWithout.equals("")){
            isNumber = true;
        } else{
            try {
                int num = Integer.parseInt(input);
                Log.i("",num+" is a number");
                isNumber = true;
            } catch (NumberFormatException e) {
                isNumber = false;
            }
        }

        return isNumber;
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