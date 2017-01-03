package com.liftdom.template_editor;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.liftdom.liftdom.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SetsLevelChildFrag extends android.app.Fragment {

    // Butterknife
    @BindView(R.id.sets) EditText setsEditText;
    @BindView(R.id.reps) EditText repsEditText;
    @BindView(R.id.weight) EditText weightEditText;
    //@BindView(R.id.extraOptions) Spinner extraOptions;
    @BindView(R.id.lbs) TextView pounds;
    @BindView(R.id.extraOptionsButton) ImageView extraOptionsButton;

    public SetsLevelChildFrag() {
        // Required empty public constructor
    }

    Boolean isEdit = false;
    String setSchemeEdited;
    Boolean isEditFirstEdit = false;


    // Callback
    public interface setSchemesCallback{
        String getExerciseValue();
        String getDoWValue();
        Boolean getCheckBoxValue();
    }

    private setSchemesCallback callback;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sets_level_child, container, false);

        ButterKnife.bind(this, view);

        callback = (setSchemesCallback) getParentFragment();

        //ArrayList<String> optionsAL = new ArrayList<>();
        //optionsAL.add("Weights");
        //optionsAL.add("Body-weight");
        //optionsAL.add("To failure (reps)");
        //optionsAL.add("Back to normal reps");
//
        //ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item,
        //        optionsAL);
//
        //extraOptions.setAdapter(dataAdapter);
        //extraOptions.setOnItemSelectedListener(new YourItemSelectedListener());
        //extraOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {
        //    @Override
        //    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //        parent.getItemAtPosition(position);
        //        if(position == 0){
        //            weightEditText.setText("W");
        //        }else if(position == 1){
        //            weightEditText.setText("B.W.");
        //        }
        //    }
        //});

        String delims = "[x,@]";

        if(isEdit){
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

            //int weightInt = Integer.parseInt(weightWithoutSpaces);

            // sets
            //String setsWithSpaces = setSchemesEachArray[0];
            //String setsWithoutSpaces = setsWithSpaces.replaceAll("\\s+","");
            ////int setsInt = Integer.parseInt(setsWithoutSpaces);
            //setsEditText.setText(setsWithoutSpaces);
//
            //// reps
            //String repsWithSpaces = setSchemesEachArray[1];
            //if(!setSchemesEachArray[2].equals(" failure")){
            //    String repsWithout = repsWithSpaces.replaceAll("\\s+","");
            //    repsEditText.setText(repsWithout);
            //} else if(setSchemesEachArray[2].equals(" failure")){
            //    repsEditText.setText(setSchemesEachArray[2]);
            //}
            //String repsWithoutSpaces = repsWithSpaces.replaceAll("\\s+","");
            ////int repsInt = Integer.parseInt(repsWithoutSpaces);
            //repsEditText.setText(repsWithoutSpaces);
//
            //String weightWithSpaces = setSchemesEachArray[2];
            //if(!setSchemesEachArray[2].equals(" B.W.")){
            //    String weightWithoutSpaces = weightWithSpaces.replaceAll("\\s+","");
            //    weightEditText.setText(weightWithoutSpaces);
            //} else if(setSchemesEachArray[2].equals(" B.W.")){
            //    weightEditText.setText(setSchemesEachArray[2]);
            //}
            ////int weightInt = Integer.parseInt(weightWithoutSpaces);
        }


        extraOptionsButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), ExtraOptionsActivity.class);
                //int exID = exerciseButton.getId();
                //intent.putExtra("exID", exID);
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
        if(requestCode == 3)
        {
            if(data.getStringExtra("MESSAGE") != null && data != null ) {
                String message = data.getStringExtra("MESSAGE");
                if(message.equals("bodyweight")){
                    weightEditText.setText("B.W.");
                    pounds.setVisibility(View.GONE);
                    weightEditText.setEnabled(false);
                } else if(message.equals("to failure")){
                    repsEditText.setText("T.F.");
                    pounds.setVisibility(View.VISIBLE);
                    repsEditText.setEnabled(false);
                } else if(message.equals("defaultWeight")){
                    weightEditText.setText("");
                    weightEditText.setEnabled(true);
                    weightEditText.setHint("W");
                    weightEditText.setEnabled(true);
                } else if(message.equals("defaultReps")){
                    repsEditText.setText("");
                    repsEditText.setEnabled(true);
                    repsEditText.setHint("R");
                }
            }
        }
    }

    //public class YourItemSelectedListener implements AdapterView.OnItemSelectedListener {
//
    //    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//
    //            if (pos == 0) {
    //                weightEditText.setHint("W");
    //                if(weightEditText.getText().toString().equals("B.W.")){
    //                    weightEditText.setText("");
    //                }
    //                pounds.setVisibility(View.VISIBLE);
    //                weightEditText.setEnabled(true);
    //            } else if (pos == 1) {
    //                weightEditText.setText("B.W.");
    //                pounds.setVisibility(View.GONE);
    //                weightEditText.setEnabled(false);
    //            } else if (pos == 2){
    //                repsEditText.setText("failure");
    //                repsEditText.setEnabled(false);
    //            } else if (pos == 3){
    //                if(repsEditText.getText().toString().equals("failure")){
    //                    repsEditText.setText("");
    //                }
    //                weightEditText.setHint("R");
    //                weightEditText.setEnabled(true);
    //            }
//
    //    }
//
    //    public void onNothingSelected(AdapterView parent) {
    //        // Do nothing.
    //    }
    //}




    @Override
    public void onPause(){
        super.onPause();

        if(!callback.getExerciseValue().equals("Click to add exercise")) {
            String parentSpinnerValue = callback.getExerciseValue();
            String doWSelected = callback.getDoWValue();
            Boolean isChecked = callback.getCheckBoxValue();
            EditTemplateAssemblerClass.getInstance().setDoW(doWSelected);
            EditTemplateAssemblerClass.getInstance().setExerciseValue(parentSpinnerValue, doWSelected, isChecked);

            String setsString = setsEditText.getText().toString();
            String repsString = repsEditText.getText().toString();
            String weightString = weightEditText.getText().toString();

            String value = null;

            if (!setsString.equals("") && !repsString.equals("") && !weightString.equals("")) {
                value = setsString + " x " + repsString + " @ " +
                        weightString;
            }

            if (value != null && EditTemplateAssemblerClass.getInstance().isOnSaveClick) {
                EditTemplateAssemblerClass.getInstance().setSetSchemeValue(value, parentSpinnerValue, doWSelected);
            }
        }

    }




}
