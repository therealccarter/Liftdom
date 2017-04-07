package com.liftdom.tools;


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
    @BindView(R.id.weightEditText) EditText bodyWeightEdit;
    @BindView(R.id.resultView) TextView resultsView;
    @BindView(R.id.summaryView) TextView summaryView;

    String bodyWeightUnit = "null";
    double bodyWeight = 0;
    String heightUnit = "null";
    String height;
    int age = 0;
    String sex = "null";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bmicalc, container, false);

        ButterKnife.bind(this, view);

        DatabaseReference settingsRef = mRootRef.child("users").child(uid);

        if(savedInstanceState == null){
            settingsRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    int inc = 0;

                    for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        String key = dataSnapshot1.getKey();
                        if(!key.equals("maxes")){
                            if(key.equals("bodyWeightUnit")){
                                bodyWeightUnit = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("bodyweight")){
                                bodyWeight = (double) Integer.parseInt(dataSnapshot1.getValue(String.class));
                            }else if(key.equals("heightUnit")){
                                heightUnit = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("height")){
                                height = dataSnapshot1.getValue(String.class);
                            }else if(key.equals("age")){
                                age = Integer.parseInt(dataSnapshot1.getValue(String.class));
                            }else if(key.equals("sex")){
                                sex = dataSnapshot1.getValue(String.class);
                            }
                        }
                        inc++;

                        if(inc == dataSnapshot.getChildrenCount()){
                            //TODO change units
                            bodyWeightEdit.setText(String.valueOf(bodyWeight));
                            if(heightUnit.equals("footInches")){
                                String delims = "[_]";
                                String[] tokens = height.split(delims);
                                heightFeetEdit.setText(tokens[0]);
                                heightInchesEdit.setText(tokens[1]);
                            }else{
                                //TODO use kilos?
                            }
                            ageEdit.setText(String.valueOf(age));

                            if(sex.equals("male")){
                                maleRadioButton.setChecked(true);
                            }else if(sex.equals("female")){
                                femaleRadioButton.setChecked(true);
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }

        calculateButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                boolean isMale = false;
                if(sex.equals("male")){
                    isMale = true;
                }

                double heightCm;
                if(heightUnit.equals("footInches")){
                    heightCm = heightConvertToMet(height);
                }else{
                    heightCm = (double) Integer.parseInt(height);
                }

                double weightKg;
                if(bodyWeightUnit.equals("pounds")){
                    weightKg = weightConvertToMet(bodyWeight);
                }else{
                    weightKg = bodyWeight;
                }

                BMICalculatorClass calcClass = new BMICalculatorClass(weightKg, heightCm);

                double BMI = calcClass.getBMI();

                resultsView.setText(Double.toString(BMI));

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
        String[] tokens = height.split(delims);
        int inches = (Integer.parseInt(tokens[0]) * 12) + Integer.parseInt(tokens[1]);

        double cm = (double) inches * 2.54;
        return cm;
    }

}
