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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.*;
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
    @BindView(R.id.textView1) TextView textView1;
    @BindView(R.id.textView2) TextView textView2;
    @BindView(R.id.textView3) TextView textView3;
    @BindView(R.id.textView4) TextView textView4;
    @BindView(R.id.textView5) TextView textView5;
    @BindView(R.id.textView6) TextView textView6;
    @BindView(R.id.textView7) TextView textView7;
    @BindView(R.id.textView8) TextView textView8;
    @BindView(R.id.textView9) TextView textView9;
    @BindView(R.id.textView10) TextView textView10;


    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    // declare_auth
    private FirebaseAuth mAuth;
    private FirebaseUser mFirebaseUser;


    private FirebaseAuth.AuthStateListener mAuthListener;

    String templateName = "fail";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        setTitle("Algorithm:");
        this.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title_1);
        this.setFinishOnTouchOutside(false);

        ButterKnife.bind(this);

        String[] algoDataArray = new String[7];

        algoDataArray = EditTemplateAssemblerClass.getInstance().algorithmDataList;

        for(int i = 0; i < 7; i++){
            String value = algoDataArray[i];
            if(EditTemplateAssemblerClass.getInstance().isEditAndFirstTime){
                applyAlgo.setChecked(true);

                EditTemplateAssemblerClass.getInstance().isEditAndFirstTime = false;
            }

            if(i == 0){
                setsWeeksEditText.setText(value);
            } else if(i == 1){
                setsIncreasedEditText.setText(value);
            } else if(i == 2){
                repsWeeksEditText.setText(value);
            } else if(i == 3){
                repsIncreasedEditText.setText(value);
            } else if(i == 4){
                weightsWeeksEditText.setText(value);
            } else if(i == 5){
                weightsIncreasedEditText.setText(value);
            }
        }


        if(EditTemplateAssemblerClass.getInstance().isAlgoFirstTime && EditTemplateAssemblerClass.getInstance().isAlgoLooper){
            algorithmLooper.setChecked(true);
        }

        if(EditTemplateAssemblerClass.getInstance().isApplyAlgo && !EditTemplateAssemblerClass.getInstance()
                .isAlgoFirstTime){
            setsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            setsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
            repsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            repsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
            weightsWeeksEditText.setTextColor(Color.parseColor("#000000"));
            weightsIncreasedEditText.setTextColor(Color.parseColor("#000000"));

            applyAlgo.setChecked(true);

            if(EditTemplateAssemblerClass.getInstance().isAlgoLooper){
                algorithmLooper.setChecked(true);
            }

            textView1.setTextColor(Color.parseColor("#000000"));
            textView2.setTextColor(Color.parseColor("#000000"));
            textView3.setTextColor(Color.parseColor("#000000"));
            textView4.setTextColor(Color.parseColor("#000000"));
            textView5.setTextColor(Color.parseColor("#000000"));
            textView6.setTextColor(Color.parseColor("#000000"));
            textView7.setTextColor(Color.parseColor("#000000"));
            textView8.setTextColor(Color.parseColor("#000000"));
            textView9.setTextColor(Color.parseColor("#000000"));
            textView10.setTextColor(Color.parseColor("#000000"));

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
            algorithmLooper.setChecked(true);
            //applyAlgo.setChecked(false);

            textView1.setTextColor(Color.parseColor("#E8E8E8"));
            textView2.setTextColor(Color.parseColor("#E8E8E8"));
            textView3.setTextColor(Color.parseColor("#E8E8E8"));
            textView4.setTextColor(Color.parseColor("#E8E8E8"));
            textView5.setTextColor(Color.parseColor("#E8E8E8"));
            textView6.setTextColor(Color.parseColor("#E8E8E8"));
            textView7.setTextColor(Color.parseColor("#E8E8E8"));
            textView8.setTextColor(Color.parseColor("#E8E8E8"));
            textView9.setTextColor(Color.parseColor("#E8E8E8"));
            textView10.setTextColor(Color.parseColor("#E8E8E8"));

            setsWeeksEditText.setEnabled(false);
            setsIncreasedEditText.setEnabled(false);
            repsWeeksEditText.setEnabled(false);
            repsIncreasedEditText.setEnabled(false);
            weightsWeeksEditText.setEnabled(false);
            weightsIncreasedEditText.setEnabled(false);
        }

        if(EditTemplateAssemblerClass.getInstance().isAlgoLooper && !EditTemplateAssemblerClass.getInstance()
                .isAlgoFirstTime){
            algorithmLooper.setChecked(true);
            algorithmLooper.setTextColor(Color.parseColor("#000000"));
        }

        applyAlgo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EditTemplateAssemblerClass.getInstance().isApplyAlgo = true;

                    EditTemplateAssemblerClass.getInstance().isAlgoFirstTime = false;


                    setsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    setsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
                    repsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    repsIncreasedEditText.setTextColor(Color.parseColor("#000000"));
                    weightsWeeksEditText.setTextColor(Color.parseColor("#000000"));
                    weightsIncreasedEditText.setTextColor(Color.parseColor("#000000"));

                    textView1.setTextColor(Color.parseColor("#000000"));
                    textView2.setTextColor(Color.parseColor("#000000"));
                    textView3.setTextColor(Color.parseColor("#000000"));
                    textView4.setTextColor(Color.parseColor("#000000"));
                    textView5.setTextColor(Color.parseColor("#000000"));
                    textView6.setTextColor(Color.parseColor("#000000"));
                    textView7.setTextColor(Color.parseColor("#000000"));
                    textView8.setTextColor(Color.parseColor("#000000"));
                    textView9.setTextColor(Color.parseColor("#000000"));
                    textView10.setTextColor(Color.parseColor("#000000"));

                    algorithmLooper.setTextColor(Color.parseColor("#000000"));

                    setsWeeksEditText.setEnabled(true);
                    setsIncreasedEditText.setEnabled(true);
                    repsWeeksEditText.setEnabled(true);
                    repsIncreasedEditText.setEnabled(true);
                    weightsWeeksEditText.setEnabled(true);
                    weightsIncreasedEditText.setEnabled(true);

                    CharSequence toastText = "(+) Algorithm Added";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                    toast.show();
                }else{
                    EditTemplateAssemblerClass.getInstance().isApplyAlgo = false;

                    setsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    setsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    repsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    repsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    weightsWeeksEditText.setTextColor(Color.parseColor("#E8E8E8"));
                    weightsIncreasedEditText.setTextColor(Color.parseColor("#E8E8E8"));

                    textView1.setTextColor(Color.parseColor("#E8E8E8"));
                    textView2.setTextColor(Color.parseColor("#E8E8E8"));
                    textView3.setTextColor(Color.parseColor("#E8E8E8"));
                    textView4.setTextColor(Color.parseColor("#E8E8E8"));
                    textView5.setTextColor(Color.parseColor("#E8E8E8"));
                    textView6.setTextColor(Color.parseColor("#E8E8E8"));
                    textView7.setTextColor(Color.parseColor("#E8E8E8"));
                    textView8.setTextColor(Color.parseColor("#E8E8E8"));
                    textView9.setTextColor(Color.parseColor("#E8E8E8"));
                    textView10.setTextColor(Color.parseColor("#E8E8E8"));

                    algorithmLooper.setTextColor(Color.parseColor("#E8E8E8"));

                    setsWeeksEditText.setEnabled(false);
                    setsIncreasedEditText.setEnabled(false);
                    repsWeeksEditText.setEnabled(false);
                    repsIncreasedEditText.setEnabled(false);
                    weightsWeeksEditText.setEnabled(false);
                    weightsIncreasedEditText.setEnabled(false);

                    CharSequence toastText = "(-) Algorithm Removed";
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(getApplicationContext(), toastText, duration);
                    toast.show();
                }
            }
        });

        algorithmLooper.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    EditTemplateAssemblerClass.getInstance().isAlgoLooper = true;
                    algorithmLooper.setTextColor(Color.parseColor("#000000"));
                }else{
                    EditTemplateAssemblerClass.getInstance().isAlgoLooper = false;
                    algorithmLooper.setTextColor(Color.parseColor("#E8E8E8"));
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

                EditTemplateAssemblerClass.getInstance().isAlgoFirstTime = false;

                Intent intent = new Intent();
                setResult(4, intent);
                finish();

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent();
                setResult(1, intent);

                EditTemplateAssemblerClass.getInstance().isAlgoFirstTime = false;

                finish();
            }
        });


        if(EditTemplateAssemblerClass.getInstance().isEditAndFirstTime){

        }



        }


    @Override
    public void onPause(){
        super.onPause();
        EditTemplateAssemblerClass.getInstance().isAlgoFirstTime = false;
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }

    @Override
    public void onBackPressed(){
        EditTemplateAssemblerClass.getInstance().isAlgoFirstTime = false;
        Intent intent = new Intent();
        setResult(1, intent);
        finish();
    }
}
