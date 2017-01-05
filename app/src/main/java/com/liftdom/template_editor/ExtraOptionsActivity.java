package com.liftdom.template_editor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

public class ExtraOptionsActivity extends AppCompatActivity {

    @BindView(R.id.cancelButton1) Button cancelButton;
    @BindView(R.id.defaultRepsOptionButton) LinearLayout defaultRepsOptionButton;
    @BindView(R.id.toFailureOptionButton) LinearLayout toFailureOptionButton;
    @BindView(R.id.defaultWeightOptionButton) LinearLayout defaultWeightOptionButton;
    @BindView(R.id.bodyWeightOptionButton) LinearLayout bodyWeightOptionButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Options:");
        setContentView(R.layout.activity_extra_options);
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
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

        if(isNumber(weightText)){ // if weight is text
            bodyWeightOptionButton.setVisibility(View.VISIBLE);
        }else{
            defaultWeightOptionButton.setVisibility(View.VISIBLE);
        }

        if(isNumber(repsText)){ // if reps is text
            toFailureOptionButton.setVisibility(View.VISIBLE);
        }else{
            defaultRepsOptionButton.setVisibility(View.VISIBLE);
        }

        defaultRepsOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "defaultReps";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        toFailureOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "to failure";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        defaultWeightOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "defaultWeight";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        bodyWeightOptionButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String message = "bodyweight";
                Intent intent = new Intent();
                intent.putExtra("MESSAGE", message);
                setResult(3, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent();
                setResult(1, intent);
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
