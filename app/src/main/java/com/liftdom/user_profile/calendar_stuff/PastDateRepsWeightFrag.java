package com.liftdom.user_profile.calendar_stuff;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class PastDateRepsWeightFrag extends Fragment {

    String repsWeight = "null";


    public PastDateRepsWeightFrag() {
        // Required empty public constructor
    }

    @BindView(R.id.repsWeightTextView) TextView repsWeightTextView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_past_date_reps_weight, container, false);

        ButterKnife.bind(this, view);

        String delims = "[@]";

        String[] tokens = repsWeight.split(delims);



        //TODO: kg if kg
        try{

            String formattedText;

            if(tokens[1].equals("B.W.")){
                 formattedText  = tokens[0] + " reps @ " + tokens[1];
            }else {
                formattedText  = tokens[0] + " reps @ " + tokens[1] + " lbs";
            }
            repsWeightTextView.setText(formattedText);

        } catch (IndexOutOfBoundsException e){
            repsWeightTextView.setText(repsWeight);
        }




        return view;
    }

}
