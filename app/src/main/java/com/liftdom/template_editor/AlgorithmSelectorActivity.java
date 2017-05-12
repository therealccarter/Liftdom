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

public class AlgorithmSelectorActivity extends AppCompatActivity {

    ArrayList<String> algoInfoList = new ArrayList<>();
    boolean isLoop = false;

    @BindView(R.id.setsWeeksEditText) EditText setsWeeksEditText;
    @BindView(R.id.setsIncreaseEditText) EditText setsIncreasedEditText;
    @BindView(R.id.repsWeeksEditText) EditText repsWeeksEditText;
    @BindView(R.id.repsIncreaseEditText) EditText repsIncreasedEditText;
    @BindView(R.id.weightsWeeksEditText) EditText weightsWeeksEditText;
    @BindView(R.id.weightsIncreaseEditText) EditText  weightsIncreasedEditText;
    @BindView(R.id.cancelButton) ImageButton cancelButton;
    @BindView(R.id.confirmButton) ImageButton confirmButton;
    @BindView(R.id.algorithmLooper) CheckBox algorithmLooper;
    @BindView(R.id.title) TextView titleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_algorithm_selector);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        ButterKnife.bind(this);

        String exName = getIntent().getExtras().getString("exName");

        String concat = "Set Algorithm For: " + exName;
        titleView.setText(concat);



        confirmButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String setsWeeks = setsWeeksEditText.getText().toString();
                String repsWeeks = repsWeeksEditText.getText().toString();
                String weightWeeks = weightsWeeksEditText.getText().toString();

                String setsIncrease = setsIncreasedEditText.getText().toString();
                String repsIncrease = repsIncreasedEditText.getText().toString();
                String weightIncrease = weightsIncreasedEditText.getText().toString();

                String isLooper = String.valueOf(algorithmLooper.isChecked());

                algoInfoList.add(setsWeeks);
                algoInfoList.add(repsWeeks);
                algoInfoList.add(weightWeeks);
                algoInfoList.add(setsIncrease);
                algoInfoList.add(repsIncrease);
                algoInfoList.add(weightIncrease);
                algoInfoList.add(isLooper);

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
