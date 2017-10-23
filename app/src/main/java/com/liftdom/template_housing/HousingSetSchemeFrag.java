package com.liftdom.template_housing;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.TypedValue;
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
public class HousingSetSchemeFrag extends Fragment {


    public HousingSetSchemeFrag() {
        // Required empty public constructor
    }

    public String setSchemeString = "error";
    boolean differentType = false;
    public boolean isSmallerText;
    boolean isTemplateImperial;
    boolean isCurrentUserImperial;

    @BindView(R.id.setSchemeString) TextView setSchemesView;
    @BindView(R.id.pounds) TextView pounds;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_housing_set_scheme, container, false);

        ButterKnife.bind(this, view);

        if(savedInstanceState != null){
            setSchemeString = savedInstanceState.getString("set_scheme_string");
            setSchemesView.setText(handleUnitConversion(setSchemeString));
        }

        if(differentType){
            pounds.setVisibility(View.GONE);
        }

        if(isSmallerText){
            setSchemesView.setTextSize(TypedValue.COMPLEX_UNIT_PX, getContext().getResources().getDimension(R.dimen
                    .sixteen_sp));
        }

         if(isCurrentUserImperial){
             pounds.setText("lbs");
         }else{
             pounds.setText("kgs");
         }

        setSchemesView.setText(handleUnitConversion(setSchemeString));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        savedInstanceState.putString("set_scheme_string", setSchemeString);

        super.onSaveInstanceState(savedInstanceState);
    }

    private String handleUnitConversion(String oldValue){
        String newString;

        String delims = "[@]";
        String[] tokens = oldValue.split(delims);

        if(!tokens[1].equals("B.W.")){
            String newValue;
            if(isTemplateImperial && !isCurrentUserImperial){
                // the template is imperial, but the user is metric
                double valueDouble = Double.parseDouble(tokens[1]);
                int valueInt = (int) Math.round(valueDouble * 0.45359237);
                newValue = String.valueOf(valueInt);
                newString = tokens[0] + "@" + newValue;
            }else if(!isTemplateImperial && isCurrentUserImperial){
                // the template is metric, but the user is imperial
                double valueDouble = Double.parseDouble(tokens[1]);
                int valueInt = (int) Math.round(valueDouble / 0.45359237);
                newValue = String.valueOf(valueInt);
                newString = tokens[0] + "@" + newValue;
            }else{
                newString = oldValue;
            }
        }else{
            newString = oldValue;
        }

        return newString;
    }

}
