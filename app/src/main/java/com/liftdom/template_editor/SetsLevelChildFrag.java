package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelChildFrag extends android.app.Fragment {


    public SetsLevelChildFrag() {
        // Required empty public constructor
    }

    Boolean isEdit = false;
    String setSchemeEdited;
    Boolean isEditFirstEdit = false;

    // Callback
    public interface setSchemesCallback{
        String getExerciseValueFormatted();
        String getDoWValue();
    }

    public interface removeFragCallback{
        void removeFrag(String fragTag);
    }

    private setSchemesCallback callback;
    private removeFragCallback fragCallback;

    public String fragTag;

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    @BindView(R.id.lbs) TextView pounds;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;
    @BindView(R.id.destroyFrag1) ImageButton destroyFrag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();

        String delims = "[x,@]";

        if(isEdit){

            if(setSchemeEdited.equals("")){
                setsEditText.setText("");
                repsEditText.setText("");
                weightEditText.setText("");
            }else{
                String[] setSchemesEachArray = setSchemeEdited.split(delims);

                // sets
                String setsWithSpaces = setSchemesEachArray[0];
                String setsWithoutSpaces = setsWithSpaces.replaceAll("\\s+","");
                //int setsInt = Integer.parseInt(setsWithoutSpaces);
                setsEditText.setText(setsWithoutSpaces);

                // reps
                String repsWithSpaces = setSchemesEachArray[1];
                String repsWithout = repsWithSpaces.replaceAll("\\s+","");
                repsEditText.setText(repsWithout);

                String weightWithSpaces = setSchemesEachArray[2];
                String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
                weightEditText.setText(weightWithoutSpaces);
            }

        }

        fragCallback = (removeFragCallback) getParentFragment();

        destroyFrag.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fragCallback.removeFrag(fragTag);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        // check if the request code is same as what is passed  here it is 3
        if(requestCode == 3)
        {
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

    public String getSetSchemeString(){
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
