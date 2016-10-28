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

    String reps = "fail";
    String weight = "fail";

    @BindView(R.id.repsEditText) EditText repsEditText;
    @BindView(R.id.weightEditText) EditText weightEditText;
    @BindView(R.id.checkBox) CheckBox checkBox;
    @BindView(R.id.main_layout) LinearLayout mainLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight, container, false);

        ButterKnife.bind(this, view);

        repsEditText.setText(reps);
        weightEditText.setText(weight);

        checkBox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(checkBox.isChecked()){
                    mainLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                } else{
                    mainLayout.setBackgroundColor(Color.parseColor("#EBEBEB"));
                }
            }
        });

        return view;
    }

}
