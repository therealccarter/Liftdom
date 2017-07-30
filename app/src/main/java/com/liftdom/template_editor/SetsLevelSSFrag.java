package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelSSFrag extends android.app.Fragment {


    public SetsLevelSSFrag() {
        // Required empty public constructor
    }

    boolean isEdit;
    String isEditSetScheme;

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView pounds;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_s, container, false);

        ButterKnife.bind(this, view);

        if(isEdit){
            if(isEditSetScheme.equals("")){
                setsEditText.setText("");
                repsEditText.setText("");
                weightEditText.setText("");
            }else{
                String delims = "[x,@]";
                String[] tokens = isEditSetScheme.split(delims);
                setsEditText.setText(tokens[0]);
                repsEditText.setText(tokens[1]);
                weightEditText.setText(tokens[2]);
            }
        }

        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsDialog.class);
                String weightText = weightEditText.getText().toString();
                String repsText = repsEditText.getText().toString();
                intent.putExtra("repsText", repsText);
                intent.putExtra("weightText", weightText);
                startActivityForResult(intent, 3);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(data != null){
            if(requestCode == 3){
                if(data.getStringExtra("MESSAGE1") != null && data != null ) {
                    String message = data.getStringExtra("MESSAGE1");
                    if(message.equals("bodyweight")){
                        weightEditText.setText("B.W.");
                        pounds.setVisibility(View.GONE);
                        weightEditText.setEnabled(false);
                    } else if(message.equals("defaultWeight")){
                        if(!isNumber(weightEditText.getText().toString())){
                            weightEditText.setText("");
                            weightEditText.setEnabled(true);
                            weightEditText.setHint("W");
                            pounds.setVisibility(View.VISIBLE);
                            weightEditText.setEnabled(true);
                        }
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null && data != null ) {
                    String message = data.getStringExtra("MESSAGE2");
                    if(message.equals("to failure")){
                        repsEditText.setText("T.F.");
                        repsEditText.setEnabled(false);
                    } else if(message.equals("defaultReps")){
                        if(!isNumber(repsEditText.getText().toString())){
                            repsEditText.setText("");
                            repsEditText.setEnabled(true);
                            repsEditText.setHint("R");
                        }
                    }
                }
            }
        }
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

    public String getInfoList(){
        String setSchemeString = "";
        String setsString = setsEditText.getText().toString();
        String repsString = repsEditText.getText().toString();
        String weightString = weightEditText.getText().toString();
        if (!setsString.equals("") && !repsString.equals("") && !weightString.equals("")) {
            setSchemeString = setsString + "x" + repsString + "@" +
                    weightString;
        }
        return setSchemeString;
    }

}
