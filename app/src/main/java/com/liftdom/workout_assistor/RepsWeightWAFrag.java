package com.liftdom.workout_assistor;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.*;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;
import com.liftdom.template_editor.ExtraOptionsDialog;
import com.liftdom.template_editor.SetsLevelChildFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepsWeightWAFrag extends android.app.Fragment {


    public RepsWeightWAFrag() {
        // Required empty public constructor
    }

    String repsWeightString = "false";
    String fragTag1;
    boolean isEdit = false;


    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private removeFragCallback removeFrag;

    // Butterknife
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.unit) TextView unitView;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;
    @BindView(R.id.holderView) LinearLayout holderView;
    @BindView(R.id.checkBox) CheckBox checkBox;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reps_weight_wa, container, false);

        ButterKnife.bind(this, view);

        removeFrag = (removeFragCallback) getParentFragment();

        if(isEdit){
            String delims = "[@,_]";
            String[] tokens = repsWeightString.split(delims);

            repsEditText.setText(tokens[0]);
            weightEditText.setText(tokens[1]);
            if(tokens[2].equals("checked")){
                checkBox.setChecked(true);
            }else{
                checkBox.setChecked(false);
            }
        }else{
            String[] tokens = repsWeightString.split("@");

            repsEditText.setText(tokens[0]);
            weightEditText.setText(tokens[1]);
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    holderView.setBackgroundColor(Color.parseColor("#cccccc"));
                }else{
                    holderView.setBackgroundColor(Color.parseColor("#FFFFFF"));
                }
            }
        });

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                removeFrag.removeFrag(fragTag1);
            }
        });

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

    public String getInfo(){
        //TODO: Just like a lot of things, we'll have to adjust for different units later
        String info = repsEditText.getText().toString() + "@" + weightEditText.getText().toString();
        if(checkBox.isChecked()){
            info = info + "_checked";
        }else{
            info = info + "_unchecked";
        }
        return info;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(requestCode == 3){
            if(data != null){
                if(data.getStringExtra("MESSAGE1") != null) {
                    String message = data.getStringExtra("MESSAGE1");
                    if(message.equals("bodyweight")){
                        weightEditText.setText("B.W.");
                        unitView.setVisibility(View.GONE);
                        weightEditText.setEnabled(false);
                    } else if(message.equals("defaultWeight")){
                        if(!isNumber(weightEditText.getText().toString())){
                            weightEditText.setText("");
                            weightEditText.setEnabled(true);
                            weightEditText.setHint("W");
                            unitView.setVisibility(View.VISIBLE);
                            weightEditText.setEnabled(true);
                        }
                    }
                }
                if(data.getStringExtra("MESSAGE2") != null) {
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

}
