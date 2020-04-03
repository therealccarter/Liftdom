package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
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
    @BindView(R.id.amrap) RadioButton amrapRadioButton;
    @BindView(R.id.noButton) Button noButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_options);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        String repsText = getIntent().getExtras().getString("repsText");
        String weightText = getIntent().getExtras().getString("weightText");
        String isPercentageString = getIntent().getExtras().getString("isPercentageString");
        String isAmrapString = getIntent().getExtras().getString("isAmrap");

        if(isPercentageString.equals("true")){
            percentageRadioButton.setChecked(true);
        }else if(isPercentageString.equals("dontShow")){
            percentageRadioButton.setVisibility(View.GONE);
            if(!isNumber(weightText)){ // if weight is text
                bodyWeightRadioButton.setChecked(true);
            }else{
                numericalWeightRadioButton.setChecked(true);
            }
        }else{
            if(!isNumber(weightText)){ // if weight is text
                bodyWeightRadioButton.setChecked(true);
            }else{
                numericalWeightRadioButton.setChecked(true);
            }
        }

        if(isAmrapString.equals("true")){
            amrapRadioButton.setChecked(true);
        }else{
            if(!isNumber(repsText)){ // if reps are text
                toFailureRadioButton.setChecked(true);
            }else{
                numericalRepsRadioButton.setChecked(true);
            }
        }

        noButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        /**
         * But how can we ensure that using someone else's percentage-based template works if
         * we're using arbitrary-based percentages? Some sort of notification when first receiving a template with
         * arbitrary percentages. Just would need a method in the template editor model class. They could then just
         * go in and edit that. But what if it's a current max and they don't have it? We'd have to notify that as well.
         */

        /**
         * So what we'll do is create a string that gives us the required data on getInfo
         * For example, p_85_a_345 which means (percentage flag) 85% of arbitrary number 345 (lbs or kgs depending on
         * template imperial status).
         * We could also choose from current maxes, and that'd be p_85_Bench Press (Barbell - Flat)
         *
         * We need to have an option to set this through the parent exercise as well as through the sets level child
         */

        /**
         * How about a different approach. What if we just, on choosing %, change the scheme to (S x R @ _ % of []) ?
         * And then when you click on the box on the right it opens up a thing that shows the options??
         * And then you can do the same thing at the exercise level to allow for changes of all set schemes.
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
                String message = "percentage";
                intent.putExtra("MESSAGE1", message);
            }else{
                String message = "bodyweight";
                intent.putExtra("MESSAGE1", message);
            }

            if(repsRadioGroup.getCheckedRadioButtonId() == numericalRepsRadioButton.getId()){
                String message = "defaultReps";
                intent.putExtra("MESSAGE2", message);
            }else if(repsRadioGroup.getCheckedRadioButtonId() == amrapRadioButton.getId()){
                String message = "amrap";
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
