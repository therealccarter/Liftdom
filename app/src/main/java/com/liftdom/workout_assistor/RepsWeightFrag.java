package com.liftdom.workout_assistor;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightFrag extends Fragment {


    public RepsWeightFrag() {
        // Required empty public constructor
    }

    int setNumber;

    //TODO: Delete this bitch

    public String reps = "fail";
    public String weight = "fail";
    public String parentExercise = "failed";
    public String fullString = "failed";
    public boolean isFromCalendar = false;
    public boolean isCheckbox = false;

    @BindView(R.id.repsEditText) EditText repsEditText;
    @BindView(R.id.weightEditText) EditText weightEditText;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.main_layout) LinearLayout mainLayout;
    @BindView(R.id.pounds) TextView pounds;

    Bundle mSaved;

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_reps_weight, container, false);

        ButterKnife.bind(this, view);

        mSaved = savedInstanceState;

        repsEditText.setText(reps);
        if(weight.equals("B.W.")){
            weightEditText.setText(weight);
            weightEditText.setEnabled(false);
            pounds.setVisibility(View.GONE);
            weightEditText.setTextColor(Color.parseColor("#000000"));
        }else{
            weightEditText.setText(weight);
        }

        if(isFromCalendar){
            checkBox.setVisibility(View.GONE);
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    mainLayout.setBackgroundColor(Color.parseColor("#EBEBEB"));
                } else{
                    mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        if(isCheckbox){
            checkBox.setChecked(true);
        }

        return view;
    }

    @Override
    public void onPause(){
        super.onPause();

        if(mSaved == null && !isFromCalendar) {
            CheckBox checkBox = (CheckBox) getView().findViewById(R.id.checkBox);
            EditText repsEditText = (EditText) getView().findViewById(R.id.repsEditText);
            EditText weightEditText = (EditText) getView().findViewById(R.id.weightEditText);

            if (checkBox.isChecked()) {
                String compiledString = repsEditText.getText() + "@" + weightEditText.getText();
                WorkoutAssistorAssemblerClass.getInstance().setRepsWeight(parentExercise, compiledString);
            }
        }
    }

    public String getCheckedStatus(){
        boolean isChecked;

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);
        isChecked = checkBox.isChecked();

        String toString = String.valueOf(isChecked);

        return toString;
    }

    public String getParentExercise(){
        return parentExercise;
    }

}
