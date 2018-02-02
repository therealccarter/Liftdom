package com.liftdom.charts_stats_tools.tools;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;
import com.liftdom.liftdom.R;
import com.liftdom.user_profile.UserModelClass;

/**
 * A simple {@link Fragment} subclass.
 */
public class BMICalcFrag extends Fragment {


    public BMICalcFrag() {
        // Required empty public constructor
    }

    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference();
    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

    //TODO: Just a thought, but it'd probably be more addictive if things like this run once upon creation

    @BindView(R.id.calculateButton) Button calculateButton;
    @BindView(R.id.ageEditText) EditText ageEdit;
    @BindView(R.id.maleRadioButton) RadioButton maleRadioButton;
    @BindView(R.id.femaleRadioButton) RadioButton femaleRadioButton;
    @BindView(R.id.heightFeet) EditText heightFeetEdit;
    @BindView(R.id.heightInches) EditText heightInchesEdit;
    @BindView(R.id.resultView) TextView resultsView;
    @BindView(R.id.summaryView) TextView summaryView;
    @BindView(R.id.heightCm) EditText heightCmEdit;
    @BindView(R.id.cmTextView) TextView cmTextView;
    @BindView(R.id.weightEditText) EditText weightEdit;
    @BindView(R.id.feetTextView) TextView feetView;
    @BindView(R.id.inchesTextView) TextView inchesView;
    @BindView(R.id.weightUnit) TextView weightUnit;

    boolean isImperial;
    double bodyWeight = 0;
    String height;
    int age = 0;
    String sex = "null";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmicalc, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference settingsRef = mRootRef.child("user").child(uid);

        if(savedInstanceState == null){

            settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    UserModelClass userModelClass = dataSnapshot.getValue(UserModelClass.class);
                    if(userModelClass.isIsImperial()){
                        // is imperial
                        isImperial = true;
                        String[] heightTokens = userModelClass.getFeetInchesHeight().split("_");
                        heightFeetEdit.setText(heightTokens[0]);
                        heightInchesEdit.setText(heightTokens[1]);
                        weightEdit.setText(userModelClass.getPounds());
                        heightInchesEdit.setVisibility(View.VISIBLE);
                        heightFeetEdit.setVisibility(View.VISIBLE);
                        feetView.setVisibility(View.VISIBLE);
                        inchesView.setVisibility(View.VISIBLE);
                    }else{
                        // is metric
                        isImperial = false;
                        heightInchesEdit.setVisibility(View.GONE);
                        heightFeetEdit.setVisibility(View.GONE);
                        feetView.setVisibility(View.GONE);
                        inchesView.setVisibility(View.GONE);
                        weightUnit.setText("kg");

                        cmTextView.setVisibility(View.VISIBLE);
                        heightCmEdit.setVisibility(View.VISIBLE);
                        heightCmEdit.setText(userModelClass.getCmHeight());
                        weightEdit.setText(userModelClass.getKgs());
                    }

                    ageEdit.setText(userModelClass.getAge());
                    age = Integer.parseInt(userModelClass.getAge());


                    if(userModelClass.getSex().equals("male")){
                        maleRadioButton.setChecked(true);
                        femaleRadioButton.setChecked(false);
                    }else{
                        maleRadioButton.setChecked(false);
                        femaleRadioButton.setChecked(true);
                    }



                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isEmptyString = false;

                boolean isMale = false;
                if(sex.equals("male")){
                    isMale = true;
                }

                double heightCm = 0;
                if(isImperial){
                    String heightFeet = heightFeetEdit.getText().toString();
                    String heightInches = heightInchesEdit.getText().toString();
                    if(!heightFeet.isEmpty() && !heightInches.isEmpty()){
                        String newHeight = heightFeet + "_" + heightInches;
                        heightCm = heightConvertToMet(newHeight);
                    }else{
                        isEmptyString = true;
                    }
                }else{
                    String heightCmString = heightCmEdit.getText().toString();
                    if(!heightCmString.isEmpty()){
                        heightCm = Double.parseDouble(heightCmString);
                    }else{
                        isEmptyString = true;
                    }
                }

                double weightKg = 0;
                String weightString = weightEdit.getText().toString();
                if(!weightString.isEmpty()){
                    bodyWeight = Double.parseDouble(weightString);
                    if(isImperial){
                        weightKg = weightConvertToMet(bodyWeight);
                    }else{
                        weightKg = bodyWeight;
                    }
                }else{
                    isEmptyString = true;
                }


                if(!isEmptyString){
                    BMICalculatorClass calcClass = new BMICalculatorClass(weightKg, heightCm);

                    double BMI = calcClass.getBMI();

                    String bmiString = String.valueOf(BMI);

                    resultsView.setText(bmiString);
                }

                //TODO: In summary view, tell them what it means concerning their age and sex
                // call a method here that returns an appropriate string.

            }
        });

        return view;
    }

    public double weightConvertToMet(double weight){
        double kgs = weight * 0.453592;
        return kgs;
    }

    public double heightConvertToMet(String heightInches){
        String delims = "[_]";
        String[] tokens = heightInches.split(delims);
        int inches = (Integer.parseInt(tokens[0]) * 12) + Integer.parseInt(tokens[1]);

        double cm = (double) inches * 2.54;
        return cm;
    }

}
