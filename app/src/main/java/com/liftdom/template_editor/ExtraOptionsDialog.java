package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExtraOptionsDialog extends AppCompatActivity {

    @BindView(R.id.confirmButton) Button confirm;
    @BindView(R.id.weightRadioGroup) RadioGroup weightRadioGroup;
    @BindView(R.id.repsRadioGroup) RadioGroup repsRadioGroup;
    @BindView(R.id.numericalWeightRadioButton) RadioButton numericalWeightRadioButton;
    @BindView(R.id.bodyWeightRadioButton) RadioButton bodyWeightRadioButton;
    @BindView(R.id.percentageRadioButton) RadioButton percentageRadioButton;
    @BindView(R.id.numericalRepsRadioButton) RadioButton numericalRepsRadioButton;
    @BindView(R.id.toFailureRadioButton) RadioButton toFailureRadioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_options);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        String repsText = getIntent().getExtras().getString("repsText");
        String weightText = getIntent().getExtras().getString("weightText");

        if(!isNumber(weightText)){ // if weight is text
            bodyWeightRadioButton.setChecked(true);
        }else{
            numericalWeightRadioButton.setChecked(true);
        }

        if(!isNumber(repsText)){ // if reps are text
            toFailureRadioButton.setChecked(true);
        }else{
            numericalRepsRadioButton.setChecked(true);
        }

        /**
         * But how can we ensure that using someone else's percentage-based template works if
         * we're using arbitrary-based percentages? Some sort of notification when first receiving a template with
         * arbitrary percentages. Just would need a method in the template editor model class. They could then just
         * go in and edit that.
         */

        /**
         * So what we'll do is create a string that gives us the required data.
         * For example, p_85_a_365 which means (percentage flag) 85% of arbitrary number 365 (lbs or kgs depending on
         * template imperial status).
         * We could also choose from current maxes, and that'd be p_85_Bench Press (Barbell - Flat)
         *
         * We need to have an option to set this through the parent exercise as well as through the sets level child
         */

        percentageRadioButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){

                }
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
            Intent intent = new Intent();

            if(weightRadioGroup.getCheckedRadioButtonId() == numericalWeightRadioButton.getId()){
                String message = "defaultWeight";
                intent.putExtra("MESSAGE1", message);
            }else if(weightRadioGroup.getCheckedRadioButtonId() == percentageRadioButton.getId()){

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
