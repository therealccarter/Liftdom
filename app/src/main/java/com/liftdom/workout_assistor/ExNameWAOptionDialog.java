package com.liftdom.workout_assistor;

import android.content.Intent;
import android.graphics.Typeface;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.helper_classes.DigitsInputFilter;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.InputFilterMinMax;

public class ExNameWAOptionDialog extends AppCompatActivity {

    private String exName;
    private boolean isImperialPOV;
    private String weight;
    private String reps;
    private String exID;

    @BindView(R.id.exName) TextView exNameTextView;
    @BindView(R.id.units) TextView unitsTextView;
    @BindView(R.id.cancelButton) Button cancelButton;
    @BindView(R.id.confirmButton) Button confirmButton;
    @BindView(R.id.weightEditText) EditText weightEditText;
    @BindView(R.id.repsEditText) EditText repsEditText;
    @BindView(R.id.weightTextView) TextView weightTextView;
    @BindView(R.id.weightLL) LinearLayout weightLL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ex_name_wa_options_dialog);
        this.setFinishOnTouchOutside(true);

        ButterKnife.bind(this);

        if(getIntent().getExtras() == null){
            finish();
        }else{
            exName = getIntent().getStringExtra("exercise");
            exNameTextView.setTypeface(null, Typeface.BOLD);
            exNameTextView.setText(exName);
            exID = getIntent().getStringExtra("exID");
            isImperialPOV = getIntent().getBooleanExtra("isImperialPOV", true);
            if(isBodyweight(exName)){
                weightTextView.setVisibility(View.GONE);
                weightLL.setVisibility(View.GONE);
                weightEditText.setText("");
            }else{
                if(isImperialPOV){
                    unitsTextView.setText("lbs");
                    weightEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    weightEditText.setFilters(new InputFilter[]{new InputFilterMinMax(1, 1000)});
                }else{
                    unitsTextView.setText("kgs");
                    weightEditText.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                    weightEditText.setFilters(new InputFilter[]{new DigitsInputFilter(4, 2, 500)});
                }
            }
        }

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();

                weight = weightEditText.getText().toString();
                reps = repsEditText.getText().toString();

                intent.putExtra("weight", weight);
                intent.putExtra("reps", reps);
                intent.putExtra("fragTag", exID);
                intent.putExtra("exName", exName);

                setResult(1, intent);
                finish();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private boolean isBodyweight(String exName){
        boolean isBW = false;

        String delims = "[ ]";
        String[] tokens = exName.split(delims);
        for(String string : tokens){
            if(string.equals("(Bodyweight)")){
                isBW = true;
            }
        }

        return isBW;
    }
}