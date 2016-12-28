package com.liftdom.user_profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import org.w3c.dom.Text;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryRepsWeightFrag extends Fragment {


    public HistoryRepsWeightFrag() {
        // Required empty public constructor
    }

    String reps = "fail";
    String weight = "fail";

    @BindView(R.id.reps) TextView repsEditText;
    @BindView(R.id.weight) TextView weightEditText;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_history_reps_weight, container, false);

        ButterKnife.bind(this, view);

        if(weight.equals("B.W.")){
            weightEditText.setText(weight);
            pounds.setVisibility(View.GONE);
        }else{
            weightEditText.setText(weight);
        }

        repsEditText.setText(reps);


        return view;
    }

}
